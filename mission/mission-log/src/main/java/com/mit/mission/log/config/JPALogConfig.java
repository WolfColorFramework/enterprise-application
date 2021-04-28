package com.mit.mission.log.config;

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

/**
 *  @autor: gaoy
 *  @date: 2021/4/21 9:54
 *  @description: log模块的jpa配置类
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryLog",
        transactionManagerRef = "transactionManagerLog",
        basePackages = {"com.mit.mission.log.repository"})
public class JPALogConfig {
    @Resource
    private JpaProperties jpaProperties;

    @Resource
    private HibernateProperties hibernateProperties;

    @Bean(name = "logDataSource")
    @ConfigurationProperties(prefix = "log.spring.datasource")  //使用application.properties的log数据源配置
    public DataSource logDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "entityManagerLog")        //log实体管理器
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryLog(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryLog")    //log实体工厂
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryLog(EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties =
                hibernateProperties.determineHibernateProperties(
                        jpaProperties.getProperties(),
                        new HibernateSettings());

        return builder.dataSource(logDataSource())
                .properties(properties)
                .packages("com.mit.mission.log.domain")     //换成你自己的实体类所在位置
                .persistenceUnit("logPersistenceUnit")
                .build();
    }

    @Bean(name = "transactionManagerLog")         //log事务管理器
    public PlatformTransactionManager transactionManagerLog(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryLog(builder).getObject());
    }
}
