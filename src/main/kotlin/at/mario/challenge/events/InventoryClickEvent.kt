package at.mario.challenge.events

import at.mario.challenge.challenges.Battles
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.challenges.Goals
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.guis.*
import at.mario.challenge.guis.ChallengeGUI.open
import at.mario.challenge.guis.ChallengeGUI.playerPages
import at.mario.challenge.utils.Config
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
        if (title.contains("Hauptmenü") || title.contains("Wähle eine Challenge") || title.contains("Wähle ein Ziel") || title.contains("Wähle einen Battle-Modus") || title.contains("Wähle einen Randomizer") || title.contains("Setze die Einstellungen") || title.contains("Mob-Army-Waves") || title.contains("Wave")) {
            it.isCancelled = true
        }
        // Navigation logic for GUIs
        if (title.contains("Hauptmenü")) {
            when (it.currentItem?.itemMeta?.displayName()?.plainText()) {
                "§d§lZiele" -> GoalGUI.open(it.whoClicked)
                "§b§lChallenges" -> ChallengeGUI.open(it.whoClicked)
                "§a§lBattles" -> BattleGUI.open(it.whoClicked)
                "§c§lSettings" -> SettingsGUI.open(it.whoClicked)
            }
        } else if (title.contains("Wähle eine Challenge")) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains("Wähle ein Ziel")) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains("Wähle einen Battle-Modus")) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains("Wähle einen Randomizer")) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        } else if (title.contains("Setze die Einstellungen")) {
            if (it.currentItem?.type?.name == "DARK_OAK_DOOR") MainGUI.open(it.whoClicked)
        }
        if (it.view.title == "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Wähle eine Challenge") {
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
            if (it.currentItem?.itemMeta?.name == cmp("§7< Vorherige Seite")) {
                val currentPage = playerPages[player.name] ?: 0
                open(player, currentPage - 1)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            } else if (it.currentItem?.itemMeta?.name == cmp("§7Nächste Seite >")) {
                val currentPage = playerPages[player.name] ?: 0
                open(player, currentPage + 1)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (it.currentItem?.itemMeta?.name == cmp("Zurück")){
                MainGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }

        }else if(it.view.title == "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Hauptmenü") {
            it.isCancelled = true
            if (cmp("${ChatColor.BOLD}Challenges", KColors.LIGHTBLUE)== it.currentItem?.itemMeta?.name) {
                ChallengeGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (cmp("${ChatColor.BOLD}Ziele", KColors.LIGHTPURPLE)== it.currentItem?.itemMeta?.name) {
                GoalGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (cmp("${ChatColor.BOLD}Battles", KColors.LIGHTGREEN)== it.currentItem?.itemMeta?.name) {
                BattleGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
            if (cmp("${ChatColor.BOLD}Settings", KColors.LIGHTCORAL)== it.currentItem?.itemMeta?.name) {
                SettingsGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)

            }
        }else if (it.view.title == "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Wähle ein Ziel"){
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
            if (it.currentItem?.itemMeta?.name == cmp("Zurück")){
                MainGUI.open(player)
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
            }
        }else if (it.view.title == "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Setze die Einstellungen"){
            it.isCancelled = true
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
            if (it.currentItem?.itemMeta?.name == cmp("Natürliche Regeneration", KColors.AZURE, true)) {
                if (it.currentItem?.lore() == listOf(
                        cmp("Status: ", KColors.GRAY) + cmp(
                            "Aktiviert",
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
                        cmp("Status: ", KColors.GRAY) + cmp(
                            "Deaktiviert",
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
            if (it.currentItem?.itemMeta?.name == cmp("Sichtweite", KColors.GAINSBORO, true)) {
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
            if (it.currentItem?.itemMeta?.name == cmp("Simulationsdistanz", KColors.PURPLE, true)) {
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
            if (it.currentItem?.itemMeta?.name == cmp("PVP", KColors.TOMATO, true)) {
                if (it.currentItem?.lore() == listOf(
                        cmp("Status: ", KColors.GRAY) + cmp(
                            "Aktiviert",
                            KColors.LIGHTGREEN
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    for (world in Bukkit.getWorlds()) {
                        world.pvp = false
                    }
                    Config().addBoolean("settings.pvp", false)
                } else if (it.currentItem?.lore() == listOf(
                        cmp("Status: ", KColors.GRAY) + cmp(
                            "Deaktiviert",
                            KColors.ORANGERED
                        )
                    )
                ) {
                    val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                    player.playSound(sound)
                    for (world in Bukkit.getWorlds()) {
                        world.pvp = true
                    }
                    Config().addBoolean("settings.pvp", true)
                }
            }
            Config().addInt("settings.view-distance", Bukkit.getWorld("world")?.viewDistance ?: 10)
            Config().addInt("settings.simulation-distance", Bukkit.getWorld("world")?.simulationDistance ?: 10)
            SettingsGUI.open(player)
            if (it.currentItem?.itemMeta?.name == cmp("Zurück")){
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                MainGUI.open(player)
            }
        }else if (it.view.title == "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Wähle einen Battle-Modus"){
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
            if (it.currentItem?.itemMeta?.name == cmp("Zurück")){
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                MainGUI.open(player)
            }
        }else if (it.view.title == "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Wähle einen Randomizer"){
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
            if (it.currentItem?.itemMeta?.name == cmp("Zurück")){
                val sound = Sound.sound(Key.key("block.dispenser.dispense"), Sound.Source.MASTER, 0.5f, 1f)
                player.playSound(sound)
                ChallengeGUI.open(player)
            }
        }
    }
}