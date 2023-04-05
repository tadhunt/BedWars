package org.screamingsandals.bedwars.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.screamingsandals.bedwars.Main;

public class TeamsConfig {
    private HashMap<UUID, TeamConfig> teams;

    public TeamsConfig() {
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

    public TeamConfig UuidToTeam(UUID uuid) {
        return this.teams.get(uuid);
    }
}
