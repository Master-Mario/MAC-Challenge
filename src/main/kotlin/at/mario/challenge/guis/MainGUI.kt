package at.mario.challenge.guis

import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Lang
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Main menu GUI for the plugin. Provides navigation to goals, challenges, battles, and settings.
 */
object MainGUI {
    /**
     * Opens the main menu GUI for the given player.
     * @param player The player to open the GUI for
     */
    fun open(player: HumanEntity){
        val inventory: Inventory = Bukkit.createInventory(null, 45, Lang.translate("main_menu_title"))
        val item1 = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val meta1: ItemMeta = item1.itemMeta!!
        meta1.displayName(cmp(""))
        item1.itemMeta = meta1

        for (i in 0..8) {
            inventory.setItem(i, item1)
        }
        for (i in inventory.size-9 ..inventory.size-1) {
            inventory.setItem(i, item1)
        }
        inventory.setItem(21, Utils().createItem(Material.DRAGON_HEAD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("${ChatColor.BOLD}${Lang.translate("goals")}", KColors.LIGHTPURPLE)))
        inventory.setItem(19, Utils().createItem(Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("${ChatColor.BOLD}${Lang.translate("challenges")}", KColors.LIGHTBLUE)))
        inventory.setItem(23, Utils().createItem(Material.SHIELD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("${ChatColor.BOLD}${Lang.translate("battles")}", KColors.LIGHTGREEN)))
        inventory.setItem(25, Utils().createItem(Material.COMPARATOR, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("${ChatColor.BOLD}${Lang.translate("settings")}", KColors.LIGHTCORAL)))

        player.openInventory(inventory)
    }
}