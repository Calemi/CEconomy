package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.main.CEconomyMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class ValuableItemReloadListener implements SimpleSynchronousResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

    private static final ArrayList<ValuableItem> valuableItemMap = new ArrayList<>();

    public static ArrayList<ValuableItem> getValuableItems() {
        return valuableItemMap;
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("tutorial", "my_resources");
    }

    @Override
    public void reload(ResourceManager manager) {

        CEconomyMain.LOGGER.info("Collecting Valuable Item Data...");

        valuableItemMap.clear();

        TypeToken<ArrayList<ValuableItem>> typeToken = new TypeToken<>(){};

        for (Identifier id : manager.findResources("valuable", path -> path.getPath().endsWith(".json")).keySet()) {

            try (InputStream stream = manager.getResource(id).get().getInputStream()) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                valuableItemMap.addAll(GSON.fromJson(reader, typeToken.getType()));
            }

            catch(Exception e) {
                CEconomyMain.LOGGER.error("Error occurred while loading resource json" + id.toString(), e);
            }
        }

        Collections.sort(valuableItemMap);

        CEconomyMain.LOGGER.info("Valuable Items : " + valuableItemMap);
    }
}
