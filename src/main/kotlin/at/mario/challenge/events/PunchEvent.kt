package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Battles
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.challenges.TrySystem
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.kill
import de.miraculixx.kpaper.extensions.bukkit.plus
import kotlinx.coroutines.channels.BroadcastChannel
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

object PunchEvent {
    val onPunch = listen<EntityDamageByEntityEvent> {
        if (!it.entity.type.isSpawnable){
            return@listen
        }
        if (it.damageSource.causingEntity is Player){
            if (Challenges.SEQUENCE.active){
                val player = it.damageSource.causingEntity as Player
                if (Config().config.getString("sequence._${Config().config.getInt("sequence.next")}_") != player.name){
                    it.isCancelled = true
                    player.sendMessage(Main.prefix + cmp("Du bist nicht der Nächste!", KColors.RED))
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