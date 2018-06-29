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

package ovh.rwx.utils.plugin.webapp.controllers.api.pocket

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ovh.rwx.habbo.database.user.UserInformationDao
import ovh.rwx.habbo.database.user.UserStatsDao
import ovh.rwx.habbo.game.user.information.UserInformation
import ovh.rwx.utils.plugin.webapp.controllers.api.pocket.json.AvatarPocketHabbo
import ovh.rwx.utils.plugin.webapp.controllers.api.pocket.json.LoginPocketHabbo
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*
import javax.servlet.http.HttpSession

@RestController
class PocketHabboController {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/api/public/info/hello")
    fun helloWorld(): Map<String, String> {
        log.debug("Got hello!")

        return mapOf("message" to "hello world")
    }

    @PostMapping("/api/public/authentication/login")
    fun login(@RequestBody loginPocketHabbo: LoginPocketHabbo, httpSession: HttpSession): Map<String, String> {
        log.debug("Got login!")

        val userInformation = UserInformationDao.getUserInformationByEmailAndPassword(loginPocketHabbo.email, loginPocketHabbo.password)
                ?: return mapOf("message" to "falha-login")

        httpSession.setAttribute("userInformation", userInformation)

        return mapOf("message" to "ok")
    }

    @GetMapping("/api/ssotoken")
    fun ssoToken(@SessionAttribute("userInformation") userInformation: UserInformation?): Map<String, String> {
        log.debug("Got ssotoken!")

        if (userInformation == null) return mapOf("message" to "error-auth")

        val ssoToken = UUID.randomUUID().toString()

        UserInformationDao.updateAuthTicket(userInformation, ssoToken)

        return mapOf("ssoToken" to ssoToken)
    }

    @GetMapping("/api/user/avatars")
    fun avatars(@SessionAttribute("userInformation") userInformation: UserInformation?): List<AvatarPocketHabbo> {
        log.debug("Got avatar!")

        if (userInformation == null) return emptyList()

        val userStats = UserStatsDao.getUserStats(userInformation.id)

        return listOf(AvatarPocketHabbo(
                userInformation.id,
                userInformation.figure,
                userInformation.motto,
                false,
                true,
                UUID.randomUUID().toString(),
                userStats.lastOnline.toEpochSecond(OffsetDateTime.now().offset),
                Instant.now().epochSecond
        ))
    }
}