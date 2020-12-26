//import com.eden.orchid.api.OrchidContext
//import com.eden.orchid.api.compilers.TemplateFunction
//import com.eden.orchid.api.converters.StringConverter
//import com.eden.orchid.api.options.annotations.Description
//import com.eden.orchid.api.options.annotations.Option
//import com.eden.orchid.api.theme.pages.OrchidPage
//import com.eden.orchid.utilities.encodeSpaces
//import io.ktor.client.HttpClient
//import io.ktor.client.request.get
//import io.ktor.client.request.header
//import kotlinx.coroutines.runBlocking
//
//@Description("Encode space characters as `&nbsp;` to preserve indentation.", name = "Encode Spaces")
//class EncodeSpacesFunction : TemplateFunction("encodeSpaces", true) {
//
//    @Option
//    @Description("The input to encode.")
//    lateinit var input: String
//
//    override fun parameters(): Array<String> {
//        return arrayOf("input")
//    }
//
//    override fun apply(): Any? = Unit
//
//}
//
//enum class ContentType(val text: String) {
//    RDF_XML("application/rdf+xml")
//}
//
//fun request(doi: String, type: String, style: String? = null): String = HttpClient().use {
//        runBlocking {
//            it.get<String>("https://doi.org/$doi") {
//                header("Accept", "text/x-bibliography; style=${style ?: ""}")
//            }
//        }
//    }
