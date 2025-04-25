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

class Utils {
    fun createItem(
        type: Material?,
        amount: Int,
        glow: Boolean,
        unbreakable: Boolean,
        hideUnbreakable: Boolean,
        name: Component,
        lines: List<Component>
    ): ItemStack {
        val item = ItemStack(type!!, amount)
        val meta = item.itemMeta
        if (glow) {
            meta.addItemFlags(*arrayOf(ItemFlag.HIDE_ENCHANTS))
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true)
        }
        if (unbreakable) {
            meta.isUnbreakable = true
        }
        if (hideUnbreakable) {
            meta.addItemFlags(*arrayOf(ItemFlag.HIDE_UNBREAKABLE))
        }
        if (name != null) {
            meta.displayName(name)
        }
        val lore: MutableList<Component> = ArrayList()
        for (line in lines) {
            lore.add(line)
        }
        meta.lore(lore)
        item.setItemMeta(meta)
        return item
    }fun createItem(
        type: Material?,
        amount: Int,
        glow: Boolean,
        unbreakable: Boolean,
        hideUnbreakable: Boolean,
    ): ItemStack {
        val item = ItemStack(type!!, amount)
        val meta = item.itemMeta
        if (glow) {
            meta.addItemFlags(*arrayOf(ItemFlag.HIDE_ENCHANTS))
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true)
        }
        if (unbreakable) {
            meta.isUnbreakable = true
        }
        if (hideUnbreakable) {
            meta.addItemFlags(*arrayOf(ItemFlag.HIDE_UNBREAKABLE))
        }
        item.setItemMeta(meta)
        return item
    }
    fun createItem(
        type: Material?,
        amount: Int,
        glow: Boolean,
        unbreakable: Boolean,
        hideUnbreakable: Boolean,
        name: Component?,
        vararg lines: String
    ): ItemStack {
        val item = ItemStack(type!!, amount)
        val meta = item.itemMeta
        if (glow) {
            meta.addItemFlags(*arrayOf(ItemFlag.HIDE_ENCHANTS))
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true)
        }
        if (unbreakable) {
            meta.isUnbreakable = true
        }
        if (hideUnbreakable) {
            meta.addItemFlags(*arrayOf(ItemFlag.HIDE_UNBREAKABLE))
        }
        if (name != null) {
            meta.displayName(name)
        }
        val lore: MutableList<String> = ArrayList()
        for (line in lines) {
            lore.add(line)
        }
        meta.setLore(lore)
        item.setItemMeta(meta)
        return item
    }

    fun enchantItem(item: ItemStack, enchantment: Enchantment?, level: Int): ItemStack {
        item.addUnsafeEnchantment(enchantment!!, level)
        return item
    }

    fun makeArmourSet(
        helmet: ItemStack,
        chestplate: ItemStack,
        leggins: ItemStack,
        boots: ItemStack
    ): Array<ItemStack> {
        val amour = arrayOf(boots, leggins, chestplate, helmet)
        return amour
    }
    fun getSpawnEggMaterial(entityType: EntityType): Material? {
        // Check if there is a corresponding spawn egg material for the entity type
        val materialName = "${entityType.name}_SPAWN_EGG"
        return try {
            Material.valueOf(materialName)
        } catch (e: IllegalArgumentException) {
            null // No corresponding spawn egg for this entity type
        }
    }

    fun getEntityByName(name: String?): EntityType? {
        for (type in EntityType.values()) {
            if (type.name.equals(name!!.removePrefix("  "), ignoreCase = true)) {
                return type
            }
        }
        return null
    }
    fun joinServer(player: Player, name: String){
        // Prepare connection data
        val out: ByteArrayDataOutput = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(name)

        player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray())
    }
}