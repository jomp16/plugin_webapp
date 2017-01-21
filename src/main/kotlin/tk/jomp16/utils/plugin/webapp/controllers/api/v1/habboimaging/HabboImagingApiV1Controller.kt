package tk.jomp16.utils.plugin.webapp.controllers.api.v1.habboimaging

import net.sf.ehcache.Ehcache
import net.sf.ehcache.Element
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import tk.jomp16.habbo.HabboServer
import tk.jomp16.habbo.imaging.GroupBadge
import tk.jomp16.habbo.kotlin.addAndGetEhCache
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Controller
class HabboImagingApiV1Controller {
    private val badgeCache: Ehcache = HabboServer.cacheManager.addAndGetEhCache("badgeCache")
    private val groupBadge = GroupBadge(HabboServer.habboGame.groupManager.badgesBases, HabboServer.habboGame.groupManager.badgeBaseColors, HabboServer.habboGame.groupManager.badgesSymbols, HabboServer.habboGame.groupManager.badgeSymbolColors)

    @RequestMapping(value = "/api/v1/habbo-imaging/badge/{badge_code}.gif", produces = arrayOf(MediaType.IMAGE_GIF_VALUE))
    @ResponseBody
    fun getBadgeGif(@PathVariable("badge_code") badgeCode: String) = getBadgeImage(badgeCode, "gif")

    @RequestMapping(value = "/api/v1/habbo-imaging/badge/{badge_code}.png", produces = arrayOf(MediaType.IMAGE_PNG_VALUE))
    @ResponseBody
    fun getBadgePng(@PathVariable("badge_code") badgeCode: String) = getBadgeImage(badgeCode, "png")

    private fun getBadge(badgeCode: String): BufferedImage {
        if (!badgeCache.isKeyInCache(badgeCode)) badgeCache.put(Element(badgeCode, groupBadge.getGroupBadge(badgeCode)))

        return badgeCache[badgeCode]!!.objectValue as BufferedImage
    }

    private fun getBadgeImage(badgeCode: String, extension: String): ByteArray {
        val key = "$badgeCode.$extension"

        if (!badgeCache.isKeyInCache(key)) {
            val badgeImage = getBadge(badgeCode)

            val byteArrayOutputStream = ByteArrayOutputStream()

            ImageIO.write(badgeImage, extension, byteArrayOutputStream)

            badgeCache.put(Element(key, byteArrayOutputStream.toByteArray()))
        }

        return badgeCache[key]!!.objectValue as ByteArray
    }
}