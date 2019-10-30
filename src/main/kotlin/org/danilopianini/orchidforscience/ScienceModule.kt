package org.danilopianini.orchidforscience

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.utilities.nl2br
import java.time.format.DateTimeParseException
import java.util.Locale

class ScienceModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        // addToSet<TemplateFunction>()
        addToSet<TemplateTag>(YouTubeTag::class)
    }
}

private typealias AspectRatio = Pair<Int, Int>
val AspectRatio.width
    get() = component1()
val AspectRatio.height
    get() = component2()
val AspectRatio.asPercent
    get() = "%.2f".format(Locale.ENGLISH, height.toDouble() / width.toDouble() * 100)

private const val tagname = "ytvideo"

@Description("Embed a YouTube video in your page.", name = tagname)
class YouTubeTag : TemplateTag(tagname, Type.Simple, true) {

    @Option
    @Description("The Youtube video Id.")
    lateinit var id: String

    @Option
    @Description("The start time of the video, in MM:SS format.")
    @StringDefault("0:0")
    lateinit var start: String

    @Option
    @Description("The aspect ratio of the video in W:L, defaults to 16:9.")
    @StringDefault("16:9")
    lateinit var aspectRatio: String

    override fun parameters(): Array<String> {
        return arrayOf("id", "start")
    }

    fun getStartSeconds(): Int {
        if(start.isNotBlank() && start.contains(":")) {
            try {
                val time = start.split(":")
                if(time.size == 2) {
                    return (Integer.parseInt(time[0]) * (60)) + (Integer.parseInt(time[1]))
                }
                else if(time.size == 3) {
                    return (Integer.parseInt(time[0]) * (60*60)) + (Integer.parseInt(time[1]) * (60)) + (Integer.parseInt(time[2]))
                }
            } catch (e: DateTimeParseException) {
                Clog.e(e.message, e)
            }
        }
        return 0
    }

    fun getAspectRatioPercent(): String = (
            ratio.matchEntire(aspectRatio)
                ?.let { AspectRatio(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
                ?: defaultAspectRatio
        ).asPercent

    companion object {
        val ratio: Regex = Regex("""\s*(\d*)\s*:\s*(\d*)\s*""")
        val defaultAspectRatio = AspectRatio(16, 9)
    }
}