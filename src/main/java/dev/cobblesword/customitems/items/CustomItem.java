package dev.cobblesword.customitems.items;

import dev.cobblesword.customitems.commons.utils.FastUUID;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CustomItem
{
    private UUID uniqueId;

    private String name;
    private List<String> lore;

    public CustomItem(UUID uniqueId, String name)
    {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public UUID getUniqueId()
    {
        return this.uniqueId;
    }

    public String getName()
    {
        return this.name;
    }

    public List<String> getLore()
    {
        return this.lore;
    }

    public boolean isItem(ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return false;
        List<String> lore = itemMeta.getLore();
        if(lore == null) return false;
        if(lore.isEmpty()) return false;
        String line = lore.get(0);
        return FastUUID.parseUUID(line.substring(6)).equals(this.uniqueId);
    }

    public NBTTagCompound getNBT()
    {
        NBTTagCompound baseTag = new NBTTagCompound();
        NBTTagCompound displayTag = new NBTTagCompound();
        NBTTagList loreTag = new NBTTagList();

        for (String s : this.getLore()) loreTag.add(new NBTTagString(s));

        displayTag.setString("Name", this.getName());
        displayTag.set("Lore", loreTag);

        baseTag.set("display", displayTag);

        return baseTag;
    }
}
