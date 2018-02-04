/*
 * Copyright (C) 2015-2018 jomp16 <root@rwx.ovh>
 *
 * This file is part of plugin_webapp.
 *
 * plugin_webapp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * plugin_webapp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with plugin_webapp. If not, see <http://www.gnu.org/licenses/>.
 */

package ovh.rwx.utils.plugin.webapp.controllers.api.v1.rooms

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ovh.rwx.habbo.HabboServer
import ovh.rwx.habbo.game.room.Room

@RestController
class RoomsApiV1Controller {
    @GetMapping("/api/v1/rooms")
    fun getRooms(): List<Int> = HabboServer.habboGame.roomManager.rooms.values.map { it.roomData.id }

    @GetMapping("/api/v1/rooms/{id}")
    fun getRoomInfoById(@PathVariable("id") roomId: Int): Map<String, Any> = getRoomInfoMap(HabboServer.habboGame.roomManager.rooms[roomId])

    private fun getRoomInfoMap(room: Room?) =
            if (room == null) mapOf("error" to "Room not found")
            else mapOf(
                    "id" to room.roomData.id,
                    "name" to room.roomData.name,
                    "description" to room.roomData.description,
                    "owner_id" to room.roomData.ownerId,
                    "group_id" to room.roomData.groupId,
                    "users_now" to room.roomUsers.size
            )
}