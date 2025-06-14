package at.mario.challenge.commands

import at.mario.challenge.timer.Timer
import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.textArgument
import net.kyori.adventure.text.format.NamedTextColor
import kotlin.time.Duration

/**
 * Command for controlling the challenge timer. Allows pausing, resuming, hiding, resetting, and setting the timer.
 */
class TimerCommand {
    /**
     * The timer command tree for timer control.
     */
    val timerCommand = commandTree("timer"){
        literalArgument("pause"){
            anyExecutor { _, _ ->  Timer.paused = true}
        }
        literalArgument("stop"){
            anyExecutor { _, _ ->  Timer.paused = true}
        }
        literalArgument("resume"){
            anyExecutor { _, _ ->  Timer.paused = false}
        }
        literalArgument("start"){
            anyExecutor { _, _ ->
                Timer.paused = false
                Timer.hidden = false
            }
        }
        literalArgument("hide"){
            anyExecutor { _, _ ->  Timer.hidden = true}
        }
        literalArgument("show"){
            anyExecutor { _, _ ->  Timer.hidden = false}
        }
        literalArgument("reset"){
            anyExecutor { _, _ -> Timer.setTime(Duration.ZERO) }
        }
        literalArgument("set"){
            textArgument("time"){
                anyExecutor { sender, args ->
                    val timeString = args[0] as String
                    val time = try {
                        Duration.parse(timeString)
                    }catch (_: IllegalArgumentException){
                        sender.sendMessage(cmp("Du hast keine gültige Zeit angegeben!", NamedTextColor.RED))
                        return@anyExecutor
                    }
                    Timer.setTime(time)
                }
            }
        }
    }
}