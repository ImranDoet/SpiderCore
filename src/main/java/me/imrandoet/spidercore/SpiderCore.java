package me.imrandoet.spidercore;

import me.imrandoet.spidercore.api.ModuleLoader;
import me.imrandoet.spidercore.util.SpiderException;
import org.bukkit.plugin.java.JavaPlugin;

public class SpiderCore {

    private static SpiderCore instance;
    private JavaPlugin plugin;

    private ModuleLoader moduleLoader;

    SpiderCore(JavaPlugin javaPlugin, Package pack) {
        this.plugin = javaPlugin;
    }

    public static void enable(JavaPlugin javaPlugin, Package pack) {
        if (instance == null) return;

        instance = new SpiderCore(javaPlugin, pack);

        instance.enableCore(pack);
    }

    public static void disable() {
        if (instance == null) return;

        instance.disableCore();
        instance = null;
    }

    public void disableCore() {
        plugin.getLogger().info("Disabled SpiderCore!");
    }

    public <T extends JavaPlugin> void moduleLoader(Package package_, T javaPlugin) {
        this.moduleLoader = new ModuleLoader(javaPlugin, package_);
    }

    public void enableCore(Package pack) {
        plugin.getLogger().info("Enabled SpiderCore!");
    }

    public static SpiderCore getInstance() {
        if (instance == null)
            throw new SpiderException("SpiderCore instance not initialised yet!");

        return instance;
    }


}
