package com.larkisedosti.app.data

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

fun appCheckFactory(): AppCheckProviderFactory = PlayIntegrityAppCheckProviderFactory.getInstance()
