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
    
    // Cache config instance to avoid repeated Config() calls in hot paths
    private val config = Config()
    
    /**
     * Gets randomized drop material for a block type and player/mode.
     * Caches config instance to avoid repeated instantiation.
     */
    private fun getRandomizedDrop(blockType: String, playerName: String?, isPerPlayer: Boolean): String? {
        return if (isPerPlayer && playerName != null) {
            config.randomizerConfig.getString("randomizer.$playerName.$blockType")
        } else {
            config.randomizerConfig.getString("randomizer.everyone.$blockType")
        }
    }
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
            val isPerPlayer = Randomizer.PER_PLAYER.active
            val mapped = getRandomizedDrop(it.block.type.name, it.player.name, isPerPlayer)
            
            if (mapped != null){
                it.block.location.world.dropItemNaturally(
                    it.block.location, ItemStack(
                        Material.valueOf(mapped),
                        1
                    )
                )
            } else if (isPerPlayer) {
                Bukkit.broadcast(Main.prefix + cmp(Lang.translate("block_no_drop_defined", it.block.type.name, it.player.name)))
                it.isCancelled = true
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
            val isPerPlayer = Randomizer.PER_PLAYER.active
            
            if (!isPerPlayer){
                val mapped = getRandomizedDrop(it.block.type.name, null, false)
                if (mapped != null) {
                    it.block.location.world.dropItemNaturally(
                        it.block.location, ItemStack(
                            Material.valueOf(mapped),
                            1
                        )
                    )
                }
            } else {
                // Find closest player for per-player randomization
                var closest = Double.MAX_VALUE
                var closestp: Player? = null
                for (player in Bukkit.getOnlinePlayers()) {
                    val dist = player.location.distance(it.block.location)
                    if (closest == Double.MAX_VALUE || dist < closest) {
                        closest = dist
                        closestp = player
                    }
                }
                if (closestp == null) {
                    Bukkit.broadcast(Main.prefix + cmp("No player found for block break!"))
                } else {
                    val mapped = getRandomizedDrop(it.block.type.name, closestp.name, true)
                    if (mapped != null) {
                        it.block.location.world.dropItemNaturally(
                            it.block.location, ItemStack(
                                Material.valueOf(mapped),
                                1
                            )
                        )
                    } else {
                        Bukkit.broadcast(Main.prefix + cmp("${it.block.type.name} has no drop defined! Triggered by ${closestp.name}"))
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
                    Bukkit.broadcast(Main.prefix + cmp("No player found for explosion!"))
                } else {
                    val mapped = getRandomizedDrop(it.block.type.name, closestp.name, true)
                    if (mapped != null) {
                        it.block.location.world.dropItemNaturally(
                            it.block.location, ItemStack(
                                Material.valueOf(mapped),
                                1
                            )
                        )
                    } else {
                        Bukkit.broadcast(Main.prefix + cmp("${it.block.type.name} has no drop defined! Triggered by ${closestp.name}"))
                        it.isCancelled = true
                    }
                }
            } else {
                val mapped = getRandomizedDrop(it.block.type.name, null, false)
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