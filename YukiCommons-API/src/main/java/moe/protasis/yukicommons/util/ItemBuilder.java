package moe.protasis.yukicommons.util;

import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ItemBuilder {
    protected ItemStack item;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int qty) {
        item = new ItemStack(material, qty);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
    }

    public ItemBuilder SetDisplay(String str) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', str));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder SetLore(String... lores) {
        ItemMeta meta = item.getItemMeta();
        List<String> current = meta.getLore();
        if (current == null) current = new ArrayList<>();
        current.addAll(Arrays.stream(lores).map(c -> ChatColor.translateAlternateColorCodes('&', c)).collect(Collectors.toList()));
        meta.setLore(current);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder SetDurability(int durability) {
        item.setDurability((short)durability);
        return this;
    }

    public ItemBuilder SetUnbreakable(boolean unbreakable) {
        var version = IVersionAdaptor.Get();
        if (version == null) return this;

        item = version.SetUnbreakable(item, unbreakable);
        return this;
    }

    public ItemBuilder SetBlockColor(DyeColor color) {
        item.setDurability(color.getWoolData());
        return this;
    }

    public ItemBuilder SetDyeColor(DyeColor color) {
        item.setDurability(color.getDyeData());
        return this;
    }

    public ItemBuilder SetAux(int aux) {
        MaterialData data = item.getData();
        data.setData((byte)aux);
        item.setData(data);
        return this;
    }

    public ItemBuilder AddEnchant(Enchantment ench, int level) {
        item.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder AddCustom(Consumer<ItemStack> func) {
        func.accept(item);
        return this;
    }

    public ItemBuilder SetItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ItemBuilder SetItem(Supplier<ItemStack> func) {
        this.item = func.get();
        return this;
    }

    public ItemBuilder AddTag(String key) {
        item = IVersionAdaptor.Get().AddTag(item, key);
        return this;
    }

    public ItemBuilder AddPotionEffects(PotionEffect... effects) {
        PotionMeta meta = (PotionMeta)item.getItemMeta();

        for (PotionEffect effect : effects) {
            meta.addCustomEffect(effect, true);
        }

        item.setItemMeta(meta);
        return this;
    }

    public ItemStack Build() {
        return item.clone();
    }

    public static ItemBuilder Unbreakable(Material material) {
        return new ItemBuilder(material).SetUnbreakable(true);
    }
}
