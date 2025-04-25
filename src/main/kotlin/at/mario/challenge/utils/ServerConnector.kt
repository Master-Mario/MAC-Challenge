package at.mario.challenge.utils

import at.mario.challenge.Main
import at.mario.challenge.utils.ServerController.serverDirectories
import com.google.common.io.ByteStreams
import de.miraculixx.kpaper.runnables.task
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object ServerConnector {
    private val playersPerPort: MutableMap<Int, MutableSet<UUID>> = mutableMapOf()

    fun connect(player: Player, serverName: String) {
        // Serververfügbarkeit prüfen
        val isOnline = isServerOnline(ServerController.serverNameToPort[serverName] ?: return)

        // Wenn der Server online ist, sende das Connect-Paket
        if (isOnline) {
            val out = ByteStreams.newDataOutput().apply {
                writeUTF("Connect")
                writeUTF(serverName)
            }

            Bukkit.getOnlinePlayers()
                .forEach {
                    it.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray())
                    playersPerPort[ServerController.serverNameToPort[serverName]]!! -= it.uniqueId
                }
        } else {
            task(delay = 5) {
                connect(player, serverName)
            }
        }
    }

    fun isServerOnline(port: Int): Boolean {
        if (Main.SERVER_RUNS_ON_VPS){
            return PterodactylClient().getServerStatus(ServerController.serverUUIDs[port] ?: "not-used") =="running" ||
                    PterodactylClient().getServerStatus(ServerController.serverUUIDs[port] ?: "not-used") == "starting"
        }
        if (serverDirectories[port]==null) {
            serverDirectories[port] = serverDirectories[port] ?: return false
        }
        return Config().config.getBoolean("serverstatus_${serverDirectories[port]?.absolutePath?.removePrefix(ServerController.BASE_PATH + "\\")}")
    }
}

