package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.scheduler.PluginTask;

import java.util.List;
import java.util.Map;

public class VictoryTask extends PluginTask<HotPotato> {

    private final String taskName = "VictoryTask";
    private final Room room;
    private int victoryTime;

    public VictoryTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
        this.victoryTime = 10;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 3) {
            this.cancel();
        }
        if (this.victoryTime < 1) {
            if (!this.room.task.contains(this.taskName)) {
                this.room.task.add(this.taskName);
                for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                    if (entry.getKey() == this.room.victoryPlayer) {
                        this.cmd(entry.getKey(), owner.getConfig().getStringList("胜利执行命令"));
                    }else {
                        this.cmd(entry.getKey(), owner.getConfig().getStringList("失败执行命令"));
                    }
                }
                this.room.endGame();
                this.room.task.remove(this.taskName);
            }
            this.cancel();
        }else {
            this.victoryTime--;
            /*for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                if (entry.getValue() == 1) {
                    Tools.spawnFirework(entry.getKey());
                }
            }*/
        }
    }

    private void cmd(Player player, List<String> cmds) {
        for (String s : cmds) {
            String[] cmd = s.split("&");
            if ((cmd.length > 1) && (cmd[1].equals("con"))) {
                owner.getServer().dispatchCommand(new ConsoleCommandSender(), cmd[0].replace("@p", player.getName()));
            } else {
                owner.getServer().dispatchCommand(player, cmd[0].replace("@p", player.getName()));
            }
        }
    }

}
