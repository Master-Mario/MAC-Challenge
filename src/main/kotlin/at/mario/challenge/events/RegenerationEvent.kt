package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityRegainHealthEvent

/**
 * Handles player regeneration events, including logic for shared regeneration challenge.
 */
object RegenerationEvent {
    /**
     * Listens for entity regain health events and applies shared regeneration logic.
     */
    val onRegeneration = listen<EntityRegainHealthEvent> {
        if (it.entity !is Player) {
            return@listen
        }
        if (!Challenges.SHARED_REGENERATION.active){
            return@listen
        }
        val player = it.entity as Player
        for (online in Bukkit.getServer().onlinePlayers) {
            if (online.name == player.name) {
                player.sendMessage(Main.prefix + cmp(Lang.translate("regen_self_message", it.amount / 2, it.regainReason)))
            }else if (!it.isCancelled && it.amount != 0.0){
                online.sendMessage(Main.prefix + cmp(Lang.translate("regen_other_message", player.name, it.amount / 2, it.regainReason)))
                if (online.health + it.amount > online.maxHealth) {
                    online.health = online.maxHealth
                }else{
                    online.health += it.amount
                }
            }
        }
    }
}