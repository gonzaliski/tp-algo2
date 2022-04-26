package edu.unsam.algo2

import com.google.gson.GsonBuilder
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ServiceDestinoSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val destinos = mutableListOf(
        Destino(
            pais = "Inglaterra",
            ciudad = "Londres",
            costoBase = 60000.0
        ),
        Destino(
            pais = "Estados Unidos",
            ciudad = "New York",
            costoBase = 5000.00
        )
    )
    val mockedServiceDestino = mockServiceDestino(destinos)
    val repo = Repositorio<Destino>()
    val actualizador = ActualizadorDestino(repo).apply { serviceDestino = mockedServiceDestino }

    it("Al actualizar el repositorio, se hace una llamada al servicio") {
        // Act - When
        actualizador.updateDestinos()

        // Assert - Then
        verify(exactly = 1) { mockedServiceDestino.getDestinos() }
        repo.elementos.shouldNotBeEmpty()
        repo.existenTodos(destinos)
    }
    it("Al recibir objetos con ID, son actualizados sus datos") {
        // Act - When
        destinos.add(
            Destino(
                pais = "Brasil",
                ciudad = "Florianopolis",
                costoBase = 30000.0
            ).apply { id = 1 }
        )
        actualizador.updateDestinos()
        // Assert - Then
        verify(exactly = 1) { mockedServiceDestino.getDestinos() }
        repo.elementos.shouldNotBeEmpty()
        repo.existenTodos(destinos)
    }
})

fun mockServiceDestino(destinos: MutableList<Destino>): ServiceDestino {
    val service = mockk<ServiceDestino>(relaxUnitFun = true)
    val gson = GsonBuilder().setPrettyPrinting().create()

    every { service.getDestinos() } answers { gson.toJson(destinos) }

    return service
}
