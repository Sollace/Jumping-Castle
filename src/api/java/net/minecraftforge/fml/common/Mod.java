package net.minecraftforge.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.relauncher.Side;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
    String modid();

    String name() default "";

    String version() default "";

    String dependencies() default "";

    boolean useMetadata() default false;

    boolean clientSideOnly() default false;

    boolean serverSideOnly() default false;

    String acceptedMinecraftVersions() default "";

    String acceptableRemoteVersions() default "";

    String acceptableSaveVersions() default "";

    String certificateFingerprint() default "";

    String modLanguage() default "java";

    String modLanguageAdapter() default "";

    boolean canBeDeactivated() default false;

    String guiFactory() default "";

    String updateJSON() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface EventHandler{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface EventBusSubscriber {
        Side[] value() default { Side.CLIENT, Side.SERVER };
        String modid() default "";
    }
}