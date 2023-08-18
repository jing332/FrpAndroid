package com.github.jing332.frpandroid.model.frp

import android.content.Context
import com.github.jing332.frpandroid.util.FileUtils.readAllText
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DocumentTableManager {
    val serverTables = mutableListOf<Table>()
    val clientTables = mutableListOf<Table>()
    val proxyTables = mutableListOf<Table>()
    val visitorTables = mutableListOf<Table>()

    suspend fun load(context: Context) = coroutineScope {
        fun mdString(path: String) =
            context.assets.open(path).readAllText()

        fun loadTables(mutableList: MutableList<Table>, path: String) {
            launch {
                mutableList.clear()
                mutableList.addAll(parseTables(mdString(path)))
            }
        }

        loadTables(serverTables, "docs/zh/server-configures.md")
        loadTables(clientTables, "docs/zh/client-configures.md")
        loadTables(proxyTables, "docs/zh/proxy.md")
        loadTables(visitorTables, "docs/zh/visitor.md")
    }

    suspend fun findDocumentToMarkdown(name: String): String {
        while (clientTables.isEmpty() || serverTables.isEmpty()) {
            delay(500)
        }

        fun kv(key: String, value: String) = "${key}：`${value}`".replace("``", "无")

        val sb = StringBuilder()
        fun addParameter(parameter: DocumentTableManager.Parameter) {
            sb.appendLine("参数： `${parameter.name}`")
            sb.appendLine()
            sb.appendLine("类型：`" + parameter.type + "`")
            sb.appendLine()
            sb.appendLine(kv("默认", parameter.defaultValue))
            sb.appendLine()
            sb.appendLine(kv("可选", parameter.optionalValues))
            sb.appendLine()
            sb.appendLine("> " + parameter.description)
            sb.appendLine()
            sb.appendLine("> " + parameter.remark)
            sb.appendLine()
            sb.appendLine()
        }

        fun addTables(list: List<DocumentTableManager.Table>, type: String) {
            val ret = mutableListOf<DocumentTableManager.Parameter>()
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

        addTables(clientTables, "Client")
        addTables(serverTables, "Server")
        addTables(proxyTables, "Proxy")
        addTables(visitorTables, "Visitor")

        return sb.toString()
    }


    data class Table(
        val title: String,
        val parameters: List<Parameter>
    )

    data class Parameter(
        val name: String,
        val type: String,
        val description: String,
        val defaultValue: String,
        val optionalValues: String,
        val remark: String
    )

    fun parseTables(tableString: String): List<Table> {
        val lines = tableString.trim().lines()
        val tables = mutableListOf<Table>()

        var currentTableTitle = ""
        var currentTableRows = mutableListOf<Parameter>()
        for (line in lines) {
            if (line.startsWith("##")) {
                if (currentTableTitle.isNotEmpty()) {
                    tables.add(Table(currentTableTitle, currentTableRows))
                    currentTableRows = mutableListOf()
                }
                currentTableTitle = line.trimStart { it == '#' }
            } else if (line.startsWith("|")) {
                val values = line.trim('|').split("|").map { it.trim() }
                if (values.size >= 6) {
                    val parameter = Parameter(
                        name = values[0],
                        type = values[1],
                        description = values[2],
                        defaultValue = values[3],
                        optionalValues = values[4],
                        remark = values[5]
                    )

                    // Ignore row:
                    // name=参数, type=类型, description=说明, defaultValue=默认值, optionalValues=可选值, remark=备注
                    // name=:---, type=:---, description=:---, defaultValue=:---, optionalValues=:---, remark=:---
                    if (parameter.name.endsWith("---") && parameter.type.endsWith("---")) {
                        currentTableRows.removeAt(currentTableRows.size - 1)
                        continue
                    }

                    currentTableRows.add(parameter)
                }
            }
        }

        if (currentTableTitle.isNotEmpty())
            tables.add(Table(currentTableTitle, currentTableRows))

        return tables
    }


}