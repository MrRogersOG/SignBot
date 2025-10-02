import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SignBot {

    private final MinecraftClient client;
    private final String logFilePath = "signbot-log.txt";
    private boolean scanningEnabled = false;
    private final List<BlockPos> signQueue = new ArrayList<>();

    private int totalSigns = 0;
    private int ignoredCodyTags = 0;
    private int blankSigns = 0;
    private int validSigns = 0;

    public SignBot(MinecraftClient client) {
        this.client = client;
    }

    // Toggle scanning on/off
    public void toggleScanning() {
        scanningEnabled = !scanningEnabled;
        sendChat("[SignBot] Scanning " + (scanningEnabled ? "enabled" : "paused"));
        if (scanningEnabled) scanSigns();
    }

    // Scan signs and populate queue
    public void scanSigns() {
        if (client.world == null || client.player == null) return;

        totalSigns = 0;
        ignoredCodyTags = 0;
        blankSigns = 0;
        validSigns = 0;
        signQueue.clear();

        for (BlockEntity entity : client.world.blockEntities) {
            try {
                if (entity instanceof SignBlockEntity sign) {
                    totalSigns++;

                    String line1 = sign.getText(0, false).getString().trim();
                    String line2 = sign.getText(1, false).getString().trim();
                    String line3 = sign.getText(2, false).getString().trim();
                    String line4 = sign.getText(3, false).getString().trim();

                    boolean isBlank = line1.isEmpty() && line2.isEmpty() && line3.isEmpty() && line4.isEmpty();
                    boolean isCodyTag = line1.equals("codysmile11") && line2.startsWith("was here:)");

                    BlockPos pos = sign.getPos();
                    String posString = pos.getX() + " " + pos.getY() + " " + pos.getZ();

                    if (isBlank) {
                        blankSigns++;
                        log("[SKIPPED] Blank sign at " + posString);
                    } else if (isCodyTag) {
                        ignoredCodyTags++;
                        log("[IGNORED] codysmile11 tag at " + posString);
                    } else {
                        validSigns++;
                        signQueue.add(pos);
                        log("[QUEUED] Valid sign at " + posString + " — \"" + line1 + " | " + line2 + " | " + line3 + " | " + line4 + "\"");
                    }
                }
            } catch (Exception e) {
                log("[ERROR] Failed to process sign — " + e.getMessage());
            }
        }

        scanningEnabled = false; // Auto-pause after scan
        showSummary();
    }

    // Send one Baritone command from queue
    public void dispatchNextSign() {
        if (client.player == null || signQueue.isEmpty()) return;

        BlockPos pos = signQueue.remove(0);
        String command = "#goto " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
        client.player.sendChatMessage(command);
        log("[DISPATCHED] Sent Baritone command to " + pos.toShortString());
    }

    // Show scan summary in chat
    private void showSummary() {
        sendChat("[SignBot] Scan complete: " + totalSigns + " signs detected");
        if (ignoredCodyTags > 0) {
            sendChat("[SignBot] " + ignoredCodyTags + " codysmile11 signs ignored 😎");
        }
        sendChat("[SignBot] " + blankSigns + " blank signs skipped");
        sendChat("[SignBot] " + validSigns + " valid signs added to queue");
        sendChat("[SignBot] Finished scan ✅");
    }

    // Send message to chat
    private void sendChat(String message) {
        client.player.sendMessage(Text.of(message), false);
    }

    // Write to log file
    private void log(String message) {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            buffer.write("[" + timestamp + "] " + message + "\n");
        } catch (IOException e) {
            System.err.println("SignBot failed to write to log file: " + e.getMessage());
        }
    }
}
