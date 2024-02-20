package lydia.yuan.dajavu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
        viewModel.getAbility(limit = 10, offset = 0)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(text = "Swipe to Load More Pokemon Ability", style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.height(32.dp))

        SwipeRefresh(
            state = isRefreshing,
            onRefresh = {
                viewModel.getAbility(limit = 10, offset = ability.size)
            },
            refreshTriggerDistance = 46.dp,
            content = {
                // LazyVerticalStaggeredGrid with items and load more logic
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

                            // Trigger loading more items when reaching the end of the list
                            if (index == ability.size - 1) {
                                viewModel.getAbility(limit = 10, offset = ability.size)
                            }
                        }
                    }
                )
            }
        )
    }
}
