package com.edu.report.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtTimestampValidator
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
class SecurityConfig {
    @Bean
    fun HttpSecurity.securityFilterChain(): SecurityFilterChain {
        authorizeHttpRequests {
            it.requestMatchers("/reports").hasRole("prothetic_user")
            it.anyRequest().denyAll()
        }
            .oauth2ResourceServer {
                it.jwt { jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) }
            }
        return build()
    }

    @Bean
    fun corsConfig(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf(
            "http://localhost:3000"
        )
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        config.allowedHeaders = listOf("*")
        config.exposedHeaders = listOf("authorization", "content-type")
        config.allowCredentials = true
        config.maxAge = 1800

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun corsFilter(): CorsFilter {
        return CorsFilter(corsConfig())
    }

    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val roles = (jwt.claims["realm_access"] as? Map<*, *>)?.get("roles") as? List<*> ?: emptyList<Any>()

            roles.mapNotNull { role ->
                role?.toString()?.let { SimpleGrantedAuthority("ROLE_$it") }
            } as Collection<GrantedAuthority>
        }
        return converter
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val jwtDecoder = NimbusJwtDecoder
            .withJwkSetUri("http://keycloak:8080/realms/reports-realm/protocol/openid-connect/certs")
            .build()


        // Оставляем ТОЛЬКО проверку времени действия токена (без проверки issuer)
        jwtDecoder.setJwtValidator(JwtTimestampValidator())

        return jwtDecoder
    }
}