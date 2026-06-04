package com.santidev.aprendiendotesting.funcionesDePractica

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class ValidarTextoTest {
  
  @Test
  fun `validar que el nombre correcto es Juan si lo es devuelve true`() {
    val validacion = ValidarTexto()
    val resultado = validacion.validarNombre("Juan")
    assertTrue(resultado) //<- assertTrue lo uso cuando necesito que la respuesta esperada sea Verdadero
  }
  //todo:Descubrimientos =
  //Esta funcion no valida que el nombre esperado sea "Juan", valida que el campo de texto, efectivamente tenga un texto.
  //Mientras que tenga un caracter sean (letras, numeros, simbolos, INCLUSO ESPACIOS VACIOS), devolvera verdadero.
  //La funcion no espera un string, O numero, solo espera que el campo no este vacio para poder devolver.
  //El campo esta vacio? = True O false (Boolean).
  //Para solucionar este error de "Espacios vacios", hay que modificar el codigo de la funcionalidad =
  //  fun validarNombre(nombre: String): Boolean {
  //    return nombre.isNotEmpty() <- Reemplazar isNotEmpty x isNotBlank
  //  }
  
  @Test
  fun `validar si el nombre esta vacio, si lo esta devuelve false`() {
    val validacion = ValidarTexto()
    val resultado = validacion.validarNombre("") //<- aun con espacio vacio, el test pasa
    assertFalse(resultado) //<- assertFalse lo uso cuando necesito que la respuesta esperada sea Falso
  }
  // isNotBlank() no pregunta "¿hay caracteres?"
  // pregunta "¿hay contenido real?"
  // Los espacios son caracteres pero NO son contenido real
  
  @Test
  fun `nombre con solo espacios devuelve false`() {
    val resultado = validacion.validarNombre(" ")
    assertFalse(resultado)
  }
  
  lateinit var validacion: ValidarTexto
  
  @Before
  fun funcionPrevia() {
    validacion = ValidarTexto()
  }
  
  @Test
  fun `verificamos que email con solo arroba devuelve false`() {
    val resultado = validacion.validarEmail("@")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que email sin texto antes del arroba devuelve false`() {
    val resultado = validacion.validarEmail("@gmail.com")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que email sin dominio devuelve false`() {
    val resultado = validacion.validarEmail("test@")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que email sin arroba devuelve false`() {
    val resultado = validacion.validarEmail("testgmail.com")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que el email solo con arroba y dominio devuelve false`() {
    val resultado = validacion.validarEmail("@.com")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que password sin mayuscula devuelve false`() {
    val resultado = validacion.validarPassword("12345678a!")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que password sin minuscula devuelve false`() {
    val resultado = validacion.validarPassword("12345678A!")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que password sin numero devuelve false`() {
    val resultado = validacion.validarPassword("AbcdefgH!")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que password sin simbolo devuelve false`() {
    val resultado = validacion.validarPassword("Abcdefg1")
    assertFalse(resultado)
  }
  
  @Test
  fun `verificamos que password con todos los requisitos devuelve true`() {
    val resultado = validacion.validarPassword("Abcdefg1!")
    assertTrue(resultado)
  }
  
  @Test
  fun `validamos que el usuario tenga 18 años si los tiene que devuelva true`() {
    val resultado = validacion.validarEdad(18)
    assertTrue(resultado)
  }
  
  @Test
  fun `validamos que el usuario tenga menos de 18 si los tiene que devuelva false`() {
    val resultado = validacion.validarEdad(17)
    assertFalse(resultado)
  }
  
  @Test
  fun `validamos que edad negativa devuelve false`() {
    val resultado = validacion.validarEdad(-17)
    assertFalse(resultado)
  }
}