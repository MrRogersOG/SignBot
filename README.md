# SignBot 🪧🤖

Deprecated Code.
My code didn't meet the standards for Meteor Client, so I started over with Tabula Rasa below. Wtih Tabula Rasa I can do more then just search for signs. If someone want's to copy SignBot and do what they want with it, cool, have at it.
https://github.com/MrRogersOG/TabulaRasa

**SignBot** is a Minecraft automation tool designed to scan signs in loaded chunks, filter out unwanted ones, and send Baritone commands to visit valid sign locations. It’s built for Fabric mod environments and optimized for modular control and persistent logging.

---

## 🔍 Features

- Scans all loaded sign blocks in the world
- Filters out:
  - Blank signs
  - Signs starting with `codysmile11` followed by `was here:)`
- Queues valid signs for Baritone pathfinding
- Sends `#goto` commands one at a time
- Logs all actions to `signbot-log.txt`
- Displays a fun chat summary after each scan

---

## 🧠 Control Flow

- Toggle scanning with a keybind or method call (`toggleScanning()`)
- Scan once, then auto-pause to prevent memory overload
- Queue valid signs for dispatch
- Dispatch signs manually using `dispatchNextSign()` or via timer
- Chat summary includes total signs, Cody tags ignored, blanks skipped, and valid signs queued

---

## 📦 Installation

1. Clone this repo:
   ```bash
   git clone https://github.com/yourusername/SignBot.git
   ```
2. Add `SignBot.java` to your Fabric mod’s source folder (e.g. `src/main/java`)
3. Wire up keybinds or triggers to call:
   - `toggleScanning()` to start scanning
   - `dispatchNextSign()` to send Baritone commands

---

## 🚀 Baritone Integration

This mod integrates [Baritone](https://github.com/cabaletta/baritone) for automated pathfinding and navigation.

**Note:** Baritone is not bundled with this mod. You must install it separately.

### How to Install Baritone

1. Download the latest compatible version of Baritone from the [Baritone GitHub Releases](https://github.com/cabaletta/baritone/releases).
2. Place the Baritone `.jar` file into your `mods` folder alongside this mod.
3. Ensure both mods are compatible with the same Minecraft version and mod loader (e.g. Fabric or Forge).
4. Launch the game and verify that Baritone is loaded (you can test with a simple `#help` command in chat).

### Credits

Special thanks to the Baritone developers for their outstanding work. This mod uses Baritone’s API for pathfinding and navigation logic.

---

## 📝 Logging

All actions are logged to `signbot-log.txt` in your Minecraft directory. Example entries:

```
[2025-10-01 23:05:12] [QUEUED] Valid sign at 123 64 -456 — "Welcome | to | my | base"
[2025-10-01 23:05:13] [IGNORED] codysmile11 tag at 321 70 -100
[2025-10-01 23:05:14] [DISPATCHED] Sent Baritone command to 123 64 -456
```

---

## 💬 Chat Summary Example

After scanning:

```
[SignBot] Scan complete: 42 signs detected
[SignBot] 15 codysmile11 signs ignored 😎
[SignBot] 5 blank signs skipped
[SignBot] 22 valid signs added to queue
[SignBot] Finished scan ✅
```

---

## 🛠️ Future Ideas

- Auto-resume scanning after queue is empty
- Prioritize signs by proximity
- GUI overlay for live status
- Emoji toggle or seasonal flair
- Baritone queue manager for multi-sign routing
- Configurable filters for sign content
- Integration with minimap mods for visual tracking

---

## 👤 Author

Created by [MrRogersOG](https://github.com/MrRogersOG)

---

## 📄 License

MIT License
```
