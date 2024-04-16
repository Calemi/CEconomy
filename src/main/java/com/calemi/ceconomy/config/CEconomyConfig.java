package com.calemi.ceconomy.config;

import com.calemi.ceconomy.main.CEconomyRef;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = CEconomyRef.MOD_ID)
public class CEconomyConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Gui.Excluded
    public static CEconomyClientConfig CLIENT;

    @ConfigEntry.Gui.Excluded
    public static CEconomyCommonConfig COMMON;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    CEconomyClientConfig clientConfig = new CEconomyClientConfig();

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    CEconomyCommonConfig commonConfig = new CEconomyCommonConfig();

    public static void init() {
        AutoConfig.register(CEconomyConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        CLIENT = AutoConfig.getConfigHolder(CEconomyConfig.class).getConfig().clientConfig;
        COMMON = AutoConfig.getConfigHolder(CEconomyConfig.class).getConfig().commonConfig;
    }
}