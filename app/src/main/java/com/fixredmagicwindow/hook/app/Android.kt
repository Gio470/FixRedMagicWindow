package com.fixredmagicwindow.hook.app

import com.fixredmagicwindow.hook.app.android.DisableWrAutoHang
import com.fixredmagicwindow.hook.app.android.PreventWrMiniToHangBubble
import com.fixredmagicwindow.hook.app.android.RmWindowReplyLimits
import com.fixredmagicwindow.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Android : AppRegister() {
    override val packageName: List<String> = listOf("android")
    override val processName: List<String> = emptyList()
    override val logTag: String = "FixRedMagicWindow-System"
    override val loadDexkit: Boolean = false

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(
            lpparam,
            RmWindowReplyLimits,//解除小窗限制
            DisableWrAutoHang,//禁止小窗拖动后自动贴边悬挂
            PreventWrMiniToHangBubble,//禁止小窗缩小到最小时自动变成悬浮气泡
        )
    }
}
