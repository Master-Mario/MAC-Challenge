package at.mario.challenge.challenges

import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

/**
 * Enum representing all available randomizer types for the challenge.
 * Each randomizer has an icon, name, description, and activation logic.
 */
enum class Randomizer(
    val icon: Material,
    val nameComponent: Component,
    val nameString: String,
    val description: Component,
    private var isActive: Boolean
) {
    BLOCK_DROP_RANDOMIZER(
        Material.DIAMOND_PICKAXE,
        cmp("Block Drop Randomizer", bold = true, color = KColors.ALICEBLUE),
        "Block Drop Randomizer",
        cmp("Die Blöcke droppen zufällige Items.", KColors.LIGHTGRAY),
        false
    ),
    CHEST_RANDOMIZER(
        Material.CHEST,
        cmp("Kisten Randomizer", bold = true, color = KColors.SADDLEBROWN),
        "Kisten Randomizer",
        cmp("Die Truhen haben zufällige Items.", KColors.LIGHTGRAY),
        false
    ),
    ENTITY_RANDOMIZER(
        Material.ZOMBIE_HEAD,
        cmp("Entity Randomizer", bold = true, color = KColors.SEAGREEN),
        "Entity Randomizer",
        cmp("Die Entities haben zufällige Drops.", KColors.LIGHTGRAY),
        false
    ),
    CRAFT_RANDOMIZER(
        Material.CRAFTING_TABLE,
        cmp("Crafting Randomizer", bold = true, color = KColors.LIGHTPURPLE),
        "Crafting Randomizer",
        cmp("Die Crafting Rezepte sind zufällig.", KColors.LIGHTGRAY),
        false
    ),
    PER_PLAYER(
        Material.PLAYER_HEAD,
        cmp("Pro-Spieler Randomizer", bold = true, color = KColors.LIGHTPURPLE),
        "Per Player Randomizer",
        cmp("Die Items sind für jeden Spieler unterschiedlich.", KColors.LIGHTGRAY),
        false
    );

    /**
     * Indicates if the randomizer is active. Setting this property triggers player notifications and updates the global randomizer state.
     */
    var active: Boolean
        get() = isActive
        set(value) {
            if (isActive != value) {
                isActive = value
                val status = if (isActive) "${ChatColor.GREEN}aktiviert" else "${ChatColor.RED}deaktiviert"
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    onlinePlayer.showTitle(
                        Title.title(
                            cmp(nameString, KColors.RED) + cmp(" Modul", KColors.LIGHTGRAY),
                            cmp("wurde $status", KColors.LIGHTGRAY)
                        )
                    )
                }
            }
            var oneActive = false
            for (randomizer in values()) {
                if (randomizer.active) {
                    oneActive = true
                }
            }
            Challenges.RANDOMIZER.active = oneActive
        }
}