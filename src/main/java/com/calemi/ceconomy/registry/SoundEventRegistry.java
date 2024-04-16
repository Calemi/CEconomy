package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventRegistry {

    public static SoundEvent COIN = regSound("item.coin", SoundEvent.of(new Identifier(CEconomyRef.MOD_ID, "item.coin")));
    public static SoundEvent CHEAP_MONEY_BAG = regSound("item.cheap_money_bag", SoundEvent.of(new Identifier(CEconomyRef.MOD_ID, "item.cheap_money_bag")));
    public static SoundEvent RICH_MONEY_BAG = regSound("item.rich_money_bag", SoundEvent.of(new Identifier(CEconomyRef.MOD_ID, "item.rich_money_bag")));

    private static SoundEvent regSound(String name, SoundEvent soundEvent) {
        return Registry.register(Registries.SOUND_EVENT, new Identifier(CEconomyRef.MOD_ID, name), soundEvent);
    }

    public static void init() {
        CEconomyMain.LOGGER.info("Registering Sounds...");
    }
}
