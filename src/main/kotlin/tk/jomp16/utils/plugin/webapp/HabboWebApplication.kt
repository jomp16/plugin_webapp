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

package tk.jomp16.utils.plugin.webapp

import ro.pippo.core.Application
import tk.jomp16.habbo.BuildConfig
import tk.jomp16.habbo.HabboServer
import tk.jomp16.habbo.database.information.UserInformationDao
import tk.jomp16.habbo.game.room.Room
import tk.jomp16.habbo.game.user.information.UserInformation
import tk.jomp16.habbo.imaging.GroupBadge
import tk.jomp16.habbo.util.Utils
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.management.ManagementFactory
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

class HabboWebApplication : Application() {
    lateinit var groupBadgeCache: MutableMap<String, ByteArray>
    lateinit var groupBadge: GroupBadge

    override fun onInit() {
        super.onInit()

        groupBadgeCache = HashMap()
        groupBadge = GroupBadge(HabboServer.habboGame.groupManager.groupBadgesBases, HabboServer.habboGame.groupManager.groupBaseColors, HabboServer.habboGame.groupManager.groupBadgesSymbols, HabboServer.habboGame.groupManager.groupBadgeSymbolColors)

        GET("/api/v1/users/connected", {
            it.response.json().send(HabboServer.habboSessionManager.habboSessions.values.filter { it.authenticated }.map { it.userInformation.id })
        })

        GET("/api/v1/users/{id}", {
            val userInformation = UserInformationDao.getUserInformationById(it.request.getParameter("id").toInt())

            if (userInformation == null) {
                it.response.json().send(
                        mapOf(
                                "error" to "User not found"
                        )
                )

                return@GET
            }

            if (userInformation is UserInformation) {
                it.response.json().send(
                        mapOf(
                                "id" to userInformation.id,
                                "username" to userInformation.username,
                                "real_name" to userInformation.realname,
                                "figure" to userInformation.figure,
                                "gender" to userInformation.gender,
                                "motto" to userInformation.motto,
                                "vip" to userInformation.vip
                        )
                )
            }
        })

        GET("/api/v1/rooms/loaded", {
            it.response.json().send(HabboServer.habboGame.roomManager.roomTaskManager.rooms.map { it.roomData.id })
        })

        GET("/api/v1/rooms/{id}", {
            val room = HabboServer.habboGame.roomManager.rooms[it.request.getParameter("id").toInt()]

            if (room == null) {
                it.response.json().send(
                        mapOf(
                                "error" to "Room not found"
                        )
                )

                return@GET
            }

            if (room is Room) {
                it.response.json().send(
                        mapOf(
                                "id" to room.roomData.id,
                                "name" to room.roomData.name,
                                "description" to room.roomData.description,
                                "owner_id" to room.roomData.ownerId,
                                "group_id" to room.roomData.groupId,
                                "users_now" to room.roomUsers.size
                        )
                )
            }
        })

        GET("/api/v1/status", {
            it.response.json().send(mapOf(
                    "version" to BuildConfig.VERSION,
                    "used_ram" to Utils.ramUsageString,
                    "users_online" to HabboServer.habboSessionManager.habboSessions.size,
                    "rooms_loaded" to HabboServer.habboGame.roomManager.roomTaskManager.rooms.size,
                    "uptime_seconds" to TimeUnit.MILLISECONDS.toSeconds(ManagementFactory.getRuntimeMXBean().uptime)
            ))
        })

        GET("/api/v1/habbo-imaging/badge/{badge_code}", {
            val badgeCodeFile = File(it.getParameter("badge_code").toString(""))
            val badgeCode = badgeCodeFile.nameWithoutExtension
            val extension = if (badgeCodeFile.extension.isEmpty()) "gif" else badgeCodeFile.extension

            if (!groupBadgeCache.containsKey(badgeCode)) {
                val badgeImage = groupBadge.getGroupBadge(badgeCode)

                val byteArrayOutputStream = ByteArrayOutputStream()

                ImageIO.write(badgeImage, extension, byteArrayOutputStream)

                groupBadgeCache.put("$badgeCode.$extension", byteArrayOutputStream.toByteArray())
            }

            val badgeImageByteArray = groupBadgeCache[badgeCodeFile.name]

            it.response.contentType("image/$extension")
            it.response.characterEncoding(null)
            it.response.outputStream.write(badgeImageByteArray)
        })
    }
}