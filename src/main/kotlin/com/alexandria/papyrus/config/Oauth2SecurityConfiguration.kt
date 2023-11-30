package com.alexandria.papyrus.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Profile("oauth2")
@Configuration
class Oauth2SecurityConfiguration {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorizeConfig ->
                authorizeConfig.anyRequest().authenticated()
            }
            .httpBasic(withDefaults())
            .formLogin(withDefaults())
            .oauth2Login(withDefaults())
            .csrf { it.disable() }
            .build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user1 =
            User.builder()
                .username("user")
                .password("{noop}user")
                .authorities("ROLE_USER")
                .build()
        val user2 =
            User.builder()
                .username("admin")
                .password("{noop}admin")
                .authorities("ROLE_ADMIN")
                .build()
        return InMemoryUserDetailsManager(user1, user2)
    }
}
