package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.inventory.ItemStack

/**
 * Handles chest generation events, e.g. for custom loot or randomizer logic when a chest is placed or opened.
 */
object ChestGenerationEvent {
    /**
     * When loot is generated (e.g. chest opened), randomize the loot if CHEST_RANDOMIZER is active.
     */
    var onChestGenerate = listen<LootGenerateEvent> {
        if (Randomizer.CHEST_RANDOMIZER.active) {
            if (!Randomizer.PER_PLAYER.active){
                for (i in 0 until it.loot.size) {
                    try {
                        it.loot[i] ?: return@listen
                    }catch (e: IndexOutOfBoundsException){
                        return@listen
                    }
                    if (Config().randomizerConfig.getString("randomizer.everyone." + it.loot[i].type.name) != null) {
                        it.loot.removeAt(i)
                        it.loot[i] = ItemStack(Material.valueOf(Config().randomizerConfig.getString("randomizer.everyone." + it.loot[i].type.name)!!), it.loot[i].amount)
                    }else{
                        Bukkit.broadcast(Main.prefix + cmp("${it.loot[i].type.name} hat keinen Drop definiert!"))
                        it.isCancelled = true
                    }
                }
            }else {
                for (i in 0 until it.loot.size) {
                    try {
                        it.loot[i] ?: return@listen
                    } catch (e: IndexOutOfBoundsException) {
                        return@listen
                    }
                    if (Config().randomizerConfig.getString("randomizer." + it.entity?.name + "." + it.loot[i].type.name) != null) {
                        it.loot.removeAt(i)
                        it.loot[i] = ItemStack(
                            Material.valueOf(Config().randomizerConfig.getString("randomizer." + it.entity?.name + "." + it.loot[i].type.name)!!),
                            it.loot[i].amount
                        )
                    } else {
                        Bukkit.broadcast(Main.prefix + cmp("${it.loot[i].type.name} hat keinen Drop definiert!"))
                        it.isCancelled = true
                    }
                }
            }
        }
    }
}
