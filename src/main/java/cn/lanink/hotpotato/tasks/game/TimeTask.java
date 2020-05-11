package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoPlayerDeathEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.tasks.VictoryTask;
import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
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
        if (!this.room.task.contains(this.taskName)) {
            this.room.task.add(this.taskName);
            owner.getServer().getScheduler().scheduleAsyncTask(owner, new AsyncTask() {
                @Override
                public void onRun() {
                    int j = 0;
                    for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                        if (entry.getValue() != 0) {
                            j++;
                        }
                    }
                    if (j <= 1) {
                        for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                            if (entry.getValue() == 1) {
                                room.victoryName = entry.getKey().getName();
                                break;
                            }
                        }
                        room.setMode(3);
                        owner.getServer().getScheduler().scheduleRepeatingTask(
                                owner, new VictoryTask(owner, room), 20, true);
                    }
                    room.task.remove(taskName);
                }
            });
        }
    }

    private void sendMessage(String string) {
        for (Player player : this.room.getPlayers().keySet()) {
            player.sendMessage(string);
        }
    }

}
