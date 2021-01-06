package dev.cobblesword.customitems;

import dev.cobblesword.customitems.items.items.FishSlapper;
import dev.cobblesword.customitems.items.items.StickOfPower;
import dev.cobblesword.customitems.listeners.PlayerListener;
import dev.cobblesword.customitems.items.ItemManager;
import dev.cobblesword.customitems.items.ItemInfoInjectorPacketListener;
import dev.cobblesword.customitems.commons.protocol.ProtocolManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CustomItemsPlugin extends JavaPlugin
{
    private static final Class<?>[] ITEM_PACKETS = new Class<?>[]{
            PacketPlayOutWindowItems.class,
            PacketPlayOutSetSlot.class
    };

    private ProtocolManager protocolManager;
    private ItemManager itemManager;

    public static HashSet<UUID> changingGamemode = new HashSet<>();

    @Override
    public void onEnable()
    {
        this.itemManager = new ItemManager();
        this.itemManager.registerCustomItem(new StickOfPower());
        this.itemManager.registerCustomItem(new FishSlapper());

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            this.protocolManager = new ProtocolManager(this);
            this.protocolManager.registerPacketListener(ITEM_PACKETS, new ItemInfoInjectorPacketListener(this.itemManager));

            Bukkit.getPluginManager().registerEvents(new PlayerListener(this, this.protocolManager), this);
        }, 20L);
    }

    @Override
    public void onDisable()
    {

    }
}
