package com.baidu.shunba.fwk.persistence;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidDatasouceConfiguration {
	@Bean(name="mysqlDatasource")
	@Qualifier("mysqlDatasource")
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSourceMySql() {
//		DruidDataSource dataSource = new DruidDataSource();
//		dataSource.setDriverClassName(properties.determineDriverClassName());
//		dataSource.setUrl(properties.determineUrl());
//		dataSource.setUsername(properties.determineUsername());
//		dataSource.setPassword(properties.determinePassword());
//		DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
//		String validationQuery = databaseDriver.getValidationQuery();
//		if (validationQuery != null) {
//			dataSource.setTestOnBorrow(true);
//			dataSource.setValidationQuery(validationQuery);
//		}
//		try {
//			// 开启Druid的监控统计功能，mergeStat代替stat表示sql合并,wall表示防御SQL注入攻击
//			dataSource.setFilters("mergeStat,wall,log4j");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return dataSource;
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name="gpDatasource")
	@Qualifier("gpDatasource")
	@ConfigurationProperties(prefix="gp.datasource")
	public DataSource dataSourceGp() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name="mysqlJdbcTemplate")
	@Qualifier
	@Primary
	JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDatasource") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	@Bean(name="mysqlNamedParameterJdbcTemplate")
	@Qualifier
	@Primary
	NamedParameterJdbcTemplate mysqlNamedParameterJdbcTemplate(@Qualifier("mysqlDatasource") DataSource ds) {
		return new NamedParameterJdbcTemplate(ds);
	}

	@Bean(name="gpJdbcTemplate")
	@Qualifier
	JdbcTemplate gpJdbcTemplate(@Qualifier("gpDatasource") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	@Bean(name="gpNamedParameterJdbcTemplate")
	@Qualifier
	NamedParameterJdbcTemplate gpNamedParameterJdbcTemplate(@Qualifier("gpDatasource") DataSource ds) {
		return new NamedParameterJdbcTemplate(ds);
	}
	
	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		// 白名单
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		// IP黑名单（共同存在时，deny优先于allow）
		servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
		// 登陆查看新的是账户密码
		servletRegistrationBean.addInitParameter("loginUsername", "druid");
		servletRegistrationBean.addInitParameter("loginPassword", "123456");
		// 是否能够重置数据
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean statFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息
		filterRegistrationBean.addInitParameter("exclusions", "*.js.*.gif,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
}