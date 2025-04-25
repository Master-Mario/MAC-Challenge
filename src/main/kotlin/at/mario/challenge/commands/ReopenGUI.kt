package at.mario.challenge.commands

import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.onlinePlayers
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

class ReopenGUI {
    val reopenGUI = commandTree("reopenMAB") {
        anyExecutor { _, _ ->
            val waveGuiHome = Bukkit.createInventory(null, 9, "${ChatColor.DARK_RED}Mob-Army-Waves")
            waveGuiHome.setItem(2, Utils().createItem(Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Wave 1", KColors.ORANGERED)))
            waveGuiHome.setItem(4, Utils().createItem(Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Wave 2", KColors.ORANGERED)))
            waveGuiHome.setItem(6, Utils().createItem(Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Wave 3", KColors.ORANGERED)))
            waveGuiHome.setItem(8, Utils().createItem(Material.LIME_DYE, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Fertig", KColors.LIME)))
            onlinePlayers.forEach { player ->  player.openInventory(waveGuiHome) }
        }
    }
}