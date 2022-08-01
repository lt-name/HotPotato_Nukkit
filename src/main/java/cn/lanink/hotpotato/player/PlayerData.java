package cn.lanink.hotpotato.player;

import cn.nukkit.utils.Config;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * @author LT_Name
 */
@Data
public class PlayerData {

    private final Config config;
    private final String name;

    private int victoryCount;

    public PlayerData(@NotNull Config config, @NotNull String playerName) {
        this.config = config;
        this.name = playerName;

        this.victoryCount = config.getInt("victoryCount");
    }

    public void addVictoryCount() {
        this.victoryCount++;
    }

    public void save() {
        this.save(false);
    }

    public void save(boolean async) {
        this.config.set("victoryCount", this.victoryCount);

        this.config.save(async);
    }

}
