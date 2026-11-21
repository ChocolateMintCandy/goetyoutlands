package com.mint.goetyoutlands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = goety_outlands.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    public static final String NECROMANCERS_KEY = "config.goety_outlands.necromancers";
    public static final NecromancerSettings ILLAGER_NECROMANCER;
    public static final NecromancerSettings ARCHMAGE_NECROMANCER;
    static {
        BUILDER.translation(NECROMANCERS_KEY).push("necromancers");
        ILLAGER_NECROMANCER = new NecromancerSettings("illager", 220.0D, 4.0D, 2);
        ARCHMAGE_NECROMANCER = new NecromancerSettings("archmage", 220.0D, 5.0D, 2);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static final class NecromancerSettings {
        public final String translationKey;
        public final ForgeConfigSpec.DoubleValue baseHealth;
        public final ForgeConfigSpec.DoubleValue baseAttackDamage;
        public final ForgeConfigSpec.IntValue summonLimit;

        private NecromancerSettings(String group, double health, double damage, int limit) {
            translationKey = NECROMANCERS_KEY + "." + group;
            BUILDER.translation(translationKey).push(group);
            baseHealth = BUILDER.comment("Level 0 max health. Levels 1 and 2 use 1.25x and 2.5x this value. Default: " + health)
                    .translation(NECROMANCERS_KEY + ".baseHealth").defineInRange("baseHealth", health, 1.0D, 1024.0D);
            baseAttackDamage = BUILDER.comment("Base damage per normal projectile, before rank and other bonuses. Default: " + damage)
                    .translation(NECROMANCERS_KEY + ".baseAttackDamage").defineInRange("baseAttackDamage", damage, 1.0D, 2048.0D);
            summonLimit = BUILDER.comment("Maximum servants of this type per player. Lowering this does not remove existing servants. Default: " + limit)
                    .translation(NECROMANCERS_KEY + ".summonLimit").defineInRange("summonLimit", limit, 1, Integer.MAX_VALUE);
            BUILDER.pop();
        }

        public double maxHealthForLevel(int rank) {
            return baseHealth.get() * Math.max(1.0D, rank * 1.25D);
        }
    }

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(ResourceLocation.tryParse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(itemName)))
                .collect(Collectors.toSet());
    }
}
