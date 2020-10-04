package me.kei.sex.craft;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SexCraft extends JavaPlugin implements Listener {

    public static Map<String, Integer> data;
    List<EntityType> entities = Arrays.asList(
            EntityType.BAT,
            EntityType.BEE,
            EntityType.BLAZE,
            EntityType.CAT,
            EntityType.CAVE_SPIDER,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.DOLPHIN,
            EntityType.DONKEY,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.FOX,
            EntityType.GIANT,
            EntityType.GHAST,
            EntityType.GUARDIAN,
            EntityType.HUSK,
            EntityType.LLAMA,
            EntityType.MAGMA_CUBE,
            EntityType.MULE,
            EntityType.MUSHROOM_COW,
            EntityType.OCELOT,
            EntityType.PANDA,
            EntityType.PARROT,
            EntityType.PHANTOM,
            EntityType.PIG,
            EntityType.PIG_ZOMBIE,
            EntityType.POLAR_BEAR,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.SHEEP,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SLIME,
            EntityType.SPIDER,
            EntityType.SQUID,
            EntityType.STRAY,
            EntityType.VILLAGER,
            EntityType.WITCH,
            EntityType.WITHER,
            EntityType.WITHER_SKELETON,
            EntityType.WOLF,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_HORSE,
            EntityType.ZOMBIE_VILLAGER
    );

    @Override
    public void onEnable() {

        saveDefaultConfig();

        data = new HashMap<>();
        for (String s : this.getConfig().getStringList("users")) {
            data.put(s, 0);
        }

        new BossbarTask(this).runTaskTimer(this, 2, 2);
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if(!e.isSneaking()) return;
        if (!data.containsKey(e.getPlayer().getName())) {
            Player p = e.getPlayer();
            for (Entity entity : p.getNearbyEntities(1, 1, 1)) {
                if (entity instanceof Player) {
                    Player target = ((Player) entity);
                    if (data.containsKey(target.getName())) {
                        int o = data.get(target.getName()) + 1;
                        for(Player pl : Bukkit.getOnlinePlayers()){
                            pl.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
                        }

                        p.getLocation().getWorld().spawnParticle(Particle.HEART, p.getLocation().clone().add(0, 2.5, 0), 3);
                        p.getLocation().getWorld().spawnParticle(Particle.HEART, target.getLocation().clone().add(0, 2.5, 0), 3);

                        if (o >= this.getConfig().getInt(target.getName())) {
                            data.replace(target.getName(), 0);

                            Location loc = target.getLocation();
                            Collections.shuffle(entities);
                            loc.getWorld().spawnEntity(loc, entities.get(0)).setCustomName(ChatColor.LIGHT_PURPLE + target.getName() + ChatColor.WHITE + " x " + ChatColor.AQUA + p.getName() + ChatColor.WHITE + " の子供");
                            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + target.getName() + ChatColor.WHITE + "と" + ChatColor.AQUA + p.getName() + ChatColor.WHITE + "の間に" + ChatColor.GOLD + entities.get(0).name() + ChatColor.WHITE + "が産まれた！");
                            for(Player pl : Bukkit.getOnlinePlayers()){
                                pl.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                            }
                        } else {
                            data.replace(target.getName(), o);
                        }
                    }
                }
            }
        }
    }
}
