package at.mario.challenge.timer

import at.mario.challenge.Main
import at.mario.challenge.challenges.Battles
import at.mario.challenge.challenges.ChallengeManager
import at.mario.challenge.challenges.Goals
import at.mario.challenge.utils.Config
import at.mario.challenge.mab.MABServerManagement
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.extensions.broadcast
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import de.miraculixx.kpaper.extensions.onlinePlayers
import de.miraculixx.kpaper.runnables.task
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/**
 * Timer object for managing the challenge timer, including pause, resume, display, and win logic.
 * Handles timer state, action bar updates, and integration with challenges and config.
 */
object Timer {
    /** MiniMessage instance for formatting messages. */
    private val miniMessage = MiniMessage.miniMessage()
    /** Current timer value. */
    private var time = Duration.ZERO
    /** Whether the timer display is hidden. */
    var hidden = true
    /** Whether the win condition has been reached. */
    var win = false

    /**
     * Whether the timer is paused. Setting this property triggers broadcast and event logic.
     */
    var paused = true
        set(value) {
            if (value && !win) broadcast(Main.prefix + cmp(Lang.translate("timer_paused")) + cmp(Lang.translate("timer_paused_state"), NamedTextColor.RED))
            else if (!win) broadcast(Main.prefix + cmp(Lang.translate("timer_resumed")) + cmp(Lang.translate("timer_resumed_state"), NamedTextColor.GREEN))
            else if(!value) win = false
            if (!value && Battles.MOB_ARMY_BATTLE.active) {
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    task(false, 0, 1, 1) {
                        MABServerManagement().onEventStart()
                    }
                }
            }
            field = value
        }
    /** Offset for gradient animation in timer display. */
    private var offset = 0.0

    /**
     * Sets the timer to a specific duration.
     * @param duration The duration to set
     */
    fun setTime(duration: Duration){
        time = duration
    }

    /**
     * Displays the timer in the action bar for all online players.
     */
    private fun displayTimer() {
        val display = if (paused) miniMessage.deserialize("<gray>Timer pausiert</gray> <gray>-</gray> <red>$time</red>") else miniMessage.deserialize("<gradient:#ff1e00:#ff4400:$offset><b>$time")
        if (!hidden)
            onlinePlayers.forEach { player ->
                player.sendActionBar(display)
            }
    }

    /**
     * Schedules the timer update and display tasks.
     */
    private fun schedule() {
        task (false, 0, 1){
            offset += 0.05
            if (offset > 1.0) offset -= 2
        }
        task(true, 0, 20) {
            displayTimer()
            if (Goals.TIMER.active){
                if (time.toInt(DurationUnit.SECONDS) == 0 && !win){
                    ChallengeManager().timerEnd()
                }else {
                    if (!paused) time -= 1.seconds
                }
            }else if (!Goals.TIMER.active){
                if (!paused) time += 1.seconds
            }
            ChallengeManager()
            val config = Config()
            config.setInt("timer", time.toInt(DurationUnit.SECONDS))
            config.save()
        }
    }

    /**
     * Gets the current timer value.
     * @return The current timer duration
     */
    fun getTime(): Duration {
        return time
    }

    /**
     * Initializes the timer and schedules tasks on object creation.
     */
    init {
        schedule()
    }
}