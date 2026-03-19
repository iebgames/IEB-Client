// IEB Client Example Script
// Place this in .minecraft/ieb/scripts/example.js

function register(manager) {
    manager.registerModule(
        "AutoJumpScript",      // Name
        "MOVEMENT",            // Category
        "Otomatik zıplama scripti örneği.", // Description
        0,                     // Keybind (None)
        {
            onUpdate: function() {
                if (mc.thePlayer.onGround && !mc.thePlayer.isInWater()) {
                    mc.thePlayer.jump();
                }
            },
            onEnable: function() {
                print("Script aktif edildi!");
            },
            onDisable: function() {
                print("Script deaktif edildi!");
            }
        }
    );
}
