package com.liujia95.tdialog

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.Window
import com.liujia95.tdialog.androidcodeutil.Utils
import java.lang.reflect.Method


/**
 * author : liujia95me@126.com
 * time   : 2020/12/25
 * desc   :
 * version: 1.0
 */

fun isNavBarVisible(context: Context): Boolean {
    return Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0
}

//px
fun navBarHeight(context: Context): Int {
    if (!isNavBarVisible(context)) { //这个函数请查看上一片文章
        return 0
    }
    val resources: Resources = context.resources
    val resourceId: Int = resources.getIdentifier(
        "navigation_bar_height",
        "dimen", "android"
    )
    var height = 0
    if (resourceId > 0) {
        height = resources.getDimensionPixelSize(resourceId)
    }
    return height
}

fun dpToPx(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}


/**
 * 获取虚拟按键的高度
 */
fun getNavigationBarHeight(context: Context): Int {
    var result = 0
    if (hasNavBar(context)) {
        val res = context.resources
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
    }
    return result
}

/**
 * 检查是否存在虚拟按键栏
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
fun hasNavBar(context: Context): Boolean {
    val res = context.resources
    val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
    return if (resourceId != 0) {
        var hasNav = res.getBoolean(resourceId)
        val sNavBarOverride: String? = getNavBarOverride()
        if ("1" == sNavBarOverride) {
            hasNav = false
        } else if ("0" == sNavBarOverride) {
            hasNav = true
        }
        hasNav
    } else {
        !ViewConfiguration.get(context).hasPermanentMenuKey()
    }
}

fun getNavBarOverride(): String? {
    var sNavBarOverride: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            val c = Class.forName("android.os.SystemProperties")
            val m: Method = c.getDeclaredMethod("get", String::class.java)
            m.setAccessible(true)
            sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String?
        } catch (e: Exception) {
        }
    }
    return sNavBarOverride
}


fun isNavBarVisible(window: Window): Boolean {
    var isVisible = false
    val decorView = window.decorView as ViewGroup
    var i = 0
    val count = decorView.childCount
    while (i < count) {
        val child = decorView.getChildAt(i)
        val id = child.id
        if (id != View.NO_ID) {
            val resourceEntryName: String = window.context.resources.getResourceEntryName(id)
            if ("navigationBarBackground" == resourceEntryName && child.visibility == View.VISIBLE) {
                isVisible = true
                break
            }
        }
        i++
    }
    if (isVisible) {
        // 对于三星手机，android10以下非OneUI2的版本，比如 s8，note8 等设备上，
        // 导航栏显示存在bug："当用户隐藏导航栏时显示输入法的时候导航栏会跟随显示"，会导致隐藏输入法之后判断错误
        // 这个问题在 OneUI 2 & android 10 版本已修复
        if (RomUtils.isSamsung()
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT < 29
        ) {
            try {
                return Settings.Global.getInt(
                    Utils.getApp().contentResolver,
                    "navigationbar_hide_bar_enabled"
                ) == 0
            } catch (ignore: java.lang.Exception) {
            }
        }
        val visibility = decorView.systemUiVisibility
        isVisible = visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
    }
    return isVisible
}