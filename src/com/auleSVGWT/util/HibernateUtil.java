package com.auleSVGWT.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {

  private static final SessionFactory sessionFactory;
  private static final ServiceRegistry serviceRegistry;

  static {
    try {
      // Create the SessionFactory from hibernate.cfg.xml
      Configuration configuration = new Configuration();
      configuration.configure();
      serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
              configuration.getProperties()).build();
      sessionFactory = configuration.buildSessionFactory(serviceRegistry);

      //sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      // Make sure you log the exception, as it might be swallowed
      System.err.println("Initial SessionFactory creation failed. " + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}