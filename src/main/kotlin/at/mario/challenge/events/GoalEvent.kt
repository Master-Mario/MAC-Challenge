package at.mario.challenge.events

import at.mario.challenge.challenges.ChallengeManager
import at.mario.challenge.challenges.Goals
import de.miraculixx.kpaper.event.listen
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent

object GoalEvent {
    var onGoal = listen<EntityDeathEvent> {
        val entityType = it.entity.type
        when (entityType) {
            EntityType.ENDER_DRAGON -> if (Goals.ENDER_DRAGON.active) ChallengeManager().win("Ender-Drache getötet")
            EntityType.ELDER_GUARDIAN -> if (Goals.ELDER_GUARDIAN.active) ChallengeManager().win("Elder-Guardian getötet")
            EntityType.WITHER -> if (Goals.WITHER.active) ChallengeManager().win("Wither getötet")
            else -> {}
        }
    }
}
