/*
 * Copyright (C) 2015-2017 jomp16 <root@rwx.ovh>
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

package ovh.rwx.utils.plugin.webapp.controllers.api.v1.habboimaging

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import ovh.rwx.habbo.HabboServer
import ovh.rwx.habbo.imaging.GroupBadge
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Controller
class HabboImagingApiV1Controller {
    private val badgeCache: MutableMap<String, ByteArray> = mutableMapOf()
    private val groupBadge = GroupBadge(
            HabboServer.habboGame.groupManager.groupBadgesBases,
            HabboServer.habboGame.groupManager.groupBaseColors,
            HabboServer.habboGame.groupManager.groupBadgesSymbols,
            HabboServer.habboGame.groupManager.groupBadgeSymbolColors
    )

    @RequestMapping(value = ["/api/v1/habbo-imaging/badge/{badge_code}.gif"], produces = [(MediaType.IMAGE_GIF_VALUE)], method = [(RequestMethod.GET)])
    @ResponseBody
    fun getBadgeGif(@PathVariable("badge_code") badgeCode: String) = getBadgeImage(badgeCode, "gif")

    @RequestMapping(value = ["/api/v1/habbo-imaging/badge/{badge_code}.png"], produces = [(MediaType.IMAGE_PNG_VALUE)], method = [(RequestMethod.GET)])
    @ResponseBody
    fun getBadgePng(@PathVariable("badge_code") badgeCode: String) = getBadgeImage(badgeCode, "png")

    private fun getBadgeImage(badgeCode: String, extension: String): ByteArray {
        val key = "$badgeCode.$extension"

        if (!badgeCache.containsKey(key)) {
            val badgeImage = groupBadge.getGroupBadge(badgeCode)
            val byteArrayOutputStream = ByteArrayOutputStream()

            ImageIO.write(badgeImage, extension, byteArrayOutputStream)

            badgeCache.put(key, byteArrayOutputStream.toByteArray())
        }

        return badgeCache[key]!!
    }
}