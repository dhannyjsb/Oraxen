package io.th0rgal.oraxen.utils.timers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimersFactory {

    private long delay;
    private Map<UUID, Timer> timersPerUUID = new HashMap<>();

    public TimersFactory(int delay) {
        this.delay = delay;
    }

    public TimersFactory(long delay) {
        this.delay = delay;
    }

    public Timer getTimer(Player player) {
        UUID playerUniqueID = player.getUniqueId();
        if (!timersPerUUID.containsKey(playerUniqueID))
            timersPerUUID.put(playerUniqueID, new Timer(delay));
        return timersPerUUID.get(playerUniqueID);
    }

}