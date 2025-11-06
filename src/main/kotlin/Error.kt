interface Error {
    val message: String
}

data class Whoopsie(override val message: String) : Error
data class Figures(override val message: String) : Error
data class OhComeOn(override val message: String) : Error

