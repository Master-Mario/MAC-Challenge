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
                    for (player in Bukkit.getOnlinePlayers()) {
                        Config().addInt("sequenz.next", 0)
                        player.sendMessage(prefix + cmp(Lang.translate("reset_next_player")))
                    }
                }
            }
            literalArgument("run-blocks"){
                anyExecutor{ _, _ ->
                    for (player in Bukkit.getOnlinePlayers()) {
                        Config().add("run-randomizer.run-blocks-amount.${player.name}", 0.0)
                        player.sendMessage(prefix + cmp(Lang.translate("reset_run_block")))
                    }
                }
            }
        }
        literalArgument("activate"){
            greedyStringArgument("challenges") {
                replaceSuggestions(ArgumentSuggestions.stringCollection{ ChallengeManager().list})
                anyExecutor { _, challengeName ->
                    for (challenge in Challenges.values()) {
                        if (challengeName[0] == challenge.nameString)
                            if (!challenge.active) {
                                challenge.active = true
                                Config().addBoolean(challenge.nameString, true)
                                Config().save()
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
                    for (challenge in Challenges.values()) {
                        if (challengeName[0] == challenge.nameString)
                            if (challenge.active) {
                                challenge.active = false
                                Config().addBoolean(challenge.nameString, false)
                                Config().save()
                                Bukkit.broadcast(prefix + cmp(Lang.translate("challenge_deactivated", challenge.nameString)))
                            } else
                                Bukkit.broadcast(prefix + cmp(Lang.translate("challenge_already_deactivated", challenge.nameString)))
                    }
                }
            }
        }
    }
}