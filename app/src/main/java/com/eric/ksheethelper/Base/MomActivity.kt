package com.eric.ksheethelper.Base

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eric.ksheethelper.R

abstract class MomActivity : AppCompatActivity() {

    lateinit var mSheetUrl:String
    abstract fun getUrl():String
    abstract fun customToast(s:String,c:Context,img:Int)

    open fun setUrl(ms:String){
        mSheetUrl=ms
    }
    fun AddPage(
        nextFragment: Fragment,
        name: String,
        inAni: Int,
        outAni: Int,
        inBackAni: Int,
        outBackAni: Int
    ) {
        val manager = this.supportFragmentManager

        manager?.beginTransaction()?.addToBackStack(name)
            ?.setCustomAnimations(inAni, outAni, inBackAni, outBackAni)
            ?.add(R.id.container, nextFragment, name)?.commit()
    }

    fun freeze(i: Int){
//        if(i==1){setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)}
//        else{
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//        }
    }
}