package com.github.jing332.frpandroid.model.frp

object IniConfigParser {
    private fun matchSection(line: String): String {
        val sectionMatch = Regex("\\[(.*)\\]").find(line)
        if (sectionMatch != null) {
            return sectionMatch.groupValues[1]
        }
        return ""
    }

    private fun matchKeyValue(line: String): Pair<String, String> {
        val keyValueMatch = Regex("(.*)=(.*)").find(line)
        if (keyValueMatch != null) {
            val key = keyValueMatch.groupValues[1]
            val value = keyValueMatch.groupValues[2]
            return Pair(key, value)
        }
        return Pair("", "")
    }

    fun load(str: String): HashMap<String, HashMap<String, String>> {
        val map = HashMap<String, HashMap<String, String>>()
        var currentSection = ""
        for (line in str.lines()) {
            if (line.startsWith("#")) continue

            val section = matchSection(line)
            if (section.isNotEmpty()) {
                currentSection = section.trim()
                map[section] = HashMap()
            }

            val kv = matchKeyValue(line)
            if (kv.first.isNotEmpty() && kv.second.isNotEmpty()) {
                map[currentSection]?.set(kv.first.trim(), kv.second.trim())
            }

        }

        return map
    }

    fun edit(str: String, section: String, key: String, value: String): String {
        val sb = StringBuilder()

        var isEdited = false
        var currentSection = ""
        for (line in str.lines()) {
            if (line.startsWith("#")) {
                sb.appendLine(line)
                continue
            }

            val s = matchSection(line)
            if (s.isNotEmpty()) {
                if (!isEdited && currentSection == section) {
                    sb.appendLine("$key=$value")
                }
                currentSection = s
                sb.appendLine(line)
                continue
            }

            val kv = matchKeyValue(line)
            if (kv.first.isNotEmpty() && kv.second.isNotEmpty()) {
                if (currentSection == section && kv.first == key) {
                    isEdited = true
                    sb.appendLine("$key=$value")
                    continue
                }
            }

            sb.appendLine(line)
        }

        return sb.toString()
    }
}