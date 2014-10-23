package com.an.sfs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContext {
    private static volatile boolean initialized = false;
    private static ApplicationContext appContext;

    public AppContext() {
    }

    public synchronized static void init() {
        if (!initialized) {
            synchronized (AppContext.class) {
                if (!initialized) {
                    appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
                    initialized = true;
                }
            }
        }
    }

    public static SussDao getDao() {
        return (SussDao) appContext.getBean("sussDaoImpl");
    }
}
