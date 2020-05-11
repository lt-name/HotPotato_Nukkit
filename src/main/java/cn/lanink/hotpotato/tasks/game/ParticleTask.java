package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.RedstoneParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;

import java.util.Map;

public class ParticleTask extends PluginTask<HotPotato> {

    private final String taskName = "ParticleTask";
    private final Room room;

    public ParticleTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 2) {
            this.cancel();
        }
        if (!this.room.task.contains(this.taskName)) {
            this.room.task.add(this.taskName);
            owner.getServer().getScheduler().scheduleAsyncTask(owner, new AsyncTask() {
                @Override
                public void onRun() {
                    for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                        if (entry.getValue() == 2) {
                            Player player = entry.getKey();
                            Level level = player.getLevel();
                            //四个方向粒子
                            Vector3 vector3 = new Vector3(player.x + 1, player.y + 1, player.z + 1);
                            level.addParticle(new RedstoneParticle(vector3));
                            vector3 = new Vector3(player.x + 1, player.y + 1, player.z - 1);
                            level.addParticle(new RedstoneParticle(vector3));
                            vector3 = new Vector3(player.x - 1, player.y + 1, player.z - 1);
                            level.addParticle(new RedstoneParticle(vector3));
                            vector3 = new Vector3(player.x - 1, player.y + 1, player.z + 1);
                            level.addParticle(new RedstoneParticle(vector3));
                        }
                    }
                    room.task.remove(taskName);
                }
            });
        }
    }
}