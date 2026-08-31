package com.fixredmagicwindow.hook.app.android

import android.annotation.SuppressLint
import android.graphics.Rect
import com.fixredmagicwindow.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object DisableWrAutoHang : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        val displayContentClass = getDefaultCL().loadClass("com.android.server.wm.DisplayContent")
        val activityRecordClass = getDefaultCL().loadClass("com.android.server.wm.ActivityRecord")
        val windowManagerServiceClass =
            getDefaultCL().loadClass("com.android.server.wm.WindowManagerService")
        val taskLaunchParamsModifierMifavorClass =
            getDefaultCL().loadClass("com.android.server.wm.TaskLaunchParamsModifierMifavor")

        //releasing a small-window drag near an edge auto-docks it as a hang bubble instead of
        //leaving it where it was dropped; forcing this to a no-op keeps the drop position.
        val performHangForWr = taskLaunchParamsModifierMifavorClass.getDeclaredMethod(
            "performHangForWr",
            displayContentClass,
            Rect::class.java,
            activityRecordClass,
            windowManagerServiceClass,
            Int::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        val blockHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = false
            }
        }
        XposedBridge.hookMethod(performHangForWr, blockHooker)
    }
}
