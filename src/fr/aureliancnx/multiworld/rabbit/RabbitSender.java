package fr.aureliancnx.multiworld.rabbit;

import com.rabbitmq.client.Channel;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import fr.aureliancnx.multiworld.Multiworld;
import com.rabbitmq.client.AMQP;

public class RabbitSender
{
    public static void publishMessage(final String queueName, final String message, final long ttl) throws Exception {
        Throwable t = null;
        try {
            final Channel channel = RabbitConnection.connection.createChannel();
            try {
                channel.exchangeDeclare(queueName, "fanout");
                AMQP.BasicProperties properties = null;
                final byte[] messageBodyBytes = message.getBytes();
                if (ttl != -1L) {
                    properties = new AMQP.BasicProperties.Builder().expiration(Long.toString(ttl)).build();
                }
                channel.basicPublish(queueName, "", properties, messageBodyBytes);
                if (Multiworld.instance.debug) {
                    Logs.log("[AsgardiaMultiworld - DEBUG] Sent to queue " + queueName + " : '" + message + "'", false);
                }
            }
            finally {
                if (channel != null) {
                    channel.close();
                }
            }
        }
        finally {
            if (t == null) {
                final Throwable exception;
                t = exception;
            }
            else {
                final Throwable exception;
                if (t != exception) {
                    t.addSuppressed(exception);
                }
            }
        }
    }
}
