package com.larkisedosti.app.data

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

fun appCheckFactory(): AppCheckProviderFactory = DebugAppCheckProviderFactory.getInstance()
