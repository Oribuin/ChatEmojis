package xyz.oribuin.chatemojis.hooks;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.managers.Manager;

public class VaultHook extends Manager {
    private static Economy vaultEco = null;
    private static Permission permission = null;

    public VaultHook(ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void reload() {
        // Unused
    }

    public void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null)
            return;

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return;

        vaultEco = rsp.getProvider();
    }

    public void setupPermissions() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null)
            return;

        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null)
            return;

        permission = rsp.getProvider();
    }

    public static Economy getVaultEco() {
        return vaultEco;
    }

    public static Permission getPermission() {
        return permission;
    }
}
