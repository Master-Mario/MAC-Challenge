package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.ItemStack

/**
 * Handles crafting events. Cancels crafting if NO_CRAFTING is active, and applies randomizer logic if enabled.
 */
object CraftEvent {
    private val prefix = Main.prefix
    /**
     * Listens for crafting events and applies challenge/randomizer rules.
     */
    val onCraft = listen<CraftItemEvent> {
        val player = it.whoClicked as Player
        if (Challenges.NO_CRAFTING.active) {
            it.isCancelled = true
            return@listen
        }
        if (Randomizer.CRAFT_RANDOMIZER.active) {
            if (!Randomizer.PER_PLAYER.active) {
                val mapped = Config().randomizerConfig.getString("randomizer.everyone." + it.recipe.result.type.name)
                if (mapped != null) {
                    it.currentItem = ItemStack(Material.valueOf(mapped), it.currentItem!!.amount)
                }
            } else {
                val mapped = Config().randomizerConfig.getString("randomizer." + player.name + "." + it.recipe.result.type.name)
                if (mapped != null) {
                    it.currentItem = ItemStack(Material.valueOf(mapped), it.currentItem!!.amount)
                } else {
                    Bukkit.broadcast(Main.prefix + cmp(Lang.translate("craft_no_drop_defined", it.recipe.result.type.name, player.name)))
                    it.isCancelled = true
                }
            }
        }
    }
}