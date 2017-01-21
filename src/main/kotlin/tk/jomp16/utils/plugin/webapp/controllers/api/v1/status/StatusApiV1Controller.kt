package tk.jomp16.utils.plugin.webapp.controllers.api.v1.status

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.jomp16.habbo.util.Utils

@RestController
class StatusApiV1Controller {
    @RequestMapping("/api/v1/status")
    fun serverStatus(): Map<String, Any> = Utils.serverStatus
}