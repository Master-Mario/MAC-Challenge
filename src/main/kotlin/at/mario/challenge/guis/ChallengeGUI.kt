package at.mario.challenge.guis

import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object ChallengeGUI {

    private const val CHALLENGES_PER_PAGE = 5
    val playerPages = mutableMapOf<String, Int>()

    fun open(player: HumanEntity, page: Int = 0) {
        val challenges = Challenges.values()
        val maxPages = challenges.size / CHALLENGES_PER_PAGE

        val inventory = Bukkit.createInventory(null, 27, "${org.bukkit.ChatColor.BOLD}${org.bukkit.ChatColor.DARK_GRAY}Wähle eine Challenge")
        val filler = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
            itemMeta = itemMeta?.apply { displayName(cmp("")) }
        }

        // Rahmen setzen
        for (i in 0..8) inventory.setItem(i, filler)
        for (i in inventory.size-9..inventory.size-1) inventory.setItem(i, filler)

        // Zurück-Button
        inventory.setItem(9, Utils().createItem(Material.DARK_OAK_DOOR, 1, glow = false, unbreakable = false, false, cmp("Zurück")))
        inventory.setItem(10, Utils().createItem(Material.STRUCTURE_VOID, 1, glow = false, unbreakable = false, false, cmp("Zurück")))
        inventory.setItem(16, Utils().createItem(Material.STRUCTURE_VOID, 1, glow = false, unbreakable = false, false, cmp("Zurück")))

        // Navigation
        if (page > 0) {
            inventory.setItem(9, Utils().createItem(Material.ARROW, 1, glow = false, unbreakable = false, false, cmp("§7< Vorherige Seite")))
        }
        if (page < maxPages) {
            inventory.setItem(17, Utils().createItem(Material.ARROW, 1, glow = false, unbreakable = false, false, cmp("§7Nächste Seite >")))
        }

        // Challenges anzeigen
        val startIndex = page * CHALLENGES_PER_PAGE
        val endIndex = minOf(startIndex + CHALLENGES_PER_PAGE, challenges.size)

        for (i in startIndex until endIndex) {
            val challenge = challenges[i]
            val item = ItemStack(challenge.icon)
            val meta = item.itemMeta!!
            meta.displayName(challenge.nameComponent)
            meta.lore(
                listOf(
                    challenge.description,
                    cmp(""),
                    cmp("Status: ", KColors.GRAY) + if (challenge.active) cmp("Aktiv", KColors.GREEN) else cmp("Inaktiv", KColors.RED)
                )
            )
            item.itemMeta = meta
            inventory.addItem( item)
        }

        inventory.setItem(10, ItemStack(Material.AIR))
        inventory.setItem(16, ItemStack(Material.AIR))
        playerPages[player.name] = page
        player.openInventory(inventory)
    }
}
