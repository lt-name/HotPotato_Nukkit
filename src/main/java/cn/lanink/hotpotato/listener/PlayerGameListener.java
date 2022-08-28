package cn.lanink.hotpotato.listener;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoPlayerDeathEvent;
import cn.lanink.hotpotato.event.HotPotatoTransferEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.room.RoomStatus;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.Task;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;

/**
 * 游戏监听器（nk事件）
 * @author lt_name
 */
public class PlayerGameListener implements Listener {

    private final HotPotato hotPotato;
    private final Language language;
    private final HashSet<Player> playerAttackCooldown = new HashSet<>();

    public PlayerGameListener(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.language = hotPotato.getLanguage();
    }

    @EventHandler
    public void onPlayerChangeSkin(PlayerChangeSkinEvent event) { //此事件仅玩家主动修改皮肤时触发，不需要针对插件修改特判
        Player player = event.getPlayer();
        Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
        if (room == null || !room.isPlaying(player)) {
            return;
        }
        event.setCancelled(true);
    }

    /**
     * 实体受到另一实体伤害事件
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player player = (Player) event.getEntity();
            if (damager == null || player == null) {
                return;
            }
            Room room = this.hotPotato.getRooms().getOrDefault(damager.getLevel().getName(), null);
            if (room == null || !room.isPlaying(damager) || !room.isPlaying(player)) {
                return;
            }
            if (room.getStatus() == RoomStatus.GAME) {
                if (room.getPlayerMode(damager) != 2 && this.playerAttackCooldown.contains(damager)) {
                    event.setCancelled(true);
                    return;
                }
                if (room.getPlayerMode(damager) == 2 && room.getPlayerMode(player) != 0) {
                    Server.getInstance().getPluginManager().callEvent(new HotPotatoTransferEvent(room, damager, player));
                }
                event.setCancelled(false);
                event.setDamage(0);
                this.playerAttackCooldown.add(damager);
                Server.getInstance().getScheduler().scheduleDelayedTask(this.hotPotato, new Task() {
                    @Override
                    public void onRun(int i) {
                        playerAttackCooldown.remove(damager);
                    }
                }, 20);
            }else {
                event.setCancelled(true);
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
            if (room == null) {
                return;
            }
            switch (event.getCause()) {
                case FALL:
                    event.setDamage(event.getDamage()/2);
                case SUFFOCATION:
                case FIRE:
                case LAVA:
                case DROWNING:
                    //正常伤害无需修改
                    break;
                case VOID:
                    if (room.getStatus() == RoomStatus.GAME) {
                        Server.getInstance().getPluginManager().callEvent(new HotPotatoPlayerDeathEvent(room, player));
                    }else {
                        player.teleport(room.getWaitSpawn());
                    }
                    event.setCancelled(true);
                    break;
                default:
                    event.setDamage(0);
                    break;
            }
            if (event.getFinalDamage() + 1 >= player.getHealth()) {
                player.getInventory().clearAll();
                player.setAllowModifyWorld(true);
                player.setAdventureSettings((new AdventureSettings(player)).set(AdventureSettings.Type.ALLOW_FLIGHT, true));
                player.setGamemode(3);
                room.addPlaying(player, 0);
                Tools.setPlayerInvisible(player, true);
                Tools.broadcastMessage(room, this.language.playerDeathOther.replace("%player%", player.getName()));
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

        //禁止容器交互
        Block block = event.getBlock();
        switch (block.getId()) {
            case Item.CRAFTING_TABLE:
            case Item.CHEST:
            case Item.ENDER_CHEST:
            case Item.ANVIL:
            case Item.SHULKER_BOX:
            case Item.UNDYED_SHULKER_BOX:
            case Item.FURNACE:
            case Item.BURNING_FURNACE:
            case Item.DISPENSER:
            case Item.DROPPER:
            case Item.HOPPER:
            case Item.BREWING_STAND:
            case Item.CAULDRON:
            case Item.BEACON:
            case Item.FLOWER_POT:
            case Item.JUKEBOX:
                event.setCancelled(true);
                break;
            default:
                break;
        }

        if (event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            player.setAllowModifyWorld(false);
        }

        if (room.getStatus() == RoomStatus.WAIT_PLAYER) {
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
                if (room.getStatus() == RoomStatus.GAME) {
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
        String message = event.getMessage();
        if (player == null || message == null) {
            return;
        }
        Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
        if (room == null || !room.isPlaying(player)) {
            return;
        }
        message = message.replace("/", "").split(" ")[0];
        if (this.hotPotato.getCmdUser().equalsIgnoreCase(message) ||
                this.hotPotato.getCmdAdmin().equalsIgnoreCase(message)) {
            return;
        }
        for (String string : this.hotPotato.getCmdWhitelist()) {
            if (string.equalsIgnoreCase(message)) {
                return;
            }
        }
        event.setCancelled(true);
        player.sendMessage(this.language.useCmdInRoom);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getMessage() == null) return;
        Room room = this.hotPotato.getRooms().getOrDefault(player.getLevel().getName(), null);
        if (room == null || !room.isPlaying(player)) {
            for (Room r : this.hotPotato.getRooms().values()) {
                for (Player p : r.getPlayers().keySet()) {
                    event.getRecipients().remove(p);
                }
            }
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
