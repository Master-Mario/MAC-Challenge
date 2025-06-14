package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import de.miraculixx.kpaper.event.listen
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Handles player join and quit events, including health adjustments for certain challenges.
 */
object JoinQuitEvent {
    private val prefix = Main.prefix
    /**
     * Listens for player join events and applies challenge logic (e.g., max health for NO_DAMAGE).
     */
    val onJoin = listen<PlayerJoinEvent> {
        if (Challenges.NO_DAMAGE.active) {
            it.player.maxHealth = 0.5
        } else {
            it.player.resetMaxHealth()
        }
    }
    /**
     * Listens for player quit events. Add custom logic here if needed.
     */
    val onQuit = listen<PlayerQuitEvent> {
        // Add custom quit logic if needed
    }
}