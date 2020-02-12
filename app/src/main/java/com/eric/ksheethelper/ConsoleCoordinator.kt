package com.eric.ksheethelper

import android.content.ContentValues
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import org.koin.ext.getFullName

class ConsoleCoordinator {
    lateinit var activity: FragmentActivity
    var lastAddTime: Long = 0
}


    fun ConsoleCoordinator.toTablePage() {
        addPage(SheetSmallTableFragment())
    }

    fun ConsoleCoordinator.toNewPage() {
        addPage(NewDataFragment())
    }

fun ConsoleCoordinator.addPage(fragment: Fragment) {
    if (System.currentTimeMillis() - lastAddTime < 500) {
        return
    }
    lastAddTime = System.currentTimeMillis()
    if (activity.supportFragmentManager.fragments.size > 0) {
        var needToHideFragment: Fragment? = null
        for (topFragment in activity.supportFragmentManager.fragments) {
            if (topFragment.isVisible) {
                needToHideFragment = topFragment
            }
        }

        if (needToHideFragment == null) {
            activity.supportFragmentManager.beginTransaction()
//                .setCustomAnimations(
//                    R.anim.anim_in,
//                    R.anim.anim_out,
//                  R.anim.pop_in,
//                    R.anim.pop_out
////                0,0
//                )
                .add(R.id.fragment_container, fragment, fragment::class.getFullName())
                .addToBackStack(fragment::class.getFullName())
                .commit()
        } else {
            activity.supportFragmentManager.beginTransaction()
//                .setCustomAnimations(
//                    R.anim.anim_in,
//                    R.anim.anim_out,
////                    0,0
//                    R.anim.pop_in,
//                    R.anim.pop_out
//                )
                .add(R.id.fragment_container, fragment, fragment::class.getFullName())
                .hide(needToHideFragment)
                .addToBackStack(fragment::class.getFullName())
                .commit()
        }
    } else {
        activity.supportFragmentManager.beginTransaction()
//            .setCustomAnimations(
//                R.anim.anim_in,
//                R.anim.anim_out,
////                0,0
//                R.anim.pop_in,
//                R.anim.pop_out
//            )
            .add(R.id.fragment_container, fragment, fragment::class.getFullName())
            .addToBackStack(fragment::class.getFullName())
            .commit()
    }
}
