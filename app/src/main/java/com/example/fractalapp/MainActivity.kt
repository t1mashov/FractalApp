package com.example.fractalapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fractalapp.fractal.FractalViewModel
import com.example.fractalapp.fractal.ui.FractalScreen
import com.example.fractalapp.home.FractalLikedListWidgetViewModel
import com.example.fractalapp.home.FractalSamplesListWidgetViewModel
import com.example.fractalapp.home.HomeViewModel
import com.example.fractalapp.home.ui.HomeScreen
import com.example.fractalapp.rulestext.RulesText
import com.example.fractalapp.saves.SavesViewModel
import com.example.fractalapp.saves.ui.SavesScreen
import com.example.fractalapp.ui.theme.FractalAppTheme
import com.example.fractalapp.ui.theme.FractalTheme
import java.io.IOException

class MainActivity : ComponentActivity(), AppFractal {

    private var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {result ->
        if (result) bmpSave?.let { trySaveFractal(it) }
    }



    private var bmpSave: Bitmap? = null


    private lateinit var fractalViewModel: FractalViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var savesViewModel: SavesViewModel
    private lateinit var fractalSamplesViewModel: FractalSamplesListWidgetViewModel
    private lateinit var fractalLikedViewModel: FractalLikedListWidgetViewModel

    private lateinit var sharedPreferences: SharedPreferences

    private val requiredPermissions = listOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).sampleDatabase.setup(this)

        fractalViewModel = FractalViewModel(
            (application as App).repository
        )
        fractalSamplesViewModel = FractalSamplesListWidgetViewModel(
            (application as App).repository
        )
        fractalLikedViewModel = FractalLikedListWidgetViewModel(
            (application as App).repository
        )

        homeViewModel = HomeViewModel(
            (application as App).repository,
            fractalSamplesViewModel
        )
        savesViewModel = SavesViewModel(
            (application as App).repository,
            fractalLikedViewModel
        )

        sharedPreferences = getSharedPreferences(SHARED_PREFS_FRACTAL, Context.MODE_PRIVATE)

        setContent {

            val navController = rememberNavController()

            FractalAppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NavigationComponent(
                        app = this,
                        navController = navController
                    )

                }
            }
        }

    }



    override fun trySaveFractal(bmp: Bitmap?) {
        bmpSave = bmp

        val saveExec = {
            bmp?.let {
                val name = "${System.currentTimeMillis()}"
                val res = saveFractalToExternalStorage(name, it)
                if (res) Toast.makeText(this, "Изображение было сохранено", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, "Предоставте разрешение", Toast.LENGTH_SHORT).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, requiredPermissions[0]
                ) == PackageManager.PERMISSION_GRANTED) {
                saveExec()
            }
            else requestPermissionLauncher.launch(requiredPermissions[0])
        }
        else {
            if (ContextCompat.checkSelfPermission(
                    this, requiredPermissions[1]
                ) == PackageManager.PERMISSION_GRANTED) {
                saveExec()
            }
            else requestPermissionLauncher.launch(requiredPermissions[1])

        }

    }


    private fun saveFractalToExternalStorage(displayName: String, bmp: Bitmap): Boolean {

        val bmpRes = Bitmap.createBitmap(bmp.width, bmp.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmpRes)
        val paint = Paint().apply {
            color = Color.Black.toArgb()
        }
        canvas.drawRect(0f, 0f, bmp.width.toFloat(), bmp.height.toFloat(), paint)
        canvas.drawBitmap(bmp, 0f, 0f, null)

        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.WIDTH, bmpRes.width)
            put(MediaStore.Images.Media.HEIGHT, bmpRes.height)
        }

        return try {
            contentResolver.insert(imageCollection, contentValues)?.also {uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmpRes.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)) {
                        throw IOException("Couldn't save a bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    override fun onStop() {
        super.onStop()
        fractalViewModel.fractalImage.value = null
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun NavigationComponent(
        app: AppFractal,
        navController: NavHostController
    ) {

        NavHost(
            navController = navController,
            startDestination = SCREEN_HOME
        ) {
            composable(SCREEN_HOME) {
                HomeScreen(
                    vm = homeViewModel,
                    navController = navController
                )
            }
            composable("$SCREEN_FRACTAL_BUILDER/{id}/{sid}") {
                val fractalId = it.arguments?.getString("id", "-1")?.toInt()
                val savedFractalId = it.arguments?.getString("sid", "-1")?.toInt()

                println("MainActivity >> id=$fractalId, sid=$savedFractalId")

                if (fractalId == -1 && savedFractalId == -1) {
                    fractalViewModel.clearSettings()
                    fractalViewModel.fractalImage.value =
                        Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
                }


                fractalViewModel.id = fractalId
                fractalViewModel.sid = savedFractalId


                FractalScreen(vm = fractalViewModel, app = app)
            }
            composable(SCREEN_SAVED) {
                SavesScreen(vm = savesViewModel, navHostController = navController)
            }
            composable(SCREEN_ABOUT_L_SYSTEM) {
                println("MainActivity >> NavigationComponent() >> SCREEN_ABOUT_L_SYSTEM")
                RulesText()
            }
        }
    }

}



