package fr.aureliancnx.multiworld.rabbit;

import com.rabbitmq.client.ConnectionFactory;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import org.bukkit.configuration.file.FileConfiguration;
import com.rabbitmq.client.Connection;

public class RabbitConnection
{
    public static Connection connection;
    
    public static boolean start(final FileConfiguration config) {
        Logs.log("[AsgardiaMultiworld] Connecting to RabbitMQ server...", true);
        try {
            final String hostname = config.getString("rabbit.hostname");
            final int port = config.getInt("rabbit.port");
            final String username = config.getString("rabbit.username");
            final String password = config.getString("rabbit.password");
            final String virtualHost = config.getString("rabbit.vhost");
            Logs.log("[AsgardiaMultiworld] Connecting to RabbitMQ server: " + hostname + ":" + port + " ..", true);
            long time = System.currentTimeMillis();
            final ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(hostname);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setVirtualHost(virtualHost);
            RabbitConnection.connection = factory.newConnection();
            time = System.currentTimeMillis() - time;
            Logs.log("[AsgardiaMultiworld] Connected to RabbitMQ server in " + time + " ms!", true);
            return true;
        }
        catch (Exception error) {
            StackTraceElement[] stackTrace;
            for (int length = (stackTrace = error.getStackTrace()).length, i = 0; i < length; ++i) {
                final StackTraceElement t = stackTrace[i];
                Logs.log("[AsgardiaMultiworld] [WARN] " + t.toString(), true);
            }
            Logs.log("[AsgardiaMultiworld] [WARN] Error while connecting to RabbitMQ server!", true);
            Logs.log("[AsgardiaMultiworld] [WARN] Plugin features are disabled!", true);
            return false;
        }
    }
}
