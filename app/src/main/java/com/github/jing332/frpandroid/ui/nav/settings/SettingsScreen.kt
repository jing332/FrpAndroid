package com.github.jing332.frpandroid.ui.nav.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.jing332.frpandroid.config.AppConfig
import com.github.jing332.frpandroid.ui.MyTools.killBattery
import com.github.jing332.frpandroid.util.ToastUtils.longToast

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    Column(Modifier.statusBarsPadding()) {
        var checkUpdate by remember { AppConfig.isAutoCheckUpdate }
        PreferenceSwitch(
            title = { Text("自动检查更新") },
            subTitle = { Text("打开程序主界面时从Github检查更新") },
            checked = checkUpdate,
            onCheckedChange = { checkUpdate = it },
            icon = {
                Icon(Icons.Default.ArrowCircleUp, contentDescription = null)
            }
        )

        var wakeLock by remember { AppConfig.enabledWakeLock }
        PreferenceSwitch(
            title = { Text("唤醒锁") },
            subTitle = { Text("打开后可防止锁屏后CPU休眠，但在部分系统可能会导致杀后台") },
            checked = wakeLock,
            onCheckedChange = { wakeLock = it },
            icon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            }
        )

        PreferenceWidget(onClick = {
            if (context.killBattery()){
                context.longToast("程序已在电池优化白名单")
            }
        }, title = {
            Text("请求设置电池优化白名单")
        }, subTitle = {
            Text("如果程序在后台运行时会被系统杀死，可以尝试设置。")
        }) {

        }


    }
}