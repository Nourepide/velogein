package net.intervallayers.velogein.security

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter
import net.intervallayers.velogein.view.auth.AuthView
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
@Configuration
class SecurityConfiguration(var securityUserDetailsService: SecurityUserDetailsService) : VaadinWebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        setLoginView(http, AuthView::class.java)
    }

    /**
     * Разрешает доступ к статическим ресурсам в обход безопасности Spring.
     */
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/images/**")
        super.configure(web)
    }

    @Bean
    public override fun userDetailsService(): UserDetailsService {
        return securityUserDetailsService
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun daoAuthProvider(): DaoAuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setPasswordEncoder(passwordEncoder())
            setUserDetailsService(userDetailsService())
        }
    }
}
