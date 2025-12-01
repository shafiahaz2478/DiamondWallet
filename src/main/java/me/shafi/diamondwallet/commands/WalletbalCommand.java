package me.shafi.diamondwallet.commands;

import me.shafi.diamondwallet.DiamondWallet;
import me.shafi.diamondwallet.inventory.InventoryManager;
import me.shafi.diamondwallet.inventory.WalletHolder;
import me.shafi.diamondwallet.util.ChatUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class WalletbalCommand implements CommandExecutor {

    DiamondWallet plugin;

    public WalletbalCommand(DiamondWallet plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {

        if(sender.hasPermission("diamondwallet.walletbal")){
            if(!plugin.sql.isConnected()){
                sender.sendMessage(ChatUtil.formatWithPrefix(plugin.getConfig().getString("messages.mysql-not-connected")));
                return false;
            }
            if(args.length == 0){
                if(sender.hasPermission("diamondwallet.walletbal-others")) {
                    if (sender instanceof Player p) {
                        p.sendMessage(ChatUtil.walletbalformat(LegacyComponentSerializer.legacySection().serialize(p.displayName()), plugin.getData().getDiamond(p.getUniqueId()), plugin.getConfig().getString("messages.walletbal-self")));
                    } else {
                        sender.sendMessage(ChatUtil.formatWithPrefix(plugin.getConfig().getString("messages.not-a-player")));
                    }
                }
            }else if (args.length == 1){
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                if(player.hasPlayedBefore()) {
                    String playername = player.getName();
                    if(player.isOnline()){
                        playername = LegacyComponentSerializer.legacySection().serialize(player.getPlayer().displayName());
                    }
                    sender.sendMessage(ChatUtil.walletbalformat(playername, plugin.getData().getDiamond(player.getUniqueId()) , plugin.getConfig().getString("messages.walletbal-other")));
                }else {
                    sender.sendMessage(ChatUtil.formatWithPrefix(plugin.getConfig().getString("messages.player-dont-exist") ));
                }
            }
        }

        return false;
    }
}
