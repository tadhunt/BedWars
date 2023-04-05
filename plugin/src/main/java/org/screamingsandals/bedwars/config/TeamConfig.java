package org.screamingsandals.bedwars.config;

import java.util.ArrayList;
import java.util.UUID;

public class TeamConfig {
    public String name;
    public ArrayList<UUID> members;

    public TeamConfig(String name, ArrayList<String> members) {
        this.name = name;
        this.members = new ArrayList<UUID>();
        
        for(int i = 0; i < members.size(); i++) {
            String member = members.get(i);
            UUID memberUuid = UUID.fromString(member);
            this.members.add(memberUuid);
        }
    }
}
