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

package cc.hawkbot.regnum.client;

import cc.hawkbot.regnum.client.command.ICommand;
import cc.hawkbot.regnum.client.command.impl.PermissionProviderImpl;
import cc.hawkbot.regnum.client.command.permission.IPermissionProvider;
import cc.hawkbot.regnum.client.core.discord.GameAnimator;
import cc.hawkbot.regnum.client.core.internal.RegnumImpl;
import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.IEventManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Builder for {@link Regnum} instances
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RegnumBuilder {

    private String host;
    private String token;
    private Function<String, String> gameTranslator = string -> string;
    private long gameAnimatorInterval = 30;
    private IEventManager eventManager = new AnnotatedEventManager();
    private List<Object> eventListeners = new ArrayList<>();
    private List<GameAnimator.Game> games = new ArrayList<>();
    private IPermissionProvider permissionProvider = new PermissionProviderImpl();
    private String defaultPrefix;
    private List<ICommand> commands = new ArrayList<>();
    private List<Long> owners = new ArrayList<>();

    /**
     * Returns the current host of the Regnum server.
     *
     * @return the current host of the Regnum server
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host of the Regnum server.
     *
     * @param host the host
     * @return the current builder
     */
    public RegnumBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Returns the current event manager {@link IEventManager}
     *
     * @return the current event manager {@link IEventManager}
     */
    public IEventManager getEventManager() {
        return eventManager;
    }

    /**
     * Sets the event manager {@link IEventManager} that is used by {@link Regnum} and the {@link cc.hawkbot.regnum.client.core.discord.Discord}.
     *
     * @param eventManager the event manager
     * @return the current builder
     */
    public RegnumBuilder setEventManager(@NotNull IEventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }

    /**
     * Returns the current list of event listeners.
     *
     * @return the current list of event listeners
     */
    public List<Object> getEventListeners() {
        return eventListeners;
    }

    /**
     * Sets the list of event listeners for {@link Regnum} and {@link cc.hawkbot.regnum.client.core.discord.Discord}.
     *
     * @param listeners the list of listeners
     * @return the current builder
     */
    public RegnumBuilder setEventListeners(List<Object> listeners) {
        this.eventListeners = listeners;
        return this;
    }

    /**
     * Registers event listeners.
     *
     * @param listeners the listeners
     * @return the current builder
     */
    public RegnumBuilder registerEvents(Object... listeners) {
        Collections.addAll(eventListeners, listeners);
        return this;
    }

    /**
     * Registers event listeners.
     *
     * @param listeners the listeners
     * @return the current builder
     */
    public RegnumBuilder registerEvents(Collection<Object> listeners) {
        eventListeners.addAll(listeners);
        return this;
    }

    /**
     * Returns the current token for Regnum authentication.
     *
     * @return the current token or {@code null} if it doesn't exists
     */
    @Nullable
    public String getToken() {
        return token;
    }

    /**
     * Sets the token that is used for Regnum authentication.
     *
     * @param token the token
     * @return the current builder
     */
    public RegnumBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    /**
     * Returns the current list of games for the game animator {@link GameAnimator}.
     *
     * @return the current list of games for the game animator {@link GameAnimator}
     */
    public List<GameAnimator.Game> getGames() {
        return games;
    }

    /**
     * Sets the list of games for the game animator {@link GameAnimator}.
     *
     * @param games the list
     * @return the current builder
     */
    public RegnumBuilder setGames(List<GameAnimator.Game> games) {
        this.games = games;
        return this;
    }

    /**
     * Adds an array of games for the game animator {@link GameAnimator}.
     *
     * @param games the array
     * @return the current builder
     */
    public RegnumBuilder addGames(GameAnimator.Game... games) {
        Collections.addAll(this.games, games);
        return this;
    }

    /**
     * Adds a collection of games for the game animator {@link GameAnimator}.
     *
     * @param games the collection
     * @return the current builder
     */
    public RegnumBuilder addGames(Collection<GameAnimator.Game> games) {
        this.games.addAll(games);
        return this;
    }

    /**
     * Returns the current variable translator used by the {@link GameAnimator}.
     *
     * @return the current variable translator used by the {@link GameAnimator}
     */
    public Function<String, String> getGameTranslator() {
        return gameTranslator;
    }

    /**
     * Sets the variable translator used by the {@link GameAnimator}.
     *
     * @param gameTranslator the translator
     * @return the current builder
     */
    public RegnumBuilder setGameTranslator(Function<String, String> gameTranslator) {
        this.gameTranslator = gameTranslator;
        return this;
    }

    /**
     * Returns the current interval of the {@link GameAnimator}.
     *
     * @return the current interval of the {@link GameAnimator}
     */
    public long getGameAnimatorInterval() {
        return gameAnimatorInterval;
    }

    /**
     * Sets the interval of the {@link GameAnimator}.
     *
     * @param gameAnimatorInterval the interval
     * @return the current builder
     */
    public RegnumBuilder setGameAnimatorInterval(long gameAnimatorInterval) {
        this.gameAnimatorInterval = gameAnimatorInterval;
        return this;
    }

    /**
     * Returns the {@link IPermissionProvider}.
     * @return the {@link IPermissionProvider}
     */
    public IPermissionProvider getPermissionProvider() {
        return permissionProvider;
    }

    /**
     * Sets the {@link IPermissionProvider}.
     *
     * @param permissionProvider the provider
     * @return the current builder
     */
    public RegnumBuilder setPermissionProvider(IPermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
        return this;
    }


    /**
     * Returns the default prefix used by the {@link cc.hawkbot.regnum.client.command.CommandParser}.
     * @return the default prefix used by the {@link cc.hawkbot.regnum.client.command.CommandParser}
     */
    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    /**
     * Sets the default prefix used by the {@link cc.hawkbot.regnum.client.command.CommandParser}.
     * @param defaultPrefix the prefix
     * @return the builder
     */
    public RegnumBuilder setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
        return this;
    }

    /**
     * Returns the list of registered commands.
     * @return the list of registered commands
     */
    public List<ICommand> getCommands() {
        return commands;
    }

    /**
     * Sets the list of registered commands.
     * @param commands the list
     * @return the builder
     */
    public RegnumBuilder setCommands(List<ICommand> commands) {
        this.commands = commands;
        return this;
    }

    /**
     * Registers a command.
     * @param commands the command
     * @return the builder
     */
    public RegnumBuilder registerCommands(ICommand... commands) {
        Collections.addAll(this.commands, commands);
        return this;
    }

    /**
     * Registers a command.
     * @param commands the command
     * @return the builder
     */
    public RegnumBuilder registerCommands(Collection<ICommand> commands) {
        this.commands.addAll(commands);
        return this;
    }

    /**
     * Returns a list of all ids with owner permissions.
     * @return a list of all ids with owner permissions
     */
    public List<Long> getOwners() {
        return owners;
    }

    /**
     * Sets the list for all ids with owner permissions used by the {@link cc.hawkbot.regnum.client.command.CommandParser}
     * @param owners the list
     * @return the builder
     */
    public RegnumBuilder setOwners(List<Long> owners) {
        this.owners = owners;
        return this;
    }

    /**
     * Assigns an user owner permissions
     * @param ownerIds the id of the user
     * @return the builder
     */
    public RegnumBuilder addOwners(Long... ownerIds) {
        Collections.addAll(owners, ownerIds);
        return this;
    }

    /**
     * Assigns an user owner permissions
     * @param ownerIds the id of the user
     * @return the builder
     */
    public RegnumBuilder addOwners(Collection<Long> ownerIds) {
        owners.addAll(ownerIds);
        return this;
    }

    /**
     * Build a new {@link Regnum} instance and connects to the server
     *
     * @return the instance
     * @throws NullPointerException When a required argument is null
     */
    public Regnum build() {
        // Null checks
        Preconditions.checkNotNull(host, "Host may not be null");
        Preconditions.checkNotNull(token, "Token may not be null");
        Preconditions.checkNotNull(eventListeners, "Event listeners may not be null");
        Preconditions.checkNotNull(games, "Games may not be null");
        Preconditions.checkNotNull(gameTranslator, "Game translator may not be null");
        Preconditions.checkNotNull(permissionProvider, "Permission provider may not be null");
        Preconditions.checkNotNull(defaultPrefix, "Prefix may not be null");

        // Register events
        eventListeners.forEach(eventManager::register);

        // Build
        return new RegnumImpl(
                host,
                eventManager,
                token,
                games,
                gameTranslator,
                gameAnimatorInterval,
                permissionProvider,
                defaultPrefix,
                commands,
                owners
        );
    }
}
