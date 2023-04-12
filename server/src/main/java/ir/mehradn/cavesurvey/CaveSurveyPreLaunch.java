package ir.mehradn.cavesurvey;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class CaveSurveyPreLaunch implements PreLaunchEntrypoint {
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }
}
