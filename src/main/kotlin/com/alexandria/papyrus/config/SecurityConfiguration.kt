package com.alexandria.papyrus.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorizeConfig ->
                authorizeConfig.requestMatchers(HttpMethod.GET, "/actuator/health/**").permitAll()
                authorizeConfig.requestMatchers(HttpMethod.GET, "/actuator/info/**").hasAnyRole("ADMIN")
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
