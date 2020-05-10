package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import tip.messages.ScoreBoardMessage;
import tip.messages.TipMessage;
import tip.utils.Api;

import java.util.LinkedList;


/**
 * 信息显示
 */
public class TipsTask extends PluginTask<HotPotato> {

    private final String taskName = "TipsTask";
    private final Room room;
    private final boolean bottom, scoreBoard;
    private TipMessage bottomMessage;
    private ScoreBoardMessage scoreBoardMessage;

    public TipsTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
        this.bottom = owner.getConfig().getBoolean("底部显示信息", true);
        this.scoreBoard = owner.getConfig().getBoolean("计分板显示信息", false);
        this.bottomMessage = new TipMessage(room.getLevel().getName(), true, 0, null);
        this.scoreBoardMessage = new ScoreBoardMessage(
                room.getLevel().getName(), true, "§e烫手山芋", new LinkedList<>());
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() == 0) {
            this.cancel();
        }
        if (!this.room.task.contains(this.taskName)) {
            this.room.task.add(this.taskName);
            owner.getServer().getScheduler().scheduleAsyncTask(HotPotato.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    if (room.getPlayers().values().size() > 0) {
                        if (room.getMode() == 1) {
                            if (room.getPlayers().values().size() >= 3) {
                                bottomMessage.setMessage("§a当前已有: " + room.getPlayers().size() + " 位玩家" +
                                        "\n§a游戏还有: " + room.waitTime + " 秒开始！");
                                LinkedList<String> ms = new LinkedList<>();
                                ms.add("玩家: §a" + room.getPlayers().size() + "/16 ");
                                ms.add("§a开始倒计时： §l§e" + room.waitTime + " ");
                                scoreBoardMessage.setMessages(ms);
                            }else {
                                bottomMessage.setMessage("§c等待玩家加入中,当前已有: " + room.getPlayers().size() + " 位玩家");
                                LinkedList<String> ms = new LinkedList<>();
                                ms.add("玩家: §a" + room.getPlayers().size() + "/16 ");
                                ms.add("最低游戏人数为 3 人 ");
                                ms.add("等待玩家加入中 ");
                                scoreBoardMessage.setMessages(ms);
                            }
                            this.sendMessage();
                        }else if (room.getMode() == 2) {
                            int playerNumber = 0;
                            for (Integer integer : room.getPlayers().values()) {
                                if (integer != 0) {
                                    playerNumber++;
                                }
                            }
                            for (Player player : room.getPlayers().keySet()) {
                                if (bottom) {
                                    TipMessage tip = new TipMessage(room.getLevel().getName(), true, 0, null);
                                    tip.setMessage("§l§a倒计时： §e" + room.gameTime + " §a秒 \n" +
                                            "§l§a存活人数：" + playerNumber);
                                    Api.setPlayerShowMessage(player.getName(), tip);
                                }
                                if (scoreBoard) {
                                    ScoreBoardMessage score = new ScoreBoardMessage(
                                            room.getLevel().getName(), true, "§eGunWar", new LinkedList<>());
                                    LinkedList<String> ms = new LinkedList<>();
                                    ms.add("§l§a倒计时： §e" + room.gameTime + " §a秒 ");
                                    ms.add("§l§a存活人数：" + playerNumber);
                                    score.setMessages(ms);
                                    Api.setPlayerShowMessage(player.getName(), score);
                                }
                            }
                        }else if (room.getMode() == 3) {
                            bottomMessage.setMessage("§e胜利者：" + room.victoryName);
                            LinkedList<String> ms = new LinkedList<>();
                            ms.add("§e胜利者：" + room.victoryName);
                            scoreBoardMessage.setMessages(ms);
                            this.sendMessage();
                        }
                    }
                    room.task.remove(taskName);
                }

                private void sendMessage() {
                    for (Player player : room.getPlayers().keySet()) {
                        if (bottom) {
                            Api.setPlayerShowMessage(player.getName(), bottomMessage);
                        }
                        if (scoreBoard) {
                            Api.setPlayerShowMessage(player.getName(), scoreBoardMessage);
                        }
                    }
                }

            });
        }
    }

}
