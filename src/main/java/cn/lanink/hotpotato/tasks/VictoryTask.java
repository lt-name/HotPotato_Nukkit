package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoRoomEndEvent;
import cn.lanink.hotpotato.player.PlayerDataManager;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.room.RoomStatus;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;

import java.util.LinkedList;
import java.util.Map;

public class VictoryTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;
    private int victoryTime;

    public VictoryTask(HotPotato owner, Room room) {
        super(owner);
        this.language = owner.getLanguage();
        this.room = room;
        this.victoryTime = 10;
        LinkedList<String> ms = new LinkedList<>();
        ms.add(this.language.victoryMessage.replace("%player%", room.victoryPlayer.getName()));
        for (Player player : this.room.getPlayers().keySet()) {
            owner.getIScoreboard().showScoreboard(player, this.language.scoreBoardTitle, ms);
        }

        if (this.room.victoryPlayer != null) {
            PlayerDataManager.getData(this.room.victoryPlayer).addVictoryCount();
        }
    }

    @Override
    public void onRun(int i) {
        if (this.room.getStatus() != RoomStatus.VICTORY) {
            this.cancel();
            return;
        }
        if (this.victoryTime < 1) {
            owner.getServer().getPluginManager().callEvent(new HotPotatoRoomEndEvent(this.room, this.room.victoryPlayer));
            this.room.endGame();
            this.cancel();
        }else {
            this.victoryTime--;
            if (room.getPlayers().size() > 0) {
                for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                    if (entry.getValue() == 1) {
                        Tools.spawnFirework(entry.getKey());
                    }
                    entry.getKey().sendTip(this.language.victoryMessage
                            .replace("%player%", room.victoryPlayer.getName()));
                }
            }
        }
    }

}
