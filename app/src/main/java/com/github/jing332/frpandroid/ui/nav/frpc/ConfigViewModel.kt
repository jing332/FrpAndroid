package com.github.jing332.frpandroid.ui.nav.frpc

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.github.jing332.frpandroid.model.frp.IniConfigParser
import com.github.jing332.frpandroid.util.FileUtils.readAllText

class ConfigViewModel : ViewModel() {
    //    val configList = mutableStateListOf<Item>()
    val groupedItems = mutableStateMapOf<Group, SnapshotStateList<Item>>()

    fun init(context: Context) {
        groupedItems.clear()

        val fullIni = context.assets.open("defaultData/frpc_full.ini").readAllText()
        val map = IniConfigParser.load(fullIni)
        map.toGrouped().forEach { entry ->
            groupedItems[entry.key] = groupedItems[entry.key] ?: SnapshotStateList()
            entry.value.forEach {
                groupedItems[entry.key]!!.add(it)
            }
        }
    }

    private fun HashMap<String, HashMap<String, String>>.toGrouped(): HashMap<Group, List<Item>> {
        val map = hashMapOf<Group, List<Item>>()
        this.forEach { sections ->
            val section = sections.key

            sections.value.forEach { kv ->
                val key = kv.key
                val value = kv.value
                val group = Group(section)
                map[group] = map[group] ?: mutableListOf()
                (map[group]!! as MutableList).add(Item(section, key, value))
            }
        }

        return map
    }

    data class Group(val section: String)

    data class Item(
        val section: String,
        val key: String,
        val value: String,
        val id: String = "${section}_${key}"
    )
}