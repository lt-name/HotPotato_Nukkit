package cn.lanink.hotpotato.ui;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;

public class GuiListener implements Listener {

    private final HotPotato hotPotato;
    private final Language language;

    public GuiListener(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.language = hotPotato.getLanguage();
    }

    /**
     * 玩家操作ui事件
     * 直接执行现有命令，减小代码重复量，也便于维护
     * @param event 事件
     */
    @EventHandler
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getWindow() == null || event.getResponse() == null) {
            return;
        }
        GuiType cache = GuiCreate.UI_CACHE.containsKey(player) ? GuiCreate.UI_CACHE.get(player).get(event.getFormID()) : null;
        if (cache == null) {
            return;
        }
        GuiCreate.UI_CACHE.get(player).remove(event.getFormID());
        String uName = this.hotPotato.getCmdUser();
        String aName = this.hotPotato.getCmdAdmin();
        if (event.getWindow() instanceof FormWindowSimple) {
            FormWindowSimple simple = (FormWindowSimple) event.getWindow();
            if (cache == GuiType.USER_MENU) {
                switch (simple.getResponse().getClickedButtonId()) {
                    case 0:
                        HotPotato.getInstance().getServer().dispatchCommand(player, uName + " join");
                        break;
                    case 1:
                        HotPotato.getInstance().getServer().dispatchCommand(player, uName + " quit");
                        break;
                    case 2:
                        GuiCreate.sendRoomListMenu(player);
                        break;
                }
            }else if (cache == GuiType.ROOM_LIST_MENU) {
                if (simple.getResponse().getClickedButton().getText().equals(language.buttonReturn)) {
                    GuiCreate.sendUserMenu(player);
                }else {
                    GuiCreate.sendRoomJoinOkMenu(player,
                            simple.getResponse().getClickedButton().getText().split("\n")[0]);
                }
            }else if (cache == GuiType.ADMIN_MENU) {
                switch (simple.getResponse().getClickedButtonId()) {
                    case 0:
                        HotPotato.getInstance().getServer().dispatchCommand(player, aName + " setwaitspawn");
                        break;
                    case 1:
                        HotPotato.getInstance().getServer().dispatchCommand(player, aName + " addrandomspawn");
                        break;
                    case 2:
                        GuiCreate.sendAdminTimeMenu(player);
                        break;
                    case 3:
                        HotPotato.getInstance().getServer().dispatchCommand(player, aName + " reloadroom");
                        break;
                    case 4:
                        HotPotato.getInstance().getServer().dispatchCommand(player, aName + " unloadroom");
                        break;
                }
            }
        }else if (event.getWindow() instanceof FormWindowCustom) {
            FormWindowCustom custom = (FormWindowCustom) event.getWindow();
            if (cache == GuiType.ADMIN_TIME_MENU) {
                HotPotato.getInstance().getServer().dispatchCommand(player, aName + " setwaittime " + custom.getResponse().getInputResponse(0));
                HotPotato.getInstance().getServer().dispatchCommand(player, aName + " setgametime " + custom.getResponse().getInputResponse(1));
            }
        }else if (event.getWindow() instanceof FormWindowModal) {
            FormWindowModal modal = (FormWindowModal) event.getWindow();
            if (cache == GuiType.ROOM_JOIN_OK) {
                if (modal.getResponse().getClickedButtonId() == 0 && !modal.getButton1().equals(language.buttonReturn)) {
                    String[] s = modal.getContent().split("\"");
                    HotPotato.getInstance().getServer().dispatchCommand(
                            player, uName + " join " + s[1].replace("§e", "").trim());
                }else {
                    GuiCreate.sendRoomListMenu(player);
                }
            }
        }
    }

}