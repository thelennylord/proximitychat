package me.thelennylord.ProximityChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

public class ProximityChatEvent implements CommandExecutor, Listener {

    private Main plugin;
    private final ArrayList<UUID> globalChatQueue = new ArrayList<>();
    private final Hashtable<UUID, Long> globalChatCooldown = new Hashtable<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;

            if (strings.length == 0) {
                sender.sendMessage(ChatColor.RED + "Incorrect usage. Usage: /globalchat <message>");
                return true;
            }

            // check if user has cooldown
            if (!sender.hasPermission("proximitychat.bypassCooldown")) {
                if (globalChatCooldown.containsKey(sender.getUniqueId())) {
                    Long expireTime = globalChatCooldown.get(sender.getUniqueId());
                    Long now = Instant.now().getEpochSecond();
                    long diff = expireTime - now;

                    if ( diff < 0) {
                        globalChatCooldown.remove(sender.getUniqueId());
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "You cannot talk in global for another %s seconds.".replace("%s", String.valueOf(diff)));
                        return true;
                    }

                } else {
                    globalChatCooldown.put(sender.getUniqueId(), Long.sum(Instant.now().getEpochSecond(), plugin.getConfig().getLong("global-chat-cooldown")));
                }
            }

            globalChatQueue.add(sender.getUniqueId());
            sender.chat(String.join(" ", strings));

        } else {
            commandSender.sendMessage("You must be a player to use this command!");
        }

        return true;
    }

    @EventHandler
    public void onChatEvent(PlayerChatEvent e) {
        Player p = e.getPlayer();

        if (globalChatQueue.contains(p.getUniqueId())) {
            e.setFormat(ChatColor.GREEN + "[GLOBAL] " + ChatColor.RESET + e.getFormat());
            globalChatQueue.remove(p.getUniqueId());

        } else {

            if (plugin.getConfig().getBoolean("show-proximity-prefix")) {
                e.setFormat(ChatColor.AQUA + "[PROXIMITY] " + ChatColor.RESET + e.getFormat());
            }

            int x = plugin.getConfig().getInt("radius.x");
            int y = plugin.getConfig().getInt("radius.y");
            int z = plugin.getConfig().getInt("radius.z");
            Entity[] nearbyPlayers = p.getNearbyEntities(x, y, z).stream().filter(en -> en instanceof Player).toArray(Entity[]::new);

            // set recipients to nearby players only
            e.getRecipients().removeIf(player -> !this.contains(nearbyPlayers, player));
            e.getRecipients().add(p);
        }
    }

    public void setInstance(Main main) {
        this.plugin = main;
    }

    private boolean contains(Entity[] array, Player p) {
        for (Entity plr : array) {
            if (plr.equals(p)) {
                return true;
            }
        }
        return false;
    }
}
