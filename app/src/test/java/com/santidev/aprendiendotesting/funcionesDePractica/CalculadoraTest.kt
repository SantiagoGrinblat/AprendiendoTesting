package com.santidev.aprendiendotesting.funcionesDePractica

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class CalculadoraTest {
  
  //Antes de empezar vamos a aclarar como se definiran las funciones:
  //Seran con backticks.
  //`sumar 8 mas 6 devuelve 14`
  //por que? =
  //Se lee como una oracion normal
  //Cuando el test falla, JUnit te muestra el nombre exacto en el reporte de errores
  //Podes usar espacios, acentos, y hasta signos si queres.
  
  //Hay otra forma de hacerlo?.
  //Si, con guiones bajos =
  //sumar_8_mas_6_devuelve_14
  //cual es la diferencia?:
  //Es dificil de leer de un vistazo
  //Parece codigo, no una descripcion
  //Si el nombre es largo se vuelve ilegible rapidamente
  
  //todo La regla general:
  // El nombre del test debe poder leerlo alguien que no sabe programar y entender que se esta verificando.
  
  //Empecemos con un test basico
  
  @Test  //<- se utiliza para seleccionar que la siguiente funcion es un test
  fun `sumar 8 mas 6 devuelve 14`() {  //funcion descriptiva, dice explicitamente lo que hace
    val calculadora = Calculadora()  // se crea una instancia de la clase calculadora que representa a la funcion calculadora
    val resultado = calculadora.sumar(8, 6) // se almacena en resultado para la siguiente operacion.
    assertEquals(14, resultado) // se compara el resultado esperado con el obtenido.
    //assertEquals es una funcion que verifica que un valor esperado coincida exactamente con un valor real obtenido durante la ejecucion del codigo.
    //esta funcion espera que el resultado sea 14 y lo compara con el (resultado = variable) obtenido.
  }
  
  @Test
  fun `restar 8 menos 6 devuelve 2`() {
    val calculadora = Calculadora()
    val resultado = calculadora.restar(8, 6)
    assertEquals(2, resultado)
  }
  
  @Test
  fun `multiplicar 8 por 6 devuelve 48`() {
    val calculadora = Calculadora()
    val resultado = calculadora.multiplicar(8, 6)
    assertEquals(48, resultado)
  }
  
  @Test
  fun `dividir 8 entre 6 devuelve 1`() {
    val calculadora = Calculadora()
    val resultado = calculadora.dividir(8, 6)
    assertEquals(1, resultado)
  }
  
  //TEST BASICO
  //-------------------------------------------------------------------------
  //TEST MEJORADO
  //Usando patron AAA
  
  @Test
  fun `sumando 8 mas 6 devuelve 14`() {
    //Arrage = Organizar < - primera A
    val calculadora = Calculadora()  // que se va a testear
    val numero1 = 8 // dato de entrada
    val numero2 = 6 // dato de entrada
    //Organizo que voy a testear
    
    //ACT = Actuar <- segunda A
    val resultado = calculadora.sumar(numero1, numero2)
    //Actuar que se va a testear y como
    
    //Assert = Afirmar <- tercera A
    assertEquals(14, resultado)
    // Afirmar que el resultado esperado (14) coincida con el resultado obtenido
  }
  // el resto del codigo seria lo mismo para los 3 ejemplos, solo hay que cambiar 2 cosas :
  //calculadora.sumar(numero1, numero2) <- que voy a testear.
  //assertEquals(14, resultado) <- afimar el resultado segun lo que se teste
  
  //-------------------------------------------------------------------------
  //TEST CON TIPs PRO
  //Agregando Before, para ahorrar escribir tanto codigo repetido.
  
  lateinit var calculadora: Calculadora
  //lateinit = le decis a kotlin "esta variable va a existir, pero la voy INICIALIZAR despues".
  //Si no le pones lateinit, kotlin te obligaria a darle un valor a la variable calculadora.
  //var = es muy importante que sea variable y no un valor, ya que este ultimo sera asignado en la funcionPrevia
  
  @Before
  fun funcionPrevia() {
    calculadora = Calculadora()
  }
  // @Before = hace que funcionPrevia() se ejecute automaticamente antes de cada @Test.
  // sin necesidad de llamarla manualmente.
  // Su objetivo es inicializar la variable calculadora con una instancia nueva.
  // antes de que cada test comience, garantizando que ningun test afecte al siguiente.
  
  @Test
  fun `restando 8 menos 6 devuelve 2`() {
    val resultado = calculadora.restar(8, 6)
    assertEquals(2, resultado)
  }
  
  @Test
  fun `multiplicando 8 por 6 devuelve 48`() {
    val resultado = calculadora.multiplicar(8, 6)
    assertEquals(48, resultado)
  }
  
  @Test
  fun `dividiendo 8 entre 6 devuelve 1`() {
    val resultado = calculadora.dividir(8, 6)
    assertEquals(1, resultado)
  }
  // Nota: esta division es entera (Int), por eso devuelve 1
  // si usara el tipo Double devolveria : 1.33......
  //todo: QUE MEJORO? = El beneficio es que no repetis val calculadora = Calculadora() en cada test, como se hacia antes
  
  //-------------------------------------------------------------------------
  //TEST CON EXCEPCIONES (errores)
  //Se mostraran y explicaran algunos casos de uso de porque testear generando un error.
  //Esto nos ayudara a pensar : ¿Que pasa si alguien le manda un dato invalido a esta funcion?.
  //De esta forma podremos entender cuando es necesario testear, incluso esperando un error en lugar de un resultado Y por que?.
  
  @Test
  fun `dividir por cero lanza excepcion`() {
    assertThrows(ArithmeticException::class.java) {
      calculadora.dividir(8, 0) //<- esto detona la bomba de tiempo
    }
  }
  //assertThrows = Es una funcion de JUnit, como assertEquals pero diferente.
  //En lugar de verificar un resultado, verifica que se lance una excepcion (evento inesperado).
  //No genera el error, simplemente verifica que efectivamente sea un error.
  //Si la excepcion NO ocurre, el test falla.
  
  //ArithmeticException = es la excepcion que lanza Kotlin/Java cuando dividis por cero
  //Lo que dice es : Espero que ese error sea de tipo aritmetico.
  
  //por que seria una bomba de tiempo?, porque es imposible dividir por eso, entonces genero el error aproposito.
  //todo = Testear errores no es testear que algo funciona mal, es testear que falla de la forma correcta y esperada.
  
  //todo:siempre tengo que tener tests que generen errores a proposito??
  //No, no siempre. Depende de la logica de la funcion.
  //¿Tu funcion puede recibir una entrada invalida o imposible?
  //Si sí → testea ese error
  //Si no → no hace falta
  
  @Test
  fun `restar con numeros negativos -8 menos -6 devuelve -2`() {
    val resultado = calculadora.restar(-8, -6)
    assertEquals(-2, resultado)
  }
  // Verifica que restar dos numeros negativos devuelve el resultado correcto
  // -8 - (-6) = -8 + 6 = -2
}