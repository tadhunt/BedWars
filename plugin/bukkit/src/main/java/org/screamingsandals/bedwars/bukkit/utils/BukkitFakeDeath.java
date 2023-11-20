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

package org.screamingsandals.bedwars.bukkit.utils;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.nms.accessors.CombatTrackerAccessor;
import org.screamingsandals.bedwars.nms.accessors.LivingEntityAccessor;
import org.screamingsandals.bedwars.nms.accessors.PlayerAccessor;
import org.screamingsandals.bedwars.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.bedwars.player.BedWarsPlayer;
import org.screamingsandals.bedwars.utils.FakeDeath;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.ComponentAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class BukkitFakeDeath implements FakeDeath {
    @Override
    public boolean isAvailable() {
        return true;
    }

    public void die(BedWarsPlayer gamePlayer) {
        var player = gamePlayer.as(Player.class);
        if (player.isDead()) {
            return;
        }

        var loot = new ArrayList<ItemStack>();
        Collections.addAll(loot, player.getInventory().getContents());
        loot.removeIf(Objects::isNull); // remove nulls;

        var deathWorld = player.getWorld();
        var deathLoc = player.getLocation();

        String message = null;
        try {
            var combatTracker = Reflect.fastInvoke(ClassStorage.getHandle(player), LivingEntityAccessor.getMethodGetCombatTracker1());
            var component = Reflect.fastInvoke(combatTracker, CombatTrackerAccessor.getMethodGetDeathMessage1());
            message = (String) Reflect.fastInvoke(component, ComponentAccessor.getMethodFunc_150254_d1());
        } catch (Throwable ignored) {}

        var event = new PlayerDeathEvent(player, loot, player.getTotalExperience(), 0, message);
        Bukkit.getServer().getPluginManager().callEvent(event);

        for (var stack : event.getDrops()) {
            if (stack == null || stack.getType() == Material.AIR) continue;

            deathWorld.dropItem(deathLoc, stack);
        }

        player.closeInventory();

        var deathMessage = event.getDeathMessage();
        if (deathMessage != null && !deathMessage.trim().equals("") && Boolean.parseBoolean(deathWorld.getGameRuleValue("showDeathMessages"))) {
            Bukkit.broadcastMessage(deathMessage);
        }

        // TODO: find better way how to broadcast this effect and don't break the game

        /*
        try {
            Reflect.getMethod(ClassStorage.getHandle(deathWorld), "broadcastEntityEffect,func_72960_a", ClassStorage.NMS.Entity, byte.class)
                    .invoke(ClassStorage.getHandle(player), (byte) 3);
        } catch (Throwable t) {
        }
         */

        // ignoring PacketPlayOutCombatEvent, client probably didn't know that he died

        try {
            Reflect.fastInvoke(ClassStorage.getHandle(player), PlayerAccessor.getMethodRemoveEntitiesOnShoulder1());
        } catch (Throwable ignored) {}

        if (Server.isVersion(1, 16)) {
            try {
                Boolean b = deathWorld.getGameRuleValue(GameRule.FORGIVE_DEAD_PLAYERS);
                if (b != null && b) {
                    Reflect.fastInvoke(ClassStorage.getHandle(player), ServerPlayerAccessor.getMethodTellNeutralMobsThatIDied1());
                }
            } catch (Throwable ignored) {}
        }

        int i = event.getDroppedExp();
        while (i > 0) {
            int j = getOrbValue(i);
            i -= j;
            ((ExperienceOrb) deathWorld.spawnEntity(deathLoc, EntityType.EXPERIENCE_ORB)).setExperience(j);
        }

        if (!event.getKeepInventory()) {
            gamePlayer.invClean();
        }
        gamePlayer.resetLife();
        try {
            player.setKiller(null);
            player.setLastDamageCause(null);
        } catch (Throwable ignored) {}

        if (event.getKeepLevel() || event.getKeepInventory()) {
            player.setTotalExperience(event.getNewTotalExp());
            player.setLevel(event.getNewLevel());
            player.setExp(event.getNewExp());
        }

        try {
            Reflect.fastInvoke(ClassStorage.getHandle(player), ServerPlayerAccessor.getMethodSetCamera1(), ClassStorage.getHandle(player));
        } catch (Throwable ignored) {}

        try {
            Object combatTracker = Reflect.fastInvoke(ClassStorage.getHandle(player), LivingEntityAccessor.getMethodGetCombatTracker1());
            Reflect.fastInvoke(combatTracker, CombatTrackerAccessor.getMethodRecheckStatus1());
        } catch (Throwable ignored) {}

        // respawn location will be changed by PlayerListener
        var respawnEvent = new PlayerRespawnEvent(player, player.getLocation(), false);
        Bukkit.getServer().getPluginManager().callEvent(respawnEvent);

        gamePlayer.teleport(Location.fromPlatform(respawnEvent.getRespawnLocation()));
    }

    public int getOrbValue(int i) {
        if (i > 162670129) return i - 100000;
        if (i > 81335063) return 81335063;
        if (i > 40667527) return 40667527;
        if (i > 20333759) return 20333759;
        if (i > 10166857) return 10166857;
        if (i > 5083423) return 5083423;
        if (i > 2541701) return 2541701;
        if (i > 1270849) return 1270849;
        if (i > 635413) return 635413;
        if (i > 317701) return 317701;
        if (i > 158849) return 158849;
        if (i > 79423) return 79423;
        if (i > 39709) return 39709;
        if (i > 19853) return 19853;
        if (i > 9923) return 9923;
        if (i > 4957) return 4957;
        return i >= 2477 ? 2477 : (i >= 1237 ? 1237 : (i >= 617 ? 617 : (i >= 307 ? 307 : (i >= 149 ? 149 : (i >= 73 ? 73 : (i >= 37 ? 37 : (i >= 17 ? 17 : (i >= 7 ? 7 : (i >= 3 ? 3 : 1)))))))));
    }
}
