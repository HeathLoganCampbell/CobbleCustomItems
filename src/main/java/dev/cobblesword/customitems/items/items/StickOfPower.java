package dev.cobblesword.customitems.items.items;

import dev.cobblesword.customitems.commons.utils.FastUUID;
import dev.cobblesword.customitems.items.CustomItem;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StickOfPower extends CustomItem
{
    public static final UUID ID = FastUUID.parseUUID("32e09f96-af43-4f6f-9e10-65f9bd548f28");

    public StickOfPower()
    {
        super(ID, ChatColor.YELLOW + "Stick of Power");
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "",
                ChatColor.GRAY + "I am Sprocky wocky",
                ChatColor.RED + "" + System.currentTimeMillis(),
                ChatColor.YELLOW + "--===---===----==--"
        );
    }
}
