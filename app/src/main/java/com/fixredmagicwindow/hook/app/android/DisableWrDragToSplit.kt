package com.fixredmagicwindow.hook.app.android

import android.annotation.SuppressLint
import com.fixredmagicwindow.util.PatchKeys
import com.fixredmagicwindow.util.XSPUtils
import com.fixredmagicwindow.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object DisableWrDragToSplit : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        val taskPositionerClass = getDefaultCL().loadClass("com.android.server.wm.TaskPositioner")

        //dragging a small window to the top/bottom edge (portrait) or left/right edge (landscape)
        //toggles it into split-screen mode; forcing both checks false disables that gesture.
        val falseHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean(PatchKeys.DISABLE_WR_DRAG_TO_SPLIT, PatchKeys.defaultFor(PatchKeys.DISABLE_WR_DRAG_TO_SPLIT))) {
                    param!!.result = false
                }
            }
        }

        val checkNeedToggleFromWrToSplitForPorirait = taskPositionerClass.getDeclaredMethod(
            "checkNeedToggleFromWrToSplitForPorirait"
        )
        XposedBridge.hookMethod(checkNeedToggleFromWrToSplitForPorirait, falseHooker)

        val checkWrToSplitForLand = taskPositionerClass.getDeclaredMethod("checkWrToSplitForLand")
        XposedBridge.hookMethod(checkWrToSplitForLand, falseHooker)
    }
}
