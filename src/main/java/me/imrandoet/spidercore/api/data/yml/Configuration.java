package me.imrandoet.spidercore.api.data.yml;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

//Credit to Logout4000
public class Configuration extends YamlConfiguration {

    private final JavaPlugin javaPlugin;

    private File fileConfiguration;

    private String configName;
    private String defaultConfig;

    public Configuration(JavaPlugin javaPlugin, String configName) {
        this.javaPlugin = javaPlugin;
        this.configName = configName.concat(".yml");
        this.fileConfiguration = new File(javaPlugin.getDataFolder(), configName);
        reload();
    }

    public Configuration(JavaPlugin javaPlugin, String configName, String defaultConfig) {
        this(javaPlugin, configName);
        this.defaultConfig = defaultConfig.concat(".yml");
    }

    public void reload() {
        if (!fileConfiguration.exists()) {
            try {
                fileConfiguration.getParentFile().mkdirs();
                fileConfiguration.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        try {
            load(fileConfiguration);

            if (defaultConfig != null) {
                InputStreamReader reader = new InputStreamReader(javaPlugin.getResource(defaultConfig));
                FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                setDefaults(defaultsConfig);
                options().copyDefaults(true);

                reader.close();
                save();

            }
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }

    }

    public void save() {
        try {
            options().indent(2);
            save(fileConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void load() {
//        this.fileConfiguration = new File(javaPlugin.getDataFolder(), this.configName.concat(".yml"));
//
//        if (!fileConfiguration.exists()) {
//            try {
//                this.fileConfiguration.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            this.yamlConfiguration.load(this.fileConfiguration);
//        } catch (IOException | InvalidConfigurationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void save() {
//        try {
//            this.yamlConfiguration.save(this.fileConfiguration);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
