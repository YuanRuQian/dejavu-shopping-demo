package lydia.yuan.dajavu.network

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class EggGroupResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: Long,
    val name: String,
    val names: List<Name>,
    @JsonNames("pokemon_species")
    val pokemonSpecies: List<PokemonSpecy>,
)

@Serializable
data class Name(
    val language: Language,
    val name: String,
)

@Serializable
data class Language(
    val name: String,
    val url: String,
)

@Serializable
data class PokemonSpecy(
    val name: String,
    val url: String,
)