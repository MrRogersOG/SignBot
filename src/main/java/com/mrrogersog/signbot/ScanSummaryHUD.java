package com.mrrogersog.signbot;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementType;
import meteordevelopment.meteorclient.renderer.NormalRenderer;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class ScanSummaryHUD extends HudElement {
    private final SignBot signBot;

    public ScanSummaryHUD(SignBot signBot) {
        super("Scan Summary", "Displays live scan stats", HudElementType.TEXT);
        this.signBot = signBot;
    }

    @Override
    public void render(NormalRenderer renderer) {
        int x = getX(), y = getY();
        renderer.text("SignBot - Scan Summary", x, y, Color.WHITE, true); y += 12;
        renderer.text("Total signs: " + signBot.getTotalSigns(), x, y, Color.GRAY, true); y += 12;
        renderer.text("Ignored signs: " + signBot.getIgnoredTagCount(), x, y, Color.GRAY, true); y += 12;
        renderer.text("Blank signs: " + signBot.getBlankSignCount(), x, y, Color.GRAY, true); y += 12;
        renderer.text("Valid signs: " + signBot.getValidSignCount(), x, y, Color.GRAY, true); y += 12;

        if (!signBot.getSignQueue().isEmpty()) {
            BlockPos next = signBot.getSignQueue().get(0);
            renderer.text("Next Sign: " + next.getX() + " " + next.getY() + " " + next.getZ(), x, y, Color.WHITE, true);
        }
    }
}