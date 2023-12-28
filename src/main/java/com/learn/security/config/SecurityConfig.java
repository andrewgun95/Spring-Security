package com.learn.security.config;

import com.learn.security.security.ApiKeySecretAuthenticationFilter;
import com.learn.security.security.BearerTokenAuthenticationFilter;
import com.learn.security.security.MyPasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new LdapShaPasswordEncoder();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new StandardPasswordEncoder();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MyPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private final String apiKey;
    private final String apiSecret;

    public SecurityConfig(@Value("${api.key}") String apiKey, @Value("${api.secret}") String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    // Exposing Global Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ApiKeySecretAuthenticationFilter apiKeySecretAuthenticationFilter(AuthenticationManager authenticationManager) {
        ApiKeySecretAuthenticationFilter apiKeySecretAuthenticationFilter = new ApiKeySecretAuthenticationFilter(AntPathRequestMatcher.antMatcher("/api/**"));
        apiKeySecretAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return apiKeySecretAuthenticationFilter;
    }

    @Bean
    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter = new BearerTokenAuthenticationFilter(AntPathRequestMatcher.antMatcher("/api/**"));
        bearerTokenAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return bearerTokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector, //
                                                   ApiKeySecretAuthenticationFilter restAuthenticationFilter, //
                                                   BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);

        // To permit all on specific request
        // The matcher must be defined before
        // To authenticated on any requests

        // Permit all on static resources and h2-console
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                AntPathRequestMatcher.antMatcher("/"), //
                AntPathRequestMatcher.antMatcher("/css/**/*"), //
                AntPathRequestMatcher.antMatcher("/error"), //
                AntPathRequestMatcher.antMatcher("/static/favicon.ico"), //
                AntPathRequestMatcher.antMatcher("/h2-console/**") //
        ).permitAll());

        // PUBLIC ACCESS PAGE
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                AntPathRequestMatcher.antMatcher("/profile"), //
                AntPathRequestMatcher.antMatcher("/item/*") //
        ).permitAll());

        // ADMIN ACCESS PAGE
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                AntPathRequestMatcher.antMatcher("/item/my/*") //
        ).hasAnyRole("ADMIN"));

        // CUSTOMER ACCESS PAGE
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                AntPathRequestMatcher.antMatcher("/item/*/buy") //
        ).hasAnyRole("CUSTOMER"));

        // PUBLIC ACCESS API
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/auth") //
        ).permitAll());
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/item/**") //
        ).permitAll());
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/v1/cart"), //
                mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/v1/cart/{id}") //
        ).permitAll());

        // ADMIN ACCESS API
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers( //
                AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/item/**") //
        ).hasAnyRole("ADMIN"));

        // Authenticated on any request
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(restAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(bearerTokenAuthenticationFilter, ApiKeySecretAuthenticationFilter.class);

        // H2 Database using Frame and by default it will block by Spring Security
        // FrameOptions to deny
        // change FrameOptions to sameOrigin
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    //    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("root").password("{noop}andregokil").roles("ADMIN").build();

        // Using NoOp Password Encoder
        // UserDetails user = User.withUsername("user").password("password").roles("USER").build();

        // Using LDAP Password Encoder
        // UserDetails user = User.withUsername("user").password("{SSHA}+mI52jDzC0fSe73DW15nN+rhqb3uKCQOc4+3vQ==").roles("USER").build();

        // Using SHA-256 Password Encoder
        // UserDetails user = User.withUsername("user").password("d521c2aaf1aa8fa7f2897e2ffbcda5d7c86730f0090737fb9b69943fc361c379846b176cb3a1fbf9").roles("USER").build();

        // Using BCrypt Password Encoder
        // UserDetails user = User.withUsername("user").password("$2a$10$uAu8lIrklD3qBBtXWvgpeuNFgh9pFwM/ObLWhLDBqX34ffcRX3Ul.").roles("USER").build();

        // Using Delegate Password Encoder
        UserDetails user = User.withUsername("user").password("{bcrypt}$2a$10$w/KrleE4IomXstwZxlx.4.0nIFOmNW7CaQVDmQbdXjzUnJ.03QsoK").roles("USER").build();

        // Using NoOp Password Encoder
        // UserDetails customer = User.withUsername("scott").password("{noop}tiger").roles("CUSTOMER").build();

        // Using LDAP Password Encoder
        // UserDetails customer = User.withUsername("scott").password("{SSHA}+mI52jDzC0fSe73DW15nN+rhqb3uKCQOc4+3vQ==").roles("CUSTOMER").build();

        // Using SHA-256 Password Encoder
        // UserDetails customer = User.withUsername("scott").password("d521c2aaf1aa8fa7f2897e2ffbcda5d7c86730f0090737fb9b69943fc361c379846b176cb3a1fbf9").roles("CUSTOMER").build();

        // Using BCrypt Password Encoder
        // UserDetails customer = User.withUsername("scott").password("$2a$10$uAu8lIrklD3qBBtXWvgpeuNFgh9pFwM/ObLWhLDBqX34ffcRX3Ul.").roles("CUSTOMER").build();

        // Using Delegate Password Encoder
        // UserDetails customer = User.withUsername("scott").password("{sha256}9d24a62bb8ed770dda1be8a52194dc6355598544deb9f1feb8037ae9296cf8bfb8b246c4b3fa2eb5").roles("CUSTOMER").build();

        // Using Custom BCrypt 15 Strength Password Encoder
        UserDetails customer = User.withUsername("scott").password("{bcrypt15}$2a$15$fUA1NXXsnJfGOLO99mTth.LPVveakw0dNSjEGFafwvVTrdjEmwB0G").roles("CUSTOMER").build();

        UserDetails service = User.withUsername(apiKey).password(String.format("{bcrypt}%s", apiSecret)).roles("ADMIN").build();
        return new InMemoryUserDetailsManager(admin, user, customer, service);
    }

}
