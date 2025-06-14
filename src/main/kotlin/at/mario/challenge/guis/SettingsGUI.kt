package at.mario.challenge.guis

import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
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
            Bukkit.createInventory(null, 27, Lang.translate("settings_title"))
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
                cmp(Lang.translate("back"))
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
                    cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                    lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("peaceful"), KColors.LIGHTGREEN))
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
                    cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                    lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("easy"), KColors.LIGHTYELLOW))
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
                    cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                    lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("normal"), KColors.GOLDENROD))
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
                    cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                    lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("hard"), KColors.ORANGERED))
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
                    cmp(Lang.translate("pvp"), KColors.TOMATO, true),
                    lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("enabled"), KColors.LIGHTGREEN))
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
                    cmp(Lang.translate("pvp"), KColors.TOMATO, true),
                    lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("disabled"), KColors.ORANGERED))
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
                    cmp(Lang.translate("natural_regen"), KColors.AZURE, true),
                    lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("enabled"), KColors.LIGHTGREEN))
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
                    cmp(Lang.translate("natural_regen"), KColors.AZURE, true),
                    lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("disabled"), KColors.ORANGERED))
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
                cmp(Lang.translate("view_distance"), KColors.GAINSBORO, true),
                lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Bukkit.getWorld("world")!!.viewDistance.toString(), KColors.LIGHTGREEN))
            )
        )

        inventory.addItem(
            Utils().createItem(
                Material.ENDER_PEARL,
                1,
                glow = false,
                unbreakable = false,
                false,
                cmp(Lang.translate("simulation_distance"), KColors.PURPLE, true),
                lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Bukkit.getWorld("world")!!.simulationDistance.toString(), KColors.LIGHTGREEN))
            )
        )

        inventory.addItem(
            Utils().createItem(
                Material.BOOK,
                1,
                glow = false,
                unbreakable = false,
                false,
                cmp(Lang.translate("language"), KColors.LIGHTBLUE, true),
                lines = listOf(cmp(Lang.translate("language_change_hint"), KColors.GRAY),
                    cmp(Lang.translate("current"), KColors.GRAY) + cmp(Config().config.getString("language") ?: "de", KColors.LIGHTGREEN))
            )
        )

        inventory.setItem(10, ItemStack(Material.AIR))
        player.openInventory(inventory)
    }
}