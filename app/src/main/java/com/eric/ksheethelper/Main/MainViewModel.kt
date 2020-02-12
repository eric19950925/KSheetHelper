package com.eric.ksheethelper.Main

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.eric.ksheethelper.Base.BaseViewModel

class MainViewModel (private val toLogInPage: () -> Unit,private val toConsolePage: () -> Unit): BaseViewModel() {

    fun toLogInPage() {
        toLogInPage.invoke()
        Log.d("0205"," : vm")
//        navigator.addPage(Login.newInstance())
    }
    val inputWebUrl = MutableLiveData<String>().apply {
        value = ""
    }

    fun toConsolePage(){
        toConsolePage.invoke()
    }



}