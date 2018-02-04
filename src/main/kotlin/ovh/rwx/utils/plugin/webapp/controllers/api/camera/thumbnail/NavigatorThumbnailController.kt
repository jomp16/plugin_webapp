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

package ovh.rwx.utils.plugin.webapp.controllers.api.camera.thumbnail

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import ovh.rwx.habbo.HabboServer
import ovh.rwx.utils.plugin.webapp.controllers.api.camera.CameraImageNotFoundException
import java.nio.file.Files

@Controller
class NavigatorThumbnailController {
    @GetMapping(value = ["/api/camera/navigator-thumbnail/{room_id}.png"])
    @ResponseBody
    fun navigatorThumbnail(@PathVariable("room_id") roomId: Int): ByteArray {
        val cameraThumbnailPath = HabboServer.habboGame.cameraManager.cameraNavigatorThumbnailDirectory.resolve("$roomId.png")

        if (Files.notExists(cameraThumbnailPath)) throw CameraImageNotFoundException()

        return cameraThumbnailPath.toFile().readBytes()
    }
}