package at.mario.challenge.events

import at.mario.challenge.timer.Timer
import de.miraculixx.kpaper.event.listen
import org.bukkit.event.block.BlockPlaceEvent

/**
 * Handles block break and explosion events. Applies challenge and randomizer logic to block drops.
 */
object BlockPlaceEvent {

    /**
     * Handels block place events, cancelling them if the timer is paused.
     */
    var onPlace = listen<BlockPlaceEvent> {
        if (Timer.paused){
            it.isCancelled = true
            return@listen
        }
    }

}