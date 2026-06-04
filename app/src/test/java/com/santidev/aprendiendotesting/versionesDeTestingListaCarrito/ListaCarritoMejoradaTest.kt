package com.santidev.aprendiendotesting.versionesDeTestingListaCarrito

import com.santidev.pruebatesting.funciones.versionesDeTestingListaCarrito.ListaCarritoMejorada
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListaCarritoMejoradaTest {
  
  // VERSION MEJORADA
  //Mejoras respecto a la version base:
  //@Before para no repetir inicializacion
  //data class con nombre y precio juntos
  //Helper functions para evitar repeticion (DRY)
  //Tests negativos con assertThrows
  //calcularTotal sin parametros
  
  //En la version mejorada arrancamos como deberia hacerse un buen test =
  //Patron: AAA = Arrange: Organizar, Act: Actuar, Assert: Afirmar.
  //Before: para ahorrar llamados de funcion base
  //lateinit var: para inicializar la variable mas tarden
  
  private lateinit var listaCarritoMejorada: ListaCarritoMejorada
  
  @Before
  fun funcionPrevia() {
    listaCarritoMejorada = ListaCarritoMejorada()
  }
  
  //Se pueden hacer funciones que almacenen productos puntuales para testear y evitar repetir codigo O generar errores de tipeo.
  //se llaman helper functions o funciones de ayuda.
  //PJ: trabajo en un supermercado y tengo que testear productos dentro del area que le corresponde.
  //podria armar funciones con los productos de cada area =
  private fun productosCocina() {
    listaCarritoMejorada.agregarProducto("Mesa", 50.0)
    listaCarritoMejorada.agregarProducto("Silla", 20.0)
  }
  
  private fun productosBaño() {
    listaCarritoMejorada.agregarProducto("Lava manos", 150.0)
    listaCarritoMejorada.agregarProducto("Cortina", 5.0)
  }
  //Y luego puedo armar test por cada area, verificando los objetos, precios, productos, de la cocina, de baño, etc...
  //Llamando a la funcion que necesito dentro de cada test =
  //@Test
  //fun `verificamos el total de productos de cocina`() {
  //    productosCocina() <- llamo solo a los productos que necesito
  //    assertEquals(70.0, listaCarritoMejorada.calcularTotal(), 0.0)
  //}
  //Este concepto se llama DRY - Don't Repeat Yourself = No te repitas a vos mismo.
  // SIN DRY - si cambia el precio de Mesa, lo cambias en 3 tests
  //listaCarritoMejorada.agregarProducto("Mesa", 50.0) // test 1
  //listaCarritoMejorada.agregarProducto("Mesa", 50.0) // test 2
  //listaCarritoMejorada.agregarProducto("Mesa", 50.0) // test 3
  
  // CON DRY - solo hacemos el cambio una vez
  //private fun productosCocina() {
  //listaCarritoMejorada.agregarProducto("Mesa", 50.0) // solo acá
  //}
  
  @Test
  fun `agregamos un producto con su nombre y precio`() {
    //sin @Before = AAA explicito, el Arrange esta adentro del test
    //con @Before = AAA implicito, el Arrange esta afuera del test
    //el patron no desaparece, solo el Arrange se mueve al @Before
    
    // Arrange implícito = @Before ya inicializo listaCarritoMejorada
    
    // Act - lo que ejecuto
    //listaCarritoMejorada.agregarProducto("Mesa", 50.0)
    //listaCarritoMejorada.agregarProducto("Silla", 20.0)
    productosCocina()
    
    // Assert - lo que verifico
    assertEquals(2, listaCarritoMejorada.cantidadDeProductos())
  }
  
  @Test
  fun `verificamos que el total de mesa(50,0) y silla(20,0) sea 70,0`() {
    
    //agrego los productos, porque necesito saber cuales son sus precios para poder hacer el calculo total
//    listaCarritoMejorada.agregarProducto("Mesa", 50.0)
//    listaCarritoMejorada.agregarProducto("Silla", 20.0)
    productosCocina()
    
    val resultado = listaCarritoMejorada.calcularTotal() //<- se puede reemplazar si hace falta.
    //la forma de "guardar" el resultado en una variable, es mas un hecho didactica y de comprension.
    //que necesaria en realidad.
    //tranquilamente podrias eliminar la variable y directamente llamar a calcular total en el assertEquals y estaria bien
    //assertEquals(70.0, listaCarritoMejorada.calcularTotal(), 00.0)
    
    assertEquals(70.0, resultado, 00.0)
  }
  
  @Test
  fun `eliminamos un producto de la lista y verificamos que solo quede 1`() {

//    listaCarritoMejorada.agregarProducto("Mesa", 50.0)
//    listaCarritoMejorada.agregarProducto("Silla", 20.0)
    productosCocina()
    
    listaCarritoMejorada.eliminarProducto("Mesa")
    //eliminamos un producto segun su nombre
    
    assertEquals(1, listaCarritoMejorada.cantidadDeProductos())
    
    //Una de las formas de ver el proceso que hace el test es usar println
    //listaCarritoMejorada.agregarProducto("Mesa", 50.0)
    //println("Después de agregar Mesa: ${listaCarritoMejorada.cantidadDeProductos()} productos")
    //se agrega el primer producto y se guarda internamente en el mutableListOf.
    
    //listaCarritoMejorada.agregarProducto("Silla", 20.0)
    //println("Después de agregar Silla: ${listaCarritoMejorada.cantidadDeProductos()} productos")
    //se agrega el segundo producto y se guarda internamente en el mutableListOf.
    //marcara como que ya hay dos productos
    
    //listaCarritoMejorada.eliminarProducto("Mesa")
    //println("Después de eliminar Mesa: ${listaCarritoMejorada.cantidadDeProductos()} productos")
    //mostrara el elemento que se elimino y cuantos quedan
    
    //assertEquals(1, listaCarritoMejorada.cantidadDeProductos())
    //println("Resultado esperado: 1, Resultado obtenido: ${listaCarritoMejorada.cantidadDeProductos()}")
    //por ultimo muestra el resultado espero y el resultado obtenido.
    //todo: ESTO ES SOLO CON FINES DIDACTICOS - en produccion real no se usa println en los tests
  }
  
  @Test
  fun `verificamos que el carrito este vacio, si lo esta devuelve true`() {
    
    productosCocina()
    
    listaCarritoMejorada.eliminarProducto("Mesa")
    listaCarritoMejorada.eliminarProducto("Silla")
    
    assertTrue(listaCarritoMejorada.estaVacio())
  }
  
  @Test
  fun `verificamos que el carrito con productos, no este vacio`() {
    productosCocina()
    assertFalse(listaCarritoMejorada.estaVacio())
  }
  
  @Test
  fun `se agrega un producto con nombre vacio que lanza una excepcion`() {
    assertThrows<IllegalArgumentException>{
      listaCarritoMejorada.agregarProducto("", 50.0)
    }
    //en Kotlin, assertThrows puede usarse de dos formas:
    //assertThrows(IllegalArgumentException::class.java) { ... } // forma Java
    //assertThrows<IllegalArgumentException> { ... }  // forma Kotlin
  }
  
  @Test
  fun `se elimina un producto que no existe`() {
    assertThrows<IllegalArgumentException>{
      listaCarritoMejorada.eliminarProducto("Mesa")
    }
  }
  
  @Test
  fun `si se elimina un producto con nombre vacio, lanza excepcion`() {
    assertThrows<IllegalArgumentException> {
      listaCarritoMejorada.eliminarProducto("")
    }
  }
  
  @Test
  fun `si se agrega producto con precio negativo, lanza excepcion`() {
    assertThrows<IllegalArgumentException> {
      listaCarritoMejorada.agregarProducto("Termo", -40.0)
    }
  }
  
  @Test
  fun `si se agrega producto con precio cero, lanza excepcion`() {
    assertThrows<IllegalArgumentException> {
      listaCarritoMejorada.agregarProducto("Mate", 00.0)
    }
  }
  //esperas que funcione -> assertEquals, assertTrue, assertFalse
  //esperas que explote correctamente -> assertThrows
  //assertThrows hace que el test SI pase cuando explota.
  //el test pasa porque exploto de la forma correcta, no porque algo funciono bien.
  
  //Formula para nombrar tests negativos:
  //`[accion] | con [dato invalido] | lanza excepcion`
  //Ejemplos:
  //`si se agrega un producto | con precio negativo | , lanza excepcion`
  //`si se elimina un producto | con nombre vacio |, lanza excepcion`
  
}