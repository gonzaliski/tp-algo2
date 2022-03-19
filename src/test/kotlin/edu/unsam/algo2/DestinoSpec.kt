package edu.unsam.algo2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class DestinoSpec: DescribeSpec ({
    describe("Test con usuario con mucha antiguedad (+15), pais de residencia igual al pais destino ") {
        val usuario = Usuario( 
                nombre = "Frank", 
                apellido = "Arnold", 
                username = "frarnold", 
                fecha_alta = LocalDate.of(2003, 2, 5),
                pais_residencia = "Inglaterra"
            )

        describe("Test con destino no local"){
            val londres = Destino(
                pais="Inglaterra",
                ciudad="Londres",
                costo_base=60000
            )
            it("20% más por no ser destino local y descuento por antiguedad (15%), costo de 61200"){
                londres.costo(usuario) shouldBe 61200
            }
        }

        
    }
})