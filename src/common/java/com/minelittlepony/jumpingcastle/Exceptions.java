package com.minelittlepony.jumpingcastle;

import org.apache.logging.log4j.Logger;

class Exceptions {
    public static void logged(Runnable action, Logger log) {
        try {
            action.run();
        } catch (Throwable e) {
            log.error("Unhandled exception detected from callee.", e);
        }
    }
}
