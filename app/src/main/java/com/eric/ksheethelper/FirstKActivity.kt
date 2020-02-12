package com.eric.ksheethelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.eric.ksheethelper.Base.MomActivity
import com.google.android.material.navigation.NavigationView
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
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*
import kotlinx.android.synthetic.main.custom_toast_container.*
import kotlinx.android.synthetic.main.custom_toast_container.view.*


class FirstKActivity : MomActivity(){
    override fun customToast(s: String,c:Context,img:Int) {
        val v = layoutInflater.inflate(img, custom_toast_container, false)
        v.text_view.text = s
        with(Toast(c)) {
            duration = Toast.LENGTH_LONG
            view = v
            show()
        }
    }

    open override fun getUrl(): String {
        return mSheetUrl
    }
    val TAG=FirstKActivity::class.java.simpleName
    var msheetUrl:String=""
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
        val intent_add = getIntent()
        mSheetUrl = intent_add.getStringExtra("sheet_Url")
        Log.d(TAG,"0117 after jump url : "+msheetUrl)
        setUrl(mSheetUrl)
        initNavi()
//        startServer()


    }

//    override fun onBackPressed() {
//
//        AlertDialog.Builder(this@FirstKActivity).setTitle(R.string.enter_user_name)
//            .setMessage("退出程式?")
//            .setPositiveButton("確認") { dialogInterface, i ->
//                finish()
//                System.exit(0)
//            }.setNeutralButton("取消", null).show()
//
////        super.onBackPressed()
//    }

//    private fun startServer() {
//        val server = embeddedServer(Netty, port = 8080) {
//            routing {
//                get("/") {
//                    call.respondText("OoHaiYoo SaKai!", ContentType.Text.Plain)
//                }
//                get("/demo") {
//                    call.respondText("HELLO WORLD!")
//                }
//            }
//        }
//        server.start()
//    }
//
//    fun Application.main() {
//        routing {
//            get("/") {
//                call.respond("hello world")
//            }
//            get("talks") {
//
//            }
//        }
//
//    }
//

    fun initNavi() {
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


    fun addFragment(f: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, f)
        transaction.commit()
    }

    fun replaceFragment(f : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, f)
        transaction.commit()
    }
    fun removeFragment(f: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(f)

    }
    open fun postdata(SheetIdName:String, kind:String, region:String, name:String, sex:String, gf:String,
                 insertNum:String) {
        var spreadsheetId_moo = msheetUrl
        val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build()
//        val result = service.spreadsheets().values().get(spreadsheetId_moo, SheetIdName+range_moo_col).execute()
        var body_region = ValueRange().setValues(Arrays.asList(Arrays.asList(region), Arrays.asList(1), Arrays.asList("")))
        var body_name = ValueRange().setValues(Arrays.asList(Arrays.asList(name), Arrays.asList()))
        var body_sex = ValueRange().setValues(Arrays.asList(Arrays.asList(sex), Arrays.asList()))
        var body_gf = ValueRange().setValues(Arrays.asList(Arrays.asList(gf), Arrays.asList()))
        var insertNumPlus7 = (insertNum.toIntOrNull()?.plus(7)).toString()
        if(kind.equals("固定牧養")){
            //insert region
            Log.d(TAG, "0117 range : "+SheetIdName+"!B"+insertNumPlus7+" "+body_region)
            service.spreadsheets().values()
                .update(spreadsheetId_moo, "施懿宸!B9:C", body_region)
                .setValueInputOption("RAW")
                .execute()
            /*
            //insert name
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!C"+insertNumPlus7, body_name)
                .setValueInputOption("RAW")
                .execute()
            //insert sex
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!D"+insertNumPlus7, body_sex)
                .setValueInputOption("RAW")
                .execute()
            //insert gf-1
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!E"+insertNumPlus7, body_gf)
                .setValueInputOption("RAW")
                .execute()
            //insert gf-2
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!F"+insertNumPlus7, body_gf)
                .setValueInputOption("RAW")
                .execute()
        }
        else{
            //insert region
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!I"+insertNumPlus7, body_region)
                .setValueInputOption("RAW")
                .execute()
            //insert name
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!J"+insertNumPlus7, body_name)
                .setValueInputOption("RAW")
                .execute()
            //insert sex
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!K"+insertNumPlus7, body_sex)
                .setValueInputOption("RAW")
                .execute()
            //insert gf-1
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!L"+insertNumPlus7, body_gf)
                .setValueInputOption("RAW")
                .execute()
            //insert gf-2
            service.spreadsheets().values()
                .update(spreadsheetId_moo, SheetIdName+"!M"+insertNumPlus7, body_gf)
                .setValueInputOption("RAW")
                .execute()

             */
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