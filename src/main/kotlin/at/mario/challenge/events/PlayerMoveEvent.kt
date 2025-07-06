package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.runnables.task
import io.papermc.paper.event.entity.EntityMoveEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.collections.set

/**
 * Handles player movement events, including logic for run randomizer and jump multiplier challenges.
 */
object PlayerMoveEvent {
    /**
     * Listens for entity move events and cancels movement if the timer is paused.
     */
    val onRunEntity = listen<EntityMoveEvent> {
        if(Timer.paused){
            it.isCancelled = true
            return@listen
        }
    }
    /**
     * Listens for player move events and applies run randomizer and jump multiplier logic.
     */
    val onRun = listen<PlayerMoveEvent> {
        if (Timer.paused && (it.player.gameMode == GameMode.SURVIVAL || it.player.gameMode == GameMode.ADVENTURE) && Config().config.getBoolean("settings.freeze-on-pause")){
            if (it.to.block == it.from.block){
                return@listen
            }
            it.isCancelled = true
            return@listen
        }
        if (Challenges.RUN_RANDOMIZER.active) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (!Main.bossBars.contains(player)) {
                    Main.bossBars[player] = Bukkit.createBossBar(Lang.translate("run_randomizer_bossbar"), BarColor.GREEN, BarStyle.SOLID)
                }
                val runBlocks = Config().config.getDouble("run-randomizer.run-blocks-amount.${player.name}")
                Main.bossBars[player]!!.progress = Math.clamp(runBlocks/Config().config.getDouble("run-randomizer.anzahl-der-distanz"), 0.01, 0.99)
                Main.bossBars[player]!!.setTitle("${ChatColor.BOLD}${ChatColor.GREEN}Lauf-Randomizer: ${runBlocks.toInt()}/${Config().config.getDouble("run-randomizer.anzahl-der-distanz").toInt()}")
                if (!Main.bossBars[player]!!.players.contains(player)) {
                    Main.bossBars[player]!!.addPlayer(player)
                }
                task(false, 0, 0, 1){task ->
                    Config().add("run-randomizer.run-blocks-amount.${it.player.name}", Config().config.getDouble("run-randomizer.run-blocks-amount.${it.player.name}") + it.to.distance(it.from))
                    if (Config().config.getDouble("run-randomizer.run-blocks-amount.${it.player.name}")>= Config().config.getDouble("run-randomizer.anzahl-der-distanz")) {
                        Config().add("run-randomizer.run-blocks-amount.${player.name}", 0.0)
                        val sound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 0.5f, 1f)
                        player.playSound(sound)
                        var everyMaterial: MutableList<Material> = mutableListOf()
                        for (material in Material.values()) {
                            if (material.isItem) {
                                everyMaterial = everyMaterial.plus(material) as MutableList<Material>
                            }
                        }
                        val randomMaterial : Material = everyMaterial.random()
                        it.player.inventory.addItem(Utils().createItem(
                            randomMaterial, 64,
                            glow = false,
                            unbreakable = false,
                            hideUnbreakable = false,
                        ))
                    }
                }
            }
        }
        if (!Challenges.JUMP_MULTIPLIER.active){
            return@listen
        }
        if (it.to.y <= it.from.y) {
            return@listen
        }
        if (getDistance(it.to) != 0.41999998688697815 && getDistance(it.to) != 0.5199999809265137 && getDistance(it.to) >= 0.6){
            return@listen
        }
        if (Main.jumpHeight>94){
            Main.jumpHeight = 94
        }
        it.player.removePotionEffect(PotionEffectType.JUMP_BOOST)
        it.player.addPotionEffect(PotionEffect(PotionEffectType.JUMP_BOOST, Int.MAX_VALUE, Main.jumpHeight))
        if (Main.jumpHeight<4){
            Main.jumpHeight++
        }else {
            Main.jumpHeight = Main.jumpHeight + Main.jumpHeight / 4
        }
    }

    /**
     * Calculates the distance to the next solid block below a given location.
     * @param e The location
     * @return The distance to the next solid block below
     */
    fun getDistance(e: Location): Double {
        val loc: Location = e.clone()
        val y = loc.blockY
        var solidBlock = loc.block
        for (i in y downTo -64) {
            loc.y = i.toDouble()
            if (loc.block.type.isSolid()) {
                solidBlock = loc.block
                break
            }
        }
        return loc.y-solidBlock.location.y-1
    }
}