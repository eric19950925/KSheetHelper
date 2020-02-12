package com.eric.ksheethelper.Base

import com.eric.ksheethelper.ConsoleViewModel
import com.eric.ksheethelper.Login.LoginCoordinator
import com.eric.ksheethelper.Login.LoginViewModel
import com.eric.ksheethelper.Main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


    val viewModelModule = module {
        viewModel {
            MainViewModel(
                get<LoginCoordinator>()::toLogInPage,
                get<Navigator>()::toConsolePage
            )
        }
        viewModel {
            LoginViewModel(
            get<Navigator>()::toMainHome
            )
        }
        viewModel {
            ConsoleViewModel(
            get<Navigator>()::toTablePage,
                get<Navigator>()::toNewPage
//                get<Navigator>()::toMainHome
            )
        }
    }
