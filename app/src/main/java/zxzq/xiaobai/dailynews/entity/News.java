package zxzq.xiaobai.dailynews.entity;

import java.io.Serializable;

/**
 * Created by 蔡传飞 on 2017-05-03.
 */

public class News implements Serializable {
    /**
     * Serializable 接口是启用序列化功能，实现java.io.Serializable是可序列化的
     */
    /*
      “type”:1,
			“nid”:新闻编号,
			“stamp":新闻时间戳,
			“icon”:图标路径,
			“title”:,新闻标题
			“summary”:,新闻摘要
			“link”:新闻链接
     */
    private static final long serialVersionUID=1L;//版本号
    private int type;
    private int nid;
    private String stamp;
    private String icon;
    private String title;
    private String summary;
    private String link;

    public News(int type, int nid, String stamp, String icon, String title, String summary, String link) {
        this.type = type;
        this.nid = nid;
        this.stamp = stamp;
        this.icon = icon;
        this.title = title;
        this.summary = summary;
        this.link = link;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getType() {
        return type;
    }

    public int getNid() {
        return nid;
    }

    public String getStamp() {
        return stamp;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "News{" +
                "type=" + type +
                ", nid=" + nid +
                ", stamp='" + stamp + '\'' +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
