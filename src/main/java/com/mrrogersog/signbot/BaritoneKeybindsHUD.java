package com.mrrogersog.signbot;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementType;
import meteordevelopment.meteorclient.renderer.NormalRenderer;

import java.awt.*;

public class BaritoneKeybindsHUD extends HudElement {
    private final SignBot signBot;

    public BaritoneKeybindsHUD(SignBot signBot) {
        super("Baritone Keybinds", "Displays Baritone keybind states", HudElementType.TEXT);
        this.signBot = signBot;
    }

    @Override
    public void render(NormalRenderer renderer) {
        int x = getX(), y = getY();
        renderer.text("SignBot - Baritone Keybinds", x, y, Color.WHITE, true); y += 12;

        drawFlash(renderer, "B Dispatch", signBot.getDispatchState().pressed(), x, y); y += 12;
        drawFlash(renderer, "N Defer", signBot.getDeferState().pressed(), x, y); y += 12;
        drawFlash(renderer, "M Cancel", signBot.getCancelState().pressed(), x, y); y += 12;
        drawFlash(renderer, ", Clear", signBot.getClearState().pressed(), x, y); y += 12;
        drawToggle(renderer, ". Auto", signBot.isAutoDispatchEnabled(), x, y); y += 12;
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