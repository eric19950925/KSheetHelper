package com.eric.ksheethelper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import kotlinx.android.synthetic.main.custom_toast_container.*
import kotlinx.android.synthetic.main.custom_toast_container.view.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {
    var TAG = MainActivity::class.java.simpleName
    val duration = Toast.LENGTH_SHORT
    val APPLICATION_NAME = "Google Sheets API KSheetHelper"
    val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
    val TOKENS_DIRECTORY_PATH = "tokens"
//    val SCOPES = listOf(SheetsScopes.SPREADSHEETS_READONLY)
    val SCOPES : List<String> = Arrays.asList("https://spreadsheets.google.com/feeds")
    val CREDENTIALS_FILE_PATH = "/credentials.json"
    val HTTP_TRANSPORT = com.google.api.client.http.javanet.NetHttpTransport()
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101


    val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
    val spreadsheetId_moo = "1j4El6Q92vAUoH_yiZBTxXQNCO2Ay3BC7zPAHcX0Kp78"
    val range = "Class Data!A2:E"
    val range_moo = "Class Data!B8:C"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn_LogIn = findViewById<Button>(R.id.btn_login)
        permission_write()
        val v = layoutInflater.inflate(R.layout.custom_toast_container, custom_toast_container, false)
        btn_LogIn.setOnClickListener(View.OnClickListener {
//            var intent = Intent(this,FirstKActivity::class.java)
//            startActivity(intent)
           if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){

                showSheetData()
               v.text_view.text = "成功取得資料，\n尚未制作呈現頁面"
               with(Toast(this)) {
                   duration = Toast.LENGTH_LONG
                   view = v
                   show()
               }
           }else{
               v.text_view.text = "沒有讀檔權限，無法驗證資料，\n請手動開啟權限"
               with(Toast(this)) {
                   duration = Toast.LENGTH_LONG
                   view = v
                   show()
               }
//                val toast = Toast.makeText(this,"沒有讀檔權限，無法驗證資料，\n請手動開啟權限", duration)
//                toast.show()

            }
        })


    }

    private fun showSheetData() {
        Thread(Runnable {
            val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build()
            val result = service.spreadsheets().values().get(spreadsheetId, range).execute()
            //            val numRows = if (result.getValues() != null) result.getValues().size else 0


            var values = result.getValues()

            showresult(values)
        }).start()

    }

    private fun permission_write() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            // Permission has already been granted
            Log.d(TAG,"permission ok")

        }
    }

    private fun showresult(values: MutableList<MutableList<Any>>) {

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.")
        } else {
//            System.out.println("Name, Major")
            Log.d(TAG,"Name, Major")
            for ( row  in values) {
                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row.get(0), row.get(4))
                Log.d(TAG,row.get(0).toString()+" , "+ row.get(4).toString()+"\n")

            }
        }

    }

    private fun getCredentials(httpTransport: NetHttpTransport): Credential {
        var esd : String? = Environment.getExternalStorageDirectory().toString()
        val tokenFolder = File(esd+ File.separator + TOKENS_DIRECTORY_PATH)
        if (!tokenFolder.exists()) {
            tokenFolder.mkdirs()
        }

        //get c file
        val in_ = FirstKActivity::class.java!!.getResourceAsStream(CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found 0113: $CREDENTIALS_FILE_PATH")
        //將c file 包入Credential物件
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(in_))
        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(tokenFolder))
            .setAccessType("offline")
            .build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()//可有可無

        val browser = AuthorizationCodeInstalledApp.Browser {
            if (it != null) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(it)
                startActivity(intent)
            }
        }
        var  ab =AuthorizationCodeInstalledApp(
                flow, receiver, browser).authorize("user")

        return ab
    }
}
