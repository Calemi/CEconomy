package com.calemi.ceconomy.datagen;

import com.calemi.ceconomy.main.CEconomyRef;
import com.calemi.ceconomy.registry.BlockRegistry;
import com.calemi.ceconomy.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {

    private static final List<ItemConvertible> RARITANIUM_ORES = List.of(BlockRegistry.RARITANIUM_ORE, BlockRegistry.DEEPSLATE_RARITANIUM_ORE);

    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, RARITANIUM_ORES, RecipeCategory.MISC, ItemRegistry.RARITANIUM,
                0.7f, 200, "raritanium");
        offerBlasting(exporter, RARITANIUM_ORES, RecipeCategory.MISC, ItemRegistry.RARITANIUM,
                0.7f, 100, "raritanium");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.WALLET)
                .pattern("LCL")
                .input('L', Items.LEATHER)
                .input('C', ItemRegistry.COPPER_COIN)
                .criterion("wallet", conditionsFromItem(ItemRegistry.COPPER_COIN)).offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.CHECK)
                .pattern("PCP")
                .input('P', Items.PAPER)
                .input('C', ItemRegistry.COPPER_COIN)
                .criterion("check", conditionsFromItem(ItemRegistry.COPPER_COIN)).offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.CURRENCY_NETWORK_CABLE, 16)
                .pattern("GCG")
                .input('G', itemTag("c:glass_blocks"))
                .input('C', ItemRegistry.COPPER_COIN)
                .criterion("wallet", conditionsFromItem(ItemRegistry.COPPER_COIN)).offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK)
                .input(BlockRegistry.CURRENCY_NETWORK_CABLE)
                .criterion("currency_network_cable_block", conditionsFromItem(BlockRegistry.CURRENCY_NETWORK_CABLE)).offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.CURRENCY_NETWORK_CABLE)
                .input(BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK)
                .criterion("currency_network_cable_from_block", conditionsFromItem(BlockRegistry.CURRENCY_NETWORK_CABLE))
                .offerTo(exporter, new Identifier(CEconomyRef.MOD_ID, "currency_network_cable_from_block"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.CURRENCY_NETWORK_GATE)
                .pattern("CIC").pattern("IXI").pattern("CIC")
                .input('C', BlockRegistry.CURRENCY_NETWORK_CABLE)
                .input('I', itemTag("c:iron_ingots"))
                .input('X', itemTag("c:redstone_dusts"))
                .criterion("currency_network_gate", conditionsFromItem(BlockRegistry.CURRENCY_NETWORK_CABLE)).offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.BANK)
                .pattern("CIC").pattern("IXI").pattern("CIC")
                .input('C', BlockRegistry.CURRENCY_NETWORK_CABLE)
                .input('I', itemTag("c:iron_ingots"))
                .input('X', ItemRegistry.GOLD_COIN)
                .criterion("bank", conditionsFromItem(BlockRegistry.CURRENCY_NETWORK_CABLE)).offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.ENDER_BANK)
                .pattern("CEC").pattern("EXE").pattern("CEC")
                .input('C', BlockRegistry.CURRENCY_NETWORK_CABLE)
                .input('X', ItemRegistry.GOLD_COIN)
                .input('E', Items.ENDER_EYE)
                .criterion("ender_bank", conditionsFromItem(BlockRegistry.CURRENCY_NETWORK_CABLE)).offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.TRADING_POST)
                .pattern("CIC").pattern("I I").pattern("CIC")
                .input('C', BlockRegistry.CURRENCY_NETWORK_CABLE)
                .input('I', itemTag("c:iron_ingots"))
                .criterion("trading_post", conditionsFromItem(BlockRegistry.CURRENCY_NETWORK_CABLE)).offerTo(exporter);
    }

    private TagKey<Item> itemTag(String key) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(key));
    }
}