package it.skrape

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

class MyObject(var message: String? = null, var headline: String = "", var allParagraphs: List<String> = emptyList())

class MyOtherObject {
    var message: String? = null
}

class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

@Testcontainers
open class HttpBinSetup {

    @Container
    private val httpBin = KGenericContainer("kennethreitz/httpbin")
            .withExposedPorts(80)
            .waitingFor(Wait.forHttp("/").forStatusCode(200))

    fun httpBin(option: String = "html") =
            "http://localhost:${httpBin.firstMappedPort}/$option"

    fun fileContent(fileName: String) = this::class.java.getResource("files/" + fileName).readText(Charsets.UTF_8)
}

