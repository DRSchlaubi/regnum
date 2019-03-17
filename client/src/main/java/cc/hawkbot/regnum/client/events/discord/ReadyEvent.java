/*
 * Regnum - A Discord bot clustering system made for Hawk
 *
 * Copyright (C) 2019  Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package cc.hawkbot.regnum.client.events.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * Event that is fired when all Discord shards are connected and received READY event.
 */
public class ReadyEvent extends net.dv8tion.jda.api.events.ReadyEvent {

    private final int availableGuilds;
    private final int unavailableGuilds;

    /**
     * Constructs a new ready event.
     *
     * @param shardManager      the shard manager
     * @param availableGuilds   the count of available guilds
     * @param unavailableGuilds the count of unavailable guilds
     * @see net.dv8tion.jda.api.events.ReadyEvent#ReadyEvent(JDA, long)
     */
    @SuppressWarnings("unused")
    public ReadyEvent(ShardManager shardManager, int availableGuilds, int unavailableGuilds) {
        super(shardManager.getShards().get(0), 200);
        this.availableGuilds = availableGuilds;
        this.unavailableGuilds = unavailableGuilds;
    }


    /**
     * Returns the count of available guilds.
     *
     * @return the count of available guilds
     * @see net.dv8tion.jda.api.events.ReadyEvent#getGuildAvailableCount()
     */
    @Override
    public int getGuildAvailableCount() {
        return availableGuilds;
    }

    /**
     * Returns the count of unavailable guilds.
     *
     * @return the count of unavailable guilds
     * @see net.dv8tion.jda.api.events.ReadyEvent#getGuildUnavailableCount()
     */
    @Override
    public int getGuildUnavailableCount() {
        return unavailableGuilds;
    }
}