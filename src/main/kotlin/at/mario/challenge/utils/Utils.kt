package at.mario.challenge.utils

import at.mario.challenge.Main
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.util.Vector
import java.io.File
import java.net.InetSocketAddress
import java.util.*
import javax.net.SocketFactory
import kotlin.collections.ArrayList

/**
 * Utility class providing helper methods for item creation, enchantments, entity handling, and server connections.
 */
class Utils {
    /**
     * Creates an ItemStack with custom name and lore (Component version).
     * @param type Material type
     * @param amount Item amount
     * @param glow Whether the item should glow
     * @param unbreakable Whether the item is unbreakable
     * @param hideUnbreakable Whether to hide the unbreakable flag
     * @param name Display name as Component
     * @param lines Lore lines as Components
     * @return The created ItemStack
     */
    fun createItem(
        type: Material?,
        amount: Int,
        glow: Boolean,
        unbreakable: Boolean,
        hideUnbreakable: Boolean,
        name: Component,
        lines: List<Component>
    ): ItemStack {
        requireNotNull(type) { "Material type must not be null" }
        val item = ItemStack(type, amount)
        val meta = item.itemMeta
        if (glow) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true)
        }
        if (unbreakable) {
            meta.isUnbreakable = true
        }
        if (hideUnbreakable) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        }
        meta.displayName(name)
        meta.lore(lines)
        item.setItemMeta(meta)
        return item
    }

    /**
     * Creates an ItemStack without name or lore.
     * @param type Material type
     * @param amount Item amount
     * @param glow Whether the item should glow
     * @param unbreakable Whether the item is unbreakable
     * @param hideUnbreakable Whether to hide the unbreakable flag
     * @return The created ItemStack
     */
    fun createItem(
        type: Material?,
        amount: Int,
        glow: Boolean,
        unbreakable: Boolean,
        hideUnbreakable: Boolean,
    ): ItemStack {
        requireNotNull(type) { "Material type must not be null" }
        val item = ItemStack(type, amount)
        val meta = item.itemMeta
        if (glow) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true)
        }
        if (unbreakable) {
            meta.isUnbreakable = true
        }
        if (hideUnbreakable) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        }
        item.setItemMeta(meta)
        return item
    }

    /**
     * Creates an ItemStack with name and lore (String version).
     * @param type Material type
     * @param amount Item amount
     * @param glow Whether the item should glow
     * @param unbreakable Whether the item is unbreakable
     * @param hideUnbreakable Whether to hide the unbreakable flag
     * @param name Display name as Component
     * @param lines Lore lines as Strings
     * @return The created ItemStack
     */
    fun createItem(
        type: Material?,
        amount: Int,
        glow: Boolean,
        unbreakable: Boolean,
        hideUnbreakable: Boolean,
        name: Component?,
        vararg lines: String
    ): ItemStack {
        requireNotNull(type) { "Material type must not be null" }
        val item = ItemStack(type, amount)
        val meta = item.itemMeta
        if (glow) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true)
        }
        if (unbreakable) {
            meta.isUnbreakable = true
        }
        if (hideUnbreakable) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        }
        name?.let { meta.displayName(it) }
        meta.setLore(lines.toList())
        item.setItemMeta(meta)
        return item
    }

    /**
     * Adds an unsafe enchantment to an ItemStack.
     * @param item The item to enchant
     * @param enchantment The enchantment to add
     * @param level The enchantment level
     * @return The enchanted ItemStack
     */
    fun enchantItem(item: ItemStack, enchantment: Enchantment?, level: Int): ItemStack {
        requireNotNull(enchantment) { "Enchantment must not be null" }
        item.addUnsafeEnchantment(enchantment, level)
        return item
    }

    /**
     * Returns an array representing a full armor set.
     * @param helmet Helmet item
     * @param chestplate Chestplate item
     * @param leggins Leggings item
     * @param boots Boots item
     * @return Array of armor items in correct order
     */
    fun makeArmourSet(
        helmet: ItemStack,
        chestplate: ItemStack,
        leggins: ItemStack,
        boots: ItemStack
    ): Array<ItemStack> {
        val amour = arrayOf(boots, leggins, chestplate, helmet)
        return amour
    }

    /**
     * Gets the Material for a spawn egg of a given EntityType, or null if not available.
     * @param entityType The entity type
     * @return The corresponding spawn egg Material, or null
     */
    fun getSpawnEggMaterial(entityType: EntityType): Material? {
        val materialName = "${entityType.name}_SPAWN_EGG"
        return try {
            Material.valueOf(materialName)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /**
     * Gets the EntityType by its name (case-insensitive, trims leading spaces).
     * @param name The entity name
     * @return The EntityType, or null if not found
     */
    fun getEntityByName(name: String?): EntityType? {
        if (name == null) return null
        return EntityType.values().find { it.name.equals(name.trim(), ignoreCase = true) }
    }

    /**
     * Connects a player to another server via BungeeCord.
     * @param player The player to connect
     * @param name The server name
     */
    fun joinServer(player: Player, name: String){
        val out: ByteArrayDataOutput = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(name)
        player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray())
    }
}