package com.fixredmagicwindow.hook.app.android

import android.annotation.SuppressLint
import com.fixredmagicwindow.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object RmWindowReplyLimits : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        //allow all package
        val pkgInWhiteList = getDefaultCL().loadClass("android.app.WindowReplyUtils")
            .getDeclaredMethod("isForceSupportWhiteListForWR", String::class.java)
        val pkgWhiteListHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = true
            }
        }
        XposedBridge.hookMethod(pkgInWhiteList, pkgWhiteListHooker)

        //patch max window count
        val isMaxForMulti =
            getDefaultCL().loadClass("com.android.server.wm.ActivityTaskManagerService")
                .getDeclaredMethod("isReachWrMaxSizeForMulti")
        val maxMultiHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = false
            }
        }
        XposedBridge.hookMethod(isMaxForMulti, maxMultiHooker)
    }
}
