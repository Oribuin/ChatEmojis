package xyz.oribuin.chatemojis.hook;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.orilibrary.Manager;

public class VaultHook extends Manager {
    private static Economy vaultEco = null;
    private static Permission permission = null;

    public VaultHook(ChatEmojis plugin) {
        super(plugin);
    }

    public static Economy getVaultEco() {
        return vaultEco;
    }

    public static Permission getPermission() {
        return permission;
    }

    @Override
    public void enable() {
        // Unused
    }

    @Override
    public void disable() {
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
}
