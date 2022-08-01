package cn.lanink.hotpotato.room;

/**
 * @author LT_Name
 */
public enum RoomStatus {

    /**
     * 等待（Task未初始化）
     */
    WAIT,

    /**
     * 等待中（等待更多玩家加入）
     */
    WAIT_PLAYER,

    /**
     * 游戏中
     */
    GAME,

    /**
     * 胜利结算中
     */
    VICTORY;

}
