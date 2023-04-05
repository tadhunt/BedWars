package org.screamingsandals.bedwars.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.screamingsandals.bedwars.Main;

public class TeamsConfig {
    private HashMap<UUID, TeamConfig> teams;
    private HashMap<UUID, Boolean> spectators;

    public TeamsConfig() {
        this.teamsInit();
        this.spectatorsInit();
    }

    private void teamsInit() {
        this.teams = new HashMap<UUID, TeamConfig>();

        try {
            List<?> cfg = Main.getConfigurator().config.getList("teams");

            if (cfg == null) {
                return;
            }

            Bukkit.getConsoleSender().sendMessage("teams type: " + cfg.getClass().getName());

            for(int i = 0; i < cfg.size(); i++) {
                Object tc = cfg.get(i);
                Bukkit.getConsoleSender().sendMessage(String.format("team %d: type %s", i, tc.getClass().getName()));

                LinkedHashMap<String,?> h = (LinkedHashMap) tc;

                String name = (String) h.get("name");
                ArrayList<String> members = (ArrayList) h.get("members");

                TeamConfig teamConfig = new TeamConfig(name, members);

                for (int j = 0; j < teamConfig.members.size(); j++) {
                    UUID uuid = (UUID) teamConfig.members.get(j);
                    this.teams.put(uuid, teamConfig);
                }
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("bad config: " + e.toString());
        }
    }
    private void spectatorsInit() {
        this.spectators = new HashMap<UUID, Boolean>();

        List<?> spectators = Main.getConfigurator().config.getList("spectators");
    
        if (spectators == null) {
            return;
        }
    
        for (int i = 0; i < spectators.size(); i++) {
            String spectator;
            try {
                spectator = (String) spectators.get(i);
            } catch(Exception e) {
                Bukkit.getConsoleSender().sendMessage("bad config expected String got: " + spectators.get(i).getClass().getName());
                continue;
            }
            if (spectator == null) {
                Bukkit.getConsoleSender().sendMessage(String.format("bad config: spectator %d is null", i));
                continue;
            }
    
            UUID uuid;
            try {
                uuid = UUID.fromString(spectator);
            } catch(Exception e) {
                Bukkit.getConsoleSender().sendMessage(String.format("bad config: spectator %d (%s) not a UUID", i, spectator));
                continue;
            }
            
            this.spectators.put(uuid, true);
        }    
    }    

    public TeamConfig getTeam(UUID uuid) {
        return this.teams.get(uuid);
    }

    public Boolean isSpectator(UUID uuid) {
        return this.spectators.containsKey(uuid);
    }
}
