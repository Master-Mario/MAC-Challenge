package at.mario.challenge.challenges

import at.mario.challenge.utils.Config

/**
 * Manages the number of challenge attempts ("trys") and persists them in the config.
 * Automatically loads and saves the attempt count on changes.
 */
class TrySystem {
    /**
     * The current number of attempts. Setting this value saves it to the config.
     */
    var attempts: Int = 0
        set(value) {
            field = value
            saveToConfig(value)
        }

    /**
     * Loads the attempt count from the config on initialization.
     */
    init {
        loadFromConfig()
    }

    /**
     * Loads the attempt count from the config file.
     */
    private fun loadFromConfig() {
        attempts = Config().config.getInt("try_attempts")
    }

    /**
     * Saves the attempt count to the config file.
     * @param int The number of attempts to save
     */
    private fun saveToConfig(int: Int) {
        Config().addInt("try_attempts", int)
        Config().save()
    }
}