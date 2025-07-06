package at.mario.challenge.guis

import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getWorlds
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack
import kotlin.math.min

/**
 * GUI for changing server settings such as difficulty, PVP, natural regeneration, view distance, and simulation distance.
 * Displays the current status of each setting and allows navigation back to the main menu.
 */
object SettingsGUI {
    private const val SETTINGS_PER_PAGE = 5
    val playerPages = mutableMapOf<String, Int>()

    /**
     * Opens the settings GUI for the given player.
     * @param player The player to open the GUI for
     */
    fun open(player: HumanEntity, page: Int = 0) {
        val settings = mutableListOf<ItemStack>()
        // Difficulty
        val difficulty = Bukkit.getWorld("world")!!.difficulty
        when (difficulty) {
            org.bukkit.Difficulty.PEACEFUL -> settings.add(Utils().createItem(
                Material.LIME_DYE, 1, glow = false, unbreakable = false, false,
                cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("peaceful"), KColors.LIGHTGREEN))
            ))
            org.bukkit.Difficulty.EASY -> settings.add(Utils().createItem(
                Material.YELLOW_DYE, 1, glow = false, unbreakable = false, false,
                cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("easy"), KColors.LIGHTYELLOW))
            ))
            org.bukkit.Difficulty.NORMAL -> settings.add(Utils().createItem(
                Material.ORANGE_DYE, 1, glow = false, unbreakable = false, false,
                cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("normal"), KColors.GOLDENROD))
            ))
            org.bukkit.Difficulty.HARD -> settings.add(Utils().createItem(
                Material.RED_DYE, 1, glow = false, unbreakable = false, false,
                cmp(Lang.translate("difficulty"), KColors.GOLD, true),
                lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Lang.translate("hard"), KColors.ORANGERED))
            ))
        }
        // PVP
        if (Bukkit.getWorld("world")?.pvp ?: false) {
            settings.add(Utils().createItem(
                Material.DIAMOND_SWORD, 1, glow = true, unbreakable = false, false,
                cmp(Lang.translate("pvp"), KColors.TOMATO, true),
                lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("enabled"), KColors.LIGHTGREEN))
            ))
        } else {
            settings.add(Utils().createItem(
                Material.DIAMOND_SWORD, 1, glow = false, unbreakable = false, false,
                cmp(Lang.translate("pvp"), KColors.TOMATO, true),
                lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("disabled"), KColors.ORANGERED))
            ))
        }
        // Disable Movement while Timer paused
        if (Config().config.getBoolean("settings.freeze-on-pause")) {
            settings.add(Utils().createItem(
                Material.IRON_BOOTS, 1, glow = true, unbreakable = false, false,
                cmp(Lang.translate("freeze_on_pause"), KColors.SKYBLUE, true),
                lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("enabled"), KColors.LIGHTGREEN))
            ))
        } else {
            settings.add(Utils().createItem(
                Material.IRON_BOOTS, 1, glow = false, unbreakable = false, false,
                cmp(Lang.translate("freeze_on_pause"), KColors.SKYBLUE, true),
                lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("disabled"), KColors.ORANGERED))
            ))
        }
        // Natural Regeneration
        if (getWorlds()[0].getGameRuleValue(GameRule.NATURAL_REGENERATION) == true) {
            settings.add(Utils().createItem(
                Material.GLISTERING_MELON_SLICE, 1, glow = true, unbreakable = false, false,
                cmp(Lang.translate("natural_regen"), KColors.AZURE, true),
                lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("enabled"), KColors.LIGHTGREEN))
            ))
        } else {
            settings.add(Utils().createItem(
                Material.GLISTERING_MELON_SLICE, 1, glow = false, unbreakable = false, false,
                cmp(Lang.translate("natural_regen"), KColors.AZURE, true),
                lines = listOf(cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("disabled"), KColors.ORANGERED))
            ))
        }
        // View Distance
        settings.add(Utils().createItem(
            Material.SPYGLASS, 1, glow = false, unbreakable = false, false,
            cmp(Lang.translate("view_distance"), KColors.GAINSBORO, true),
            lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Bukkit.getWorld("world")!!.viewDistance.toString(), KColors.LIGHTGREEN))
        ))
        // Simulation Distance
        settings.add(Utils().createItem(
            Material.ENDER_PEARL, 1, glow = false, unbreakable = false, false,
            cmp(Lang.translate("simulation_distance"), KColors.PURPLE, true),
            lines = listOf(cmp(Lang.translate("current"), KColors.GRAY) + cmp(Bukkit.getWorld("world")!!.simulationDistance.toString(), KColors.LIGHTGREEN))
        ))
        // Language
        settings.add(Utils().createItem(
            Material.BOOK, 1, glow = false, unbreakable = false, false,
            cmp(Lang.translate("language"), KColors.LIGHTBLUE, true),
            lines = listOf(
                cmp(Lang.translate("language_change_hint"), KColors.GRAY),
                cmp(Lang.translate("current"), KColors.GRAY) + cmp(Config().config.getString("language") ?: "de", KColors.LIGHTGREEN)
            )
        ))

        val maxPages = (settings.size - 1) / SETTINGS_PER_PAGE
        val inventory = Bukkit.createInventory(null, 27, cmp(Lang.translate("settings_title")))
        val filler = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
            itemMeta = itemMeta?.apply { displayName(cmp("")) }
        }
        // Set frame
        for (i in 0..8) inventory.setItem(i, filler)
        for (i in inventory.size-9..inventory.size-1) inventory.setItem(i, filler)
        // Back button
        inventory.setItem(9, Utils().createItem(Material.DARK_OAK_DOOR, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("back"))))
        inventory.setItem(10, Utils().createItem(Material.STRUCTURE_VOID, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("back"))))
        inventory.setItem(16, Utils().createItem(Material.STRUCTURE_VOID, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("back"))))
        // Navigation
        if (page > 0) {
            inventory.setItem(9, Utils().createItem(Material.ARROW, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("previous_page"))))
        }
        if (page < maxPages) {
            inventory.setItem(17, Utils().createItem(Material.ARROW, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("next_page"))))
        }
        // Show settings for this page
        val startIndex = page * SETTINGS_PER_PAGE
        val endIndex = min(startIndex + SETTINGS_PER_PAGE, settings.size)
        for (i in startIndex until endIndex) {
            inventory.addItem(settings[i])
        }
        inventory.setItem(10, ItemStack(Material.AIR))
        inventory.setItem(16, ItemStack(Material.AIR))
        playerPages[player.name] = page
        player.openInventory(inventory)
    }
}