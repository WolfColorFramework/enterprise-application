package com.mit.mission.flowable.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryFlowable",
        transactionManagerRef="transactionManagerFlowable",
        basePackages= { "com.mit.mission.flowable.repository" })
public class JPAFlowableConfig {

    @Resource
    private JpaProperties jpaProperties;

    @Resource
    private HibernateProperties hibernateProperties;

    @Bean(name = "flowableDataSource")
    @ConfigurationProperties(prefix="flowable.spring.datasource")  //使用application.properties的flowable数据源配置
    public DataSource flowableDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "entityManagerFlowable")        //flowable实体管理器
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryFlowable(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryFlowable")    //flowable实体工厂
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryFlowable (EntityManagerFactoryBuilder builder) {

        Map<String,Object> properties =
                hibernateProperties.determineHibernateProperties(
                        jpaProperties.getProperties(),
                        new HibernateSettings());

        return builder.dataSource(flowableDataSource())
                .properties(properties)
                .packages("com.mit.mission.flowable.domain")     //换成你自己的实体类所在位置
                .persistenceUnit("flowablePersistenceUnit")
                .build();
    }

    @Bean(name = "transactionManagerFlowable")         //flowable事务管理器
    public PlatformTransactionManager transactionManagerFlowable(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryFlowable(builder).getObject());
    }

}
