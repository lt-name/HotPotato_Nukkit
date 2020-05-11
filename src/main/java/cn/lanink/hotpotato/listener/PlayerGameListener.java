package cn.lanink.hotpotato.listener;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoTransferEvent;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * 游戏监听器（nk事件）
 * @author lt_name
 */
public class PlayerGameListener implements Listener {

    private final HotPotato hotPotato;

    public PlayerGameListener(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
    }

    /**
     * 实体受到另一实体伤害事件
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player1 = (Player) event.getDamager();
            Player player2 = (Player) event.getEntity();
            if (player1 == null || player2 == null) {
                return;
            }
            Room room = this.hotPotato.getRooms().getOrDefault(player1.getLevel().getName(), null);
            if (room != null) {
                if (room.getMode() == 2) {
                    if (room.isPlaying(player1) && room.getPlayerMode(player1) == 2 &&
                            room.isPlaying(player2) && room.getPlayerMode(player2) != 0) {
                        Server.getInstance().getPluginManager().callEvent(new HotPotatoTransferEvent(room, player1, player2));
                    }
                    event.setCancelled(false);
                    event.setDamage(0);
                }else {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * 玩家点击事件
     * @param event 事件
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();
        if (player == null || item == null || !item.hasCompoundTag()) {
            return;
        }
        Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
        if (room == null || !room.isPlaying(player)) {
            return;
        }
        if (room.getMode() == 1) {
            CompoundTag tag = item.getNamedTag();
            if (tag.getBoolean("isHotPotatoItem") && tag.getInt("HotPotatoType") == 10) {
                event.setCancelled(true);
                room.quitRoom(player);
                player.sendMessage("§a你已退出房间");
            }
        }
    }

}
