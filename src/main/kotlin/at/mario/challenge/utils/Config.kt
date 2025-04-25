package at.mario.challenge.utils

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException


class Config {
    val config: YamlConfiguration
    var killConfig: YamlConfiguration
    var randomizerConfig: YamlConfiguration
    private val file: File
    val killsFile: File
    private val randomizerFile: File

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
    fun addIntKill(path: String, value: Int){
        killConfig.set(path, value)
        killConfig.save(killsFile)
    }
    fun addString(path: String, value: String){
        config.set(path, value)
        config.save(file)
    }
    fun addRandomizer(path: String, value: String){
        randomizerConfig.set(path, value)
        randomizerConfig.save(randomizerFile)
    }
    fun addBoolean(path: String, value: Boolean){
        config.set(path, value)
        config.save(file)
    }
    fun add(path: String, value: Any){
        config.set(path, value)
        config.save(file)
    }
    fun addInt(path: String, value: Int){
        config.set(path, value)
        config.save(file)
    }
    fun save() {
        try {
            config.save(file)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
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