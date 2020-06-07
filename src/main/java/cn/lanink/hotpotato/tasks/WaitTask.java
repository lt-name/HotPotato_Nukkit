package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoRoomStartEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.PluginTask;
import tip.messages.ScoreBoardMessage;
import tip.utils.Api;

import java.util.LinkedList;

public class WaitTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;

    public WaitTask(HotPotato owner, Room room) {
        super(owner);
        owner.taskList.add(this.getTaskId());
        this.language = owner.getLanguage();
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 1) {
            this.cancel();
            return;
        }
        if (this.room.getPlayers().size() >= 3) {
            if (this.room.waitTime > 0) {
                this.room.waitTime--;
                if (this.room.waitTime <= 5) {
                    Tools.addSound(this.room, Sound.RANDOM_CLICK);
                }
                for (Player player : this.room.getPlayers().keySet()) {
                    player.sendActionBar(this.language.waitTimeBottom
                            .replace("%playerNumber%", this.room.getPlayers().size() + "")
                            .replace("%time%", this.room.waitTime + ""));
                    LinkedList<String> ms = new LinkedList<>();
                    for (String string : language.waitTimeScoreBoard.split("\n")) {
                        ms.add(string.replace("%playerNumber%", room.getPlayers().size() + "")
                                .replace("%time%", room.waitTime + ""));
                    }
                    ScoreBoardMessage score = new ScoreBoardMessage(
                            room.getLevel().getName(), true, this.language.scoreBoardTitle, ms);
                    Api.setPlayerShowMessage(player.getName(), score);
                }
            }else {
                owner.getServer().getPluginManager().callEvent(new HotPotatoRoomStartEvent(this.room));
                this.cancel();
            }
        }else if (this.room.getPlayers().size() > 0) {
            if (this.room.waitTime != this.room.getSetWaitTime()) {
                this.room.waitTime = this.room.getSetWaitTime();
            }
            for (Player player : this.room.getPlayers().keySet()) {
                player.sendActionBar(language.waitBottom
                        .replace("%playerNumber%", room.getPlayers().size() + ""));
                LinkedList<String> ms = new LinkedList<>();
                for (String string : language.waitScoreBoard.split("\n")) {
                    ms.add(string.replace("%playerNumber%", room.getPlayers().size() + ""));
                }
                ScoreBoardMessage score = new ScoreBoardMessage(
                        room.getLevel().getName(), true, this.language.scoreBoardTitle, ms);
                Api.setPlayerShowMessage(player.getName(), score);
            }
        }else {
            this.room.endGame();
            this.cancel();
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
