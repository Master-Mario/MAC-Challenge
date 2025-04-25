package at.mario.challenge.commands

import at.mario.challenge.Main
import at.mario.challenge.guis.ChallengeGUI
import at.mario.challenge.challenges.ChallengeManager
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.guis.MainGUI
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit

class ChallengeCommand {
    val prefix = Main.prefix
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
                        player.sendMessage(prefix + cmp("Der nächste Spieler wurde zurückgesetzt"))
                    }
                }
            }
            literalArgument("run-blocks"){
                anyExecutor { _, _ ->
                    for (player in Bukkit.getOnlinePlayers()) {
                        Config().add("run-randomizer.run-blocks-amount.${player.name}", 0.0)
                        player.sendMessage(prefix + cmp("Dein Run-Block wurde zurückgesetzt"))
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
                                Bukkit.broadcast(prefix + cmp(("Die \"" + challenge.nameString + "\" Challenge wurde aktiviert")))
                            } else
                                Bukkit.broadcast(prefix + cmp(("Die \"" + challenge.nameString + "\" Challenge ist bereits aktiviert")))
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
                                Bukkit.broadcast(prefix + cmp(("Die \"" + challenge.nameString + "\" Challenge wurde deaktiviert")))
                            } else
                                Bukkit.broadcast(prefix + cmp(("Die \"" + challenge.nameString + "\" Challenge ist bereits deaktiviert")))
                    }
                }
            }
        }
    }
}