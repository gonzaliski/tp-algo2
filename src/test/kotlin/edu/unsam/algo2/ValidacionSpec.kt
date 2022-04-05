package edu.unsam.algo2

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.style.DescribeSpec
import java.time.LocalDate
import java.time.LocalTime

internal class ValidacionSpec : DescribeSpec({
    describe("Validacion de destino") {
        it("No tira error xD") {
            shouldNotThrowAny {
                Destino(
                    pais = "Argentina",
                    ciudad = "Runcuncun",
                    costoBase = 25_000.0
                )
            }
        }
    }
    describe("Validaciones en Usuario") {
        val destino = Destino(
            pais = "Argentina",
            ciudad = "Runcuncun",
            costoBase = 25_000.0
        )
        it("Usuario bien creado no falla") {
            shouldNotThrowAny {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 15,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino)
                )
            }
        }
        it("Usuario falla con fecha de alta en el futuro") {
            shouldThrowMessage("La fecha de alta no puede ser posterior a la del día.") {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now().plusDays(3),
                    diasDisponibles = 2,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino)
                )
            }
        }
        it("Usuario falla sin destinos deseados") {
            shouldThrowMessage("Todos los usuarios deben tener al menos un destino deseado.") {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 2,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf()
                )
            }
        }
        it("Usuario falla si no tiene dias para viajar") {
            shouldThrowMessage("Los días para viajar, deben ser mayores a cero.") {
                Usuario(
                    nombre = "Peperoni",
                    fechaAlta = LocalDate.now(),
                    diasDisponibles = 0,
                    criterio = Relajado,
                    apellido = "Peter",
                    username = "ppeter",
                    paisResidencia = "Venezuela",
                    destinosDeseados = mutableListOf(destino)
                )
            }
        }
    }
    it("Actividades con horarios solapados tira error") {
        shouldThrowMessage("Los horarios de las actividades no se pueden solapar") {
            Itinerario(
                creador = Usuario(
                    nombre = "Carlos",
                    apellido = "Gomez",
                    username = "cgomez",
                    fechaAlta = LocalDate.now().minusYears(16),
                    paisResidencia = "Chile",
                    diasDisponibles = 1,
                    criterio = Relajado,
                    destinosDeseados = mutableListOf(
                        Destino(
                            pais = "Argentina",
                            ciudad = "MDQ",
                            costoBase = 25_000.0
                        ),
                        Destino(
                            pais = "Argentina",
                            ciudad = "Buenos Aires",
                            costoBase = 25_000.0
                        )
                    )
                ),
                destino = Destino(
                    pais = "Chile",
                    ciudad = "Santiago",
                    costoBase = 2.0
                ),
                dias = mutableListOf(
                    Itinerario.DiaDeItinerario(
                        mutableListOf(
                            Actividad(
                                dificultad = Actividad.Dificultad.MEDIA,
                                descripcion = "dasdjasdja",
                                inicio = LocalTime.of(12, 30, 0, 0),
                                fin = LocalTime.of(14, 30, 0, 0),
                                costo = 3.0
                            ),
                            Actividad(
                                dificultad = Actividad.Dificultad.MEDIA,
                                descripcion = "agsdjasdja",
                                inicio = LocalTime.of(13, 0, 0, 0),
                                fin = LocalTime.of(15, 30, 0, 0),
                                costo = 3.0
                            )
                        )
                    )
                ),
            )
        }

    }

    describe("puntuaciones"){
        val user = Usuario(
            nombre = "Peperoni",
            fechaAlta = LocalDate.now(),
            diasDisponibles = 15,
            criterio = Relajado,
            apellido = "Peter",
            username = "ppeter",
            paisResidencia = "Venezuela",
            destinosDeseados = mutableListOf(
                Destino(
                    pais = "Argentina",
                    ciudad = "MDQ",
                    costoBase = 25_000.0
                ),
                Destino(
                    pais = "Argentina",
                    ciudad = "Buenos Aires",
                    costoBase = 25_000.0
                )
            )
        )
       var itinerario = Itinerario(
            creador = user,
            destino = Destino(
                pais = "Argentina",
                ciudad = "Runcuncun",
                costoBase = 25_000.0
            ),
            dias = mutableListOf(
                Itinerario.DiaDeItinerario(
                    mutableListOf(
                        Actividad(
                            dificultad = Actividad.Dificultad.MEDIA,
                            descripcion = "dasdjasdja",
                            inicio = LocalTime.of(12, 30, 0, 0),
                            fin = LocalTime.of(14, 30, 0, 0),
                            costo = 3.0
                        ),
                        Actividad(
                            dificultad = Actividad.Dificultad.MEDIA,
                            descripcion = "agsdjasdja",
                            inicio = LocalTime.of(15, 0, 0, 0),
                            fin = LocalTime.of(16, 30, 0, 0),
                            costo = 3.0
                        )
                    )
                )
            ),
        )


        it("Puntuacion no esta entre 1 y 10 tira error"){
            shouldThrowMessage("Puntuacion solo puede ser un valor entre 1 y 10"){
                user.puntuar(itinerario,11)
            }
        }
        it("Usuario no puede puntuar mas de una vez"){
            shouldThrowMessage("El usuario ya ha puntuado este itinerario"){
                user.puntuar(itinerario,10)
                user.puntuar(itinerario,10)
            }
        }
    }
})