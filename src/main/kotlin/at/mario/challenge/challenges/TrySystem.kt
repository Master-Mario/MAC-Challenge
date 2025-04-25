package at.mario.challenge.challenges

import at.mario.challenge.utils.Config

class TrySystem() {
    var attempts: Int = 0
        set(value) {
            field = value
            saveToConfig(value)
        }
    init {
        loadFromConfig()
    }

    private fun loadFromConfig() {
        attempts = Config().config.getInt("try_attempts")
    }

    private fun saveToConfig(int: Int) {
        Config().addInt("try_attempts", int)
        Config().save()
    }
}