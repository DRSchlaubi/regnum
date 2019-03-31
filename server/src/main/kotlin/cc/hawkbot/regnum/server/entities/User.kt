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

package cc.hawkbot.regnum.server.entities

import cc.hawkbot.regnum.entites.cassandra.CassandraEntity
import cc.hawkbot.regnum.entites.cassandra.SnowflakeCassandraEntity
import com.datastax.driver.mapping.Result
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.Param
import com.datastax.driver.mapping.annotations.Query
import com.datastax.driver.mapping.annotations.Table

/**
 * Entity for users.
 */
@Suppress("unused")
@Table(name = CassandraEntity.TABLE_PREFIX + "user")
class User : SnowflakeCassandraEntity<User> {

    /**
     * The language tag.
     */
    @Column(name = "language_tag")
    var languageTag = "en-US"

    constructor(id: Long) : super(id)

    constructor() : this(-1)

    @com.datastax.driver.mapping.annotations.Accessor
    interface Accessor {
        @Query("SELECT * FROM " + CassandraEntity.TABLE_PREFIX + "user WHERE id = :id")
        operator fun get(@Param("id") id: Long): Result<User>
    }

}
