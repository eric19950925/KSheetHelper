package com.eric.ksheethelper
//import android.Manifest
//import android.content.ContentValues.TAG
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import com.google.api.client.json.JsonFactory
//import com.google.api.client.auth.oauth2.Credential
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.util.store.FileDataStoreFactory
//import com.google.api.services.sheets.v4.Sheets
//import com.google.api.services.sheets.v4.SheetsScopes
import android.os.Bundle
//import android.os.Environment
//import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import com.google.api.client.json.jackson2.JacksonFactory
//import com.google.api.client.json.jackson2.JacksonFactory
//import java.io.FileNotFoundException
//import java.io.InputStreamReader
//import java.io.File.separator
//import android.os.Environment.getExternalStorageDirectory
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
//import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver
//import java.io.File
//import java.io.IOException


class AddSheetFragment:FragmentBase() {
//        val murl = "https://docs.google.com/spreadsheets/d/1j4El6Q92vAUoH_yiZBTxXQNCO2Ay3BC7zPAHcX0Kp78/edit#gid=1830286630"

    val WRITE_EXTERNAL_STORAGE_CODE=101
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_addsheet
    }
}