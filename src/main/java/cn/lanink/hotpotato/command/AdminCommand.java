package cn.lanink.hotpotato.command;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.command.base.BaseCommand;
import cn.lanink.hotpotato.command.base.adminsub.SetSpawnCommand;
import cn.lanink.hotpotato.ui.GuiCreate;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

public class AdminCommand extends BaseCommand {

    HotPotato hotPotato = HotPotato.getInstance();

    public AdminCommand(String name) {
        super(name, "HotPotato 管理命令");
        this.setPermission("HotPotato.op");
        this.addSubCommand(new SetSpawnCommand("setspawn"));

        this.loadCommandBase();
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            if (player.isOp()) {
                if (strings.length > 0) {
                    switch (strings[0]) {
                        case "setwaittime":
                            if (strings.length == 2) {
                                if (strings[1].matches("[0-9]*")) {
                                    hotPotato.roomSetWaitTime(Integer.valueOf(strings[1]), hotPotato.getRoomConfig(player.getLevel()));
                                    commandSender.sendMessage("§a等待时间已设置为：" + Integer.valueOf(strings[1]));
                                }else {
                                    commandSender.sendMessage("§a时间只能设置为正整数！");
                                }
                            }else {
                                commandSender.sendMessage("§a查看帮助：/" + getName() + " help");
                            }
                            return true;
                        case "setgametime":
                            if (strings.length == 2) {
                                if (strings[1].matches("[0-9]*")) {
                                    if (Integer.parseInt(strings[1]) > 5) {
                                        hotPotato.roomSetGameTime(Integer.valueOf(strings[1]), hotPotato.getRoomConfig(player.getLevel()));
                                        commandSender.sendMessage("§a爆炸等待时间已设置为：" + Integer.valueOf(strings[1]));
                                    } else {
                                        commandSender.sendMessage("§a爆炸等待时间最小不能低于5秒！");
                                    }
                                }else {
                                    commandSender.sendMessage("§a时间只能设置为正整数！");
                                }
                            }else {
                                commandSender.sendMessage("§a查看帮助：/" + getName() + " help");
                            }
                            return true;
                        case "reload": case "重载":
                            hotPotato.reLoadRooms();
                            commandSender.sendMessage("§a配置重载完成！请在后台查看信息！");
                            return true;
                        case "unload":
                            hotPotato.unloadRooms();
                            commandSender.sendMessage("§a已卸载所有房间！请在后台查看信息！");
                            return true;
                        default:

                            return true;
                    }
                }else {
                    GuiCreate.sendAdminMenu(player);
                    return true;
                }
            }else {
                commandSender.sendMessage("§c你没有权限！");
                return true;
            }
        }else {
            if(strings.length > 0 && strings[0].equals("reload")) {
                hotPotato.reLoadRooms();
                commandSender.sendMessage("§a配置重载完成！");
                return true;
            }else if(strings.length > 0 && strings[0].equals("unload")) {
                hotPotato.unloadRooms();
                commandSender.sendMessage("§a已卸载所有房间！");
                return true;
            }else {
                commandSender.sendMessage("§a请在游戏内输入！");
            }
            return true;
        }
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§eHotPotato--命令帮助");
        sender.sendMessage("§a/" + getName() + " §e打开ui");
        sender.sendMessage("§a/" + getName() + " setspawn §e设置当前位置为出生点");
        sender.sendMessage("§a/" + getName() + " setwaittime 数字 §e设置游戏人数足够后的等待时间");
        sender.sendMessage("§a/" + getName() + " setgametime 数字 §e设置爆炸等待时间");
        sender.sendMessage("§a/" + getName() + " reload §e重载所有房间");
        sender.sendMessage("§a/" + getName() + " unload §e关闭所有房间,卸载配置");
    }

}
