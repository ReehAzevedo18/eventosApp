package com.eventoapp;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@Profile("prod")
public class DataConfiguration {

	
	//Configuração para o heroku
	@Bean
    public BasicDataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
	
	public JpaVendorAdapter jpaVendorAdapter() {
		   HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		   adapter.setDatabase(Database.POSTGRESQL);
		   adapter.setShowSql(true);
		   adapter.setGenerateDdl(true);
		   adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
		   adapter.setPrepareConnection(true);
		   return adapter;
	}
	//Configurando meu banco
	/*
	 * @Bean public DataSource dataSource() { DriverManagerDataSource ds = new
	 * DriverManagerDataSource();
	 * ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	 * ds.setUrl("jdbc:sqlserver://DESKTOP-2N621GG\\SQLEXPRESS;Database=dbEvento");
	 * ds.setUsername("renatinha"); ds.setPassword("04045050");
	 * 
	 * return ds; }
	 * 
	 * //Configurando o hibernate
	 * 
	 * @Bean public JpaVendorAdapter jpaVendoAdapter() { HibernateJpaVendorAdapter
	 * adapter = new HibernateJpaVendorAdapter();
	 * adapter.setDatabase(Database.SQL_SERVER); adapter.setShowSql(true);
	 * adapter.setGenerateDdl(true);
	 * adapter.setDatabasePlatform("org.hibernate.dialect.SQLServerDialect");
	 * adapter.setPrepareConnection(true);
	 * 
	 * return adapter; }
	 */
}
