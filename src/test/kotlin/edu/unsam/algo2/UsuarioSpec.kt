package edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate
import java.time.LocalTime

class UsuarioSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val actividadesBasicas = mutableListOf(
        Actividad(
            dificultad = Dificultad.BAJA,
            descripcion = "asdjasdja",
            inicio = LocalTime.of(12, 30, 0, 0),
            fin = LocalTime.of(14, 30, 0, 0),
            costo = 0.0

        ), Actividad(
            dificultad = Dificultad.MEDIA,
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
        email = "pepe.peter@mail.com",
        fechaAlta = LocalDate.now(),
        paisResidencia = "Venezuela",
        diasDisponibles = 15,
        criterio = Relajado,
        destinosDeseados = mutableListOf(
            roma, cucurun
        ),
        vehiculoPreferencia = Caprichoso


    ).apply {
        destinosVisitados = mutableListOf(
            santiagoDeChile
        )
    }
    val otroUsuario = Usuario(
        nombre = "Carlos",
        apellido = "Gomez",
        username = "cgomez",
        email = "carlos.gomez@mail.com",
        fechaAlta = LocalDate.now().minusYears(16),
        paisResidencia = "Chile",
        diasDisponibles = 1,
        criterio = Relajado,
        destinosDeseados = mutableListOf(
            roma, cucurun
        ),
        vehiculoPreferencia = Caprichoso
    )
    val itinerarioDeOtroUsuario = Itinerario(
        creador = otroUsuario,
        destino = laPaz,
        dias = mutableListOf(
            DiaDeItinerario(actividadesBasicas)
        )
    )

    it("Puede puntuar itinerario de otro cuyo destino conoce") {
        val itinerario = Itinerario(
            creador = otroUsuario,
            destino = usuario.destinosDeseados.first(),
            dias = mutableListOf(
                DiaDeItinerario(actividadesBasicas)
            )
        )

        usuario.puedePuntuar(itinerario).shouldBeTrue()
    }

    it("No puede puntuar porque es el creador") {
        val itinerario = Itinerario(
            creador = usuario,
            destino = usuario.destinosDeseados.first(),
            dias = mutableListOf(
                DiaDeItinerario(actividadesBasicas)
            )
        )

        usuario.puedePuntuar(itinerario).shouldBeFalse()
    }

    it("No puede puntuar porque no conoce el destino") {
        val itinerario = Itinerario(
            creador = otroUsuario,
            destino = laPaz,
            dias = mutableListOf(
                DiaDeItinerario(actividadesBasicas)
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
            val dias = mutableListOf<DiaDeItinerario>()
            repeat(usuario.diasDisponibles + 1) {
                dias.add(DiaDeItinerario(actividadesBasicas))
            }
            itinerarioDeOtroUsuario.dias = dias

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }

        it("con igual cantidad de dias, puede realizarlo") {
            // Act - Given
            val dias = mutableListOf<DiaDeItinerario>()
            repeat(usuario.diasDisponibles) {
                dias.add(DiaDeItinerario(actividadesBasicas))
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
            laPaz.costoBase = 3.0 // roma.costoBase
            itinerarioDeOtroUsuario.destino = laPaz

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }

        it("con destino no deseado caro, puede realizar el itinerario") {
            // Act - When
            usuario.destinosDeseados = mutableListOf(roma)
            laPaz.costoBase = 4.0 // roma.costoBase + 1
            itinerarioDeOtroUsuario.destino = laPaz

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }
    }

    describe("Dado un Usuario Activo y un Itinerario...") {
        usuario.criterio = Activo

        it("con actividades todos los dias, puede realizar el itinerario") {
            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }

        it("con dias sin actividades, NO puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.dias.add(DiaDeItinerario(mutableListOf()))

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }
    }

    describe("Dado un Usuario Exigente y un itinerario...") {
        usuario.criterio = Exigente(porcentaje = 30.0, dificultad = Dificultad.MEDIA)
        it("con porcentaje bajo de dificultad deseada, no puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.dias.first().actividades.addAll(
                mutableListOf(
                    Actividad(
                        dificultad = Dificultad.BAJA,
                        descripcion = "asdjasdja",
                        inicio = LocalTime.of(19, 30, 0, 0),
                        fin = LocalTime.of(20, 30, 0, 0),
                        costo = 0.0

                    ), Actividad(
                        dificultad = Dificultad.BAJA,
                        descripcion = "asdjasdja",
                        inicio = LocalTime.of(20, 30, 0, 0),
                        fin = LocalTime.of(21, 30, 0, 0),
                        costo = 0.0

                    ), Actividad(
                        dificultad = Dificultad.BAJA,
                        descripcion = "asdjasdja",
                        inicio = LocalTime.of(21, 30, 0, 0),
                        fin = LocalTime.of(22, 0, 0, 0),
                        costo = 0.0

                    )
                )
            )

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeFalse()
        }
        it("con porcentaje alto de dificultad deseada, no puede realizar el itinerario") {
            // Act - When
            itinerarioDeOtroUsuario.dias.first().actividades = actividadesBasicas

            // Assert - Then
            usuario.puedeRealizar(itinerarioDeOtroUsuario).shouldBeTrue()
        }
    }
})

