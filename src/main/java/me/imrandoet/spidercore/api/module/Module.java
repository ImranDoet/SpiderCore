package me.imrandoet.spidercore.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public abstract class Module {

    public abstract void run();

    @Target(ElementType.TYPE)
    public @interface ModulePriority {

        int priority() default DEFAULT;

        int HIGHEST = 10;
        int DEFAULT = 5;
        int LOWEST = 1;

    }

}
