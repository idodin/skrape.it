package it.skrape

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import it.skrape.core.Method
import it.skrape.core.Mode
import it.skrape.exceptions.ElementNotFoundException
import it.skrape.matchers.toBe
import it.skrape.selects.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.net.SocketTimeoutException

@Testcontainers
internal class DslTest : HttpBinSetup() {

    @Test
    internal fun `dsl can skrape by url`() {
        skrape {
            url = httpBin("html")

            expect {

                assertThat(statusCode).isEqualTo(200)
                assertThat(statusMessage).isEqualTo("OK")
                assertThat(contentType).isEqualTo("text/html; charset=utf-8")

                element("h1").text() toBe "Herman Melville - Moby-Dick"

                h1 {
                    assertThat(text()).isEqualTo("Herman Melville - Moby-Dick")
                }
            }
        }
    }

    @Test
    internal fun `dsl will not follow redirects if configured`() {
        skrape {
            url = httpBin("redirect/foo")
            followRedirects = false

            expect {
                assertThat(statusCode).isEqualTo(302)
            }
        }
    }

    @Test
    internal fun `dsl can check certain header`() {
        skrape {
            url = httpBin("html")
            expect {
                val header = header("Content-Type") {
                    assertThat(this).isEqualTo("text/html; charset=utf-8")
                }
                assertThat(header).isEqualTo("text/html; charset=utf-8")

                val nonExistingHeader = header("Non-Existing") {
                    assertThat(this).isNull()
                }
                assertThat(nonExistingHeader).isNull()
            }
        }
    }

    @Test
    internal fun `dsl can check headers`() {
        skrape {
            url = httpBin("html")
            expect {
                val headers = headers {
                    assertThat(this).contains("Content-Type", "text/html; charset=utf-8")
                }
                assertThat(headers).contains("Content-Type", "text/html; charset=utf-8")
            }
        }
    }

    @Test
    internal fun `dsl can get body`() {
        skrape {
            url = httpBin("html")
            expect {
                val body = body {
                    assertThat(this.text()).contains("Herman Melville - Moby-Dick")
                }
                assertThat(body.text()).contains("Herman Melville - Moby-Dick")
            }
        }
    }

    @Test
    internal fun `dsl will follow redirect by default`() {
        skrape {
            url = httpBin("redirect/foo")
            expect {
                assertThat(statusCode).isEqualTo(404)
            }
        }
    }

    @Test
    internal fun `dsl can fetch url and use HTTP verb POST`() {
        skrape {
            url = httpBin("/post")
            method = Method.POST

            expect {

                assertThat(request.method).isEqualTo(Method.POST)

                assertThat(statusCode).isEqualTo(200)
                assertThat(statusMessage).isEqualTo("OK")
                assertThat(contentType).isEqualTo("application/json; charset=UTF-8")
            }
        }
    }

    @Test
    internal fun `dsl will throw exception on timeout`() {
        Assertions.assertThrows(SocketTimeoutException::class.java) {
            skrape {
                url = httpBin("/delay/3")
                timeout = 2000
                expect {}
            }
        }
    }

    @Test
    internal fun `dsl can fetch url and infer type on extract`() {

        skrape {
            url = httpBin("html")

            val extracted = extract {
                MyObject(statusMessage, "", emptyList())
            }
            assertThat(extracted.message).isEqualTo("OK")
        }
    }

    @Test
    internal fun `dsl can fetch url and extract using it`() {

        val extracted = skrape {
            url = httpBin("html")

            extractIt<MyOtherObject> {
                it.message = statusMessage
            }
        }
        assertThat(extracted.message).isEqualTo("OK")
    }

    @Test
    internal fun `will throw custom exception if element could not be found via lambda`() {

        Assertions.assertThrows(ElementNotFoundException::class.java) {
            skrape {
                url = httpBin("html")
                expect {
                    element(".nonExistent") {}
                }
            }
        }
    }

    @Test
    internal fun `dsl can fetch url and extract from skrape`() {

        val extracted = skrape {
            url = httpBin("html")

            extract {
                MyObject(
                        message = statusMessage,
                        headline = element("h1").text(),
                        allParagraphs = elements("p").map { it.text() }
                )
            }
        }
        assertThat(extracted.message).isEqualTo("OK")
        assertThat(extracted.headline).isEqualTo("Herman Melville - Moby-Dick")
        assertThat(extracted.allParagraphs.size).isEqualTo(1)
        assertThat(extracted.allParagraphs.first())
                .contains("Availing himself of the mild, summer-cool weather that now reigned in these latitudes")
    }

    @Test
    internal fun `can read and return html from file system with default charset (UTF-8) using the DSL`() {
        val doc = skrape(File("src/test/resources/files/example.html")) {
            assertThat(title()).isEqualTo("i'm the title")
        }
        assertThat(doc.title()).isEqualTo("i'm the title")
    }

    @Test
    internal fun `can read and return html from file system using the DSL and non default charset`() {
        val doc = skrape(File("src/test/resources/files/example.html"), Charsets.ISO_8859_1) {
            assertThat(title()).isEqualTo("i'm the title")
        }
        assertThat(doc.title()).isEqualTo("i'm the title")
    }

    @Test
    internal fun `can read and return html from String`() {
        val doc = skrape("<html><head><title>i'm the title</title></head></html>") {
            assertThat(title()).isEqualTo("i'm the title")
        }
        assertThat(doc.title()).isEqualTo("i'm the title")
    }

    @Test
    internal fun `can read html from file system with default charset (UTF-8) using the DSL`() {
        expect(File("src/test/resources/files/example.html")) {
            assertThat(title()).isEqualTo("i'm the title")
        }
    }

    @Test
    internal fun `can read html from file system using the DSL and non default charset`() {
        expect(File("src/test/resources/files/example.html"), Charsets.ISO_8859_1) {
            assertThat(title()).isEqualTo("i'm the title")
        }
    }

    @Test
    internal fun `can read html from String`() {
        expect("<html><head><title>i'm the title</title></head></html>") {
            assertThat(title()).isEqualTo("i'm the title")
        }
    }

    @Test
    internal fun `can scrape js rendered page`() {
        skrape {
            mode = Mode.DOM
            url = httpBin("anything")
            println(fileContent("js.html"))
            requestBody = fileContent("js.html")
            expect {
                element("div.dynamic").text() toBe "I have been dynamically added via Javascript"
            }
        }
    }

    @Test
    internal fun `can send cookies with request in js rendering mode`() {
        skrape {
            mode = Mode.DOM
            url = httpBin("cookies")
            cookies = mapOf("myCookie" to "myCookieValue")
            expect {
                assertThat(body).contains("\"myCookie\": \"myCookieValue\"")
            }
        }
    }

    @Test
    internal fun `can send cookies with request in http mode`() {

        skrape {
            mode = Mode.SOURCE
            url = httpBin("cookies")
            cookies = mapOf("someCookie" to "someCookieValue")
            expect {
                assertThat(body).contains("\"someCookie\": \"someCookieValue\"")
            }
        }
    }
}
