package com.mrrogersog.signbot;

/**
 * A simple interface for keybinds.
 * In a real Meteor Client addon, this would typically be handled by Meteor Client's own keybind system.
 */
public interface Keybind {
    boolean isPressed();
}
