/*
 * Copyright (C) 2015-2017 jomp16
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

package tk.jomp16.utils.plugin.webapp.controllers.api.camera

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import tk.jomp16.habbo.HabboServer
import java.nio.file.Files

@Controller
class CameraController {
    @RequestMapping(value = ["/api/camera/{type}/{username}/{camera_preview}.png"], method = [(RequestMethod.GET)])
    @ResponseBody
    fun cameraPreview(@PathVariable("type") type: String, @PathVariable("username") username: String, @PathVariable("camera_preview") cameraPreviewName: String): ByteArray {
        if (type != "preview" && type != "purchased") throw CameraMethodNotImplementedException()
        val cameraPath = (if (type == "preview") HabboServer.habboGame.cameraManager.cameraPreviewDirectory
        else HabboServer.habboGame.cameraManager.cameraPurchasedDirectory).resolve("$username/$cameraPreviewName.png")

        if (Files.notExists(cameraPath)) throw CameraImageNotFoundException()

        return cameraPath.toFile().readBytes()
    }
}