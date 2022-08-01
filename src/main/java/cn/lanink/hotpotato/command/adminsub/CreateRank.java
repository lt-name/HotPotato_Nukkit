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
        if (args.length < 2) {
            sender.sendMessage(this.language.needRankName);
            return true;
        }
        Player player = (Player) sender;
        String name = args[1];
        for (RankingManager.RankingData rankingData : RankingManager.getRANKING_DATA_LIST()) {
            if (rankingData.getName().equalsIgnoreCase(name)) {
                sender.sendMessage(this.language.rankNameRepeat.replace("%name%", name));
                return true;
            }
        }
        RankingManager.addRanking(new RankingManager.RankingData(name, player.clone()));
        RankingManager.save();
        RankingManager.load();
        sender.sendMessage(this.language.rankCreationSuccessful.replace("%name%", name));
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] { CommandParameter.newType("RankName", CommandParamType.TEXT) };
    }
}
