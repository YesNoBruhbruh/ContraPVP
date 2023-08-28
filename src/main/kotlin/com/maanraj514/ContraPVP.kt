package com.maanraj514

import com.maanraj514.api.util.title
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ContraPVP : JavaPlugin() {

    override fun onEnable() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.title("", "")
        }
        println("ContraPVP is enabled")
    }

    override fun onDisable() {
        println("ContraPVP is disabled")
    }
}