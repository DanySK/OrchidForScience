package org.danilopianini.orchidforscience

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.addToSet

/**
 * This class can be injected in an Orchid website. Enables this module features.
 */
class ScienceModule : OrchidModule() {

    override fun configure() {
        addToSet<TemplateFunction>(DoiToCitation::class)
    }
}
