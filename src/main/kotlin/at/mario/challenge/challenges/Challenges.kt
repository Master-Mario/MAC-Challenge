package at.mario.challenge.challenges

import at.mario.challenge.Main
import at.mario.challenge.utils.Config
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plainText
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.potion.PotionEffectType

/**
 * Enum representing all available challenge types for the plugin.
 * Each challenge has an icon, name, description, and activation logic.
 */
enum class Challenges(
    val icon: Material,
    val nameComponent: Component,
    val nameString: String,
    val description: Component,
    private var isActive: Boolean
) {

    NO_DAMAGE(Material.SHIELD, cmp("Kein Schaden", bold = true, color = KColors.DARKRED), "Kein Schaden", cmp("Spiele Minecraft ohne Shaden zu nehmen.", KColors.LIGHTGRAY), false),
    NO_RESPAWN(Material.SKELETON_SKULL, cmp("Kein Respawn", bold = true, color = KColors.RED), "Kein Respawn", cmp("Spiele Minecraft ohne zu Sterben.", KColors.LIGHTGRAY), false),
    TEAM_DEATH(Material.RED_BED, cmp("Teamtod", bold = true, color = KColors.BLUE), "Teamtod", cmp("Wenn einer stirbt sterben alle.", KColors.LIGHTGRAY), false),
    RANDOMIZER(Material.COMMAND_BLOCK, cmp("Zufällige Items", bold = true, color = KColors.YELLOW), "Zufällige Items", cmp("Wähle einen Randomizer aus.", KColors.LIGHTGRAY), false),
    SHARED_DAMAGE(Material.CHAIN, cmp("Geteilten Schaden", bold = true, color = KColors.PALEVIOLETRED), "Geteilten Schaden", cmp("Jeder Schaden wird auf jeden Spieler anggewant.", KColors.LIGHTGRAY), false),
    SHARED_REGENERATION(Material.GOLDEN_CARROT, cmp("Geteilte Regeneration", bold = true, color = KColors.LAWNGREEN), "Geteilte Regeneration", cmp("Jede Regeneration wird auf jeden Spieler anggewant.", KColors.LIGHTGRAY), false),
    NO_FALL_DAMAGE(Material.FEATHER, cmp("Kein Fallschaden", bold = true, color = KColors.LIGHTPURPLE), "Kein Fallschaden", cmp("Spiele Minecraft ohne Fallschaden zu nehmen.", KColors.LIGHTGRAY), false),
    JUMP_MULTIPLIER(Material.SLIME_BALL, cmp("Jump-Multiplikator", bold = true, color = KColors.LIGHTPURPLE), "Jump-Multiplikator", cmp("Springe immer höher.", KColors.LIGHTGRAY), false),
    RUN_RANDOMIZER(Material.DIAMOND_SWORD, cmp("Lauf-Randomizer", bold = true, color = KColors.LIGHTPURPLE), "Lauf-Randomizer", cmp("Gibt dir nach ${Config().config.getInt("run-randomizer.anzahl-der-distanz")} Blöcken ein Item.", KColors.LIGHTGRAY), false),
    NO_BLOCK_DROPS(Material.DIAMOND_PICKAXE, cmp("Keine Blockdrops", bold = true, color = KColors.LIGHTPURPLE), "Keine Blockdrops", cmp("Die Blöcke droppen keine Items.", KColors.LIGHTGRAY), false),
    NO_ENTITY_DROPS(Material.ZOMBIE_HEAD, cmp("Keine Mobdrops", bold = true, color = KColors.LIGHTPURPLE), "Keine Mobdrops", cmp("Mobs droppen keine Items.", KColors.LIGHTGRAY), false),
    NO_CRAFTING_TABLE(Material.CRAFTING_TABLE, cmp("Keine Werkbank", bold = true, color = KColors.LIGHTPURPLE), "Kein Werkbank", cmp("Du kannst keine Werkbank benutzen.", KColors.LIGHTGRAY), false),
    NO_CRAFTING(Material.CRAFTING_TABLE, cmp("Kein Crafting", bold = true, color = KColors.LIGHTPURPLE), "Kein Crafting", cmp("Du kannst nichts craften.", KColors.LIGHTGRAY), false),
    SEQUENCE(Material.DIAMOND_SWORD, cmp("Reihenfolge", bold = true, color = KColors.LIGHTPURPLE), "Reihenfolge", cmp("Schlage Spieler in der Reihenfolge.", KColors.LIGHTGRAY), false);

    /**
     * Indicates if the challenge is active. Setting this property triggers activation logic and player notifications.
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
                if (nameComponent == SEQUENCE.nameComponent) {
                    if (Config().config.contains("sequence._${Bukkit.getOnlinePlayers().size}_")) {
                        for(i in 1 until Bukkit.getOnlinePlayers().size){
                            val player = Config().config.getString("sequence._${cmp((i+1).toString()).plainText()}_")
                            if (player != null) {
                                val onlinePlayer = Bukkit.getPlayer(player)
                                onlinePlayer?.sendMessage(Main.prefix + cmp("Du bist der " + (i+1) + ". Spieler", KColors.LIGHTPURPLE))
                            }
                        }
                    }else{
                        // Generate sequence
                        var anzahl = 1
                        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                            Config().addString("sequence._${cmp((anzahl).toString()).plainText()}_", onlinePlayer.name)
                            Bukkit.broadcast(Main.prefix + cmp(anzahl.toString() + ". Spieler: " + onlinePlayer.name, KColors.LIGHTPURPLE))
                            anzahl++
                        }
                    }
                    if (!Config().config.contains("sequence.next")) {
                        Config().addInt("sequence.next", 1)
                    }
                }
                if (nameComponent == NO_DAMAGE.nameComponent) {
                    for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                        if (value) {
                            onlinePlayer.maxHealth = 0.5
                        } else {
                            onlinePlayer.resetMaxHealth()
                        }
                    }
                }
                if (nameComponent == JUMP_MULTIPLIER.nameComponent){
                    if (!value){
                        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
                            onlinePlayer.removePotionEffect(PotionEffectType.JUMP_BOOST)
                        }
                    }
                }
            }
        }

}