package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.scheduler.PluginTask;
import tip.messages.ScoreBoardMessage;
import tip.utils.Api;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VictoryTask extends PluginTask<HotPotato> {

    private final Language language;
    private final Room room;
    private int victoryTime;

    public VictoryTask(HotPotato owner, Room room) {
        super(owner);
        owner.taskList.add(this.getTaskId());
        this.language = owner.getLanguage();
        this.room = room;
        this.victoryTime = 10;
        LinkedList<String> ms = new LinkedList<>();
        ms.add(this.language.victoryMessage.replace("%player%", room.victoryPlayer.getName()));
        ScoreBoardMessage score = new ScoreBoardMessage(
                room.getLevel().getName(), true, this.language.scoreBoardTitle, ms);
        for (Player player : this.room.getPlayers().keySet()) {
            Api.setPlayerShowMessage(player.getName(), score);
        }
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 3) {
            this.cancel();
        }
        if (this.victoryTime < 1) {
            if (room.getPlayers().size() > 0) {
                for (Player player : room.getPlayers().keySet()) {
                    if (player == this.room.victoryPlayer) {
                        this.cmd(player, owner.getConfig().getStringList("胜利执行命令"));
                    }else {
                        this.cmd(player, owner.getConfig().getStringList("失败执行命令"));
                    }
                }
            }
            this.room.endGame();
            this.cancel();
        }else {
            this.victoryTime--;
            if (room.getPlayers().size() > 0) {
                for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                    if (entry.getValue() == 1) {
                        Tools.spawnFirework(entry.getKey());
                    }
                    entry.getKey().sendActionBar(
                            this.language.victoryMessage.replace("%player%", room.victoryPlayer.getName()));
                }
            }
        }
    }

    private void cmd(Player player, List<String> cmds) {
        if (player == null || cmds == null || cmds.size() < 1) {
            return;
        }
        for (String s : cmds) {
            String[] cmd = s.split("&");
            if ((cmd.length > 1) && (cmd[1].equals("con"))) {
                owner.getServer().dispatchCommand(new ConsoleCommandSender(), cmd[0].replace("@p", player.getName()));
            } else {
                owner.getServer().dispatchCommand(player, cmd[0].replace("@p", player.getName()));
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
