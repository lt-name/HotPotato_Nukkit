package cn.lanink.hotpotato.listener;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoPlayerDeathEvent;
import cn.lanink.hotpotato.event.HotPotatoRoomEndEvent;
import cn.lanink.hotpotato.event.HotPotatoRoomStartEvent;
import cn.lanink.hotpotato.event.HotPotatoTransferEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.tasks.game.ParticleTask;
import cn.lanink.hotpotato.tasks.game.TimeTask;
import cn.lanink.hotpotato.tasks.game.TipsTask;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.HugeExplodeSeedParticle;

import java.util.Map;
import java.util.Random;


/**
 * 游戏监听器（插件事件）
 * @author lt_name
 */
public class HotPotatoListener implements Listener {

    private final HotPotato hotPotato;
    private final Language language;

    public HotPotatoListener(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.language = hotPotato.getLanguage();
    }

    /**
     * 房间开始事件
     * @param event 事件
     */
    @EventHandler
    public void onRoomStart(HotPotatoRoomStartEvent event) {
        Room room = event.getRoom();
        int i = new Random().nextInt(room.getPlayers().size());
        int j = 0;
        int x = 0;
        for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
            Tools.rePlayerState(entry.getKey(), true);
            entry.getKey().getInventory().clearAll();
            entry.getKey().getUIInventory().clearAll();
            if (i == j) {
                entry.setValue(2);
                Tools.givePotato(entry.getKey());
            }else {
                entry.setValue(1);
            }
            j++;
            if (x >= room.getRandomSpawn().size()) {
                x = 0;
            }
            entry.getKey().teleport(room.getRandomSpawn().get(x));
            x++;
        }

        Server.getInstance().getScheduler().scheduleRepeatingTask(
                this.hotPotato, new TimeTask(this.hotPotato, room), 20);
        Server.getInstance().getScheduler().scheduleRepeatingTask(
                this.hotPotato, new ParticleTask(this.hotPotato, room), 5, true);
        Server.getInstance().getScheduler().scheduleRepeatingTask(
                this.hotPotato, new TipsTask(this.hotPotato, room), 18, true);
    }

    @EventHandler
    public void onRoomEnd(HotPotatoRoomEndEvent event) {
        Room room = event.getRoom();
        if (room.getPlayers().size() > 0) {
            for (Player player : room.getPlayers().keySet()) {
                if (player == room.victoryPlayer) {
                    Tools.cmd(player, this.hotPotato.getConfig().getStringList("胜利执行命令"));
                }else {
                    Tools.cmd(player, this.hotPotato.getConfig().getStringList("失败执行命令"));
                }
            }
        }
    }

    /**
     * 传递土豆事件
     * @param event 事件
     */
    @EventHandler
    public void onTransfer(HotPotatoTransferEvent event) {
        if (!event.isCancelled()) {
            Player player1 = event.getDamage();
            Player player2 = event.getPlayer();
            Room room = event.getRoom();
            room.addPlaying(player1, 1);
            player1.getInventory().clearAll();
            room.addPlaying(player2, 2);
            Tools.givePotato(player2);
        }
    }

    /**
     * 玩家死亡事件（游戏中死亡）
     * @param event 事件
     */
    @EventHandler
    public void onPlayerDeath(HotPotatoPlayerDeathEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            Room room = event.getRoom();
            player.getInventory().clearAll();
            player.setAllowModifyWorld(true);
            player.setAdventureSettings((new AdventureSettings(player)).set(AdventureSettings.Type.ALLOW_FLIGHT, true));
            player.setGamemode(3);
            room.addPlaying(player, 0);
            Tools.setPlayerInvisible(player, true);
            player.getLevel().addParticle(new HugeExplodeSeedParticle(player));
            Tools.playSound(room, Sound.RANDOM_EXPLODE);
            player.sendTitle("§l§c爆炸啦","");
        }
    }

}
