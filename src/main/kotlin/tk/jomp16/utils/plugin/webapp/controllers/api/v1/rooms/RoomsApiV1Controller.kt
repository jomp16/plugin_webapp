/*
 * Copyright (C) 2015-2017 jomp16
 *
 * This file is part of habbo_r63b_v2.
 *
 * habbo_r63b_v2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * habbo_r63b_v2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with habbo_r63b_v2. If not, see <http://www.gnu.org/licenses/>.
 */

package tk.jomp16.utils.plugin.webapp.controllers.api.v1.rooms

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.jomp16.habbo.HabboServer
import tk.jomp16.habbo.game.room.Room

@RestController
class RoomsApiV1Controller {
    @RequestMapping("/api/v1/rooms")
    fun getRooms(): List<Int> = HabboServer.habboGame.roomManager.rooms.values.map { it.roomData.id }

    @RequestMapping("/api/v1/rooms/{id}")
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