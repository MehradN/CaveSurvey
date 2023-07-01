package ir.mehradn.cavesurvey;

import ir.mehradn.cavesurvey.event.ItemGroupModification;
import ir.mehradn.cavesurvey.event.LootTableModification;
import ir.mehradn.cavesurvey.event.VillagerTradeModification;
import ir.mehradn.cavesurvey.item.ModItems;
import ir.mehradn.cavesurvey.item.crafting.ModRecipes;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaveSurvey implements ModInitializer {
    public static final String MOD_ID = "cave-survey";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Registering cave-survey...");
        LOGGER.info("Registering items...");
        ModItems.register();
        LOGGER.info("Registering special recipes...");
        ModRecipes.register();
        LOGGER.info("Registering events...");
        ItemGroupModification.register();
        LootTableModification.register();
        VillagerTradeModification.register();
        LOGGER.info("Registering polymer assets...");
        ModItems.registerAssets();
        LOGGER.info("Registering cave-survey completed!");
    }
}