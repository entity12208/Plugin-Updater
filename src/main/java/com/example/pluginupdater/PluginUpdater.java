package com.example.pluginupdater;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PluginUpdater extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PluginUpdater has been enabled!");
        Bukkit.getScheduler().runTaskAsynchronously(this, this::updateAllPlugins);
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginUpdater has been disabled!");
    }

    private void updateAllPlugins() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            String pluginName = plugin.getName();
            getLogger().info("Checking updates for: " + pluginName);

            // Simulated update logic. Replace with actual update logic for your environment.
            String downloadUrl = getPluginDownloadUrl(pluginName);

            if (downloadUrl != null) {
                try {
                    downloadAndReplacePlugin(plugin, downloadUrl);
                    getLogger().info("Successfully updated " + pluginName);
                } catch (IOException e) {
                    getLogger().warning("Failed to update " + pluginName + ": " + e.getMessage());
                }
            } else {
                getLogger().info("No update URL found for " + pluginName);
            }
        }
    }

    private String getPluginDownloadUrl(String pluginName) {
        // Replace this with logic to fetch the actual download URL, e.g., via an API
        // Simulated example:
        return "https://example.com/plugins/" + pluginName + ".jar";
    }

    private void downloadAndReplacePlugin(Plugin plugin, String downloadUrl) throws IOException {
        URL url = new URL(downloadUrl);
        Path pluginFile = plugin.getPluginLoader().getPluginDescriptionFile().getPath().toPath();
        Path tempFile = pluginFile.resolveSibling(pluginFile.getFileName() + ".tmp");

        try (var inputStream = url.openStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        Files.move(tempFile, pluginFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
