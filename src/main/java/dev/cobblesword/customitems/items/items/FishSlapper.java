package dev.cobblesword.customitems.items.items;

import dev.cobblesword.customitems.commons.utils.FastUUID;
import dev.cobblesword.customitems.items.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FishSlapper extends CustomItem implements Listener
{
    public static final UUID ID = FastUUID.parseUUID("2a40e288-9c60-489e-955f-d7b2048a1e2b");

    public FishSlapper()
    {
        super(ID, ChatColor.YELLOW + "Fish Slapper");
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                ChatColor.YELLOW + "--===---===----==--"
        );
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        Entity victim = e.getEntity();
        Entity damager = e.getDamager();
        if(damager instanceof Player)
        {
            ItemStack itemInHand = ((Player) damager).getItemInHand();
            if (this.isItem(itemInHand)) {
                ((Player) damager).sendMessage("Fish slap!");
            }
        }
    }
}
