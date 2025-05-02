package com.example.sitonakameerkat

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import com.example.sitonakameerkat.screen.MainScreen
import com.example.sitonakameerkat.ui.screen.dialog.MessageDialog
import com.example.sitonakameerkat.ui.theme.SitonakaMeerkatTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.MutableStateFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : ComponentActivity() {

    companion object {
        private lateinit var Instance: MainActivity
        suspend fun location(): String {
            return Instance.getCurrentLocation()
        }
    }

    private val message: MutableStateFlow<String> = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Instance = this
        loggerTest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        enableEdgeToEdge()
        setContent {
            SitonakaMeerkatTheme {
                MainScreen()
                MessageDialog(message = message.collectAsState().value) { message.value = "" }
            }
        }
    }

    private lateinit var permissionContinuation: Continuation<Boolean>
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        logger.info("RequestMultiplePermissions {}", permissions)
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                permissionContinuation.resume(true)
            }
            else -> {
                permissionContinuation.resume(false)
            }
        }
    }
    private suspend fun getPermission() = suspendCoroutine { continuation ->
        permissionContinuation = continuation
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private suspend fun getLocationOneShot(lastLocation: Task<Location>) = suspendCoroutine {
        logger.trace("getLocationOneShot")
        logger.trace("addOnSuccessListener")
        lastLocation.addOnSuccessListener { location ->
            logger.trace("OnSuccessListener")
            if (location == null) {
                it.resumeWithException(Exception("location is null"))
            }
            val text = "${location.latitude},${location.longitude}"
            it.resume(text)
        }
        logger.trace("addOnFailureListener")
        lastLocation.addOnFailureListener { exception ->
            logger.trace("OnFailureListener")
            it.resumeWithException(exception)
        }
    }
    private suspend fun getCurrentLocation(): String {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return getLocationOneShot(fusedLocationClient.lastLocation)
        } else {
            val granted = getPermission()
            if (granted) {
                logger.trace("getPermission granted.")
                return getLocationOneShot(fusedLocationClient.lastLocation)
            } else {
                val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (shouldShow) {
                    message.value = "App need ACCESS_FINE_LOCATION"
                }
                throw Exception("Permission denied.")
            }
        }
    }

    private fun loggerTest() {
        logger.trace("Logger TEST")
        logger.debug("Logger TEST")
        logger.info("Logger TEST")
        logger.warn("Logger TEST")
        logger.error("Logger TEST")
    }
}

val logger: Logger by lazy { LoggerFactory.getLogger("SitonakaMeerkat") }
