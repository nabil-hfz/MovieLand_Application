package com.example.movieland.utilities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller


object Utility {

    const val INITIALIZED = "INITIALIZED"


    /**
     * @param REQUEST_CODE_RECOVER_PLAY_SERVICES  for checking if google play services existed.
     */
    private const val REQUEST_CODE_RECOVER_PLAY_SERVICES: Int = 212
    /**
     * @param LOCATION_PERMISSIONS_REQUEST for requesting a location permission.
     */
    private const val LOCATION_PERMISSIONS_REQUEST: Int = 313

    /**
     *  Public helper method which can check if there is need to install an SSL layer [TLS] for
     *  secure internet connection through Google play Services.
     */
    fun checkTls(context: Context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                ProviderInstaller.installIfNeeded(context)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }

        }
    }

    /**
     *  Public helper method returns a boolean which can identify if [Google_play_services_App] exists or not.
     */
    private fun checkGooglePlayServices(context: Context): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val checkGooglePlayServices = apiAvailability.isGooglePlayServicesAvailable(context)
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            apiAvailability.getErrorDialog(
                context as Activity, checkGooglePlayServices,
                REQUEST_CODE_RECOVER_PLAY_SERVICES
            ).show()
            return false
        }
        return true
    }

    /**
     *  Public helper method returns a boolean which can identify if there is internet connection or not.
     */
    fun getNetworkAvailability(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

}