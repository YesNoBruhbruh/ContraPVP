package me.maanraj514.contrapvp.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private List<String> lore = new ArrayList<>();

    public ItemBuilder(Material material){
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount){
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, byte b){
        this.itemStack = new ItemStack(material, amount, b);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack stack){
        this.itemStack = stack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(String displayName){
        this.itemMeta.setDisplayName(MessageUtils.color(displayName));
        return this;
    }

    public ItemBuilder setAmount(int amount){
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setType(Material material){
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder addLoreLine(String line){
        this.lore.add(MessageUtils.color(line));
        return this;
    }

    public ItemBuilder addLoreLines(String... lines){
        this.lore.addAll(Arrays.asList(lines));
        return this;
    }

    public ItemBuilder addLoreLines(List<String> lines){
        this.lore.addAll(lines);
        return this;
    }

    public ItemBuilder setLore(List<String> lore){
        this.lore = lore;
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags){
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setUnbreakAble(){
        this.itemMeta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level){
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemStack build(){
        this.itemMeta.setLore(lore);
        this.itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}