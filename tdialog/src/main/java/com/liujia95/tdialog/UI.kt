package com.liujia95.tdialog

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.liujia95.tdialog.androidcodeutil.Utils

/**
 * author : liujia95me@126.com
 * time   : 2020/04/26
 * desc   :
 * version: 1.0
 */

val context: Context = Utils.getApp()

private val handler = Handler(Looper.getMainLooper())

fun bold(tv: TextView) {
    tv.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
}

fun unBold(tv: TextView) {
    tv.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
}

val Number.dp: Int get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()

fun dpToPx(dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun pxToDp(pxValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return pxValue / scale + 0.5f
}

fun spToPx(spValue: Float): Float {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return spValue * fontScale + 0.5f
}

fun getColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(context, id)
}

fun getString(@StringRes id: Int): String {
    return context.getString(id)
}


fun getString(@StringRes id: Int, vararg formatArgs: String): String {
    return context.getString(id, formatArgs)
}

fun runDelayed(delayMillis: Long, block: () -> Unit) {
    handler.postDelayed(block, delayMillis)
}

fun runMainThread(block: () -> Unit) {
    handler.post(block)
}

fun View.removeParent() {
    (this.parent as? ViewGroup)?.removeView(this)
}

fun View.gone(any:Any?) {
    if(any == null){
        this.visibility = View.GONE
    }else{
        this.visibility = View.VISIBLE
    }
}

fun View.invisible(any:Any?) {
    if(any == null){
        this.visibility = View.INVISIBLE
    }else{
        this.visibility = View.VISIBLE
    }
}

fun View.gone(gone:Boolean) {
    if(gone){
        this.visibility = View.GONE
    }else{
        this.visibility = View.VISIBLE
    }
}

fun View.invisible(invisible:Boolean) {
    if(invisible){
        this.visibility = View.INVISIBLE
    }else{
        this.visibility = View.VISIBLE
    }
}

//把一组数据 根据某条件 拆成多组
//例如一组账单需按天拆
inline fun <T> Iterable<T>.split(predicate: (T, T?) -> Boolean): ArrayList<ArrayList<T>> {
    val destination = ArrayList<ArrayList<T>>()
    var lastElement: T? = null
    for ((index, element) in this.withIndex()) {
        if (index == 0) {
            destination.add(ArrayList<T>().apply { add(element) })
        } else {
            if (predicate(element, lastElement)) {
                destination[destination.lastIndex].add(element)
            } else {
                destination.add(ArrayList<T>().apply { add(element) })
            }
        }
        lastElement = element
    }
    return destination
}

fun isNavigationBarShow(activity: Activity): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val display: Display = activity.windowManager.defaultDisplay
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        realSize.y != size.y
    } else {
        val menu = ViewConfiguration.get(activity).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        !(menu || back)
    }
}

//------------- 动画
fun startAnimator(view: View, from: Float, to: Float, dur: Long = 300L, interp: TimeInterpolator = LinearInterpolator(), endListener: (() -> Unit)? = null) {
    ObjectAnimator
            .ofFloat(view, "translationY", from, to)
            .apply {
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}
                    override fun onAnimationEnd(p0: Animator?) {
                        endListener?.invoke()
                    }

                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationStart(p0: Animator?) {}
                })
                duration = dur
                interpolator = interp
                start()
            }
}

//------------- drawable

fun Drawable.tint(color: Int): Drawable {
    val wrappedDrawable: Drawable = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrappedDrawable, color)
    return wrappedDrawable
}
