package com.mrrogersog.signbot;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.ModuleCategory;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.BoolSetting;

public class SignBotModule extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SignBot signBot;

    private final Setting<Boolean> enableScanning = sgGeneral.add(new BoolSetting.Builder()
        .name("Enable Scanning")
        .description("Toggle sign scanning.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> enableDestroy = sgGeneral.add(new BoolSetting.Builder()
        .name("Auto Destroy")
        .description("Toggle auto-destroy of signs.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> enableDebug = sgGeneral.add(new BoolSetting.Builder()
        .name("Debug Mode")
        .description("Toggle debug logging.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> enableAutoDispatch = sgGeneral.add(new BoolSetting.Builder()
        .name("Auto Dispatch")
        .description("Toggle Baritone auto-dispatch.")
        .defaultValue(false)
        .build());

    public SignBotModule(SignBot signBot) {
        super(new ModuleCategory("SignBot"), "SignBot", "Unified control panel for SignBot features.");
        this.signBot = signBot;
    }

    @Override
    public void onActivate() {
        // Optional: trigger scan or dispatch when module is toggled
    }

    @Override
    public void onDeactivate() {
        // Optional: clean up or cancel paths
    }

    @Override
    public void onTick() {
        signBot.setScanningEnabled(enableScanning.get());
        signBot.setDestroyEnabled(enableDestroy.get());
        signBot.setDebugEnabled(enableDebug.get());
        signBot.setAutoDispatchEnabled(enableAutoDispatch.get());
    }
}