package at.mastermario.maclobby.mab

data class Team(
    val name: String,
    val farmingServerId: String? = null,
    val battleServerId: String? = null
)
