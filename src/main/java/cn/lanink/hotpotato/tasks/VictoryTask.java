package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.scheduler.PluginTask;

public class VictoryTask extends PluginTask<HotPotato> {

    private final Room room;
    private int victoryTime;

    public VictoryTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
        this.victoryTime = 10;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 3) {
            this.cancel();
        }
        if (this.victoryTime < 1) {
            this.room.endGame();
            this.cancel();
        }else {
            this.victoryTime--;
            /*for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                if (entry.getValue() == 1) {
                    Tools.spawnFirework(entry.getKey());
                }
            }*/
        }
    }

}
