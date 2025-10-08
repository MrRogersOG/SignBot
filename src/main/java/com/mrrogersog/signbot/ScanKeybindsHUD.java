package com.mrrogersog.signbot;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementType;
import meteordevelopment.meteorclient.renderer.NormalRenderer;

import java.awt.*;

public class ScanKeybindsHUD extends HudElement {
    private final SignBot signBot;

    public ScanKeybindsHUD(SignBot signBot) {
        super("Scan Keybinds", "Displays scan keybind states", HudElementType.TEXT);
        this.signBot = signBot;
    }

    @Override
    public void render(NormalRenderer renderer) {
        int x = getX(), y = getY();
        renderer.text("SignBot - Scan Keybinds", x, y, Color.WHITE, true); y += 12;

        drawToggle(renderer, "G Scan", signBot.isScanningEnabled(), x, y); y += 12;
        drawToggle(renderer, "H Destroy", signBot.isDestroyEnabled(), x, y); y += 12;
        drawFlash(renderer, "J Skip", signBot.isSkipTriggered(), x, y); y += 12;
        drawToggle(renderer, "K Pause", signBot.isScanPaused(), x, y); y += 12;
        drawToggle(renderer, "L Debug", signBot.isDebugEnabled(), x, y); y += 12;
    }

    private void drawToggle(NormalRenderer renderer, String label, boolean state, int x, int y) {
        renderer.text(label, x, y, Color.GRAY, true);
        Color color = state ? Color.GREEN : Color.RED;
        renderer.quad(x + 100, y, 8, 8, color);
    }

    private void drawFlash(NormalRenderer renderer, String label, boolean active, int x, int y) {
        renderer.text(label, x, y, Color.GRAY, true);
        Color color = active ? Color.GREEN : Color.GRAY;
        renderer.quad(x + 100, y, 8, 8, color);
    }
}