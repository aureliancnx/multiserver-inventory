package fr.aureliancnx.multiworld.rabbit.listeners;

import java.io.IOException;
import fr.aureliancnx.multiworld.Multiworld;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Channel;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import fr.aureliancnx.multiworld.rabbit.RabbitConnection;
import java.util.HashMap;
import fr.aureliancnx.multiworld.rabbit.packets.PlayerCommand;
import java.util.Map;

public class ReceiveCommandListener
{
    public static Map<String, PlayerCommand> tmpCommandData;
    
    static {
        ReceiveCommandListener.tmpCommandData = new HashMap<String, PlayerCommand>();
    }
    
    public ReceiveCommandListener(final String queueName) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    final Channel channel = RabbitConnection.connection.createChannel();
                    channel.exchangeDeclare(queueName, "fanout");
                    final String qName = channel.queueDeclare().getQueue();
                    channel.queueBind(qName, queueName, "");
                    Logs.log("[AsgardiaMultiworld] Loaded queue '" + queueName + "'", true);
                    final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        if (Multiworld.instance.disabled) {
                            this.stop();
                            return;
                        }
                        final String message = new String(delivery.getBody(), "UTF-8");
                        if (Multiworld.instance.debug) {
                            Logs.log("[AsgardiaMultiworld - DEBUG] Received in queue " + str + " '" + message + "'", false);
                        }
                        ReceiveCommandListener.this.work(message);
                    };
                    channel.basicConsume(qName, true, deliverCallback, consumerTag -> {});
                }
                catch (Exception error) {
                    StackTraceElement[] stackTrace;
                    for (int length = (stackTrace = error.getStackTrace()).length, i = 0; i < length; ++i) {
                        final StackTraceElement t = stackTrace[i];
                        Logs.log("[AsgardiaMultiworld] [WARN] " + t.toString(), true);
                    }
                    Logs.log("[AsgardiaMultiworld] [WARN] ------------- CRITICAL ISSUE ----------", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] We were not able to create the command listener", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] This will cause command sync issues!!!", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] ------------- CRITICAL ISSUE ----------", true);
                }
            }
        };
        Multiworld.instance.insertThread(thread);
        thread.start();
    }
    
    public void work(final String json) {
        try {
            final PlayerCommand command = PlayerCommand.fromJson(json);
            if (command.isExpired()) {
                Logs.log("[AsgardiaMultiworld] Just skipped an expired command data since " + (System.currentTimeMillis() - command.expire) + " ms.", true);
                return;
            }
            ReceiveCommandListener.tmpCommandData.put(command.playerName, command);
        }
        catch (Exception error) {
            Logs.log("[AsgardiaMultiworld] [WARN] /!\\ ----------- UNABLE TO SAVE COMMAND ---------- /!\\", true);
            Logs.log("[AsgardiaMultiworld] [WARN] /!\\ ----------- THIS CAN CAUSES COMMAND ISSUES ---------- /!\\", true);
            Logs.log("[AsgardiaMultiworld] [WARN] /!\\ ----------- STACK TRACE BELOW ---------- /!\\", true);
            StackTraceElement[] stackTrace;
            for (int length = (stackTrace = error.getStackTrace()).length, i = 0; i < length; ++i) {
                final StackTraceElement t = stackTrace[i];
                Logs.log("[AsgardiaMultiworld] [WARN] " + t.toString(), true);
            }
            Logs.log("[AsgardiaMultiworld] [WARN] /!\\ ----------- END ---------- /!\\", true);
        }
    }
}
