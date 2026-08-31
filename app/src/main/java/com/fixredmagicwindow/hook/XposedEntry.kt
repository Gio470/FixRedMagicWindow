package com.fixredmagicwindow.hook

import com.fixredmagicwindow.hook.app.Android
import com.fixredmagicwindow.util.xposed.EasyXposedInit
import com.fixredmagicwindow.util.xposed.base.AppRegister

class XposedEntry : EasyXposedInit() {
    override val registeredApp: List<AppRegister> = listOf(
        Android, //Android
    )
}
