package fr.aureliancnx.multiworld.rabbit.listeners;

import org.bukkit.Bukkit;
import fr.aureliancnx.multiworld.rabbit.packets.PlayerChat;
import java.io.IOException;
import fr.aureliancnx.multiworld.Multiworld;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Channel;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import fr.aureliancnx.multiworld.rabbit.RabbitConnection;

public class ReceiveChatListener
{
    public ReceiveChatListener(final String queueName) {
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
                        ReceiveChatListener.this.work(message);
                    };
                    channel.basicConsume(qName, true, deliverCallback, consumerTag -> {});
                }
                catch (Exception error) {
                    StackTraceElement[] stackTrace;
                    for (int length = (stackTrace = error.getStackTrace()).length, i = 0; i < length; ++i) {
                        final StackTraceElement t = stackTrace[i];
                        Logs.log("[AsgardiaMultiworld] [WARN] " + t.toString(), true);
                    }
                    Logs.log("[AsgardiaMultiworld] ------------- CRITICAL ISSUE ----------", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] We were not able to create the chat listener", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] This will cause chat sync issues!!!", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] ------------- CRITICAL ISSUE ----------", true);
                }
            }
        };
        Multiworld.instance.insertThread(thread);
        thread.start();
    }
    
    public void work(final String json) {
        try {
            final PlayerChat chat = PlayerChat.fromJson(json);
            if (chat.isExpired()) {
                Logs.log("[AsgardiaMultiworld] Just skipped an expired chat data since " + (System.currentTimeMillis() - chat.expire) + " ms", true);
                return;
            }
            Bukkit.broadcastMessage(chat.message);
        }
        catch (Exception error) {
            Logs.log("[AsgardiaMultiworld] [WARN] /!\\ ----------- UNABLE TO FORMAT CHAT MESSAGE ---------- /!\\", true);
            Logs.log("[AsgardiaMultiworld] [WARN] /!\\ ----------- THIS CAN CAUSES CHAT ISSUES ---------- /!\\", true);
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
