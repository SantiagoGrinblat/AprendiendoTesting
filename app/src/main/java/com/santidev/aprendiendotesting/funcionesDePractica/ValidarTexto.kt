package com.santidev.aprendiendotesting.funcionesDePractica

class ValidarTexto {
  fun validarNombre(nombre: String): Boolean {
    return nombre.isNotBlank()
  }
  fun validarEmail(email: String): Boolean {
    return email.contains("@") &&
        //Esto verifica que haya exactamente un solo @
        email.count { it == '@' } == 1 &&
        //Esto verifica que tenga algo antes del @
        email.indexOf("@") > 0 &&
        //Esto verifica que tenga un punto para poder agregar el dominio
        email.contains(".") &&
        //Esto verifica que el punto este despues del @
        email.indexOf("@") < email.lastIndexOf(".")
  }
  fun validarPassword(password: String): Boolean {
    return password.length >= 8
        //verifica que el password tenga al menos 1 caracter en mayuscula
        && password.any { it.isUpperCase() }
        //verifica que el password tenga al menos 1 caracter en minuscula
        && password.any { it.isLowerCase() }
        //verifica que el password tenga al menos 1 numero
        && password.any { it.isDigit() }
        //verifica que el password tenga al menos 1 simbolo
        && password.any { it in "!@#\$%^&*+{]}[,.?/_-=" }
  }
  fun validarEdad(edad: Int): Boolean {
    return edad >= 18
  }
}