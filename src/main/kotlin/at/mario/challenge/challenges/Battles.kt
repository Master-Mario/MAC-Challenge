package at.mario.challenge.challenges

import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plainText
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

/**
 * Enum representing all available battle challenges.
 * Each battle has an icon, name, description, and activation logic.
 */
enum class Battles(
    val icon: Material,
    val nameComponent: Component,
    val nameString: String,
    val description: Component,
    private var isActive: Boolean
) {
    /** Mob Army Battle challenge. */
    MOB_ARMY_BATTLE(
        Material.WITHER_SKELETON_SKULL,
        cmp(Lang.translate("mob_army_battle"), bold = true, color = KColors.DARKRED),
        Lang.translate("mob_army_battle"),
        cmp(Lang.translate("mob_army_battle_desc"), KColors.LIGHTGRAY),
        false
    );

    /**
     * Indicates if the battle is active. Setting this property triggers activation logic and player notifications.
     */
    var active: Boolean
        get() = isActive
        set(value) {
            if (value) {
                if(nameString == Lang.translate("mob_army_battle")){
                    Config().resetKills()
                    try {
                        // Multiverse-Core world management logic can be added here if needed
                    }catch (e: Exception){
                        for (player in Bukkit.getOnlinePlayers()) {
                            Bukkit.broadcast(cmp(Lang.translate("multiverse_not_found"), KColors.RED))
                            isActive = false
                            return
                        }
                    }
                }
            }
            if (isActive != value) {
                isActive = value
                val status = if (isActive) Lang.translate("activated") else Lang.translate("deactivated")
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    onlinePlayer.showTitle(
                        Title.title(
                            cmp(nameString, KColors.RED) + cmp(" Modul", KColors.LIGHTGRAY),
                            cmp(Lang.translate("module_status", status), KColors.LIGHTGRAY)
                        )
                    )
                }
            }
        }
}