# Multiserver inventory

This project was created for a French Minecraft server but it's flexible enough for a quick implementation on your side. It requires only the AMQ library. The plugin allows you to link player inventories on several servers, which allows for servers containing many players to split their worlds into small servers to avoid CPU and memory overloads. It also synchronizes the ingame chat between the servers.

A RabbitMQ server is required to send player inventories. A fallback check verifies that the server has received the player's inventory data as well as the player's level, XPs and all the player's information in order to avoid loss or duplication of data. Must be used with Bungeecord.

# Configuration

- RabbitMQ credentials must be modified in the provided configuration file
- The name of the RabbitMQ channels can be changed in the configuration to use the Publish/Subscribe method. 
- The path of the data to be copied and the name of the world can also be modified.
- Debug mode: enable or disable, if you have any issue. It can help you to locate the issue and resolve it
- TTL expiry

# Limitations

This plugin only manages the sharing of player data when moving from one world to another, via a command that can be executed and linked to a portal for example.

It does not handle the synchronization of worlds.

# Tests

- Tested on a server with more than 90 players and with two/three different "servers".
- Tested on two different dedicated servers
- Tested with Minecraft Servers in Docker Containers

# Warning

In order to work at its best, dedicated servers must have the date and time synchronized, ideally to the a two second delay maximum. I advise to use an NTP server and a crontab to avoid time lag.
It is a necessity to have a synchronized time because the TTL of the RabbitMQ packets is checked with an overlay.
