package at.mario.challenge.commands

import at.mario.challenge.Main
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.stringArgument
import org.bukkit.Bukkit

class MABCommand {
    val mab_command = commandTree("mobarmybattle") {
        literalArgument("team1"){
            stringArgument("Player"){
                replaceSuggestions(ArgumentSuggestions.stringCollection{ Main.onlinePlayersCollection})
                anyExecutor{_, args ->
                    val player = Bukkit.getPlayer(args[0] as String)
                    Main.mabTeam2.remove(player!!)
                    if (!Main.mabTeam1.contains(player)) Main.mabTeam1.add(player)
                    Bukkit.broadcast(cmp(at.mario.challenge.utils.Lang.translate("joined_team1", player.name), KColors.GREEN))
                    Config().addString(player.name + ".team", "Team1")
                }
            }
        }
        literalArgument("team2"){
            stringArgument("Player"){
                replaceSuggestions(ArgumentSuggestions.stringCollection{ Main.onlinePlayersCollection})
                anyExecutor{_, args ->
                    val player = Bukkit.getPlayer(args[0] as String)
                    Main.mabTeam1.remove(player!!)
                    if (!Main.mabTeam2.contains(player)) Main.mabTeam2.add(player)
                    Bukkit.broadcast(cmp(at.mario.challenge.utils.Lang.translate("joined_team2", player.name), KColors.GREEN))
                    Config().addString(player.name + ".team", "Team2")
                }
            }
        }
    }
}