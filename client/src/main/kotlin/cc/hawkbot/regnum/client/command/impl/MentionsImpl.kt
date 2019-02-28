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

import cc.hawkbot.regnum.client.command.context.Mentions
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

/**
 * Implementation of [Mentions].
 * @param message the message
 * @property everyone whether the message mentions everyone or not
 * @constructor constructs a new MentionsImpl
 */
class MentionsImpl(
        private val message: Message
) : Mentions {

    override val everyone: Boolean = message.mentionsEveryone()

    override fun list(): List<IMentionable> {
        val list: MutableList<IMentionable> = message.mentionedChannels.toMutableList()
        list.addAll(message.mentionedUsers)
        list.addAll(message.mentionedRoles)
        return list
    }

    override fun list(vararg type: Message.MentionType): List<IMentionable> {
        return message.getMentions(*type)
    }

    override fun members(): List<Member> {
        return message.mentionedMembers
    }
}