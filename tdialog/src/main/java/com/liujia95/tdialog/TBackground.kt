package com.liujia95.tdialog

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

/**
 * author : liujia95me@126.com
 * time   : 2020/12/11
 * desc   :
 * version: 1.0
 */
class TBackground(val context: Context) {

    val view: View = View(context)

    companion object {
        const val TAG = "TBackground"
    }

    var dimAmount = 0.4f // 背景透明度

    var inAnim = R.anim.dialog_alpha_enter

    var outAnim = R.anim.dialog_alpha_exit

    fun showInAnim(duration:Long = 200L):Animation {
        val anim = AnimationUtils.loadAnimation(context, inAnim)
        anim.duration = duration
        view.startAnimation(anim)
        return anim
    }

    fun showOutAnim(duration:Long = 200L):Animation{
        val anim = AnimationUtils.loadAnimation(context, outAnim)
        anim.duration = duration
        view.startAnimation(anim)
        return anim
    }


}