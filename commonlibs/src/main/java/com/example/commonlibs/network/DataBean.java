package com.example.commonlibs.network;

import java.util.List;

public class DataBean {


    /**
     * date : 20190425
     * stories : [{"images":["https://pic3.zhimg.com/v2-0b7148b0fb94d4b4c32644c5de78c5f2.jpg"],"type":0,"id":9496537,"ga_prefix":"042509","title":"「汉堡是垃圾食品」的说法，究竟有没有道理？"},{"title":"大码模特：胖女孩也可以很美吗？","ga_prefix":"042508","images":["https://pic1.zhimg.com/v2-5296e3ef6bcb3f5374b261389795b0ac.jpg"],"multipic":true,"type":0,"id":9710685},{"title":"玩手机把孩子遗忘在车内致死，该如何避免类似的悲剧？","ga_prefix":"042507","images":["https://pic3.zhimg.com/v2-523936560e8035775e38216678e1e33a.jpg"],"multipic":true,"type":0,"id":9710660},{"images":["https://pic1.zhimg.com/v2-c3d2d80375c155be788ebddd16cf3780.jpg"],"type":0,"id":9710666,"ga_prefix":"042506","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic4.zhimg.com/v2-a6ce90dd54915267bb01678ba3ea4967.jpg","type":0,"id":9710660,"ga_prefix":"042507","title":"玩手机把孩子遗忘在车内致死，该如何避免类似的悲剧？"},{"image":"https://pic1.zhimg.com/v2-da5785b72cb096d9812bcc462e8d00b0.jpg","type":0,"id":9710597,"ga_prefix":"042407","title":"17 岁男孩跳桥身亡，请不要再「杀人诛心」了"},{"image":"https://pic1.zhimg.com/v2-37f17a07c3534905d9b018606e524788.jpg","type":0,"id":9710541,"ga_prefix":"042320","title":"今晚看「复联 4」之前，请做好这些准备"},{"image":"https://pic3.zhimg.com/v2-d79e8dbab7c9d0de60dcef30621c5086.jpg","type":0,"id":9710586,"ga_prefix":"042308","title":"《复仇者联盟》为什么有这么大的魅力？"},{"image":"https://pic2.zhimg.com/v2-9bbd384b48c5060165434119b3b9f20d.jpg","type":0,"id":9710554,"ga_prefix":"042307","title":"漫画家从业血泪史"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic3.zhimg.com/v2-0b7148b0fb94d4b4c32644c5de78c5f2.jpg"]
         * type : 0
         * id : 9496537
         * ga_prefix : 042509
         * title : 「汉堡是垃圾食品」的说法，究竟有没有道理？
         * multipic : true
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic4.zhimg.com/v2-a6ce90dd54915267bb01678ba3ea4967.jpg
         * type : 0
         * id : 9710660
         * ga_prefix : 042507
         * title : 玩手机把孩子遗忘在车内致死，该如何避免类似的悲剧？
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }
}
