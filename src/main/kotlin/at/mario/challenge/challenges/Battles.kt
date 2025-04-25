package at.mario.challenge.challenges

import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

enum class Battles(val icon: Material, val nameComponent: Component, val nameString: String, val description: Component, private var isActive: Boolean) {
    MOB_ARMY_BATTLE(Material.WITHER_SKELETON_SKULL, cmp("Mob Army Battle", bold = true, color = KColors.DARKRED), "Mob Army Battle", cmp("Baue deine eigene Monster Armee auf.", KColors.LIGHTGRAY), false);
    var active: Boolean
        get() = isActive
        set(value) {
            if (value) {
                if(nameString == "Mob Army Battle"){
                    Config().resetKills()
                    try {
                        /*Main.core = Bukkit.getServer().pluginManager.getPlugin("Multiverse-Core") as MultiverseCore
                        Main.core!!.mvWorldManager.deleteWorld("team2");
                        Main.core!!.mvWorldManager.addWorld("team2", World.Environment.NORMAL, null, WorldType.NORMAL, true, null)
                        Main.core!!.mvWorldManager.loadWorld("team2")*/
                    }catch (e: Exception){
                        for (player in Bukkit.getOnlinePlayers()) {
                            Bukkit.broadcast(cmp("Multiverse-Core nicht gefunden, bitte installieren", KColors.RED))
                            isActive = false
                            return
                        }
                    }
                }
            }
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
        }

}