package at.mario.challenge.guis

import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * GUI for selecting a randomizer. Displays all available randomizer options and their status.
 */
object RandomizerGUI {
    /**
     * Opens the randomizer selection GUI for the given player.
     * @param player The player to open the GUI for
     */
    fun open(player: HumanEntity) {
        val inventory: Inventory = Bukkit.createInventory(null, 27, "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Wähle einen Randomizer")
        val item1 = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val meta1: ItemMeta = item1.itemMeta!!
        meta1.displayName(cmp(""))
        item1.itemMeta = meta1

        for (i in 0..8) {
            inventory.setItem(i, item1)
        }
        for (i in 18..26) {
            inventory.setItem(i, item1)
        }
        inventory.addItem(Utils().createItem(Material.DARK_OAK_DOOR, 1, glow = false, unbreakable = false, false, cmp("Zurück")))
        inventory.setItem(10, item1)

        for (randomizer in Randomizer.values()) {
            val item = ItemStack(randomizer.icon)
            val meta: ItemMeta = item.itemMeta!!
            meta.displayName(randomizer.nameComponent)
            if (randomizer.active) {
                meta.lore(listOf(randomizer.description, cmp(""), cmp("Status: ", KColors.GRAY) + cmp("Aktiv", KColors.GREEN)))
            }else{
                meta.lore(listOf(randomizer.description, cmp(""), cmp("Status: ", KColors.GRAY) + cmp("Inaktiv", KColors.RED)))
            }
            item.itemMeta = meta
            inventory.addItem(item)
        }
        inventory.setItem(10, ItemStack(Material.AIR))
        player.openInventory(inventory)
    }
}