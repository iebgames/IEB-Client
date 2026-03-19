package dev.iebgames.iebclient.module;

public enum Category {
    COMBAT   ("⚔ Combat"),
    MOVEMENT ("🏃 Movement"),
    RENDER   ("👁 Render"),
    PLAYER   ("👤 Player"),
    WORLD    ("🌍 World"),
    EXPLOIT  ("🧪 Exploit"),
    MISC     ("⚙ Misc"),
    SCRIPTS  ("📜 Scripts"),
    MENU     ("🛠 Menu Settings");

    private final String displayName;
    Category(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
