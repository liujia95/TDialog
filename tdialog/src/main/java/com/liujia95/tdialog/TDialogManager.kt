package com.liujia95.tdialog

/**
 * author : liujia95me@126.com
 * time   : 2020/12/11
 * desc   :
 * dialog栈
 */
object TDialogManager {

    private val dialogs = mutableListOf<TDialog>()

    fun push(dialog: TDialog) {
        dialogs.add(dialog)
    }

    //如果有dialog，则把dialog关闭，返回true
    //如果不存在dialog，则返回false
    fun pop(): Boolean {
        if (dialogs.size > 0) {
            val dialog = dialogs.removeAt(dialogs.size - 1)
            dialog.dismiss()
            return true
        }
        return false
    }

    fun pop(dialog: TDialog) {
        if (dialogs.contains(dialog)) {
            dialogs.remove(dialog)
        }
    }

    fun clear() {
        for (dialog in dialogs) {
            dialog.dismiss(pop = false)
        }
        dialogs.clear()
    }
}