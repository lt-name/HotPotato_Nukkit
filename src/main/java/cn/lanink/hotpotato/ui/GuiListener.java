package cn.lanink.hotpotato.ui;

import cn.lanink.hotpotato.HotPotato;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;

public class GuiListener implements Listener {

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
        String uName = HotPotato.getInstance().getConfig().getString("插件命令", "hotpotato");
        String aName = HotPotato.getInstance().getConfig().getString("管理命令", "hotpotatoadmin");
        if (event.getWindow() instanceof FormWindowSimple) {
            FormWindowSimple simple = (FormWindowSimple) event.getWindow();
            if (event.getFormID() == GuiCreate.USER_MENU) {
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
            }else if (event.getFormID() == GuiCreate.ROOM_LIST_MENU) {
                if (simple.getResponse().getClickedButton().getText().equals("§c返回")) {
                    GuiCreate.sendUserMenu(player);
                }else {
                    GuiCreate.sendRoomJoinOkMenu(player, simple.getResponse().getClickedButton().getText());
                }
            }else if (event.getFormID() == GuiCreate.ADMIN_MENU) {
                switch (simple.getResponse().getClickedButtonId()) {
                    case 0:
                        HotPotato.getInstance().getServer().dispatchCommand(player, aName + " setspawn");
                        break;
                    case 1:
                        GuiCreate.sendAdminTimeMenu(player);
                        break;
                    case 2:
                        HotPotato.getInstance().getServer().dispatchCommand(player, aName + " reload");
                        break;
                    case 3:
                        HotPotato.getInstance().getServer().dispatchCommand(player, aName + " unload");
                        break;
                }
            }
        }else if (event.getWindow() instanceof FormWindowCustom) {
            FormWindowCustom custom = (FormWindowCustom) event.getWindow();
            if (event.getFormID() == GuiCreate.ADMIN_TIME_MENU) {
                HotPotato.getInstance().getServer().dispatchCommand(player, aName + " setwaittime " + custom.getResponse().getInputResponse(0));
                HotPotato.getInstance().getServer().dispatchCommand(player, aName + " setgametime " + custom.getResponse().getInputResponse(1));
            }
        }else if (event.getWindow() instanceof FormWindowModal) {
            FormWindowModal modal = (FormWindowModal) event.getWindow();
            if (event.getFormID() == GuiCreate.ROOM_JOIN_OK) {
                if (modal.getResponse().getClickedButtonId() == 0 && !modal.getButton1().equals("§c返回")) {
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