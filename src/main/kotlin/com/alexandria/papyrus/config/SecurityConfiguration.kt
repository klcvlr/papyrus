package com.alexandria.papyrus.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Profile("!oauth2")
@Configuration
@EnableAutoConfiguration(exclude = [OAuth2ClientAutoConfiguration::class])
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorizeConfig ->
                authorizeConfig.anyRequest().authenticated()
            }
            .httpBasic(withDefaults())
            .formLogin(withDefaults())
            .csrf { it.disable() }
            .build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user =
            User.builder()
                .username("user")
                .password("{noop}user")
                .authorities("ROLE_USER")
                .build()
        val admin =
            User.builder()
                .username("admin")
                .password("{noop}admin")
                .authorities("ROLE_ADMIN")
                .build()
        return InMemoryUserDetailsManager(user, admin)
    }
}
