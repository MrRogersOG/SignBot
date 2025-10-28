import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A Minecraft bot designed to automatically detect, process, and interact with signs in the game.
 * This bot can scan for signs, filter them based on content, queue them for processing,
 * and integrate with Baritone pathfinding for navigation.
 */
public class SignBot {

    private final MinecraftClient client; // Minecraft client instance for game interaction
    private final String logFilePath = "signbot-log.txt"; // Path to the log file

    // Feature toggles - boolean flags for various bot functionalities
    private boolean scanningEnabled = false; // Controls whether sign scanning is active
    private boolean destroyEnabled = false; // Controls whether automatic sign destruction is active
    private boolean scanPaused = false; // Controls whether scanning is paused
    private boolean debugEnabled = false; // Controls debug mode for additional logging
    private boolean autoDispatchEnabled = false; // [Baritone] Auto-dispatch toggle for automatic pathing

    // Debounce state for each keybind - prevents rapid repeated key presses
    private final KeybindState scanState = new KeybindState(); // State for scanning toggle keybind
    private final KeybindState destroyState = new KeybindState(); // State for destruction toggle keybind
    private final KeybindState skipState = new KeybindState(); // State for skip current sign keybind
    private final KeybindState pauseState = new KeybindState(); // State for pause/resume keybind
    private final KeybindState debugState = new KeybindState(); // State for debug mode toggle keybind
    private final KeybindState dispatchState = new KeybindState();     // [Baritone]
    private final KeybindState deferState = new KeybindState();        // [Baritone]
    private final KeybindState cancelState = new KeybindState();       // [Baritone]
    private final KeybindState clearState = new KeybindState();        // [Baritone]
    private final KeybindState autoDispatchState = new KeybindState(); // [Baritone]

    // Queue of valid signs to process
    private final List<BlockPos> signQueue = new ArrayList<>();

    // Scan statistics
    private int totalSigns = 0;
    private int ignoredCodyTags = 0;
    private int blankSigns = 0;
    private int validSigns = 0;

    // GUI-ready flag for skip
    private boolean skipTriggered = false;

    public SignBot(MinecraftClient client) {
        this.client = client;
    }

    public void handleInput(Keybind scanKey, Keybind destroyKey, Keybind skipKey, Keybind pauseKey, Keybind debugKey,
                            Keybind dispatchKey, Keybind deferKey, Keybind cancelKey, Keybind clearKey, Keybind autoDispatchKey) {

        if (scanState.toggled(scanKey)) {
            scanningEnabled = !scanningEnabled;
            sendChat("[SignBot] Scanning " + (scanningEnabled ? "enabled" : "disabled"));
            if (scanningEnabled) scanSigns();
        }

        if (destroyState.toggled(destroyKey)) {
            destroyEnabled = !destroyEnabled;
            sendChat("[SignBot] Auto-destroy " + (destroyEnabled ? "enabled" : "disabled"));
        }

        if (skipState.toggled(skipKey)) {
            skipCurrentSign("Manual skip");
            skipTriggered = true;
        } else {
            skipTriggered = false;
        }

        if (pauseState.toggled(pauseKey)) {
            scanPaused = !scanPaused;
            sendChat("[SignBot] Scanning " + (scanPaused ? "paused" : "resumed"));
        }

        if (debugState.toggled(debugKey)) {
            debugEnabled = !debugEnabled;
            sendChat("[SignBot] Debug mode " + (debugEnabled ? "enabled" : "disabled"));
        }

        // [Baritone] Keybinds
        if (dispatchState.toggled(dispatchKey)) dispatchNextSign();
        if (deferState.toggled(deferKey)) deferCurrentSign();
        if (cancelState.toggled(cancelKey)) cancelPath();
        if (clearState.toggled(clearKey)) clearQueue();
        if (autoDispatchState.toggled(autoDispatchKey)) toggleAutoDispatch();
    }

    public void tick() {
        if (destroyEnabled && !signQueue.isEmpty() && client.player != null) {
            BlockPos target = signQueue.get(0);
            double distance = client.player.getPos().distanceTo(Vec3d.ofCenter(target));
            if (distance < 4.5) {
                client.interactionManager.attackBlock(target, client.player.getHorizontalFacing());
                log("[DESTROYED] Sign at " + target.toShortString());
                signQueue.remove(0);
            }
        }

        // [Baritone] Auto-dispatch loop
        if (autoDispatchEnabled && !BaritoneHelper.isPathing() && !signQueue.isEmpty()) {
            dispatchNextSign();
        }
    }

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
                        saveSign(pos, line1, line2, line3, line4);
                    }
                }
            } catch (Exception e) {
                log("[ERROR] Failed to process sign — " + e.getMessage());
            }
        }

        scanningEnabled = false;
        showSummary();
    }

    private void saveSign(BlockPos pos, String l1, String l2, String l3, String l4) {
        String line = pos.getX() + "\t" + pos.getY() + "\t" + pos.getZ() + "\t" + l1 + "\t" + l2 + "\t" + l3 + "\t" + l4;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("signs.tsv", true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            log("[ERROR] Failed to save sign data — " + e.getMessage());
        }
    }

    // [Baritone] Dispatch next sign from queue
    public void dispatchNextSign() {
        if (client.player == null || signQueue.isEmpty()) return;
        BlockPos pos = signQueue.remove(0);
        BaritoneHelper.goTo(pos);
        log("[DISPATCHED] Sent Baritone goal to " + pos.toShortString());
    }

    // [Baritone] Skip current sign permanently
    public void skipCurrentSign(String reason) {
        if (!signQueue.isEmpty()) {
            BlockPos skipped = signQueue.remove(0);
            BaritoneHelper.cancelPath();
            log("[SKIPPED] " + reason + " at " + skipped.toShortString());
            dispatchNextSign();
        }
    }

    // [Baritone] Defer current sign to back of queue
    public void deferCurrentSign() {
        if (!signQueue.isEmpty()) {
            BlockPos deferred = signQueue.remove(0);
            signQueue.add(deferred);
            BaritoneHelper.cancelPath();
            log("[DEFERRED] Sign moved to back of queue: " + deferred.toShortString());
            dispatchNextSign();
        }
    }

    // [Baritone] Cancel current path
    public void cancelPath() {
        BaritoneHelper.cancelPath();
        log("[CANCELLED] Baritone pathing canceled");
    }

    // [Baritone] Clear queue and cancel path
    public void clearQueue() {
        BaritoneHelper.cancelPath();
        signQueue.clear();
        log("[CLEARED] Sign queue emptied");
    }

    // [Baritone] Toggle auto-dispatch
    public void toggleAutoDispatch() {
        autoDispatchEnabled = !autoDispatchEnabled;
        sendChat("[SignBot] Auto-dispatch " + (autoDispatchEnabled ? "enabled" : "disabled"));
    }

    /**
     * Displays a summary of the sign scanning operation, including total signs detected,
     * ignored signs, blank signs skipped, and valid signs added to the queue.
     */
    private void showSummary() {
        // Send total signs detected message
        sendChat("[SignBot] Scan complete: " + totalSigns + " signs detected");
        // Send message about ignored Cody tags if any were ignored
        if (ignoredCodyTags > 0) {
            sendChat("[SignBot] " + ignoredCodyTags + " codysmile11 signs ignored 😎");
        }
        // Send message about blank signs that were skipped
        sendChat("[SignBot] " + blankSigns + " blank signs skipped");
        // Send message about valid signs that were added to the queue
        sendChat("[SignBot] " + validSigns + " valid signs added to queue");
        // Send completion message with checkmark emoji
        sendChat("[SignBot] Finished scan ✅");
    }
}