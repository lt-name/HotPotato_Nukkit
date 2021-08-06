package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.RedstoneParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.PluginTask;

import java.util.Map;

public class ParticleTask extends PluginTask<HotPotato> {

    private final Room room;

    public ParticleTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getStatus() != 2) {
            this.cancel();
            return;
        }
        for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
            if (entry.getValue() == 2) {
                Player player = entry.getKey();
                Level level = player.getLevel();
                //四个方向粒子
                for (int y1 = 0; y1 < 3; y1++) {
                    Vector3 vector3 = new Vector3(player.x + 1, player.y + y1, player.z + 1);
                    level.addParticle(new RedstoneParticle(vector3));
                    vector3 = new Vector3(player.x + 1, player.y + y1, player.z - 1);
                    level.addParticle(new RedstoneParticle(vector3));
                    vector3 = new Vector3(player.x - 1, player.y + y1, player.z - 1);
                    level.addParticle(new RedstoneParticle(vector3));
                    vector3 = new Vector3(player.x - 1, player.y + y1, player.z + 1);
                    level.addParticle(new RedstoneParticle(vector3));
                }
            }
        }
    }

}
