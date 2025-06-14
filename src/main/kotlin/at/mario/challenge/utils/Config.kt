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
            Bukkit.getConsoleSender().sendMessage("Creating Config...")
            dir.mkdir()
        }

        this.file = File(dir, "config.yml")
        this.killsFile = File(dir, "kills.yml")
        this.randomizerFile = File(dir, "randomizer.yml")
        if (!file.exists()){
            try {
                file.createNewFile()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        if (!killsFile.exists()){
            try {
                killsFile.createNewFile()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        if (!randomizerFile.exists()){
            try {
                randomizerFile.createNewFile()
            }catch (e: IOException){
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
        try {
            config.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
        try {
            config.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Adds any value to the main config and saves it.
     * @param path Path in the config
     * @param value Value to set
     */
    fun add(path: String, value: Any){
        config.set(path, value)
        try {
            config.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Adds an integer value to the main config and saves it.
     * @param path Path in the config
     * @param value Integer value to set
     */
    fun addInt(path: String, value: Int){
        config.set(path, value)
        try {
            config.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Saves the main config to disk.
     */
    fun save() {
        try {
            config.save(file)
        }catch (e: IOException){
            e.printStackTrace()
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
            e.printStackTrace()
        }
        this.killConfig = YamlConfiguration.loadConfiguration(killsFile)
    }
}