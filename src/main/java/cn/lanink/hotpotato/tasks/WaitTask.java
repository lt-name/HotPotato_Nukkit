package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoRoomStartEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.PluginTask;

import java.util.LinkedList;

public class WaitTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;

    public WaitTask(HotPotato owner, Room room) {
        super(owner);
        this.language = owner.getLanguage();
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getStatus() != 1) {
            this.cancel();
            return;
        }
        if (this.room.getPlayers().size() >= room.getMinPlayers()) {
            if (this.room.waitTime > 0) {
                this.room.waitTime--;
                if (this.room.waitTime <= 5) {
                    Tools.addSound(this.room, Sound.RANDOM_CLICK);
                }
                LinkedList<String> ms = new LinkedList<>();
                for (String string : language.waitTimeScoreBoard.split("\n")) {
                    ms.add(string.replace("%playerNumber%", room.getPlayers().size() + "")
                            .replace("%time%", room.waitTime + ""));
                }
                for (Player player : this.room.getPlayers().keySet()) {
                    player.sendTip(this.language.waitTimeBottom
                            .replace("%playerNumber%", this.room.getPlayers().size() + "")
                            .replace("%time%", this.room.waitTime + ""));
                    owner.getIScoreboard().showScoreboard(player, this.language.scoreBoardTitle, ms);
                }
            }else {
                owner.getServer().getPluginManager().callEvent(new HotPotatoRoomStartEvent(this.room));
                this.cancel();
            }
        }else if (this.room.getPlayers().size() > 0) {
            if (this.room.waitTime != this.room.getSetWaitTime()) {
                this.room.waitTime = this.room.getSetWaitTime();
            }
            LinkedList<String> ms = new LinkedList<>();
            for (String string : language.waitScoreBoard.split("\n")) {
                ms.add(string.replace("%playerNumber%", room.getPlayers().size() + ""));
            }
            for (Player player : this.room.getPlayers().keySet()) {
                player.sendTip(language.waitBottom
                        .replace("%playerNumber%", room.getPlayers().size() + ""));
                owner.getIScoreboard().showScoreboard(player, this.language.scoreBoardTitle, ms);
            }
        }else {
            this.room.endGame();
            this.cancel();
        }
    }

}
