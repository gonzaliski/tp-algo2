package edu.unsam.algo2

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.*
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalTime

class TareaSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Dado un Usuario y una Tarea de Puntuar Itinerarios") {
        val destinoConocido = Destino(ciudad = "destino", pais = "destino", costoBase = 123456.0)
        val usuarioQuePuntua = Usuario(
            nombre = "a",
            apellido = "b",
            username = "c",
            email = "abc@mail.com",
            paisResidencia = "Colombia",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                destinoConocido
            ),
            diasDisponibles = 8,
            vehiculoPreferencia = Caprichoso
        )

        val usuarioCreador = Usuario(
            nombre = "h",
            apellido = "j",
            username = "k",
            email = "hjk@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                Destino(ciudad = "u", pais = "u", costoBase = 123456.0),
                destinoConocido
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        )

        val itinerariosAPuntuar = mutableListOf(
            Itinerario(
                creador = usuarioCreador,
                destino = destinoConocido,
                dias = mutableListOf(
                    DiaDeItinerario(
                        actividades = mutableListOf(
                            Actividad(
                                dificultad = Dificultad.MEDIA,
                                costo = 56_000.0,
                                descripcion = "lsidvjpa",
                                inicio = LocalTime.MIN,
                                fin = LocalTime.MAX
                            )
                        )
                    )
                )
            )
        )

        usuarioQuePuntua.agregarVariosItinerariosAPuntuar(itinerariosAPuntuar)

        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)

        describe("Se agrega la tarea a la lista del usuario") {
            it("Con puntuación baja, no se puntúa y no se envía mail") {
                // Arrange
                val puntuarItinerarioTarea =
                    PuntuarItinerarioTarea(nombre = "puntuar", mailSender = mockedMailSender, puntuacion = 0)
                usuarioQuePuntua.agregarTarea(puntuarItinerarioTarea)
                // Assert
                shouldThrow<IllegalArgumentException> { usuarioQuePuntua.realizarTareas() }
                verify(exactly = 0) {
                    mockedMailSender.sendMail(any())
                }
            }

            it("Con puntuación alta, no se puntúa y no se envía mail") {
                // Arrange
                val puntuarItinerarioTarea =
                    PuntuarItinerarioTarea(nombre = "puntuar", mailSender = mockedMailSender, puntuacion = 11)
                usuarioQuePuntua.agregarTarea(puntuarItinerarioTarea)
                // Assert
                shouldThrow<IllegalArgumentException> { usuarioQuePuntua.realizarTareas() }
                verify(exactly = 0) {
                    mockedMailSender.sendMail(any())
                }
            }

            it("Si el usuario ya puntuó al itinerario, da error y no se envía mail") {
                // Arrange
                val puntuarItinerarioTarea =
                    PuntuarItinerarioTarea(nombre = "puntuar", mailSender = mockedMailSender, puntuacion = 10)
                usuarioQuePuntua.agregarMultiplesTareas(listOf(puntuarItinerarioTarea, puntuarItinerarioTarea))
                // Assert
                shouldThrow<IllegalArgumentException> { usuarioQuePuntua.realizarTareas() }
                itinerariosAPuntuar.all { it.fuePuntuadoPor(usuarioQuePuntua) }.shouldBeTrue()
                itinerariosAPuntuar.all { it.puntuaciones[usuarioQuePuntua] == 10 }.shouldBeTrue()
                verify(exactly = 1) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "puntuar",
                            to = "abc@mail.com",
                            subject = "Se realizo la tarea: puntuar",
                            content = "Se realizo la tarea: puntuar"
                        )
                    )
                }
            }

            describe("Si el usuario no puede puntuar al itinerario") {
                it("por ser el creador del itinerario, no se puntúa y no se envía mail") {// Arrange
                    val puntuarItinerarioTarea =
                        PuntuarItinerarioTarea(nombre = "puntuar", mailSender = mockedMailSender, puntuacion = 10)
                    usuarioCreador.agregarTarea(puntuarItinerarioTarea)
                    usuarioCreador.agregarVariosItinerariosAPuntuar(itinerariosAPuntuar)
                    // Assert
                    shouldThrow<InvalidAction> { usuarioCreador.realizarTareas() }
                    itinerariosAPuntuar.any { it.fuePuntuadoPor(usuarioCreador) }.shouldBeFalse()
                    verify(exactly = 0) {
                        mockedMailSender.sendMail(any())
                    }
                }

                it("por no conocer el destino del itinerario, no se puntúa y no se envía mail") {
                    // Arrange
                    usuarioQuePuntua.destinosDeseados.remove(destinoConocido)
                    val puntuarItinerarioTarea =
                        PuntuarItinerarioTarea(nombre = "puntuar", mailSender = mockedMailSender, puntuacion = 9)
                    usuarioQuePuntua.agregarTarea(puntuarItinerarioTarea)
                    // Assert
                    shouldThrow<InvalidAction> { usuarioQuePuntua.realizarTareas() }
                    verify(exactly = 0) {
                        mockedMailSender.sendMail(any())
                    }
                }
            }

            it("Con puntuación válida, se puntúan todos los itinerarios a puntuar") {
                // Arrange
                val puntuarItinerarioTarea =
                    PuntuarItinerarioTarea(nombre = "puntuar", mailSender = mockedMailSender, puntuacion = 10)
                usuarioQuePuntua.agregarTarea(puntuarItinerarioTarea)
                // Act
                usuarioQuePuntua.realizarTareas()
                // Assert
                //itinerariosAPuntuar.forEach { println("${it.puntuaciones}") }
                itinerariosAPuntuar.all { it.fuePuntuadoPor(usuarioQuePuntua) }.shouldBeTrue()
                itinerariosAPuntuar.all { it.puntuaciones[usuarioQuePuntua] == 10 }.shouldBeTrue()
                verify(exactly = 1) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "puntuar",
                            to = "abc@mail.com",
                            subject = "Se realizo la tarea: puntuar",
                            content = "Se realizo la tarea: puntuar"
                        )
                    )
                }
            }
        }
    }

    describe("Dado un Usuario y una Tarea de Transferir Itinerarios") {
        val destinoConocido = Destino(ciudad = "destino", pais = "destino", costoBase = 123456.0)

        val usuarioReceptor = Usuario(
            nombre = "a",
            apellido = "b",
            username = "c",
            email = "abc@mail.com",
            paisResidencia = "Colombia",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(destinoConocido),
            diasDisponibles = 8,
            vehiculoPreferencia = Caprichoso
        )

        val usuarioCreador = Usuario(
            nombre = "h",
            apellido = "j",
            username = "k",
            email = "hjk@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                Destino(ciudad = "u", pais = "u", costoBase = 123456.0),
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        ).apply {
            agregarAmigo(usuarioReceptor)
        }

        val itinerarios = mutableListOf(
            Itinerario(
                creador = usuarioCreador,
                destino = destinoConocido,
                dias = mutableListOf(
                    DiaDeItinerario(
                        actividades = mutableListOf(
                            Actividad(
                                dificultad = Dificultad.MEDIA,
                                costo = 56_000.0,
                                descripcion = "lsidvjpa",
                                inicio = LocalTime.MIN,
                                fin = LocalTime.MAX
                            )
                        )
                    )
                )
            )
        )

        val repoItinerarios = RepositorioDeItinerarios().apply {
            elementos.addAll(itinerarios)
        }
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)

        describe("Si el usuario creador original tiene itinerarios") {

            it("se los transfiere al usuario receptor y se envía mail") {
                val transferirTarea = TransferirItinerariosTarea("asd", mockedMailSender, repoItinerarios)
                usuarioCreador.agregarTarea(transferirTarea)

                usuarioCreador.realizarTareas()

                repoItinerarios.itinerariosDe(usuarioReceptor) shouldContainAll itinerarios

                verify(exactly = 1) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "asd",
                            to = "hjk@mail.com",
                            subject = "Se realizo la tarea: asd",
                            content = "Se realizo la tarea: asd"
                        )
                    )
                }
            }
        }

        describe("Si el usuario creador original NO tiene itinerarios") {

            it("no se transfieren itinerarios pero SI se envía mail") {
                val transferirTarea = TransferirItinerariosTarea("asd", mockedMailSender, repoItinerarios)
                usuarioCreador.agregarTarea(transferirTarea)
                repoItinerarios.elementos.clear()

                usuarioCreador.realizarTareas()

                repoItinerarios.itinerariosDe(usuarioReceptor).shouldBeEmpty()

                verify(exactly = 1) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "asd",
                            to = "hjk@mail.com",
                            subject = "Se realizo la tarea: asd",
                            content = "Se realizo la tarea: asd"
                        )
                    )
                }
            }
        }

    }

    describe("Dado un Usuario y una Tarea de Hacerse Amigo") {
        val destinoConocido = Destino(ciudad = "destino", pais = "destino", costoBase = 123456.0)

        val usuarioAmigo = Usuario(
            nombre = "a",
            apellido = "b",
            username = "c",
            email = "abc@mail.com",
            paisResidencia = "Colombia",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(destinoConocido),
            diasDisponibles = 8,
            vehiculoPreferencia = Caprichoso
        )

        val usuarioCreador = Usuario(
            nombre = "h",
            apellido = "j",
            username = "k",
            email = "hjk@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                Destino(ciudad = "u", pais = "u", costoBase = 123456.0),
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        ).apply {
            agregarAmigo(usuarioAmigo)
        }

        val usuarioNoAmigo = Usuario(
            nombre = "n",
            apellido = "a",
            username = "na",
            email = "na@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                Destino(ciudad = "u", pais = "u", costoBase = 123456.0),
                destinoConocido
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        )

        val usuarios = mutableListOf(
            usuarioNoAmigo,
            usuarioAmigo
        )

        val repositorioDeUsuarios = RepositorioDeUsuarios().apply {
            usuarios.forEach { create(it) }
        }
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)

        it("El usuario creador se hace amigo solo de quienes no lo era antes si conocen el destino") {
            val hacerseAmigoTarea =
                HacerseAmigoTarea("hacerse amigo", mockedMailSender, repositorioDeUsuarios, destinoConocido)

            usuarioCreador.agregarTarea(hacerseAmigoTarea)

            usuarioCreador.realizarTareas()

            usuarioCreador.esAmigoDe(usuarioNoAmigo).shouldBeTrue()
            usuarioCreador.amigos.shouldNotContainDuplicates()

            verify(exactly = 1) {
                mockedMailSender.sendMail(
                    Mail(
                        from = "hacerse amigo",
                        to = "hjk@mail.com",
                        subject = "Se realizo la tarea: hacerse amigo",
                        content = "Se realizo la tarea: hacerse amigo"
                    )
                )
            }

        }

        describe("El usuario creador no puede agregarse a si mismo como amigo") {
            val hacerseAmigoTarea =
                HacerseAmigoTarea("hacerse amigo", mockedMailSender, repositorioDeUsuarios, destinoConocido)
            usuarioCreador.agregarTarea(hacerseAmigoTarea)
            usuarioCreador.agregarAmigo(usuarioNoAmigo)

            usuarioCreador.realizarTareas()

            usuarioCreador.amigos.shouldContainExactly(usuarioAmigo, usuarioNoAmigo)

            verify(exactly = 1) {
                mockedMailSender.sendMail(
                    Mail(
                        from = "hacerse amigo",
                        to = "hjk@mail.com",
                        subject = "Se realizo la tarea: hacerse amigo",
                        content = "Se realizo la tarea: hacerse amigo"
                    )
                )
            }
        }

    }

    describe("Dado un Usuario creador de una Tarea con amigos") {
        val destinoBarato = Destino(ciudad = "destino", pais = "destino", costoBase = 123.0)
        val destinoCaro = Destino(ciudad = "u", pais = "u", costoBase = 123456.0)
        val usuarioAmigo = Usuario(
            nombre = "a",
            apellido = "b",
            username = "c",
            email = "abc@mail.com",
            paisResidencia = "Colombia",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(destinoBarato),
            diasDisponibles = 8,
            vehiculoPreferencia = Caprichoso
        )

        val usuarioCreador = Usuario(
            nombre = "h",
            apellido = "j",
            username = "k",
            email = "hjk@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                destinoBarato,
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        )
        usuarioCreador.agregarAmigo(usuarioAmigo)

        val amigoQueConoceOtroDestino = Usuario(
            nombre = "n",
            apellido = "a",
            username = "na",
            email = "na@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                destinoCaro,
                destinoBarato
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        )
        usuarioCreador.agregarAmigo(amigoQueConoceOtroDestino)

        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)

        describe("y dada una Tarea de Agregar Destino Mas Caro"){
            val agregarDestinoMasCaroTarea = AgregarDestinoMasCaroTarea("agregar destino mas caro de amigos", mockedMailSender)
            usuarioCreador.agregarTarea(agregarDestinoMasCaroTarea)

            it("Se agrega el destino más caro de cada amigo como deseado"){
                usuarioCreador.realizarTareas()

                usuarioCreador.destinosDeseados.shouldContainExactlyInAnyOrder(destinoBarato, destinoCaro, destinoBarato)
                usuarioCreador.destinosDeseados.shouldContainDuplicates()
            }
        }

    }
})