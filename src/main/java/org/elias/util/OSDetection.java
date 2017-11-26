package org.elias.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for os detection
 */
public class OSDetection {

    private static Logger logger = LoggerFactory.getLogger(OSDetection.class);

    /**
     * Return current OS
     * @return
     */
    public static OpSystem getOS() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().indexOf("linux") != -1) {
            logger.info("Detected Linux");
            return OpSystem.LINUX;
        } else if (os.toLowerCase().indexOf("windows") != -1) {
            logger.info("Detected Windows");
            return OpSystem.WINDOWS;
        } else {
            logger.info("Detected Unknown OS: {}", os);
            return OpSystem.UNKNOWN;
        }
    }

}
