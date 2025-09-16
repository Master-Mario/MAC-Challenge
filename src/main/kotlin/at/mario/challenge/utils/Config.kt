package at.mario.challenge.utils

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

/**
 * Handles configuration management for the ChallengePlugin. Provides access to main, kills, and randomizer configs.
 * Supports adding and saving various types of values, and resetting the kills file.
 */
class Config {
    
    companion object {
        // Configuration key constants for consistency and to prevent typos
        object Keys {
            const val CAN_TRY = "canTry"
            const val IS_RESET = "isReset"
            const val LANGUAGE = "language"
            const val TIMER = "timer"
            
            // Settings section
            const val SETTINGS_VIEW_DISTANCE = "settings.view-distance"
            const val SETTINGS_SIMULATION_DISTANCE = "settings.simulation-distance"
            const val SETTINGS_PVP = "settings.pvp"
            const val SETTINGS_FREEZE_ON_PAUSE = "settings.freeze-on-pause"
            
            // Run randomizer section
            const val RUN_RANDOMIZER_DISTANCE_GOAL = "run-randomizer.distance-goal"
            const val RUN_RANDOMIZER_RUN_BLOCKS_AMOUNT = "run-randomizer.run-blocks-amount"
            
            // Sequence section
            const val SEQUENCE_NEXT = "sequence.next"
        }
    }
    
    /** Main configuration (config.yml) */
    val config: YamlConfiguration
    /** Kills configuration (kills.yml) */
    var killConfig: YamlConfiguration
    /** Randomizer configuration (randomizer.yml) */
    var randomizerConfig: YamlConfiguration
    /** File reference for config.yml */
    private val file: File
    /** File reference for kills.yml */
    val killsFile: File
    /** File reference for randomizer.yml */
    private val randomizerFile: File

    /**
     * Initializes config files and loads their contents. Creates files if they do not exist.
     */
    init {
        val dir = File("plugins/ChallengePlugin")

        if (!dir.exists()){
            Bukkit.getConsoleSender().sendMessage(Lang.translate("config_create"))
            dir.mkdir()
        }

        this.file = File(dir, "config.yml")
        this.killsFile = File(dir, "kills.yml")
        this.randomizerFile = File(dir, "randomizer.yml")
        if (!file.exists()){
            try {
                file.createNewFile()
            }catch (e: IOException){
                Bukkit.getConsoleSender().sendMessage(Lang.translate("config_file_create_error", file.name))
                e.printStackTrace()
            }
        }
        if (!killsFile.exists()){
            try {
                killsFile.createNewFile()
            }catch (e: IOException){
                Bukkit.getConsoleSender().sendMessage(Lang.translate("config_file_create_error", killsFile.name))
                e.printStackTrace()
            }
        }
        if (!randomizerFile.exists()){
            try {
                randomizerFile.createNewFile()
            }catch (e: IOException){
                Bukkit.getConsoleSender().sendMessage(Lang.translate("config_file_create_error", randomizerFile.name))
                e.printStackTrace()
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file)
        this.killConfig = YamlConfiguration.loadConfiguration(killsFile)
        this.randomizerConfig = YamlConfiguration.loadConfiguration(randomizerFile)

    }

    /**
     * Adds an integer value to the kills config and saves it.
     * @param path Path in the config
     * @param value Integer value to set
     */
    fun addIntKill(path: String, value: Int){
        killConfig.set(path, value)
        try {
            killConfig.save(killsFile)
        } catch (e: IOException) {
            Bukkit.getConsoleSender().sendMessage(Lang.translate("config_save_error", killsFile.name))
            e.printStackTrace()
        }
    }

    /**
     * Adds a string value to the main config and saves it.
     * @param path Path in the config
     * @param value String value to set
     */
    fun addString(path: String, value: String){
        config.set(path, value)
        save()
    }

    /**
     * Adds a string value to the randomizer config and saves it.
     * @param path Path in the config
     * @param value String value to set
     */
    fun addRandomizer(path: String, value: String){
        randomizerConfig.set(path, value)
        try {
            randomizerConfig.save(randomizerFile)
        } catch (e: IOException) {
            Bukkit.getConsoleSender().sendMessage(Lang.translate("config_save_error", randomizerFile.name))
            e.printStackTrace()
        }
    }

    /**
     * Adds a boolean value to the main config and saves it.
     * @param path Path in the config
     * @param value Boolean value to set
     */
    fun addBoolean(path: String, value: Boolean){
        config.set(path, value)
        save()
    }

    /**
     * Adds any value to the main config and saves it.
     * @param path Path in the config
     * @param value Value to set
     */
    fun add(path: String, value: Any){
        config.set(path, value)
        save()
    }

    /**
     * Adds an integer value to the main config and saves it.
     * @param path Path in the config
     * @param value Integer value to set
     */
    fun addInt(path: String, value: Int){
        config.set(path, value)
        save()
    }

    /**
     * Sets a string value in the main config without saving.
     * @param path Path in the config
     * @param value String value to set
     */
    fun setString(path: String, value: String){
        config.set(path, value)
    }

    /**
     * Sets a boolean value in the main config without saving.
     * @param path Path in the config
     * @param value Boolean value to set
     */
    fun setBoolean(path: String, value: Boolean){
        config.set(path, value)
    }

    /**
     * Sets any value in the main config without saving.
     * @param path Path in the config
     * @param value Value to set
     */
    fun set(path: String, value: Any){
        config.set(path, value)
    }

    /**
     * Sets an integer value in the main config without saving.
     * @param path Path in the config
     * @param value Integer value to set
     */
    fun setInt(path: String, value: Int){
        config.set(path, value)
    }

    /**
     * Saves the main config to disk.
     */
    fun save() {
        try {
            config.save(file)
        }catch (e: IOException){
            Bukkit.getConsoleSender().sendMessage(Lang.translate("config_save_error", file.name))
            e.printStackTrace()
        }
    }

    /**
     * Migrates old German configuration keys to new English keys.
     * This ensures backward compatibility while transitioning to English-only config.
     */
    fun migrateConfigKeys() {
        var needsSave = false
        
        // Migrate German "anzahl-der-distanz" to English "distance-goal"
        if (config.contains("run-randomizer.anzahl-der-distanz") && !config.contains(Keys.RUN_RANDOMIZER_DISTANCE_GOAL)) {
            val oldValue = config.getInt("run-randomizer.anzahl-der-distanz")
            config.set(Keys.RUN_RANDOMIZER_DISTANCE_GOAL, oldValue)
            config.set("run-randomizer.anzahl-der-distanz", null) // Remove old key
            needsSave = true
            Bukkit.getConsoleSender().sendMessage(Lang.translate("config_migrated_key", "run-randomizer.anzahl-der-distanz", Keys.RUN_RANDOMIZER_DISTANCE_GOAL))
        }
        
        // Migrate old "sequenz" to English "sequence" (though this seems already done)
        if (config.contains("sequenz")) {
            // Get all keys under "sequenz" section
            val sequenzSection = config.getConfigurationSection("sequenz")
            if (sequenzSection != null && !config.contains("sequence")) {
                for (key in sequenzSection.getKeys(false)) {
                    config.set("sequence.$key", sequenzSection.get(key))
                }
                config.set("sequenz", null) // Remove old section
                needsSave = true
                Bukkit.getConsoleSender().sendMessage(Lang.translate("config_migrated_key", "sequenz", "sequence"))
            }
        }
        
        if (needsSave) {
            save()
        }
    }

    /**
     * Deletes and recreates the kills file, resetting all kill data.
     */
    fun resetKills(){
        killsFile.delete()
        try {
            killsFile.createNewFile()
        }catch (e: IOException){
            Bukkit.getConsoleSender().sendMessage(Lang.translate("config_file_create_error", killsFile.name))
            e.printStackTrace()
        }
        this.killConfig = YamlConfiguration.loadConfiguration(killsFile)
    }
}