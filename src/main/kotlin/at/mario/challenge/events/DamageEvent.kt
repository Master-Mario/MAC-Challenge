package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.kill
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

/**
 * Handles player damage events, including logic for no fall damage, no damage, and shared damage challenges.
 */
object DamageEvent {
    /**
     * Listens for entity damage events and applies challenge logic for damage handling.
     */
    val onDamage = listen<EntityDamageEvent> {
        if (Challenges.NO_FALL_DAMAGE.active){
            if (it.cause == EntityDamageEvent.DamageCause.FALL) {
                val player = it.entity
                if (player is Player) {
                    player.sendMessage(Main.prefix + cmp(Lang.translate("damage_you_took", "Du", "${it.finalDamage / 2}", it.cause)))
                    player.health = 0.0
                }

            }
        }
        if (Challenges.NO_DAMAGE.active) {
            val player = it.entity
            if (player !is Player) return@listen

            for (online in Bukkit.getServer().onlinePlayers) {
                if (online.name != player.name) {
                    online.sendMessage(Main.prefix + cmp(Lang.translate("damage_other_took", player.name, "${it.finalDamage / 2}", it.cause)))
                }else if (!it.isCancelled && it.finalDamage.toInt() != 0){
                    player.sendMessage(Main.prefix + cmp(Lang.translate("damage_you_took", "Du", "${it.finalDamage / 2}", it.cause)))
                    if (it.finalDamage < player.health)
                        player.health = 0.0
                }
            }
        } else if (Challenges.SHARED_DAMAGE.active) {
            val player = it.entity
            if (player !is Player) return@listen

            for (online in Bukkit.getServer().onlinePlayers) {
                if (online.name == player.name && it.finalDamage != 0.0 && !it.isCancelled) {
                    player.sendMessage(Main.prefix + cmp(Lang.translate("damage_you_took", "Du", "${it.finalDamage / 2}", it.cause)))
                }else if (!it.isCancelled && it.finalDamage != 0.0){
                    online.sendMessage(Main.prefix + cmp(Lang.translate("damage_other_took", player.name, "${it.finalDamage / 2}", it.cause)))
                    if (online.health > it.damage) {
                        online.health -= it.damage
                    }else{
                        online.health = 0.0
                    }
                }
            }
        }
    }
}