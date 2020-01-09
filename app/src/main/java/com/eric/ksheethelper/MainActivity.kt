package com.eric.ksheethelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn_LogIn = findViewById<Button>(R.id.btn_login)
        btn_LogIn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,FirstKActivity::class.java)
            startActivity(intent)
        })
    }
}
