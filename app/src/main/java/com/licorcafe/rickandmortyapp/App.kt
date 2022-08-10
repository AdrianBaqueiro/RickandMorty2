package com.licorcafe.rickandmortyapp

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory


class App : Application(), ModuleOwner, ImageLoaderFactory {

    override val appModule by lazy { AppModule.create() }
    override fun newImageLoader(): ImageLoader = ImageLoader(this)
}

fun Context.appModule(): AppModule = (applicationContext as ModuleOwner).appModule