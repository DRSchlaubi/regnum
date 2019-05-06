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

package cc.hawkbot.regnum.client.core

import cc.hawkbot.regnum.client.core.internal.WebsocketImpl
import cc.hawkbot.regnum.entities.Payload
import cc.hawkbot.regnum.net.PacketProcessor
import com.google.common.base.Preconditions
import java.io.Closeable

/**
 * Class to represent websocket connection.
 */
interface Websocket : Closeable {

    /**
     * The [Heart].
     */
    val heart: Heart

    val packetProcessor: PacketProcessor

    /**
     * Connects to the websocket.
     */
    fun start()

    /**
     * Sends a message to the server.
     * @param message the message
     */
    fun sendMessage(message: String)

    /**
     * Sends a payload to the server.
     * @param payload the payload
     */
    fun send(payload: Payload) = sendMessage(payload.toJson())

    /**
     * Returns the last ping between the client and server heartbeat.
     * @return the last ping between the client and server heartbeat
     */
    fun ping(): Int {
        Preconditions.checkArgument((this as WebsocketImpl).isHeartInitialized(), "Heart is not initialized yet")
        return heart.ping
    }
}