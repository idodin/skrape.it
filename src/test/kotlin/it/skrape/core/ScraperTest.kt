package it.skrape.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import it.skrape.HttpBinSetup
import it.skrape.selects.element
import org.junit.jupiter.api.Test

internal class ScraperTest : HttpBinSetup() {

    @Test
    internal fun `can scrape html via custom http request`() {
        val result = Scraper(request = Request(url = httpBin("html"))).scrape()

        assertThat(result.statusCode).isEqualTo(200)
        assertThat(result.document.element("h1")).isEqualTo("Herman Melville - Moby-Dick")
    }
}
