/*
 * Copyright (C) 2023 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.bedwars.game;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;

public class Team implements Cloneable, org.screamingsandals.bedwars.api.Team {
    public TeamColor color;
    public boolean newColor;
    public String name;
    public Location bed;
    public Location spawn;
    public int maxPlayers;
    public @Nullable String saveName;
    public Game game;
    public HashMap<UUID, Boolean> members;

    public Team clone() {
        Team t = new Team();
        t.color = this.color;
        t.newColor = this.newColor;
        t.name = this.name;
        t.bed = this.bed;
        t.spawn = this.spawn;
        t.maxPlayers = this.maxPlayers;
        t.game = this.game;

        if (this.members != null) {
            t.members = new HashMap<UUID, Boolean>();
            for (UUID member : t.members.keySet()) {
                t.members.put(member, true);
            }
        }

        t.saveName = this.saveName;
        return t;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public org.screamingsandals.bedwars.api.TeamColor getColor() {
        return color.toApiColor();
    }

    @Override
    public boolean isNewColor() {
        return newColor;
    }

    @Override
    public Location getTeamSpawn() {
        return spawn;
    }

    @Override
    public Location getTargetBlock() {
        return bed;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public Game getGame() {
        return game;
    }

    public Boolean isMember(UUID uuid) {
        if (members == null) {
            return false;
        }

        return members.containsKey(uuid);

    public String getSaveName() {
        return saveName != null ? saveName : name.replace('.', '_').replace(' ', '_');
    }
}
