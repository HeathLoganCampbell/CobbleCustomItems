package dev.cobblesword.customitems.listeners;

import dev.cobblesword.customitems.CustomItemsPlugin;
import dev.cobblesword.customitems.items.items.FishSlapper;
import dev.cobblesword.customitems.items.items.StickOfPower;
import dev.cobblesword.customitems.commons.protocol.ProtocolManager;
import dev.cobblesword.customitems.commons.utils.FastUUID;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutWindowItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.EnumSet;

public class PlayerListener implements Listener
{
    private JavaPlugin plugin;
    private ProtocolManager protocolManager;

    public PlayerListener(JavaPlugin plugin, ProtocolManager protocolManager)
    {
        this.plugin = plugin;
        this.protocolManager = protocolManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(Material.STICK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Id: " + FastUUID.toString(StickOfPower.ID)));
        item.setItemMeta(itemMeta);

        e.getPlayer().getInventory().addItem(item);

        org.bukkit.inventory.ItemStack fishItem = new org.bukkit.inventory.ItemStack(Material.RAW_FISH);
        ItemMeta itemMetaFish = item.getItemMeta();
        itemMetaFish.setLore(Arrays.asList(ChatColor.GRAY + "Id: " + FastUUID.toString(FishSlapper.ID)));
        fishItem.setItemMeta(itemMetaFish);
        e.getPlayer().getInventory().addItem(fishItem);
    }

    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent e)
    {
        Player player = e.getPlayer();
        if (e.getNewGameMode() == GameMode.CREATIVE)
        {
            CustomItemsPlugin.changingGamemode.add(player.getUniqueId());

            updateInventory(player);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                CustomItemsPlugin.changingGamemode.remove(player.getUniqueId());
            }, 40L);
        }

        if(player.getGameMode() == GameMode.CREATIVE)
        {
            updateInventory(player);
        }
    }

    private void updateInventory(Player player)
    {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        Container container = handle.activeContainer;
        protocolManager.sendPacket(player, new PacketPlayOutSetSlot(-1, -1, handle.inventory.getCarried()));

        if(container != null)
        {
            protocolManager.sendPacket(player, new PacketPlayOutWindowItems(container.windowId, container.a()));
            if (EnumSet.<InventoryType>of(InventoryType.CRAFTING, InventoryType.WORKBENCH).contains(container.getBukkitView().getType()))
                protocolManager.sendPacket(player, new PacketPlayOutSetSlot(container.windowId, 0, container.getSlot(0).getItem()));
        }
    }
}
