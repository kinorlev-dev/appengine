package com.kinorlev.appengine.extension

fun Exception.Ext_message() = message ?: "un-known error message"