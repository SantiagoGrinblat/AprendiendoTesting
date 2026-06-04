package com.santidev.aprendiendotesting.listaDeCarritoVersionPRO

import com.santidev.pruebatesting.funciones.listaDeCarritoVersionPRO.CarritoVersionPro
import com.santidev.pruebatesting.funciones.listaDeCarritoVersionPRO.DescuentoService
import com.santidev.pruebatesting.funciones.listaDeCarritoVersionPRO.PagoService
import com.santidev.pruebatesting.funciones.listaDeCarritoVersionPRO.Producto
import com.santidev.pruebatesting.funciones.listaDeCarritoVersionPRO.StockService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.sql.DriverManager
import kotlin.test.assertEquals

class CarritoVersionProTest {
  
  private lateinit var carritoVersionPro: CarritoVersionPro
  private lateinit var pagoService: PagoService
  private lateinit var stockService: StockService
  private lateinit var descuentoService: DescuentoService
  
  @Before
  fun setUp() {
    pagoService = mockk()
    stockService = mockk()
    descuentoService = mockk()
    carritoVersionPro = CarritoVersionPro(stockService, pagoService, descuentoService)
  }
  
  //para entender perfecto como funciona la secuencia que se debe seguir para probar un test es:
  //orden correcto del pago: agrego, verifico, pago, reduzco, finalizo.
  //si cambia el orden el test falla SIEMPRE
  
  //VALIDACIONES DE ENTRADA =
  //estos test validan que no entren datos invalidos al carrito
  @Test
  fun `si la cantidad del producto es 0 o inferior tira una excepcion`() {
    assertThrows<IllegalArgumentException> {
      carritoVersionPro.agregarProducto(Producto(4, "Silla", 90.0, 0))
    }
  }
  
  @Test
  fun `si el precio es 0 o menos, tira una excepcion`() {
    assertThrows<IllegalArgumentException> {
      carritoVersionPro.agregarProducto(Producto(4, "Silla", 0.0, 2))
    }
  }
  
  @Test
  fun `si el carrito esta vacio, no se realiza la compra y tira una excepcio`() {
    //No se agrega ningun producto, se intenta la compra directamente
    assertThrows<IllegalArgumentException> { carritoVersionPro.realizarCompra() }
  }
  
  //GESTION DE PRODUCTOS =
  //Test que estan relacionado con agregar productos
  @Test
  fun `verificamos si se pueden aumantar la cantidad de un producto`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(32, "Mate", 50.0, 1))
    carritoVersionPro.agregarProducto(Producto(32, "Mate", 50.0, 1))
    
    //act
    every { stockService.verificarStock(32, 2) } returns true
    DriverManager.println("vemos si se agrego el producto: " +
        "${stockService.verificarStock(productoId = 32, 2)}"
    )
    
    TestCase.assertEquals(100.0, carritoVersionPro.calcularTotal())
    DriverManager.println("Se agregaron ambos productos por eso el total es: ${carritoVersionPro.calcularTotal()}")
    
    verify(exactly = 1) { stockService.verificarStock(32, 2) }
    
    every { pagoService.procesarPago(100.0) } returns true
    
    every { stockService.reducirStock(32, 2) } just runs
    
    //assert
    TestCase.assertEquals(true, carritoVersionPro.realizarCompra())
  }
  
  //CALCULOS DE TOTALES =
  //primero los calculos simples
  @Test
  fun `hay stock, pago aprobado, cupon valido, se actualiza el precio con el descuento aplicado`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(5, "Mesa", 50.0, 1))
    
    //act
    every { stockService.verificarStock(5, 1) } returns true
    every { descuentoService.validarCupon("DESCUENTO10") } returns 10.0
    //el nombre del cupon debe ser exactamente el mismo que esta en validarCupon
    
    //assert
    TestCase.assertEquals(45.0, carritoVersionPro.calcularTotalConDescuento("DESCUENTO10"))
  }
  
  //VALIDACIONES DE CUPONES =
  //Todos los errores relacionados con descuentos
  @Test
  fun `cupon expirado devuelve una excepcion`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(2, "Mesa", 50.0, 3))
    
    //act
    every { descuentoService.validarCupon("DESCUENTO10") } throws IllegalArgumentException("Cupon expirado")
    
    //assert
    assertThrows<IllegalArgumentException> {
      carritoVersionPro.calcularTotalConDescuento("DESCUENTO10")
    }
  }
  
  @Test
  fun `cupon inexistente devuelve una excepcion`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(2, "Mesa", 50.0, 3))
    
    //act
    every { descuentoService.validarCupon("DESCUENTO10") } throws IllegalArgumentException("Cupon inexistente")
    
    //assert
    assertThrows<IllegalArgumentException> {
      carritoVersionPro.calcularTotalConDescuento("DESCUENTO10")
    }
  }
  
  @Test
  fun `descuento negativo en el cupon tira excepcion`() {
    carritoVersionPro.agregarProducto(Producto(8, "Termo", 50.0, 4))
    
    every { descuentoService.validarCupon("ERROR") } returns -30.0
    
    assertThrows<IllegalArgumentException> {
      carritoVersionPro.calcularTotalConDescuento("ERROR")
    }
  }
  
  @Test
  fun `si el cupon es mayor al 100% del producto, tira una excepcion`() {
    carritoVersionPro.agregarProducto(Producto(1, "Mesa", 100.0, 1))
    
    every { descuentoService.validarCupon("DESCUENTO105") } returns 105.0
    
    assertThrows<IllegalArgumentException> {
      carritoVersionPro.calcularTotalConDescuento("DESCUENTO105")
    }
  }
  
  //DESCUENTOS DINAMICOS =
  //con answers
  @Test
  fun `simular un descuento dinamico segun el cupon`() {
    carritoVersionPro.agregarProducto(Producto(3, "Mesa", 60.0, 3))
    carritoVersionPro.agregarProducto(Producto(4, "Silla", 90.0, 1))
    
    every { stockService.verificarStock(3, 3) } returns true
    every { stockService.verificarStock(4, 1) } returns true
    every { descuentoService.validarCupon(any()) } answers {
      val cupon = firstArg<String>()
      when (cupon) {
        "DESCUENTO10" -> 10.0
        "DESCUENTO50" -> 50.0
        else -> throw IllegalArgumentException("El tiempo del cupon expiro")
      }
    }
    
    val resultadoConDescuentoDe10 = carritoVersionPro.calcularTotalConDescuento("DESCUENTO10")
    val resultadoConDescuentoDe50 = carritoVersionPro.calcularTotalConDescuento("DESCUENTO50")
    
    TestCase.assertEquals(243.0, resultadoConDescuentoDe10)
    TestCase.assertEquals(135.0, resultadoConDescuentoDe50)
  }
  
  //FLUJO DE STOCK =
  //todo lo relacionado con la disponibilidad de productos.
  @Test
  fun `si no hay stock, no se agrega el producto al carrito y lanza una excepcion`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(3, "Silla", 50.0, 6))
    
    //act
    every {
      stockService.verificarStock(
        3,
        6
      )
    } throws IllegalArgumentException("No hay esa cantidad en stock")
    
    //assert
    assertThrows<IllegalArgumentException> { carritoVersionPro.realizarCompra() }
    
    verify(exactly = 0) { pagoService.procesarPago(any()) }
    verify(exactly = 0) { stockService.reducirStock(any(), any()) }
  }
  
  //FLUJO DE PAGO =
  //todo lo relacionado con los pagos
  @Test
  fun `si hay stock, y el pago es aprobado, devolvemos true`() {
    
    //agrego un producto al carrito con la cantidad que quiero de ese producto
    carritoVersionPro.agregarProducto(Producto(2, "Mesa", 50.0, 1))
    
    //simulamos que hay stock para ese producto
    every { stockService.verificarStock(2, 1) } returns true
    //los ids en every deben coincidir exactamente con los del producto agregado
    //si el producto tiene id = 2, todos los every deben usar id = 2
    //de lo contrario MockK no encuentra la respuesta configurada y explota
    
    //simulamos que el pago es aprobado
    every { pagoService.procesarPago(50.0) } returns true
    
    //una vez que se proceso el pago, reducimos el stock del producto en la cantidad que el usuario selecciono
    every { stockService.reducirStock(2, 1) } just runs
    //just runs = "ejecutate y no devuelvas nada"
    //se usa cuando la funcion es de tipo Unit
    //equivalente a returns pero para funciones que no tienen valor de retorno
    
    //finaliza la compra
    TestCase.assertEquals(true, carritoVersionPro.realizarCompra())
  }
  
  @Test
  fun `hay stock, pero el pago falla, entonces devuelve false`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(4, "Mesa", 50.0, 1))
    
    //act
    every { stockService.verificarStock(4, 1) } returns true
    every { pagoService.procesarPago(50.0) } returns false
    
    //assert
    TestCase.assertEquals(false, carritoVersionPro.realizarCompra())
    
    //el pago se intento al menos 1 vez (devolvio false, pero se llamo)
    verify(exactly = 1) { pagoService.procesarPago(any()) }
    
    //el stock NO se redujo porque el pago fallo
    verify(exactly = 0) { stockService.reducirStock(any(), any()) }
  }
  
  @Test
  fun `pago lanza una excepcion`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(2, "Mesa", 50.0, 1))
    
    //act
    every { stockService.verificarStock(2, 1) } returns true
    every { pagoService.procesarPago(50.0) } throws IllegalArgumentException("Error al pagar")
    
    //assert
    assertThrows<IllegalArgumentException> {
      carritoVersionPro.realizarCompra()
    }
    
    verify(exactly = 1) { pagoService.procesarPago(any()) }
    verify(exactly = 0) { stockService.reducirStock(any(), any()) }
    //el pago se intento 1 vez pero lanzó una excepción
    //como fallo, el stock nunca se redujo
  }
  
  //CAPTURA DE ARGUEMNTOS =
  //usamos slot
  @Test
  fun `usamos slot para capturar el monto del pago`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(2, "Mesa", 50.0, 1))
    
    val slot = slot<Double>()
    
    every { stockService.verificarStock(2, 1) } returns true
    every { pagoService.procesarPago(capture(slot)) } returns true // ← capturás acá
    every { stockService.reducirStock(2, 1) } just runs
    
    // Act
    carritoVersionPro.realizarCompra()
    
    // Assert
    TestCase.assertEquals(50.0, slot.captured) // ← verificás después de realizarCompra
    
    verifyOrder {
      stockService.verificarStock(2, 1)
      pagoService.procesarPago(50.0)
      stockService.reducirStock(2, 1)
    }
  }
  
  @Test
  fun `capturamos con slot el productId que se paso a reducirStock`() {
    //arrange
    carritoVersionPro.agregarProducto(Producto(6, "Mesa", 50.0, 2))
    
    val slot = slot<Int>()
    
    every { stockService.verificarStock(6, 2) } returns true
    every { pagoService.procesarPago(any()) } returns true
    every { stockService.reducirStock(capture(slot), any()) } just runs
    
    //act
    carritoVersionPro.realizarCompra()
    
    //assert
    TestCase.assertEquals(6, slot.captured)
    DriverManager.println("el id del producto que se redujo es: ${slot.captured}")
    verify(exactly = 1) { stockService.reducirStock(6, any()) }
  }
  
  //VERIFICACION DE ORDEN
  //todo lo relacionado con el orden de ejecucion
  
  //verifyOrder =
  //sirve para verificar el ORDEN EXACTO en que se llamaron los metodos.
  //Eso solo confirma que: “se llamo”.
  //Pero NO confirma: cuando, en quw orden, si fue antes o despues de otra cosa
  //Y ahi entra verifyOrder.
  
  //Problema real que resuelve
  //Imaginate este flujo:
  // 1 verificar stock
  // 2 procesar pago
  // 3 reducir stock
  
  //Ese es el flujo correcto.
  //Pero, que pasa si el codigo hace esto? :
  // 1 reducir stock
  // 2 procesar pago
  // 3 verificar stock
  //Si el test estuviera con con verify(exactly) PASA IGUAL.
  //Porque los metodos SI fueron llamados.
  //El problema: fueron llamados MAL.
  
  //con verifyOrder
  //MockK verifica: primero stock, despues pago, despues reduccion
  //Si el orden cambia: el test falla.
  //funciona exactamente por que lo que es un verificador de orden exacto
  
  //esto es muy importante cuando el orden SI IMPORTA. en casos como BANCOS, ECOMMERCES LOGIN ETC...
  @Test
  fun `Multiples productos con verifyOrder`() {
    carritoVersionPro.agregarProducto(Producto(1, "Mesa", 60.0, 2))
    carritoVersionPro.agregarProducto(Producto(2, "Silla", 90.0, 3))
    
    every { stockService.verificarStock(1, 2) } returns true
    every { stockService.verificarStock(2, 3) } returns true
    every { pagoService.procesarPago(390.0) } returns true
    every { stockService.reducirStock(1, 2) } just runs
    every { stockService.reducirStock(2, 3) } just runs
    
    val resultado = carritoVersionPro.calcularTotal()
    
    carritoVersionPro.realizarCompra()
    
    TestCase.assertEquals(390.0, resultado)
    
    verifyOrder {
      stockService.verificarStock(1, 2)
      stockService.verificarStock(2, 3)
      pagoService.procesarPago(390.0)
      stockService.reducirStock(1, 2)
      stockService.reducirStock(2, 3)
    }
  }
  
  //verifySequence -> verifica que EXACTAMENTE esas llamadas ocurrieron
  //sin ninguna llamada extra, ni antes ni despues ni en el medio
  //si hay una llamada de mas, el test falla
  //en este caso usamos verifySequence porque queremos asegurarnos
  //que el flujo de compra fue EXACTAMENTE ese, sin pasos extras
  @Test
  fun `verificamos que la secuencia de pasos que usa el test sea la correcta`() {
    carritoVersionPro.agregarProducto(Producto(1, "Mesa", 100.0, 1))
    
    every { stockService.verificarStock(1, 1) } returns true
    every { pagoService.procesarPago(100.0) } returns true
    every { stockService.reducirStock(1, 1) } just runs
    
    val resultado = carritoVersionPro.calcularTotal()
    
    carritoVersionPro.realizarCompra()
    
    assertEquals(100.0, resultado)
    
    verifySequence {
      stockService.verificarStock(1, 1)
      pagoService.procesarPago(100.0)
      stockService.reducirStock(1, 1)
    }
  }
  
}