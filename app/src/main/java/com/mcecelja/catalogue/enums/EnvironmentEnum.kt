package com.mcecelja.catalogue.enums

enum class EnvironmentEnum (val url: String) {

    LOCAL_NETWORK("http://192.168.0.112:8080/api/"),
    LOCAL_MOBILE_NETWORK("http://192.168.176.14:8080/api/"),
    PROD("https://catalogue-backend.herokuapp.com/api/");
}