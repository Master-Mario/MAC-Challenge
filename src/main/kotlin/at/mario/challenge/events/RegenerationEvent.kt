package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityRegainHealthEvent

object RegenerationEvent {
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
                player.sendMessage(Main.prefix + cmp(ChatColor.RED.toString() + "Du" + ChatColor.DARK_GRAY + " hast " + ChatColor.RED + "${it.amount / 2} ❤ Heilung ${ChatColor.DARK_GRAY} von " + ChatColor.RED + it.regainReason + ChatColor.DARK_GRAY + " bekommen"))
            }else if (!it.isCancelled && it.amount != 0.0){
                online.sendMessage(Main.prefix + cmp(ChatColor.RED.toString() + player.name + ChatColor.DARK_GRAY + " hat " + ChatColor.RED + "${it.amount / 2} ❤ Heilung ${ChatColor.DARK_GRAY} von " + ChatColor.RED + it.regainReason + ChatColor.DARK_GRAY + " bekommen"))
                if (online.health + it.amount > online.maxHealth) {
                    online.health = online.maxHealth
                }else{
                    online.health += it.amount
                }
            }
        }
    }
}