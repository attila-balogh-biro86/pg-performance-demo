package com.banfico;

import com.blazebit.persistence.spring.data.impl.repository.BlazePersistenceRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

@SpringBootApplication
@EnableJpaRepositories(
		repositoryFactoryBeanClass = BlazePersistenceRepositoryFactoryBean.class,
		queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND
)
public class PgPerformanceDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PgPerformanceDemoApplication.class, args);
	}

}
