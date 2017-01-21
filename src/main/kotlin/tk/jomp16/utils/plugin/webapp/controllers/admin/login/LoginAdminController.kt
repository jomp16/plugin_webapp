package tk.jomp16.utils.plugin.webapp.controllers.admin.login

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LoginAdminController {
    @RequestMapping("/admin/login")
    fun loginPage() = "admin/login/index"
}