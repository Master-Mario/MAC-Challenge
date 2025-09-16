package at.mario.challenge.utils

object Lang {
    private val translations = mapOf(
        "material_type_null" to mapOf(
            "de" to "Materialtyp darf nicht null sein",
            "en" to "Material type must not be null",
            "es" to "El tipo de material no debe ser nulo"
        ),
        "enchantment_null" to mapOf(
            "de" to "Verzauberung darf nicht null sein",
            "en" to "Enchantment must not be null",
            "es" to "El encantamiento no debe ser nulo"
        ),
        "config_create" to mapOf(
            "de" to "Konfiguration wird erstellt...",
            "en" to "Creating config...",
            "es" to "Creando configuración..."
        ),
        "config_file_create_error" to mapOf(
            "de" to "Fehler beim Erstellen der Datei: %s",
            "en" to "Error creating file: %s",
            "es" to "Error al crear el archivo: %s"
        ),
        "config_save_error" to mapOf(
            "de" to "Fehler beim Speichern der Konfiguration: %s",
            "en" to "Error saving config: %s",
            "es" to "Error al guardar la configuración: %s"
        ),
        "plugin_upload_success" to mapOf(
            "de" to "📦 Plugin ChallengePLUGIN hochgeladen (Code %d): %s",
            "en" to "📦 Plugin ChallengePLUGIN uploaded (Code %d): %s",
            "es" to "📦 Plugin ChallengePLUGIN subido (Código %d): %s"
        ),
        "no_free_allocations" to mapOf(
            "de" to "Keine freien Allocations verfügbar!",
            "en" to "No free allocations available!",
            "es" to "¡No hay asignaciones libres disponibles!"
        ),
        "server_starting" to mapOf(
            "de" to "⏵ Server %s wird gestartet.",
            "en" to "⏵ Server %s is starting.",
            "es" to "⏵ Servidor %s se está iniciando."
        ),
        "timer_paused" to mapOf(
            "de" to "Der Timer wurde ",
            "en" to "The timer was ",
            "es" to "El temporizador fue "
        ),
        "timer_paused_state" to mapOf(
            "de" to "pausiert",
            "en" to "paused",
            "es" to "pausado"
        ),
        "timer_resumed" to mapOf(
            "de" to "Der Timer wurde ",
            "en" to "The timer was ",
            "es" to "El temporizador fue "
        ),
        "timer_resumed_state" to mapOf(
            "de" to "gestartet",
            "en" to "started",
            "es" to "iniciado"
        ),
        "timer_hidden" to mapOf(
            "de" to "Timer ist jetzt versteckt.",
            "en" to "Timer is now hidden.",
            "es" to "El temporizador ahora está oculto."
        ),
        "timer_shown" to mapOf(
            "de" to "Timer ist jetzt sichtbar.",
            "en" to "Timer is now visible.",
            "es" to "El temporizador ahora es visible."
        ),
        "timer_reset" to mapOf(
            "de" to "Timer wurde zurückgesetzt.",
            "en" to "Timer has been reset.",
            "es" to "El temporizador ha sido reiniciado."
        ),
        "timer_set" to mapOf(
            "de" to "Timer wurde auf %s gesetzt.",
            "en" to "Timer set to %s.",
            "es" to "El temporizador se ha establecido en %s."
        ),
        "timer_invalid_format" to mapOf(
            "de" to "Ungültiges Zeitformat! Beispiel: PT1H2M3S",
            "en" to "Invalid time format! Example: PT1H2M3S",
            "es" to "¡Formato de tiempo inválido! Ejemplo: PT1H2M3S"
        ),
        "team_servers_created" to mapOf(
            "de" to "Team %s Server erstellt: Farming-Server ID = %s, Battle-Server ID = %s",
            "en" to "Team %s servers created: Farming-Server ID = %s, Battle-Server ID = %s",
            "es" to "Servidores del equipo %s creados: ID de Farming-Server = %s, ID de Battle-Server = %s"
        ),
        "settings_title" to mapOf(
            "de" to "Einstellungen",
            "en" to "Settings",
            "es" to "Configuración"
        ),
        "back" to mapOf(
            "de" to "Zurück",
            "en" to "Back",
            "es" to "Atrás"
        ),
        "difficulty" to mapOf(
            "de" to "Schwierigkeitsgrad",
            "en" to "Difficulty",
            "es" to "Dificultad"
        ),
        "current" to mapOf(
            "de" to "Aktuell: ",
            "en" to "Current: ",
            "es" to "Actual: "
        ),
        "peaceful" to mapOf(
            "de" to "Friedlich",
            "en" to "Peaceful",
            "es" to "Pacífico"
        ),
        "easy" to mapOf(
            "de" to "Leicht",
            "en" to "Easy",
            "es" to "Fácil"
        ),
        "normal" to mapOf(
            "de" to "Normal",
            "en" to "Normal",
            "es" to "Normal"
        ),
        "hard" to mapOf(
            "de" to "Schwer",
            "en" to "Hard",
            "es" to "Difícil"
        ),
        "pvp" to mapOf(
            "de" to "PVP",
            "en" to "PVP",
            "es" to "PVP"
        ),
        "status" to mapOf(
            "de" to "Status: ",
            "en" to "Status: ",
            "es" to "Estado: "
        ),
        "enabled" to mapOf(
            "de" to "Aktiviert",
            "en" to "Enabled",
            "es" to "Activado"
        ),
        "disabled" to mapOf(
            "de" to "Deaktiviert",
            "en" to "Disabled",
            "es" to "Desactivado"
        ),
        "natural_regen" to mapOf(
            "de" to "Natürliche Regeneration",
            "en" to "Natural Regeneration",
            "es" to "Regeneración natural"
        ),
        "view_distance" to mapOf(
            "de" to "Sichtweite",
            "en" to "View Distance",
            "es" to "Distancia de visión"
        ),
        "simulation_distance" to mapOf(
            "de" to "Simulationsdistanz",
            "en" to "Simulation Distance",
            "es" to "Distancia de simulación"
        ),
        "randomizer_title" to mapOf(
            "de" to "Wähle einen Randomizer",
            "en" to "Choose a Randomizer",
            "es" to "Elige un Randomizer"
        ),
        "randomizer_back" to mapOf(
            "de" to "Zurück",
            "en" to "Back",
            "es" to "Atrás"
        ),
        "randomizer_status" to mapOf(
            "de" to "Status: ",
            "en" to "Status: ",
            "es" to "Estado: "
        ),
        "randomizer_enabled" to mapOf(
            "de" to "Aktiviert",
            "en" to "Enabled",
            "es" to "Activado"
        ),
        "randomizer_disabled" to mapOf(
            "de" to "Deaktiviert",
            "en" to "Disabled",
            "es" to "Desactivado"
        ),
        "main_menu_title" to mapOf(
            "de" to "§l§8Hauptmenü",
            "en" to "§l§8Main Menu",
            "es" to "§l§8Menú Principal"
        ),
        "goals" to mapOf(
            "de" to "Ziele",
            "en" to "Goals",
            "es" to "Objetivos"
        ),
        "challenges" to mapOf(
            "de" to "Challenges",
            "en" to "Challenges",
            "es" to "Desafíos"
        ),
        "battles" to mapOf(
            "de" to "Battles",
            "en" to "Battles",
            "es" to "Batallas"
        ),
        "settings" to mapOf(
            "de" to "Einstellungen",
            "en" to "Settings",
            "es" to "Configuración"
        ),
        "goal_menu_title" to mapOf(
            "de" to "§l§8Wähle ein Ziel",
            "en" to "§l§8Choose a Goal",
            "es" to "§l§8Elige un objetivo"
        ),
        "active" to mapOf(
            "de" to "Aktiv",
            "en" to "Active",
            "es" to "Activo"
        ),
        "inactive" to mapOf(
            "de" to "Inaktiv",
            "en" to "Inactive",
            "es" to "Inactivo"
        ),
        "challenge_menu_title" to mapOf(
            "de" to "§l§8Wähle eine Challenge",
            "en" to "§l§8Choose a Challenge",
            "es" to "§l§8Elige un desafío"
        ),
        "previous_page" to mapOf(
            "de" to "§7< Vorherige Seite",
            "en" to "§7< Previous Page",
            "es" to "§7< Página anterior"
        ),
        "next_page" to mapOf(
            "de" to "§7Nächste Seite >",
            "en" to "§7Next Page >",
            "es" to "§7Siguiente página >"
        ),
        "battle_menu_title" to mapOf(
            "de" to "§l§8Wähle einen Battle-Modus",
            "en" to "§l§8Choose a Battle Mode",
            "es" to "§l§8Elige un modo de batalla"
        ),
        "regen_self_message" to mapOf(
            "de" to "Du hast %s ❤ Heilung von %s bekommen",
            "en" to "You received %s ❤ healing from %s",
            "es" to "Has recibido %s ❤ de curación de %s"
        ),
        "regen_other_message" to mapOf(
            "de" to "%s hat %s ❤ Heilung von %s bekommen",
            "en" to "%s received %s ❤ healing from %s",
            "es" to "%s ha recibido %s ❤ de curación de %s"
        ),
        "not_next_sequence" to mapOf(
            "de" to "Du bist nicht der Nächste!",
            "en" to "You are not next!",
            "es" to "¡No eres el siguiente!"
        ),
        "run_randomizer_bossbar" to mapOf(
            "de" to "Lauf-Randomizer",
            "en" to "Run Randomizer",
            "es" to "Randomizador de carrera"
        ),
        "mab_waves_title" to mapOf(
            "de" to "§cMob-Army-Waves",
            "en" to "§cMob Army Waves",
            "es" to "§cOleadas de Mob Army"
        ),
        "mab_wave1" to mapOf(
            "de" to "Welle 1",
            "en" to "Wave 1",
            "es" to "Oleada 1"
        ),
        "mab_wave2" to mapOf(
            "de" to "Welle 2",
            "en" to "Wave 2",
            "es" to "Oleada 2"
        ),
        "mab_wave3" to mapOf(
            "de" to "Welle 3",
            "en" to "Wave 3",
            "es" to "Oleada 3"
        ),
        "goal_enderdragon_killed" to mapOf(
            "de" to "Ender-Drache",
            "en" to "Ender Dragon",
            "es" to "Ender Dragón"
        ),
        "goal_elderguardian_killed" to mapOf(
            "de" to "Elder-Guardian",
            "en" to "Elder Guardian",
            "es" to "Elder Guardian"
        ),
        "goal_wither_killed" to mapOf(
            "de" to "Wither",
            "en" to "Wither",
            "es" to "Wither"
        ),
        "death_broadcast" to mapOf(
            "de" to "<gray>-----------------------------------------</gray>\n\n <gradient:#ff1e00:#ff4400:1>%s ist gestorben\n\n <gray>Timer pausiert - </gray><red>%s</red>\n <gray>Versuch - </gray><red>%s</red>\n\n<gray>-----------------------------------------</gray>",
            "en" to "<gray>-----------------------------------------</gray>\n\n <gradient:#ff1e00:#ff4400:1>%s died\n\n <gray>Timer paused - </gray><red>%s</red>\n <gray>Attempt - </gray><red>%s</red>\n\n<gray>-----------------------------------------</gray>",
            "es" to "<gray>-----------------------------------------</gray>\n\n <gradient:#ff1e00:#ff4400:1>%s ha muerto\n\n <gray>Temporizador pausado - </gray><red>%s</red>\n <gray>Intento - </gray><red>%s</red>\n\n<gray>-----------------------------------------</gray>"
        ),
        "reset_hover" to mapOf(
            "de" to "Setzt die Welt zurück",
            "en" to "Reset the world",
            "es" to "Reinicia el mundo"
        ),
        "entity_no_drop_defined" to mapOf(
            "de" to "%s hat keinen Drop definiert!",
            "en" to "%s has no drop defined!",
            "es" to "¡%s no tiene drop definido!"
        ),
        "invalid_material_config" to mapOf(
            "de" to "Ungültiges Material in Config: %s",
            "en" to "Invalid material in config: %s",
            "es" to "Material inválido en la configuración: %s"
        ),
        "damage_you_took" to mapOf(
            "de" to "%s hast %s ❤ Schaden von %s genommen",
            "en" to "%s took %s ❤ damage from %s",
            "es" to "%s recibió %s ❤ de daño de %s"
        ),
        "damage_other_took" to mapOf(
            "de" to "%s hat %s ❤ Schaden von %s genommen",
            "en" to "%s took %s ❤ damage from %s",
            "es" to "%s recibió %s ❤ de daño de %s"
        ),
        "craft_no_drop_defined" to mapOf(
            "de" to "%s hat keinen Drop im Crafting-Randomizer! Ausgelöst von %s",
            "en" to "%s has no drop defined in crafting randomizer! Triggered by %s",
            "es" to "¡%s no tiene drop definido en el randomizador de crafteo! Activado por %s"
        ),
        "block_no_drop_defined" to mapOf(
            "de" to "%s hat keinen Drop im Block-Randomizer! Ausgelöst von %s",
            "en" to "%s has no drop defined in block randomizer! Triggered by %s",
            "es" to "¡%s no tiene drop definido en el randomizador de bloques! Activado por %s"
        ),
        "teleport_spawn_success" to mapOf(
            "de" to "Du wurdest zum Spawn teleportiert!",
            "en" to "You have been teleported to spawn!",
            "es" to "¡Has sido teletransportado al spawn!"
        ),
        "heal_success" to mapOf(
            "de" to "Du wurdest geheilt!",
            "en" to "You have been healed!",
            "es" to "¡Has sido curado!"
        ),
        "no_permission" to mapOf(
            "de" to "Du hast keine Berechtigung um diesen Befehl zu benutzen!",
            "en" to "You do not have permission to use this command!",
            "es" to "¡No tienes permiso para usar este comando!"
        ),
        "try_started" to mapOf(
            "de" to "Try %d gestartet",
            "en" to "Try %d started",
            "es" to "Intento %d iniciado"
        ),
        "try_reset" to mapOf(
            "de" to "Trys gelöscht",
            "en" to "Tries reset",
            "es" to "Intentos reiniciados"
        ),
        "try_get" to mapOf(
            "de" to "Try %d",
            "en" to "Try %d",
            "es" to "Intento %d"
        ),
        "reset_kick_message" to mapOf(
            "de" to "%s hat die Welt zurückgesetzt",
            "en" to "%s has reset the world",
            "es" to "%s ha reiniciado el mundo"
        ),
        "done" to mapOf(
            "de" to "Fertig",
            "en" to "Done",
            "es" to "Listo"
        ),
        "randomizing_for_player" to mapOf(
            "de" to "Randomisiere %s für %s",
            "en" to "Randomizing %s for %s",
            "es" to "Aleatorizando %s para %s"
        ),
        "randomizing_for_everyone" to mapOf(
            "de" to "Randomisiere %s für alle Spieler",
            "en" to "Randomizing %s for all players",
            "es" to "Aleatorizando %s para todos los jugadores"
        ),
        "joined_team1" to mapOf(
            "de" to "%s ist dem Team 1 beigetreten",
            "en" to "%s joined Team 1",
            "es" to "%s se unió al Equipo 1"
        ),
        "joined_team2" to mapOf(
            "de" to "%s ist dem Team 2 beigetreten",
            "en" to "%s joined Team 2",
            "es" to "%s se unió al Equipo 2"
        ),
        "reset_next_player" to mapOf(
            "de" to "Der nächste Spieler wurde zurückgesetzt.",
            "en" to "Next player has been reset.",
            "es" to "El siguiente jugador ha sido reiniciado."
        ),
        "reset_run_block" to mapOf(
            "de" to "Run-Block-Zähler wurde zurückgesetzt.",
            "en" to "Run block counter has been reset.",
            "es" to "El contador de bloques de carrera ha sido reiniciado."
        ),
        "challenge_activated" to mapOf(
            "de" to "Challenge %s wurde aktiviert!",
            "en" to "Challenge %s has been activated!",
            "es" to "¡El desafío %s ha sido activado!"
        ),
        "challenge_already_activated" to mapOf(
            "de" to "Challenge %s ist bereits aktiviert!",
            "en" to "Challenge %s is already activated!",
            "es" to "¡El desafío %s ya está activado!"
        ),
        "challenge_deactivated" to mapOf(
            "de" to "Challenge %s wurde deaktiviert!",
            "en" to "Challenge %s has been deactivated!",
            "es" to "¡El desafío %s ha sido desactivado!"
        ),
        "challenge_already_deactivated" to mapOf(
            "de" to "Challenge %s ist bereits deaktiviert!",
            "en" to "Challenge %s is already deactivated!",
            "es" to "¡El desafío %s ya está desactivado!"
        ),
        "block_drop_randomizer" to mapOf(
            "de" to "Block Drop Randomizer",
            "en" to "Block Drop Randomizer",
            "es" to "Randomizador de Bloques"
        ),
        "block_drop_randomizer_desc" to mapOf(
            "de" to "Die Blöcke droppen zufällige Items.",
            "en" to "Blocks drop random items.",
            "es" to "Los bloques sueltan objetos aleatorios."
        ),
        "chest_randomizer" to mapOf(
            "de" to "Kisten Randomizer",
            "en" to "Chest Randomizer",
            "es" to "Randomizador de Cofres"
        ),
        "chest_randomizer_desc" to mapOf(
            "de" to "Die Truhen haben zufällige Items.",
            "en" to "Chests have random items.",
            "es" to "Los cofres tienen objetos aleatorios."
        ),
        "entity_randomizer" to mapOf(
            "de" to "Entity Randomizer",
            "en" to "Entity Randomizer",
            "es" to "Randomizador de Entidades"
        ),
        "entity_randomizer_desc" to mapOf(
            "de" to "Die Entities haben zufällige Drops.",
            "en" to "Entities have random drops.",
            "es" to "Las entidades tienen drops aleatorios."
        ),
        "craft_randomizer" to mapOf(
            "de" to "Crafting Randomizer",
            "en" to "Crafting Randomizer",
            "es" to "Randomizador de Crafteo"
        ),
        "craft_randomizer_desc" to mapOf(
            "de" to "Die Crafting-Rezepte ergeben zufällige Items.",
            "en" to "Crafting recipes result in random items.",
            "es" to "Las recetas de crafteo dan objetos aleatorios."
        ),
        "per_player_randomizer" to mapOf(
            "de" to "Pro-Spieler Randomizer",
            "en" to "Per Player Randomizer",
            "es" to "Randomizador por Jugador"
        ),
        "per_player_randomizer_desc" to mapOf(
            "de" to "Die Items sind für jeden Spieler unterschiedlich.",
            "en" to "Items are different for each player.",
            "es" to "Los objetos son diferentes para cada jugador."
        ),
        "goal_enderdragon_killed_desc" to mapOf(
            "de" to "Wenn der Ender-Drache stirbt, gewinnt ihr.",
            "en" to "When the Ender Dragon dies, you win.",
            "es" to "Cuando el Ender Dragón muere, ganan."
        ),
        "goal_elderguardian_killed_desc" to mapOf(
            "de" to "Wenn der Elder Guardian stirbt, gewinnt ihr.",
            "en" to "When the Elder Guardian dies, you win.",
            "es" to "Cuando el Elder Guardian muere, ganan."
        ),
        "goal_wither_killed_desc" to mapOf(
            "de" to "Wenn der Wither stirbt, gewinnt ihr.",
            "en" to "When the Wither dies, you win.",
            "es" to "Cuando el Wither muere, ganan."
        ),
        "goal_timer" to mapOf(
            "de" to "Timer",
            "en" to "Timer",
            "es" to "Temporizador"
        ),
        "goal_timer_desc" to mapOf(
            "de" to "Wenn der Timer endet, gewinnt ihr.",
            "en" to "When the timer ends, you win.",
            "es" to "Cuando el temporizador termina, ganan."
        ),
        "challenge_no_damage" to mapOf(
            "de" to "Kein Schaden",
            "en" to "No Damage",
            "es" to "Sin Daño"
        ),
        "challenge_no_damage_desc" to mapOf(
            "de" to "Wenn du Schaden bekommst, verlierst du.",
            "en" to "If you take damage, you lose.",
            "es" to "Si recibes daño, pierdes."
        ),
        "challenge_no_respawn" to mapOf(
            "de" to "Kein Respawn",
            "en" to "No Respawn",
            "es" to "Sin Reaparición"
        ),
        "challenge_no_respawn_desc" to mapOf(
            "de" to "Wenn du stirbst, kannst du nicht respawnen.",
            "en" to "If you die, you can't respawn.",
            "es" to "Si mueres, no puedes reaparecer."
        ),
        "challenge_team_death" to mapOf(
            "de" to "Team Tod",
            "en" to "Team Death",
            "es" to "Muerte de Equipo"
        ),
        "challenge_team_death_desc" to mapOf(
            "de" to "Wenn ein Teammitglied stirbt, verliert das ganze Team.",
            "en" to "If a team member dies, the whole team loses.",
            "es" to "Si un miembro del equipo muere, todo el equipo pierde."
        ),
        "challenge_randomizer" to mapOf(
            "de" to "Randomizer",
            "en" to "Randomizer",
            "es" to "Randomizador"
        ),
        "challenge_randomizer_desc" to mapOf(
            "de" to "Verschiedene Randomizer werden aktiviert.",
            "en" to "Various randomizers are activated.",
            "es" to "Se activan varios randomizadores."
        ),
        "challenge_shared_damage" to mapOf(
            "de" to "Geteilter Schaden",
            "en" to "Shared Damage",
            "es" to "Daño Compartido"
        ),
        "challenge_shared_damage_desc" to mapOf(
            "de" to "Alle Spieler teilen sich den erlittenen Schaden.",
            "en" to "All players share the damage taken.",
            "es" to "Todos los jugadores comparten el daño recibido."
        ),
        "challenge_shared_regeneration" to mapOf(
            "de" to "Geteilte Regeneration",
            "en" to "Shared Regeneration",
            "es" to "Regeneración Compartida"
        ),
        "challenge_shared_regeneration_desc" to mapOf(
            "de" to "Alle Spieler teilen sich die Regeneration.",
            "en" to "All players share regeneration.",
            "es" to "Todos los jugadores comparten la regeneración."
        ),
        "challenge_no_fall_damage" to mapOf(
            "de" to "Kein Fallschaden",
            "en" to "No Fall Damage",
            "es" to "Sin Daño de Caída"
        ),
        "challenge_no_fall_damage_desc" to mapOf(
            "de" to "Du bekommst keinen Fallschaden.",
            "en" to "You take no fall damage.",
            "es" to "No recibes daño de caída."
        ),
        "challenge_jump_multiplier" to mapOf(
            "de" to "Sprung Multiplikator",
            "en" to "Jump Multiplier",
            "es" to "Multiplicador de Salto"
        ),
        "challenge_jump_multiplier_desc" to mapOf(
            "de" to "Deine Sprunghöhe ist erhöht.",
            "en" to "Your jump height is increased.",
            "es" to "Tu altura de salto está aumentada."
        ),
        "challenge_run_randomizer" to mapOf(
            "de" to "Lauf-Randomizer",
            "en" to "Run Randomizer",
            "es" to "Randomizador de Carrera"
        ),
        "challenge_run_randomizer_desc" to mapOf(
            "de" to "Alle %d Blöcke wird ein Randomizer aktiviert.",
            "en" to "Every %d blocks a randomizer is activated.",
            "es" to "Cada %d bloques se activa un randomizador."
        ),
        "challenge_no_block_drops" to mapOf(
            "de" to "Keine Block Drops",
            "en" to "No Block Drops",
            "es" to "Sin Drops de Bloques"
        ),
        "challenge_no_block_drops_desc" to mapOf(
            "de" to "Blöcke droppen keine Items.",
            "en" to "Blocks drop no items.",
            "es" to "Los bloques no sueltan objetos."
        ),
        "challenge_no_entity_drops" to mapOf(
            "de" to "Keine Entity Drops",
            "en" to "No Entity Drops",
            "es" to "Sin Drops de Entidades"
        ),
        "challenge_no_entity_drops_desc" to mapOf(
            "de" to "Entities droppen keine Items.",
            "en" to "Entities drop no items.",
            "es" to "Las entidades no sueltan objetos."
        ),
        "challenge_no_crafting_table" to mapOf(
            "de" to "Keine Werkbank",
            "en" to "No Crafting Table",
            "es" to "Sin Mesa de Crafteo"
        ),
        "challenge_no_crafting_table_desc" to mapOf(
            "de" to "Werkbänke können nicht benutzt werden.",
            "en" to "Crafting tables cannot be used.",
            "es" to "No se pueden usar mesas de crafteo."
        ),
        "challenge_no_crafting" to mapOf(
            "de" to "Kein Crafting",
            "en" to "No Crafting",
            "es" to "Sin Crafteo"
        ),
        "challenge_no_crafting_desc" to mapOf(
            "de" to "Crafting ist komplett deaktiviert.",
            "en" to "Crafting is completely disabled.",
            "es" to "El crafteo está completamente desactivado."
        ),
        "challenge_sequence" to mapOf(
            "de" to "Reihenfolge",
            "en" to "Sequence",
            "es" to "Secuencia"
        ),
        "challenge_sequence_desc" to mapOf(
            "de" to "Die Spieler müssen in einer bestimmten Reihenfolge handeln.",
            "en" to "Players must act in a specific sequence.",
            "es" to "Los jugadores deben actuar en un orden específico."
        ),
        "timer_end_title" to mapOf(
            "de" to "<b><gradient:#ff4d00:#f7000c>Zeit vorbei",
            "en" to "<b><gradient:#ff4d00:#f7000c>Time's up",
            "es" to "<b><gradient:#ff4d00:#f7000c>Tiempo terminado"
        ),
        "challenge_completed" to mapOf(
            "de" to "Challenge geschafft",
            "en" to "Challenge completed",
            "es" to "Desafío completado"
        ),
        "attempts" to mapOf(
            "de" to "Versuche",
            "en" to "Attempts",
            "es" to "Intentos"
        ),
        "timer_paused_broadcast" to mapOf(
            "de" to "Timer pausiert",
            "en" to "Timer paused",
            "es" to "Temporizador pausado"
        ),
        "mob_army_battle" to mapOf(
            "de" to "Mob Army Battle",
            "en" to "Mob Army Battle",
            "es" to "Batalla de Ejército de Monstruos"
        ),
        "mob_army_battle_desc" to mapOf(
            "de" to "Baue deine eigene Monster Armee auf.",
            "en" to "Build your own monster army.",
            "es" to "Construye tu propio ejército de monstruos."
        ),
        "multiverse_not_found" to mapOf(
            "de" to "Multiverse-Core nicht gefunden, bitte installieren",
            "en" to "Multiverse-Core not found, please install it",
            "es" to "Multiverse-Core no encontrado, por favor instálalo"
        ),
        "activated" to mapOf(
            "de" to "aktiviert",
            "en" to "activated",
            "es" to "activado"
        ),
        "deactivated" to mapOf(
            "de" to "deaktiviert",
            "en" to "deactivated",
            "es" to "desactivado"
        ),
        "module" to mapOf(
            "de" to "Modul",
            "en" to "module",
            "es" to "módulo"
        ),
        "goal" to mapOf(
            "de" to "Ziel",
            "en" to "goal",
            "es" to "objetivo"
        ),
        "module_status" to mapOf(
            "de" to "wurde %s",
            "en" to "was %s",
            "es" to "fue %s"
        ),
        "language" to mapOf(
            "de" to "Sprache",
            "en" to "Language",
            "es" to "Idioma"
        ),
        "language_change_hint" to mapOf(
            "de" to "Klicke, um die Sprache zu ändern",
            "en" to "Click to change language",
            "es" to "Haz clic para cambiar el idioma"
        ),
        "language_changed" to mapOf(
            "de" to "Sprache geändert zu: %s",
            "en" to "Language changed to: %s",
            "es" to "Idioma cambiado a: %s"
        ),
        "freeze_on_pause" to mapOf(
            "de" to "Stopp bei Pause",
            "en" to "Freeze on Pause",
            "es" to "Parar en Pausa"
        ),
        "config_migrated_key" to mapOf(
            "de" to "Konfigurationsschlüssel migriert: %s -> %s",
            "en" to "Migrated config key: %s -> %s",
            "es" to "Clave de configuración migrada: %s -> %s"
        ),
    )
    
    // Cache the current language to avoid Config() calls in hot paths
    private var currentLanguage: String = "en"
    private var config: Config? = null
    
    /**
     * Updates the cached language setting. Should be called when language config changes.
     */
    fun refreshLanguage() {
        if (config == null) config = Config()
        currentLanguage = config?.config?.getString("language") ?: "en"
    }
    
    /**
     * Initialize language on first access
     */
    init {
        refreshLanguage()
    }

    fun translate(key: String, vararg args: Any): String {
        val map = translations[key] ?: return key
        val template = map[currentLanguage] ?: map["de"] ?: key
        return String.format(template, *args)
    }
}
