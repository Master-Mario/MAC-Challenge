package at.mario.challenge.mab

import at.mario.challenge.Main
import at.mario.challenge.utils.PterodactylClient
import at.mario.challenge.utils.ServerConnector
import org.bukkit.entity.Player
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

/**
 * Manages the creation, starting, stopping, and player teleportation for team-specific Minecraft servers (farming and battle servers).
 * Integrates with Pterodactyl for server management and BungeeCord for player transfer.
 */
class MABServerManagement(
    private val pterodactylClient: PterodactylClient = PterodactylClient(),
) {
    /** List of all teams and their associated server IDs. */
    private val teamList: MutableList<Team> = mutableListOf()

    /**
     * Creates farming and battle servers for a team and stores their IDs.
     * @param teamName The name of the team
     */
    fun createTeamServers(teamName: String) {
        val farmingServerResponse = pterodactylClient.createServer(teamName, "farming")
        val farmingServerId = parseServerId(farmingServerResponse)
        val battleServerResponse = pterodactylClient.createServer(teamName, "battle")
        val battleServerId = parseServerId(battleServerResponse)
        val team = Team(teamName, farmingServerId, battleServerId)
        teamList.add(team)
        println("Team $teamName servers created: Farming-Server ID = $farmingServerId, Battle-Server ID = $battleServerId")
    }

    /**
     * Starts the farming and battle servers for a team.
     * @param teamName The name of the team
     */
    fun startTeamServers(teamName: String) {
        val team = teamList.find { it.name == teamName }
        team?.let {
            pterodactylClient.startServer(it.farmingServerId!!)
            pterodactylClient.startServer(it.battleServerId!!)
            println("Starting servers for team $teamName...")
        }
    }

    /**
     * Stops the farming and battle servers for a team.
     * @param teamName The name of the team
     */
    fun stopTeamServers(teamName: String) {
        val team = teamList.find { it.name == teamName }
        team?.let {
            pterodactylClient.stopServer(it.farmingServerId!!)
            pterodactylClient.stopServer(it.battleServerId!!)
            println("Stopping servers for team $teamName...")
        }
    }

    /**
     * Parses the server ID from a Pterodactyl API response.
     * @param response The API response as a JSON string
     * @return The server ID as a string
     */
    private fun parseServerId(response: String): String {
        val parser = JSONParser()
        val jsonObject = parser.parse(response) as JSONObject
        return (jsonObject["attributes"] as JSONObject)["id"] as String
    }

    /**
     * Monitors the status of a team's servers and prints their current state.
     * @param teamName The name of the team
     */
    private fun monitorServerStatus(teamName: String) {
        val server1Status = pterodactylClient.getServerStatus("${teamName}_1")
        val server2Status = pterodactylClient.getServerStatus("${teamName}_2")
        println("Status for ${teamName}_1: $server1Status")
        println("Status for ${teamName}_2: $server2Status")
    }

    /**
     * Teleports players of each team to their respective servers using BungeeCord.
     * @param teamName The name of the team
     */
    private fun teleportPlayersToServer(teamName: String) {
        val team1Players = getPlayersInTeam(1)
        val team2Players = getPlayersInTeam(2)
        for (player in team1Players) {
            ServerConnector.connect(player, "${teamName}_1")
        }
        for (player in team2Players) {
            ServerConnector.connect(player, "${teamName}_2")
        }
    }

    /**
     * Gets the list of players for a given team number.
     * @param teamNumber The team number (1 or 2)
     * @return List of Player objects in the team
     */
    private fun getPlayersInTeam(teamNumber: Int): List<Player> {
        return when (teamNumber) {
            1 -> Main.mabTeam1
            2 -> Main.mabTeam2
            else -> emptyList()
        }
    }

    /**
     * Entry point for starting the event: creates servers for both teams.
     */
    fun onEventStart(){
        createTeamServers("Team1")
        createTeamServers("Team2")
    }
}
