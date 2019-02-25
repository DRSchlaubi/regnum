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

package cc.hawkbot.regnum.client.core.internal

import cc.hawkbot.regnum.client.Regnum
import cc.hawkbot.regnum.client.core.Websocket
import cc.hawkbot.regnum.client.core.discord.Discord
import cc.hawkbot.regnum.client.core.discord.GameAnimator
import net.dv8tion.jda.api.hooks.IEventManager
import java.util.function.Function

class RegnumImpl(
        host: String,
        override val eventManager: IEventManager,
        override val token: String,
        val games: MutableList<GameAnimator.Game>,
        val gameTranslator: Function<String, String>,
        val gameAnimatorInterval: Long
) : Regnum {

    override val websocket: Websocket
    override lateinit var discord: Discord

    init {
        eventManager.register(HeartBeater())
        eventManager.register(PacketHandler(this))
        websocket = WebsocketImpl(host, this)
        try {
            websocket.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}