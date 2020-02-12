package com.eric.ksheethelper.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.ksheethelper.Base.FragmentBase
import com.eric.ksheethelper.Base.Navigator
import com.eric.ksheethelper.Main.MainActivity
import com.eric.ksheethelper.R
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class Login : FragmentBase(){

//    private val navigator by inject<Navigator>()
    private val loginViewModel by viewModel<LoginViewModel>()
    override fun initView() {
        btn_signin.setOnClickListener {
            loginViewModel.toMainHome()
        }
        btn_signup.setOnClickListener {
//            loginViewModel.toMainHome()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_login
    }
    companion object {
        fun newInstance() = Login()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = super.onCreateView(inflater, container, savedInstanceState)

        return view
    }
}