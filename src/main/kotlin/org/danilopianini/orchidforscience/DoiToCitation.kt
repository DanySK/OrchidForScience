package org.danilopianini.orchidforscience

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import org.apache.commons.text.StringEscapeUtils

/**
 * Given a [doi], prints an HTML citation.
 * Several [style]s are supported, see the DOI web service. Defaults to APA style.
 * Supports generating HTML-friendly bibtex and other [type]s of data.
 *
 * Usage examples:
 *
 * ```markdown
 * {{ cite('10.1057/jos.2012.27') }}
 * {{ cite doi = '10.1057/jos.2012.27' }}
 * {{ cite doi = '10.1057/jos.2012.27' style = 'harvard3' }}
 * {{ cite doi = '10.1057/jos.2012.27' type = 'bibtex' }}
 * ```
 */
@Description("Mutates a DOI into a citation string", name = "DOI to citation")
class DoiToCitation : TemplateFunction("cite", true) {

    /**
     * The digital object identifier.
     */
    @Option
    @Description("The DOI")
    lateinit var doi: String

    /**
     * Citation style, used in case of formatted citation.
     */
    @Option
    @Description("The Citation format to be used if type is text/x-bibliography. Defaults to apa")
    @StringDefault("apa")
    lateinit var style: String

    /**
     * Citation type. Please refer to the DOI citation formatting web service.
     * Defaults to `text/x-bibliography`, bibtex is supported also with a shorthand: `bibtex`
     */
    @Option
    @Description("The Citation type. Defaults to text/x-bibliography")
    @StringDefault("text/x-bibliography")
    lateinit var type: String

    /**
     * If true, the citation is wrapped in a link to `https://doi.org/[doi]`.
     */
    @Option
    @Description("Whether to link the citation")
    @BooleanDefault(true)
    var link: Boolean = true

    override fun parameters(): Array<String> {
        return arrayOf("input")
    }

    override fun apply(context: OrchidContext?, page: OrchidPage?): String = this()

    /**
     * Zero-argument version of [apply], to ease testing.
     */
    operator fun invoke(): String {
        val realType = when (type.toLowerCase()) {
            "bibtex" -> "application/x-bibtex"
            "bibliography" -> "text/x-bibliography"
            else -> type
        }
        val citation = khttp.get("https://doi.org/$doi", headers = mapOf("Accept" to "$realType; style=$style"))
            .content.decodeToString()
            .apply(StringEscapeUtils::escapeHtml4)
            .replace("(?:\\n|\\r\\n)".toRegex(), if ("bibtex" in realType) "<br>\n" else "")
            .let { if ("bibtex" in realType) it.replace("\\n\\s".toRegex(), "\n &nbsp;&nbsp;") else it }
        return if (link) {
            "<a href=\"https://doi.org/$doi\">$citation</a>"
        } else {
            citation
        }
    }
}
