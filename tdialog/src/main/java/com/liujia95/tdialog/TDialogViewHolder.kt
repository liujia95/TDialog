package com.denglin.duck.dialog

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes

/**
 * author : liujia95me@126.com
 * time   : 2020/12/11
 * desc   :
 * version: 1.0
 */
class TDialogViewHolder(val rootView: View) {

    var views: SparseArray<View> = SparseArray()

    fun <T : View> getView(@IdRes viewId: Int): T {
        var view = views[viewId]
        if (view == null) {
            view = rootView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T
    }

    fun setText(@IdRes viewId: Int, value: CharSequence) {
        getView<TextView>(viewId).text = value
    }

    fun setText(@IdRes viewId: Int, @StringRes strId: Int) {
        getView<TextView>(viewId).setText(strId)
    }

    fun setVisible(@IdRes viewId: Int, visible: Boolean) {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    fun setOnClickListener(@IdRes vararg viewIds: Int, listener: (View) -> Unit) {
        for (id in viewIds) {
            val view = getView<View>(id)
            view.isClickable = true
            view.setOnClickListener {
                listener(view)
            }
        }
    }
}