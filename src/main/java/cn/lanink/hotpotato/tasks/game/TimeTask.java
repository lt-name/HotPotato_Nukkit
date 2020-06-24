package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoPlayerDeathEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.PluginTask;

import java.util.Map;

/**
 * 游戏时间计算
 */
public class TimeTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;

    public TimeTask(HotPotato owner, Room room) {
        super(owner);
        owner.taskList.add(this.getTaskId());
        this.language = owner.getLanguage();
        this.room = room;
    }

    public void onRun(int i) {
        if (this.room.getMode() != 2) {
            this.cancel();
            return;
        }
        if (this.room.gameTime > 0) {
            this.room.gameTime--;
            for (Map.Entry<Player, Integer> entry : this.room.getPlayers().entrySet()) {
                if (entry.getValue() == 2) {
                    Effect effect = entry.getKey().getEffect(1);
                    if (effect == null) {
                        effect = Effect.getEffect(1);
                        effect.setVisible(false);
                        effect.setDuration(100);
                        effect.setAmplifier(2);
                        entry.getKey().addEffect(effect);
                    }
                }else {
                    entry.getKey().removeAllEffects();
                }
            }
        }else {
            this.room.gameTime = this.room.getSetGameTime();
            for (Map.Entry<Player, Integer> entry : this.room.getPlayers().entrySet()) {
                if (entry.getValue() == 2) {
                    owner.getServer().getPluginManager().callEvent(new HotPotatoPlayerDeathEvent(this.room, entry.getKey()));
                    this.sendMessage(this.language.playerDeath.replace("%player%", entry.getKey().getName()));
                    break;
                }
            }
        }
    }

    private void sendMessage(String string) {
        for (Player player : this.room.getPlayers().keySet()) {
            player.sendMessage(string);
        }
    }

    @Override
    public void cancel() {
        while (owner.taskList.contains(this.getTaskId())) {
            owner.taskList.remove(this.getTaskId());
        }
        super.cancel();
    }

}
