package me.kei.sex.craft;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossbarTask extends BukkitRunnable {

    SexCraft plugin;
    Map<String, BossBar> boss;

    public BossbarTask(SexCraft sexCraft) {
        this.boss = new HashMap<>();
        this.plugin = sexCraft;
    }

    @Override
    public void run() {
        List<String> names = this.plugin.getConfig().getStringList("users");
        for(String s : names){
            Player p = Bukkit.getPlayer(s);
            if(p != null){
                if(p.isOnline()){
                    if(SexCraft.data.containsKey(p.getName())){
                        if(!boss.containsKey(p.getName())){
                            BossBar bossbar = Bukkit.createBossBar(p.getName() + "が孕むまで", BarColor.PINK, BarStyle.SEGMENTED_10);
                            boss.put(p.getName(), bossbar);
                        }

                        for(Map.Entry<String, BossBar> entry : boss.entrySet()){
                            entry.getValue().removeAll();
                            double d = (SexCraft.data.get(p.getName()) / plugin.getConfig().getDouble(p.getName()));
                            if(d > 1) d = 1;
                            entry.getValue().setProgress(d);
                        }

                        for(Entity entity : p.getNearbyEntities(10, 10, 10)){
                            if(entity instanceof Player){
                                boss.get(p.getName()).addPlayer(((Player) entity));
                            }
                        }
                    }
                }
            }
        }
    }
}
