package com.github.jing332.frpandroid.model.frp

object DocTableParser {
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