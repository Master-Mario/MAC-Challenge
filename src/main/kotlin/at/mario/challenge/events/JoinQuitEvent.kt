package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object JoinQuitEvent {
    private val prefix = Main.prefix
    val onJoin = listen<PlayerJoinEvent> {
        if (Challenges.NO_DAMAGE.active) {
            it.player.maxHealth = 0.5
        }else{
            it.player.resetMaxHealth()
        }
    }
}