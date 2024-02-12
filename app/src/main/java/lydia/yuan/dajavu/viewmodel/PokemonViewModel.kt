package lydia.yuan.dajavu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import lydia.yuan.dajavu.MyApplication
import lydia.yuan.dajavu.network.EggGroupResponse
import lydia.yuan.dajavu.network.PokemonRepository
import lydia.yuan.dajavu.network.PokemonSpecy

class PokemonViewModel(
    val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _pokemon = MutableStateFlow<List<PokemonSpecy>>(emptyList())
    val pokemon get() = _pokemon

    fun getEggGroup(name: String) {
        viewModelScope.launch {
            val pokemonSpecies: EggGroupResponse
            val pokemon: List<PokemonSpecy>
            try {
                Log.d("PokemonViewModel", "getEggGroup: $name")
                pokemonSpecies = pokemonRepository.getEggGroup(name)
                if (pokemonSpecies.pokemonSpecies.isEmpty()) {
                    Log.d("PokemonViewModel", "getEggGroup: empty")
                } else {
                    _pokemon.value = pokemonSpecies.pokemonSpecies
                    Log.d("PokemonViewModel", "getEggGroup: ${pokemonSpecies.pokemonSpecies}")
                }
            } catch (e: Exception) {
                _pokemon.value = emptyList()
                Log.e("PokemonViewModel", "getEggGroup: ${e.message}")
            }

            Log.d("PokemonViewModel", "getEggGroup: ${_pokemon.value}")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                PokemonViewModel(pokemonRepository = application.container.pokemonRepository)
            }
        }
    }
}