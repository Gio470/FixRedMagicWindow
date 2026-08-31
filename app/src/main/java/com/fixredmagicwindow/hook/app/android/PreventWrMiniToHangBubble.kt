package com.fixredmagicwindow.hook.app.android

import android.annotation.SuppressLint
import com.fixredmagicwindow.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object PreventWrMiniToHangBubble : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        val taskClass = getDefaultCL().loadClass("com.android.server.wm.Task")
        val activityRecordClass = getDefaultCL().loadClass("com.android.server.wm.ActivityRecord")

        //releasing a resize-drag below the "mini area" threshold shrinks the window down and
        //converts it into a tap-to-restore hang bubble; no-oping this keeps it a small but
        //still-interactive window instead of turning it into a restore icon.
        val changeToHangInDragAnim = taskClass.getDeclaredMethod(
            "changeToHangInDragAnim", activityRecordClass
        )
        val blockHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = null
            }
        }
        XposedBridge.hookMethod(changeToHangInDragAnim, blockHooker)
    }
}
