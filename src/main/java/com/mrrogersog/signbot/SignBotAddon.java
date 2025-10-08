package com.mrrogersog.signbot;

import meteordevelopment.meteorclient.addon.Addon;
import meteordevelopment.meteorclient.systems.hud.HudElements;
import net.minecraft.client.MinecraftClient;

public class SignBotAddon extends Addon {
    private SignBot signBot;

    @Override
    public void onInitialize() {
        signBot = new SignBot(MinecraftClient.getInstance());

        HudElements.register(new ScanSummaryHUD(signBot));
        HudElements.register(new ScanKeybindsHUD(signBot));
        HudElements.register(new BaritoneKeybindsHUD(signBot));
    }
}