import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import de.miraculixx.kpaper.runnables.task
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*

object SkinUtils {
    fun applySkinMASTERMARIOSuit(player: Player) {
        task(false, 0,0,1) {
            val texture = "ewogICJ0aW1lc3RhbXAiIDogMTc0NTU5NTkyNzI5NywKICAicHJvZmlsZUlkIiA6ICIxNzRjZmRiNGEzY2I0M2I1YmZjZGU0MjRjM2JiMmM2ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXJhZWwxOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YTA4OTk1ODA0OWYyMDQ2OWQ0ZmFkMWNiMjZjYmY0OWNmNDk4MWM2MzA2MGNmNjk3MmExMDQ2YzE2MTAwM2YiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ=="
            val signature = "uS8Vj68WVlki+GTf+d21tInfiHpWjWEFBtbmIxQmU+rvAFIdC6fk59ISSNhzXp3SPAtWEynYkH9culr9g596ECz590kChOa+ry/OOLWop1iAchF1UgyMFdfXmftgx2HTvKxEDKcB5tJHrc6V6tFNC7ve+lVh90dwP4mSCk5IFknRBJ/2m0oZbcHiD5cl+m73cxmRxy5+EYlBqDbMmgxAkkM6+kZ4hUSdXwy4S+UrjfQPIrszCZD4dL7MfjvOvLYmnq9/9mozw74oIG/EONnaEKqk7KJyxIFkF49HKb0DBL6nl9gUYMiFuZ/WBBxtxBjne7MUxPo5yewfjS3DY3Fkz+zL2hsgSQd7xPWogYTrkEAkvCBAfxV453jL4ZgLnQwLUsfTX9qzXYefOHUiWHFu0msewpf9TWPWa6AUNewGnDA++m74hSzvUQlenZsNGH9T6bRoseOB2Esj5NLmVJHzm4b6pB6E12EMUAkZ7FAHCL6sh8lFM40oqhpVUGsUoBVD++0l+hMBC+kR37M7OVwUKCu2qXWAjST8j/MvGj/SiRFH1ks+DXBRqxz9M8bJUAGAwZntmalTQfhcjaCV0s5P7I88Z07ervUm09vfZ3w6ITUE0evFOKIPBKG3sqOVB7LsHil1dIobxseeF1C9bk66THizRh3W2WeLymdi0BO7b+A="
            val craftPlayer = player as CraftPlayer
            val profile = GameProfile(craftPlayer.profile.id, craftPlayer.profile.name)
            profile.properties.clear()
            profile.properties.put("textures", Property("textures", texture, signature))

            val entityPlayer = craftPlayer.handle
            val field = entityPlayer::class.java.getDeclaredField("bH") // Für 1.20.1!
            field.isAccessible = true
            field.set(entityPlayer, profile)

            craftPlayer.updateCommands() // z.B. kleine Aktionen zum Reload
        }
    }
    fun applySkinMASTERMARIONormal(player: Player) {
        task(false, 0,0,1) {
            val texture = "ewogICJ0aW1lc3RhbXAiIDogMTc0NTU5NjI0MDc3OSwKICAicHJvZmlsZUlkIiA6ICIwNDg2YWUwMWI4Y2I0OWUzODMyZDcwOTNmMWJlNzI3NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJfcGFrbWFuXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NzNjYWM1YTg3OGQzOTEzYzYzYTU1ODE5MjM4NTc5YTA5NGJiNDY5NDM4N2JkMTljYTFhYmQzMTBlYTExMWQ3IgogICAgfQogIH0KfQ=="
            val signature = "FI1j+KmsswyyughaVq3dGMFIp2c6IFZ3Nn6NGv1FXg0dvPeFrp0ljMeCVsJKaDWSKh557lb25mu8QU6d1HEevWkjK0WOMTZlb+CLkupYvN2LhSrPd56+Aetwx2bTc3fDQ2sbLgd6c7Fu3aqaxJDnlHOURVHWDJLHIl//mOix59/S/kN9Yt4gB09JBq72DujLtl/65NtJZLb6mCcs67SMBa0oO7dIm4G6RTCGywdO/ZGQvmfaAWZAiTPz31X+lW7xcofX9XIZHHHE1NUsz7ccpXiWi0vjdrWomohuT1G4R/yV8XtE0TixXdFo5vwKYhT9bQmltiLzQ0JCv6f2RmEEFe8UESd6I76tVLuJ5aoEB91QU+lUB0rSPnimW+6teFvcpkaEdApWZ9p/E9si1X3SE/ri1NLyqS0axwnbnACsmCW6v9psgvr0OWe0ch8NOuelw4XJod+RTiYEoYdVlHq3qlnQSUoN1TR9EwGIXQTwmWnh6/aIrF0wYQILY18owApzqUfONrDLQDqnTLZvJA9yrnZZPaKed35knl9FKoojiAb6o49uqPH6+5/fV1DEKe6PzMB33fh81wT4mgim8swp5I63L3ei/0HpVL4+P34nFw5AiXbKbN/THx6Y8uYgvxv+OOruIcxG3uE08LJ7LZNYZ2yzsah04VqFh2t4zNg1CWc="
            val craftPlayer = player as CraftPlayer
            val profile = GameProfile(craftPlayer.profile.id, craftPlayer.profile.name)
            profile.properties.clear()
            profile.properties.put("textures", Property("textures", texture, signature))

            val entityPlayer = craftPlayer.handle
            val field = entityPlayer::class.java.getDeclaredField("bH") // Für 1.20.1!
            field.isAccessible = true
            field.set(entityPlayer, profile)

            craftPlayer.updateCommands() // z.B. kleine Aktionen zum Reload
        }
    }
}
