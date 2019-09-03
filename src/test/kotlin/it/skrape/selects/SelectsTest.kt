package it.skrape.selects

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import it.skrape.HttpBinSetup
import it.skrape.core.Request
import it.skrape.exceptions.ElementNotFoundException
import it.skrape.expect
import it.skrape.extract
import it.skrape.matchers.toBe
import it.skrape.matchers.toContain
import it.skrape.skrape
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class SelectsTest : HttpBinSetup() {

    @Test
    internal fun `will throw custom exception if element could not be found via element function`() {

        Assertions.assertThrows(ElementNotFoundException::class.java) {
            Request().expect {
                element(".nonExistent")
            }
        }
    }

    @Test
    internal fun `will throw custom exception if element could not be found via el function`() {

        Assertions.assertThrows(ElementNotFoundException::class.java) {
            Request().expect {
                el(".nonExistent")
            }
        }
    }

    @Test
    internal fun `can pick elements via select functions`() {

        val expectedValue = "Herman Melville - Moby-Dick"

        skrape {
            url = httpBin("html")
            extract {
                assertAll {
                    assertThat(el("h1").text()).isEqualTo(expectedValue)
                    assertThat(element("h1").text()).isEqualTo(expectedValue)
                    assertThat(elements("h1").first().text()).isEqualTo(expectedValue)
                    assertThat(`$`("h1").first().text()).isEqualTo(expectedValue)
                }

            }
        }
    }

    @Test
    internal fun `can pick certain header select functions`() {
        skrape {
            url = httpBin("html")
            headers = mapOf("Content-Type" to "text/html; charset=UTF-8")
            expect {
                header("Content-Type") toBe "text/html; charset=UTF-8"
                header("Content-Type") toContain "html"
                header("notExisting") toBe null
            }
        }
    }
}