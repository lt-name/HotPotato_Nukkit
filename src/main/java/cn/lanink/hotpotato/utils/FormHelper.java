package cn.lanink.hotpotato.utils;

import cn.lanink.gamecore.form.element.ResponseElementButton;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;

import java.util.Map;


public class FormHelper {

    public static final String PLUGIN_NAME = "§l§7[§1H§2o§3t§4P§5o§6t§aa§ct§bo§7]";

    /**
     * 显示用户菜单
     * @param player 玩家
     */
    public static void sendUserMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(PLUGIN_NAME);

        simple.addButton(new ResponseElementButton(language.userMenuButton1, new ElementButtonImageData("path", "textures/ui/switch_start_button"))
                .onClicked(cp -> HotPotato.getInstance().getServer().dispatchCommand(cp, HotPotato.getInstance().getCmdUser() + " join"))
        );
        simple.addButton(new ResponseElementButton(language.userMenuButton2, new ElementButtonImageData("path", "textures/ui/switch_select_button"))
                .onClicked(cp -> HotPotato.getInstance().getServer().dispatchCommand(cp, HotPotato.getInstance().getCmdUser() + " quit"))
        );
        simple.addButton(new ResponseElementButton(language.userMenuButton3, new ElementButtonImageData("path", "textures/ui/servers"))
                .onClicked(FormHelper::sendRoomListMenu)
        );

        player.showFormWindow(simple);
    }

    /**
     * 显示管理菜单
     * @param player 玩家
     */
    public static void sendAdminMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(PLUGIN_NAME, language.adminMenuSetLevel.replace("%name%", player.getLevel().getName()));

        simple.addButton(new ResponseElementButton(language.adminMenuButton1, new ElementButtonImageData("path", "textures/ui/World"))
                .onClicked(cp -> HotPotato.getInstance().getServer().dispatchCommand(cp, HotPotato.getInstance().getCmdAdmin() + " setwaitspawn"))
        );
        simple.addButton(new ResponseElementButton(language.adminMenuButton2, new ElementButtonImageData("path", "textures/ui/World"))
                .onClicked(cp -> HotPotato.getInstance().getServer().dispatchCommand(player, HotPotato.getInstance().getCmdAdmin() + " addrandomspawn"))
        );
        simple.addButton(new ResponseElementButton(language.adminMenuButton3, new ElementButtonImageData("path", "textures/ui/timer"))
                .onClicked(FormHelper::sendAdminTimeMenu)
        );
        simple.addButton(new ResponseElementButton(language.adminMenuButton4,  new ElementButtonImageData("path", "textures/ui/refresh_light"))
                .onClicked(cp -> HotPotato.getInstance().getServer().dispatchCommand(player, HotPotato.getInstance().getCmdAdmin() + " reloadroom"))
        );
        simple.addButton(new ResponseElementButton(language.adminMenuButton5, new ElementButtonImageData("path", "textures/ui/redX1"))
                .onClicked(cp -> HotPotato.getInstance().getServer().dispatchCommand(player, HotPotato.getInstance().getCmdAdmin() + " unloadroom"))
        );

        player.showFormWindow(simple);
    }

    /**
     * 显示设置时间菜单
     * @param player 玩家
     */
    public static void sendAdminTimeMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(PLUGIN_NAME);

        custom.addElement(new ElementInput(language.adminTimeMenuInputText1, "", "60"));
        custom.addElement(new ElementInput(language.adminTimeMenuInputText2, "", "20"));

        custom.onResponded((formResponseCustom, cp) -> {
            HotPotato.getInstance().getServer().dispatchCommand(cp, HotPotato.getInstance().getCmdAdmin() + " setwaittime " + formResponseCustom.getInputResponse(0));
            HotPotato.getInstance().getServer().dispatchCommand(cp, HotPotato.getInstance().getCmdAdmin() + " setgametime " + formResponseCustom.getInputResponse(1));
        });

        player.showFormWindow(custom);
    }

    /**
     * 显示房间列表菜单
     * @param player 玩家
     */
    public static void sendRoomListMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(PLUGIN_NAME, "");

        for (Map.Entry<String, Room> entry : HotPotato.getInstance().getRooms().entrySet()) {
            simple.addButton(new ResponseElementButton("§e" + entry.getKey() +
                    "\nPlayer: " + entry.getValue().getPlayers().size() + "/" + entry.getValue().getMaxPlayers(),
                    new ElementButtonImageData("path", "textures/ui/switch_start_button"))
                    .onClicked(cp -> sendRoomJoinOkMenu(cp, entry.getKey()))
            );
        }
        simple.addButton(new ResponseElementButton(language.buttonReturn, new ElementButtonImageData("path", "textures/ui/cancel"))
                .onClicked(FormHelper::sendUserMenu)
        );

        player.showFormWindow(simple);
    }

    /**
     * 加入房间确认(自选)
     * @param player 玩家
     */
    public static void sendRoomJoinOkMenu(Player player, String roomName) {
        Language language = HotPotato.getInstance().getLanguage();
        if (HotPotato.getInstance().getRooms().containsKey(roomName)) {
            Room room = HotPotato.getInstance().getRooms().get(roomName);
            if (room.getStatus() == 2 || room.getStatus() == 3) {
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                        PLUGIN_NAME, language.joinRoomIsPlaying, language.buttonReturn, language.buttonReturn);

                modal.onClickedTrue(FormHelper::sendRoomListMenu);
                modal.onClickedFalse(FormHelper::sendRoomListMenu);

                player.showFormWindow(modal);
            }else if (room.getPlayers().size() >= room.getMaxPlayers()) {
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                        PLUGIN_NAME, language.joinRoomIsFull, language.buttonReturn, language.buttonReturn);

                modal.onClickedTrue(FormHelper::sendRoomListMenu);
                modal.onClickedFalse(FormHelper::sendRoomListMenu);

                player.showFormWindow(modal);
            }else {
                AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                        PLUGIN_NAME, language.joinRoomOK.replace("%name%", "\"" + roomName + "\""), language.buttonOK, language.buttonReturn);

                modal.onClickedTrue(cp -> HotPotato.getInstance().getServer().dispatchCommand(cp, HotPotato.getInstance().getCmdUser() + " join " + roomName));
                modal.onClickedFalse(FormHelper::sendRoomListMenu);

                player.showFormWindow(modal);
            }
        }else {
            AdvancedFormWindowModal modal = new AdvancedFormWindowModal(
                    PLUGIN_NAME, language.joinRoomIsNotFound, language.buttonReturn, language.buttonReturn);

            modal.onClickedTrue(FormHelper::sendRoomListMenu);
            modal.onClickedFalse(FormHelper::sendRoomListMenu);

            player.showFormWindow(modal);
        }
    }

}
