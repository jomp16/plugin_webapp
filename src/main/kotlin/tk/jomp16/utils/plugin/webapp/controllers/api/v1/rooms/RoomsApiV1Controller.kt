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