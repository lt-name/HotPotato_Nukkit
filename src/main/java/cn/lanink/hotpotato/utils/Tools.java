package cn.lanink.hotpotato.utils;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.utils.DyeColor;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Tools {

    public static void broadcastMessage(Room room, String string) {
        for (Player player : room.getPlayers().keySet()) {
            player.sendMessage(string);
        }
    }

    public static double toDouble(Object object) {
        return new BigDecimal(object.toString()).doubleValue();
    }

    public static int toInt(Object object) {
        return new BigDecimal(object.toString()).intValue();
    }

    /**
     * Vector3 转为 Map
     *
     * @param vector3 Vector3
     * @return Map
     */
    public static LinkedHashMap<String, Double> vector3ToMap(Vector3 vector3) {
        LinkedHashMap<String, Double> map = new LinkedHashMap<>();
        map.put("x", vector3.getX());
        map.put("y", vector3.getY());
        map.put("z", vector3.getZ());
        return map;
    }

    /**
     * Map 转为 Vector3
     *
     * @param map Map
     * @return Vector3
     */
    @SuppressWarnings("rawtypes")
    public static Vector3 mapToVector3(Map map) {
        return new Vector3(
                toDouble(map.get("x")),
                toDouble(map.get("y")),
                toDouble(map.get("z"))
        );
    }

    public static void cmd(Player player, List<String> cmds) {
        if (player == null || cmds == null || cmds.size() < 1) {
            return;
        }
        for (String s : cmds) {
            String[] cmd = s.split("&");
            if ((cmd.length > 1) && (cmd[1].equals("con"))) {
                Server.getInstance().dispatchCommand(new ConsoleCommandSender(), cmd[0].replace("@p", player.getName()));
            } else {
                Server.getInstance().dispatchCommand(player, cmd[0].replace("@p", player.getName()));
            }
        }
    }

    public static void giveItem(Player player, int i) {
        switch (i) {
            case 1:
                givePotato(player);
                break;
            case 10:
                Item item = Item.get(324, 0, 1);
                item.setNamedTag(new CompoundTag()
                        .putBoolean("isHotPotatoItem", true)
                        .putInt("HotPotatoType", 10));
                item.setCustomName(HotPotato.getInstance().getLanguage().itemQuitRoom);
                item.setLore(HotPotato.getInstance().getLanguage().itemQuitRoomLore.split("\n"));
                player.getInventory().setItem(8, item);
                break;
        }
    }

    public static void givePotato(Player player) {
        Item item = Item.get(393, 0, 1);
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, item);
        }
    }

    /**
     * 设置玩家皮肤
     * @param player 玩家
     * @param skin 皮肤
     */
    public static void setPlayerSkin(Player player, Skin skin) {
        PlayerSkinPacket packet = new PlayerSkinPacket();
        packet.skin = skin;
        packet.newSkinName = skin.getSkinId();
        packet.oldSkinName = player.getSkin().getSkinId();
        packet.uuid = player.getUniqueId();
        player.setSkin(skin);
        player.dataPacket(packet);
    }

    /**
     * 设置玩家是否隐身
     * @param player 玩家
     * @param invisible 是否隐身
     */
    public static void setPlayerInvisible(Player player, boolean invisible) {
        player.setDataFlag(0, 5, invisible);
    }

    /**
     * 重置玩家状态
     * @param player 玩家
     * @param joinRoom 是否为加入房间
     */
    public static void rePlayerState(Player player, boolean joinRoom) {
        player.removeAllEffects();
        player.setHealth(player.getMaxHealth());
        player.getFoodData().setLevel(player.getFoodData().getMaxLevel());
        player.setNameTag(player.getName());
        player.setGamemode(0);
        if (joinRoom) {
            player.setAllowModifyWorld(false);
        }else {
            setPlayerInvisible(player, false);
        }
        player.getAdventureSettings().set(AdventureSettings.Type.FLYING, false);
        player.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, false);
        player.getAdventureSettings().update();
    }

    /**
     * 添加声音
     * @param room 房间
     * @param sound 声音
     */
    public static void playSound(Room room, Sound sound) {
        for (Player player : room.getPlayers().keySet()) {
            playSound(player, sound);
        }
    }

    public static void playSound(Player player, Sound sound) {
        PlaySoundPacket packet = new PlaySoundPacket();
        packet.name = sound.getSound();
        packet.volume = 1.0F;
        packet.pitch = 1.0F;
        packet.x = player.getFloorX();
        packet.y = player.getFloorY();
        packet.z = player.getFloorZ();
        player.dataPacket(packet);
    }

    /**
     * 清理实体
     * @param level 世界
     */
    public static void cleanEntity(Level level) {
        if (level.getEntities() != null && level.getEntities().length > 0) {
            for (Entity entity : level.getEntities()) {
                if (!(entity instanceof Player)) {
                    entity.close();
                }
            }
        }
    }

    /**
     * 获取底部 Y
     * 调用前应判断非空
     * @param player 玩家
     * @return Y
     */
    public static double getFloorY(Player player) {
        for (int y = 0; y < 10; y++) {
            Level level = player.getLevel();
            Block block = level.getBlock(player.getFloorX(), player.getFloorY() - y, player.getFloorZ());
            if (block.getId() != 0) {
                if (block.getBoundingBox() != null) {
                    return block.getBoundingBox().getMaxY() + 0.2;
                }
                return block.getMinY() + 0.2;
            }
        }
        return player.getFloorY();
    }

    /**
     * 放烟花
     *
     * GitHub：https://github.com/PetteriM1/FireworkShow
     * @param player 玩家
     */
    public static void spawnFirework(Position player) {
        Level level = player.getLevel();
        ItemFirework item = new ItemFirework();
        CompoundTag tag = new CompoundTag();
        CompoundTag ex = new CompoundTag();
        ex.putByteArray("FireworkColor",new byte[]{
                (byte) DyeColor.values()[HotPotato.RANDOM.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].getDyeData()
        });
        ex.putByteArray("FireworkFade",new byte[0]);
        ex.putBoolean("FireworkFlicker",HotPotato.RANDOM.nextBoolean());
        ex.putBoolean("FireworkTrail",HotPotato.RANDOM.nextBoolean());
        ex.putByte("FireworkType",ItemFirework.FireworkExplosion.ExplosionType.values()
                [HotPotato.RANDOM.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].ordinal());
        tag.putCompound("Fireworks",(new CompoundTag("Fireworks")).putList(new ListTag<CompoundTag>("Explosions").add(ex)).putByte("Flight",1));
        item.setNamedTag(tag);
        CompoundTag nbt = new CompoundTag();
        nbt.putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("",player.x+0.5D))
                .add(new DoubleTag("",player.y+0.5D))
                .add(new DoubleTag("",player.z+0.5D))
        );
        nbt.putList(new ListTag<DoubleTag>("Motion")
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
        );
        nbt.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("",0.0F))
                .add(new FloatTag("",0.0F))

        );
        nbt.putCompound("FireworkItem", NBTIO.putItemHelper(item));
        EntityFirework entity = new EntityFirework(level.getChunk((int)player.x >> 4, (int)player.z >> 4), nbt);
        entity.spawnToAll();
    }

}
