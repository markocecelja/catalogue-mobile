package com.mcecelja.catalogue.enums

import com.mcecelja.catalogue.utils.handlers.ErrorHandler
import com.mcecelja.catalogue.utils.handlers.SessionExpiredErrorHandler

enum class ErrorEnum(val message: String, val errorHandler: ErrorHandler?) {

    SESSION_EXPIRED("Potrebna ponovna prijava!", SessionExpiredErrorHandler()),
    BAD_CREDENTIALS("Netočno korisničko ime ili lozinka!", null),
    BAD_REQUEST("Greška pri obradi podataka!", null),
    PASSWORD_MISMATCH("Lozinke se ne podudaraju!", null),
    INVALID_EMAIL_ADDRESS("E-mail adresa nije dobro formatirana!", null),
    NON_EXISTING_PRODUCT("Odabrani proizvod ne postoji!", null),
    NON_EXISTING_ORGANIZATION("Organizacija ne postoji!", null),
    USERNAME_ALREADY_IN_USE("Korisničko ime se već koristi!", null)
}