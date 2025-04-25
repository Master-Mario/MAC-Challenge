package at.mario.challenge.utils

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.resolver.Resolver
import java.io.File

class ConfigManager(private val configFilePath: String) {

    private val yaml: Yaml
    private var configData: MutableMap<String, Any>

    init {
        // Configure YAML settings
        val options = DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            isPrettyFlow = true
        }
        yaml = Yaml(Constructor(LoaderOptions()), Representer(options), options, Resolver())

        // Load existing config data or create a new map if the file does not exist
        configData = loadConfig()
    }

    // Loads the config data from the YAML file
    private fun loadConfig(): MutableMap<String, Any> {
        val file = File(configFilePath)
        return if (file.exists()) {
            file.inputStream().use { input ->
                yaml.load(input) as MutableMap<String, Any>
            }
        } else {
            mutableMapOf()
        }
    }

    // Saves the config data to the YAML file
    private fun saveConfig() {
        File(configFilePath).outputStream().use { output ->
            yaml.dump(configData, output.writer())
        }
    }

    // Sets a boolean value in the config and saves it
    fun setBoolean(key: String, value: Boolean) {
        configData[key] = value
        saveConfig()
    }
    fun getBoolean(key: String): Boolean {
        return configData[key] as Boolean
    }
}