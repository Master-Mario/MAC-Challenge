package at.mario.challenge.commands

import at.mario.challenge.Main
import at.mario.challenge.guis.ChallengeGUI
import at.mario.challenge.challenges.ChallengeManager
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.guis.MainGUI
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit

/**
 * Command for managing and controlling challenges. Allows opening GUIs, resetting, activating, and disabling challenges.
 */
class ChallengeCommand {
    val prefix = Main.prefix
    /**
     * The challenge command tree for challenge control.
     */
    val challengeCommand = commandTree("challenge"){
        playerExecutor {player, _ ->
            MainGUI.open(player)
            val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
            player.playSound(sound)
        }
        literalArgument("gui"){
            playerExecutor {player, _ -> ChallengeGUI.open(player) }
        }
        literalArgument("reset"){
            literalArgument("next-player"){
                anyExecutor { _, _ ->
                    val config = Config()
                    config.setInt(Config.Keys.SEQUENCE_NEXT, 0)
                    config.save()
                    val message = prefix + cmp(Lang.translate("reset_next_player"))
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.sendMessage(message)
                    }
                }
            }
            literalArgument("run-blocks"){
                anyExecutor{ _, _ ->
                    val config = Config()
                    val message = prefix + cmp(Lang.translate("reset_run_block"))
                    for (player in Bukkit.getOnlinePlayers()) {
                        config.set("${Config.Keys.RUN_RANDOMIZER_RUN_BLOCKS_AMOUNT}.${player.name}", 0.0)
                        player.sendMessage(message)
                    }
                    config.save()
                }
            }
        }
        literalArgument("activate"){
            greedyStringArgument("challenges") {
                replaceSuggestions(ArgumentSuggestions.stringCollection{ ChallengeManager().list})
                anyExecutor { _, challengeName ->
                    val config = Config()
                    for (challenge in Challenges.values()) {
                        if (challengeName[0] == challenge.nameString)
                            if (!challenge.active) {
                                challenge.active = true
                                config.setBoolean(challenge.nameString, true)
                                config.save()
                                Bukkit.broadcast(prefix + cmp(Lang.translate("challenge_activated", challenge.nameString)))
                            } else
                                Bukkit.broadcast(prefix + cmp(Lang.translate("challenge_already_activated", challenge.nameString)))
                    }
                }
            }
        }
        literalArgument("disable"){
            greedyStringArgument("challenges") {
                replaceSuggestions(ArgumentSuggestions.stringCollection{ ChallengeManager().list})
                anyExecutor { _, challengeName ->
                    val config = Config()
                    for (challenge in Challenges.values()) {
                        if (challengeName[0] == challenge.nameString)
                            if (challenge.active) {
                                challenge.active = false
                                config.setBoolean(challenge.nameString, false)
                                config.save()
                                Bukkit.broadcast(prefix + cmp(Lang.translate("challenge_deactivated", challenge.nameString)))
                            } else
                                Bukkit.broadcast(prefix + cmp(Lang.translate("challenge_already_deactivated", challenge.nameString)))
                    }
                }
            }
        }
    }
}