package net.vortrex.trivia.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Messages {

    private final JavaPlugin plugin;
    private YamlConfiguration messages;

    public Messages(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(file);
    }

    public Component get(String key) {
        String text = messages.getString(key);
        if (text == null) {
            plugin.getLogger().warning("Falta el mensaje: " + key);
            return Component.text("Mensaje no encontrado: " + key);
        }
        String prefix = messages.getString("prefix", "");
        return MiniMessage.miniMessage().deserialize(text, Placeholder.parsed("prefix", prefix));
    }

    public void send(CommandSender to, String key) {
        to.sendMessage(get(key));
    }
}
