package edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ServiceDestinoSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val destinosIniciales = mutableListOf(
        Destino(
            pais = "Argentina",
            ciudad = "MDQ",
            costoBase = 1.0
        ),
        Destino(
            pais = "Brasil",
            ciudad = "rio",
            costoBase = 2.0
        )
    )
    val destinos = mutableListOf(
        Destino(
            pais = "Argentina",
            ciudad = "Mar del Plata",
            costoBase = 10000.0
        ),
        Destino(
            pais = "Brasil",
            ciudad = "Rio de Janeiro",
            costoBase = 20000.0
        ),
        Destino(
            pais = "Indonesia",
            ciudad = "Bali",
            costoBase = 30000.0
        )
    )
    val mockedServiceDestino = mockServiceDestino()
    val repo = Repositorio<Destino>().apply {
        destinosIniciales.forEach{ this.create(it) }
    }
    val actualizador = ActualizadorDestino(repo, serviceDestino = mockedServiceDestino)

    it("Al actualizar el repositorio, se hace una llamada al servicio") {
        // Act - When
        actualizador.updateDestinos()

        // Assert - Then
        verify(exactly = 1) { mockedServiceDestino.getDestinos() }
        repo.elementos.shouldNotBeEmpty()
    }
    it("Al recibir objetos con ID, son actualizados sus datos") {
        actualizador.updateDestinos()
        // Assert - Then
        destinos.shouldContainAll(repo.elementos)
        repo.elementos.shouldNotContain(destinosIniciales)
    }
})

fun mockServiceDestino(): ServiceDestino {
    val service = mockk<ServiceDestino>(relaxUnitFun = true)
    val json = """
 [
    {
        "id": 1,
        "pais": "Argentina",
        "ciudad": "Mar del Plata",
        "costo": 10000
    }, 
    {
        "id": 2,
        "pais": "Brasil",
        "ciudad": "Rio de Janeiro",
        "costo": 20000
    },
    {
        "pais": "Indonesia",
        "ciudad": "Bali",
        "costo": 30000
    }
]
"""

    every { service.getDestinos() } answers { json }

    return service
}
