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

package tk.jomp16.utils.plugin.webapp.controllers.api.v1.status

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.jomp16.habbo.BuildConfig
import tk.jomp16.habbo.HabboServer
import tk.jomp16.habbo.util.Utils
import java.lang.management.ManagementFactory
import java.util.concurrent.TimeUnit

@RestController
class StatusApiV1Controller {
    @RequestMapping("/api/v1/status")
    fun serverStatus(): Map<String, Any> = mapOf(
            "version" to BuildConfig.VERSION,
            "used_ram" to Utils.ramUsageString,
            "users_online" to HabboServer.habboSessionManager.habboSessions.size,
            "rooms_loaded" to HabboServer.habboGame.roomManager.roomTaskManager.rooms.size,
            "uptime_seconds" to TimeUnit.MILLISECONDS.toSeconds(ManagementFactory.getRuntimeMXBean().uptime)
    )
}