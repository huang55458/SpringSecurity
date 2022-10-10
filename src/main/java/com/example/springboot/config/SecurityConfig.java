package com.example.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;

/**
 * @author chumeng
 * @date 2022/10/10
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("root").password("root").roles("admin").and()
                .withUser("chumeng").password("123").roles("user")
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 不被 security 拦截的路径，一般用于静态文件
        web.ignoring().antMatchers("/images/**","/hello");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //
                .antMatchers("/admin/**","/--").hasRole("admin")
                .antMatchers("/user/**").hasRole("user")
                // 只要是登入成功不管什么身份
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                .loginPage("/login.html")
//                // 配置用户名和密码传入的借口的地址，默认是login.html
//                .loginProcessingUrl("/login.html")
                // 更改用户名的查询参数，默认为 username
                .usernameParameter("username")
                // 更改密码的查询参数，默认为 password
                .passwordParameter("password")
//                // 登入成功回调地址，一般写登入地址，有一个重载方法，传入第一个参数 true 时，输入其他地址，登入验证成功时，统一重定向到 /hello
//                .defaultSuccessUrl("/hello")
//                // 登入失败回调
//                .failureUrl("/hello")
                // 下面用于前后端分离，返回 json 数据格式
                .successHandler(((request, response, authentication) -> {
                    Object principal = authentication.getPrincipal();
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println(new ObjectMapper().writeValueAsString(principal));
                    System.out.println(new ObjectMapper().writeValueAsString(principal));
                    out.flush();
                    out.close();
                }))
//                .failureHandler(((request, resp, e) -> {
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    RespBean respBean = RespBean.error(e.getMessage());
//                    if (e instanceof LockedException) {
//                        respBean.setMsg("账户被锁定，请联系管理员!");
//                    } else if (e instanceof CredentialsExpiredException) {
//                        respBean.setMsg("密码过期，请联系管理员!");
//                    } else if (e instanceof AccountExpiredException) {
//                        respBean.setMsg("账户过期，请联系管理员!");
//                    } else if (e instanceof DisabledException) {
//                        respBean.setMsg("账户被禁用，请联系管理员!");
//                    } else if (e instanceof BadCredentialsException) {
//                        respBean.setMsg("用户名或者密码输入错误，请重新输入!");
//                    }
//                    out.write(new ObjectMapper().writeValueAsString(respBean));
//                    out.flush();
//                    out.close();
//                }))
                .permitAll()
                .and()
                .csrf().disable()
                // 尚未登入的用户访问
//                .exceptionHandling()
//                .authenticationEntryPoint(((request, response, authException) -> {
//                    System.out.println(1);
//                    response.setContentType("application/json;charset=UTF-8");
//                    PrintWriter out = response.getWriter();
//                    out.write("尚未登入，请先登入");
//                    out.flush();
//                    out.close();
//                }))
//                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req, resp, authentication) -> {
                    System.out.println(2);
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("注销成功");
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
        ;
    }
}
