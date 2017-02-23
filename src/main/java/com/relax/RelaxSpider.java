package com.relax;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.TimerTask;


public class RelaxSpider extends TimerTask implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private  WangYiNews wangYiNews;

    private Logger log = Logger.getLogger(RelaxSpider.class);

    public RelaxSpider() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        wangYiNews = (WangYiNews) applicationContext.getBean("wangYiNews");
    }

    public void run() {
        log.info("网易新闻---------");
        wangYiNews.getNews();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        log.info("初始化ApplicationContext...........");
    }
}