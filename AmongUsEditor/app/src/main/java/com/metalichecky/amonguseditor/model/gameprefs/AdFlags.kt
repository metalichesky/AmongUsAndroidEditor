package com.metalichecky.amonguseditor.model.gameprefs

enum class AdFlags(flag: Int) {//Personalized 0x00, NonPersonalized 0x01, Accepted 0x80, Purchased 0x82
    PERSONALIZED(0x00),
    NON_PERSONALIZED(0x01),
    PURCHASED(0x82),
    ACCEPTED(0x80)
}