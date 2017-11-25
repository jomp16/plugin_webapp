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

package ovh.rwx.utils.plugin.webapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import ovh.rwx.habbo.HabboServer
import ovh.rwx.utils.plugin.api.PluginListener

@Suppress("unused", "UNUSED_PARAMETER")
class HabboWebListener : PluginListener() {
    lateinit private var application: ConfigurableApplicationContext

    override fun onCreate() {
        super.onCreate()

        log.info("Lauching web application...")

        System.getProperties().put("org.springframework.boot.logging.LoggingSystem", "none")
        System.getProperties().put("server.port", HabboServer.habboConfig.webPort)

        application = SpringApplication.run(HabboWebApplication::class.java)

        log.info("Web application launched on port {}!", HabboServer.habboConfig.webPort)
    }

    override fun onDestroy() {
        super.onDestroy()

        log.info("Stopping web application...")

        if (application.isRunning) SpringApplication.exit(application)

        log.info("Stopped web application!")
    }
}

@SpringBootApplication
open class HabboWebApplication