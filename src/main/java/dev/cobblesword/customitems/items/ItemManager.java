package dev.cobblesword.customitems.items;

import dev.cobblesword.customitems.CustomItemsPlugin;
import dev.cobblesword.customitems.items.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class ItemManager
{
    private HashMap<UUID, CustomItem> customItems = new HashMap<>();

    public void registerCustomItem(CustomItem item)
    {
        this.customItems.put(item.getUniqueId(), item);
        if(item instanceof Listener)
        {
            Bukkit.getPluginManager().registerEvents((Listener) item, CustomItemsPlugin.getPlugin(CustomItemsPlugin.class));
        }
    }

    public CustomItem getItem(UUID uuid)
    {
        return this.customItems.get(uuid);
    }
}
