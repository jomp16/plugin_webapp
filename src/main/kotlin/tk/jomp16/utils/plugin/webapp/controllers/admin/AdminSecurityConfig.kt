package tk.jomp16.utils.plugin.webapp.controllers.admin

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCrypt
import tk.jomp16.habbo.database.information.UserInformationDao

@Configuration
@EnableWebSecurity
open class AdminSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(UserAuthenticationProvider())
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/admin", "/admin/*")
                .hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/admin/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/admin/logout")
                .permitAll()
    }
}

class UserAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        val username = authentication.name.trim()
        val password = authentication.credentials.toString().trim()

        val userInformation = UserInformationDao.getUserInformationByUsername(username) ?: return null

        if (!BCrypt.checkpw(password, userInformation.password)) return null

        return UsernamePasswordAuthenticationToken(username, password, listOf(SimpleGrantedAuthority("ADMIN")))
    }

    override fun supports(authentication: Class<*>) = UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}