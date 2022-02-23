package it.ipzs.cieid;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Logger {

    public enum LogLevel
    {
        NONE,
        DEBUG,
        INFO,
        ERROR;

        static public Logger.LogLevel getLevelFromInteger(Integer value) {
            LogLevel logLevel = Logger.defaultLogLevel;
            switch (value) {
            case 0:
                logLevel = NONE;
                break;
            case 1:
                logLevel = DEBUG;
                break;
            case 2:
                logLevel = INFO;
                break;
            case 3:
                logLevel = ERROR;
                break;
            default:
                throw new IllegalArgumentException("Integer value out of range");
            }
            return logLevel;
        }
    };

    public static LogLevel defaultLogLevel = LogLevel.ERROR;

    private LogLevel level;
    private static Logger Instance;


    private Logger() {
    }

    private Logger(LogLevel logLevel) {
        setLevel(logLevel);
    }

    private void Log(String message, LogLevel messageLevel)
    {
        if (level.compareTo(LogLevel.NONE) > 0 && (level.compareTo(messageLevel) <= 0))
        {
            Write(message);
        }
    }

    private void Write(String message)
    {
        OffsetDateTime currentOffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC);

        String currentDate = currentOffsetDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String logFileName = String.format("CIEID_%s.log", currentDate);
        Path logFilePath = Paths.get(System.getProperty("user.home"), ".CIEPKI", logFileName);

        String currentTimestamp = currentOffsetDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String timestampedMessage = String.format("%s  %s\n", currentTimestamp, message);

        try
        {
            Files.write(logFilePath,
                        timestampedMessage.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.APPEND,
                        StandardOpenOption.CREATE
                       );
        }
        catch(Exception exception)
        {
            System.out.print("Exception at Logger.Write() - message: " + timestampedMessage);
        }
    }


    public static Logger getInstance() {
        return getInstance(defaultLogLevel);
    }

    public static Logger getInstance(LogLevel logLevel) {
        if(Instance == null) {
            Instance = new Logger(logLevel);
        }
        return Instance;
    }

    public void Debug(String message)
    {
        Log(String.format("[D] %s", message), LogLevel.DEBUG);
    }

    public void Info(String message)
    {
        Log(String.format("[I] %s", message), LogLevel.INFO);
    }

    public void Error(String message)
    {
        Log(String.format("[E] %s", message), LogLevel.ERROR);
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public LogLevel getLevel() {
        return this.level;
    }
}
