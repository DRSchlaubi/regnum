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

package cc.hawkbot.regnum.client.command.impl

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.command.CommandParser
import cc.hawkbot.regnum.client.command.ICommand
import cc.hawkbot.regnum.client.command.ISubCommand
import cc.hawkbot.regnum.client.command.permission.IPermissionProvider
import cc.hawkbot.regnum.client.util.EmbedUtil
import cc.hawkbot.regnum.client.util.Emotes
import cc.hawkbot.regnum.client.util.Misc
import cc.hawkbot.regnum.client.util.SafeMessage
import cc.hawkbot.regnum.util.logging.Logger
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.CompletableFuture

class CommandParserImpl(
        private val defaultPrefix: String,
        private val permissionProvider: IPermissionProvider,
        private val regnum: Regnum
) : CommandParser {

    private val log = Logger.getLogger()

    private val commandAssociations = mutableMapOf<String, ICommand>()

    override fun commands(): Map<String, ICommand> {
        return commandAssociations
    }

    override fun registerCommand(command: ICommand) {
        command.aliases.forEach { commandAssociations[it] = command }
    }

    override fun unregisterCommand(command: ICommand) {
        commandAssociations.remove(command.name())
    }

    @SubscribeEvent
    override fun receiveMessage(event: GuildMessageReceivedEvent) {
        // Basic checks
        if (event.author.isBot || event.isWebhookMessage) {
            return
        }
        // TODO: BLACK/WHITELIST
        // TODO: Custom prefix
        val guildPrefix = "-1"
        val mention = event.guild.selfMember.asMention
        val content = event.message.contentRaw
        val prefix = when {
            content.startsWith(defaultPrefix, true) -> defaultPrefix
            content.startsWith(mention, true) -> mention
            content.startsWith(guildPrefix, true) -> guildPrefix
            else -> return
        }
        // Cut off prefix
        val input = content.substring(prefix.length).trim()
        val rawArgs = input.split("\\s+".toRegex()).toTypedArray()
        val invoke = rawArgs[0].toLowerCase()
        if (invoke !in commandAssociations) {
            return
        }
        var command = commandAssociations[invoke]!!
        // Sub commands
        if (rawArgs.size >= 2) {
            val subInvoke = rawArgs[1].toLowerCase()
            if (subInvoke in command.subCommandAssociations)
                command = command.subCommandAssociations[subInvoke]!!
        }
        if (!permissionProvider.hasPermission(command.permissions, event.member)) {
            if (!permissionProvider.hasPermission(command.group.permissions, event.member)) {
                TODO("HANDLE PERMISSIONS")
            }
        }

        val args = if (command is ISubCommand)
            Arrays.copyOfRange(rawArgs, 2, rawArgs.size)
        else
            Arrays.copyOfRange(rawArgs, 1, rawArgs.size)
        val context = ContextImpl(event, command, ArgumentsImpl(args), MentionsImpl(event.message), regnum)

        try {
            command.execute(context.args, context)
        } catch (e: Exception) {
            handlerError(e, context)
        }
    }

    private fun handlerError(e: Exception, context: ContextImpl) {
        log.error("[CommandParser] An error occurred while executing command.", e)
        //TODO: Translations
        val future = SafeMessage.sendMessage(EmbedUtil.error("An unknown error occurred", Emotes.LOADING + "Collecting information about the error"), context.channel()).submit()
        try {
            collectErrorInformation(e, context, future)
        } catch (e: Exception) {
            log.error("[CommandParser] An error occurred while reporting error", e)
            future.thenApply { SafeMessage.editMessage(it, "ERROR: ${Misc.stringifyException(e)}") }
        }
    }

    private fun collectErrorInformation(e: Exception, context: ContextImpl, future: CompletableFuture<Message>) {
        val information = StringBuilder()
        val channel = context.channel()
        information.append("TextChannel: ").append("#").append(channel.name)
                .append("(").append(channel.id).append(")").append("\n")
        val guild = context.guild()
        information.append("Guild: ").append(guild.name).append("(").append(guild.id)
                .append(")").append("\n")
        val executor = context.author()
        information.append("Executor: ").append("@").append(executor.name).append("#")
                .append(executor.discriminator).append("(").append(executor.id).append(")")
                .append("\n")
        val selfMember = guild.selfMember
        information.append("Permissions: ").append(selfMember.permissions).append("\n")
        information.append("Channel permissions: ").append(selfMember.getPermissions(channel))
                .append("\n")
        information.append("Timestamp: ").append(LocalDateTime.now()).append("\n")
        val exception = Misc.stringifyException(e)
        information.append("Stacktrace: ").append("\n").append(exception)
        future
                .thenAccept { message ->
                    Misc.postToHastebinAsync(information.toString())
                            .thenAccept {
                                SafeMessage.editMessage(message, EmbedUtil.error("An error occurred", "Please report [this]($it) link to the devs"))
                            }
                }
                .exceptionally {
                    log.error("[CommandParser] An error occurred while reporting error", e)
                    return@exceptionally null
                }
    }

}