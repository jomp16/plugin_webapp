package tk.jomp16.utils.plugin.webapp.controllers.home

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HomeController {
    @RequestMapping("/")
    fun index() = "home/index"
}