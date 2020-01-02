package collaborative.engine.logger;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

import java.util.concurrent.ThreadLocalRandom;

@Plugin(name="status", category = "Lookup")
public class StatusLookup implements StrLookup {
    @Override
    public String lookup(String key) {
        return ThreadLocalRandom.current().nextBoolean() ? "IDLE" : "BUSY";
    }

    @Override
    public String lookup(LogEvent event, String key) {
        return ThreadLocalRandom.current().nextBoolean() ? "IDLE" : "BUSY";
    }
}
