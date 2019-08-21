package io.th0rgal.oraxen.settings;

import org.bukkit.ChatColor;

public enum Plugin implements ConfigEnum {

    NAME("Plugin.name"),
    PREFIX("Plugin.prefix");

    private Object value;

    Plugin(String section) {
        this.value = ConfigsManager.getSettings().get(section);
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', this.value.toString());
    }

}