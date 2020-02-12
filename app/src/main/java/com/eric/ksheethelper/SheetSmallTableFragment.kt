package com.eric.ksheethelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.ksheethelper.Base.FragmentBase

class SheetSmallTableFragment : FragmentBase() {
    override fun initView() {


    }
    companion object {
        fun newInstance() = SheetSmallTableFragment()
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_sheet_small_table
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}