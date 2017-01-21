package tk.jomp16.utils.plugin.webapp.controllers.api.v1.users

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.jomp16.habbo.database.information.UserInformationDao
import tk.jomp16.habbo.game.user.information.UserInformation

@RestController
class UsersApiV1Controller {
    @RequestMapping("/api/v1/users")
    fun getUsers(): List<Int> = UserInformationDao.getAllUsers().map { it.id }

    @RequestMapping("/api/v1/users/{id}")
    fun getUserById(@PathVariable("id") userId: Int) = getUserInfoMap(UserInformationDao.getUserInformationById(userId))

    private fun getUserInfoMap(userInformation: UserInformation?): Map<String, Any> =
            if (userInformation == null) mapOf("error" to "User not found")
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