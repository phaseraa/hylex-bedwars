package com.uzm.hylex.bedwars.nms;

import com.uzm.hylex.bedwars.arena.Arena;
import com.uzm.hylex.bedwars.arena.generators.Generator;
import com.uzm.hylex.bedwars.arena.team.Team;
import com.uzm.hylex.bedwars.nms.entity.*;
import com.uzm.hylex.bedwars.nms.entity.ballon.EntityBallonBat;
import com.uzm.hylex.bedwars.nms.entity.ballon.EntityBallonGiant;
import com.uzm.hylex.bedwars.nms.entity.ballon.EntityBallonLeash;
import com.uzm.hylex.core.nms.reflections.Accessors;
import com.uzm.hylex.core.nms.reflections.acessors.FieldAccessor;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NMS {

  private static final FieldAccessor<Map> CLASS_TO_ID;
  private static final FieldAccessor<Map> CLASS_TO_NAME;
  private static final FieldAccessor<List> PATHFINDERGOAL_B;
  private static final FieldAccessor<List> PATHFINDERGOAL_C;

  static {
    CLASS_TO_ID = Accessors.getField(EntityTypes.class, "f", Map.class);
    CLASS_TO_NAME = Accessors.getField(EntityTypes.class, "d", Map.class);
    PATHFINDERGOAL_B = Accessors.getField(PathfinderGoalSelector.class, 0, List.class);
    PATHFINDERGOAL_C = Accessors.getField(PathfinderGoalSelector.class, 1, List.class);

    CLASS_TO_ID.get(null).put(EntityBedWarsFireball.class, 12);
    CLASS_TO_ID.get(null).put(EntityGenerator.class, 30);
    CLASS_TO_ID.get(null).put(EntityTeamDragon.class, 63);
    CLASS_TO_ID.get(null).put(EntityTeamIronGolem.class, 99);
    CLASS_TO_ID.get(null).put(EntityTeamSilverfish.class, 60);
    CLASS_TO_ID.get(null).put(EntityBallonGiant.class, 53);
    CLASS_TO_ID.get(null).put(EntityBallonBat.class, 65);
    CLASS_TO_ID.get(null).put(EntityBallonLeash.class, 8);

    CLASS_TO_NAME.get(null).put(EntityBedWarsFireball.class, "EntityBedWarsFireball");
    CLASS_TO_NAME.get(null).put(EntityGenerator.class, "EntityGenerator");
    CLASS_TO_NAME.get(null).put(EntityTeamDragon.class, "EntityTeamDragon");
    CLASS_TO_NAME.get(null).put(EntityTeamIronGolem.class, "EntityTeamIronGolem");
    CLASS_TO_NAME.get(null).put(EntityTeamSilverfish.class, "EntityTeamSilverfish");
    CLASS_TO_NAME.get(null).put(EntityBallonGiant.class, "EntityBallonGiant");
    CLASS_TO_NAME.get(null).put(EntityBallonBat.class, "EntityBallonBat");
    CLASS_TO_NAME.get(null).put(EntityBallonLeash.class, "EntityBallonLeash");

  }

  public static EntityGenerator createGeneratorEntity(Generator generator) {
    EntityGenerator entityGenerator = new EntityGenerator(generator);
    Location location = generator.getLocation().clone().add(0, 1.5, 0);
    entityGenerator.setPosition(location.getX(), location.getY(), location.getZ());
    if (entityGenerator.world.addEntity(entityGenerator, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      ArmorStand armor = entityGenerator.getEntity();
      armor.setHelmet(generator.getType().getBlock());
      return entityGenerator;
    }

    return null;
  }

  public static Fireball createFireball(Player player) {
    EntityBedWarsFireball fireball = new EntityBedWarsFireball(player);
    Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(1.2));
    fireball.getBukkitEntity().setVelocity(location.getDirection().normalize().multiply(0.5));
    fireball.setPosition(location.getX(), location.getY(), location.getZ());
    if (fireball.world.addEntity(fireball, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return (Fireball) fireball.getBukkitEntity();
    }

    return null;
  }

  public static EntityBallonLeash createBalloonLeash(Location location) {
    EntityBallonLeash leash = new EntityBallonLeash(location);
    if (leash.world.addEntity(leash, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return leash;
    }

    return null;
  }

  public static EntityBallonBat createBalloonBat(Location location, EntityBallonLeash leash) {
    EntityBallonBat bat = new EntityBallonBat(location,leash);
    if (bat.world.addEntity(bat, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return bat;
    }
    return null;
  }

  public static EntityBallonGiant createBalloonGiant(Location location,int animationTick, ItemStack[] frames) {
    EntityBallonGiant giant = new EntityBallonGiant(location, animationTick, frames);
    if (giant.world.addEntity(giant, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return giant;
    }
    return null;
  }
  public static void createBallon(Location location, int animationTick, ItemStack... stacks) {
   EntityBallonLeash leash = NMS.createBalloonLeash(location);

    Location batLocation = location.clone();
    batLocation.setX(batLocation.getX() - 4.0);
    batLocation.setY(batLocation.getY() + 18.0);
    batLocation.setZ(batLocation.getZ() + 5.5);
   EntityBallonBat bat = NMS.createBalloonBat(batLocation, leash);

    Location giantLocation = location.clone();
    giantLocation.setX(giantLocation.getX() - 2.0);
    giantLocation.setY(giantLocation.getY() + 9.0);
    giantLocation.setZ(giantLocation.getZ() + 3.0);

    EntityBallonGiant giant = NMS.createBalloonGiant(giantLocation, animationTick,stacks);
  }

  public static EntityTeamDragon createTeamDragon(Arena arena, Team team) {
    EntityTeamDragon dragon = new EntityTeamDragon(arena, team);
    Location location = team.getBorder().getCenterLocation();
    dragon.setPosition(location.getX(), location.getY(), location.getZ());
    if (dragon.world.addEntity(dragon, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return dragon;
    }

    return null;
  }

  public static EntityTeamIronGolem createIronGolem(Arena arena, Team team, Location location) {
    EntityTeamIronGolem dragon = new EntityTeamIronGolem(arena, team);
    dragon.setPosition(location.getX(), location.getY(), location.getZ());
    if (dragon.world.addEntity(dragon, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return dragon;
    }
    return null;
  }


  public static EntityTeamSilverfish createSilverfish(Arena arena, Team team, Location location) {
    EntityTeamSilverfish dragon = new EntityTeamSilverfish(arena, team);
    dragon.setPosition(location.getX(), location.getY(), location.getZ());
    if (dragon.world.addEntity(dragon, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return dragon;
    }
    return null;
  }

  public static void hideOrShowArmor(Player player, boolean hide, Collection<Player> receivers) {

    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(ep.getId(), 1, hide ? null : ep.getEquipment(1));
    PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(ep.getId(), 2, hide ? null : ep.getEquipment(2));
    PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(ep.getId(), 3, hide ? null : ep.getEquipment(3));
    PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(ep.getId(), 4, hide ? null : ep.getEquipment(4));

    for (Player pls : receivers) {
      ep = ((CraftPlayer) pls).getHandle();
      ep.playerConnection.sendPacket(helmetPacket);
      ep.playerConnection.sendPacket(chestPacket);
      ep.playerConnection.sendPacket(legPacket);
      ep.playerConnection.sendPacket(bootsPacket);
    }
  }


  public static void sendFakeSpectator(Player player) {
    player.setGameMode(GameMode.SPECTATOR);
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, ep);
    FieldAccessor<Object> accessor = Accessors.getField(packet.getClass(), "b");
    List<PacketPlayOutPlayerInfo.PlayerInfoData> infoList = new ArrayList<>();
    infoList.add(packet.new PlayerInfoData(ep.getProfile(), ep.ping, WorldSettings.EnumGamemode.CREATIVE, ep.listName));
    accessor.set(packet, infoList);

    ep.playerConnection.sendPacket(packet);
  }

  public static void clearPathfinderGoal(Object entity) {
    if (entity instanceof org.bukkit.entity.Entity) {
      entity = ((CraftEntity) entity).getHandle();
    }

    net.minecraft.server.v1_8_R3.Entity handle = (net.minecraft.server.v1_8_R3.Entity) entity;
    if (handle instanceof EntityInsentient) {
      EntityInsentient entityInsentient = (EntityInsentient) handle;
      PATHFINDERGOAL_B.get(entityInsentient.goalSelector).clear();
      PATHFINDERGOAL_C.get(entityInsentient.targetSelector).clear();
    }
  }
}
