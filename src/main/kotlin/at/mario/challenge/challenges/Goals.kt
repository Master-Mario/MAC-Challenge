package at.mario.challenge.challenges

import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

enum class Goals(val icon: Material, val nameComponent: Component, val nameString: String, val description: Component, private var isActive: Boolean) {
    ENDER_DRAGON(Material.DRAGON_HEAD, cmp("Ender Drache", bold = true, color = KColors.LIGHTPURPLE), "Ender Drache", cmp("Wenn der Ender Drache stirbt gewinnt ihr.", KColors.LIGHTGRAY), false),
    ELDER_GUARDIAN(Material.TRIDENT, cmp("Elder Guardian", bold = true, color = KColors.AQUA), "Elder Guardian", cmp("Wenn der Elder Guardian stirbt gewinnt ihr.", KColors.LIGHTGRAY), false),
    WITHER(Material.WITHER_SKELETON_SKULL, cmp("Wither", bold = true, color = KColors.DARKGRAY), "Wither", cmp("Wenn der Wither stirbt gewinnt ihr.", KColors.LIGHTGRAY), false),
    TIMER(Material.CLOCK, cmp("Timer", bold = true, color = KColors.RED), "Timer", cmp("Wenn der Timer endet gewinnt ihr.", KColors.LIGHTGRAY), false);

    var active: Boolean
        get() = isActive
        set(value) {
            if (isActive != value) {
                isActive = value
                val status = if (isActive) "${ChatColor.GREEN}aktiviert" else "${ChatColor.RED}deaktiviert"
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    onlinePlayer.showTitle(Title.title(cmp(nameString, KColors.RED) + cmp(" Ziel", KColors.LIGHTGRAY), cmp("wurde $status", KColors.LIGHTGRAY)))
                }
            }
        }

}