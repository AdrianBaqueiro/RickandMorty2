package com.licorcafe.rickandmortyapp.character.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.licorcafe.rickandmortyapp.common.ErrorTextRes
import com.licorcafe.rickandmortyapp.common.IdTextRes
import com.licorcafe.rickandmortyapp.common.TextRes

@Composable
fun CharacterLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun CharacterProblem(
    textRes: TextRes,
    onClick: (() -> Unit) = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("CharacterProblem"), contentAlignment = Alignment.Center
    ) {
        when (textRes) {
            is ErrorTextRes -> Text(
                text = stringResource(id = textRes.id, stringResource(id = textRes.retryTextId)),
                modifier = Modifier.clickable(onClick = onClick),
                textAlign = TextAlign.Center
            )
            is IdTextRes -> Text(
                text = stringResource(id = textRes.id),
                textAlign = TextAlign.Center
            )
        }
    }
}
