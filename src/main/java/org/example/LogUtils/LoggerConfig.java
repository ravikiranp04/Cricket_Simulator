package org.example.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

    public  static Logger configure(String matchId){
        try {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            Path logDirectory  = Paths.get("logs", date);
            Files.createDirectories(logDirectory);
            Path logPath = logDirectory.resolve(matchId+".log");
            Logger logger = Logger.getLogger("Game-" + matchId);

            logger.setUseParentHandlers(false);
            logger.setLevel(Level.INFO);

            FileHandler fileHandler =
                    new FileHandler(logPath.toString(), false);

            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            return logger;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to configure logging for Match-" + matchId,e
            );
        }
    }
}
