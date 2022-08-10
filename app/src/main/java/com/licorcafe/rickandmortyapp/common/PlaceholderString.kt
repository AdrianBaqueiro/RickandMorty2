package com.licorcafe.rickandmortyapp.common

import android.content.res.Resources
import androidx.annotation.StringRes

data class PlaceholderString(@StringRes val stringId: Int, val replacement: String) {
    fun string(res: Resources): String = res.getString(stringId, replacement)
}