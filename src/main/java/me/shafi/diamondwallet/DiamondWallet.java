package me.shafi.diamondwallet;

import me.shafi.diamondwallet.commands.WalletCommand;
import me.shafi.diamondwallet.commands.WalletTopCommand;
import me.shafi.diamondwallet.commands.WalletbalCommand;
import me.shafi.diamondwallet.data.MySQL;
import me.shafi.diamondwallet.data.SQLgetter;
import me.shafi.diamondwallet.listener.InventoryListener;
import me.shafi.diamondwallet.util.ChatUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public final class DiamondWallet extends JavaPlugin {

    private static DiamondWallet instance;
    public static Component prefix;


    public boolean sql_enabled = false;
    public MySQL sql;
    private SQLgetter data;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        prefix = ChatUtil.format(getConfig().getString("prefix"));

        getServer().getPluginManager().registerEvents(new InventoryListener(this) , this);
        getCommand("wallet").setExecutor(new WalletCommand(this));
        getCommand("walletbal").setExecutor(new WalletbalCommand(this));
        getCommand("wallettop").setExecutor(new WalletTopCommand(this));

        sql_enabled = this.getConfig().getBoolean("SQL.use-mysql");
        if(sql_enabled) {
            this.sql = new MySQL(this.getConfig().getString("SQL.host"), this.getConfig().getString("SQL.port"),
                    this.getConfig().getString("SQL.username"), this.getConfig().getString("SQL.password"),
                    this.getConfig().getString("SQL.database"));

            try {
                sql.connect();
            } catch (SQLException e) {
                sql_enabled = false;
                getLogger().log(Level.SEVERE, "Failed to connect to SQL", e);
            }
        }


        if(sql_enabled) {
            getLogger().info("Database connected ");
            getLogger().info("Leaderboard is enabled");

            this.data = new SQLgetter(this);
        }else{

            getLogger().warning("Database is not connected");
            getLogger().warning("Leaderboard is disabled");
        }

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            getServer().getServicesManager().register(
                    net.milkbowl.vault.economy.Economy.class,
                    new VaultHook(this),
                    this,
                    org.bukkit.plugin.ServicePriority.Highest
            );
            getLogger().info("Hooked into Vault successfully!");
        }


    }

    @Override
    public void onDisable() {
        instance = null;
        sql.disconnect();
    }

    public void updateDiamondCount(OfflinePlayer player, Inventory inventory){
        int diamonds = getDiamondCount(inventory);

        if(sql_enabled) {
            data.setDiamond(player, diamonds);
        }


    }

    public int getDiamondCount(Inventory inventory){
        int diamonds = 0;
        int diamond_blocks = 0;

        for(ItemStack itemStack : inventory.getContents()){

            if (itemStack != null) {

                if (itemStack.getType() == Material.DIAMOND_BLOCK) {
                    diamond_blocks += itemStack.getAmount();
                } else if (itemStack.getType() == Material.DIAMOND) {
                    diamonds += itemStack.getAmount();
                }
            }

        }

        diamonds = (diamond_blocks * 9) + diamonds;


        return diamonds;
    }

    public SQLgetter getData() {
        return data;
    }

    public static DiamondWallet getInstance(){
        return instance;
    }
}
