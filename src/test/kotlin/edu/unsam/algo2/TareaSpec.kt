package edu.unsam.algo2

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
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
            amigos.add(usuarioReceptor)
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

    describe("Dado un Usuario y una Tarea de Hacerse amigos de los que conozcan un destino") {
        val destinoConocido = Destino(ciudad = "Bariloche", pais = "Argentina", costoBase = 123456.0)
        val destinoPeru = Destino(ciudad = "Cusco", pais="Peru", costoBase= 70000.0)
        val destinoCosta = Destino(ciudad = "Mar del Plata", pais="Argentina", costoBase= 40000.0)

        val usuarioPepe = Usuario(
            nombre = "pepe",
            apellido = "pepito",
            username = "pepepe",
            email = "pepe@mail.com",
            paisResidencia = "Colombia",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(destinoCosta),
            diasDisponibles = 8,
            vehiculoPreferencia = Caprichoso
        ).apply{
            destinosVisitados.add(destinoPeru)
        }
        val usuarioOtro = Usuario(
            nombre = "otro",
            apellido = "otro apellido",
            username = "otro_otro",
            email = "otro@mail.com",
            paisResidencia = "Uruguay",
            criterio = Relajado,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(destinoConocido),
            diasDisponibles = 8,
            vehiculoPreferencia = SinLimite
        ).apply{
            destinosVisitados.add(destinoPeru)
            destinosVisitados.add(destinoCosta)
        }

        val usuarioCreador = Usuario(
            nombre = "usuario",
            apellido = "loco",
            username = "usuarioloco",
            email = "usuarioloco@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                Destino(ciudad = "u", pais = "u", costoBase = 123456.0),
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        )

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
        val usuarios = mutableListOf(usuarioCreador,usuarioOtro,usuarioPepe)
        val repoUsuarios = RepositorioDeUsuarios().apply {
            elementos.addAll(usuarios)
        }
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
        val hacerseAmigoTarea = HacerseAmigoTarea("hacerse amigo tarea", mockedMailSender,repoUsuarios,destinoConocido)
        it("Se agrega a la lista de amigos del usuario cuando otro(usuario) conoce el destino") {
            usuarioCreador.agregarTarea(hacerseAmigoTarea)
            usuarioCreador.realizarTareas()
            usuarioCreador.amigos shouldContain usuarioOtro
        }
    }
    describe("Dado un Usuario y una tarea de agregar destino mas caro de los amigos a la lista de destinos deseados") {
        val destinoConocido = Destino(ciudad = "Bariloche", pais = "Argentina", costoBase = 123456.0)
        val destinoPeru = Destino(ciudad = "Cusco", pais="Peru", costoBase= 70000.0)
        val destinoCosta = Destino(ciudad = "Mar del Plata", pais="Argentina", costoBase= 40000.0)
        val destinoSierra = Destino(ciudad = "Tandil", pais="Argentina", costoBase= 50000.0)

        val usuarioPepe = Usuario(
            nombre = "pepe",
            apellido = "pepito",
            username = "pepepe",
            email = "pepe@mail.com",
            paisResidencia = "Colombia",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(destinoCosta,destinoSierra),
            diasDisponibles = 8,
            vehiculoPreferencia = Caprichoso
        )
        val usuarioOtro = Usuario(
            nombre = "otro",
            apellido = "otro apellido",
            username = "otro_otro",
            email = "otro@mail.com",
            paisResidencia = "Uruguay",
            criterio = Relajado,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(destinoCosta,destinoPeru),
            diasDisponibles = 8,
            vehiculoPreferencia = SinLimite
        )

        val usuarioCreador = Usuario(
            nombre = "usuario",
            apellido = "loco",
            username = "usuarioloco",
            email = "usuarioloco@mail.com",
            paisResidencia = "Mexico",
            criterio = Activo,
            fechaAlta = LocalDate.now(),
            destinosDeseados = mutableListOf(
                Destino(ciudad = "u", pais = "u", costoBase = 123456.0),
            ),
            diasDisponibles = 5,
            vehiculoPreferencia = Neofilo
        ).apply{
            amigos.add(usuarioPepe)
            amigos.add(usuarioOtro)
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
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
        val AgregarDestinoMasCaroTarea = AgregarDestinoMasCaroTarea("Agregar destino mas caro de los amigos a destinos deseados", mockedMailSender)
        it("Se agrega a la lista de destinos deseados los destinos mas caros de los amigos") {
            usuarioCreador.agregarTarea(AgregarDestinoMasCaroTarea)
            usuarioCreador.realizarTareas()
          //  val destinosMasCarosAmigos = mutableListOf(usuarioCreador.destinosMasCaroDeAmigos())
            usuarioCreador.destinosDeseados shouldContain destinoSierra
            usuarioCreador.destinosDeseados shouldContain destinoPeru
        }
    }
})