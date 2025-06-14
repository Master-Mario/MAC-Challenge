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
                        var everyMaterial: MutableList<String> = mutableListOf()
                        for (material in Material.values()) {
                            if (material.isItem) {
                                everyMaterial = everyMaterial.plus(material.name) as MutableList<String>
                            }
                        }
                        for (material in Material.values()) {
                            for (player in Bukkit.getOnlinePlayers()) {
                                Bukkit.broadcast(Main.prefix + cmp("Randomizing ${material.name} für ${player.name}"))
                                Config().addRandomizer(
                                    "randomizer.${player.name}.${material.name}",
                                    everyMaterial.random()
                                )
                            }
                        }
                    }
                }
                /*literalArgument("block-to-items"){
                    anyExecutor{ _, _ ->
                        var everyItem : MutableList<String> = mutableListOf()
                        for (material in Material.values()) {
                            if (material.isItem) {
                                everyItem = everyItem.plus(material.name) as MutableList<String>
                            }
                        }

                        for (material in Material.values()) {
                            if (material.isBlock) {
                                for (player in Bukkit.getOnlinePlayers()) {
                                    Bukkit.broadcast(Main.prefix + cmp("Randomizing ${material.name} for ${player.name}"))
                                    Config().addRandomizer(
                                        "randomizer.${player.name}.${material.name}",
                                        everyItem.random()
                                    )
                                }
                            }
                        }
                    }
                }
                literalArgument("chest"){
                    anyExecutor{ _, _ ->
                        var everyItem : MutableList<String> = mutableListOf()
                        for (material in Material.values()) {
                            if (material.isItem){
                                everyItem = everyItem.plus(material.name) as MutableList<String>
                            }
                        }

                        for (material in Material.values()) {
                            if (material.isItem) {
                                for (player in Bukkit.getOnlinePlayers()) {
                                    Bukkit.broadcast(Main.prefix + cmp("Randomizing ${material.name} for ${player.name}"))
                                    Config().addRandomizer("randomizer.${player.name}.${material.name}", everyItem.random())
                                }
                            }
                        }
                    }
                }*/
            }
            literalArgument("everyone") {
                literalArgument("items-to-items") {
                    anyExecutor { _, _ ->
                        var everyMaterial: MutableList<String> = mutableListOf()
                        for (material in Material.values()) {
                            if (material.isItem) {
                                everyMaterial = everyMaterial.plus(material.name) as MutableList<String>
                            }
                        }
                        for (material in Material.values()) {
                            Bukkit.broadcast(Main.prefix + cmp("Randomizing ${material.name} für alle Spieler"))
                            Config().addRandomizer(
                                "randomizer.everyone.${material.name}",
                                everyMaterial.random()
                            )
                        }
                    }
                }
                literalArgument("block-to-items"){
                    anyExecutor{ _, _ ->
                        var everyItem : MutableList<String> = mutableListOf()
                        for (material in Material.values()) {
                            if (material.isItem) {
                                everyItem = everyItem.plus(material.name) as MutableList<String>
                            }
                        }
                        for (material in Material.values()) {
                            if (material.isBlock) {
                                Bukkit.broadcast(Main.prefix + cmp("Randomizing ${material.name} für alle Spieler"))
                                Config().addRandomizer(
                                    "randomizer.everyone.${material.name}",
                                    everyItem.random()
                                )
                            }
                        }
                    }
                }
                literalArgument("entity-to-items"){
                    anyExecutor{ _, _ ->
                        var everyItem : MutableList<String> = mutableListOf()
                        for (material in Material.values()) {
                            if (material.isItem) {
                                everyItem = everyItem.plus(material.name) as MutableList<String>
                            }
                        }
                        for (entity in EntityType.values()) {
                            Bukkit.broadcast(Main.prefix + cmp("Randomizing ${entity.name} für alle Spieler"))
                            Config().addRandomizer(
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