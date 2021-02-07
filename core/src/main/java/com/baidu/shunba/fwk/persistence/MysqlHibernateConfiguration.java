package com.baidu.shunba.fwk.persistence;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MysqlHibernateConfiguration {

//	@Value("${db.driver}")
//	private String DB_DRIVER;
//
//	@Value("${db.url}")
//	private String DB_URL;
//
//	@Value("${db.username}")
//	private String DB_USERNAME;
//
//	@Value("${db.password}")
//	private String DB_PASSWORD;

	@Value("${hibernate.dialect}")
	private String HIBERNATE_DIALECT;

	@Value("${hibernate.show_sql}")
	private String HIBERNATE_SHOW_SQL;

	@Value("${hibernate.hbm2ddl.auto}")
	private String HIBERNATE_HBM2DDL_AUTO;

	@Value("${entitymanager.packagesToScan}")
	private String ENTITYMANAGER_PACKAGES_TO_SCAN;
	
	@Value("${hibernate.id.new_generator_mappings}")
	private String NEW_GENERATOR_MAPPINGS;
	
	@Value("${hibernate.cache.use_query_cache}")
	private String USE_QUERY_CACHE;
	
	@Value("${hibernate.cache.use_second_level_cache}")
	private String USE_SECOND_LEVEL_CACHE;
	
	@Value("${hibernate.cache.provider_class}")
	private String CACHE_PROVIDER_CLASS;
	
	@Value("${hibernate.cache.region.factory_class}")
	private String CACHE_REGION_FACTORY_CLASS;
	

	@Autowired
	@Qualifier("mysqlDatasource")
	private DataSource mysqlDatasource;
	
	@Bean(name="sessionFactoryMysql")
	public LocalSessionFactoryBean sessionFactoryMysql() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(mysqlDatasource);
		sessionFactory.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", HIBERNATE_DIALECT);
		hibernateProperties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
		hibernateProperties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
		hibernateProperties.put("hibernate.id.new_generator_mappings", NEW_GENERATOR_MAPPINGS);
		hibernateProperties.put("hibernate.cache.use_query_cache", USE_QUERY_CACHE);
		hibernateProperties.put("hibernate.cache.use_second_level_cache", USE_SECOND_LEVEL_CACHE);
		hibernateProperties.put("hibernate.cache.provider_class", CACHE_PROVIDER_CLASS);
		hibernateProperties.put("hibernate.cache.region.factory_class", CACHE_REGION_FACTORY_CLASS);
		//hibernateProperties.put("hibernate.ejb.interceptor", new HiberAspect());
		
		sessionFactory.setEntityInterceptor(new HiberAspect());
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	@Bean(name="transactionManagerMysql")
	@Primary
	public HibernateTransactionManager transactionManagerMysql() {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactoryMysql().getObject());
		return txManager;
	}
}
