package cn.lanink.hotpotato.command.base;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SmallasWater
 */
public abstract class BaseCommand extends Command {

    private final ArrayList<BaseSubCommand> subCommand = new ArrayList<>();
    private final ConcurrentHashMap<String, Integer> subCommands = new ConcurrentHashMap<>();
    protected HotPotato hotPotato = HotPotato.getInstance();
    protected Language language = hotPotato.getLanguage();

    public BaseCommand(String name, String description) {
        super(name, description);
    }

    /**
     * 判断权限
     * @param sender 玩家
     * @return 是否拥有权限
     */
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(this.getPermission());
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(hasPermission(sender)) {
            if(args.length > 0) {
                String subCommand = args[0].toLowerCase();
                if (subCommands.containsKey(subCommand)) {
                    BaseSubCommand command = this.subCommand.get(this.subCommands.get(subCommand));
                    if (command.canUser(sender)) {
                        return command.execute(sender, s, args);
                    }else if (sender.isPlayer()) {
                        sender.sendMessage(this.language.noPermission);
                    }else {
                        sender.sendMessage(this.language.useCmdInCon);
                    }
                }else {
                    this.sendHelp(sender);
                }
            }else {
                if (sender.isPlayer()) {
                    this.sendUI(sender);
                }else {
                    this.sendHelp(sender);
                }
            }
            return true;
        }
        sender.sendMessage(this.language.noPermission);
        return true;
    }

    /**
     * 发送帮助
     * @param sender 玩家
     * */
    public abstract void sendHelp(CommandSender sender);

    /**
     * 发送UI
     * @param sender 玩家
     */
    public abstract void sendUI(CommandSender sender);

    protected void addSubCommand(BaseSubCommand cmd) {
        this.subCommand.add(cmd);
        int commandId = (this.subCommand.size()) - 1;
        this.subCommands.put(cmd.getName().toLowerCase(), commandId);
        for (String alias : cmd.getAliases()) {
            this.subCommands.put(alias.toLowerCase(), commandId);
        }
    }

    protected void loadCommandBase(){
        this.commandParameters.clear();
        for(BaseSubCommand subCommand : this.subCommand){
            LinkedList<CommandParameter> parameters = new LinkedList<>();
            parameters.add(CommandParameter.newEnum(subCommand.getName(), new String[]{subCommand.getName()}));
            parameters.addAll(Arrays.asList(subCommand.getParameters()));
            this.commandParameters.put(subCommand.getName(),parameters.toArray(new CommandParameter[0]));
        }
    }

}
