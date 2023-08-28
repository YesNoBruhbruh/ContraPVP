package com.maanraj514.api.util

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player

fun String.color() =
    ChatColor.translateAlternateColorCodes('&', this)
fun Player.title(title: String, subtitle: String, fadein: Int = 0, stay: Int = 20, fadeout: Int = 0) {
    this.sendTitle(title.color(), subtitle.color(), fadein, stay, fadeout)
}
fun Player.actionBar(message: String) =
    this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message.color()))
class MessageUtil {
}