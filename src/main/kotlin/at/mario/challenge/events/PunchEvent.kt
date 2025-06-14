package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.kill
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * Handles punch (entity damage by entity) events, including sequence challenge logic and sound feedback.
 */
object PunchEvent {
    /**
     * Listens for entity damage by entity events and applies sequence challenge logic.
     */
    val onPunch = listen<EntityDamageByEntityEvent> {
        if (!it.entity.type.isSpawnable){
            return@listen
        }
        if (it.damageSource.causingEntity is Player){
            if (Challenges.SEQUENCE.active){
                val player = it.damageSource.causingEntity as Player
                if (Config().config.getString("sequence._${Config().config.getInt("sequence.next")}_") != player.name){
                    it.isCancelled = true
                    player.sendMessage(Main.prefix + cmp("Du bist nicht der Nächste!", de.miraculixx.kpaper.chat.KColors.RED))
                    player.health = 0.0
                    Bukkit.getOnlinePlayers().forEach { player ->
                        val sound = Sound.sound(Key.key("block.anvil.land"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    }
                }else{
                    Bukkit.getOnlinePlayers().forEach { player ->
                        val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                    }
                    if (Bukkit.getOnlinePlayers().size <= Config().config.getInt("sequence.next")){
                        Config().addInt("sequence.next", 1)
                    }else{
                        Config().addInt("sequence.next", Config().config.getInt("sequence.next") + 1)
                    }
                }
            }
        }
    }
}