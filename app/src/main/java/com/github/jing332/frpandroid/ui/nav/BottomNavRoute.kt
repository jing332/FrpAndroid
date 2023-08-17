package com.github.jing332.frpandroid.ui.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.github.jing332.frpandroid.R

sealed class BottomNavRoute(
    @StringRes val strId: Int,
    val id: String,
    val icon: @Composable (() -> Unit),
) {
    companion object {
        val routes = listOf(
            Frps,
            Frpc,
            Settings,
        )
    }

    data object Frps : BottomNavRoute(R.string.frps, "frps", {
        Icon(
            painterResource(id = R.drawable.server), null,
        )
    })

    data object Frpc : BottomNavRoute(R.string.frpc, "frpc", {
        Icon(
            painterResource(id = R.drawable.client), null,
        )
    })

    data object Settings : BottomNavRoute(R.string.settings, "settings", {
        Icon(Icons.Default.Settings, null)
    })
}