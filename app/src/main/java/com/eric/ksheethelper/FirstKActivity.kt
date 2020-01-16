package com.eric.ksheethelper

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import io.ktor.application.Application
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.cio.websocket.FrameType.Companion.get
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.apache.http.client.HttpClient
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list


class FirstKActivity : AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var img_user : ImageView
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101
    val APPLICATION_NAME = "Google Sheets API Java Quickstart"
    val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
    val TOKENS_DIRECTORY_PATH = "tokens"
    val SCOPES = listOf(SheetsScopes.SPREADSHEETS_READONLY)
    val CREDENTIALS_FILE_PATH = "/credentials.json"
    val HTTP_TRANSPORT = com.google.api.client.http.javanet.NetHttpTransport()
    val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
    val range = "Class Data!A2:E"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firstk_act)
        initNavi()
        permission_write()
        startServer()


//        Toast.makeText(this, response, Toast.LENGTH_LONG).show()

//        img_user = findViewById<ImageView>(R.id.user_image)
//
//        Thread(Runnable {
//            val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build()
//            val result = service.spreadsheets().values().get(spreadsheetId, range).execute()
//            val numRows = if (result.getValues() != null) result.getValues().size else 0
//            System.out.printf("%d rows retrieved.", numRows)
//            Log.d(ContentValues.TAG,"0113 : "+ numRows.toString())
//
//        }).start()

    }

    private fun startServer() {
        val server = embeddedServer(Netty, port = 8080) {
            routing {
                get("/") {
                    call.respondText("OoHaiYoo SaKai!", ContentType.Text.Plain)
                }
                get("/demo") {
                    call.respondText("HELLO WORLD!")
                }
            }
        }
        server.start()
    }

    //    private fun getCredentials(httpTransport: NetHttpTransport): Credential {
//        var esd : String? = Environment.getExternalStorageDirectory().toString()
//        val tokenFolder = File(esd+ File.separator + TOKENS_DIRECTORY_PATH)
//        if (!tokenFolder.exists()) {
//            tokenFolder.mkdirs()
//        }
//        val in_ = FirstKActivity::class.java!!.getResourceAsStream(CREDENTIALS_FILE_PATH)
//            ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
//        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(in_))
//        val flow = GoogleAuthorizationCodeFlow.Builder(
//            httpTransport, JSON_FACTORY, clientSecrets, SCOPES
//        )
//            .setDataStoreFactory(FileDataStoreFactory(tokenFolder))
//            .setAccessType("offline")
//            .build()
//        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
//
//        var  ab = AuthorizationCodeInstalledApp(flow, receiver){
//            fun onAuthorization( authorizationUrl: AuthorizationCodeRequestUrl){
//                var url = (authorizationUrl.build())
//                var browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                this.startActivity(browserIntent)
//            }
//        }
//        return ab.authorize("user")
//    }
    fun Application.main(){
        routing {
            get("/") {
                call.respond("hello world")
            }
            get("talks"){

            }
        }
//        val talks = listOf(
//            @Serializable
//            AgendaEntry(
//                id = 0,
//                title = "This is the first talk",
//                date = "28/09/2019",
//                startTime = "09:00:00",
//                endTime = "10:00:00",
//                description = "Description for something very interesting, we hope.",
//                speaker = "TBC"
//            )
//        )
}

//    fun AgendaEntry.toJsonString(): String =
//        Json.stringify(AgendaEntry.serializer(), this)
//
//    fun List<AgendaEntry>.toJsonString(): String =
//        Json.stringify(AgendaEntry.serializer().list, this)



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
        }
    }

    private fun initNavi() {
        setSupportActionBar(findViewById(R.id.toolbar))
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
            .apply {
                setStatusBarBackground(R.color.colorPrimaryDark)
            }
        //init navController and appBarConfiguration that change nav_host_fragment.
        //nav_host_fragment is in firstk_act.xml
        //child fragments are in nav_host_fragment(nav_graph.xml)
        val navController: NavController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.addsheet_fragment_dd, R.id.profile_fragment)
                .setDrawerLayout(drawerLayout)
                .build()
        //connect NavigationView and nav_host_fragment
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

    }

    //make NavigationView can control nav_host_fragment to show
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}