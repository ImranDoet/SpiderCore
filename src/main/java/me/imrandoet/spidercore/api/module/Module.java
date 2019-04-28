package me.imrandoet.spidercore.api.module;

public abstract class Module {

    public abstract void run();



    public @interface ModulePriority {

        int priority() default DEFAULT;

        int HIGHEST = 10;
        int DEFAULT = 5;
        int LOWEST = 1;

    }

}
