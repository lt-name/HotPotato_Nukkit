package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoPlayerDeathEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.tasks.VictoryTask;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;

import java.util.Map;

/**
 * 游戏时间计算
 */
public class TimeTask extends PluginTask<HotPotato> {

    private final String taskName = "TimeTask";
    private final Room room;

    public TimeTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
    }

    public void onRun(int i) {
        if (this.room.getMode() != 2) {
            this.cancel();
        }
        if (this.room.gameTime > 0) {
            this.room.gameTime--;
        }else {
            this.room.gameTime = this.room.getSetGameTime();
            for (Map.Entry<Player, Integer> entry : this.room.getPlayers().entrySet()) {
                if (entry.getValue() == 2) {
                    owner.getServer().getPluginManager().callEvent(new HotPotatoPlayerDeathEvent(this.room, entry.getKey()));
                    this.sendMessage("§c" + entry.getKey().getName() + " 爆炸了！");
                }
            }
        }
        int j = 0;
        for (Map.Entry<Player, Integer> entry : this.room.getPlayers().entrySet()) {
            if (entry.getValue() == 1) {
                j++;
            }
        }
/*        if (j <= 1) {
            for (Map.Entry<Player, Integer> entry : this.room.getPlayers().entrySet()) {
                if (entry.getValue() == 1) {
                    this.room.victoryName = entry.getKey().getName();
                    break;
                }
            }
            this.room.setMode(3);
            owner.getServer().getScheduler().scheduleRepeatingTask(
                    owner, new VictoryTask(owner, this.room), 20, true);
        }*/
    }

    private void sendMessage(String string) {
        for (Player player : this.room.getPlayers().keySet()) {
            player.sendMessage(string);
        }
    }

}