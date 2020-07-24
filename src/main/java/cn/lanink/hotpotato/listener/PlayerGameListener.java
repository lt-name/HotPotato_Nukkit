package cn.lanink.hotpotato.listener;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoPlayerDeathEvent;
import cn.lanink.hotpotato.event.HotPotatoTransferEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.Map;
import java.util.Random;

/**
 * 游戏监听器（nk事件）
 * @author lt_name
 */
public class PlayerGameListener implements Listener {

    private final HotPotato hotPotato;
    private final Language language;

    public PlayerGameListener(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.language = hotPotato.getLanguage();
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
     * 实体受到伤害事件
     * @param event 事件
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
            if (room == null) return;
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (room.getMode() == 2) {
                    Server.getInstance().getPluginManager().callEvent(new HotPotatoPlayerDeathEvent(room, player));
                }else {
                    player.teleport(room.getWaitSpawn());
                }
                event.setCancelled(true);
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
        if (player == null || item == null) {
            return;
        }
        Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
        if (room == null || !room.isPlaying(player)) {
            return;
        }
        if (event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            player.setAllowModifyWorld(false);
        }
        if (room.getMode() == 1) {
            if (!item.hasCompoundTag()) return;
            CompoundTag tag = item.getNamedTag();
            if (tag.getBoolean("isHotPotatoItem") && tag.getInt("HotPotatoType") == 10) {
                event.setCancelled(true);
                room.quitRoom(player);
                player.sendMessage(this.language.quitRoom);
            }
        }
    }

    /**
     * 玩家重生事件
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        for (Room room : this.hotPotato.getRooms().values()) {
            if (room.isPlaying(player)) {
                if (room.getMode() == 2) {
                    event.setRespawnPosition(room.getRandomSpawn().get(new Random().nextInt(room.getRandomSpawn().size())));
                }else {
                    event.setRespawnPosition(room.getWaitSpawn());
                }
                break;
            }
        }
    }

    /**
     * 玩家执行命令事件
     * @param event 事件
     */
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getMessage() == null) return;
        Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
        if (room == null || !room.isPlaying(player)) {
            return;
        }
        if (event.getMessage().startsWith(this.hotPotato.getCmdUser(), 1) ||
                event.getMessage().startsWith(this.hotPotato.getCmdAdmin(), 1)) {
            return;
        }
        event.setCancelled(true);
        player.sendMessage(this.language.useCmdInRoom);
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getMessage() == null) return;
        Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
        if (room == null || !room.isPlaying(player)) {
            return;
        }
        if (room.getPlayerMode(player) == 0) {
            String message = "§7[§cDeath§7]§r " + player.getName() + " §b>>>§r " + event.getMessage();
            for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                if (entry.getValue() == 0) {
                    entry.getKey().sendMessage(message);
                }
            }
        }else {
            String message = "§7[§aRoom§7]§r " + player.getName() + " §b>>>§r " + event.getMessage();
            room.getPlayers().keySet().forEach(p -> p.sendMessage(message));
        }
        event.setMessage("");
        event.setCancelled(true);
    }

}
