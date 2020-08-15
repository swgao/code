package com.gao.blog.vo;

import com.gao.blog.dao.BlogRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Tag;
import com.gao.blog.pojo.Type;
import com.gao.blog.pojo.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class test {
    public static void main(String[] args) throws IOException, ParseException {
        for (int i = 0; i <= 5; i++) {
            Document document = Jsoup.connect("https://bbs.csdn.net/forums/Java/recommend?page="+i).get();
            Elements elements = document.getElementsByClass("forums_title ");
            for (Element element : elements) {
                System.out.println(element.text());
                String url = "https://bbs.csdn.net"+element.attr("href");
                System.out.println(url);
                System.out.println("-------------------");
                Document document1 = Jsoup.connect(url).get();
                Element first = document1.getElementsByClass("post_body").first();
                System.out.println(first.html());
//                Blog blog = new Blog();
//                blog.setContent(first.html());
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Element one = document1.getElementsByClass("control_l fl").first();
                Element 时间 = one.getElementsByClass("date_time").first();
//                blog.setCreateTime(sf.parse(时间.text()));
//                blog.setTitle(element.text());
//                blog.setFirstPicture("https://picsum.photos/seed/picsum/800/450");
//                Random r = new Random();
//                blog.setViews(r.nextInt(300));
//                blog.setAppreciation(true);
//                blog.setCommentabled(true);
//                blog.setCommentabled(true);
//                blog.setPublished(true);
//                blog.setRecommend(true);
//                blog.setUpdateTime(sf.parse(时间.text()));
//                blog.setFlag("转载");
//                User user = new User();
//                user.setId(8L);
//                blog.setUser(user);
//                Type type = new Type();
//                blog.setType(type);
//                blogRepository.save(blog);
            }
        }

    }
}
