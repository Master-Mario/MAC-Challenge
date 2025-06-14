package at.mario.challenge.utils

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.resolver.Resolver
import java.io.File

/**
 * Manages YAML configuration files for the plugin. Supports loading, saving, and updating config values.
 */
class ConfigManager(private val configFilePath: String) {

    /** YAML parser instance */
    private val yaml: Yaml
    /** In-memory config data map */
    private var configData: MutableMap<String, Any>

    /**
     * Initializes the YAML parser and loads config data from file.
     */
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

    /**
     * Loads the config data from the YAML file, or creates a new map if the file does not exist.
     * @return MutableMap with config data
     */
    private fun loadConfig(): MutableMap<String, Any> {
        val file = File(configFilePath)
        return if (file.exists()) {
            file.inputStream().use { input ->
                yaml.load(input) as? MutableMap<String, Any> ?: mutableMapOf()
            }
        } else {
            mutableMapOf()
        }
    }

    /**
     * Saves the current config data to the YAML file.
     */
    private fun saveConfig() {
        File(configFilePath).outputStream().use { output ->
            yaml.dump(configData, output.writer())
        }
    }

    /**
     * Sets a boolean value in the config and saves it.
     * @param key Config key
     * @param value Boolean value to set
     */
    fun setBoolean(key: String, value: Boolean) {
        configData[key] = value
        saveConfig()
    }

    /**
     * Gets a boolean value from the config.
     * @param key Config key
     * @return Boolean value
     */
    fun getBoolean(key: String): Boolean {
        return configData[key] as? Boolean ?: false
    }
}