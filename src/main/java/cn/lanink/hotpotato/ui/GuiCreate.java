package cn.lanink.hotpotato.ui;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;

import java.util.Map;


public class GuiCreate {

    public static final String PLUGIN_NAME = "§l§7[§1H§2o§3t§4P§5o§6t§aa§ct§bo§7]";
    public static final int USER_MENU = 1858874311;
    public static final int ADMIN_MENU = 1858874312;
    public static final int ADMIN_TIME_MENU = 1858874313;
    public static final int ROOM_LIST_MENU = 1858874314;
    public static final int ROOM_JOIN_OK = 1858874315;

    /**
     * 显示用户菜单
     * @param player 玩家
     */
    public static void sendUserMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        FormWindowSimple simple = new FormWindowSimple(PLUGIN_NAME, "");
        simple.addButton(new ElementButton(language.userMenuButton1, new ElementButtonImageData("path", "textures/ui/switch_start_button")));
        simple.addButton(new ElementButton(language.userMenuButton2, new ElementButtonImageData("path", "textures/ui/switch_select_button")));
        simple.addButton(new ElementButton(language.userMenuButton3, new ElementButtonImageData("path", "textures/ui/servers")));
        player.showFormWindow(simple, USER_MENU);
    }

    /**
     * 显示管理菜单
     * @param player 玩家
     */
    public static void sendAdminMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        FormWindowSimple simple = new FormWindowSimple(PLUGIN_NAME, language.adminMenuSetLevel.replace("%name%", player.getLevel().getName()));
        simple.addButton(new ElementButton(language.adminMenuButton1, new ElementButtonImageData("path", "textures/ui/World")));
        simple.addButton(new ElementButton(language.adminMenuButton2, new ElementButtonImageData("path", "textures/ui/World")));
        simple.addButton(new ElementButton(language.adminMenuButton3, new ElementButtonImageData("path", "textures/ui/timer")));
        simple.addButton(new ElementButton(language.adminMenuButton4,  new ElementButtonImageData("path", "textures/ui/refresh_light")));
        simple.addButton(new ElementButton(language.adminMenuButton5, new ElementButtonImageData("path", "textures/ui/redX1")));
        player.showFormWindow(simple, ADMIN_MENU);
    }

    /**
     * 显示设置时间菜单
     * @param player 玩家
     */
    public static void sendAdminTimeMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        FormWindowCustom custom = new FormWindowCustom(PLUGIN_NAME);
        custom.addElement(new ElementInput(language.adminTimeMenuInputText1, "", "60"));
        custom.addElement(new ElementInput(language.adminTimeMenuInputText2, "", "20"));
        player.showFormWindow(custom, ADMIN_TIME_MENU);
    }

    /**
     * 显示房间列表菜单
     * @param player 玩家
     */
    public static void sendRoomListMenu(Player player) {
        Language language = HotPotato.getInstance().getLanguage();
        FormWindowSimple simple = new FormWindowSimple(PLUGIN_NAME, "");
        for (Map.Entry<String, Room> entry : HotPotato.getInstance().getRooms().entrySet()) {
            simple.addButton(new ElementButton("§e" + entry.getKey(), new ElementButtonImageData("path", "textures/ui/switch_start_button")));
        }
        simple.addButton(new ElementButton(language.buttonReturn, new ElementButtonImageData("path", "textures/ui/cancel")));
        player.showFormWindow(simple, ROOM_LIST_MENU);
    }

    /**
     * 加入房间确认(自选)
     * @param player 玩家
     */
    public static void sendRoomJoinOkMenu(Player player, String roomName) {
        Language language = HotPotato.getInstance().getLanguage();
        if (HotPotato.getInstance().getRooms().containsKey(roomName.replace("§e", "").trim())) {
            Room room = HotPotato.getInstance().getRooms().get(roomName.replace("§e", "").trim());
            if (room.getMode() == 2 || room.getMode() == 3) {
                FormWindowModal modal = new FormWindowModal(
                        PLUGIN_NAME, language.joinRoomIsPlaying, language.buttonReturn, language.buttonReturn);
                player.showFormWindow(modal, ROOM_JOIN_OK);
            }else if (room.getPlayers().size() > 15){
                FormWindowModal modal = new FormWindowModal(
                        PLUGIN_NAME, language.joinRoomIsFull, language.buttonReturn, language.buttonReturn);
                player.showFormWindow(modal, ROOM_JOIN_OK);
            }else {
                FormWindowModal modal = new FormWindowModal(
                        PLUGIN_NAME, language.joinRoomOK.replace("%name%", "\"" + roomName + "\""), language.buttonOK, language.buttonReturn);
                player.showFormWindow(modal, ROOM_JOIN_OK);
            }
        }else {
            FormWindowModal modal = new FormWindowModal(
                    PLUGIN_NAME, language.joinRoomIsNotFound, language.buttonReturn, language.buttonReturn);
            player.showFormWindow(modal, ROOM_JOIN_OK);
        }
    }

}
