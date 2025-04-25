package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

object CraftEvent {
    private val prefix = Main.prefix
    val onCraft = listen<CraftItemEvent> {
        val player = it.whoClicked as Player
        if (Challenges.NO_CRAFTING.active) {
            it.isCancelled = true
            return@listen
        }
        if (Randomizer.CRAFT_RANDOMIZER.active) {
            if (!Randomizer.PER_PLAYER.active) {
                if (Config().randomizerConfig.getString("randomizer.everyone." + it.recipe.result.type.name) != null) {
                    it.currentItem = ItemStack(Material.valueOf(Config().randomizerConfig.getString("randomizer.everyone." + it.recipe.result.type.name)!!), it.currentItem!!.amount)
                }
            } else if (Config().randomizerConfig.getString("randomizer." + player.name + "." + it.recipe.result.type.name) != null) {
                it.currentItem = ItemStack(Material.valueOf(Config().randomizerConfig.getString("randomizer." + player.name + "." + it.recipe.result.type.name)!!), it.currentItem!!.amount)
            } else {
                Bukkit.broadcast(Main.prefix + cmp("${it.recipe.result.type.name} hat keinen Drop definiert! Ausgelößst durch ${player.name}"));
                it.isCancelled = true
            }
        }
    }
}