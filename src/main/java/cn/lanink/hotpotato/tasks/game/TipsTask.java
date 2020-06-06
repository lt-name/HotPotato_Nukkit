package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import tip.messages.ScoreBoardMessage;
import tip.utils.Api;

import java.util.LinkedList;


/**
 * 信息显示
 */
public class TipsTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;

    public TipsTask(HotPotato owner, Room room) {
        super(owner);
        owner.taskList.add(this.getTaskId());
        this.language = owner.getLanguage();
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 2) {
            this.cancel();
            return;
        }
        if (room.getPlayers().values().size() > 0) {
            int playerNumber = 0;
            for (Integer integer : this.room.getPlayers().values()) {
                if (integer != 0) {
                    playerNumber++;
                }
            }
            LinkedList<String> ms = new LinkedList<>();
            for (String string : language.gameTimeScoreBoard.split("\n")) {
                ms.add(string.replace("%time%", this.room.gameTime + "")
                        .replace("%playerNumber%", playerNumber + ""));
            }
            ScoreBoardMessage score = new ScoreBoardMessage(
                    room.getLevel().getName(), true, this.language.scoreBoardTitle, ms);
            for (Player player : this.room.getPlayers().keySet()) {
                player.sendTip(this.language.gameTimeBottom.replace("%time%", this.room.gameTime + "")
                        .replace("%playerNumber%", playerNumber + ""));
                Api.setPlayerShowMessage(player.getName(), score);
            }
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
