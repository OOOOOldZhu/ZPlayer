package com.z.zmusicplayer.bean;

import java.util.List;

public class MvListBean {

    private int totalCount;
    private List<VideoBean> videos;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VideoBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoBean> videos) {
        this.videos = videos;
    }
}
