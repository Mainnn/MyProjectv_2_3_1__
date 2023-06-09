package Main.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan(value = "Main")
public class JavaConfig {

   private final Environment env;

   public JavaConfig(Environment env) {
      this.env = env;
   }

   @Bean
   public DataSource dataSource() throws PropertyVetoException {
      ComboPooledDataSource dataSource = new ComboPooledDataSource();
      dataSource.setDriverClass(env.getProperty("db.driver"));
      dataSource.setJdbcUrl(env.getProperty("db.url"));
      dataSource.setUser(env.getProperty("db.username"));
      dataSource.setPassword(env.getProperty("db.password"));
      return dataSource;
   }

   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException {
      JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean(); // HibernateExceptions, PersistenceExceptions... to DataAccessException
      em.setDataSource(dataSource());
      em.setPackagesToScan("Main.model");
      em.setJpaVendorAdapter(vendorAdapter);
      em.setJpaProperties(getHibernateProperties());
      return em;
   }

   @Bean
   public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(emf);
      return transactionManager;
   }
   public Properties getHibernateProperties(){
     try{
        Properties properties = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("hibernate.properties");
        properties.load(is);
        return properties;
     }catch (IOException e){
        throw new IllegalArgumentException("Can't load file",e);
     }

   }
}
