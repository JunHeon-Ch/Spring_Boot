package com.example.demo.mfa.configures;

import com.example.demo.mfa.configures.filter.CustomUsernamePasswordAuthenticationFilter;
import com.example.demo.mfa.configures.filter.PreUsernamePasswordAuthenticationFilter;
import com.example.demo.mfa.configures.provider.CustomDaoAuthenticationProvider;
import com.example.demo.mfa.service.MfaService;
import com.example.demo.mfa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackages = {"com.example.demo.mfa.repositories"})
@EntityScan(basePackages = {"com.example.demo.mfa.data"}, basePackageClasses = {Jsr310Converters.class})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private String permitalUrl = "/login,/,/prelogin,/mfator,/purelogin";
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final MfaService mfaService;

    @Autowired
    public WebSecurityConfiguration(UserDetailsService userDetailsService, UserService userService, MfaService mfaService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.mfaService = mfaService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/i18n/**")
                .mvcMatchers("/static/**")
                .mvcMatchers("/css/**")
                .mvcMatchers("/js/**")
                .mvcMatchers("/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new PreUsernamePasswordAuthenticationFilter(bCryptPasswordEncoder(), userService, mfaService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(customDaoAuthenticationProvider());

        http.cors().and()
                .csrf().disable();

        http.headers().frameOptions().sameOrigin()
                .and().authorizeRequests().antMatchers(permitalUrl.split(",")).permitAll()
                .and().formLogin().loginPage("/login")
                .and().logout().logoutUrl("/logout").invalidateHttpSession(false).permitAll()
                .and().authorizeRequests().anyRequest().authenticated();

        super.configure(http);
    }

    private CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(this.authenticationManagerBean());
//        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new SuccessHan);

        return customUsernamePasswordAuthenticationFilter;
    }

    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
        CustomDaoAuthenticationProvider customDaoAuthenticationProvider = new CustomDaoAuthenticationProvider(userDetailsService);
        customDaoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return customDaoAuthenticationProvider;
    }
}
