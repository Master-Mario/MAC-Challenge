package at.mario.challenge.commands

import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.commandsenders.BukkitProxiedCommandSender
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import org.bukkit.Bukkit

/**
 * Command for resetting the server/world. Kicks all players and restarts the server if confirmed by an operator.
 */
class ResetCommand {
    /**
     * Indicates if a reset is in progress.
     */
    var reset: Boolean = false
    /**
     * The reset command tree. Only available for operators.
     */
    val resetCommand = commandTree("reset"){
        literalArgument("confirm"){
            anyExecutor{ executer, _ ->
                if (!executer.isOp){
                    return@anyExecutor
                }
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    onlinePlayer.kick(cmp(Lang.translate("reset_kick_message", executer.name), KColors.RED))
                }
                reset = true
                Bukkit.spigot().restart()
            }
        }
    }
}