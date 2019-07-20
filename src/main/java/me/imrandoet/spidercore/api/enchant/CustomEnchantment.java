package me.imrandoet.spidercore.api.enchant;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public abstract class CustomEnchantment extends Enchantment {

    public CustomEnchantment(String name) {
        super(NamespacedKey.minecraft(name.trim().toLowerCase()));
        Validate.notNull(name, "Enchantment name must not be null...");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }


}
