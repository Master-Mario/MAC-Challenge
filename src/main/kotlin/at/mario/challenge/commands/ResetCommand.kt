package at.mario.challenge.commands

import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.commandsenders.BukkitProxiedCommandSender
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.Bukkit

class ResetCommand {
    var reset: Boolean = false
    val resetCommand = commandTree("reset"){
        literalArgument("confirm"){
            anyExecutor{ executer, _ ->
                if (!executer.isOp){
                    return@anyExecutor
                }
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    onlinePlayer.kick(cmp("${executer.name} hat die Welt zurückgesetzt", KColors.RED))
                }
                reset = true
                Bukkit.spigot().restart()
            }
        }
    }
}