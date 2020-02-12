package com.eric.ksheethelper.Base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val tag by lazy { this.javaClass.simpleName }
    val empty = MutableLiveData<Boolean>().apply { value = false }

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val toastMessage = MutableLiveData<String>()
//    val error = SingleLiveEvent<Throwable>()

    val isProgressing = MutableLiveData(false)

//    val disposable = CompositeDisposable()

    override fun onCleared() {
//        disposable.clear()
        super.onCleared()
    }

    fun getString(resId: Int) =
        MyApplication.instance.getString(resId)
}