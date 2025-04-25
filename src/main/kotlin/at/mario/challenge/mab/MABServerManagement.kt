package at.mario.challenge.mab

import at.mario.challenge.Main
import at.mario.challenge.utils.PterodactylClient
import at.mario.challenge.utils.ServerConnector
import at.mario.challenge.utils.ServerController
import at.mastermario.maclobby.mab.Team
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class MABServerManagement(
    private val pterodactylClient: PterodactylClient = PterodactylClient(),
) {
    private val teamList: MutableList<Team> = mutableListOf()

    // Methode, um Server für ein Team zu erstellen
    fun createTeamServers(teamName: String) {
        // Erstelle den Farming-Server
        val farmingServerResponse = pterodactylClient.createServer(teamName, "farming")
        val farmingServerId = parseServerId(farmingServerResponse)

        // Erstelle den Battle-Server
        val battleServerResponse = pterodactylClient.createServer(teamName, "battle")
        val battleServerId = parseServerId(battleServerResponse)

        // Speichern des Teams und der Server-IDs
        val team = Team(teamName, farmingServerId, battleServerId)
        teamList.add(team)

        println("Team $teamName Server erstellt: Farming-Server ID = $farmingServerId, Battle-Server ID = $battleServerId")
    }

    // Methode, um Server für ein Team zu starten
    fun startTeamServers(teamName: String) {
        val team = teamList.find { it.name == teamName }
        team?.let {
            pterodactylClient.startServer(it.farmingServerId!!)
            pterodactylClient.startServer(it.battleServerId!!)
            println("Starten von Servern für Team $teamName...")
        }
    }

    // Methode, um Server für ein Team zu stoppen
    fun stopTeamServers(teamName: String) {
        val team = teamList.find { it.name == teamName }
        team?.let {
            pterodactylClient.stopServer(it.farmingServerId!!)
            pterodactylClient.stopServer(it.battleServerId!!)
            println("Stoppen von Servern für Team $teamName...")
        }
    }

    // Methode, um die Server-IDs aus der API-Antwort zu extrahieren
    private fun parseServerId(response: String): String {
        val parser = JSONParser()
        val jsonObject = parser.parse(response) as JSONObject
        return (jsonObject["attributes"] as JSONObject)["id"] as String
    }

    // Methode, um den Serverstatus zu überwachen (z. B. nach dem Start)
    private fun monitorServerStatus(teamName: String) {
        // Du kannst hier eine Schleife verwenden, um regelmäßig den Status der Server zu prüfen
        val server1Status = pterodactylClient.getServerStatus("${teamName}_1")
        val server2Status = pterodactylClient.getServerStatus("${teamName}_2")

        println("Status für ${teamName}_1: $server1Status")
        println("Status für ${teamName}_2: $server2Status")
    }

    // Methode, um Spieler zu den Team-Servern zu teleportieren
    private fun teleportPlayersToServer(teamName: String) {
        // Hole alle Spieler, die zum Team gehören
        val team1Players = getPlayersInTeam(1)
        val team2Players = getPlayersInTeam(2)

        // Teleportiere Team 1 Spieler zum Server 1
        for (player in team1Players) {
            ServerConnector.connect(player, "${teamName}_1")
        }

        // Teleportiere Team 2 Spieler zum Server 2
        for (player in team2Players) {
            ServerConnector.connect(player, "${teamName}_2")
        }
    }

    // Beispielmethode, um Spieler basierend auf Teamnamen zu bekommen
    private fun getPlayersInTeam(teamNumber: Int): List<Player> {
        // Ersetze dies durch deine Logik, um die Spieler für ein bestimmtes Team zu bekommen
        if (teamNumber == 1) {
            return Main.mabTeam1
        }else if (teamNumber == 2) {
            return Main.mabTeam2
        }else{
            return emptyList()
        }
    }

    fun onEventStart(){
        createTeamServers("Team1")
        createTeamServers("Team2")
    }
}
