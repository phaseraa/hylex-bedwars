package com.uzm.hylex.bedwars.arena.generators;

import com.uzm.hylex.bedwars.arena.Arena;
import com.uzm.hylex.bedwars.nms.NMS;
import com.uzm.hylex.bedwars.nms.entity.EntityGenerator;
import com.uzm.hylex.core.libraries.holograms.HologramLibrary;
import com.uzm.hylex.core.libraries.holograms.api.Hologram;
import com.uzm.hylex.core.spigot.items.ItemStackUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static com.uzm.hylex.bedwars.arena.generators.Generator.Type.DIAMOND;
import static com.uzm.hylex.bedwars.arena.generators.Generator.Type.EMERALD;

public class Generator {

  public enum Type {
    EMERALD(Material.EMERALD),
    DIAMOND(Material.DIAMOND);

    private Material material;

    Type(Material material) {
      this.material = material;
    }

    public String getName() {
      if (this == EMERALD) {
        return "§2§lESMERALDA";
      }

      return "§b§lDIAMANTE";
    }

    public Material getItem() {
      return material;
    }

    public ItemStack getBlock() {
      return new ItemStack(Material.matchMaterial(this.name() + "_BLOCK"));
    }
  }


  private Type type;
  private int countdown;
  private Location location;

  private Arena arena;
  private EntityGenerator block;
  private Hologram hologram;

  public Generator(Arena arena, Type type, Location location) {
    this.arena = arena;
    this.type = type;
    this.location = location;
  }

  private int tick = 0;
  private boolean floatLoop = true;

  public void enable() {
    this.disable();
    this.block = NMS.createGeneratorEntity(this);
    this.hologram = HologramLibrary
      .createHologram(location.clone().add(0, 2.4, 0), "§eSpawnando em §c" + this.countdown + " §esegundos", this.type.getName(), "§eNível §c" + StringUtils.repeat("I", 1));
  }

  public void disable() {
    this.countdown = type == DIAMOND ? arena.getUpgradeState().getDiamondDelay() : arena.getUpgradeState().getEmeraldDelay();
    if (this.block != null) {
      this.block.killEntity();
      this.block = null;
    }
    if (this.hologram != null) {
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }
  }

  public void tick() {
    ArmorStand armorStand = this.block.getEntity();
    Location location = armorStand.getLocation();
    location.setYaw((location.getYaw() + 8.5F));
    if (!this.floatLoop) {
      location.add(0, 0.08, 0);

      if (armorStand.getLocation().getY() > (0.30 + (this.location.getY() + 1.5))) {
        this.floatLoop = true;
      }
    } else {
      location.subtract(0, 0.08, 0);

      if (armorStand.getLocation().getY() < (-0.33 + (this.location.getY() + 1.5))) {
        this.floatLoop = false;
      }
    }

    armorStand.teleport(location);

    if (this.tick == 20) {
      this.tick = 0;
      if (this.countdown == 0) {
        if (ItemStackUtils.getAmountOfItem(this.type.getItem(), this.location) < 4) {
          Item item = this.location.getWorld().dropItem(this.location, new ItemStack(this.type.getItem()));
          item.setPickupDelay(20);
          item.setVelocity(new Vector().setY(0.02));

        }
        this.countdown = type == DIAMOND ? arena.getUpgradeState().getDiamondDelay() : arena.getUpgradeState().getEmeraldDelay();
        this.hologram.updateLine(1, "§eSpawnando em §c" + getDelay() + " §esegundos");
        this.hologram.updateLine(3, "§eNível §c" + StringUtils.repeat("I", getTier()));
        return;
      }
      this.hologram.updateLine(1, "§eSpawnando em §c" + countdown + " §esegundos");
      this.hologram.updateLine(3, "§eNível §c" + StringUtils.repeat("I", getTier()));
      this.countdown--;
      return;
    }

    this.tick++;
  }


  public int getTier() {
    return getType() == EMERALD ? arena.getUpgradeState().getEmeraldLevel() : arena.getUpgradeState().getDiamondLevel();
  }

  public int getDelay() {
    return getType() == EMERALD ? arena.getUpgradeState().getEmeraldDelay() : arena.getUpgradeState().getDiamondDelay();
  }


  public Location getLocation() {
    return location;
  }

  public Type getType() {
    return type;
  }
}
