package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Battles
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.challenges.TrySystem
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

/**
 * Handles player and entity death events, including challenge logic for no respawn, team death, entity drops, and randomizer drops.
 */
object DeathEvent {
    /**
     * Handles player death events, including no respawn and team death logic.
     */
    val onDeath = listen<PlayerDeathEvent> {
        val player = it.entity
        var someoneDead = false
        for (online in Bukkit.getServer().onlinePlayers) {
            if (Challenges.NO_RESPAWN.active) {
                for (deathchecker in Bukkit.getServer().onlinePlayers) {
                    if (deathchecker.uniqueId != online.uniqueId) {
                        if (deathchecker.isDead) {
                            someoneDead = true
                        }
                    }
                }
                it.isCancelled = true
                player.gameMode = GameMode.SPECTATOR
                if (Challenges.TEAM_DEATH.active && someoneDead) {
                    // Team death logic can be added here
                } else {
                    Timer.paused = true
                    Bukkit.broadcast(
                        MiniMessage.miniMessage().deserialize(
                            Lang.translate("death_broadcast",
                                it.player.name,
                                Timer.getTime(),
                                TrySystem().attempts
                            )
                        )
                    )
                    var message: TextComponent = Component.text("/reset confirm").color(TextColor.color(KColors.RED))
                    message = message.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/reset confirm"))
                    message = message.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, cmp(Lang.translate("reset_hover"))))
                    for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                        onlinePlayer.sendMessage(message)
                    }
                }
            }
            if (Challenges.TEAM_DEATH.active) {
                if (online.uniqueId != player.uniqueId) {
                    if (online.gameMode != GameMode.SPECTATOR) {
                        if (online.health != 0.0) {
                            online.health = 0.0
                        }
                    }
                }
            }
            someoneDead = false
        }
    }
    /**
     * Handles entity death events, including no entity drops, battle kill tracking, and randomizer drops.
     */
    val onEntityDeath = listen<EntityDeathEvent>{
        if (Challenges.NO_ENTITY_DROPS.active){
            it.drops.clear()
            return@listen
        }

        if (Battles.MOB_ARMY_BATTLE.active) {
            if (it.entity.killer is Player && it.entity !is Player) {
                val player: Player = it.entity.killer!!

                val team: String = if (Main.mabTeam1.contains(player)){
                    "Team 1"
                }else if (Main.mabTeam2.contains(player)){
                    "Team 2"
                }else{
                    return@listen
                }

                Config().addIntKill(
                    team + "." + it.entity.name,
                    Config().killConfig.getInt(team + "." + it.entity.name) + 1
                )
            }
        }

        if (Randomizer.ENTITY_RANDOMIZER.active){
            if (Randomizer.PER_PLAYER.active) {
                val killerName = it.damageSource.causingEntity?.name
                val config = Config().randomizerConfig

                val originalDrops = it.drops.toList() // Make a copy
                it.drops.clear() // Clear original drops, so we can re-add

                for (item in originalDrops) {
                    var path = "randomizer.$killerName.${item.type.name}"
                    val newTypeStr = config.getString(path)

                    if (newTypeStr == null) {
                        Bukkit.broadcast(Main.prefix + cmp(Lang.translate("entity_no_drop_defined", item.type.name)))
                        return@listen
                    }

                    val newMaterial = Material.getMaterial(newTypeStr)
                    if (newMaterial == null) {
                        Bukkit.broadcast(Main.prefix + cmp(Lang.translate("invalid_material_config", newTypeStr)))
                        return@listen
                    }

                    it.drops.add(ItemStack(newMaterial, item.amount))
                }
            }else{
                val config = Config().randomizerConfig
                it.drops.clear()

                var path = "randomizer.everyone.${it.entity.type.name}"
                val newTypeStr = config.getString(path)

                if (newTypeStr == null) {
                    Bukkit.broadcast(Main.prefix + cmp(Lang.translate("entity_no_drop_defined", it.entity.type.name)))
                    return@listen
                }

                val newMaterial = Material.getMaterial(newTypeStr)
                if (newMaterial == null) {
                    Bukkit.broadcast(Main.prefix + cmp(Lang.translate("invalid_material_config", newTypeStr)))
                    return@listen
                }

                it.drops.add(ItemStack(newMaterial))
            }
        }
    }
}