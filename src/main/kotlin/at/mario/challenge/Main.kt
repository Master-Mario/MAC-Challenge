package at.mario.challenge

import at.mario.challenge.events.MABClickEvent
import at.mario.challenge.challenges.*
import at.mario.challenge.commands.*
import at.mario.challenge.events.*
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.ConfigManager
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


class Main : KPaper() {
    companion object{
        var jumpHeight = 0
        lateinit var instance : Main
        val prefix = cmp("MAC", KColors.ORANGERED) + cmp(" | ", KColors.GRAY)
        var teams = mutableMapOf<Int, List<String>?>(1 to Config().config.getList("team1") as List<String>?, 2 to Config().config.getList("team2") as List<String>?)
        var onlinePlayersCollection: Collection<String> = listOf()
            get(){
                field = listOf()
                for (player in Bukkit.getOnlinePlayers()) {
                    field += player.name
                }
                return field
            }
        
        var taskStarting: KPaperRunnable? = null
        var killFile: File? = null
        var killData: FileConfiguration? = null

        val killCounts: MutableMap<UUID, MutableMap<EntityType, Int>> = mutableMapOf()
        var mabTeam1: MutableList<Player> = mutableListOf()
        var mabTeam2: MutableList<Player> = mutableListOf()
        val team = config.getString("team")
        val mode = config.getString("mode")
        const val SERVER_RUNS_ON_VPS = true
        val bossBars = mutableMapOf<Player, BossBar>()
    }
    private var resetCommand: ResetCommand? = null
    private var reset = false


    override fun load() {

        for (player in Bukkit.getOnlinePlayers()) {
            if (!Config().config.contains("run-randomizer.run-blocks-amount.${player.name}")){
                Config().add(
                    "run-randomizer.run-blocks-amount.${player.name}", 0.0
                )
            }
        }

        instance = this
        TryCommand()
        TimerCommand()
        ChallengeCommand()
        MAB_Command()
        RandomizerCommand()
        ReopenGUI()
        UtilsCommand()
        ChallengeManager()
        resetCommand = ResetCommand()

        for (player in Bukkit.getOnlinePlayers()) {
            if (Config().config.contains(player.name + ".team")){
                if (Config().config.getString(player.name + ".team") == "Team1") {
                    mabTeam1 += player
                } else if (Config().config.getString(player.name + ".team") == "Team2") {
                    mabTeam2 += player
                }
            }
        }


        if (!Config().config.contains("canTry"))
            Config().addBoolean("canTry", false)
            Config().save()

        if (!Config().config.contains("isReset"))
            Config().addBoolean("isReset", false)
            Config().save()

        if (Config().config.getBoolean("isReset")) {
            reset = true
            try {
                val world = File(Bukkit.getWorldContainer(), "world")
                val nether = File(Bukkit.getWorldContainer(), "world_nether")
                val end = File(Bukkit.getWorldContainer(), "world_the_end")
                FileUtils.deleteDirectory(world)
                FileUtils.deleteDirectory(nether)
                FileUtils.deleteDirectory(end)

                world.mkdir()
                nether.mkdir()
                end.mkdir()

                File(world, "data").mkdir()
                File(world, "datapacks").mkdir()
                File(world, "playerdata").mkdir()
                File(world, "poi").mkdir()
                File(world, "region").mkdir()

                File(nether, "data").mkdir()
                File(nether, "datapacks").mkdir()
                File(nether, "playerdata").mkdir()
                File(nether, "poi").mkdir()
                File(nether, "region").mkdir()

                File(end, "data").mkdir()
                File(end, "datapacks").mkdir()
                File(end, "playerdata").mkdir()
                File(end, "poi").mkdir()
                File(end, "region").mkdir()

            }catch (e: IOException){
                e.printStackTrace()
            }

            Config().addBoolean("isReset", false)
            Config().save()
        }
    }

    override fun startup() {
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
        CommandAPI.onEnable()
        server.consoleSender.sendMessage(cmp("\n"+
                "_________ .__           .__  .__                   ____       __________.____            ____ .__        \n" +
                "\\_   ___ \\|  |__ _____  |  | |  |   ____   ____   / ___\\  ____\\______   |    |    __ __ / ___\\|__| ____  \n" +
                "/    \\  \\/|  |  \\\\__  \\ |  | |  | _/ __ \\ /    \\ / /_/  _/ __ \\|     ___|    |   |  |  / /_/  |  |/    \\ \n" +
                "\\     \\___|   Y  \\/ __ \\|  |_|  |_\\  ___/|   |  \\\\___  /\\  ___/|    |   |    |___|  |  \\___  /|  |   |  \\\n" +
                " \\______  |___|  (____  |____|____/\\___  |___|  /_____/  \\___  |____|   |_______ |____/_____/ |__|___|  /\n" +
                "        \\/     \\/     \\/               \\/     \\/             \\/                 \\/                    \\/ ")
        )
        if (Config().config.contains("settings.view-distance")) {
            for (world in Bukkit.getWorlds()) {
                world.viewDistance = Config().config.getInt("settings.view-distance")
            }
        } else {
            Config().addInt("settings.view-distance", 10)
            Config().save()
        }
        if (Config().config.contains("settings.simulation-distance")) {
            for (world in Bukkit.getWorlds()) {
                world.simulationDistance = Config().config.getInt("settings.simulation-distance")
            }
        } else {
            Config().addInt("settings.simulation-distance", 10)
            Config().save()
        }
        if (Config().config.contains("settings.pvp")) {
            for (world in Bukkit.getWorlds()) {
                world.pvp = Config().config.getBoolean("settings.pvp")
            }
        } else {
            Config().addBoolean("settings.pvp", true)
            Config().save()
        }
        for (challenges in Challenges.values()){
            if (!Config().config.contains(challenges.nameString)){
                Config().addBoolean(challenges.nameString, false)
                Config().save()
            }else{
                challenges.active = Config().config.getBoolean(challenges.nameString)
            }
        }
        for (goals in Goals.values()){
            if (!Config().config.contains(goals.nameString)){
                Config().addBoolean(goals.nameString, false)
                Config().save()
            }else{
                goals.active = Config().config.getBoolean(goals.nameString)
            }
        }
        for (battles in Battles.values()){
            if (!Config().config.contains(battles.nameString)){
                Config().addBoolean(battles.nameString, false)
                Config().save()
            }else{
                battles.active = Config().config.getBoolean(battles.nameString)
            }
        }
        for (randomizer in Randomizer.values()){
            if (!Config().config.contains(randomizer.nameString)){
                Config().addBoolean(randomizer.nameString, false)
                Config().save()
            }else{
                randomizer.active = Config().config.getBoolean(randomizer.nameString)
            }
        }
        if (!Config().config.contains("timer")){
            Config().addInt("timer", 0)
            Config().save()
        }else if (!reset){
            Timer.setTime(Config().config.getInt("timer").seconds)
        }else{
            Timer.setTime(ZERO)
        }

        if (!Config().config.contains("run-randomizer.anzahl-der-distanz")){
            Config().addInt("run-randomizer.anzahl-der-distanz", 500)
        }

    }

    override fun shutdown() {
        reset = false
        if (resetCommand!!.reset)
            Config().addBoolean("isReset", true)

        if (!Config().config.getBoolean("isReset")){
            ConfigManager("C:\\Spiele\\MAC-Netzwerk\\ChallengeTestserver\\plugins\\MAC-ServerManager\\config.yml").setBoolean("online", false)
        }
        Config().save()
        CommandAPI.onDisable()
        server.logger.info("Shutting down ChallengePlugin")
    }
}