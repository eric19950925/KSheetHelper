package com.eric.ksheethelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class FragmentBase:Fragment() {


    protected abstract fun getLayoutRes(): Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(getLayoutRes(), container, false)
        return v
    }
}