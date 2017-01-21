package tk.jomp16.utils.plugin.webapp.controllers

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import tk.jomp16.habbo.BuildConfig

@ControllerAdvice
class DefaultControllerAdvice {
    @ModelAttribute("applicationConfig")
    fun applicationConfig(): Map<String, Any> = mapOf(
            "name" to BuildConfig.NAME,
            "version" to BuildConfig.VERSION,
            "git_revision" to BuildConfig.GIT_REVISION_LONG
    )
}