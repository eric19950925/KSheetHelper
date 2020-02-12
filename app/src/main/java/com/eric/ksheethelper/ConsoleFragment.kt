package com.eric.ksheethelper
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import com.eric.ksheethelper.Base.FragmentBase
import com.eric.ksheethelper.Ext.listenClick
import com.eric.ksheethelper.Main.MainActivity
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
import kotlinx.android.synthetic.main.fragment_console.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*


class ConsoleFragment: FragmentBase() {
    override fun initView() {

    }

    val APPLICATION_NAME = "Google Sheets API KSheetHelper"
    val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
    val TOKENS_DIRECTORY_PATH = "tokens"
    //    val SCOPES = listOf(SheetsScopes.SPREADSHEETS_READONLY)
    val SCOPES : List<String> = Arrays.asList("https://spreadsheets.google.com/feeds")
    val CREDENTIALS_FILE_PATH = "/credentials.json"
    val HTTP_TRANSPORT = com.google.api.client.http.javanet.NetHttpTransport()
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101


    val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
    var spreadsheetId_moo = "1j4El6Q92vAUoH_yiZBTxXQNCO2Ay3BC7zPAHcX0Kp78"
    val range = "Class Data!A2:E"
    val range_moo_user = "施懿宸"
    val range_moo_col = "!B8:F8"
    val firstFragment = SheetSmallTableFragment()
    val secondFragment = NewDataFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = super.onCreateView(inflater, container, savedInstanceState)
        spreadsheetId_moo=(activity as MainActivity)?.getUrl()
        Log.d(TAG,"0117 url : "+spreadsheetId_moo)
//        (activity as FirstKActivity)?.replaceFragment(firstFragment)

        Thread(Runnable {
//            showresult()

        }).start()
        return view
    }
    private val consoleViewModel by viewModel<ConsoleViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_container.visibility= VISIBLE
        consoleViewModel.toTablePage()
        first_fragment.text=getString(R.string.btn_insert_record)
        first_fragment_dark.text=getString(R.string.btn_insert_record)
        second_fragment.text=getString(R.string.btn_insert)
        second_fragment_dark.text=getString(R.string.btn_insert)
        btn_renew.text="更換試算表"
        var intent=Intent(activity, MainActivity::class.java)
        btn_renew.listenClick {
            AlertDialog.Builder(activity).setTitle(R.string.reEnter)
                .setPositiveButton("確認") { _, _ ->
                    startActivity(intent)
                }.setNeutralButton("取消"){_, _ ->}.show()

        }
        first_fragment.setOnClickListener {
            second_fragment_dark.visibility=GONE
            second_fragment.visibility= VISIBLE
            first_fragment.visibility=GONE
            first_fragment_dark.visibility= VISIBLE
            consoleViewModel.toTablePage()
        }
        second_fragment.setOnClickListener {
            first_fragment_dark.visibility=GONE
            first_fragment.visibility= VISIBLE
            second_fragment.visibility=GONE
            second_fragment_dark.visibility= VISIBLE

            consoleViewModel.toNewPage()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showresult() {
        val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build()


        val result = service.spreadsheets().values().get(spreadsheetId_moo, range_moo_user+range_moo_col).execute()
        var values = result.getValues()
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.")
        } else {
//            System.out.println("Name, Major")
            Log.d(TAG,"\n分區, 姓名 , 性別")
            for ( row  in values) {
                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row.get(0), row.get(4))
                Log.d(TAG,row.get(0).toString()+" , "+ row.get(1).toString()+" , "+ row.get(2).toString()+"\n")

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
        var  ab = AuthorizationCodeInstalledApp(
            flow, receiver, browser).authorize("user")

        return ab
    }

    companion object {
        fun newInstance() = ConsoleFragment()
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_console
    }
}