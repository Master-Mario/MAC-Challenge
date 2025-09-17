package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Battles
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.challenges.Goals
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.guis.*
import at.mario.challenge.guis.ChallengeGUI.open
import at.mario.challenge.guis.ChallengeGUI.playerPages
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plainText
import de.miraculixx.kpaper.extensions.bukkit.plus
import de.miraculixx.kpaper.items.name
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.minecraft.world.level.GameRules
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import java.awt.Desktop
import javax.swing.Action

/**
 * Handles inventory click events for all custom GUIs in the plugin.
 * Routes clicks to the correct GUI logic and prevents unwanted item movement.
 */
object InventoryClickEvent {
    /**
     * Listens for inventory click events and handles GUI navigation and item movement prevention.
     */
    val onInventoryClick = listen<InventoryClickEvent> {
        val player = it.whoClicked as Player
        val title = it.view.title
        // Prevent item movement in all custom GUIs
        if (title.contains(Lang.translate("main_menu_title")) || title.contains(Lang.translate("challenge_menu_title")) || title.contains(Lang.translate("goal_menu_title")) || title.contains(Lang.translate("battle_menu_title")) || title.contains(Lang.translate("randomizer_title")) || title.contains(Lang.translate("settings_title")) || title.contains(Lang.translate("mab_waves_title")) || title.contains(Lang.translate("mab_wave1")) || title.contains(Lang.translate("mab_wave2")) || title.contains(Lang.translate("mab_wave3"))) {
            it.isCancelled = true
        }
        // Navigation logic for GUIs
        if (title.contains(Lang.translate("main_menu_title"))) {
            when (it.currentItem?.itemMeta?.displayName()?.plainText()) {
                "§d§l" + Lang.translate("goals") -> GoalGUI.open(it.whoClicked)
                "§b§l" + Lang.translate("challenges") -> ChallengeGUI.open(it.whoClicked)
                "§a§l" + Lang.translate("battles") -> BattleGUI.open(it.whoClicked)
                "§c§l" + Lang.translate("settings") -> SettingsGUI.open(it.whoClicked)
            }
        } else if (title.contains(Lang.translate("challenge_menu_title"))) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains(Lang.translate("goal_menu_title"))) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains(Lang.translate("battle_menu_title"))) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains(Lang.translate("randomizer_title"))) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains(Lang.translate("settings_title"))) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        }
        if (it.view.title == Lang.translate("challenge_menu_title")) {
            it.isCancelled = true
            for (challenges in Challenges.values()) {
                if (challenges.nameComponent == it.currentItem?.itemMeta?.name) {
                    if (challenges.nameComponent == Challenges.RANDOMIZER.nameComponent){
                        RandomizerGUI.open(player)
                        val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                        return@listen
                    }
                    if (challenges.active) {
                        challenges.active = false
                        Config().addBoolean(challenges.nameString, false)
                        val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    } else {
                        challenges.active = true
                        Config().addBoolean(challenges.nameString, true)
                        val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    }
                    open(player, playerPages[player.name] ?: 0)
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("previous_page"))) {
                val currentPage = playerPages[player.name] ?: 0
                open(player, currentPage - 1)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            } else if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("next_page"))) {
                val currentPage = playerPages[player.name] ?: 0
                open(player, currentPage + 1)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("back"))){
                MainGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }

        }else if(it.view.title == Lang.translate("main_menu_title")) {
            it.isCancelled = true
            if (cmp("${ChatColor.BOLD}" + Lang.translate("challenges"), KColors.LIGHTBLUE)== it.currentItem?.itemMeta?.name) {
                ChallengeGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (cmp("${ChatColor.BOLD}" + Lang.translate("goals"), KColors.LIGHTPURPLE)== it.currentItem?.itemMeta?.name) {
                GoalGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (cmp("${ChatColor.BOLD}" + Lang.translate("battles"), KColors.LIGHTGREEN)== it.currentItem?.itemMeta?.name) {
                BattleGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (cmp("${ChatColor.BOLD}" + Lang.translate("settings"), KColors.LIGHTCORAL)== it.currentItem?.itemMeta?.name) {
                SettingsGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)

            }
        }else if (it.view.title == Lang.translate("goal_menu_title")){
            it.isCancelled = true
            for (goals in Goals.values()) {
                if (goals.nameComponent == it.currentItem?.itemMeta?.name) {
                    if (goals.active) {
                        goals.active = false
                        Config().addBoolean(goals.nameString, false)
                        val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    } else {
                        goals .active = true
                        Config().addBoolean(goals.nameString, true)
                        val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    }
                    GoalGUI.open(player)
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("back"))){
                MainGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
        }else if (it.view.title == Lang.translate("settings_title")){
            it.isCancelled = true
            // Navigation for settings pages
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("previous_page"))) {
                val currentPage = at.mario.challenge.guis.SettingsGUI.playerPages[player.name] ?: 0
                at.mario.challenge.guis.SettingsGUI.open(player, currentPage - 1)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                return@listen
            } else if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("next_page"))) {
                val currentPage = at.mario.challenge.guis.SettingsGUI.playerPages[player.name] ?: 0
                at.mario.challenge.guis.SettingsGUI.open(player, currentPage + 1)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                return@listen
            }
            if (it.currentItem?.type == Material.LIME_DYE){
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                for (world in Bukkit.getWorlds()) {
                    world.difficulty = Difficulty.EASY
                }
            }else if (it.currentItem?.type == Material.YELLOW_DYE) {
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                for (world in Bukkit.getWorlds()) {
                    world.difficulty = Difficulty.NORMAL
                }
            }else if (it.currentItem?.type == Material.ORANGE_DYE) {
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                for (world in Bukkit.getWorlds()) {
                    world.difficulty = Difficulty.HARD
                }
            }else if (it.currentItem?.type == Material.RED_DYE) {
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                for (world in Bukkit.getWorlds()) {
                    world.difficulty = Difficulty.PEACEFUL
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("natural_regen"), KColors.AZURE, true)) {
                if (it.currentItem?.lore() == listOf(
                        cmp(Lang.translate("status"), KColors.GRAY) + cmp(
                            Lang.translate("enabled"),
                            KColors.LIGHTGREEN
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    for (world in Bukkit.getWorlds()) {
                        world.setGameRule(GameRule.NATURAL_REGENERATION, false)
                    }
                } else if (it.currentItem?.lore() == listOf(
                        cmp(Lang.translate("status"), KColors.GRAY) + cmp(
                            Lang.translate("disabled"),
                            KColors.ORANGERED
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    for (world in Bukkit.getWorlds()) {
                        world.setGameRule(GameRule.NATURAL_REGENERATION, true)
                    }
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("freeze_on_pause"), KColors.SKYBLUE, true)) {
                if (it.currentItem?.lore() == listOf(
                        cmp(Lang.translate("status"), KColors.GRAY) + cmp(
                            Lang.translate("enabled"),
                            KColors.LIGHTGREEN
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    Config().addBoolean(Config.Companion.Keys.SETTINGS_FREEZE_ON_PAUSE, false)
                } else if (it.currentItem?.lore() == listOf(
                        cmp(Lang.translate("status"), KColors.GRAY) + cmp(
                            Lang.translate("disabled"),
                            KColors.ORANGERED
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    Config().addBoolean(Config.Companion.Keys.SETTINGS_FREEZE_ON_PAUSE, true)
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("view_distance"), KColors.GAINSBORO, true)) {
                if (it.isLeftClick) {
                    for (world in Bukkit.getWorlds()) {
                        world.viewDistance += 1
                    }
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                } else if (it.isRightClick) {
                    for (world in Bukkit.getWorlds()) {
                        world.viewDistance -= 1
                    }
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                } else {
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("simulation_distance"), KColors.PURPLE, true)) {
                if (it.isLeftClick){
                    for (world in Bukkit.getWorlds()) {
                        world.simulationDistance += 1
                    }
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                }else if (it.isRightClick){
                    for (world in Bukkit.getWorlds()) {
                        world.simulationDistance -= 1
                    }
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                }else{
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("pvp"), KColors.TOMATO, true)) {
                if (it.currentItem?.lore() == listOf(
                        cmp(Lang.translate("status"), KColors.GRAY) + cmp(
                            Lang.translate("enabled"),
                            KColors.LIGHTGREEN
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    for (world in Bukkit.getWorlds()) {
                        world.pvp = false
                    }
                    Config().addBoolean(Config.Companion.Keys.SETTINGS_PVP, false)
                } else if (it.currentItem?.lore() == listOf(
                        cmp(Lang.translate("status"), KColors.GRAY) + cmp(
                            Lang.translate("disabled"),
                            KColors.ORANGERED
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    for (world in Bukkit.getWorlds()) {
                        world.pvp = true
                    }
                    Config().addBoolean(Config.Companion.Keys.SETTINGS_PVP, true)
                }
            }
            Config().addInt(Config.Companion.Keys.SETTINGS_VIEW_DISTANCE, Bukkit.getWorld("world")?.viewDistance ?: 10)
            Config().addInt(Config.Companion.Keys.SETTINGS_SIMULATION_DISTANCE, Bukkit.getWorld("world")?.simulationDistance ?: 10)
            SettingsGUI.open(player)
            if (it.currentItem?.type == Material.BOOK) {
                val config = Config()
                val current = config.config.getString("language") ?: "en"
                val available = listOf("de", "en", "es")
                val idx = available.indexOf(current)
                val next = available[(idx + 1) % available.size]
                config.addString("language", next)
                Lang.refreshLanguage() // Update language cache
                SettingsGUI.open(player)
                player.sendMessage(Main.prefix + cmp(Lang.translate("language_changed", next), KColors.LIGHTPURPLE))
                Bukkit.reload()
                return@listen
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("back"))){
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                MainGUI.open(player)
            }
        }else if (it.view.title == Lang.translate("battle_menu_title")){
            it.isCancelled = true
            for (battles in Battles.values()) {
                if (battles.nameComponent == it.currentItem?.itemMeta?.name) {
                    if (battles.active) {
                        battles.active = false
                        Config().addBoolean(battles.nameString, false)
                        val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    } else {
                        battles.active = true
                        Config().addBoolean(battles.nameString, true)
                        val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    }
                    BattleGUI.open(player)
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("back"))){
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                MainGUI.open(player)
            }
        }else if (it.view.title == Lang.translate("randomizer_title")){
            it.isCancelled = true
            for (randomizer in Randomizer.values()) {
                if (randomizer.nameComponent == it.currentItem?.itemMeta?.name) {
                    if (randomizer.active) {
                        randomizer.active = false
                        Config().addBoolean(randomizer.nameString, false)
                        val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    } else {
                        randomizer.active = true
                        Config().addBoolean(randomizer.nameString, true)
                        val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    }
                    RandomizerGUI.open(player)
                }
            }
            if (it.currentItem?.itemMeta?.name == cmp(Lang.translate("back"))){
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                ChallengeGUI.open(player)
            }
        }
    }
}