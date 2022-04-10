package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate
import java.time.LocalTime

class UsuarioSpec : DescribeSpec({
    val actividadesBasicas = mutableListOf(
        Actividad(
            dificultad = Actividad.Dificultad.BAJA,
            descripcion = "asdjasdja",
            inicio = LocalTime.of(12, 30, 0, 0),
            fin = LocalTime.of(14, 30, 0, 0),
            costo = 0.0

        ), Actividad(
            dificultad = Actividad.Dificultad.MEDIA,
            descripcion = "asdjasdjsa",
            inicio = LocalTime.of(15, 30, 0, 0),
            fin = LocalTime.of(17, 30, 0, 0),
            costo = 0.0
        )
    )
    val roma = Destino(
        pais = "Italia", ciudad = "Roma", costoBase = 3.0
    )
    val cucurun = Destino(
        pais = "Runcuncun", ciudad = "Cucurun", costoBase = 3.0
    )
    val santiagoDeChile = Destino(
        pais = "Chile", ciudad = "Santiago", costoBase = 3.0
    )
    val laPaz = Destino(
        pais = "Bolivia", ciudad = "La Paz", costoBase = 3.0
    )
    val usuario = Usuario(
        nombre = "Pepe",
        apellido = "Peter",
        username = "ppeter",
        fechaAlta = LocalDate.now(),
        paisResidencia = "Venezuela",
        diasDisponibles = 15,
        criterio = Relajado,
        destinosDeseados = mutableListOf(
            roma, cucurun
        )
    ).apply {
        destinosVisitados = mutableListOf(
            santiagoDeChile
        )
    }
    val otroUsuario = Usuario(
        nombre = "Carlos",
        apellido = "Gomez",
        username = "cgomez",
        fechaAlta = LocalDate.now().minusYears(16),
        paisResidencia = "Chile",
        diasDisponibles = 1,
        criterio = Relajado,
        destinosDeseados = mutableListOf(
            roma, cucurun
        )
    )

    it("Puede puntuar itinerario de otro cuyo destino conoce") {
        val itinerario = Itinerario(
            creador = otroUsuario,
            destino = usuario.destinosDeseados.first(),
            dias = mutableListOf(
                Itinerario.DiaDeItinerario(actividadesBasicas)
            )
        )

        usuario.puedePuntuar(itinerario).shouldBeTrue()
    }

    it("No puede puntuar porque es el creador") {
        val itinerario = Itinerario(
            creador = usuario,
            destino = usuario.destinosDeseados.first(),
            dias = mutableListOf(
                Itinerario.DiaDeItinerario(actividadesBasicas)
            )
        )

        usuario.puedePuntuar(itinerario).shouldBeFalse()
    }

    it("No puede puntuar porque no conoce el destino") {
        val itinerario = Itinerario(
            creador = otroUsuario,
            destino = laPaz,
            dias = mutableListOf(
                Itinerario.DiaDeItinerario(actividadesBasicas)
            )
        )

        usuario.puedePuntuar(itinerario).shouldBeFalse()
    }

})

