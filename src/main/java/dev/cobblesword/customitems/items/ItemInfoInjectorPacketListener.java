package dev.cobblesword.customitems.items;

import dev.cobblesword.customitems.CustomItemsPlugin;
import dev.cobblesword.customitems.items.ItemManager;
import dev.cobblesword.customitems.commons.protocol.PacketListener;
import dev.cobblesword.customitems.items.CustomItem;
import dev.cobblesword.customitems.commons.utils.FastUUID;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

public class ItemInfoInjectorPacketListener extends PacketListener
{
    private static Field SET_SLOT_ITEMSTACK_FIELD;
    private static Field WINDOW_ITEM_ITEMSTACK_FIELD;

    static
    {
        try
        {
            WINDOW_ITEM_ITEMSTACK_FIELD = PacketPlayOutWindowItems.class.getDeclaredField("b");
            SET_SLOT_ITEMSTACK_FIELD = PacketPlayOutSetSlot.class.getDeclaredField("c");

            SET_SLOT_ITEMSTACK_FIELD.setAccessible(true);
            WINDOW_ITEM_ITEMSTACK_FIELD.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    private ItemManager itemManager;

    public ItemInfoInjectorPacketListener(ItemManager itemManager)
    {
        this.itemManager = itemManager;
    }

    @Override
    public Object onPacketOutAsync(Player receiver, Channel channel, Object packet)
    {
        if(receiver.getGameMode() == GameMode.CREATIVE)
        {
            return packet;
        }

        if(CustomItemsPlugin.changingGamemode.contains(receiver.getUniqueId()))
        {
            return packet;
        }

        if(packet instanceof PacketPlayOutSetSlot)
        {
            PacketPlayOutSetSlot setSlot = (PacketPlayOutSetSlot) packet;
            try {
                ItemStack item = (ItemStack) SET_SLOT_ITEMSTACK_FIELD.get(setSlot);
                item = modifyItemstack(item);

                SET_SLOT_ITEMSTACK_FIELD.set(setSlot, item);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else if(packet instanceof PacketPlayOutWindowItems)
        {
            PacketPlayOutWindowItems windowItems = (PacketPlayOutWindowItems) packet;
            try {
                ItemStack[] items = (ItemStack[]) WINDOW_ITEM_ITEMSTACK_FIELD.get(windowItems);

                for (ItemStack item : items)
                {
                    modifyItemstack(item);
                }

                WINDOW_ITEM_ITEMSTACK_FIELD.set(windowItems, items);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return packet;
    }

    @Override
    public Object onPacketInAsync(Player sender, Channel channel, Object packet)
    {
        if(sender.getGameMode() == GameMode.CREATIVE
                && packet instanceof PacketPlayInWindowClick)
        {
            return null;
        }
        return packet;
    }

    private ItemStack modifyItemstack(ItemStack item)
    {
        if(item == null) return item;
        if(item.getTag() == null) return item;
        NBTTagCompound display = item.getTag().getCompound("display");
        NBTTagList lore = display.getList("Lore", 8);

        if(lore == null || lore.size() == 0) return item;
        String uuidLine = lore.getString(0);

        if(uuidLine.isEmpty()) return item;
        UUID uuid = FastUUID.parseUUID(uuidLine.substring(6));

        CustomItem customItem = this.itemManager.getItem(uuid);
        if(customItem == null) return item;

        item.setTag(customItem.getNBT());
        return item;
    }
}
