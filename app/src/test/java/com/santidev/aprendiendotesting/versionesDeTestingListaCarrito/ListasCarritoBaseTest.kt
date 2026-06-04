package com.santidev.aprendiendotesting.versionesDeTestingListaCarrito

import com.santidev.pruebatesting.funciones.versionesDeTestingListaCarrito.ListasCarritoBase
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListasCarritoTestBase {
  
  @Test
  fun `validar que se agrega un producto a la lista`() {
    val lista = ListasCarritoBase()
    val resultado = lista.agregarProducto("Mesa")
    /*assertEquals(resultado.contains("Mesa"), true)*/
    /*assertTrue(resultado.contains("Mesa"))*/
    assertTrue("Mesa" in resultado)
  }
  //Contains lo uso para verificar si el producto "Mesa" existe dentro de la lista de productos
  //hay otras formas de poder hacer este mismo test :
  //assertEquals(resultado.contains("Mesa"), true) // <- actual
  //assertTrue(resultado.contains("Mesa")) // <- usando assertTrue es mas legible y es exactamente para eso que existe.
  //assertTrue("Mesa" in resultado) // <- "in" es lo mismo que contains pero mas idiomatico en Kotlin
  
  @Test
  fun `validamos que se agreguen 2 productos al carrito y si los hay que los muestre`() {
    //Para entender esto con el patron AAA es mas facil
    // Arrange <- primera A
    val lista = ListasCarritoBase()
    
    // Act <- segunda A
    lista.agregarProducto("Mesa") // <-primer producto
    lista.agregarProducto("Silla") // <-segundo producto
    /*lista.agregarProducto("Mesa, Silla")*/ // <-producto invalido, no lo va tomar
    
    // Assert <- tercera A
    assertEquals(2, lista.cantidadDeProductos())
    //espero que haya 2 productos, y nos fijamos dentro de la lista en la cantidad de productos
  }
  //para poder crear varios productos dentro de una lista, tengo que agregar por separado cada uno de estos.
  //No puedo poner "Mesa, silla", ya que al agregarlo como producto solo toma 1 de estos: ("Mesa")
  //si quiero agregar un segundo producto, literalmente tengo que agregar un segundo producto.
  //puedo tener la cantidad de productos que quiera, siempre en cuando los cree por separado
  
  @Test
  fun `verificamos si al eliminar un producto devuelve la cantidad adecuada que es 1`() {
    val lista = ListasCarritoBase()
    
    lista.agregarProducto("Mesa")
    lista.agregarProducto("Silla")
    
    lista.eliminarProducto("Silla")
    
    assertEquals(1, lista.cantidadDeProductos())
  }
  //primero creamos ambos productos dentro de nuestro carrito
  //y despues podemos eliminar uno de esos productos
  
  @Test
  fun `calculamos el precio de los productos que hay en la lista`() {
    val lista = ListasCarritoBase()
    
    val precios = listOf(10.0, 20.0, 30.0)
    //como no tengo una funcion que toma el valor de los productos. Puedo crear una lista imaginaria de precios
    
    val resultado = lista.calcularTotal(precios)
    
    assertEquals(60.0, resultado, 0.0)
    //primer valor = 60.0 <- es la suma total esperada de los precios de los productos.
    //segundo valor = resultado <- me devuelve el precio de los productos.
    //tercer valor = de vuelve la "tolerancia" de diferencia que se permite entre el valor real y el valor esperado.
    //si espero un valor exacto, entonces uso : 0.0...
    //si tolero un valor rondando el 10% entonces uso : 0.1... : acepta entre 59.9 y 60.1
    //si tolero una diferencia de hasta 1.0 entre el valor esperado y el obtenido pongo: 1.0
    //ejemplo: espero 60.0 pero acepto un valor entre 59.0 y 61.0
  }
  
  @Test
  fun `verificamos si el carrito esta vacio, si lo esta devuelve true`() {
    val lista = ListasCarritoBase()
    
    //como solo quiero verificar si la lista esta vacia, entonces no agrego ningun producto
    
    assertTrue(lista.estaVacio())
  }
  
  @Test
  fun `verificamos que hayan un total 2 productos en la lista, si los hay confirmamos con un true`() {
    val lista = ListasCarritoBase()
    
    lista.agregarProducto("Mesa")
    lista.agregarProducto("Silla")
    
    /*assertTrue(lista.cantidadDeProductos() == 2)*/
    assertEquals(2, lista.cantidadDeProductos())
  }
  //en este hasy 2 formas de hacerlo =
  //usando = assertTrue(lista.cantidadDeProductos() == 2): -> si falla solo dice expected: true but was: false, no sabes enrealidad que valor obtuvo.
  //usando = assertEquals(2, lista.cantidadDeProductos()): -> si falla dice expected: 2 but was: 5, ves exactamente que valores devolvio
  //La segunda forma es mas recomendable de hacer, ya que podes ver el error en mas detalle
  
}