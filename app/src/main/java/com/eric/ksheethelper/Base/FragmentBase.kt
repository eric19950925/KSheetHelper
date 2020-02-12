package com.eric.ksheethelper.Base

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun initView()

    fun AddPage(nextFragment: Fragment, name: String) {
        (activity as MomActivity).AddPage(nextFragment, name, 0, 0, 0, 0)
    }
    open fun getTAG(): String = this::class.java.simpleName

}