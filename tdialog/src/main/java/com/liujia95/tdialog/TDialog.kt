package com.liujia95.tdialog

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.*
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.denglin.duck.dialog.TDialogViewHolder

/**
 * author : liujia95me@126.com
 * time   : 2020/12/11
 * desc   :
 * 1，拦截back键
 * 2，支持键盘弹出的上移
 */
class TDialog(val context: Context) {

    companion object {
        const val INVALID = -1
        const val TAG = "TDialog"
    }

    lateinit var window: Window

    lateinit var decorView: View

    //activity的根view
    lateinit var decorRootView: ViewGroup

    //dialog根view
    lateinit var rootView: ConstraintLayout

    val navBarHeight by lazy { if (BarUtils.isNavBarVisible(context as Activity)) BarUtils.getNavBarHeight() else 1 }

    lateinit var navigationSpace: View

    var navBarColor: Int = getColor(R.color.transparent)

    var marginBottom = 0f // 弹框底部距离nav-bar的距离

    var enableKeyboardAnim = true //是否需要键盘弹出时，dialog上顶的动画

    private var layoutId = INVALID

    var margin = 0f

    var gravity = Gravity.CENTER
        set(value) {
            field = value
            when (field) {
                Gravity.TOP -> vBias = 0f
                Gravity.CENTER -> vBias = 0.5f
                Gravity.BOTTOM -> vBias = 1f
            }
        }

    var isCancelable = true

    var inAnim = 0

    var outAnim = 0

    var background: TBackground = TBackground(context)

//    var navBar: TNavBar? = null//底部导航栏

    lateinit var holder: TDialogViewHolder

    var vBias: Float = 0f


    inline fun config(block: TDialog.() -> Unit): TDialog {
        (context as? Activity)?.let {
            window = it.window
            decorView = it.window.decorView
            decorRootView = decorView.rootView as ViewGroup
            rootView = ConstraintLayout(context)
            rootView.isClickable = true
            navigationSpace = View(context)
            navigationSpace.id = android.R.id.empty
            this.block()
        }
        return this
    }

    fun inflate(layoutId: Int, root: ViewGroup? = null): View {
        return LayoutInflater.from(context).inflate(layoutId, root, false)
    }

    fun background(block: TBackground.() -> Unit): TDialog {
        background = TBackground(context)
        block(background)
        return this
    }

//    fun navBar(block: TNavBar.() -> Unit): TDialog {
//        navBar = TNavBar(context)
//        block(navBar!!)
//        return this
//    }

    fun covert(layoutId: Int, block: TDialogViewHolder.(TDialog) -> Unit): TDialog {
        this.layoutId = layoutId
        holder = TDialogViewHolder(inflate(layoutId) as ViewGroup)
        block(holder, this)
        return this
    }

    fun covert(view: View, block: TDialogViewHolder.(TDialog) -> Unit): TDialog {
        holder = TDialogViewHolder(view)
        block(holder, this)
        return this
    }

    fun show() {
        TDialogManager.push(this)
        decorRootView.addView(rootView)

        background.view.setBackgroundColor(Color.parseColor("#${Integer.toHexString((255 * background.dimAmount).toInt())}000000"))

        navigationSpace.setBackgroundColor(navBarColor)


        //java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
        background.view.removeParent()
        holder.rootView.removeParent()
        navigationSpace.removeParent()
        rootView.addView(background.view)
        rootView.addView(holder.rootView)
        rootView.addView(navigationSpace)

        //-------- layout-params --------
        (navigationSpace.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            height = navBarHeight + dpToPx(marginBottom)
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        }

        (holder.rootView.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            if (gravity == Gravity.BOTTOM) {
                bottomToTop = navigationSpace.id
            } else {
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            }
            width = ViewGroup.LayoutParams.MATCH_PARENT
            leftMargin = dpToPx(margin)
            rightMargin = dpToPx(margin)
            verticalBias = vBias
        }
        //-------- listener --------
        if (isCancelable) {
            background.view.setOnClickListener {
                dismiss()
            }
        }

        holder.rootView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, keyCode: Int, keyEvent: KeyEvent): Boolean {
                when (keyEvent.action) {
                    KeyEvent.ACTION_UP -> {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dismiss()
                            return true
                        }
                    }
                }
                return false
            }
        })
        KeyboardUtils.registerSoftInputChangedListener(window) { height ->
            if (enableKeyboardAnim) {
                val beforeBottom = holder.rootView.paddingBottom
                val anim = ValueAnimator.ofInt(beforeBottom, height)
                anim.duration = 150
                anim.addUpdateListener {
                    val value: Int = it.animatedValue as Int
                    rootView.setPadding(0, 0, 0, value)
                }
                anim.start()
            } else {
                rootView.setPadding(0, 0, 0, height)
            }
        }
        //-------- animator --------
        background.showInAnim()
        if (inAnim != 0) {
            holder.rootView.startAnimation(AnimationUtils.loadAnimation(context, inAnim))
        }
    }

    fun dismiss(delay: Long = 0, needOutAnim: Boolean = true, pop: Boolean = true) {
        runDelayed(delay) {
            rootView.setPadding(0, 0, 0, 0)
            if (outAnim != 0) {
                holder.rootView.startAnimation(AnimationUtils.loadAnimation(context, outAnim))
            }
            if (needOutAnim) {
                rootView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_alpha_exit))
            }
            if (pop) {
                TDialogManager.pop(this)
            }
            decorRootView.removeView(rootView)
            if (KeyboardUtils.isSoftInputVisible(context as Activity)) {
                KeyboardUtils.hideSoftInput(context)
            }
            KeyboardUtils.unregisterSoftInputChangedListener(window)
        }
    }

}