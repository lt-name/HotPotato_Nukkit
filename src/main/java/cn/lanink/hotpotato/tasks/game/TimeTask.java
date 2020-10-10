package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoPlayerDeathEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.tasks.VictoryTask;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.PluginTask;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * 游戏时间计算
 */
public class TimeTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;

    public TimeTask(HotPotato owner, Room room) {
        super(owner);
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
                        effect.setDuration(10000);
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
                }
            }
            LinkedList<Player> playerList = new LinkedList<>();
            for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                if (entry.getValue() == 1) {
                    playerList.add(entry.getKey());
                }
            }
            if (playerList.size() > 1) {
                Player player2 = playerList.get(new Random().nextInt(playerList.size()));
                this.room.addPlaying(player2, 2);
                Tools.givePotato(player2);
            }else {
                for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                    if (entry.getValue() == 1) {
                        this.room.victoryPlayer = entry.getKey();
                        break;
                    }
                }
                this.room.setMode(3);
                Server.getInstance().getScheduler().scheduleRepeatingTask(owner,
                        new VictoryTask(owner, this.room), 20);
            }
        }
    }

    private void sendMessage(String string) {
        for (Player player : this.room.getPlayers().keySet()) {
            player.sendMessage(string);
        }
    }

}
