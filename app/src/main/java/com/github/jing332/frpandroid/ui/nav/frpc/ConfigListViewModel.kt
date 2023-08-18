package com.github.jing332.frpandroid.ui.nav.frpc

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jing332.frpandroid.model.frp.DocumentTableManager
import com.github.jing332.frpandroid.model.frp.config.IniConfig
import com.github.jing332.frpandroid.model.frp.config.IniConfigManager
import com.github.jing332.frpandroid.model.frp.config.IniConfigParser
import com.github.jing332.frpandroid.util.FileUtils.readAllText
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class ConfigListViewModel : ViewModel() {
    private val groupedItems = mutableStateListOf<Pair<Group, List<Item>>>()
    val filteredItems = mutableStateListOf<Pair<Group, SnapshotStateList<Item>>>()
    val cfgManager = IniConfigManager()

    private fun loadDocument(context: Context) {
        viewModelScope.launch {
            DocumentTableManager.load(context)
        }
    }

    fun initFromIniString(context: Context, iniStr: String) {
        loadDocument(context)

        val fullIni = context.assets.open("defaultData/frpc_full.ini").readAllText()
        val map = IniConfigParser.load(fullIni)
        map.setToModels()
    }

    fun initFromFile(context: Context, iniFilePath: String) {
        loadDocument(context)

        cfgManager.iniFilePath = iniFilePath
        viewModelScope.launch {
            cfgManager.flowConfig().conflate().collect {
                it.setToModels()
            }
        }
    }

    fun saveConfig(section: String, key: String, value: String) {
        cfgManager.edit(section, key, value)
    }

    private fun IniConfig.setToModels() {
        groupedItems.clear()
        toGrouped().forEach { entry ->
            val list = mutableStateListOf<Item>()
            groupedItems.add(Pair(entry.key, list))
            entry.value.forEach {
                list.add(it)
            }
        }

        filter()
    }

    suspend fun findDocumentToMarkdown(name: String): String {
        return DocumentTableManager.findDocumentToMarkdown(name)
    }

    private fun IniConfig.toGrouped(): HashMap<Group, List<Item>> {
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

    fun filter(searchKey: String = "") {
        filteredItems.clear()

        fun filter(onFilter: (Item) -> Boolean) {
            for (item in groupedItems) {
                val subList = mutableStateListOf<Item>()
                filteredItems.add(Pair(item.first, subList))
                item.second.forEach {
                    if (onFilter(it)) subList.add(it)
                }

                if (subList.isEmpty())
                    filteredItems.removeAt(filteredItems.size - 1)
            }
        }

        if (searchKey.isBlank())
            filter { true }
        else
            filter {
                it.key.contains(searchKey, true) || it.value.contains(searchKey, true)
            }
    }

    data class Group(val section: String)

    data class Item(
        val section: String,
        val key: String,
        val value: String,
        val id: String = "${section}_${key}"
    )
}