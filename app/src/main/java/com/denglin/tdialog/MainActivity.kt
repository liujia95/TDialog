package com.denglin.tdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import com.liujia95.tdialog.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_has).text = "是否有底部导航栏：${hasNavBar(this)}"
        findViewById<TextView>(R.id.tv_height).text = "底部导航栏高度：${getNavigationBarHeight(this)}"
        findViewById<TextView>(R.id.tv_dialog1).setOnClickListener {
            test1()
        }
    }

    private fun test1(){
        TDialog(this)
            .config {
                isCancelable = true
                gravity = Gravity.CENTER
                margin = 18f
            }
            .covert(R.layout.dialog_test){

            }
            .show()
    }

}