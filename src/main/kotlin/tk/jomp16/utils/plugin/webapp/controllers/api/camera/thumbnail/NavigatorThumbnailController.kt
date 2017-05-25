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

package tk.jomp16.utils.plugin.webapp.controllers.api.camera.thumbnail

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import tk.jomp16.habbo.HabboServer
import tk.jomp16.utils.plugin.webapp.controllers.api.camera.CameraImageNotFoundException
import java.nio.file.Files

@Controller
class NavigatorThumbnailController {
    @RequestMapping(value = "/api/camera/navigator-thumbnail/{room_id}.png", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun navigatorThumbnail(@PathVariable("room_id") roomId: Int): ByteArray {
        val cameraThumbnailPath = HabboServer.habboGame.cameraManager.cameraNavigatorThumbnailDirectory.resolve("$roomId.png")

        if (Files.notExists(cameraThumbnailPath)) throw CameraImageNotFoundException()

        return cameraThumbnailPath.toFile().readBytes()
    }
}