package me.shafi.diamondwallet.commands;

import me.shafi.diamondwallet.DiamondWallet;
import me.shafi.diamondwallet.util.ChatUtil;
import me.shafi.diamondwallet.util.WalletTopUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class WalletTopCommand implements CommandExecutor {

    DiamondWallet plugin;

    public WalletTopCommand(DiamondWallet plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        if(!plugin.sql_enabled){
            sender.sendMessage(ChatUtil.formatWithPrefix(plugin.getConfig().getString("messages.mysql-not-connected")));
            return true;
        }

        if(sender.hasPermission("diamondwallet.wallettop")){
            int offset = 1;

            if(args.length >= 1) {
                try {
                    offset = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatUtil.format("&cInvalid page number!"));
                    return true;
                }
            }

            if(offset < 1){
                offset = 1;
            }

            HashMap<String, Integer> diamondmap = plugin.getData().getDiamondMap(offset, 10);

            List<Component> lines = WalletTopUtil.formattopboard(diamondmap, offset);

            for (Component line : lines) {
                sender.sendMessage(line);
            }
        }

        return true;
    }
}
