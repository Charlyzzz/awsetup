typealias Section = Pair<String, Map<String, String>>

private data class ParseResult(val currentSection: Section?, val sections: List<Section>) {
    fun newSection(sectionName: String): ParseResult {
        var result = this
        currentSection?.also { result = result.copy(sections = sections + it) }
        return result.copy(currentSection = Section(sectionName, emptyMap()))
    }

    fun addProperty(key: String, value: String): ParseResult {
        val section = currentSection ?: error("property without section")
        return copy(
            currentSection = section.copy(second = section.second + (key to value))
        )
    }

    fun done(): List<Section> {
        var sections = sections
        currentSection?.also { sections = sections + it }
        return sections
    }

    companion object {
        val empty = ParseResult(null, emptyList())
    }
}

fun parseIni(fileContent: String): List<Section> {
    val sectionRegex = Regex("""\[(.+)]""")
    val propertyRegex = Regex("""(.+)\s*=\s*(.+)""")
    return fileContent.lineSequence().fold(ParseResult.empty) { resultSoFar, line ->
        if (line.isEmpty())
            return@fold resultSoFar
        sectionRegex.matchEntire(line)?.let {
            val (sectionName) = it.destructured
            resultSoFar.newSection(sectionName)
        } ?: propertyRegex.matchEntire(line)?.let {
            val (key, value) = it.destructured
            resultSoFar.addProperty(key, value)
        } ?: error("line match failed")
    }.done()
}


