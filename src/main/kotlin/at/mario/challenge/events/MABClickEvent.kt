package at.mario.challenge.events

import at.mario.challenge.Main
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.items.name
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.UUID

object MABClickEvent {
    private val config = Config()
    private val utils = Utils()

    private val waveGuiHome = Bukkit.createInventory(null, 9, "${ChatColor.DARK_RED}Mob-Army-Waves").apply {
        setItem(
            2,
            utils.createItem(
                Material.DIAMOND_SWORD,
                1,
                glow = false,
                unbreakable = true,
                hideUnbreakable = true,
                cmp("Wave 1", KColors.ORANGERED)
            )
        )
        setItem(
            4,
            utils.createItem(
                Material.DIAMOND_SWORD,
                1,
                glow = false,
                unbreakable = true,
                hideUnbreakable = true,
                cmp("Wave 2", KColors.ORANGERED)
            )
        )
        setItem(
            6,
            utils.createItem(
                Material.DIAMOND_SWORD,
                1,
                glow = false,
                unbreakable = true,
                hideUnbreakable = true,
                cmp("Wave 3", KColors.ORANGERED)
            )
        )
        setItem(
            8,
            utils.createItem(
                Material.LIME_DYE,
                1,
                glow = false,
                unbreakable = true,
                hideUnbreakable = true,
                cmp("Fertig", KColors.LIME)
            )
        )
    }

    private fun parseKills(): Pair<Map<String, Int>, Map<String, Int>> {
        val team1Kills = mutableMapOf<String, Int>()
        val team2Kills = mutableMapOf<String, Int>()
        var currentTeam: String? = null

        config.killsFile.forEachLine { line ->
            val parts = line.split(": ")
            if (parts.size < 2) return@forEachLine
            val key = parts[0]
            val value = parts[1].toIntOrNull() ?: return@forEachLine

            when (key) {
                "Team 1", "Team 2" -> currentTeam = key
                else -> {
                    val name = key.removePrefix("  ")
                    if (currentTeam == "Team 1") team1Kills[name] = value
                    else if (currentTeam == "Team 2") team2Kills[name] = value
                }
            }
        }

        return team1Kills to team2Kills
    }

    private fun createWaveInventory(wave: Int, teamKills: Map<String, Int>, teamName: String): Inventory {
        val inv = Bukkit.createInventory(null, 54, cmp("Wave $wave", KColors.ORANGERED))

        teamKills.forEach { (entityName, count) ->
            val entity = utils.getEntityByName(entityName) ?: return@forEach
            val used = (1..3).sumOf { config.config.getInt("Wave$it.$teamName.$entityName") }
            val available = count - used
            val usedInCurrent = config.config.getInt("Wave$wave.$teamName.$entityName")

            inv.addItem(
                utils.createItem(
                    utils.getSpawnEggMaterial(entity),
                    1,
                    false, false, false,
                    cmp("${ChatColor.WHITE}$entityName"),
                    lines = listOf(
                        cmp("${ChatColor.GREEN}Verfügbar: $available"),
                        cmp("${ChatColor.RED}Verwendet: $usedInCurrent")
                    )
                )
            )
        }

        inv.setItem(
            53,
            utils.createItem(
                Material.DARK_OAK_DOOR,
                1,
                false,
                false,
                false,
                cmp("${ChatColor.RED}Zurück"),
                listOf(cmp("${ChatColor.GRAY}Zurück zur Startseite"))
            )
        )
        return inv
    }

    private fun openInventoryForTeam(team: Collection<Player>, inventory: Inventory?) {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (team.contains(player)) {
                if (inventory != null) {
                    player.openInventory(inventory)
                } else {
                    player.closeInventory()
                }
            }
        }
    }


    val onClick = listen<org.bukkit.event.inventory.InventoryClickEvent> {
        if (it.view.title == "${ChatColor.DARK_RED}Mob-Army-Waves") {
            it.isCancelled = true
            val (team1Kills, team2Kills) = parseKills()
            val clickedName = it.currentItem?.itemMeta?.name ?: return@listen

            fun handleWaveClick(wave: Int) {
                openInventoryForTeam(Main.mabTeam1, createWaveInventory(wave, team1Kills, "Team1"))
                openInventoryForTeam(Main.mabTeam2, createWaveInventory(wave, team2Kills, "Team2"))
            }

            when (clickedName) {
                cmp("Wave 1", KColors.ORANGERED) -> handleWaveClick(1)
                cmp("Wave 2", KColors.ORANGERED) -> handleWaveClick(2)
                cmp("Wave 3", KColors.ORANGERED) -> handleWaveClick(3)
                cmp("Fertig", KColors.LIME) -> {
                    val player = it.whoClicked
                    when {
                        Main.mabTeam1.contains(player) -> {
                            config.addBoolean("Team1.Fertig", true)
                            openInventoryForTeam(Main.mabTeam1, null)
                        }

                        Main.mabTeam2.contains(player) -> {
                            config.addBoolean("Team2.Fertig", true)
                            openInventoryForTeam(Main.mabTeam2, null)
                        }
                    }

                    if (config.config.getBoolean("Team1.Fertig") && config.config.getBoolean("Team2.Fertig")) {
                        openInventoryForTeam(Main.mabTeam1 + Main.mabTeam2, null)
                        Bukkit.getOnlinePlayers().forEach { player ->
                            if ((Main.mabTeam1 + Main.mabTeam2).contains(player)) {
                                utils.joinServer(player, "mab-arena")
                            }
                        }
                    }
                }
            }
        }
        // Handling für die Wave GUI (z. B. Wave 1) kannst du ähnlich vereinfachen
    }
}
