# SignBot 🪧🤖

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

---

## 👤 Author

Created by [MrRogersOG](https://github.com/MrRogersOG)

---

## 📄 License

MIT License (optional — add a `LICENSE` file if desired)
