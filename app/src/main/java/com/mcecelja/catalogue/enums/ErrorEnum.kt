package com.mcecelja.catalogue.enums

enum class ErrorEnum(val message: String) {

    BAD_CREDENTIALS("Netočno korisničko ime ili lozinka!"),
    BAD_REQUEST("Greška pri obradi podataka!"),
    PASSWORD_MISMATCH("Lozinke se ne podudaraju!"),
    INVALID_EMAIL_ADDRESS("E-mail adresa nije dobro formatirana!")
}