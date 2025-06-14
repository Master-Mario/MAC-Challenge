package at.mario.challenge.commands

import at.mario.challenge.Main
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import dev.jorel.commandapi.kotlindsl.booleanArgument
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.entityTypeArgument
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.nbtCompoundArgument
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import net.kyori.adventure.text.NBTComponent
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

/**
 * Provides utility commands for server management, player actions, and entity control.
 * Includes commands for spawn, heal, fly, invulnerability, teleportation, inventory viewing, and entity summoning.
 */
class UtilsCommand {
    /**
     * Teleports the player to the world spawn location.
     * Only available for operators.
     */
    val spawnCommand = commandTree("spawn") {
        playerExecutor { player, _ ->
            if (player.isOp) {
                player.teleport(player.world.spawnLocation)
                player.sendMessage(Main.prefix + cmp(Lang.translate("teleport_spawn_success"), KColors.GREEN))
            }else{
                player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
            }
        }
    }
    /**
     * Heals the player to full health, food, and saturation.
     * Only available for operators.
     */
    val healCommand = commandTree("heal"){
        playerExecutor{ player, _ ->
            if (player.isOp) {
                player.health = player.maxHealth
                player.foodLevel = 20
                player.saturation = 20f
                player.fireTicks = 0
                player.sendMessage(Main.prefix + cmp(Lang.translate("heal_success"), KColors.GREEN))
            }else{
                player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
            }
        }
    }
    /**
     * Toggles flight for the player.
     * Only available for operators.
     */
    val flyCommand = commandTree("fly"){
        playerExecutor{ player, _ ->
            if (player.isOp) {
                player.allowFlight = !player.allowFlight
                if (player.allowFlight) {
                    player.sendMessage(Main.prefix + cmp("Du kannst jetzt fliegen!", KColors.GREEN))
                }else{
                    player.sendMessage(Main.prefix + cmp("Du kannst jetzt nicht mehr fliegen!", KColors.RED))
                }
            }else{
                player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
            }
        }
    }
    /**
     * Toggles invulnerability for the player.
     * Only available for operators.
     */
    val invulnerableCommand = commandTree("invulnerable"){
        playerExecutor{ player, _ ->
            if (player.isOp) {
                player.isInvulnerable = !player.isInvulnerable
                if (player.isInvulnerable) {
                    player.sendMessage(Main.prefix + cmp("Du bist jetzt unverwundbar!", KColors.GREEN))
                }else{
                    player.sendMessage(Main.prefix + cmp("Du bist jetzt verwundbar!", KColors.RED))
                }
            }else{
                player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
            }
        }
    }
    /**
     * Teleports the player to another player or entity of a given type.
     * Only available for operators.
     */
    val findCommand = commandTree("find"){
        playerArgument("player") {
            playerExecutor { player, args ->
                val targetPlayer = args["player"] as Player
                if (player.isOp) {
                    player.teleport(targetPlayer.location)
                    player.sendMessage(Main.prefix + cmp("Du wurdest zu ${targetPlayer.name} teleportiert!", KColors.GREEN))
                }else{
                    player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
                }
            }
        }
        literalArgument("entity") {
            entityTypeArgument("entityType") {
                playerExecutor { player, args ->
                    val targetType = args["entityType"] as EntityType
                    var target: Entity? = null
                    if (player.isOp) {
                        for (entity in player.location.world.entities) {
                            if (entity.type == targetType) {
                                target = entity
                            }
                        }
                        if (target == null) {
                            player.sendMessage(
                                Main.prefix + cmp(
                                    "Es gibt kein ${targetType.name} in deiner Nähe!",
                                    KColors.RED
                                )
                            )
                            return@playerExecutor
                        }
                        player.teleport(target.location)
                        player.sendMessage(
                            Main.prefix + cmp(
                                "Du wurdest zu ${targetType.name} teleportiert!",
                                KColors.GREEN
                            )
                        )
                    } else {
                        player.sendMessage(
                            Main.prefix + cmp(
                                "Du hast keine Berechtigung um diesen Befehl zu benutzen!",
                                KColors.RED
                            )
                        )
                    }
                }
            }
        }
    }
    /**
     * Opens the inventory of another player for viewing.
     * Only available for operators.
     */
    val invseeCommand = commandTree("invsee") {
        playerArgument("player") {
            playerExecutor { player, args ->
                val targetPlayer = args["player"] as Player
                if (player.isOp) {
                    player.openInventory(targetPlayer.inventory)
                    player.sendMessage(Main.prefix + cmp("Du siehst jetzt das Inventar von ${targetPlayer.name}!", KColors.GREEN))
                }else{
                    player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
                }
            }
        }
    }
    /**
     * Teleports another player or entity to the command sender.
     * Only available for operators.
     */
    val pickupCommand = commandTree("pickup"){
        playerArgument("player") {
            playerExecutor { player, args ->
                val targetPlayer = args["player"] as Player
                if (player.isOp) {
                    targetPlayer.teleport(player.location)
                    player.sendMessage(Main.prefix + cmp("${targetPlayer.name} wurde zu dir teleportiert!", KColors.GREEN))
                }else{
                    player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
                }
            }
        }
        literalArgument("entity"){
            entityTypeArgument("entityType") {
                playerExecutor { player, args ->
                    val targetType = args["entityType"] as EntityType
                    var target: Entity? = null
                    if (player.isOp) {
                        for (entity in player.location.world.entities) {
                            if (entity.type == targetType) {
                                target = entity
                            }
                        }
                        if (target == null) {
                            player.sendMessage(Main.prefix + cmp("Es gibt kein ${targetType.name} in deiner Nähe!", KColors.RED))
                            return@playerExecutor
                        }
                        target.teleport(player.location)
                        player.sendMessage(Main.prefix + cmp("${targetType.name} wurde zu dir teleportiert!", KColors.GREEN))
                    }else{
                        player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
                    }
                }
            }
        }
    }
    /**
     * Summons a given amount of entities of a specific type at the player's location.
     * Only available for operators.
     */
    val summonAmountCommand = commandTree("summonAmount"){
        entityTypeArgument("type"){
            integerArgument("amount"){
                playerExecutor { player, args ->
                    val type = args["type"] as EntityType
                    val amount = args["amount"] as Int
                    if (player.isOp) {
                        for (i in 1..amount) {
                            player.world.spawnEntity(player.location, type)
                        }
                        player.sendMessage(Main.prefix + cmp("Du hast $amount ${type}s gespawnt!", KColors.GREEN))
                    }else{
                        player.sendMessage(Main.prefix + cmp(Lang.translate("no_permission"), KColors.RED))
                    }
                }
            }
        }
    }
}