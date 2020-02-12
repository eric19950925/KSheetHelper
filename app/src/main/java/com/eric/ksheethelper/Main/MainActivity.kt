package com.eric.ksheethelper.Main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import com.eric.ksheethelper.*
import com.eric.ksheethelper.Base.*
import com.google.android.gms.common.data.FreezableUtils.freeze
import com.google.android.material.textfield.TextInputLayout
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
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toast_container.*
import kotlinx.android.synthetic.main.custom_toast_container.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : MomActivity() {
    override fun getUrl(): String {
        return mSheetUrl
    }

    override fun customToast(s: String,c:Context,img:Int) {
        val v = layoutInflater.inflate(img, custom_toast_container, false)
        v.text_view.text = s
        with(Toast(c)) {
            duration = Toast.LENGTH_LONG
            view = v
            show()
        }
    }

    var TAG = MainActivity::class.java.simpleName
    val duration = Toast.LENGTH_SHORT
    val APPLICATION_NAME = "Google Sheets API KSheetHelper"
    val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
    val TOKENS_DIRECTORY_PATH = "tokens"
//    val SCOPES = listOf(SheetsScopes.SPREADSHEETS_READONLY)
//    val SCOPES : List<String> = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY)
    val SCOPES : List<String> = Arrays.asList("https://spreadsheets.google.com/feeds")
    val CREDENTIALS_FILE_PATH = "/credentials.json"
//    val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    val HTTP_TRANSPORT = com.google.api.client.http.javanet.NetHttpTransport()
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101


    val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
    val spreadsheetId_moo = "1j4El6Q92vAUoH_yiZBTxXQNCO2Ay3BC7zPAHcX0Kp78"
    val range = "Class Data!A2:E"
    val range_moo_user = "施懿宸"
    val range_moo_col = "!B8:D8"
    var userName="eric"
    var had_do=true//read data

    private val navigator by inject<Navigator>()//ok
    private val mainViewModel by viewModel<MainViewModel>()//ok
//    val vm = getViewModel<MainViewModel>()
//    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator.activity = this

        mainViewModel.toLogInPage()
        var profilePreferences = getSharedPreferences("profile", Context.MODE_APPEND)
        var profileEditor = profilePreferences.edit()
        had_do=profilePreferences.getBoolean("PermDone",false)
        if(had_do){permission_write()}

//        PleaseEnterYourName()
        var now_name=profilePreferences.getString("login_name","")
        if(now_name.equals("")){
            val ed_title = EditText(this@MainActivity)
            AlertDialog.Builder(this@MainActivity).setTitle(R.string.enter_user_name)
                .setView(ed_title)
                .setPositiveButton("確認") { _, _ ->
                    userName = ed_title.text.toString()
                    Log.d(TAG,"0117 name you input : "+userName)
                    profileEditor.putString("login_name",userName).apply()
                    now_name=profilePreferences.getString("login_name","")
                    Log.d(TAG,"0117 name you input : "+now_name.toString())
                }.setNeutralButton("取消"){_, _ ->
                }.show()
        }

/*
        //Respond to text change events in enterEmail//

        RxTextView.afterTextChangeEvents(et_url)

//Skip enterEmail’s initial, empty state//

            .skipInitialValue()

//Transform the data being emitted//

            .map {
                et_url.error = null

//Convert the user input to a String//

                it.view().text.toString()
            }

//Ignore all emissions that occur within a 400 milliseconds timespan//

            .debounce(400,

//Make sure we’re in Android’s main UI thread//

                TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

//Apply the validateEmailAddress transformation function//

            .compose(validateWebUrl)

//Apply the retryWhenError transformation function//

            .compose(retryWhenError {
                et_url.error = it.message
            })
            .subscribe()
*/



        }
//If the app encounters an error, then try again//

        private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> = ObservableTransformer { observable ->
            observable.retryWhen { errors ->

                ///Use the flatmap() operator to flatten all emissions into a single Observable//

                errors.flatMap {
                    onError(it)
                    Observable.just("")
                }

            }
        }

//Define our ObservableTransformer and specify that the input and output must be a string//

        private val validatePassword = ObservableTransformer<String, String> { observable ->
            observable.flatMap {
                Observable.just(it).map { it.trim() }

//Only allow passwords that are at least 7 characters long//

                    .filter { it.length > 7 }

//If the password is less than 7 characters, then throw an error//

                    .singleOrError()

//If an error occurs.....//

                    .onErrorResumeNext {
                        if (it is NoSuchElementException) {

//Display the following message in the passwordError TextInputLayout//

                            Single.error(Exception("Your password must be 7 characters or more"))

                        } else {
                            Single.error(it)
                        }
                    }
                    .toObservable()


            }

        }

//Define an ObservableTransformer, where we’ll perform the email validation//

        private val validateWebUrl = ObservableTransformer<String, String> { observable ->
            observable.flatMap {
                Observable.just(it).map { it.trim() }

//Check whether the user input matches Android’s email pattern//

                    .filter {
                        Patterns.WEB_URL.matcher(it).matches()

                    }

//If the user’s input doesn’t match the email pattern...//

                    .singleOrError()
                    .onErrorResumeNext {
                        if (it is NoSuchElementException) {

////Display the following message in the emailError TextInputLayout//

                            Single.error(Exception("Please enter a valid url"))
                        } else {
                            Single.error(it)
                        }
                    }
                    .toObservable()
            }
        }

    private fun startRStream() {
//Create an Observable//
        val myObservable = getObservable()

//Create an Observer//
        val myObserver = getObserver()

//Subscribe myObserver to myObservable//
        myObservable
//            .throttleFirst(5000, TimeUnit.MILLISECONDS)
            .debounce(5000, TimeUnit.MILLISECONDS)
            .subscribe(myObserver)
    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
            }
//Every time onNext is called, print the value to Android Studio’s Logcat//
            override fun onNext(s: String) {
                Log.d(TAG, "onNext: $s")
            }
//Called if an exception is thrown//
            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }
//When onComplete is called, print the following to Logcat//
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }
        }
    }
    private fun getObservable(): Observable<String> {
        return Observable.just("1", "2", "3", "4", "5")
    }



    private fun showSheetData() {
        Thread(Runnable {
            showresult()
//            postdata()

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
                NotiPermission()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                NotiPermission()
            }

        } else {
            // Permission has already been granted
            Log.d(TAG,"permission ok")

            NotiPermission()
        }
    }

    private fun NotiPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){
            customToast("成功取得資料，\n尚未制作呈現頁面",this,
                R.layout.custom_toast_success_container
            )
            var profilePreferences = getSharedPreferences("profile", Context.MODE_APPEND)
            var profileEditor = profilePreferences.edit()
            profileEditor.putBoolean("PermDone",true).apply()

        }else{
            customToast("沒有讀檔權限，無法驗證資料，\n請手動開啟權限",this,
                R.layout.custom_toast_wrong_container
            )
//                val toast = Toast.makeText(this,"沒有讀檔權限，無法驗證資料，\n請手動開啟權限", duration)
//                toast.show()
        }
    }

    private fun showresult() {
        val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build()

        val result = service.spreadsheets().values().get(spreadsheetId, range).execute()
        Log.d(TAG,"0121")
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
        val receiver = LocalServerReceiver.Builder().setPort(8080).build()//可有可無

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
