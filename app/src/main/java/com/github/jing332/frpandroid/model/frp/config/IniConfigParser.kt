package com.github.jing332.frpandroid.model.frp.config

typealias IniKV = LinkedHashMap<String, String>
typealias IniConfig = LinkedHashMap<String, IniKV>

object IniConfigParser {
    private fun matchSection(line: String): String {
        val sectionMatch = Regex("\\[(.*)\\]").find(line)
        if (sectionMatch != null) {
            return sectionMatch.groupValues[1].trim()
        }
        return ""
    }

    private fun matchKeyValue(line: String): Pair<String, String> {
        val keyValueMatch = Regex("(.*)=(.*)").find(line)
        if (keyValueMatch != null) {
            val key = keyValueMatch.groupValues[1]
            val value = keyValueMatch.groupValues[2]
            return Pair(key.trim(), value.trim())
        }
        return Pair("", "")
    }

    fun loadFromString(str: String): IniConfig {
        val map = IniConfig()
        var currentSection = ""
        for (line in str.lines()) {
            if (line.startsWith("#")) continue

            val section = matchSection(line)
            if (section.isNotEmpty()) {
                currentSection = section
                map[section] = IniKV()
            }

            val kv = matchKeyValue(line)
            if (kv.first.isNotEmpty() && kv.second.isNotEmpty()) {
                map[currentSection]?.set(kv.first, kv.second)
            }

        }

        return map
    }

    fun load(str: String): LinkedHashMap<String, LinkedHashMap<String, String>> {
        return loadFromString(str)
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
                if (currentSection.isNotEmpty() && !isEdited && currentSection != s) {
                    sb.appendLine("$key = $value")
                }
                currentSection = s
                sb.appendLine(line)
                continue
            }

            val kv = matchKeyValue(line)
            if (kv.first.isNotEmpty() && kv.second.isNotEmpty()) {
                if (currentSection == section && kv.first == key) {
                    isEdited = true
                    sb.appendLine("$key = $value")
                    continue
                }
            }

            sb.appendLine(line)
        }

        return sb.toString()
    }
}