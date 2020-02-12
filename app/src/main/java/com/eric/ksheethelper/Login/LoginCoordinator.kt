package com.eric.ksheethelper.Login

import com.eric.ksheethelper.Base.Coordinator
import com.eric.ksheethelper.Base.Navigator
import com.eric.ksheethelper.Base.addPage

class LoginCoordinator(private val navigator: Navigator) :
    Coordinator {
    override fun start() {
        navigator.addPage(Login.newInstance())
    }
    fun toLogInPage() {
        navigator.addPage(Login.newInstance())
    }
    override fun complete() {

    }
}