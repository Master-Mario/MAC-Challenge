package at.mario.challenge.guis

import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getWorlds
import org.bukkit.ChatColor
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * GUI for changing server settings such as difficulty, PVP, natural regeneration, view distance, and simulation distance.
 * Displays the current status of each setting and allows navigation back to the main menu.
 */
object SettingsGUI {
    /**
     * Opens the settings GUI for the given player.
     * @param player The player to open the GUI for
     */
    fun open(player: HumanEntity) {
        val inventory: Inventory =
            Bukkit.createInventory(null, 27, "${ChatColor.BOLD}${ChatColor.DARK_GRAY}Setze die Einstellungen")
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
        inventory.addItem(
            Utils().createItem(
                Material.DARK_OAK_DOOR,
                1,
                glow = false,
                unbreakable = false,
                false,
                cmp("Zurück")
            )
        )
        inventory.setItem(10, item1)

        val difficulty = Bukkit.getWorld("world")!!.difficulty
        if (difficulty == Difficulty.PEACEFUL) {
            inventory.addItem(
                Utils().createItem(
                    Material.LIME_DYE,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("Schwierigkeitsgrad", KColors.GOLD, true),
                    lines = listOf(cmp("Aktuell: ", KColors.GRAY) + cmp("Friedlich", KColors.LIGHTGREEN))
                )
            )
        } else if (difficulty == Difficulty.EASY) {
            inventory.addItem(
                Utils().createItem(
                    Material.YELLOW_DYE,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("Schwierigkeitsgrad", KColors.GOLD, true),
                    lines = listOf(cmp("Aktuell: ", KColors.GRAY) + cmp("Leicht", KColors.LIGHTYELLOW))
                )
            )
        }else if (difficulty == Difficulty.NORMAL) {
            inventory.addItem(
                Utils().createItem(
                    Material.ORANGE_DYE,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("Schwierigkeitsgrad", KColors.GOLD, true),
                    lines = listOf(cmp("Aktuell: ", KColors.GRAY) + cmp("Normal", KColors.GOLDENROD))
                )
            )

        }else if (difficulty == Difficulty.HARD) {
            inventory.addItem(
                Utils().createItem(
                    Material.RED_DYE,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("Schwierigkeitsgrad", KColors.GOLD, true),
                    lines = listOf(cmp("Aktuell: ", KColors.GRAY) + cmp("Schwer", KColors.ORANGERED))
                )
            )
        }

        for (world in Bukkit.getWorlds()) {
            world.difficulty = difficulty
        }

        if (Bukkit.getWorld("world")?.pvp ?: false) {
            inventory.addItem(
                Utils().createItem(
                    Material.DIAMOND_SWORD,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("PVP", KColors.TOMATO, true),
                    lines = listOf(cmp("Status: ", KColors.GRAY) + cmp("Aktiviert", KColors.LIGHTGREEN))
                )
            )
        }else {
            inventory.addItem(
                Utils().createItem(
                    Material.DIAMOND_SWORD,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("PVP", KColors.TOMATO, true),
                    lines = listOf(cmp("Status: ", KColors.GRAY) + cmp("Deaktiviert", KColors.ORANGERED))
                )
            )
        }
        if (getWorlds()[0].getGameRuleValue(GameRule.NATURAL_REGENERATION) == true) {
            inventory.addItem(
                Utils().createItem(
                    Material.GLISTERING_MELON_SLICE,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("Natürliche Regeneration", KColors.AZURE, true),
                    lines = listOf(cmp("Status: ", KColors.GRAY) + cmp("Aktiviert", KColors.LIGHTGREEN))
                )
            )
        } else {
            inventory.addItem(
                Utils().createItem(
                    Material.GLISTERING_MELON_SLICE,
                    1,
                    glow = false,
                    unbreakable = false,
                    false,
                    cmp("Natürliche Regeneration", KColors.AZURE, true),
                    lines = listOf(cmp("Status: ", KColors.GRAY) + cmp("Deaktiviert", KColors.ORANGERED))
                )
            )
        }

        inventory.addItem(
            Utils().createItem(
                Material.SPYGLASS,
                1,
                glow = false,
                unbreakable = false,
                false,
                cmp("Sichtweite", KColors.GAINSBORO, true),
                lines = listOf(cmp("Aktuell: ", KColors.GRAY) + cmp(Bukkit.getWorld("world")!!.viewDistance.toString(), KColors.LIGHTGREEN))
            )
        )

        inventory.addItem(
            Utils().createItem(
                Material.ENDER_PEARL,
                1,
                glow = false,
                unbreakable = false,
                false,
                cmp("Simulationsdistanz", KColors.PURPLE, true),
                lines = listOf(cmp("Aktuell: ", KColors.GRAY) + cmp(Bukkit.getWorld("world")!!.simulationDistance.toString(), KColors.LIGHTGREEN))
            )
        )

        inventory.setItem(10, ItemStack(Material.AIR))
        player.openInventory(inventory)
    }
}