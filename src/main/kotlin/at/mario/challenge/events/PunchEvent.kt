package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
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
        if (Timer.paused) {
            it.isCancelled = true
            return@listen
        }
        if (!it.entity.type.isSpawnable){
            return@listen
        }
        if (it.damageSource.causingEntity is Player){
            if (Challenges.SEQUENCE.active){
                val player = it.damageSource.causingEntity as Player
                if (Config().config.getString("sequence._${Config().config.getInt(Config.Companion.Keys.SEQUENCE_NEXT)}_") != player.name){
                    it.isCancelled = true
                    player.sendMessage(Main.prefix + cmp(Lang.translate("not_next_sequence"), de.miraculixx.kpaper.chat.KColors.RED))
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
                    if (Bukkit.getOnlinePlayers().size <= Config().config.getInt(Config.Companion.Keys.SEQUENCE_NEXT)){
                        Config().addInt(Config.Companion.Keys.SEQUENCE_NEXT, 1)
                    }else{
                        Config().addInt(Config.Companion.Keys.SEQUENCE_NEXT, Config().config.getInt(Config.Companion.Keys.SEQUENCE_NEXT) + 1)
                    }
                }
            }
        }
    }
}