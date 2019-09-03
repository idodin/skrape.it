package it.skrape.core.fetcher

import assertk.assertThat
import assertk.assertions.isEqualTo
import it.skrape.HttpBinSetup
import it.skrape.core.Method
import it.skrape.core.Request
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.net.SocketTimeoutException

internal class HttpFetcherTest : HttpBinSetup() {

    @Test
    internal fun `will fetch localhost 8080 with defaults if no params`() {
        val fetched = HttpFetcher(Request()).fetch()
        assertThat(fetched.request).isEqualTo("http://localhost:8080")
    }

    @Test
    internal fun `can fetch url and use HTTP verb GET by default`() {
        // given
        val options = Request(
                url = httpBin("anything")
        )

        // when
        val fetched = HttpFetcher(options).fetch()

        // then
        assertThat(fetched.statusCode).isEqualTo(200)
        assertThat(fetched.contentType).isEqualTo("text/html; charset=UTF-8")
        assertThat(fetched.document.title()).isEqualTo("i'm the title")
    }

    @Test
    internal fun `will not throw exception on non existing url`() {
        // given
        val options = Request(url = "http://localhost:8080/not-existing")

        // when
        val fetched = HttpFetcher(options).fetch()

        // then
        assertThat(fetched.statusCode).isEqualTo(404)
    }

    @Test
    internal fun `will not follow redirects if configured`() {
        val result = HttpFetcher(Request(
                url = httpBin("/redirect"),
                followRedirects = false
        )).fetch()
        assertThat(result.statusCode).isEqualTo(302)
    }

    @Test
    internal fun `will follow redirect by default`() {
        val fetched = HttpFetcher(Request(
                url = httpBin("/redirect")
        )).fetch()
        assertThat(fetched.statusCode).isEqualTo(200)
    }

    @Test
    internal fun `can fetch url and use HTTP verb POST`() {
        // given
        val options = Request(
                url = httpBin("/anything"),
                method = Method.POST,
                requestBody = """{"data":"some value"}"""
        )

        // when
        val fetched = HttpFetcher(options).fetch()

        // then
        assertThat(fetched.statusCode).isEqualTo(200)
        assertThat(fetched.contentType).isEqualTo("application/json; charset=UTF-8")
        assertThat(fetched.body).isEqualTo("""{"data":"some value"}""")
    }

    @Test
    internal fun `will throw exception on timeout`() {
        assertThrows(SocketTimeoutException::class.java) {
            HttpFetcher(Request(
                    url = httpBin("/delay/6")
            )).fetch()
        }
    }
}
