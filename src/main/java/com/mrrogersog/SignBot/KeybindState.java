package com.mrrogersog.signbot;

/**
 * A simple class to manage keybind debouncing.
 * In a real Meteor Client addon, this would typically be handled by Meteor Client's own keybind system.
 */
public class KeybindState {
    private boolean lastState = false;

    public boolean toggled(Keybind keybind) {
        boolean currentState = keybind.isPressed();
        if (currentState && !lastState) {
            lastState = true;
            return true;
        } else if (!currentState && lastState) {
            lastState = false;
        }
        return false;
    }

    public boolean pressed() {
        return lastState;
    }
}
