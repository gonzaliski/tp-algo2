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
    val itinerarioDeOtroUsuario = Itinerario(
        creador = otroUsuario,
        destino = laPaz,
        dias = mutableListOf(
            Itinerario.DiaDeItinerario(actividadesBasicas)
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

    describe("Dado un usuario Relajado y un itinerario...") {
        it("con pocos dias, puede realizarlo") {
            // Act - Given
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }

        it("con muchos dias, NO puede realizarlo") {
            // Act - Given
            val dias = mutableListOf<Itinerario.DiaDeItinerario>()
            repeat(usuario.diasDisponibles + 1) {
                dias.add(Itinerario.DiaDeItinerario(actividadesBasicas))
            }
            itinerarioDeOtroUsuario.dias = dias

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }

        it("con igual cantidad de dias, puede realizarlo") {
            // Act - Given
            val dias = mutableListOf<Itinerario.DiaDeItinerario>()
            repeat(usuario.diasDisponibles) {
                dias.add(Itinerario.DiaDeItinerario(actividadesBasicas))
            }
            itinerarioDeOtroUsuario.dias = dias

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }
    }

    describe("Dado un Usuario Precavido y un Itinerario...") {
        usuario.criterio = Precavido(usuario)

        it("con destino conocido, puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.destino = usuario.destinosDeseados.first()

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }

        it("con destino desconocido, NO puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.destino = laPaz

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }

        it("con destino que conoce un amigo, puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.destino = laPaz
            otroUsuario.destinosVisitados.add(laPaz)
            usuario.amigos.add(otroUsuario)

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }
    }

    describe("Dado un Usuario Localista y un Itinerario...") {
        usuario.criterio = Localista
        val local = Destino(ciudad = "asd", pais = Destino.LOCAL, costoBase = 123_000.0)

        it("con destino local, puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.destino = local

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }

        it("con destino no local, NO puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.destino = laPaz

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }
    }

    describe("Dado un Usuario Soniador y un Itinerario...") {
        usuario.criterio = Soniador(usuario)

        it("con destino deseado, puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.destino = usuario.destinosDeseados.first()

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }

        it("con destino no deseado barato, NO puede realizar el itinerario") {
            // Act - When
            usuario.destinosDeseados = mutableListOf(roma)
            laPaz.costoBase = roma.costoBase - 1.0
            itinerarioDeOtroUsuario.destino = laPaz

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }

        it("con destino no deseado caro, puede realizar el itinerario") {
            // Act - When
            usuario.destinosDeseados = mutableListOf(roma)
            laPaz.costoBase = roma.costoBase + 1.0
            itinerarioDeOtroUsuario.destino = laPaz

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }
    }
})

