package com.github.jing332.frpandroid.model

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.ui.SwitchFrpActivity


object ShortCuts {
    private inline fun <reified T> buildIntent(context: Context): Intent {
        val intent = Intent(context, T::class.java)
        intent.action = Intent.ACTION_VIEW
        return intent
    }


    private fun buildFrpcSwitchShortCutInfo(context: Context): ShortcutInfoCompat {
        val msSwitchIntent = buildIntent<SwitchFrpActivity>(context).apply { action = "frpc" }
        return ShortcutInfoCompat.Builder(context, "frpc_switch")
            .setShortLabel(context.getString(R.string.frpc_switch))
            .setLongLabel(context.getString(R.string.frpc_switch))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_frpc))
            .setIntent(msSwitchIntent)
            .build()
    }

    private fun buildFrpsSwitchShortCutInfo(context: Context): ShortcutInfoCompat {
        val msSwitchIntent = buildIntent<SwitchFrpActivity>(context).apply { action = "frps" }
        return ShortcutInfoCompat.Builder(context, "frps_switch")
            .setShortLabel(context.getString(R.string.frps_switch))
            .setLongLabel(context.getString(R.string.frps_switch))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_frps))
            .setIntent(msSwitchIntent)
            .build()
    }


    fun buildShortCuts(context: Context) {
        ShortcutManagerCompat.setDynamicShortcuts(
            context, listOf(
                buildFrpcSwitchShortCutInfo(context),
                buildFrpsSwitchShortCutInfo(context)
            )
        )
    }


}