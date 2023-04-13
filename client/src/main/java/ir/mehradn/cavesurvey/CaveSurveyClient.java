package ir.mehradn.cavesurvey;

import ir.mehradn.cavesurvey.item.ModItemProperties;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class CaveSurveyClient implements ClientModInitializer {
    public static final String MOD_ID = "cave-survey-client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public void onInitializeClient() {
        ModItemProperties.register();
    }
}