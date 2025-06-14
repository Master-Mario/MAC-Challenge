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
 * Enum representing all available goal types for the challenge.
 * Each goal has an icon, name, description, and activation logic.
 */
enum class Goals(
    val icon: Material,
    val nameComponent: Component,
    val nameString: String,
    val description: Component,
    private var isActive: Boolean
) {
    ENDER_DRAGON(
        Material.DRAGON_HEAD,
        cmp(Lang.translate("goal_enderdragon_killed"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("goal_enderdragon_killed"),
        cmp(Lang.translate("goal_enderdragon_killed_desc"), KColors.LIGHTGRAY),
        false
    ),
    ELDER_GUARDIAN(
        Material.TRIDENT,
        cmp(Lang.translate("goal_elderguardian_killed"), bold = true, color = KColors.AQUA),
        Lang.translate("goal_elderguardian_killed"),
        cmp(Lang.translate("goal_elderguardian_killed_desc"), KColors.LIGHTGRAY),
        false
    ),
    WITHER(
        Material.WITHER_SKELETON_SKULL,
        cmp(Lang.translate("goal_wither_killed"), bold = true, color = KColors.DARKGRAY),
        Lang.translate("goal_wither_killed"),
        cmp(Lang.translate("goal_wither_killed_desc"), KColors.LIGHTGRAY),
        false
    ),
    TIMER(
        Material.CLOCK,
        cmp(Lang.translate("goal_timer"), bold = true, color = KColors.RED),
        Lang.translate("goal_timer"),
        cmp(Lang.translate("goal_timer_desc"), KColors.LIGHTGRAY),
        false
    );

    /**
     * Indicates if the goal is active. Setting this property triggers player notifications.
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
                            cmp(nameString, KColors.RED) + cmp(" " + Lang.translate("goal"), KColors.LIGHTGRAY),
                            cmp(Lang.translate("module_status", statusString), KColors.LIGHTGRAY)
                        )
                    )
                }
            }
        }
}