package io.th0rgal.oraxen.utils.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.th0rgal.oraxen.items.Item;
import io.th0rgal.oraxen.utils.NMS;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class PredicatesGenerator {

    private JsonObject json = new JsonObject();
    private static final Method NBT_TAG_TO_INT;

    static {
        try {
            NBT_TAG_TO_INT = NMS.NBT_TAG_INT.toClass().getMethod("asInt");
        } catch (NoSuchMethodException e) {
            throw (new RuntimeException());
        }
    }

    public PredicatesGenerator(Material material, List<Item> items) {

        //parent
        json.addProperty("parent", getParent(material));

        //textures
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", getVanillaTextureName(material));
        json.add("textures", textures);

        //overrides
        JsonArray overrides = new JsonArray();

        //custom items
        for (Item item : items) {
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            try {
                predicate.addProperty("custom_model_data", (int) NBT_TAG_TO_INT.invoke(item.getNBTBase("CustomModelData")));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            override.add("predicate", predicate);
            override.addProperty("model", item.getPackInfos().getModelName());
            overrides.add(override);
        }

        json.add("overrides", overrides);

    }

    public String getVanillaModelName(Material material) {
        return getVanillaTextureName(material);
    }

    public String getVanillaTextureName(Material material) {
        return "item/" + material
                .toString()
                .toLowerCase();

    }

    String[] tools = new String[]{"PICKAXE", "SWORD", "HOE", "AXE", "SHOVEL"};

    private String getParent(Material material) {
        if (material.isBlock())
            return "block/cube_all";
        for (String tool : tools)
            if (material.toString().contains(tool))
                return "item/handheld";
        if (material == Material.FISHING_ROD)
            return "item/handheld_rod";
        else
            return "item/generated";
    }

    public JsonObject toJSON() {
        return this.json;
    }

}
