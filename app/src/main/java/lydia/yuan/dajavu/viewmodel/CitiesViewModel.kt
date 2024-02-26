package lydia.yuan.dajavu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import lydia.yuan.dajavu.MyApplication
import lydia.yuan.dajavu.network.CitiesRepository
import lydia.yuan.dajavu.network.City

class CitiesViewModel(
    val citiesRepository: CitiesRepository
) : ViewModel() {
    private val _cities = MutableStateFlow<List<City>>(emptyList())

    val cities get() = _cities

    fun loadCities() {
        viewModelScope.launch {
            val citiesResponse = citiesRepository.getCities()
            _cities.value = citiesResponse.data.values.toList()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                CitiesViewModel(citiesRepository = application.container.citiesRepository)
            }
        }
    }
}
