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

package ovh.rwx.utils.plugin.webapp.controllers.api.pocket.json

import com.fasterxml.jackson.annotation.JsonProperty

data class AvatarPocketHabbo(
        @JsonProperty("id")
        val id: Int,
        @JsonProperty("figureString")
        val figureString: String,
        @JsonProperty("motto")
        val motto: String,
        @JsonProperty("banned")
        val banned: Boolean,
        @JsonProperty("buildersClubMember")
        val buildersClubMember: Boolean,
        @JsonProperty("uniqueId")
        val uniqueId: String,
        @JsonProperty("last_access")
        val lastAccess: Long,
        @JsonProperty("lastWebAccess")
        val lastWebAccess: Long
)