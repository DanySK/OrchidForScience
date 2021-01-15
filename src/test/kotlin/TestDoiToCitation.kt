import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.danilopianini.orchidforscience.DoiToCitation

class TestDoiToCitation : FreeSpec(
    {
        fun execution(configuration: DoiToCitation.() -> Unit = {}): String = DoiToCitation().run {
            doi = "10.1057/jos.2012.27"
            style = "apa"
            type = "text/x-bibliography"
            configuration()
            return this()
        }
        "default settings should work" {
            execution() shouldBeIgnoringWhitespace """
                <a href="https://doi.org/10.1057/jos.2012.27">Pianini, D., Montagna, S., & Viroli, M. (2013).
                Chemical-oriented simulation of computational systems with ALCHEMIST.
                Journal of Simulation, 7(3), 202–215. doi:10.1057/jos.2012.27</a>
            """.trimIndent()
        }
        "links should be removable" {
            execution { link = false } shouldBeIgnoringWhitespace """
                Pianini, D., Montagna, S., & Viroli, M. (2013).
                Chemical-oriented simulation of computational systems with ALCHEMIST.
                Journal of Simulation, 7(3), 202–215. doi:10.1057/jos.2012.27
            """.trimIndent()
        }
        "bibtex should work" {
            execution { link = false; type = "bibtex" } shouldBeIgnoringWhitespace """
                @article{Pianini_2013,<br>
                 &nbsp;&nbsp;doi = {10.1057/jos.2012.27},<br>
                 &nbsp;&nbsp;url = {https://doi.org/10.1057%2Fjos.2012.27},<br>
                 &nbsp;&nbsp;year = 2013,<br>
                 &nbsp;&nbsp;month = {aug},<br>
                 &nbsp;&nbsp;publisher = {Informa {UK} Limited},<br>
                 &nbsp;&nbsp;volume = {7},<br>
                 &nbsp;&nbsp;number = {3},<br>
                 &nbsp;&nbsp;pages = {202--215},<br>
                 &nbsp;&nbsp;author = {D Pianini and S Montagna and M Viroli},<br>
                 &nbsp;&nbsp;title = {Chemical-oriented simulation of computational systems with {ALCHEMIST}},<br>
                 &nbsp;&nbsp;journal = {Journal of Simulation}<br>
                }
            """.trimIndent()
        }
    }

) {
    companion object {
        private val whitespace = "\\s".toRegex()
        private infix fun String.shouldBeIgnoringWhitespace(other: String) {
            this.replace(whitespace, "") shouldBe other.replace(whitespace, "")
        }
    }
}
