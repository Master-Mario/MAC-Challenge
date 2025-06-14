package at.mario.challenge.utils

import at.mario.challenge.Main
import at.mario.challenge.utils.ServerController.serverDirectories
import com.google.common.io.ByteStreams
import de.miraculixx.kpaper.runnables.task
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * Handles connecting players to Minecraft servers, checking server status, and managing player-server assignments.
 */
object ServerConnector {
    /** Maps server ports to sets of player UUIDs currently assigned to them. */
    private val playersPerPort: MutableMap<Int, MutableSet<UUID>> = mutableMapOf()

    /**
     * Connects a player to a server by name. If the server is not online, retries after a delay.
     * @param player The player to connect
     * @param serverName The name of the server
     */
    fun connect(player: Player, serverName: String) {
        val port = ServerController.serverNameToPort[serverName] ?: return
        val isOnline = isServerOnline(port)
        if (isOnline) {
            val out = ByteStreams.newDataOutput().apply {
                writeUTF("Connect")
                writeUTF(serverName)
            }
            player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray())
            playersPerPort[port]?.remove(player.uniqueId)
        } else {
            task(delay = 5) {
                connect(player, serverName)
            }
        }
    }

    /**
     * Checks if a server is online, using either VPS or local config depending on environment.
     * @param port The server port
     * @return True if the server is online, false otherwise
     */
    fun isServerOnline(port: Int): Boolean {
        if (Main.SERVER_RUNS_ON_VPS){
            val status = PterodactylClient().getServerStatus(ServerController.serverUUIDs[port] ?: "not-used")
            return status == "running" || status == "starting"
        }
        if (serverDirectories[port]==null) {
            return false
        }
        return Config().config.getBoolean("serverstatus_${serverDirectories[port]?.absolutePath?.removePrefix(ServerController.BASE_PATH + "\\")}")
    }
}
