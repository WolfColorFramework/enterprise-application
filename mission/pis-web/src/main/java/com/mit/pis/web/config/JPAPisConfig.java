package com.mit.pis.web.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
 *  @date: 2021/4/21 10:02
 *  @description: pis-web模块jpa配置（含有core模块）
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryPis",
        transactionManagerRef = "transactionManagerPis",
        basePackages = {"com.mit.pis.web.repository","com.mit.mission.security.repository",
                "com.mit.mission.core.base.repository","com.mit.mission.core.traffic.repository"})
public class JPAPisConfig {
    @Resource
    JpaProperties jpaProperties;
    @Resource
    HibernateProperties hibernateProperties;

    @Primary
    @Bean(name = "pisDataSource")
    @ConfigurationProperties(prefix = "pis.spring.datasource")  //使用application.properties的pis数据源配置
    public DataSource pisDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerPis")        //pis实体管理器
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPis(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryPis")    //pis实体工厂
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPis(EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties =
                hibernateProperties.determineHibernateProperties(
                        jpaProperties.getProperties(),
                        new HibernateSettings());

        return builder.dataSource(pisDataSource())
                .properties(properties)
                .packages("com.mit.pis.web.domain","com.mit.mission.security.domain",
                        "com.mit.mission.core.base.domain","com.mit.mission.core.traffic.domain")     //换成你自己的实体类所在位置
                .persistenceUnit("pisPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "transactionManagerPis")         //pis事务管理器
    public PlatformTransactionManager transactionManagerPis(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPis(builder).getObject());
    }

}
