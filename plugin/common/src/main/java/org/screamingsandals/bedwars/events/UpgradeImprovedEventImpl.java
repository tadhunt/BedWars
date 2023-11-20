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

package org.screamingsandals.bedwars.events;

import lombok.Data;
import org.screamingsandals.bedwars.api.events.UpgradeImprovedEvent;
import org.screamingsandals.bedwars.api.upgrades.Upgrade;
import org.screamingsandals.bedwars.api.upgrades.UpgradeStorage;
import org.screamingsandals.bedwars.game.GameImpl;
import org.screamingsandals.lib.event.CancellableEvent;

@Data
public class UpgradeImprovedEventImpl implements UpgradeImprovedEvent, CancellableEvent {
    private final GameImpl game;
    private final Upgrade upgrade;
    private final UpgradeStorage storage;
    private final double oldLevel;
    private final double originalNewLevel;
    private boolean cancelled;

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public void cancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public double getNewLevel() {
        return upgrade.getLevel();
    }

    @Override
    public void setNewLevel(double newLevel) {
        upgrade.setLevel(newLevel);
    }
}
