package ir.mehradn.cavesurvey;

import ir.mehradn.cavesurvey.event.ItemGroupModification;
import ir.mehradn.cavesurvey.event.LootTableModification;
import ir.mehradn.cavesurvey.item.ModItems;
import ir.mehradn.cavesurvey.item.crafting.ModRecipes;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaveSurvey implements ModInitializer {
    public static final String MOD_ID = "cave-survey";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public void onInitialize() {
        ModItems.register();
        ModRecipes.register();
        ItemGroupModification.register();
        LootTableModification.register();
        ModItems.registerAssets();
	}
}