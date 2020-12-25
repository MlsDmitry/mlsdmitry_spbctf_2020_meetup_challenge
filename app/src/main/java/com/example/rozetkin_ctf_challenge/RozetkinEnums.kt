package com.example.rozetkin_ctf_challenge

object AuthType {
    const val AUTH_SIGN_UP_ACTION = 0
    const val AUTH_SIGN_IN_ACTION = 1
}

object NfcAction {
    const val READ_FOR_LOGIN = 0
    const val READ_FOR_REGISTRATION = 1
    const val WRITE_LOGIN = 2
    const val CLEANUP = 3
    const val READ_ONLY_UID = 4
    const val WRITE_ALL = 5
    const val WRITE_CARD_NUMBER = 6
}