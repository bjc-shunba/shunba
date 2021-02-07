package com.baidu.shunba.config;

import com.baidu.shunba.constant.AuthenticationConstant;
import com.baidu.shunba.constant.Constant;
import com.baidu.shunba.service.impl.MyRedisTokenStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class Oauth2ServerConfig {
    private static final String DEMO_RESOURCE_ID = Constant.SECUTITY_RELEASE_URL_TEST;

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().authorizeRequests().antMatchers(AuthenticationConstant.EXCLUDE_URLS)
                    .permitAll().anyRequest().authenticated();

//            http.authorizeRequests()
//                    .antMatchers("/", "/login", "/oauth/token", "/oauth/**", "/logout", "/login/**", "/swagger-ui.html", "/webjars/**", "/swagger-ui.html", "/swagger-resources", "/swagger-resources/**", "/v2/api-docs", "/csrf")
//                    .permitAll().anyRequest().authenticated();
//            http.csrf().disable();
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(AuthenticationConstant.RESOURCE_ID).stateless(true);
        }

//        @Bean
//        protected CorsConfigurationSource corsConfigurationSource() {
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration(AuthenticationConstant.CORS_CONFIGURATION_SOURCE_PATH,
//                    new CorsConfiguration().applyPermitDefaultValues());
//            return source;
//        }
    }

    @Configuration
    @EnableAuthorizationServer
    public static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        RedisConnectionFactory redisConnectionFactory;
        @Autowired
        ClientDetailsService clientDetailsService;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            /**
             *  password 方案一：明文存储，用于测试，不能用于生产
             *  String finalSecret = "123456";
             *  password 方案二：用 BCrypt 对密码编码
             *  String finalSecret = new BCryptPasswordEncoder().encode("123456");
             *  password 方案三：支持多种编码，通过密码的前缀区分编码方式
             */
            clients.inMemory().withClient(Constant.SECURITY_CLIENT_ID_1).resourceIds(DEMO_RESOURCE_ID)
                    .authorizedGrantTypes(Constant.SECURITY_CLIENT_CREDENTIALS, Constant.SECURITY_REFRESH_TOKEN)
                    .scopes(Constant.SECURITY_SELECT).authorities(Constant.SECURITY_OAUTH2)
                    .secret(Constant.FINAL_SECRET)
                    .and().withClient(Constant.CLIENT_ID_VALUE).resourceIds(DEMO_RESOURCE_ID)
                    .authorizedGrantTypes(Constant.GRANT_TYPE_VALUE, Constant.SECURITY_REFRESH_TOKEN)
                    .accessTokenValiditySeconds(Constant.NUM_THIRTY * Constant.NUM_SIXTY)
                    .scopes(Constant.SECURITY_SELECT).authorities(Constant.SECURITY_OAUTH2)
                    .secret(Constant.FINAL_SECRET);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.authenticationManager(authenticationManager)
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            // 允许表单认证
            oauthServer.allowFormAuthenticationForClients();
            oauthServer.checkTokenAccess("permitAll()");
        }


//        @Bean
//        public TokenStore tokenStore() {
//            return new MyRedisTokenStoreService(redisConnectionFactory, clientDetailsService);
//        }

    }
}
