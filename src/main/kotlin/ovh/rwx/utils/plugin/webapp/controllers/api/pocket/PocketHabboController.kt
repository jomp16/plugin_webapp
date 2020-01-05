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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        return mapOf("message" to "hello world")
    }

    @PostMapping("/api/public/authentication/login")
    fun login(@RequestBody loginPocketHabbo: LoginPocketHabbo, httpSession: HttpSession): ResponseEntity<Map<String, Any>>? {
        val userInformation = UserInformationDao.getUserInformationByEmailAndPassword(loginPocketHabbo.email, loginPocketHabbo.password)
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to "login.invalid_password", "captcha" to false))

        httpSession.setAttribute("userInformation", userInformation)

        return ResponseEntity.ok().body(null)
    }

    @PostMapping("/api/public/authentication/logout")
    fun logout(httpSession: HttpSession): ResponseEntity<Nothing> {
        httpSession.invalidate()

        return ResponseEntity.ok().body(null)
    }

    @GetMapping("/api/ssotoken")
    fun ssoToken(@SessionAttribute("userInformation") userInformation: UserInformation?): ResponseEntity<Map<String, Any>>? {
        if (userInformation == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to "login.invalid_password", "captcha" to false))

        val ssoToken = UUID.randomUUID().toString()

        UserInformationDao.updateAuthTicket(userInformation, ssoToken)

        return ResponseEntity.ok().body(mapOf("ssoToken" to ssoToken))
    }

    @GetMapping("/api/user/avatars")
    fun avatars(@SessionAttribute("userInformation") userInformation: UserInformation?): ResponseEntity<List<AvatarPocketHabbo>>? {
        if (userInformation == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(emptyList())

        val userStats = UserStatsDao.getUserStats(userInformation.id)

        return ResponseEntity.ok().body(listOf(AvatarPocketHabbo(
                userInformation.id,
                userInformation.figure,
                userInformation.motto,
                banned = false,
                buildersClubMember = true,
                uniqueId = UUID.randomUUID().toString(),
                lastAccess = userStats.lastOnline.toEpochSecond(OffsetDateTime.now().offset),
                lastWebAccess = Instant.now().epochSecond
        )))
    }

    @PostMapping("/api/user/avatars/select")
    fun selectAvatar(@SessionAttribute("userInformation") userInformation: UserInformation?, @RequestBody payload: Map<String, Any>): ResponseEntity<Nothing> {
        // it receives uniqueId
        if (userInformation == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)

        @Suppress("UNUSED_VARIABLE")
        val uniqueId = payload["uniqueId"] ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)

        return ResponseEntity.ok().body(null)
    }

    @PostMapping(value = ["/api/log/crash", "/api/log/error"])
    fun crash(@RequestBody payload: Map<String, Any>): ResponseEntity<Nothing> {
        log.error("PocketHabbo crash! ${payload["message"]}")

        return ResponseEntity.ok().body(null)
    }

    @PostMapping("/api/log/loginstep")
    fun loginstep(@RequestBody payload: Map<String, Any>): ResponseEntity<Nothing> {
        log.debug(payload.entries.joinToString())

        return ResponseEntity.ok().body(null)
    }
}