package me.shafi.diamondwallet;

import me.shafi.diamondwallet.inventory.InventoryManager;
import me.shafi.diamondwallet.inventory.WalletHolder;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VaultHook extends AbstractEconomy {

    private final DiamondWallet plugin;

    public VaultHook(DiamondWallet plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "DiamondWallet";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return (int) amount + " Diamonds";
    }

    @Override
    public String currencyNamePlural() {
        return "Diamonds";
    }

    @Override
    public String currencyNameSingular() {
        return "Diamond";
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return true;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return InventoryManager.loadBalance(player);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if(player.getName() != null) {
            org.bukkit.entity.Player p = org.bukkit.Bukkit.getPlayer(player.getName());
            if(p != null) player = p;
        }

        if (amount < 0) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Negative");

        int current = InventoryManager.loadBalance(player);
        int maxCapacity = 31104;

        int spaceLeft = maxCapacity - current;
        int amountToAdd = (int) amount;

        if (amountToAdd > spaceLeft) {
            amountToAdd = spaceLeft;

            if (amountToAdd < 0) amountToAdd = 0;
        }

        InventoryManager.saveBalance(player, current + amountToAdd);

        if (player.isOnline()) refreshGui((Player) player);
        return new EconomyResponse(amount, current + amountToAdd, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (amount < 0) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Negative");

        int current = InventoryManager.loadBalance(player);
        int toTake = (int) amount;

        if (current < toTake) {
            return new EconomyResponse(0, current, EconomyResponse.ResponseType.FAILURE, "Insufficient Funds");
        }

        InventoryManager.saveBalance(player, current - toTake);

        if (player.isOnline()) refreshGui((Player) player);

        return new EconomyResponse(toTake, current - toTake, EconomyResponse.ResponseType.SUCCESS, null);
    }

    private void refreshGui(Player p) {
        if (p.getOpenInventory().getTopInventory().getHolder() instanceof WalletHolder) {
            p.openInventory(InventoryManager.loadWallet(p));
        }
    }

    @Override public boolean createPlayerAccount(String playerName, String worldName) { return createPlayerAccount(playerName); }
    @Override public double getBalance(String playerName, String world) { return getBalance(playerName); }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

        return withdrawPlayer(player, amount);
    }

    @Override public boolean hasAccount(String playerName) { return true; }
    @Override public boolean hasAccount(String playerName, String worldName) { return true; }

    @Override
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) { return withdrawPlayer(playerName, amount); }


    @Override public EconomyResponse depositPlayer(String playerName, String worldName, double amount) { return depositPlayer(playerName, amount); }
    @Override public EconomyResponse createBank(String name, String player) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public EconomyResponse deleteBank(String name) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public EconomyResponse bankBalance(String name) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public EconomyResponse bankHas(String name, double amount) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public EconomyResponse bankWithdraw(String name, double amount) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public EconomyResponse bankDeposit(String name, double amount) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public EconomyResponse isBankOwner(String name, String playerName) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public EconomyResponse isBankMember(String name, String playerName) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "No Banks"); }
    @Override public List<String> getBanks() { return null; }
}