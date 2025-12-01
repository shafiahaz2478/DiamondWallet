package me.shafi.diamondwallet.listener;

import me.shafi.diamondwallet.DiamondWallet;
import me.shafi.diamondwallet.inventory.InventoryManager;
import me.shafi.diamondwallet.inventory.WalletHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {

    DiamondWallet plugin;
    private final List<Material> allowedItems;

    public InventoryListener(DiamondWallet plugin){
        this.plugin = plugin;
        allowedItems = new ArrayList<>();
        allowedItems.add(Material.DIAMOND);
        allowedItems.add(Material.DIAMOND_BLOCK);
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if(inventory.getHolder() != null && inventory.getHolder() instanceof WalletHolder holder){
            if(!holder.getPlayer().getUniqueId().equals(player.getUniqueId())){
                if(!player.hasPermission("diamondwallet.wallet-others")){
                    return;
                }

            }
            InventoryManager.saveWalletFromInventory(holder.getPlayer() , inventory);

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if(event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof WalletHolder ) {

                if (clickedItem != null && !allowedItems.contains(clickedItem.getType()) ) {
                    event.setCancelled(true);
                }

        }
    }



}
