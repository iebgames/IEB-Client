package dev.iebgames.iebclient.module;

import dev.iebgames.iebclient.module.modules.combat.*;
import dev.iebgames.iebclient.module.modules.movement.*;
import dev.iebgames.iebclient.module.modules.render.*;
import dev.iebgames.iebclient.module.modules.world.*;
import dev.iebgames.iebclient.module.modules.misc.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // ── Combat ────────────────────────────────────────────────
        register(new KillAura());
        register(new MultiAura());
        register(new Triggerbot());
        register(new AimAssist());
        register(new Criticals());
        register(new AutoClicker());
        register(new Reach());
        register(new Hitbox());
        register(new Velocity());
        register(new AntiKnockback());
        register(new AutoTotem());
        register(new AutoHeal());
        register(new AutoSoup());
        register(new AutoPot());
        register(new BowAimbot());
        register(new ShieldBreaker());
        register(new Backtrack());
        register(new WTapAssist());
        register(new SprintReset());
        register(new AntiBot());

        // ── Movement ──────────────────────────────────────────────
        register(new Fly());
        register(new CreativeFly());
        register(new Speed());
        register(new BHop());
        register(new LongJump());
        register(new HighJump());
        register(new Step());
        register(new ReverseStep());
        register(new NoFall());
        register(new Glide());
        register(new Jesus());
        register(new Dolphin());
        register(new Spider());
        register(new Phase());
        register(new SafeWalk());
        register(new Sprint());
        register(new Strafe());
        register(new InventoryMove());
        register(new Timer());
        register(new NoSlow());

        // ── Render ────────────────────────────────────────────────
        register(new XRay());
        register(new Fullbright());
        register(new ESP());
        register(new PlayerESP());
        register(new ChestESP());
        register(new StorageESP());
        register(new ItemESP());
        register(new MobESP());
        register(new Tracers());
        register(new NameTags());
        register(new BlockOverlay());
        register(new Breadcrumbs());
        register(new Freecam());
        register(new CameraClip());
        register(new Wallhack());
        register(new HitColor());
        register(new DamageIndicator());
        register(new Radar());
        register(new NoHurtCam());
        register(new AntiOverlay());

        // ── World ─────────────────────────────────────────────────
        register(new Scaffold());
        register(new Tower());
        register(new Nuker());
        register(new FastBreak());
        register(new FastPlace());
        register(new AutoTool());
        register(new AutoArmor());
        register(new AutoFish());
        register(new AutoMine());
        register(new ChestStealer());
        register(new Stealer());
        register(new AutoCraft());
        register(new AutoBuild());
        register(new FastLadder());
        register(new FastEat());
        register(new LiquidInteract());
        register(new GhostHand());
        register(new BlockReach());
        register(new AntiFire());
        register(new AntiWeb());

        // ── Misc ──────────────────────────────────────────────────
        register(new AntiAFK());
        register(new Blink());
        register(new FakeLag());
        register(new Disabler());
        register(new PingSpoof());
        register(new ChatSpammer());
        register(new ChatBypass());
        register(new AutoGG());
        register(new AutoRespawn());
        register(new NoRotate());
        register(new AutoQueue());
        register(new MiddleClickFriend());
        register(new FriendManager());
        register(new Macro());
        register(new ReplayRecorder());
        register(new AntiCrash());
        register(new PacketCancel());
        register(new FastRespawn());
        register(new NoWeather());
        register(new CapeChanger());
    }

    private void register(Module mod) { modules.add(mod); }

    public List<Module> getModules()                     { return modules; }
    public List<Module> getByCategory(Category cat)      {
        return modules.stream().filter(m -> m.getCategory() == cat).collect(Collectors.toList());
    }
    public Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);
    }
}
