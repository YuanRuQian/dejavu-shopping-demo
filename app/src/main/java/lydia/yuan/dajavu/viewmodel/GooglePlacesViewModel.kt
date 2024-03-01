package lydia.yuan.dajavu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import lydia.yuan.dajavu.MyApplication
import lydia.yuan.dajavu.network.GooglePlacesRepository
import lydia.yuan.dajavu.network.Place
import lydia.yuan.dajavu.network.SearchPlaceRequest

class GooglePlacesViewModel (
    val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {

    private var _places = MutableStateFlow<List<Place>>(emptyList())
    val places get() = _places

    fun searchPlace(text: String) {
        viewModelScope.launch {
            val searchPlaceRequest = SearchPlaceRequest(
                textQuery = text
            )
            val searchPlaceResponse = googlePlacesRepository.searchPlace(searchPlaceRequest)
            _places.value = searchPlaceResponse.places
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                GooglePlacesViewModel(
                    googlePlacesRepository = application.container.googlePlaceRepository
                )
            }
        }
    }
}