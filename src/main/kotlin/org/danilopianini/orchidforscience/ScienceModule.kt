package org.danilopianini.orchidforscience

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.addToSet
import khttp.get

class ScienceModule : OrchidModule() {

    override fun configure() {
        withResources(20)
        addToSet<TemplateTag>(YouTubeTag::class)
    }
}

fun main() {
    println("ciao")
    println(get(
        "https://doi.org/10.1126/science.169.3946.635",
        headers = mapOf("Accept" to "text/x-bibliography;q=1.0; style=apa")
    ).content.decodeToString())
//    KHTTO
//    HttpClient().use {
//        runBlocking {
//            println(it.get<String>("https://doi.org/10.1126/science.169.3946.635") {
//                header("Accept", "text/x-bibliography;q=1.0; style=harvard3")
//                headers {
//                    clear()
//                    appendAll("My-Custom-Header", listOf("HeaderValue1", "HeaderValue2"))
//                }
//            })
//        }
//    }
//    println("Done")
//    val wiki = GlobalScope.async {
//        val htmlContent = client.get<String>("https://en.wikipedia.org/wiki/Main_Page")
//        println(htmlContent)
//        println(client.get<String>("https://doi.org/10.1126/science.169.3946.635"))
//        client.get<String>("https://en.wikipedia.org/wiki/Main_Page")
//    }
//    client.close()
//    println(wiki.asCompletableFuture().get())
}
