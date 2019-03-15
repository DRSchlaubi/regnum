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

package cc.hawkbot.regnum.client.command.permission

/**
 * Class that represents entities which can hold permissions
 */
@Suppress("unused")
interface IPermissionHolder {

    /**
     * Returns whether an entity has a permission or not.
     * @return whether an entity has a permission or not
     */
    fun hasPermission(permission: IPermissions): Boolean

    fun assignPermission(permission: IPermissions, negated: Boolean = false)

    fun deletePermissionAssignment(permission: IPermissions)

    fun assignNegatedPermission(permission: IPermissions) {
        assignPermission(permission, true)
    }
}