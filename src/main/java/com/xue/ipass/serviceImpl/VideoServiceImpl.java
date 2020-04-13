package com.xue.ipass.serviceImpl;

import com.xue.ipass.annotation.AddCache;
import com.xue.ipass.annotation.DelCache;
import com.xue.ipass.dao.VideoMapper;
import com.xue.ipass.entity.Video;
import com.xue.ipass.entity.VideoExample;
import com.xue.ipass.po.VideoPo;
import com.xue.ipass.repository.VideoRepository;
import com.xue.ipass.service.VideoService;
import com.xue.ipass.util.AliyunOssUtil;
import com.xue.ipass.util.InterceptVideoPhotoUtil;
import com.xue.ipass.vo.VideoVo;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {


    @Resource
    VideoMapper videoMapper;

    @Resource
    HttpSession session;

    @Resource
    ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    VideoRepository videoRepository;

    @AddCache
    /**查询分页数据*/
    @Override
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {

        HashMap<String, Object> map = new HashMap<>();

        //总条数 records
        VideoExample example = new VideoExample();
        Integer records = videoMapper.selectCountByExample(example);
        map.put("records", records);

        //总页数 total
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);

        //当前页 page
        map.put("page", page);

        //数据 rows
        RowBounds rowBounds = new RowBounds(page - 1 * rows, rows);
        List<Video> videos = videoMapper.selectByRowBounds(new Video(), rowBounds);
        map.put("rows", videos);

        return map;
    }

    /**
     * 添加至阿里云
     */
    @Override
    public String add(Video video) {

        String uid = UUID.randomUUID().toString();

        video.setId(uid); //id
        video.setPublishDate(new Date());
        video.setUserId("1");
        video.setCategoryId("1");
        video.setGroupId("1");

        System.out.println("从业务成插入数据库---video---" + video);

        //向mysql添加
        videoMapper.insert(video);

        //向es中去构建索引
        //videoRepository.save(video);

        //返回数据的id
        return uid;
    }

    /**
     * 将图片上传至阿里云
     */
    @Override
    public void uploadVideo(MultipartFile path, String id, HttpServletRequest request) {
        //上传到阿里云

        //获取文件名
        String filename = path.getOriginalFilename();
        String newName = new Date().getTime() + "-" + filename;

        /*1.视频上传至阿里云
         *上传字节数组
         * 参数：
         *   bucket:存储空间名
         *   headImg: 指定MultipartFile类型的文件
         *   fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
         * */
        AliyunOssUtil.uploadFileBytes("yingx-185", path, "video/" + newName);


        //频接视频完整路径
        String netFilePath = "https://yingx-185.oss-cn-beijing.aliyuncs.com/video/" + newName;



        /*2.截取视频第一帧做封面
         * 获取指定视频的帧并保存为图片至指定目录
         * @param videofile 源视频文件路径
         * @param framefile 截取帧的图片存放路径
         * */
        String realPath = session.getServletContext().getRealPath("/upload/cover");

        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        //频接本地存放路径    D:\动画.jpg
        // 1585661687777-好看.mp4
        String[] names = newName.split("\\.");
        String interceptName = names[0];
        String coverName = interceptName + ".jpg";  //频接封面名字
        String coverPath = realPath + "\\" + coverName;  //频接视频封面的本地绝对路径


        //截取封面保存到本地
        try {
            InterceptVideoPhotoUtil.fetchFrame(netFilePath, coverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*3.将封面上传至阿里云
         *上传本地文件
         * 参数：
         *   bucket:  存储空间名
         *   fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
         *   localFilePath: 指定本地文件路径
         * */
        AliyunOssUtil.uploadFile("yingx-185", "photo/" + coverName, coverPath);

        //4.删除本地文件
        File file1 = new File(coverPath);
        //判断是一个文件，并且文件存在
        if (file1.isFile() && file1.exists()) {
            //删除文件
            boolean isDel = file1.delete();
            System.out.println("删除：" + isDel);
        }

        //5.修改视频信息
        //添加修改条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        //修改的结果
        Video video = new Video();

        video.setPath("https://yingx-185.oss-cn-beijing.aliyuncs.com/video/" + newName);
        video.setCover("https://yingx-185.oss-cn-beijing.aliyuncs.com/photo/" + coverName);

        //调用修改方法
        videoMapper.updateByExampleSelective(video, example);

    }


    /**
     * 将上传的视频截取封面
     */
    @Override
    public void uploadVideos(MultipartFile path, String id, HttpServletRequest request) {

        //上传至阿里云

        //获取文件名
        String filename = path.getOriginalFilename();
        String newName = new Date().getTime() + "-" + filename;

        /*1：视屏上传至阿里云
          上传字节数组
        * 参数：
        *      bucket:存储空间名
        *      headImg :指定MultipartFile类型的文件
        *      fileName :指定上传文件名 可以指定上传目录 目录名/文件名
        * */
        AliyunOssUtil.uploadFileBytes("yingx-185", path, "video/" + newName);


        //频接视频完整路径
        String netFilePath = "https://yingx-185.oss-cn-beijing.aliyuncs.com/video/" + newName;

        //频接本地存放路径    D:\动画.jpg
        // 1585661687777-好看.mp4
        /*String[] names = newName.split("\\.");
        String interceptName=names[0];
        String coverName=interceptName+".jpg";  //频接封面名字*/

        /**2：截取视频第一帧做封面
         * 视频截取  并上传至阿里云
         * 参数：
         *   bucker: 存储空间名
         *   fileName:远程文件文件名    目录名/文件名
         *   coverName：截取的封面名
         * */
        /*AliyunOssUtil.videoCoverIntercept("yingx-185","video/"+newName,"photo/"+coverName);*/


        //拼接本地存放路径 C:\动画.sp
        /*String[] split = newName.split("\\.");
        String intercepyName = split[0];
        String photoName = interceptName+".jpg"; //拼接封面名字*/
        String[] names = newName.split("\\.");
        String interceptName = names[0];
        String coverName = interceptName + ".jpg";  //频接封面名字

        AliyunOssUtil.videoCoverIntercept("yingx-185", "video/" + newName, "photo/" + coverName);


        //5：修给视屏信息
        VideoExample example = new VideoExample();
        //修改的条件
        example.createCriteria().andIdEqualTo(id);
        //修改的结果
        Video video = new Video();

        //网络路径 ：https://yingx-185.oss-cn-beijing.aliyuncs.com/video/1585788222338-7.jpg
        video.setPath("https://yingx-185.oss-cn-beijing.aliyuncs.com/video/" + newName);
        video.setCover("https://yingx-185.oss-cn-beijing.aliyuncs.com/photo/" + coverName);

        //调用修改方法
        videoMapper.updateByExampleSelective(video, example);


        /**添加数据时,为存入缓存数据设置id*/
        video.setId(id);
        //根据video查询，返回videos
        Video videos = videoMapper.selectOne(video);
        System.out.println(videos);

        /**向es中构建索引*/
        videoRepository.save(videos);

    }

    /**
     * 上传至阿里云修改操作
     */
    @DelCache
    @Override
    public void update(Video video) {
        System.out.println("修改" + video);
        videoMapper.updateByPrimaryKeySelective(video);
    }


    /**
     * 上传至阿里云删除操作
     */
    @Override
    public HashMap<String, Object> delete(Video video) {

        HashMap<String, Object> map = new HashMap<>();

        try {

            /**删除数据
             设置条件*/
            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(video.getId());

            //根据Id查询视频数据
            Video videos = videoMapper.selectOneByExample(example);

            //1:删除数据
            videoMapper.deleteByExample(example);


            //字符串拆分
            String[] pathSplit = videos.getPath().split("/");
            String[] coverSplit = videos.getCover().split("/");

            String aaa = "video/" + pathSplit[4];

            String videoName = pathSplit[pathSplit.length - 2] + "/" + pathSplit[pathSplit.length - 1];
            String coverName = coverSplit[coverSplit.length - 2] + "/" + coverSplit[coverSplit.length - 1];

            System.out.println(videoName);
            System.out.println(coverName);
            /**2:删除阿里云视频文件
             参数
             headImg  指定 MultipartFile 类型的文
             fileName  指定文件名 可以指定上传目录 目录名/文件名
             LocalFile 指定本地文件路径*/

            AliyunOssUtil.delete("yingx-185", videoName);

            /**3:删除阿里云封面文件
             参数
             headImg  指定 MultipartFile 类型的文
             fileName  指定文件名 可以指定上传目录 目录名/文件名
             LocalFile 指定本地文件路径
             */
            AliyunOssUtil.delete("yingx-185", coverName);

            /**删除索引*/
            videoRepository.delete(video);

            map.put("status", "200");
            map.put("message", "删除成功");

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }

    @Override
    public List<VideoVo> queryByReleaseTime() {

        List<VideoPo> videoPos = videoMapper.queryByReleaseTime();

        ArrayList<VideoVo> videoVolist = new ArrayList<>();
        //点赞数 遍历视频Id
        for (VideoPo v : videoPos) {

            String id = v.getId();
            //根据视频id去查询查询点赞数id0.................

            Integer likeCount = 18;

            VideoVo videoVo = new VideoVo(v.getId(), v.getVTitle(), v.getVBrief(), v.getVPath(),
                    v.getVCover(), v.getVPublishDate(), likeCount, v.getCateName(), v.getHeadImg()
            );
            videoVolist.add(videoVo);

        }

        return videoVolist;
    }

    /**
     * 高亮数据检索操作
     */
    @Override
    public List<Video> querySearch(String content) {

        //分词查询 指定查询词
        //查询条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingxv")  //指定索引名
                .withTypes("video")       //指定索引类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //指定搜索条件
                .build();

        //相当于元数据查询
        List<Video> videos = elasticsearchTemplate.queryForList(searchQuery, Video.class);
        videos.forEach(video -> System.out.println(video));

        return videos;
    }

    /**高亮操作数据*/
    @Override
    public List<Video> querySearchs(String content) {

        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style = 'color:red'>");//高亮前缀
        field.postTags("</span>");//后缀

        //查询条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withIndices("yingxv")  //指定索引名
            .withTypes("video")       //指定索引类型
            .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //指定搜索条件
            .withHighlightFields(field)   //处理高亮
            //.withFields("title","cover","path")  //高亮返回指定字段
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
                    //判断数据处理普通数据
                    String id = map.get("id")!=null? map.containsKey("id") ? map.get("id").toString() : null:null;
                    String title =map.get("title")!=null? map.containsKey("title") ? map.get("title").toString() : null:null;
                    String brief =map.get("brief")!=null? map.containsKey("brief") ? map.get("brief").toString() : null:null;
                    String path =map.get("path")!=null?  map.containsKey("path") ? map.get("path").toString() : null:null;
                    String cover =map.get("cover")!=null?  map.containsKey("cover") ? map.get("cover").toString() : null:null;
                    String categoryId = map.get("categoryId")!=null? map.containsKey("categoryId") ? map.get("categoryId").toString() : null:null;
                    String groupId =map.get("groupId")!=null?  map.containsKey("groupId") ? map.get("groupId").toString() : null:null;
                    String userId =map.get("userId")!=null?  map.containsKey("userId") ? map.get("userId").toString() : null:null;

                    //String publishDateStr = map.get("publishDate").toString();
                    Date date = null;
                    if (map.get("publishDate")!=null){
                        if (map.containsKey("publishDate")) {
                            String publishDateStr = map.get("publishDate").toString();
                            //处理日期转换
                            Long aLong = Long.valueOf(publishDateStr);
                            date = new Date(aLong);
                        }
                    }
                    //封装Video对象元数据
                    Video video = new Video(id, title, brief, path, cover, date, categoryId, groupId, userId);

                    System.out.println("元数据对象" + video);

                    //处理高亮数据
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    //高亮字段的判断
                    if (brief != null){
                        if (highlightFields.get("brief") != null) {
                            String briefs = highlightFields.get("brief").fragments()[0].toString();
                            video.setBrief(briefs);
                        }
                    }
                    if (title != null){
                        if (highlightFields.get("title") != null) {
                            String titles = highlightFields.get("title").fragments()[0].toString();
                            video.setTitle(titles);
                        }
                    }


                    System.out.println("高亮数据" + video);

                    //将对象放入集合
                    videos.add(video);

                }
                //返回做一个强转返回
                return new AggregatedPageImpl<T>((List<T>) videos);
            }
        });
        return videoList.getContent();
    }

}

