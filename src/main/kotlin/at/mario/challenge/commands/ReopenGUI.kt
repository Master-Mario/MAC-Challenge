package at.mario.challenge.commands

import at.mario.challenge.utils.Lang
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.onlinePlayers
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import org.bukkit.Bukkit
import org.bukkit.Material

/**
 * Command for reopening the Mob Army Battle (MAB) GUI for all online players.
 */
class ReopenGUI {
    /**
     * The command to reopen the MAB GUI for all players.
     */
    val reopenGUI = commandTree("reopenMAB") {
        anyExecutor { _, _ ->
            val waveGuiHome = Bukkit.createInventory(null, 9, Lang.translate("mab_waves_title"))
            val utils = Utils()
            
            // Create wave items with cleaner code
            val wave1Item = utils.createItem(
                Material.DIAMOND_SWORD, 1, 
                glow = false, unbreakable = true, hideUnbreakable = true, 
                cmp(Lang.translate("mab_wave1"), KColors.ORANGERED)
            )
            val wave2Item = utils.createItem(
                Material.DIAMOND_SWORD, 1, 
                glow = false, unbreakable = true, hideUnbreakable = true, 
                cmp(Lang.translate("mab_wave2"), KColors.ORANGERED)
            )
            val wave3Item = utils.createItem(
                Material.DIAMOND_SWORD, 1, 
                glow = false, unbreakable = true, hideUnbreakable = true, 
                cmp(Lang.translate("mab_wave3"), KColors.ORANGERED)
            )
            val doneItem = utils.createItem(
                Material.LIME_DYE, 1, 
                glow = false, unbreakable = true, hideUnbreakable = true, 
                cmp(Lang.translate("done"), KColors.LIME)
            )
            
            // Set items in inventory
            waveGuiHome.setItem(2, wave1Item)
            waveGuiHome.setItem(4, wave2Item)
            waveGuiHome.setItem(6, wave3Item)
            waveGuiHome.setItem(8, doneItem)
            
            // Open inventory for all online players
            onlinePlayers.forEach { player -> 
                player.openInventory(waveGuiHome) 
            }
        }
    }
}