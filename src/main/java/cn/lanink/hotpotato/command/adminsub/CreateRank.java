package cn.lanink.hotpotato.command.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.lanink.hotpotato.utils.RankingManager;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

/**
 * @author LT_Name
 */
public class CreateRank extends BaseSubCommand {

    public CreateRank(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.isPlayer() && sender.isOp();
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        //TODO 多语言支持
        if (args.length < 2) {
            sender.sendMessage("请输入排行榜名称！");
            return true;
        }
        Player player = (Player) sender;
        String name = args[1];
        RankingManager.addRanking(new RankingManager.RankingData(name, player.clone()));
        sender.sendMessage("排行榜：" + name + " 创建成功！");
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] { CommandParameter.newType("RankName", CommandParamType.TEXT) };
    }
}
