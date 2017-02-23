package com.relax;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.relax.model.News;
import com.relax.service.NewsService;
import com.relax.util.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by 周超 on 2017/02/21.
 */
@Component
public class WangYiNews implements Runnable,InitializingBean {
    private Logger log = Logger.getLogger(WangYiNews.class);

    @Resource
    private NewsService newsService;

    @Resource
    private HttpUtil httpUtil;

    @Value("#{configProperties['wangyinews.url']}")
    private String wangyi_news;

    @Value("#{configProperties['wangyinews.details']}")
    private String wangyi_news_details;

    @Value("#{configProperties['wangyinews.page']}")
    private int page;//当前页数

    @Value("#{configProperties['wangyinews.limit']}")
    private int limit;//新闻条数

    // 1 war 军事; 2	sport 体育; 3 tech 科技; 4 edu 教育; 5 ent 娱乐; 6 money 财经; 7 gupiao 股票; 8 travel 旅游; 9 lady	女人
    private String type;//新闻类型
    private String[] types = {"war", "sport", "tech", "edu", "ent", "money", "gupiao", "travel", "lady"};
    private int simpleId;//查看详细内容的

    /**
     * 爬取新闻保存到数据库中
     *
     * @return
     */
    public void getNews() {
        log.info("开始爬取新闻...");
        while (true) {
            for (String type : types) {
                String jsonString = httpUtil.get(wangyi_news + "type=" + type + "&page=" + page + "&limit=" + limit);
                JSONObject object = JSON.parseObject(jsonString);
                JSONArray array = object.getJSONArray("list");
                List<News> lists = JSON.parseArray(array.toJSONString(),News.class);
                for (News news:lists) {
                    newsService.insertNews(news);
                }
            }
            try {
                Thread.sleep(60000);
                log.info("暂停1分钟");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        getNews();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        getNews();
    }
}