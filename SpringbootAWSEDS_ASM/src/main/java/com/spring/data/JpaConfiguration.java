package com.spring.data;

import static com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder.standard;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.google.gson.Gson;

@Configuration
public class JpaConfiguration {

	private final Gson gson = new Gson();	

	@Bean
	public DataSource dataSource() {
		final AwsSecret dbCredentials = getSecret();

		return DataSourceBuilder
				.create()
				.driverClassName("com.mysql.cj.jdbc.Driver")
				.url("jdbc:"+dbCredentials.getEngine()+"://"
				 +dbCredentials.getHost()+":"+dbCredentials.getPort()+"/productdb")
				.username(dbCredentials.getUsername())
				.password(dbCredentials.getPassword())
				.build();
	}

	private AwsSecret getSecret() {

		String secretName = "product-db-cred";

		// Create a Secrets Manager client
		AWSSecretsManager client = standard().withRegion("ap-south-1").build();

		String secret;
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
				.withSecretId(secretName);
		GetSecretValueResult getSecretValueResult = null;

		try {
			getSecretValueResult = client.getSecretValue(getSecretValueRequest);
		} catch (Exception e) {
			throw e;
		} 
		if (getSecretValueResult.getSecretString() != null) {
			secret = getSecretValueResult.getSecretString();
			return gson.fromJson(secret, AwsSecret.class);
		} 
		return null;
	}


	public class AwsSecret {

		private String username;
		private String password;
		private String host;
		private String engine;
		private String port;
		private String  dbInstanceIdentifier;

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		public String getHost() {
			return host;
		}

		public String getEngine() {
			return engine;
		}

		public String getPort() {
			return port;
		}

		public String getDbInstanceIdentifier() {
			return dbInstanceIdentifier;
		}
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(org.springframework.orm.jpa.vendor.Database.MYSQL);
		jpaVendorAdapter.setGenerateDdl(true);
		return jpaVendorAdapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
		lemfb.setDataSource(dataSource());
		lemfb.setJpaVendorAdapter(jpaVendorAdapter());
		lemfb.setPackagesToScan("com.spring.data.model");
		return lemfb;
	}
}