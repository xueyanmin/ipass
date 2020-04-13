package com.xue.ipass;

import com.xue.ipass.entity.Emp;
import com.xue.ipass.entity.Video;
import com.xue.ipass.repository.EmpRepository;
import com.xue.ipass.repository.VideoRepository;
import com.xue.ipass.service.VideoService;
import com.xue.ipass.vo.VideoVo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTests {

    @Resource
    EmpRepository empRepository;

    @Resource
    ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    VideoRepository videoRepository;

    @Resource
    VideoService videoService;


    @Test
    public void save() {

        Emp emp = new Emp("3", "小橘子苹果", 32, new Date());
        empRepository.save(emp);
    }

    @Test
    public void query() {

        Iterable<Emp> all = empRepository.findAll();
        all.forEach(emp -> System.out.println(emp));
    }

    @Test
    public void querySort() {

        Iterable<Emp> all = empRepository.findAll(Sort.by(Sort.Order.asc("age")));
        all.forEach(emp -> System.out.println(all));
    }

    @Test
    public void queryPage() {

        Iterable<Emp> emp = empRepository.findAll(PageRequest.of(0, 1));
        emp.forEach(emp1 -> System.out.println(emp));
    }


    @Test
    public void queryById() {

        Optional<Emp> emps = empRepository.findById("1");
        System.out.println(emps);
    }


    @Test
    public void queryByName() {

        List<Emp> name = empRepository.findByName("小美国");
        System.out.println(name);
    }

    @Test
    public void delete() {
        empRepository.deleteById("1");
    }

    @Test
    public void saveVideo() {

       /* HashMap<String, Object> map = videoService.queryByPage(1, 30);
        List<Video> rows = (List<Video>) map.get("rows");
        for (Video row : rows) {
            System.out.println(row);
            videoRepository.save(row);
        }
*/
        videoRepository.save(new Video("5", "小橘子", "我是四川的小橘子", null, "https://yingxs.oss-cn-beijing.aliyuncs.com/photo/1585660387691-徒步.jpg", new Date(), "3", "3", "3"));
    }

    /**
     * 检索缓存之后处理高亮
     */
    @Test
    public void querySearch() {
        //分词查询 指定查询词
        String content = "橘子";

        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style = 'color:red'>");//高亮前缀
        field.postTags("</span>");//后缀

        //查询条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingxv")  //指定索引名
                .withTypes("video")       //指定索引类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //指定搜索条件
                .withHighlightFields(field)
                .build();

        //相当于元数据查询
       /* List<Video> videos = elasticsearchTemplate.queryForList(searchQuery, Video.class);
        videos.forEach(video -> System.out.println(video));*/

        /**高亮查询*/
        elasticsearchTemplate.queryForPage(nativeSearchQuery, Video.class, new SearchResultMapper() {
            //匿名内部类
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                ArrayList<Video> videos = new ArrayList<>();


                //获取查询结果
                SearchHit[] hits = searchResponse.getHits().getHits();

                for (SearchHit hit : hits) {

                    //System.out.println("元数据："+hit.getSourceAsMap());//原始数据
                    //System.out.println("高亮数据："+hit.getHighlightFields());//高亮数据

                    //处理源数据
                    Map<String, Object> map = hit.getSourceAsMap();
                    String id = map.containsKey("id") ? map.get("id").toString() : null;
                    String title = map.containsKey("title") ? map.get("title").toString() : null;
                    String brief = map.containsKey("brief") ? map.get("brief").toString() : null;
                    String path = map.containsKey("path") ? map.get("path").toString() : null;
                    String cover = map.containsKey("cover") ? map.get("cover").toString() : null;
                    String categoryId = map.containsKey("categoryId") ? map.get("categoryId").toString() : null;
                    String groupId = map.containsKey("groupId") ? map.get("groupId").toString() : null;
                    String userId = map.containsKey("userId") ? map.get("userId").toString() : null;

                    Date date = null;
                    if (map.containsKey("publishDate")) {
                        String publishDateStr = map.get("publishDate").toString();
                        //处理日期转换
                        Long aLong = Long.valueOf(publishDateStr);
                        date = new Date(aLong);
                    }

                    /*String id = map.get("id").toString();
                    String title = map.get("title").toString();
                    String brief = map.get("brief").toString();
                    String path = map.get("path").toString();
                    String cover = map.get("cover").toString();
                    //处理日期格式操作
                    String publishDateStr = map.get("publishDate").toString();
                    Long aLong = Long.valueOf(publishDateStr);
                    Date date = new Date(aLong);

                    *//*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date publisDate = null;
                    try {
                        publisDate = simpleDateFormat.parse(publishDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*//*
                    String categoryId = map.get("categoryId").toString();
                    String groupId = map.get("groupId").toString();
                    String userId = map.get("userId").toString();*/

                    //封装Video对象元数据
                    Video video = new Video(id, title, brief, path, cover, date, categoryId, groupId, userId);

                    System.out.println("元数据对象" + video);

                    //处理高亮数据
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    //高亮字段的判断
                    if (highlightFields.get("brief") != null) {
                        String briefs = highlightFields.get("brief").fragments()[0].toString();
                        video.setBrief(briefs);
                    }
                    if (highlightFields.get("title") != null) {
                        String titles = highlightFields.get("title").fragments()[0].toString();
                        video.setTitle(titles);
                    }

                    System.out.println("高亮数据" + video);

                    //将对象放入集合
                    videos.add(video);

                }
                return null;
            }
        });

    }

    @Test
    public void querySearchs() {

        //分词查询 指定查询词
        String content = "橘子";

        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style = 'color:red'>");//高亮前缀
        field.postTags("</span>");//后缀

        //查询条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingxv")  //指定索引名
                .withTypes("video")       //指定索引类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //指定搜索条件
                .withHighlightFields(field)   //处理高亮
                //.withFields("title","cover","path","publishDate")  //高亮返回指定字段
                .build();

        //相当于元数据查询
       /* List<Video> videos = elasticsearchTemplate.queryForList(searchQuery, Video.class);
        videos.forEach(video -> System.out.println(video));*/

        /**高亮查询*/
        AggregatedPage<Video> videoList = elasticsearchTemplate.queryForPage(nativeSearchQuery, Video.class, new SearchResultMapper() {
            //匿名内部类
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                ArrayList<Video> videos = new ArrayList<>();


                //获取查询结果
                SearchHit[] hits = searchResponse.getHits().getHits();

                for (SearchHit hit : hits) {

                    //处理源数据
                    Map<String, Object> map = hit.getSourceAsMap();
                    //处理普通数据
                    String id = map.get("id") != null ? map.containsKey("id") ? map.get("id").toString() : null : null;
                    String title = map.get("title") != null ? map.containsKey("title") ? map.get("title").toString() : null : null;
                    String brief = map.get("brief") != null ? map.containsKey("brief") ? map.get("brief").toString() : null : null;
                    String path = map.get("path") != null ? map.containsKey("path") ? map.get("path").toString() : null : null;
                    String cover = map.get("cover") != null ? map.containsKey("cover") ? map.get("cover").toString() : null : null;
                    String categoryId = map.get("categoryId") != null ? map.containsKey("categoryId") ? map.get("categoryId").toString() : null : null;
                    String groupId = map.get("groupId") != null ? map.containsKey("groupId") ? map.get("groupId").toString() : null : null;
                    String userId = map.get("userId") != null ? map.containsKey("userId") ? map.get("userId").toString() : null : null;
                    //String publishDateStr = map.get("publishDate").toString();
                    //System.out.println(publishDateStr);
                    Date date = null;
                    if (map.get("publishDate") != null) {
                        if (map.containsKey("publishDate")) {
                            String publishDateStr = map.get("publishDate").toString();
                            //处理日期转换
                            Long aLong = Long.valueOf(publishDateStr);
                            date = new Date(aLong);
                        }
                    }


                    //封装Video对象元数据
                    Video video = new Video(id, title, brief, path, cover, date, categoryId, groupId, userId);

                    //System.out.println("元数据对象" + video);

                    //处理高亮数据
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    //高亮字段的判断
                    if (brief != null) {
                        if (highlightFields.get("brief") != null) {
                            String briefs = highlightFields.get("brief").fragments()[0].toString();
                            video.setBrief(briefs);
                        }
                    }
                    if (title != null) {
                        if (highlightFields.get("title") != null) {
                            String titles = highlightFields.get("title").fragments()[0].toString();
                            video.setTitle(titles);
                        }
                    }


                    //System.out.println("高亮数据" + video);

                    //将对象放入集合
                    videos.add(video);

                }
                //返回做一个强转返回
                return new AggregatedPageImpl<T>((List<T>) videos);
            }
        });

        videoList.forEach(video -> System.out.println(video));
    }
}
