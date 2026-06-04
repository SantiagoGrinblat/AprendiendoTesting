package com.santidev.aprendiendotesting.EjemploBasicoDeMockk

import com.santidev.pruebatesting.funciones.usandoMock.EjemploBasicoDeMockk.MiClase
import com.santidev.pruebatesting.funciones.usandoMock.EjemploBasicoDeMockk.Servicio
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class MiClaseTest {
  
  private lateinit var servicioMock: Servicio
  private lateinit var miClase: MiClase
  
  @Before
  fun funcionDeClases() {
    servicioMock = mockk()
    miClase = MiClase(servicioMock)
  }
  
  @Test
  fun `procesar que devuelve el mensaje del servidor`() {
    //Arrange
    //servicioMock = <- clase dentro del before, por si solo no hace nada
    //no es necesario ponerlo, pero pueder ser mas didactico que este en pantalla aunque no haga nada
    //cuando es buena idea ponerlo en el Arrange?
    //solo es necesario (y recomendado) ponerlo en el Arrange si vas a especificar un comportamiento distinto al que definiste por defecto en el before.
    //servicioMock = mockk<Servicio>
    //MockK en si es una libreria que crea un objeto falso de la clase que le indicas
    //mockk = es una funcion, entre los ( <> ) les decis que CLASE queres simular y se ejecuta como una funcion
    
    every { servicioMock.obtenerMensaje() } returns "Hola simulado"
    //every Es una funcion de MockK que dice "cuando llamen a este metodo, hace esto":
    //dentro de las llaves pongo la funcion que quiero simular
    //returns devuelve el valor que quiero que devuelva
    //importante! returns (lleva: S), porque es una funcion de mockk no la palabra clave de kotlin.
    //en Kotlin, return y throw son palabras reservadas del lenguaje.
    //por eso MockK usa returns y throws (en plural) para sus funciones.
    
    //Act
    val resultado = miClase.procesarSaludo()
    
    //Assert
    assertEquals("Hola simulado", resultado)
    
    //verify se usa para confirmar que esto (el metodo) fue llamado
    verify { servicioMock.obtenerMensaje() }
    
    //no reemplaza verify a assertEquals.
    //uno dice si se llamo al servicio = verify
    //el otro si el resultado es correcto = assertEquals
    //cuando uso VERIFY ? = para conprobar que los pasos internos se ejecutaron.
    //Ejemplo del banco:
    //verify { sistema.registrar(100.0) } -> se registro el deposito?
    //verify { impresora.imprimirComprobante() } -> se imprimio el comprobante?
    //assertEquals(100.0, cuenta.getSaldo()) -> el saldo es correcto?
    //todo: En un test completo se usa los dos casos
  }
  
  //Hasta ahora usamos verify para simular una respuesta exitosa.
  //Pero en el mundo real tambien necesitas simular errores:
  @Test
  fun `procesar lanza excepcion cuando el servio falla`() {
    
    every { servicioMock.obtenerMensaje() } throws Exception("Error de conexion")
    //cuando llamo al metodo, simulo un error de conexion
    //assertThrows verifica que ese error ocurra correctamente
    
    assertThrows<Exception> {
      miClase.procesarSaludo()
    }
  }
  
  //tambien podemos usar verify para generar varias llamadas.
  //por que importa?
  //imaginate que tenes un metodo que debería llamar al servicio de pago exactamente una vez
  //si lo llama dos veces, estas cobrando dos veces al cliente
  //por eso es importante verificar la cantidad exacta de llamados que hago
  @Test
  fun `verificamos que la cantidad de pago sea exactamente una`() {
    
    every { servicioMock.obtenerMensaje() } returns "Hola mundo"
    //importante recordar usar el returns, si NO lo ponemos el error sera este =
    //no answer provided for Servicio(#1).obtenerMensaje(). el error te esta diciendo:
    //"Loco, me pediste que use este servicio falso, pero no me dijiste que responder cuando le preguntan por el mensaje".
    
    miClase.procesarSaludo() // exactly = 1
    miClase.procesarSaludo() // + exactly = 2
    //para poder usar en exactitud 2 elementos, tenes que agregrar un segundo elemento.
    //como habiamos echo en el carrito mejorado agregando un seguno producto.
    
    verify(exactly = 2) { servicioMock.obtenerMensaje() }
    //cuando usar exactly?
    //cuando es critico que un metodo se llame una cantidad especifica de veces
    //ejemplo: un pago no puede procesarse dos veces
  }
  
  //ahora vamos a probar atLeast y atMost.
  //ambas son variantes de verify para cuando no se necesita una cantidad exacta sino un rango
  
  //atLeast -> mínimo de veces:
  @Test
  fun `verificamos que se ejecute al menos 1 vez`() {
    
    every { servicioMock.obtenerMensaje() } returns "Hola mundo"
    
    miClase.procesarSaludo()
    
    verify(atLeast = 1) { servicioMock.obtenerMensaje() }
    //atLeast = quiero que se ejecute un minimo de 1 vez
    //si intento 0 veces -> falla
    //si intento 1, 2 o 3 veces -> pasa
  }
  
  //atMost -> maximo de veces:
  @Test
  fun `verificamos que se ejecute como maximo 3 veces`() {
    
    every { servicioMock.obtenerMensaje() } returns "Hola mundo por 2"
    
    miClase.procesarSaludo()
    miClase.procesarSaludo()
    miClase.procesarSaludo()
    
    verify(atMost = 3) { servicioMock.obtenerMensaje() }
    //atMost = quiero que se ejecute un maximo de 3 veces
    //si intento 4 veces -> falla
    //si intento 1, 2 o 3 veces -> pasa
  }
  
  //Combinados los dos:
  @Test
  fun `verificamos que el metodo se ejecute entre 1 y 3 veces`() {
    
    every { servicioMock.obtenerMensaje() } returns "Hola mundo por 3"
    
    miClase.procesarSaludo()
    miClase.procesarSaludo()
    
    verify(atLeast = 0, atMost = 3) { servicioMock.obtenerMensaje() }
    //quiero que se ejecute un minimo de 1 vez y que sea un maximo de 3 veces
    //se llamo entre 1 y 3 veces -> pasa
    //se llamo 0 o 4 veces -> falla
    //dato extra: si intentas 0 veces, atMost = 3 tambien pasa (porque cero es menor que tres).
    //por eso para que el test falle necesitas un numero que sea menor que 0 y esos son lo numeros negativos
    //si queres que se llame al menos una vez pero maximo tres, usas la combinacion de atLeast y atMost.
  }
  
  //Relaxed =
  @Test
  fun `verificamos que devuelva un mensaje`() {
    servicioMock = mockk<Servicio>(relaxed = true) //el problema que resuelve relaxed:
    //cuando creas un mock normal, MockK es estricto. si no declaras every para cada metodo, explota
    //osea que literalmente relaxed le dice al codigo, relajate y deja que pase,
    //pero no va a pasar solo, lo voy a pasar con los valores minimos para que no se rompa
    //si no declaras every mientas usas relaxed -> devuelve valores minimos por defecto:
    //String = "", Int = 0, Boolean = false, List = emptyList()
    //Dato extra: Esto es muy util cuando tenes interfaces grandes y solo te interesa testear un metodo especifico.
    //ignorando todos los demas llamados secundarios.
    every { servicioMock.obtenerMensaje() } returns "Hola mundo desde el server"
    
    miClase = MiClase(servicioMock)
    //cuando hacemos el llamado en relaxed en el nuevo servicioMock, tenemos que tambien agregar la clase.
    //si mas tarde en el test creas un nuevo mock con mockk(relaxed = true), ese es un objeto distinto en la memoria.
    //porque en el before lo estoy configurando de manera estricta sin relaxed
    //si no actualizas miClase, esta seguira apuntando al mock viejo (el del before), y por eso el test falla.
    
    val resultado = miClase.procesarSaludo()
    
    assertEquals("Hola mundo desde el server", resultado)
    verify(exactly = 1) { servicioMock.obtenerMensaje() }
    
    //relaxed NO reemplaza a every.
    //cuando usar relaxed?:
    //cuando no te importa el valor de retorno, solo queres verificar llamadas (verify)
    //cuando usar every:
    //cuando necesitas controlar el resultado, porque el valor afecta la logica
  }
  
  @Test
  fun `verificamos que se llama al metodo sin usar every`() {
    servicioMock = mockk<Servicio>(relaxed = true)
    miClase = MiClase(servicioMock)
    
    // Act, se llama al metodo
    miClase.procesarSaludo()
    
    // Assert
    verify(exactly = 1) { servicioMock.obtenerMensaje() }
  }
  //relaxed = true hace que el mock sea permisivo:
  //no necesitas declarar every para cada metodo, si no declaras every, devuelve valores por defecto ("", 0, false)
  //muy util cuando solo queres verificar que se llamo, no que devuelve
  //IMPORTANTE: relaxed solo aplica al mock, no a las clases reales
  //val servicioMock = mockk<Servicio>(relaxed = true) // permisivo
  //val miClase = MiClase(servicioMock) // clase real, no tiene relaxed
  
  //obtener id especifico de un usuario
  @Test
  fun `recuperamos un usuario con el id asignado`() {
    every { servicioMock.buscarUsuario(23) } returns "Usuario simulado"
    //con el every simulamos un usario con el id 23
    
    val resultado = miClase.obtenerUsuario(23)
    //con el resultado, almacenamos el usuario con el id 23
    //si no tuvieramos este almacenamiento y lo llamaramos directamente desde el assertEquals aun asi funcionaria.
    //no importa si el resultado esta en una variable o no
    
    verify(exactly = 1) { servicioMock.buscarUsuario(23) }
    //en caso de no tener almacenado el usuario, el test falla. = (val resultado = miClase.obtenerUsuario(23))
    //porque verify no puede verificar que el metodo fue llamado, si este no existe
    
    assertEquals("Usuario simulado", resultado) // resultado / servicioMock.buscarUsuario(23)
    //de ambas formas funciona
    
    //verify(exactly = 1) { servicioMock.buscarUsuario(23) }
    //en cambio si lo ponemos aca, si el test pasa, porque primero obtiene el dato y despues verifica si el meotodo fue llamado correctamente
    //verify debe ir DESPUES de llamar al metodo.
    //si va antes, falla porque el metodo todavia no fue llamado
  }
  
  @Test
  fun `recuperamos un usuario con el id usando answers`() {
    //answers es una alternativa mas flexible que returns.
    //en vez de devolver siempre un valor fijo (como hace returns), ejecuta un bloque de codigo.
    //dentro del bloque se puede acceder a los argumentos que se pasaron al metodo.
    //que argumento se pasa al metodo? buscarUsuario(5) <- el 5 es el argumento que se le pasa al metodo.
    //y para acceder a ese valor, se usa firstArg (que dice accedo al primer argumento) con un tipo entero (5)
    val id = 3456
    every { servicioMock.buscarUsuario(id) } answers {
      val idArg: Int = firstArg<Int>()
      "Otro Usuario simulado = $idArg"
      //firstArg<Int>() = Es una funcion de MockK
      //<Int> le decís de que tipo es ese argumento (en una lista de usuarios no tendremos id = 30.5, asi que queda por defecto)
    }
    //en caso que la funcion recibiera dos parametros, existe secondArg<String>() para el segundo
    //<String>, en caso que el usuario sea (id = 3445214, nombre = "Santiago")
    
    val resultado = miClase.obtenerUsuario(id)
    
    assertEquals("Otro Usuario simulado = $id", resultado)
    //println("VER QUE NUMERO DEVOLVIO: ${id} ") <- 3456
    verify(exactly = 1) { servicioMock.buscarUsuario(id) }
  }
  
  // any = para devolver cualquier id de usuario usando answers
  @Test
  fun `recuperamos un usuario con el id random`() {
    every { servicioMock.buscarUsuario(any()) } answers {
      val idArg: Int = firstArg<Int>()
      println("VERIFICAR QUE OBTENGO ACA VERSION : ANY ${idArg}")
      "Usuario simulado $idArg"
      //idArg, No tiene idea por el momento que el id es 556345999, pero cuando llegue al final del codigo lo va a saber.
      //porque?, porque "buscarUsuario", le dice al "obtenerUsuario", busca un id, cuando lo tengas avisame.
      //como tengo que pasarle un id para poder ejecutar el test, se lo doy a la clase obtenerUsuario.
      //y es ahi donde se comunican las clases, ya que ambas dependen de la otra. le dice el id es este.
      //y es entonces donde idArg, sabe que el id es 556345999. porque mas que sea establecido despues de su ejecucion,
    }
    
    val idUser = 556345999
    
    val resultado = miClase.obtenerUsuario(idUser)
    //ID hardcodeado para usar de caso de ejemplo
    
    assertEquals("Usuario simulado $idUser", resultado)
    println("VERIFICAR QUE OBTENGO ACA, VERSION ID HARDCODEADO : ${idUser}")
    verify(exactly = 1) { servicioMock.buscarUsuario(idUser) }
  }
  
  // any = para devolver cualquier id de usuario SIN USAR answer
  @Test
  fun `otro ejemplo de any sin answer`() {
    every { servicioMock.buscarUsuario(any()) } returns "Usuario simulado"
    
    val resultado = miClase.obtenerUsuario(465278359)
    //hasta un maximo de 9 digitos.
    //si pongo 1 digito mas tendria que cambiar el tipo en la funcion buscarUsuario y obtenerUsuario de Servicio.
    
    assertEquals("Usuario simulado", resultado)
    verify(exactly = 1) { servicioMock.buscarUsuario(465278359) }
  }
  
  //Que problema resuelve un slot?
  //Con verify podes confirmar que un metodo fue llamado:
  //verify { servicioMock.buscarUsuario(23) }
  //Pero si no sabes de antemano con que valor fue llamado, no podes verificarlo así.
  //Necesitas capturarlo para inspeccionarlo despues.
  
  //Una analogia comun que se suele usar es:
  //Imaginate que sos detective y queres saber que numero de telefono llamo alguien.
  //No podes estar ahí en el momento, entonces pones un grabador que captura el numero. Despues revisas el grabador.
  //Asi es como funciona slot =
  //verify -> "llamo a alguien?"
  //slot -> "a que numero llamo exactamente?"
  //El codigo basico seria algo asi
  @Test
  fun `capturo el numero con un slot`() {
    val slot = slot<Int>()
    //con slot, creo una "Jaula" le digo que esta jaula, en este caso, solo funciona para capturar (leones) numeros de tipo Int.
    
    every { servicioMock.buscarUsuario(capture(slot)) } returns "Usuario simulado"
    //busco el leon (el usuario) y que le digo, en cuanto tengas O veas uno, capturalo. y devolve este mensaje.
    
    miClase.obtenerUsuario(52)
    //obtengo el usuario / "leon", con el id 52, y lo capturo con O lo guardo dentro del slot
    
    println("EL NUMERO CAPTURADO ES ${slot.captured}")
    //con slot.captured -> accedo al valor que fue capturado
    //solo tiene valor DESPUES de que el metodo fue llamado
    // si lo llamo antes de miClase.obtenerUsuario(52), explotaria
    //Veo que hay dentro de la jaula, que leon atrape (numero entero)
    
    assertEquals(52, slot.captured)
    //verifico que sea el leon (numero) que esperaba atrapar. y la comparo con el obtenido.
  }
  
}