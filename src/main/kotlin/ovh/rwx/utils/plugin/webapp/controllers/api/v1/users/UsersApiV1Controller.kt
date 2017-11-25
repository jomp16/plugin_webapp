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

package ovh.rwx.utils.plugin.webapp.controllers.api.v1.users

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ovh.rwx.habbo.database.user.UserInformationDao
import ovh.rwx.habbo.game.user.information.UserInformation

@RestController
class UsersApiV1Controller {
    @RequestMapping("/api/v1/users/{id}")
    fun getUserById(@PathVariable("id") userId: Int) = getUserInfoMap(UserInformationDao.getUserInformationById(userId))

    private fun getUserInfoMap(userInformation: UserInformation?): Map<String, Any> =
            if (userInformation == null) mapOf(
                    "error" to "User not found"
            )
            else mapOf(
                    "id" to userInformation.id,
                    "username" to userInformation.username,
                    "real_name" to userInformation.realname,
                    "figure" to userInformation.figure,
                    "gender" to userInformation.gender,
                    "motto" to userInformation.motto,
                    "vip" to userInformation.vip
            )
}