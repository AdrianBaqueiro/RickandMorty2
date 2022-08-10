package com.licorcafe.rickandmortyapp.character.characterList

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
import com.licorcafe.rickandmortyapp.character.characterDetails.CharacterDetailsFragment
import com.licorcafe.rickandmortyapp.common.presentation.ViewModelAlgebra
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class CharactersFragment : Fragment(R.layout.fragment_compose) {

    private val viewModel: CharacterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view as ComposeView
        composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)

        val module: CharactersModule = object : CharactersModule,
            AppModule by requireActivity().appModule(),
            ViewModelAlgebra<CharactersViewState, CharactersEffect> by viewModel {}

        val actions = Channel<CharacterAction>(Channel.UNLIMITED)

        with(module) {
            val viewState = viewState.onEach { afterBind(it) }
            composeView.setContent {
                CharactersScreen(
                    stateFlow = viewState,
                    initialValue = Loading,
                    actions = actions
                )
            }

            lifecycleScope.launchWhenStarted {
                program(actions.receiveAsFlow())
            }
        }

        handleEffects()
    }

    private fun handleEffects() {
        lifecycleScope.launchWhenStarted {
            viewModel.effects.map { effect ->
                when (effect) {
                    is NavigateToDetails -> findNavController().navigate(
                        R.id.action_details,
                        CharacterDetailsFragment.newBundle(effect.characterId)
                    )
                }
            }.collect()
        }
    }
}