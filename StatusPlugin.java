package org.ultrapower.statusplugin;

import java.util.Collection;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class StatusPlugin extends Plugin {
    @Override
    public void onEnable() {
        // Registra il comando /status
        getProxy().getPluginManager().registerCommand(this, new StatusCommand());
    }

    private class StatusCommand extends Command {
        public StatusCommand() {
            super("status");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                // Esegui il comando /status list
                Collection<ServerInfo> servers = ProxyServer.getInstance().getServers().values();
                String message = "Ci sono " + servers.size() + " server collegati:";
                for (ServerInfo server : servers) {
                    int playerCount = server.getPlayers().size();
                    boolean isOnline = server.canAccess(sender);
                    message += "\n- " + server.getName() + ": " + (isOnline ? "online" : "offline");
                    if (isOnline) {
                        message += " con " + playerCount + " giocatori online.";
                    }
                }
                sender.sendMessage(ChatColor.GREEN + message);
            } else if (args.length == 1) {
                // Esegui il comando /status [nome del server]
                String serverName = args[0];
                ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);
                if (server == null) {
                    sender.sendMessage(ChatColor.RED + "Il server " + serverName + " non esiste.");
                } else {
                    int playerCount = ProxyServer.getInstance().getPlayers().stream()
                            .filter(p -> p.getServer().getInfo().equals(server))
                            .toArray().length;
                    boolean isOnline = server.canAccess(sender);
                    String message = serverName + " è " + (isOnline ? "online" : "offline");
                    if (isOnline) {
                        message += " con " + playerCount + " giocatori online.";
                    }
                    sender.sendMessage(ChatColor.GREEN + message);
                }
            }else {
                // Comando non valido
                sender.sendMessage(ChatColor.RED + "Utilizzo: /status [nome del server] o /status list");
            }
        }
    }
}