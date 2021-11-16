package cn.lanink.hotpotato.utils;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import com.smallaswater.npc.data.RsNpcConfig;
import com.smallaswater.npc.variable.BaseVariableV2;

/**
 * @author LT_Name
 */
public class RsNpcXVariable extends BaseVariableV2 {

    @Override
    public void onUpdate(Player player, RsNpcConfig rsNpcConfig) {
        int all = 0;
        for (Room room : HotPotato.getInstance().getRooms().values()) {
            all += room.getPlayers().size();
        }
        this.addVariable("{HotPotatoRoomPlayerCountAll}", String.valueOf(all));
    }

}
