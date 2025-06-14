package at.mario.challenge.challenges

import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.onlinePlayers
import de.miraculixx.kpaper.runnables.task
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.TitlePart
import org.bukkit.*
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.io.File
import java.util.UUID

/**
 * Manages challenge logic, including challenge list, timer end, and win logic.
 * Handles player notifications and Mob Army Battle GUI on timer end.
 */
class ChallengeManager {
    /** MiniMessage instance for formatting messages. */
    private var miniMessage = MiniMessage.miniMessage()
    /** List of all challenge names. */
    var list : Collection<String>? = listOf()
    /** Map of player UUIDs to their entities. */
    var map : Map<UUID, Entity> = mapOf()

    /**
     * Initializes the challenge manager and populates the challenge list.
     */
    init {
        for (challenges in Challenges.values()) {
            list = list!!.plus(challenges.nameString)
        }
    }

    /**
     * Called when the timer ends. Pauses the timer, notifies players, and opens the Mob Army Battle GUI if active.
     */
    fun timerEnd() {
        Timer.win = true
        Timer.paused = true

        for (player in Bukkit.getOnlinePlayers()) {
            player.sendTitlePart(TitlePart.TITLE, miniMessage.deserialize("<b><gradient:#ff4d00:#f7000c>Zeit vorbei"))
            player.playSound(player.location, Sound.ITEM_GOAT_HORN_SOUND_0, SoundCategory.MASTER, 4f, 1f)
        }
        if (Battles.MOB_ARMY_BATTLE.active){
            val waveGuiHome = Bukkit.createInventory(null, 9, "${ChatColor.DARK_RED}Mob-Army-Waves")
            waveGuiHome.setItem(2, Utils().createItem(Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Wave 1", KColors.ORANGERED)))
            waveGuiHome.setItem(4, Utils().createItem(Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Wave 2", KColors.ORANGERED)))
            waveGuiHome.setItem(6, Utils().createItem(Material.DIAMOND_SWORD, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Wave 3", KColors.ORANGERED)))
            waveGuiHome.setItem(8, Utils().createItem(Material.LIME_DYE, 1, glow = false, unbreakable = true, hideUnbreakable = true, cmp("Fertig", KColors.LIME)))
            onlinePlayers.forEach { player ->  player.openInventory(waveGuiHome) }
        }
    }

    /**
     * Called when a win condition is met. Pauses the timer, notifies players, and resets attempts if Mob Army Battle is active.
     * @param what The win reason or description
     */
    fun win(what: String) {
        Timer.win = true
        Timer.paused = true
        Timer.win = false

        if (Battles.MOB_ARMY_BATTLE.active) {
            Bukkit.broadcast(
                miniMessage.deserialize(
                    "<gray>-----------------------------------------</gray>\n" +
                            "\n" +
                            " <gradient:#ff1e00:#ff4400:1>Challenge geschafft - $what\n" +
                            "\n" +
                            " <gray>Timer pausiert - </gray><red>${Timer.getTime()}</red>\n" +
                            " <gray>Versuche - </gray><red>${TrySystem().attempts}</red>\n" +
                            "\n" +
                            "<gray>-----------------------------------------</gray>"
                )
            )
            TrySystem().attempts = 0
        }
    }
}