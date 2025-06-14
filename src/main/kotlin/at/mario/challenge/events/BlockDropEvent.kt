package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.challenges.Challenges
import at.mario.challenge.challenges.Randomizer
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import io.papermc.paper.event.block.BlockBreakBlockEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.inventory.ItemStack

/**
 * Handles block break and explosion events. Applies challenge and randomizer logic to block drops.
 */
object BlockDropEvent {
    /**
     * Handles block breaking by players, applying drop randomization and challenge rules.
     */
    var onBreak = listen<BlockBreakEvent>{
        if (Timer.paused){
            it.isCancelled = true
            return@listen
        }
        if (Challenges.NO_BLOCK_DROPS.active){
            it.isDropItems = false
            it.block.drops.clear()
            return@listen
        }
        if (Randomizer.BLOCK_DROP_RANDOMIZER.active) {
            it.isDropItems = false
            if (!Randomizer.PER_PLAYER.active){
                val mapped = Config().randomizerConfig.getString("randomizer.everyone." + it.block.type.name)
                if (mapped != null){
                    it.block.location.world.dropItemNaturally(
                        it.block.location, ItemStack(
                            Material.valueOf(mapped),
                            1
                        )
                    )
                }
            }else {
                val mapped = Config().randomizerConfig.getString("randomizer." + it.player.name + "." + it.block.type.name)
                if (mapped != null) {
                    it.block.location.world.dropItemNaturally(
                        it.block.location, ItemStack(
                            Material.valueOf(mapped),
                            1
                        )
                    )
                } else {
                    Bukkit.broadcast(Main.prefix + cmp(Lang.translate("block_no_drop_defined", it.block.type.name, it.player.name)))
                    it.isCancelled = true
                }
            }
        }
    }
    /**
     * Handles blocks broken by other blocks (e.g. pistons), applying drop randomization and challenge rules.
     */
    var onGetBroken = listen<BlockBreakBlockEvent> {
        if (Challenges.NO_BLOCK_DROPS.active){
            it.block.drops.clear()
            return@listen
        }
        if (Randomizer.BLOCK_DROP_RANDOMIZER.active) {
            it.drops.clear()
            if (!Randomizer.PER_PLAYER.active){
                val mapped = Config().randomizerConfig.getString("randomizer.everyone." + it.block.type.name)
                if (mapped != null) {
                    it.block.location.world.dropItemNaturally(
                        it.block.location, ItemStack(
                            Material.valueOf(mapped),
                            1
                        )
                    )
                }
            }else {
                var closest = Double.MAX_VALUE
                var closestp: Player? = null
                for (i in Bukkit.getOnlinePlayers()) {
                    val dist = i.location.distance(it.block.location)
                    if (closest == Double.MAX_VALUE || dist < closest) {
                        closest = dist
                        closestp = i
                    }
                }
                if (closestp == null) {
                    Bukkit.broadcast(Main.prefix + cmp("No player found for block break!"));
                } else {
                    val mapped = Config().randomizerConfig.getString("randomizer." + closestp.name + "." + it.block.type.name)
                    if (mapped != null) {
                        it.block.location.world.dropItemNaturally(
                            it.block.location, ItemStack(
                                Material.valueOf(mapped),
                                1
                            )
                        )
                    } else {
                        Bukkit.broadcast(Main.prefix + cmp("${it.block.type.name} has no drop defined! Triggered by ${closestp.name}"));
                    }
                    it.block.drops.clear()
                }
            }
        }
    }
    /**
     * Handles block explosions, applying drop randomization and challenge rules.
     */
    var onExplode = listen<BlockExplodeEvent> {
        if (Challenges.NO_BLOCK_DROPS.active){
            it.explodedBlockState.drops.clear()
            it.block.drops.clear()
            it.isCancelled = true
            it.block.location.block.type = Material.AIR
            return@listen
        }
        if (Randomizer.BLOCK_DROP_RANDOMIZER.active) {
            var closest = Double.MAX_VALUE
            var closestp: Player? = null
            for (i in Bukkit.getOnlinePlayers()) {
                val dist = i.location.distance(it.block.location)
                if (closest == Double.MAX_VALUE || dist < closest) {
                    closest = dist
                    closestp = i
                }
            }
            if (Randomizer.PER_PLAYER.active){
                if (closestp == null) {
                    Bukkit.broadcast(Main.prefix + cmp("No player found for explosion!"));
                } else {
                    val mapped = Config().randomizerConfig.getString("randomizer." + closestp.name + "." + it.block.type.name)
                    if (mapped != null) {
                        it.block.location.world.dropItemNaturally(
                            it.block.location, ItemStack(
                                Material.valueOf(mapped),
                                1
                            )
                        )
                    } else {
                        Bukkit.broadcast(Main.prefix + cmp("${it.block.type.name} has no drop defined! Triggered by ${closestp.name}"));
                        it.isCancelled = true
                    }
                }
            }else{
                val mapped = Config().randomizerConfig.getString("randomizer.everyone." + it.block.type.name)
                if (mapped != null) {
                    it.block.location.world.dropItemNaturally(
                        it.block.location, ItemStack(
                            Material.valueOf(mapped),
                            1
                        )
                    )
                }
            }
            it.explodedBlockState.drops.clear()
            it.block.drops.clear()
            it.isCancelled = true
            it.block.location.block.type = Material.AIR
        }
    }
}
