package at.mario.challenge.mab

/**
 * Data class representing a team and its associated farming and battle server IDs.
 * @property name The name of the team
 * @property farmingServerId The ID of the farming server for the team
 * @property battleServerId The ID of the battle server for the team
 */
data class Team(
    val name: String,
    val farmingServerId: String? = null,
    val battleServerId: String? = null
)
