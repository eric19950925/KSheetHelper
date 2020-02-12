package com.eric.ksheethelper.Base

import com.eric.ksheethelper.ConsoleCoordinator
import com.eric.ksheethelper.Login.LoginCoordinator
import org.koin.dsl.module

val coordinator = module {
    single { Navigator() }

    single { LoginCoordinator(get()) }

//    single { ConsoleCoordinator() }
}