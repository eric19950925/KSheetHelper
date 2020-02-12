package com.eric.ksheethelper.Ext

import java.util.regex.Pattern

fun String.isUrlValid(error: ((isValid: Boolean, error: String?) -> Unit)? = null) {

    if (this.isEmpty()) {
        error?.invoke(false, null)
        return
    }

    val pattern =
//        Patterns.WEB_URL.matcher(this).matches()
        Pattern.compile("^(https?|ftp|file)://docs.google.com/spreadsheets/d/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

    if (!pattern.matcher(this).matches()) {
        error?.invoke(false, "  連結網址有誤")
        return
    }
    error?.invoke(true, null)
}