package at.mario.challenge.events

import at.mario.challenge.challenges.ChallengeManager
import at.mario.challenge.challenges.Goals
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.event.listen
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent

/**
 * Handles goal-related events. Triggers win conditions when specific boss mobs are killed and the corresponding goal is active.
 */
object GoalEvent {
    /**
     * Listens for entity death events and checks if a goal boss was killed.
     */
    var onGoal = listen<EntityDeathEvent> {
        val entityType = it.entity.type
        when (entityType) {
            EntityType.ENDER_DRAGON -> if (Goals.ENDER_DRAGON.active) ChallengeManager().win(Lang.translate("goal_enderdragon_killed"))
            EntityType.ELDER_GUARDIAN -> if (Goals.ELDER_GUARDIAN.active) ChallengeManager().win(Lang.translate("goal_elderguardian_killed"))
            EntityType.WITHER -> if (Goals.WITHER.active) ChallengeManager().win(Lang.translate("goal_wither_killed"))
            else -> {}
        }
    }
}
