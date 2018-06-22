package com.example.service;

import com.example.vo.User;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
//import io.ebean.springsupport.txn.SpringAwareJdbcTransactionManager;

/**
 * Spring factory for creating the EbeanServer singleton.
 */
@Component
public class EbeanFactoryBean implements FactoryBean<EbeanServer> {


  public EbeanServer getObject() throws Exception {
    return createEbeanServer();
  }

  public Class<?> getObjectType() {
    return EbeanServer.class;
  }

  public boolean isSingleton() {
    return true;
  }

  /**
   * Create a EbeanServer instance.
   */
  private EbeanServer createEbeanServer() {

    ServerConfig config = new ServerConfig();
    config.setName("db");

    // load configuration from ebean.properties
    config.loadFromProperties();
    config.setDefaultServer(true);
    // other programmatic configuration

    return EbeanServerFactory.create(config);
  }
}
