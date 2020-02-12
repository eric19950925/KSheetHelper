package com.eric.ksheethelper

import com.eric.ksheethelper.Base.BaseViewModel

class ConsoleViewModel(private val toTablePage: () -> Unit, private val toNewPage: () -> Unit): BaseViewModel() {
    fun toTablePage(){
        toTablePage.invoke()
    }
    fun toNewPage(){
        toNewPage.invoke()
    }

}