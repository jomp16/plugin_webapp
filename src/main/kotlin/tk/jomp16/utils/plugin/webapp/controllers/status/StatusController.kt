package tk.jomp16.utils.plugin.webapp.controllers.status

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import tk.jomp16.habbo.util.Utils

@Controller
class StatusController {
    @RequestMapping("/status")
    fun status(model: Model): String {
        model.addAttribute("status", Utils.serverStatus)

        return "status/index"
    }
}