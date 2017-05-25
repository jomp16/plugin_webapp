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

package tk.jomp16.utils.plugin.webapp.controllers.api.camera

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class CameraController {
    @RequestMapping(value = "/api/camera/{type}/{username}/{camera_preview}.png", method = arrayOf(org.springframework.web.bind.annotation.RequestMethod.GET))
    @ResponseBody
    fun cameraPreview(@PathVariable("type") type: String, @PathVariable("username") username: String, @PathVariable("camera_preview") cameraPreviewName: String): ByteArray {
        if (type != "preview" && type != "purchased") throw CameraMethodNotImplementedException()

        val cameraPath =
                (if (type == "preview") tk.jomp16.habbo.HabboServer.habboGame.cameraManager.cameraPreviewDirectory
                else tk.jomp16.habbo.HabboServer.habboGame.cameraManager.cameraPurchasedDirectory)
                        .resolve("$username/$cameraPreviewName.png")

        if (java.nio.file.Files.notExists(cameraPath)) throw CameraImageNotFoundException()

        return cameraPath.toFile().readBytes()
    }
}