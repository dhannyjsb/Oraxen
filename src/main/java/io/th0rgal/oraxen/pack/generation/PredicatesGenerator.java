package io.th0rgal.oraxen.pack.generation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;
import java.util.List;

public class PredicatesGenerator {

    private final JsonObject json = new JsonObject();

    public PredicatesGenerator(Material material, List<ItemBuilder> items) {

        //parent
        json.addProperty("parent", getParent(material));

        //textures
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", getVanillaTextureName(material));

        //to support colored leather armors + potions
        ItemMeta exampleMeta = new ItemStack(material).getItemMeta();
        if (exampleMeta instanceof LeatherArmorMeta || exampleMeta instanceof PotionMeta)
            textures.addProperty("layer1", getVanillaTextureName(material) + "_overlay");

        json.add("textures", textures);

        //overrides
        JsonArray overrides = new JsonArray();

        //specific items
        if (material == Material.SHIELD) {
            overrides.add(getOverride("blocking", 1, "item/shield_blocking"));
        }

        //custom items
        for (ItemBuilder item : items)
            overrides.add(getOverride("custom_model_data",
                    item.getOraxenMeta().getCustomModelData(),
                    item.getOraxenMeta().getModelName()));

        json.add("overrides", overrides);

    }

    private JsonObject getOverride(String property, int propertyValue, String model) {
        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty(property, propertyValue);
        override.add("predicate", predicate);
        override.addProperty("model", model);
        return override;
    }

    public String getVanillaModelName(Material material) {
        return getVanillaTextureName(material);
    }

    public String getVanillaTextureName(Material material) {
        if (material.isBlock()) {
            return "block/" + material
                    .toString()
                    .toLowerCase();
        } else {
            return "item/" + material
                    .toString()
                    .toLowerCase();
        }
    }

    // not static here because only instanciated once I think
    private final String[] tools = new String[]{"PICKAXE", "SWORD", "HOE", "AXE", "SHOVEL"};

    private String getParent(Material material) {
        if (material.isBlock())
            return "block/cube_all";
        if (Arrays.stream(tools).anyMatch(tool -> material.toString().contains(tool)))
            return "item/handheld";
        if (material == Material.FISHING_ROD)
            return "item/handheld_rod";
        return "item/generated";
    }

    public JsonObject toJSON() {
        return this.json;
    }

}
