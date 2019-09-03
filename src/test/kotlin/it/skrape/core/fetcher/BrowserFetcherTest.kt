package it.skrape.core.fetcher

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import com.gargoylesoftware.htmlunit.util.NameValuePair
import it.skrape.HttpBinSetup
import it.skrape.core.Method
import it.skrape.core.Request
import it.skrape.exceptions.UnsupportedRequestOptionException
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.net.SocketTimeoutException
import java.util.*

internal class BrowserFetcherTest : HttpBinSetup() {

    @Test
    internal fun `will fetch localhost 8080 with defaults if no params`() {
        assertThat(Request().url).isEqualTo("http://localhost:8080")
    }

    @Test
    internal fun `will use HTTP verb GET by default`() {
        assertThat(Request().method).isEqualTo(Method.GET)
    }

    @Test
    internal fun `will not throw exception on non existing url`() {
        // given
        val options = Request(url = "http://localhost:8080/not-existing")

        // when
        val fetched = BrowserFetcher(options).fetch()

        // then
        assertThat(fetched.statusCode).isEqualTo(404)
    }

    @Test
    internal fun `will not follow redirects if configured`() {
        val result = BrowserFetcher(Request(
                url = httpBin("/absolute-redirect/3"),
                followRedirects = false
        )).fetch()
        assertThat(result.statusCode).isEqualTo(302)
    }

    @Test
    internal fun `will follow redirect by default`() {
        val fetched = BrowserFetcher(Request(
                url = httpBin("/redirect-to?url=http%3A%2F%2Flocalhost%2Fredirect&status_code=200")
        )).fetch()
        assertThat(fetched.statusCode).isEqualTo(200)
    }

    @Test
    internal fun `will throw exception on HTTP verb POST`() {
        // when
        val options = Request(method = Method.POST)
        assertThat { BrowserFetcher(options).fetch() }.thrownError {
            hasClass(UnsupportedRequestOptionException::class)
        }
    }

    @Test
    internal fun `can parse js rendered elements`() {
        val fetched = BrowserFetcher(Request(
                url = httpBin("anything"),
                requestBody = fileContent("js.html"),
                headers = mapOf("Content-Type" to "text/html; charset=UTF-8")
        )).fetch()

        // then
        assertThat(fetched.document.select("div.dynamic").text()).isEqualTo("I have been dynamically added via Javascript")
    }

    @Test
    @Disabled
    internal fun `can parse js rendered elements from https page`() {
        // given
        //wireMockServer.setupStub(fileName = "js.html")
        // when
        val fetched = BrowserFetcher(Request(url = "https://localhost:8089")).fetch()

        // then
        assertThat(fetched.document.select("div.dynamic").text()).isEqualTo("I have been dynamically added via Javascript")
    }

    @Test
    internal fun `can parse es6 rendered elements from https page`() {
        val fetched = BrowserFetcher(Request(
                url = httpBin("anything"),
                requestBody = fileContent("es6.html"),
                headers = mapOf("Content-Type" to "text/html; charset=UTF-8")
        )).fetch()
        val paragraphs = fetched.document.select("div.dynamic")

        // then
        paragraphs.forEach {
            assertThat(it.text()).isEqualTo("dynamically added")
        }
    }

    @Test
    internal fun `can handle uri scheme`() {
        // given
        val aValidHtml = "<html><h1>headline</h1></html>"
        val base64encoded = Base64.getEncoder().encodeToString(aValidHtml.toByteArray())
        val uriScheme = "data:text/html;charset=utf-8;base64,$base64encoded"
        // when
        val fetched = BrowserFetcher(Request(url = uriScheme)).fetch()
        val headline = fetched.document.select("h1")

        // then
        assertThat(headline.text()).isEqualTo("headline")

    }

    @Test
    internal fun `will not throw if response body is not html`() {
        val response = BrowserFetcher(Request(
                url = httpBin("anything"),
                requestBody = fileContent("data.json"),
                headers = mapOf("Content-Type" to "application/json; charset=UTF-8")
        )).fetch()
        assertThat(response.body).isEqualTo("{\"data\":\"some value\"}")
    }

    @Test
    internal fun `will throw exception on timeout`() {

        assertThat { BrowserFetcher(Request(
                url = httpBin("delay/6")
        )).fetch() }.thrownError {
            // then
            hasClass(SocketTimeoutException::class)
        }
    }

    @Test
    internal fun `will extract headers to map`() {

        val sut = listOf(
                NameValuePair("first-name", "first-value"),
                NameValuePair("second-name", "second-value")
        )
        val result = sut.toMap()
        assertThat(result).containsOnly("first-name" to "first-value", "second-name" to "second-value")
    }

    @Test
    internal fun `will create raw cookie syntax representation of map`() {

        val sut = mapOf(
                "first-name" to "first-value",
                "second-name" to "second-value"
        )
        val result = sut.asRawCookieSyntax()
        assertThat(result).isEqualTo("first-name=first-value;second-name=second-value;")
    }


}