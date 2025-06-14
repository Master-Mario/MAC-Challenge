package at.mario.challenge.commands

import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import org.bukkit.Bukkit

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
            waveGuiHome.setItem(2, at.mario.challenge.utils.Utils().createItem(org.bukkit.Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp(Lang.translate("mab_wave1"), KColors.ORANGERED)))
            waveGuiHome.setItem(4, at.mario.challenge.utils.Utils().createItem(org.bukkit.Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp(Lang.translate("mab_wave2"), KColors.ORANGERED)))
            waveGuiHome.setItem(6, at.mario.challenge.utils.Utils().createItem(org.bukkit.Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp(Lang.translate("mab_wave3"), KColors.ORANGERED)))
            waveGuiHome.setItem(8, at.mario.challenge.utils.Utils().createItem(org.bukkit.Material.LIME_DYE, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp(Lang.translate("done"), KColors.LIME)))
            de.miraculixx.kpaper.extensions.onlinePlayers.forEach { player ->  player.openInventory(waveGuiHome) }
        }
    }
}