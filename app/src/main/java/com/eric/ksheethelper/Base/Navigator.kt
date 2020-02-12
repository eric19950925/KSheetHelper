package com.eric.ksheethelper.Base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.eric.ksheethelper.ConsoleFragment
import com.eric.ksheethelper.Main.MainHome
import com.eric.ksheethelper.NewDataFragment
import com.eric.ksheethelper.R
import com.eric.ksheethelper.SheetSmallTableFragment
import org.koin.ext.getFullName

class Navigator {

    lateinit var activity: FragmentActivity

    var lastAddTime: Long = 0
}

fun Navigator.toConsolePage() {
    addPage(ConsoleFragment.newInstance())
}
fun Navigator.toMainHome() {
    addPage(MainHome.newInstance())
}

fun Navigator.toTablePage() {
    replaceFragment(SheetSmallTableFragment())
}

fun Navigator.toNewPage() {
    replaceFragment(NewDataFragment())
}

fun Navigator.addPage(fragment: Fragment) {
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
                .add(R.id.container, fragment, fragment::class.getFullName())
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
                .add(R.id.container, fragment, fragment::class.getFullName())
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
            .add(R.id.container, fragment, fragment::class.getFullName())
            .addToBackStack(fragment::class.getFullName())
            .commit()
    }
}
    fun Navigator.replaceFragment(f : Fragment){
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, f)
            .commit()
    }
