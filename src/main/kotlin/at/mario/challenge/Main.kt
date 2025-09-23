package at.mario.challenge

import at.mario.challenge.challenges.*
import at.mario.challenge.commands.*
import at.mario.challenge.events.*
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.ConfigManager
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import de.miraculixx.kpaper.main.KPaper
import de.miraculixx.kpaper.runnables.KPaperRunnable
import dev.jorel.commandapi.CommandAPI
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.boss.BossBar
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.spigotmc.SpigotConfig.config
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.mutableMapOf
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

/**
 * Main plugin class extending KPaper. Handles loading, startup, and shutdown logic for the MAC Challenge plugin.
 */
class Main : KPaper() {
    companion object{
        /**
         * The jump height for the challenge.
         */
        var jumpHeight = 0
        /**
         * Singleton instance of the Main class.
         */
        lateinit var instance : Main
        /**
         * Prefix used for plugin messages.
         */
        val prefix = cmp("MAC", KColors.ORANGERED) + cmp(" | ", KColors.GRAY)
        /**
         * Map of team numbers to player name lists, loaded from config.
         */
        var teams = mutableMapOf<Int, List<String>?>(
            1 to (Config().config.getList("team1") as? List<String>),
            2 to (Config().config.getList("team2") as? List<String>)
        )
        /**
         * Collection of online player names.
         */
        var onlinePlayersCollection: Collection<String> = listOf()
            get(){
                field = listOf()
                for (player in Bukkit.getOnlinePlayers()) {
                    field += player.name
                }
                return field
            }
        /**
         * Runnable for starting tasks.
         */
        var taskStarting: KPaperRunnable? = null
        /**
         * File for storing kill data.
         */
        var killFile: File? = null
        /**
         * FileConfiguration for kill data.
         */
        var killData: FileConfiguration? = null
        /**
         * Map of player UUIDs to their kill counts by entity type.
         */
        val killCounts: MutableMap<UUID, MutableMap<EntityType, Int>> = mutableMapOf()
        /**
         * List of players in MAB Team 1.
         */
        var mabTeam1: MutableList<Player> = mutableListOf()
        /**
         * List of players in MAB Team 2.
         */
        var mabTeam2: MutableList<Player> = mutableListOf()
        /**
         * Team name from config.
         */
        val team = config.getString("team")
        /**
         * Mode name from config.
         */
        val mode = config.getString("mode")
        /**
         * Indicates if the server runs on a VPS.
         */
        const val SERVER_RUNS_ON_VPS = true
        /**
         * Map of players to their BossBars.
         */
        val bossBars = mutableMapOf<Player, BossBar>()
    }
    /**
     * Reference to the ResetCommand instance.
     */
    private var resetCommand: ResetCommand? = null
    /**
     * Indicates if a reset is in progress.
     */
    private var reset = false

    /**
     * Called when the plugin is loaded. Initializes config, commands, and teams. Handles world reset if needed.
     */
    override fun load() {
        instance = this
        val config = Config()
        
        // Migrate old German configuration keys to English
        config.migrateConfigKeys()
        
        // Initialize player run-blocks-amount in config if not present
        for (player in Bukkit.getOnlinePlayers()) {
            val key = "${Config.Companion.Keys.RUN_RANDOMIZER_RUN_BLOCKS_AMOUNT}.${player.name}"
            if (!config.config.contains(key)){
                config.add(key, 0.0)
            }
        }
        // Register commands
        TryCommand()
        TimerCommand()
        ChallengeCommand()
        MABCommand()
        RandomizerCommand()
        ReopenGUI()
        UtilsCommand()
        ChallengeManager()
        resetCommand = ResetCommand()

        // Assign players to teams based on config
        mabTeam1.clear()
        mabTeam2.clear()
        for (player in Bukkit.getOnlinePlayers()) {
            val teamKey = "${player.name}.team"
            if (config.config.contains(teamKey)){
                when (config.config.getString(teamKey)) {
                    "Team1" -> mabTeam1 += player
                    "Team2" -> mabTeam2 += player
                }
            }
        }

        // Initialize config values if not present with proper defaults
        var needsSave = false
        
        // Core settings with English defaults
        if (!config.config.contains(Config.Companion.Keys.CAN_TRY)) {
            config.setBoolean(Config.Companion.Keys.CAN_TRY, false)
            needsSave = true
        }
        if (!config.config.contains(Config.Companion.Keys.IS_RESET)) {
            config.setBoolean(Config.Companion.Keys.IS_RESET, false)
            needsSave = true
        }
        if (!config.config.contains(Config.Companion.Keys.LANGUAGE)) {
            config.setString(Config.Companion.Keys.LANGUAGE, "en")
            needsSave = true
        }
        
        // World settings section
        if (!config.config.contains(Config.Companion.Keys.SETTINGS_VIEW_DISTANCE)) {
            config.setInt(Config.Companion.Keys.SETTINGS_VIEW_DISTANCE, 10)
            needsSave = true
        }
        if (!config.config.contains(Config.Companion.Keys.SETTINGS_SIMULATION_DISTANCE)) {
            config.setInt(Config.Companion.Keys.SETTINGS_SIMULATION_DISTANCE, 10)
            needsSave = true
        }
        if (!config.config.contains(Config.Companion.Keys.SETTINGS_PVP)) {
            config.setBoolean(Config.Companion.Keys.SETTINGS_PVP, true)
            needsSave = true
        }
        if (!config.config.contains(Config.Companion.Keys.SETTINGS_FREEZE_ON_PAUSE)) {
            config.setBoolean(Config.Companion.Keys.SETTINGS_FREEZE_ON_PAUSE, false)
            needsSave = true
        }
        
        // Run randomizer settings with English keys
        if (!config.config.contains(Config.Companion.Keys.RUN_RANDOMIZER_DISTANCE_GOAL)) {
            config.setInt(Config.Companion.Keys.RUN_RANDOMIZER_DISTANCE_GOAL, 500)
            needsSave = true
        }
        
        // Timer settings
        if (!config.config.contains(Config.Companion.Keys.TIMER)) {
            config.setInt(Config.Companion.Keys.TIMER, 0)
            needsSave = true
        }
        
        if (needsSave) {
            config.save()
        }

        // Handle world reset if isReset is true
        if (config.config.getBoolean(Config.Companion.Keys.IS_RESET)) {
            reset = true
            try {
                resetWorldDirectories()
            } catch (e: IOException) {
                server.logger.severe("Failed to reset world directories: ${e.message}")
                e.printStackTrace()
            }

            config.setBoolean(Config.Companion.Keys.IS_RESET, false)
            config.save()
        }
    }

    /**
     * Helper method to reset world directories safely
     */
    private fun resetWorldDirectories() {
        val worldContainer = Bukkit.getWorldContainer()
        val worldNames = listOf("world", "world_nether", "world_the_end")
        val subDirectories = listOf("data", "datapacks", "playerdata", "poi", "region")

        for (worldName in worldNames) {
            val worldDir = File(worldContainer, worldName)
            if (worldDir.exists()) {
                FileUtils.deleteDirectory(worldDir)
            }
            worldDir.mkdir()

            // Create subdirectories
            for (subDir in subDirectories) {
                File(worldDir, subDir).mkdir()
            }
        }
    }

    /**
     * Called when the plugin is enabled. Initializes events, loads settings, and activates challenges.
     */
    override fun startup() {
        val config = Config()
        
        // Register events
        Timer
        PlayerMoveEvent
        DeathEvent
        JoinQuitEvent
        BlockDropEvent
        GoalEvent
        DamageEvent
        PunchEvent
        ChestGenerationEvent
        InventoryClickEvent
        MABClickEvent
        CraftEvent
        RegenerationEvent
        BlockPlaceEvent
        CommandAPI.onEnable()
        server.consoleSender.sendMessage(cmp("\n"+
                "_________ .__           .__  .__                   ____       __________.____            ____ .__        \n" +
                "\\_   ___ \\|  |__ _____  |  | |  |   ____   ____   / ___\\  ____\\______   |    |    __ __ / ___\\|__| ____  \n" +
                "/    \\  \\/|  |  \\\\__  \\ |  | |  | _/ __ \\ /    \\ / /_/  _/ __ \\|     ___|    |   |  |  / /_/  |  |/    \\ \n" +
                "\\     \\___|   Y  \\/ __ \\|  |_|  |_\\  ___/|   |  \\\\___  /\\  ___/|    |   |    |___|  |  \\___  /|  |   |  \\\n" +
                " \\______  |___|  (____  |____|____/\\___  |___|  /_____/  \\___  |____|   |_______ |____/_____/ |__|___|  /\n" +
                "        \\/     \\/     \\/               \\/     \\/             \\/                 \\/                    \\/ ")
        )
        
        // Set world settings from config or use defaults
        var needsSave = false
        
        val viewDistance = if (config.config.contains(Config.Companion.Keys.SETTINGS_VIEW_DISTANCE)) {
            config.config.getInt(Config.Companion.Keys.SETTINGS_VIEW_DISTANCE)
        } else {
            config.setInt(Config.Companion.Keys.SETTINGS_VIEW_DISTANCE, 10)
            needsSave = true
            10
        }
        
        val simulationDistance = if (config.config.contains(Config.Companion.Keys.SETTINGS_SIMULATION_DISTANCE)) {
            config.config.getInt(Config.Companion.Keys.SETTINGS_SIMULATION_DISTANCE)
        } else {
            config.setInt(Config.Companion.Keys.SETTINGS_SIMULATION_DISTANCE, 10)
            needsSave = true
            10
        }
        
        val pvpEnabled = if (config.config.contains(Config.Companion.Keys.SETTINGS_PVP)) {
            config.config.getBoolean(Config.Companion.Keys.SETTINGS_PVP)
        } else {
            config.setBoolean(Config.Companion.Keys.SETTINGS_PVP, true)
            needsSave = true
            true
        }
        
        // Apply settings to all worlds
        for (world in Bukkit.getWorlds()) {
            world.viewDistance = viewDistance
            world.simulationDistance = simulationDistance
            world.pvp = pvpEnabled
        }
        // Initialize and activate challenges, goals, battles, and randomizer settings
        for (challenges in Challenges.values()){
            if (!config.config.contains(challenges.nameString)){
                config.setBoolean(challenges.nameString, false)
                needsSave = true
            } else {
                challenges.active = config.config.getBoolean(challenges.nameString)
            }
        }
        for (goals in Goals.values()){
            if (!config.config.contains(goals.nameString)){
                config.setBoolean(goals.nameString, false)
                needsSave = true
            } else {
                goals.active = config.config.getBoolean(goals.nameString)
            }
        }
        for (battles in Battles.values()){
            if (!config.config.contains(battles.nameString)){
                config.setBoolean(battles.nameString, false)
                needsSave = true
            } else {
                battles.active = config.config.getBoolean(battles.nameString)
            }
        }
        for (randomizer in Randomizer.values()){
            if (!config.config.contains(randomizer.nameString)){
                config.setBoolean(randomizer.nameString, false)
                needsSave = true
            } else {
                randomizer.active = config.config.getBoolean(randomizer.nameString)
            }
        }
        
        // Initialize timer from config or set to zero if reset
        if (!reset){
            Timer.setTime(config.config.getInt(Config.Companion.Keys.TIMER).seconds)
        } else {
            Timer.setTime(ZERO)
        }
        
        // Refresh language cache after config initialization
        Lang.refreshLanguage()
        // Update PlayerMoveEvent config cache as well
        PlayerMoveEvent.refreshConfigCache()

    }

    /**
     * Called when the plugin is disabled. Handles saving config and shutting down services.
     */
    override fun shutdown() {
        reset = false
        val config = Config()
        
        resetCommand?.let { command ->
            if (command.reset) {
                config.setBoolean(Config.Companion.Keys.IS_RESET, true)
            }
        }

        if (!config.config.getBoolean(Config.Companion.Keys.IS_RESET)){
            // Use relative path instead of hardcoded Windows path
            val serverManagerConfigPath = File(dataFolder.parentFile, "MAC-ServerManager/config.yml")
            if (serverManagerConfigPath.exists()) {
                try {
                    ConfigManager(serverManagerConfigPath.absolutePath).setBoolean("online", false)
                } catch (e: Exception) {
                    server.logger.warning("Could not update server manager config: ${e.message}")
                }
            }
        }
        config.save()
        CommandAPI.onDisable()
        server.logger.info("Shutting down ChallengePlugin")
    }
}