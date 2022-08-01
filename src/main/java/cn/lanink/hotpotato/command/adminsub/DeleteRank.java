package cn.lanink.hotpotato.command.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.lanink.hotpotato.utils.RankingManager;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

import java.util.Iterator;

/**
 * @author LT_Name
 */
public class DeleteRank extends BaseSubCommand {

    public DeleteRank(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.isOp();
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
        String name = args[1];
        Iterator<RankingManager.RankingData> iterator = RankingManager.getRANKING_DATA_LIST().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(name)) {
                iterator.remove();
                sender.sendMessage(this.language.rankDeleteSuccessful.replace("%name%", name));
                RankingManager.save();
                RankingManager.load();
                return true;
            }
        }
        sender.sendMessage(this.language.rankNotFound.replace("%name%", name));
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] { CommandParameter.newType("RankName", CommandParamType.TEXT) };
    }

}
