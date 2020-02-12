package com.eric.ksheethelper


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.eric.ksheethelper.Base.FragmentBase
import kotlinx.android.synthetic.main.fragment_new_data.*

class NewDataFragment: FragmentBase() {
    override fun initView() {

    }
    companion object {
        fun newInstance() = NewDataFragment()
    }
    var str_region= "region"
    var str_Sex= "sex"
    var str_gf= "gf"
    var str_con= "固定"
    var insertNum=8
//    var TAG=NewDataFragment::class.java.simpleName

    override fun getLayoutRes(): Int {
        return R.layout.fragment_new_data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = super.onCreateView(inflater, container, savedInstanceState)
        val firstFragment = SheetSmallTableFragment()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_insert.text=getString(R.string.btn_sure_insert)
        tv_region.text=getString(R.string.tv_region)
        tv_name.text=getString(R.string.tv_name)
        tv_sex.text=getString(R.string.tv_sex)
        tv_gf.text=getString(R.string.tv_gf)
        tv_con.text=getString(R.string.tv_con)

        val adapterCon = ArrayAdapter.createFromResource(
            context,
            R.array.Con, android.R.layout.simple_spinner_item
        )
        adapterCon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_con.setAdapter(adapterCon)
        sp_con.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                str_con = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })
        val adapterRegion = ArrayAdapter.createFromResource(
            context,
            R.array.Region, android.R.layout.simple_spinner_item
        )
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_region.setAdapter(adapterRegion)
        sp_region.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                str_region = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })
        val adapterSex = ArrayAdapter.createFromResource(
            context,
            R.array.Sex, android.R.layout.simple_spinner_item
        )
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_sex.setAdapter(adapterSex)
        sp_sex.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                str_Sex = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })
        val adapterGF = ArrayAdapter.createFromResource(
            context,
            R.array.GF, android.R.layout.simple_spinner_item
        )
        adapterGF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_gf.setAdapter(adapterGF)
        sp_gf.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                str_gf = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })

        var profilePreferences = activity?.getSharedPreferences("profile", Context.MODE_APPEND)
        var now_name=profilePreferences?.getString("login_name","")
        var profileEditor = profilePreferences?.edit()
        var insertNumString :String = ""
        var insertNumString_non :String = ""
        btn_insert.setOnClickListener {
            if(str_con.equals("固定牧養")){
                insertNumString = profilePreferences?.getString("insert_num","1").toString()
                Log.d(TAG, "0117 data : "+now_name.toString()+" ; "+str_con+" ; "+str_region+" ; "+et_name.text.toString()+" ; "+str_Sex+" ; "+str_gf+" ; "+insertNumString)
//                Thread(Runnable {
//
//                (activity as FirstKActivity).postdata(now_name.toString(),str_con,str_region,etName?.text.toString(),
//                str_Sex,str_gf,insertNumString.toString())
//
//                }).start()
                insertNumString=(insertNumString?.toIntOrNull()?.plus(1)).toString()
                profileEditor?.putString("insert_num",insertNumString)?.apply()
            }else{
                insertNumString_non = profilePreferences?.getString("insert_num_non","1").toString()
                Log.d(TAG, "0117 data : "+now_name.toString()+" ; "+str_con+" ; "+str_region+" ; "+et_name.text.toString()+" ; "+str_Sex+" ; "+str_gf+" ; "+insertNumString_non)
    //                Thread(Runnable {
    //
    //                (activity as FirstKActivity).postdata(now_name.toString(),str_con,str_region,etName?.text.toString(),
    //                str_Sex,str_gf,insertNumString.toString())
    //
    //                }).start()
                insertNumString_non=(insertNumString_non?.toIntOrNull()?.plus(1)).toString()
                profileEditor?.putString("insert_num_non",insertNumString_non)?.apply()
            }
//            (activity as FirstKActivity).replaceFragment(firstFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}