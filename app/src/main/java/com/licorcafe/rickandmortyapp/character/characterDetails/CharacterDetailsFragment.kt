package com.licorcafe.rickandmortyapp.character.characterDetails

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.licorcafe.rickandmortyapp.AppModule
import com.licorcafe.rickandmortyapp.R
import com.licorcafe.rickandmortyapp.appModule
import com.licorcafe.rickandmortyapp.character.domain.CharacterId
import com.licorcafe.rickandmortyapp.common.presentation.ViewModelAlgebra
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class CharacterDetailsFragment : Fragment(R.layout.fragment_compose) {

    private val characterId: Long by lazy(LazyThreadSafetyMode.NONE) {
        val id = requireArguments().getLong(EXTRA_CHARACTER_ID, -1)
        check(id != -1L) { "Please use newBundle() for creating the arguments" }
        id
    }

    private val viewModel: CharacterDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view as ComposeView
        composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)

        val module = object : CharacterDetailsModule,
            AppModule by requireActivity().appModule(),
            ViewModelAlgebra<CharactersDetailsViewState, CharacterDetailsEffect> by viewModel {}

        val actions = Channel<CharacterDetailsAction>(Channel.UNLIMITED)

        with(module) {
            composeView.setContent {
                CharactersDetailsScreen(
                    stateFlow = viewState,
                    initialState = Loading,
                    characterId = characterId,
                    actions = actions
                )
            }

            lifecycleScope.launchWhenStarted {
                program(characterId, actions.receiveAsFlow())
            }
        }

        handleEffects()
    }

    private fun handleEffects() {
        lifecycleScope.launchWhenStarted {
            viewModel.effects.map { effect ->
                when (effect) {
                    NavigateUp -> findNavController().navigateUp()
                }
            }.collect()
        }
    }

    companion object {
        private const val EXTRA_CHARACTER_ID = "EXTRA_CHARACTER_ID"


    fun newBundle(characterId: CharacterId): Bundle =
        Bundle().apply {
            putLong(EXTRA_CHARACTER_ID, characterId)
        }
    }
}

