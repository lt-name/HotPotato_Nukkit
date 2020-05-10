package cn.lanink.hotpotato.listener;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.SavePlayerInventory;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.scheduler.Task;

import java.util.LinkedHashMap;

/**
 * 玩家进入/退出服务器 或传送到其他世界时，退出房间
 */
public class PlayerJoinAndQuit implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player != null && HotPotato.getInstance().getRooms().containsKey(player.getLevel().getName())) {
            HotPotato.getInstance().getServer().getScheduler().scheduleDelayedTask(new Task() {
                @Override
                public void onRun(int i) {
                    if (player.isOnline()) {
                        Tools.rePlayerState(player ,false);
                        SavePlayerInventory.savePlayerInventory(player, true);
                        player.teleport(HotPotato.getInstance().getServer().getDefaultLevel().getSafeSpawn());
                    }
                }
            }, 120);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        for (Room room : HotPotato.getInstance().getRooms().values()) {
            if (room.isPlaying(player)) {
                room.quitRoom(player, false);
            }
        }
    }

    @EventHandler
    public void onPlayerTp(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String fromLevel = event.getFrom().getLevel() == null ? null : event.getFrom().getLevel().getName();
        String toLevel = event.getTo().getLevel()== null ? null : event.getTo().getLevel().getName();
        if (player == null || fromLevel == null || toLevel == null) return;
        if (!fromLevel.equals(toLevel)) {
            LinkedHashMap<String, Room> room =  HotPotato.getInstance().getRooms();
            if (room.containsKey(fromLevel) && room.get(fromLevel).isPlaying(player)) {
                event.setCancelled(true);
                player.sendMessage("§e >> §c退出房间请使用命令！");
            }else if (!player.isOp() && room.containsKey(toLevel) &&
                    !room.get(toLevel).isPlaying(player)) {
                event.setCancelled(true);
                player.sendMessage("§e >> §c要进入游戏地图，请先加入游戏！");
            }
        }
    }

}
