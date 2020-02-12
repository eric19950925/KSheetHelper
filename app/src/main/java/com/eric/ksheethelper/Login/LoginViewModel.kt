package com.eric.ksheethelper.Login

import android.util.Log
import com.eric.ksheethelper.Base.BaseViewModel

class LoginViewModel(private val toMainPage: () -> Unit): BaseViewModel() {
    fun toMainHome() {
        toMainPage.invoke()
        Log.d("0205"," : vm")
//        navigator.addPage(Login.newInstance())
    }

}