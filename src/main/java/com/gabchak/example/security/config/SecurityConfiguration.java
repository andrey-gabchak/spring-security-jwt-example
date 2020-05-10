package com.gabchak.example.security.config;


import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.security.jwt.JwtConfigurer;
import com.gabchak.example.security.jwt.JwtTokenProvider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final JwtTokenProvider jwtTokenProvider;
  private final List<String> clientOrigins;

  @Autowired
  public SecurityConfiguration(JwtTokenProvider jwtTokenProvider,
             CorsProperties corsProperties) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.clientOrigins = corsProperties.getHosts();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(UrlConstants.PAID_USER_ACCESS).hasRole(Roles.PAID_USER.name())
        .antMatchers(UrlConstants.ADMIN_ACCESS).hasRole(Roles.ADMIN.name())
        .antMatchers(UrlConstants.FREE_USER_ACCESS).hasRole(Roles.FREE_USER.name())
        .antMatchers(UrlConstants.PUBLIC_ACCESS).permitAll()
        .anyRequest().authenticated()
        .and()
        .apply(new JwtConfigurer(jwtTokenProvider));
  }

  /**
   * The bean configure CORS policy.
   * */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(clientOrigins);

    configuration.setAllowedMethods(List.of(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name()));

    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(List.of(
        HttpHeaders.AUTHORIZATION,
        HttpHeaders.CACHE_CONTROL,
        HttpHeaders.CONTENT_TYPE));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Another way to configure CORS.
   * This method creates new WebMvcConfigurer and configures CORS policy.
   *
   * @return WebMvcConfigurer bean with CORS configurations
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowCredentials(true)
            .allowedHeaders(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CACHE_CONTROL,
                HttpHeaders.CONTENT_TYPE)
            .allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name());
      }
    };
  }
}
