package com.fushuhealth.recovery.device.config;


import com.fushuhealth.recovery.device.config.properties.PermitAllUrlProperties;
import com.fushuhealth.recovery.device.core.service.UserDetailsServiceImpl;
import com.fushuhealth.recovery.device.security.filter.AuthenticationEntryPointImpl;
import com.fushuhealth.recovery.device.security.handle.JwtAuthenticationTokenFilter;
import com.fushuhealth.recovery.device.security.handle.LogoutSuccessHandlerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security配置
 *
 * @author Zhuanz
 */
@Configuration
@EnableWebSecurity //开启SpringSecurity的默认行为
@Slf4j //日志
@EnableMethodSecurity
public class SecurityConfig
{
    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
//    @Autowired
//    private CorsFilter corsFilter;

    /**
     * 允许匿名访问的地址
     */
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    // 这个主要是为了其他地方可以使用认证管理器
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 这个配置是关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                // 处理请求
                .authorizeHttpRequests(authorize -> {
                    // 放开哪些接口
                        authorize
                            .requestMatchers(HttpMethod.POST,"/login","/h5/sms/**","/h5/login")
                            .permitAll();
//                    authorize.requestMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html","/img/**", "/**/*.css", "/**/*.js", "/profile/**")
//                            .permitAll();
                    authorize.requestMatchers
                            ("/loginOut","/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/druid/**")
                            .permitAll()
                    // 其他的都需要认证
                    .anyRequest().authenticated();
                })
                // 错误处理
                .exceptionHandling(m -> {
                    m.authenticationEntryPoint(unauthorizedHandler);
                })
                .authenticationProvider(authenticationProvider())
                // 如果使用token这个配置是必须的
                .addFilterBefore(authenticationTokenFilter,UsernamePasswordAuthenticationFilter.class)
                .logout(logout->logout.logoutUrl("/loginOut")
                        .logoutSuccessHandler(logoutSuccessHandler)
                );
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份认证接口
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }
}
