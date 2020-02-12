package com.eric.ksheethelper.Main

import androidx.core.widget.doOnTextChanged
import com.eric.ksheethelper.Base.*
import com.eric.ksheethelper.Ext.isUrlValid
import com.eric.ksheethelper.Ext.listenClick
import com.eric.ksheethelper.Ext.observe
import com.eric.ksheethelper.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_mainhome.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainHome : FragmentBase() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_mainhome
    }

    private val navigator by inject<Navigator>()//ok
    private val mainViewModel by viewModel<MainViewModel>()//ok
    override fun initView() {
        btn_enter.text=getString(R.string.btn_enter)
        btn_newSheet.text=getString(R.string.btn_newSheet)


        met_url.setHintTextAppearance(R.style.textinputlayout)
        met_url.isErrorEnabled = true
        et_url.doOnTextChanged { text, _, _, _ ->
            mainViewModel.inputWebUrl.value=text.toString()}
//            et_url.onRightDrawableClicked {
//                    it.text.clear()
//                }
//            val cancel = ResourcesCompat.getDrawable(resources, R.drawable.icons8_close, null)
//            cancel?.setBounds(0, 0, cancel.intrinsicWidth, cancel.intrinsicHeight)

        observe(mainViewModel.inputWebUrl){
            //                et_url.setCompoundDrawables(
//                    null,
//                    null,
//                    if (it.isNotEmpty()) cancel else null,
//                    null
//                )
            if (it.isNotEmpty()){
                met_url.setEndIconDrawable(R.drawable.icons8_close)
                met_url.setEndIconMode(TextInputLayout.END_ICON_CUSTOM)
                met_url.setEndIconOnClickListener {
                    et_url.text?.clear()
                }
            }else{
                met_url.setEndIconDrawable(null)

            }
            it.isUrlValid { _, error ->
                if (error != null) {
                    met_url.error = error
                    met_url.setErrorTextAppearance(R.style.textinputlayout)
                } else {
                    met_url.error = null
                }
            }
        }
        btn_newSheet.listenClick{
            //            Log.d(TAG, "0117 url before jump : "+et_url.text.toString())
            (activity as MainActivity).mSheetUrl=getSheetUrlText(et_url.text.toString())
//            var intent = Intent(this, FirstKActivity::class.java)
//            intent.putExtra("sheet_Url",mUrl)
//            startActivity(intent)
//            container.visibility= View.VISIBLE
//            old_main.visibility= View.GONE
            mainViewModel.toConsolePage()
        }
//        btn_enter.setOnClickListener(View.OnClickListener {
//            var intent = Intent(this,FirstKActivity::class.java)
//            intent.putExtra("sheet_Url","General_Enter")
//            startActivity(intent)
//            showSheetData()
//            startRStream()
//        })

        btn_enter.listenClick {
            //            Log.d(TAG,"0210"+i)
//            i++
        }
    }
    private fun getSheetUrlText(preUrl:String): String {
//        var fifthslash = preUrl.indexOf('/' ,5)
//        var lastSlash = preUrl.indexOfLast { it == '/' }
//        var uu:String=""
//        uu=preUrl.substring(61,lastSlash)
//        Log.d(TAG,"0120"+uu)
//        return uu
        return preUrl
    }
    companion object {
        fun newInstance() = MainHome()
    }
}