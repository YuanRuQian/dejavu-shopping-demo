package lydia.yuan.dajavu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import lydia.yuan.dajavu.viewmodel.PokemonViewModel

@Composable
fun PaginationScreen(viewModel: PokemonViewModel = viewModel(factory = PokemonViewModel.Factory)) {
    val abilityData = viewModel.ability.collectAsState()
    val ability = abilityData.value

    val isRefreshingData = viewModel.isAbilityRefreshing.collectAsState()
    val isRefreshing = isRefreshingData.value

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(key1 = true) {
        viewModel.getAbility(limit = 25, offset = 0)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Swipe to Load More Pokemon Ability", style = MaterialTheme.typography.titleLarge)

        Divider()

        SwipeRefresh(
            state = isRefreshing,
            indicatorAlignment = Alignment.BottomCenter,
            indicatorPadding = PaddingValues(bottom = 32.dp),
            onRefresh = {
            },
            refreshTriggerDistance = 46.dp,
            content = {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(200.dp),
                    state = lazyGridState,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(count = ability.size) { index ->
                            Text(
                                text = ability[index].name,
                                modifier = Modifier.padding(8.dp)
                            )

                            if (index == ability.size - 1) {
                                viewModel.getAbility(limit = 25, offset = ability.size)
                            }
                        }
                    }
                )
            }
        )
    }
}
