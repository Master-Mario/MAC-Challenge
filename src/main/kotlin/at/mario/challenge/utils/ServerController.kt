package at.mario.challenge.utils

import at.mario.challenge.Main
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.runnables.KPaperRunnable
import de.miraculixx.kpaper.runnables.task
import org.bukkit.Bukkit
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

object ServerController {

    internal val serverDirectories = ConcurrentHashMap<Int, File>()
    private val serverProcesses = ConcurrentHashMap<Int, Process?>()
    private val serverStatus = ConcurrentHashMap<Int, Boolean>()
    private val serverStartTasks = ConcurrentHashMap<Int, KPaperRunnable?>()
    const val BASE_PATH = "C:\\Spiele\\MAC-Netzwerk"

    // UUID Mapping für VPS-Server
     val serverUUIDs = mapOf(
        25567 to "51ccccad", // ChallengeServer
        25568 to "ede53234", // Testserver optional
        25569 to "d0a1ca96", // MAC-SMP
        25570 to "not-used",  // Bedwars optional
    )
    val serverNameToPort = mapOf(
        "ChallengeServer" to 25567,
        "ChallengeTestserver" to 25568,
        "MAC-SMP" to 25569,
        "Bedwars" to 25570
    )
    val portToServerName = serverNameToPort.entries.associate { it.value to it.key }

    // Funktion für lokalen Pfad (nur bei local wichtig)
    private fun resolveDirectory(port: Int): File? {
        val baseDir = Bukkit.getWorldContainer().parentFile?.absoluteFile ?: return null
        return when (port) {
            25567 -> File(baseDir, "ChallengeServer")
            25568 -> File(baseDir, "ChallengeTestserver")
            25569 -> File(baseDir, "MAC-SMP")
            25570 -> File(baseDir, "Bedwars")
            else -> null
        }
    }

    fun isOnline(port: Int): Boolean {
        return serverStatus[port] == true
    }

    fun isRunning(port: Int): Boolean {
        return serverProcesses[port]?.isAlive == true
    }
}
