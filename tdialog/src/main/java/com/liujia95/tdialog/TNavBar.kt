package com.denglin.duck.dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.liujia95.tdialog.TDialog
import com.liujia95.tdialog.dpToPx
import com.liujia95.tdialog.navBarHeight

/**
 * author : liujia95me@126.com
 * time   : 2020/12/11
 * desc   :
 * version: 1.0
 * 模拟手机底部的nav-bar
 * 有颜色，有左右边距
 * 只要TDialog一添加TNavBar，TDialog则立马减去bar的高度
 */
class TNavBar(val context: Context) {

    val view: View = View(context)

    var color = 0x00000000 //默认透明

    var leftMargin = 0f //dp

    var rightMargin = 0f //dp

    companion object {
        const val TAG = "TNavBar"
    }

    init {
        (view.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            height = navBarHeight(context)
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            width = ViewGroup.LayoutParams.MATCH_PARENT
            leftMargin = dpToPx(context,this@TNavBar.leftMargin)
            rightMargin = dpToPx(context,this@TNavBar.rightMargin)
        }
    }

    fun bind(dialog: TDialog) {
        (dialog.rootView.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            bottomToTop = view.id
        }
    }

    fun unBind(dialog: TDialog) {
        (dialog.rootView.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        }
    }
}