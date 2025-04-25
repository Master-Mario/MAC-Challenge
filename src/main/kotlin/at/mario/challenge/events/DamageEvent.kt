package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.kill
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

object DamageEvent {
    val onDamage = listen<EntityDamageEvent> {
        if (Challenges.NO_FALL_DAMAGE.active){
            if (it.cause == EntityDamageEvent.DamageCause.FALL) {
                val player = it.entity
                if (player is Player) {
                    player.sendMessage(Main.prefix + cmp(ChatColor.RED.toString() + "Du" + ChatColor.DARK_GRAY + " hast " + ChatColor.RED + "${it.finalDamage / 2} ❤ Schaden ${ChatColor.DARK_GRAY} von " + ChatColor.RED + it.cause + ChatColor.DARK_GRAY + " genommen"))
                    player.health = 0.0
                }

            }
        }
        if (Challenges.NO_DAMAGE.active) {
            val player = it.entity
            if (player !is Player) return@listen

            for (online in Bukkit.getServer().onlinePlayers) {
                if (online.name != player.name) {
                    online.sendMessage(Main.prefix + cmp(ChatColor.RED.toString() + player.name + ChatColor.DARK_GRAY + " hat " + ChatColor.RED + "${it.finalDamage / 2} ❤ Schaden ${ChatColor.DARK_GRAY} von " + ChatColor.RED + it.cause + ChatColor.DARK_GRAY + " genommen"))
                }else if (!it.isCancelled && it.finalDamage.toInt() != 0){
                    player.sendMessage(Main.prefix + cmp(ChatColor.RED.toString() + "Du" + ChatColor.DARK_GRAY + " hast " + ChatColor.RED + "${it.finalDamage / 2} ❤ Schaden ${ChatColor.DARK_GRAY} von " + ChatColor.RED + it.cause + ChatColor.DARK_GRAY + " genommen"))
                    if (it.finalDamage < player.health)
                        player.health = 0.0
                }
            }
        } else if (Challenges.SHARED_DAMAGE.active) {
            val player = it.entity
            if (player !is Player) return@listen

            for (online in Bukkit.getServer().onlinePlayers) {
                if (online.name == player.name && it.finalDamage != 0.0 && !it.isCancelled) {
                    player.sendMessage(Main.prefix + cmp(ChatColor.RED.toString() + "Du" + ChatColor.DARK_GRAY + " hast " + ChatColor.RED + "${it.finalDamage / 2} ❤ Schaden ${ChatColor.DARK_GRAY} von " + ChatColor.RED + it.cause + ChatColor.DARK_GRAY + " genommen"))
                }else if (!it.isCancelled && it.finalDamage != 0.0){
                    online.sendMessage(Main.prefix + cmp(ChatColor.RED.toString() + player.name + ChatColor.DARK_GRAY + " hat " + ChatColor.RED + "${it.finalDamage / 2} ❤ Schaden ${ChatColor.DARK_GRAY} von " + ChatColor.RED + it.cause + ChatColor.DARK_GRAY + " genommen"))
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