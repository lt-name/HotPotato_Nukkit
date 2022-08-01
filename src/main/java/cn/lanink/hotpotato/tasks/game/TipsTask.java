package cn.lanink.hotpotato.tasks.game;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.BossBarColor;
import cn.nukkit.utils.DummyBossBar;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 信息显示
 */
public class TipsTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;

    private final ConcurrentHashMap<Player, DummyBossBar> bossBarMap = new ConcurrentHashMap<>();

    public TipsTask(HotPotato owner, Room room) {
        super(owner);
        this.language = owner.getLanguage();
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getStatus() != 2) {
            this.cancel();
            return;
        }
        if (room.getPlayers().size() > 0) {
            int playerNumber = 0;
            for (Integer integer : this.room.getPlayers().values()) {
                if (integer != 0) {
                    playerNumber++;
                }
            }
            LinkedList<String> ms = new LinkedList<>();
            for (String string : language.gameTimeScoreBoard.split("\n")) {
                ms.add(string.replace("%time%", this.room.gameTime + "")
                        .replace("%playerNumber%", playerNumber + "")
                        .replace("%round%", this.room.roundCount + 1 +""));
            }
            for (Player player : this.room.getPlayers().keySet()) {
                if (!this.bossBarMap.containsKey(player)) {
                    DummyBossBar.Builder builder = new DummyBossBar.Builder(player);
                    try {
                        Class.forName("cn.nukkit.utils.BossBarColor");
                        builder.color(BossBarColor.RED);
                    }catch (Exception ignored) {

                    }
                    this.bossBarMap.put(player, builder.build());
                }
                DummyBossBar bossBar = this.bossBarMap.get(player);
                if (!player.getDummyBossBars().containsKey(bossBar.getBossBarId())) {
                    //player.createBossBar(bossBar);
                }
                bossBar.setLength(((this.room.gameTime * 1.0F) / this.room.getSetGameTime()) * 100);
                bossBar.setText("§l§c" + this.room.gameTime);
                player.sendActionBar(this.language.gameTimeBottom.replace("%time%", this.room.gameTime + "")
                        .replace("%playerNumber%", playerNumber + ""));
                owner.getIScoreboard().showScoreboard(player, this.language.scoreBoardTitle, ms);
            }
        }
    }

    @Override
    public void onCancel() {
        for (Map.Entry<Player, DummyBossBar> entry : this.bossBarMap.entrySet()) {
            entry.getKey().removeBossBar(entry.getValue().getBossBarId());
        }
        this.bossBarMap.clear();
    }

}
