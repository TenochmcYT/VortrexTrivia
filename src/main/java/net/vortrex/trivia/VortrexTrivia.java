package net.vortrex.trivia;

import net.vortrex.trivia.command.TriviaCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VortrexTrivia extends JavaPlugin {
    //net.vortrex.trivia.VortrexTrivia
    @Override
    public void onEnable() {
        getLogger().info("Iniciado correctamente. version: " + getPluginMeta().getVersion());
        Objects.requireNonNull(getCommand("trivia"), "El comando ‘trivia’ no está declarado en plugin.yml").setExecutor(new TriviaCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Desactivado correctamente. version: " + getPluginMeta().getVersion());
    }
}
