package com.fixredmagicwindow.hook.app.android

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import com.fixredmagicwindow.util.PatchKeys
import com.fixredmagicwindow.util.XSPUtils
import com.fixredmagicwindow.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object DisableWrEdgeClamp : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        val displayContentClass = getDefaultCL().loadClass("com.android.server.wm.DisplayContent")
        val taskLaunchParamsModifierMifavorClass =
            getDefaultCL().loadClass("com.android.server.wm.TaskLaunchParamsModifierMifavor")

        //while actively dragging a small window near a screen edge, this clamps the bounds back
        //fully on-screen every frame instead of letting it get partially cut off; returning the
        //rect unchanged skips the clamp so the window can be dragged off-screen freely. The other
        //overloads all funnel into this one, so hooking it alone covers every caller.
        val adjustPositionForWr = taskLaunchParamsModifierMifavorClass.getDeclaredMethod(
            "adjustPositionForWr",
            Context::class.java,
            displayContentClass,
            Rect::class.java,
            Float::class.javaPrimitiveType
        )
        val passthroughHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean(PatchKeys.DISABLE_WR_EDGE_CLAMP, PatchKeys.defaultFor(PatchKeys.DISABLE_WR_EDGE_CLAMP))) {
                    param!!.result = param.args[2]
                }
            }
        }
        XposedBridge.hookMethod(adjustPositionForWr, passthroughHooker)
    }
}
