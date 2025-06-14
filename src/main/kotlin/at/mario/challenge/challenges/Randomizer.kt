package at.mario.challenge.challenges

import at.mario.challenge.utils.Lang
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
        cmp(Lang.translate("block_drop_randomizer"), bold = true, color = KColors.ALICEBLUE),
        "Block Drop Randomizer",
        cmp(Lang.translate("block_drop_randomizer_desc"), KColors.LIGHTGRAY),
        false
    ),
    CHEST_RANDOMIZER(
        Material.CHEST,
        cmp(Lang.translate("chest_randomizer"), bold = true, color = KColors.SADDLEBROWN),
        "Kisten Randomizer",
        cmp(Lang.translate("chest_randomizer_desc"), KColors.LIGHTGRAY),
        false
    ),
    ENTITY_RANDOMIZER(
        Material.ZOMBIE_HEAD,
        cmp(Lang.translate("entity_randomizer"), bold = true, color = KColors.SEAGREEN),
        "Entity Randomizer",
        cmp(Lang.translate("entity_randomizer_desc"), KColors.LIGHTGRAY),
        false
    ),
    CRAFT_RANDOMIZER(
        Material.CRAFTING_TABLE,
        cmp(Lang.translate("craft_randomizer"), bold = true, color = KColors.LIGHTPURPLE),
        "Crafting Randomizer",
        cmp(Lang.translate("craft_randomizer_desc"), KColors.LIGHTGRAY),
        false
    ),
    PER_PLAYER(
        Material.PLAYER_HEAD,
        cmp(Lang.translate("per_player_randomizer"), bold = true, color = KColors.LIGHTPURPLE),
        "Per Player Randomizer",
        cmp(Lang.translate("per_player_randomizer_desc"), KColors.LIGHTGRAY),
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
                val statusString = if (isActive) "§a"+Lang.translate("activated") else "§c"+Lang.translate("deactivated")
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    onlinePlayer.showTitle(
                        Title.title(
                            cmp(nameString, KColors.RED) + cmp(" "+Lang.translate("module"), KColors.LIGHTGRAY),
                            cmp(Lang.translate("module_status", statusString), KColors.LIGHTGRAY)
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