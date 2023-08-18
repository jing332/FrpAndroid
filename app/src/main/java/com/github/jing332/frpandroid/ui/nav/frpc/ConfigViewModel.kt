package com.github.jing332.frpandroid.ui.nav.frpc

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jing332.frpandroid.model.frp.DocTableParser
import com.github.jing332.frpandroid.model.frp.IniConfigParser
import com.github.jing332.frpandroid.util.FileUtils.readAllText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConfigViewModel : ViewModel() {
    //    val configList = mutableStateListOf<Item>()
    val groupedItems = mutableStateMapOf<Group, SnapshotStateList<Item>>()

    var serverTables = listOf<DocTableParser.Table>()
    var clientTables = listOf<DocTableParser.Table>()

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

        viewModelScope.launch {
            serverTables = DocTableParser.parseTables(
                context.assets.open("docs/zh/server-configures.md").readAllText()
            )
        }

        viewModelScope.launch {
            clientTables = DocTableParser.parseTables(
                context.assets.open("docs/zh/client-configures.md").readAllText()
            )
        }
    }


    suspend fun findDocumentToMarkdown(name: String): String {
        while (clientTables.isEmpty() || serverTables.isEmpty()) {
            delay(500)
        }

        val sb = StringBuilder()
        fun addParameter(parameter: DocTableParser.Parameter) {
            sb.appendLine("参数： `${parameter.name}`")
            sb.appendLine()
            sb.appendLine("类型：`" + parameter.type + "`")
            sb.appendLine()
            sb.appendLine("默认：`" + parameter.defaultValue + "`")
            sb.appendLine()
            sb.appendLine("可选：`${parameter.optionalValues}`".replace("``", ""))
            sb.appendLine()
            sb.appendLine(parameter.description)
            sb.appendLine()
            sb.appendLine("> " + parameter.remark)
            sb.appendLine()
            sb.appendLine()
        }

        fun addTables(list: List<DocTableParser.Table>, type: String) {
            val ret = mutableListOf<DocTableParser.Parameter>()
            list.forEach { table ->
                table.parameters.forEach { parameter ->
                    if (parameter.name == name) {
                        ret.add(parameter)
                    }
                }
            }
            if (ret.isNotEmpty()) {
                sb.appendLine("## $type")
                sb.appendLine()
                for (v in ret) {
                    addParameter(v)
                }
            }
        }

        addTables(clientTables, "FRPC")
        addTables(serverTables, "FRPS")

        return sb.toString()
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