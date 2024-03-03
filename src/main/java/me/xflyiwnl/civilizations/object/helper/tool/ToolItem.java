package me.xflyiwnl.civilizations.object.helper.tool;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class ToolItem {

    private UUID uniqueId = UUID.randomUUID();
    private ItemStack itemStack;
    private ToolAction action;

    public ToolItem() {
    }

    public ToolItem(ItemStack itemStack, ToolAction action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public static ToolBuilder builder() {
        return new ToolBuilder();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ToolAction getAction() {
        return action;
    }

    public void setAction(ToolAction action) {
        this.action = action;
    }

    public static class ToolBuilder {

        private Material material;
        private String name;
        private List<String> lore;
        private ToolAction action;

        public ToolBuilder() {
        }

        public ToolBuilder material(Material material) {
            this.material = material;
            return this;
        }

        public ToolBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ToolBuilder lore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public ToolBuilder action(ToolAction action) {
            this.action = action;
            return this;
        }

        public ToolItem build() {

            ToolItem tool = new ToolItem();
            tool.setAction(action);

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(TextUtil.colorize(name));
            meta.setLore(TextUtil.colorize(lore));

            PersistentDataContainer container = meta.getPersistentDataContainer();

            container.set(Civilizations.getInstance().getNamespacedKey(), PersistentDataType.STRING, tool.getUniqueId().toString());

            item.setItemMeta(meta);

            tool.setItemStack(item);

            return tool;
        }


    }

}
