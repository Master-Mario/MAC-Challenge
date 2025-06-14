package at.mario.challenge.challenges

import at.mario.challenge.Main
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

    NO_DAMAGE(
        Material.SHIELD,
        cmp(Lang.translate("challenge_no_damage"), bold = true, color = KColors.DARKRED),
        Lang.translate("challenge_no_damage"),
        cmp(Lang.translate("challenge_no_damage_desc"), KColors.LIGHTGRAY),
        false
    ),
    NO_RESPAWN(
        Material.SKELETON_SKULL,
        cmp(Lang.translate("challenge_no_respawn"), bold = true, color = KColors.RED),
        Lang.translate("challenge_no_respawn"),
        cmp(Lang.translate("challenge_no_respawn_desc"), KColors.LIGHTGRAY),
        false
    ),
    TEAM_DEATH(
        Material.RED_BED,
        cmp(Lang.translate("challenge_team_death"), bold = true, color = KColors.BLUE),
        Lang.translate("challenge_team_death"),
        cmp(Lang.translate("challenge_team_death_desc"), KColors.LIGHTGRAY),
        false
    ),
    RANDOMIZER(
        Material.COMMAND_BLOCK,
        cmp(Lang.translate("challenge_randomizer"), bold = true, color = KColors.YELLOW),
        Lang.translate("challenge_randomizer"),
        cmp(Lang.translate("challenge_randomizer_desc"), KColors.LIGHTGRAY),
        false
    ),
    SHARED_DAMAGE(
        Material.CHAIN,
        cmp(Lang.translate("challenge_shared_damage"), bold = true, color = KColors.PALEVIOLETRED),
        Lang.translate("challenge_shared_damage"),
        cmp(Lang.translate("challenge_shared_damage_desc"), KColors.LIGHTGRAY),
        false
    ),
    SHARED_REGENERATION(
        Material.GOLDEN_CARROT,
        cmp(Lang.translate("challenge_shared_regeneration"), bold = true, color = KColors.LAWNGREEN),
        Lang.translate("challenge_shared_regeneration"),
        cmp(Lang.translate("challenge_shared_regeneration_desc"), KColors.LIGHTGRAY),
        false
    ),
    NO_FALL_DAMAGE(
        Material.FEATHER,
        cmp(Lang.translate("challenge_no_fall_damage"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_no_fall_damage"),
        cmp(Lang.translate("challenge_no_fall_damage_desc"), KColors.LIGHTGRAY),
        false
    ),
    JUMP_MULTIPLIER(
        Material.SLIME_BALL,
        cmp(Lang.translate("challenge_jump_multiplier"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_jump_multiplier"),
        cmp(Lang.translate("challenge_jump_multiplier_desc"), KColors.LIGHTGRAY),
        false
    ),
    RUN_RANDOMIZER(
        Material.DIAMOND_SWORD,
        cmp(Lang.translate("challenge_run_randomizer"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_run_randomizer"),
        cmp(Lang.translate("challenge_run_randomizer_desc", Config().config.getInt("run-randomizer.anzahl-der-distanz")), KColors.LIGHTGRAY),
        false
    ),
    NO_BLOCK_DROPS(
        Material.DIAMOND_PICKAXE,
        cmp(Lang.translate("challenge_no_block_drops"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_no_block_drops"),
        cmp(Lang.translate("challenge_no_block_drops_desc"), KColors.LIGHTGRAY),
        false
    ),
    NO_ENTITY_DROPS(
        Material.ZOMBIE_HEAD,
        cmp(Lang.translate("challenge_no_entity_drops"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_no_entity_drops"),
        cmp(Lang.translate("challenge_no_entity_drops_desc"), KColors.LIGHTGRAY),
        false
    ),
    NO_CRAFTING_TABLE(
        Material.CRAFTING_TABLE,
        cmp(Lang.translate("challenge_no_crafting_table"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_no_crafting_table"),
        cmp(Lang.translate("challenge_no_crafting_table_desc"), KColors.LIGHTGRAY),
        false
    ),
    NO_CRAFTING(
        Material.CRAFTING_TABLE,
        cmp(Lang.translate("challenge_no_crafting"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_no_crafting"),
        cmp(Lang.translate("challenge_no_crafting_desc"), KColors.LIGHTGRAY),
        false
    ),
    SEQUENCE(
        Material.DIAMOND_SWORD,
        cmp(Lang.translate("challenge_sequence"), bold = true, color = KColors.LIGHTPURPLE),
        Lang.translate("challenge_sequence"),
        cmp(Lang.translate("challenge_sequence_desc"), KColors.LIGHTGRAY),
        false
    );

    /**
     * Indicates if the challenge is active. Setting this property triggers activation logic and player notifications.
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
                            cmp(nameString, KColors.RED) + cmp(" " + Lang.translate("module"), KColors.LIGHTGRAY),
                            cmp(Lang.translate("module_status", statusString), KColors.LIGHTGRAY)
                        )
                    )
                }
                if (nameComponent == SEQUENCE.nameComponent) {
                    if (Config().config.contains("sequence._${Bukkit.getOnlinePlayers().size}_")) {
                        for(i in 1 until Bukkit.getOnlinePlayers().size){
                            val player = Config().config.getString("sequence._${cmp((i+1).toString()).plainText()}_")
                            if (player != null) {
                                val onlinePlayer = Bukkit.getPlayer(player)
                                onlinePlayer?.sendMessage(Main.prefix + cmp(Lang.translate("sequence_player", (i+1)), KColors.LIGHTPURPLE))
                            }
                        }
                    }else{
                        // Generate sequence
                        var anzahl = 1
                        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                            Config().addString("sequence._${cmp((anzahl).toString()).plainText()}_", onlinePlayer.name)
                            Bukkit.broadcast(Main.prefix + cmp(Lang.translate("sequence_player_broadcast", anzahl, onlinePlayer.name), KColors.LIGHTPURPLE))
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