package dev.iebgames.iebclient.module;

public enum Category {
    COMBAT   ("⚔ Combat"),
    MOVEMENT ("🏃 Movement"),
    RENDER   ("👁 Render"),
    MISC     ("⚙ Misc");

    private final String displayName;
    Category(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
