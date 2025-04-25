package at.mario.challenge.timer

import at.mario.challenge.challenges.Battles
import at.mario.challenge.challenges.ChallengeManager
import at.mario.challenge.challenges.Goals
import at.mario.challenge.utils.Config
import at.mario.challenge.mab.MABServerManagement
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

object Timer {
    private val miniMessage = MiniMessage.miniMessage()
    private var time = Duration.ZERO
    var hidden = true
    var win = false

    var paused = true
        set(value) {
            if (value && !win) broadcast(cmp("Der Timer wurde ") + cmp("pausiert", NamedTextColor.RED))
            else if (!win) broadcast(cmp("Der Timer wurde ") + cmp("gestartet", NamedTextColor.GREEN))
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
    private var offset = 0.0

    fun setTime(duration: Duration){
        time = duration;
    }

    private fun displayTimer() {
        val display = if (paused) miniMessage.deserialize("<gray>Timer pausiert</gray> <gray>-</gray> <red>$time</red>") else miniMessage.deserialize("<gradient:#ff1e00:#ff4400:$offset><b>$time")
        if (!hidden)
            onlinePlayers.forEach { player ->
                player.sendActionBar(display)
            }
    }

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
            Config().addInt("timer", time.toInt(DurationUnit.SECONDS))
            Config().save()
        }
    }
    fun getTime(): Duration {
        return time
    }
    init {
        schedule()
    }
}