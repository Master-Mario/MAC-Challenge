package at.mario.challenge.commands

import at.mario.challenge.Main
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import de.miraculixx.kpaper.runnables.task
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType

/**
 * Command for generating and shuffling randomizer mappings for items, blocks, and entities.
 * Supports per-player and global randomization for items, blocks, and entities.
 */
class RandomizerCommand {
    /**
     * The randomizer command tree for generating randomizer mappings.
     */
    val mab_command = commandTree("randomizer") {
        literalArgument("shuffle"){
            literalArgument("per-player") {
                literalArgument("items-to-items") {
                    anyExecutor { _, _ ->
                        val config = Config()
                        
                        // Build list of all item materials efficiently
                        val everyMaterial = Material.values()
                            .filter { it.isItem }
                            .map { it.name }
                            .toMutableList()
                        
                        val materials = Material.values().filter { it.isItem }
                        val players = Bukkit.getOnlinePlayers()
                        
                        for (material in materials) {
                            for (player in players) {
                                Bukkit.broadcast(Main.prefix + cmp(at.mario.challenge.utils.Lang.translate("randomizing_for_player", material.name, player.name)))
                                config.addRandomizer(
                                    "randomizer.${player.name}.${material.name}",
                                    everyMaterial.random()
                                )
                            }
                        }
                    }
                }
            }
            literalArgument("everyone") {
                literalArgument("items-to-items") {
                    anyExecutor { _, _ ->
                        val config = Config()
                        
                        // Build list of all item materials efficiently  
                        val everyMaterial = Material.values()
                            .filter { it.isItem }
                            .map { it.name }
                            .toMutableList()
                        
                        val materials = Material.values().filter { it.isItem }
                        
                        for (material in materials) {
                            Bukkit.broadcast(Main.prefix + cmp(at.mario.challenge.utils.Lang.translate("randomizing_for_everyone", material.name)))
                            config.addRandomizer(
                                "randomizer.everyone.${material.name}",
                                everyMaterial.random()
                            )
                        }
                    }
                }
                literalArgument("block-to-items"){
                    anyExecutor{ _, _ ->
                        val config = Config()
                        
                        // Build list of all item materials efficiently
                        val everyItem = Material.values()
                            .filter { it.isItem }
                            .map { it.name }
                            .toMutableList()
                        
                        val blockMaterials = Material.values().filter { it.isBlock }
                        
                        for (material in blockMaterials) {
                            Bukkit.broadcast(Main.prefix + cmp(at.mario.challenge.utils.Lang.translate("randomizing_for_everyone", material.name)))
                            config.addRandomizer(
                                "randomizer.everyone.${material.name}",
                                everyItem.random()
                            )
                        }
                    }
                }
                literalArgument("entity-to-items"){
                    anyExecutor{ _, _ ->
                        val config = Config()
                        
                        // Build list of all item materials efficiently
                        val everyItem = Material.values()
                            .filter { it.isItem }
                            .map { it.name }
                            .toMutableList()
                        
                        for (entity in EntityType.values()) {
                            Bukkit.broadcast(Main.prefix + cmp(at.mario.challenge.utils.Lang.translate("randomizing_for_everyone", entity.name)))
                            config.addRandomizer(
                                "randomizer.everyone.${entity.name}",
                                everyItem.random()
                            )
                        }
                    }
                }
            }
        }
    }
}