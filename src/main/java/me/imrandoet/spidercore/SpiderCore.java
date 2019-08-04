package me.imrandoet.spidercore;

import me.imrandoet.spidercore.api.ModuleLoader;
import me.imrandoet.spidercore.util.SpiderException;
import org.bukkit.plugin.java.JavaPlugin;

public class SpiderCore {

    private static SpiderCore instance;
    private JavaPlugin plugin;

    private ModuleLoader moduleLoader;

    private SpiderCore(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
    }

    public static SpiderCore enable(JavaPlugin javaPlugin) {
        if (instance != null) return instance;

        instance = new SpiderCore(javaPlugin);

        instance.enableCore();

        return instance;
    }

    public static void disable() {
        if (instance == null) return;

        instance = null;
    }

    public void disableCore(ModuleLoader moduleLoader_) {
        plugin.getLogger().info("Disabled SpiderCore!");
        moduleLoader_.disableModules();
        disable();
    }

    public <T extends JavaPlugin> ModuleLoader moduleLoader(String package_, T javaPlugin) {
        return moduleLoader = new ModuleLoader(javaPlugin, package_);
    }

    public void enableCore() {
        plugin.getLogger().info("Enabled SpiderCore!");
    }

    public static SpiderCore getInstance() {
        if (instance == null)
            throw new SpiderException("SpiderCore instance not initialised yet!");

        return instance;
    }


}
