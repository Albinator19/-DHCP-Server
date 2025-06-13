package code;

import java.io.IOException;
import java.util.logging.*;


public class DHCPLogger {
	private static final Logger logger = Logger.getLogger("DHCPLogger");
    static {
        try {
            FileHandler fh = new FileHandler("dhcp.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("[LOGGER] Erreur d'initialisation : " + e.getMessage());
        }
    }
    public static void log(String message) {
        logger.info(message);
    }

}
