package org.ultrapower.statusplugin;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;

public class StatusPlugin extends Plugin {

    public void onEnable() {
        getLogger().info("StatusPlugin abilitato");
        getProxy().getPluginManager().registerCommand(this, new StatusCommand());
    }

    private class StatusCommand extends Command {

        public StatusCommand() {
            super("status");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if(sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer)sender;
                ServerInfo server;

                if (args.length > 0) {
                    String serverName = args[0];
                    server = ProxyServer.getInstance().getServerInfo(serverName);
                    if (server == null) {
                        player.sendMessage("Server non trovato");
                        return;
                    }
                } else {
                    server = ProxyServer.getInstance().getServerInfo("PUTSERVERNAME");
                }

                ProxyServer.getInstance().getServerInfo(server.getName()).ping(new Callback<ServerPing>() {
                    public void done(ServerPing ping, Throwable error) {
                        if (error == null) {
                            player.sendMessage("------------------------------------");
                            player.sendMessage("Server Status " + "PUTSERVERNAME" + ":");
                            player.sendMessage("Online: " + "✓");
                            player.sendMessage("Players Online: " + ping.getPlayers().getOnline());
                            player.sendMessage("------------------------------------");
                        } else {
                            player.sendMessage("------------------------------------");
                            player.sendMessage("Server Status " + "PUTSERVERNAME" + ": " + " ✕");
                            player.sendMessage("------------------------------------");
                        }
                    }
                });
            }
        }
    }
}