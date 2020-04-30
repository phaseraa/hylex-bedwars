package com.uzm.hylex.bedwars.arena.shop;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopItem {

  private ShopCategory category;
  private String name;
  private boolean lostOnDie;
  private String icon;
  private ItemStack price;
  private List<ItemStack> content;
  private List<String> block;
  private List<ShopItemTier> tiers;

  public ShopItem(ShopCategory category, String name, boolean lostOnDie, String icon, ItemStack price, List<ItemStack> content, List<String> block, List<ShopItemTier> tiers) {
    this.category = category;
    this.name = name;
    this.lostOnDie = lostOnDie;
    this.icon = icon;
    this.price = price;
    this.content = content;
    this.block = block;
    this.tiers = tiers;
  }

  public ShopCategory getCategory() {
    return category;
  }

  public String getName() {
    return name;
  }

  public boolean lostOnDie() {
    return lostOnDie;
  }

  public String getIcon() {
    return icon;
  }

  public ItemStack getPrice() {
    return isTieable() ? getTier(1).getPrice() : price;
  }

  public ItemStack getPrice(int tier) {
    return isTieable() ? getTier(tier).getPrice() : price;
  }

  public List<ItemStack> getContent() {
    return this.getContent(1);
  }

  public boolean isBlocked(ShopItem item) {
    return this.block.contains(item.getName());
  }

  public List<ItemStack> getContent(int tier) {
    return isTieable() ? getTier(tier).getContent() : content;
  }

  public boolean isTieable() {
    return tiers != null && tiers.size() > 0;
  }

  public ShopItemTier getTier(int tier) {
    return tiers.size() == tier ? tiers.get(tiers.size() - 1) : tiers.get(tier - 1);
  }

  public int getMaxTier() {
    return tiers.size();
  }

  static class ShopItemTier {

    private ItemStack price;
    private List<ItemStack> content;

    public ShopItemTier(ItemStack price, List<ItemStack> content) {
      this.price = price;
      this.content = content;
    }

    public ItemStack getPrice() {
      return price;
    }

    public List<ItemStack> getContent() {
      return content;
    }
  }
}