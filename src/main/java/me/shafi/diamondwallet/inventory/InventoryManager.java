package me.shafi.diamondwallet.inventory;

import me.shafi.diamondwallet.DiamondWallet;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class InventoryManager {

    private static final HashMap<UUID, Integer> balanceCache = new HashMap<>();

    public static Inventory loadWallet(OfflinePlayer player) {
        int balance = loadBalance(player);

        Inventory wallet = Bukkit.createInventory(new WalletHolder(player), 54, Component.text("Diamond Wallet"));

        int blocks = balance / 9;
        int diamonds = balance % 9;

        if (blocks > 0) wallet.addItem(new ItemStack(Material.DIAMOND_BLOCK, blocks));
        if (diamonds > 0) wallet.addItem(new ItemStack(Material.DIAMOND, diamonds));

        return  wallet;
    }

    public static void saveWalletFromInventory(OfflinePlayer player, Inventory inv) {
        int totalValue = 0;

        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;
            if (item.getType() == Material.DIAMOND_BLOCK) {
                totalValue += (item.getAmount() * 9);
            } else if (item.getType() == Material.DIAMOND) {
                totalValue += item.getAmount();
            }
        }

        if (totalValue > 31104) totalValue = 31104;

        saveBalance(player, totalValue);
    }

    public static int loadBalance(OfflinePlayer player) {
        if (player.isOnline() && balanceCache.containsKey(player.getUniqueId())) {
            return balanceCache.get(player.getUniqueId());
        }

        int finalBalance = 0;

        if (DiamondWallet.getInstance().sql_enabled) {
            int sqlBal = DiamondWallet.getInstance().getData().getDiamond(player.getUniqueId());
            if (sqlBal != -1) {
                finalBalance = sqlBal;
            } else {
                finalBalance = loadFromYaml(player);
            }
        } else {
            finalBalance = loadFromYaml(player);
        }

        if (player.isOnline()) {
            balanceCache.put(player.getUniqueId(), finalBalance);
        }

        return finalBalance;
    }

    public static void saveBalance(OfflinePlayer player, int amount) {
        if (player.isOnline()) {
            balanceCache.put(player.getUniqueId(), amount);
        }

        File file = new File(DiamondWallet.getInstance().getDataFolder(), "wallets/" + player.getUniqueId() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        config.set("balance", amount);
        try { config.save(file); } catch (IOException e) { e.printStackTrace(); }

        if (DiamondWallet.getInstance().sql_enabled) {
            Bukkit.getScheduler().runTaskAsynchronously(DiamondWallet.getInstance(), () -> {
                DiamondWallet.getInstance().getData().setDiamond(player , amount);
            });
        }
    }

    private static int loadFromYaml(OfflinePlayer player) {
        File file = new File(DiamondWallet.getInstance().getDataFolder(), "wallets/" + player.getUniqueId() + ".yml");
        if (!file.exists()) return 0;
        return YamlConfiguration.loadConfiguration(file).getInt("balance", 0);
    }

}
