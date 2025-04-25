package at.mario.challenge.utils

import at.mario.challenge.Main
import de.miraculixx.kpaper.extensions.server
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.encoding.Base64

class PterodactylClient(
    private val apiUrl: String = "https://mac-panel.ddns.net/api/client",
    private val clientApiKey: String = "ptlc_KtjFXFAvjsmAeicaCvQNGSCbtf4ZUzO9BSD2DeDyuIL",
    private val applicationApiKey: String = "ptla_WI07rqzcgsqYy2neFkXCBFYnGfE76MyUaYC0dW3jJxl"
) {

    private fun sendRequest(endpoint: String, apiKey: String, method: String = "GET", body: String? = null): String {
        val url = URL("$apiUrl$endpoint")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.setRequestProperty("Authorization", "Bearer $apiKey")
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doInput = true

        if (method == "POST" && body != null) {
            connection.doOutput = true
            connection.outputStream.use { it.write(body.toByteArray()) }
        }

        val responseCode = connection.responseCode
        val stream = if (responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        }

        return stream.bufferedReader().readText()
    }

    fun getServerStatus(serverId: String): String {
        val response = sendRequest("/servers/$serverId/resources", clientApiKey)
        val parser = JSONParser()
        val jsonObject = parser.parse(response) as JSONObject
        if (jsonObject["attributes"] == null) {
            return "Unbekannt"
        }
        val attributes = jsonObject["attributes"] as JSONObject
        return attributes["current_state"] as String
    }

    fun startServer(serverId: String): String {
        val body = """{"signal":"start"}"""
        return sendRequest("/servers/$serverId/power", "POST", body).also {
            println("⏵ Server $serverId wird gestartet.")
        }
    }

    fun stopServer(serverId: String): String {
        val body = """{"signal":"stop"}"""
        return sendRequest("/servers/$serverId/power", "POST", body).also {
            println("⏵ Server $serverId wird gestartet.")
        }
    }

    fun restartServer(serverId: String): String {
        return sendPowerSignal(serverId, "restart")
    }

    fun killServer(serverId: String): String {
        return sendPowerSignal(serverId, "kill")
    }

    private fun sendPowerSignal(serverId: String, signal: String): String {
        val body = """{"signal":"$signal"}"""
        return sendRequest("/servers/$serverId/power", "POST", body)
    }

    fun uploadPluginJar(serverId: String) {
        val pluginFile: File = File(Main.instance.javaClass.protectionDomain.codeSource.location.toURI())
        val pluginBytes = pluginFile.readBytes()

        // Erstelle das Zielverzeichnis (falls noch nicht vorhanden)
        val createDirJson = """{ "root": "/", "files": ["plugins"] }"""
        sendRequest("$apiUrl/servers/$serverId/files/create-folder", clientApiKey, "POST", createDirJson)

        val url = URL("$apiUrl/servers/$serverId/files/write?file=/plugins/ChallengePLUGIN.jar&encoding=base64")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", "Bearer $clientApiKey")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val encoded = java.util.Base64.getEncoder().encodeToString(pluginBytes)
        val body = """{ "content": "$encoded" }"""

        connection.outputStream.use { it.write(body.toByteArray()) }

        val responseCode = connection.responseCode
        val response = connection.inputStream.bufferedReader().readText()

        server.consoleSender.sendMessage("📦 Plugin ChallengePLUGIN hochgeladen (Code $responseCode): $response")
    }

    fun createServer(teamName: String, serverType: String): String {
        // 1. Allocation finden
        val allocations = getAvailableAllocations(apiUrl, applicationApiKey)
        val selectedAllocation = allocations.firstOrNull()
            ?: return "Keine freien Allocations verfügbar!"

        // 2. Server-Body vorbereiten
        val body = """
        {
          "name": "${teamName}_${serverType}_server",
          "user": 1,
          "egg": 1,
          "docker_image": "ghcr.io/pterodactyl/yolks:java_17",
          "startup": "java -Xms128M -Xmx{{SERVER_MEMORY}}M -jar server.jar nogui",
          "environment": {
              "SERVER_JARFILE": "server.jar",
              "MINECRAFT_VERSION": "1.21.1",
              "DL_PATH": "https://api.papermc.io/v2/projects/paper/versions/1.21.1/builds/133/downloads/paper-1.21.1-133.jar"
          },
          "limits": {
            "memory": 0,
            "swap": 0,
            "disk": 5120,
            "io": 500,
            "cpu": 0
          },
          "feature_limits": {
            "databases": 0,
            "allocations": 1
          },
          "allocation": {
            "default": $selectedAllocation
          },
          "deploy": {
            "locations": [1],
            "dedicated_ip": false,
            "port_range": []
          },
          "start_on_completion": true
        }
        """.trimIndent()

        return sendRequest(
            "$apiUrl/servers",
            applicationApiKey,
            "POST",
            body,
        )
    }
    fun getAvailableAllocations(apiUrl: String, apiKey: String): List<Int> {
        val response = sendRequest(
            "$apiUrl/nodes/1/allocations",
             apiKey,
            "GET",
            null,
        )

        val parser = JSONParser()
        val json = parser.parse(response) as JSONObject
        val data = json["data"] as List<*>

        val freeAllocations = data.mapNotNull {
            val obj = it as? JSONObject
            val attributes = obj?.get("attributes") as? JSONObject
            if (attributes?.get("assigned") == false) {
                (attributes["id"] as? Long)?.toInt()
            } else null
        }

        return freeAllocations
    }


    fun deleteServer(serverId: String): String {
        return sendRequest("$apiUrl/servers/$serverId", applicationApiKey, "DELETE")
    }
}