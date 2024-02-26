package lydia.yuan.dajavu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lydia.yuan.dajavu.viewmodel.CitiesViewModel

@Composable
fun CitiesScreen(viewModel: CitiesViewModel = viewModel(factory = CitiesViewModel.Factory)) {
    val citiesData = viewModel.cities.collectAsState()
    val cities = citiesData.value

    LaunchedEffect(key1 = true) {
        viewModel.loadCities()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cities in Utah", style = MaterialTheme.typography.titleLarge)

        Divider()

        LazyColumn {
            items(cities.size) { index ->
                ElevatedCard(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = cities[index].name, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
