package at.mario.challenge.utils

import at.mario.challenge.Main
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.runnables.KPaperRunnable
import de.miraculixx.kpaper.runnables.task
import org.bukkit.Bukkit
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

/**
 * Provides server management utilities for local and VPS servers, including directory resolution and status tracking.
 */
object ServerController {

    /** Maps server ports to their directories. */
    internal val serverDirectories = ConcurrentHashMap<Int, File>()
    /** Maps server ports to their running processes. */
    private val serverProcesses = ConcurrentHashMap<Int, Process?>()
    /** Maps server ports to their online status. */
    private val serverStatus = ConcurrentHashMap<Int, Boolean>()
    /** Maps server ports to their start tasks. */
    private val serverStartTasks = ConcurrentHashMap<Int, KPaperRunnable?>()
    /** Base path for local server directories. */
    const val BASE_PATH = "C:\\Spiele\\MAC-Netzwerk"

    /**
     * Maps server ports to their VPS UUIDs.
     * 25567: ChallengeServer, 25568: Testserver, 25569: MAC-SMP, 25570: Bedwars
     */
     val serverUUIDs = mapOf(
        25567 to "51ccccad", // ChallengeServer
        25568 to "ede53234", // Testserver optional
        25569 to "d0a1ca96", // MAC-SMP
        25570 to "not-used",  // Bedwars optional
    )
    /** Maps server names to their ports. */
    val serverNameToPort = mapOf(
        "ChallengeServer" to 25567,
        "ChallengeTestserver" to 25568,
        "MAC-SMP" to 25569,
        "Bedwars" to 25570
    )
    /** Maps ports to server names. */
    val portToServerName = serverNameToPort.entries.associate { it.value to it.key }

    /**
     * Resolves the local directory for a given port (used for local servers only).
     * @param port The server port
     * @return The File object for the directory, or null if not found
     */
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

    /**
     * Checks if a server is marked as online.
     * @param port The server port
     * @return True if online, false otherwise
     */
    fun isOnline(port: Int): Boolean {
        return serverStatus[port] == true
    }

    /**
     * Checks if a server process is running.
     * @param port The server port
     * @return True if running, false otherwise
     */
    fun isRunning(port: Int): Boolean {
        return serverProcesses[port]?.isAlive == true
    }
}
