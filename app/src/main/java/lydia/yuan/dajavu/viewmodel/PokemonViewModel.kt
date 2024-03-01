package lydia.yuan.dajavu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import lydia.yuan.dajavu.MyApplication
import lydia.yuan.dajavu.network.Ability
import lydia.yuan.dajavu.network.AbilityResponse
import lydia.yuan.dajavu.network.EggGroupResponse
import lydia.yuan.dajavu.network.PokemonRepository
import lydia.yuan.dajavu.network.PokemonSpecy
import java.time.Duration

class PokemonViewModel(
    val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _pokemon = MutableStateFlow<List<PokemonSpecy>>(emptyList())
    val pokemon get() = _pokemon

    private val _ability = MutableStateFlow<List<Ability>>(emptyList())
    val ability get() = _ability

    private val _isAbilityRefreshing = MutableStateFlow(SwipeRefreshState(false))
    val isAbilityRefreshing get() = _isAbilityRefreshing
    private var hasLogOutJobRunning = false

    private var _userLikeThis = MutableStateFlow(false)
    val userLikeThis get() = _userLikeThis

    private val USER_LIKE_THIS = "User Likes This Stuff"
    private val USER_FEELS_NOTHING_ABOUT_THIS = "User Feels Nothing About This Stuff"

    private var _userLikeThisText = MutableStateFlow(USER_FEELS_NOTHING_ABOUT_THIS)
    val userLikeThisText get() = _userLikeThisText

    private suspend fun toggleLikeButtonStatus() {
        _userLikeThis.value = !_userLikeThis.value
        delay(100)
        _userLikeThisText.value =
            if (_userLikeThis.value) USER_LIKE_THIS else USER_FEELS_NOTHING_ABOUT_THIS
    }

    fun throttledToggleLikeButtonStatus(delayDuration: Long) {
        viewModelScope.launch {
            if (hasLogOutJobRunning) return@launch
            hasLogOutJobRunning = true
            toggleLikeButtonStatus()
            delay(delayDuration)
            hasLogOutJobRunning = false
        }
    }

    fun getAbility(limit: Int, offset: Int) {
        if (isAbilityRefreshing.value.isRefreshing) return
        viewModelScope.launch {
            _isAbilityRefreshing.value = SwipeRefreshState(true)
            val abilityResponse: AbilityResponse
            delay(2000)
            try {
                abilityResponse = pokemonRepository.getAbility(limit, offset)
                if (abilityResponse.results.isEmpty()) {
                    Log.d("PokemonViewModel", "getAbility: empty")
                } else {
                    _ability.value = _ability.value + abilityResponse.results
                    Log.d("PokemonViewModel", "getAbility: ${abilityResponse.results}")
                }
            } catch (e: Exception) {
                _ability.value = emptyList()
                Log.e("PokemonViewModel", "getAbility: ${e.message}")
            } finally {
                _isAbilityRefreshing.value = SwipeRefreshState(false)
            }
        }
    }

    fun getEggGroup(name: String) {
        viewModelScope.launch {
            val pokemonSpecies: EggGroupResponse
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