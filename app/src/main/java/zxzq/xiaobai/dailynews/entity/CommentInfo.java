package zxzq.xiaobai.dailynews.entity;

/**
 * Created by 蔡传飞 on 2017-05-17.
 */

public class CommentInfo {
    private int cid;//评论编号
    private String uid;//评论者名字
    private String portroit;//用户头像链接
    private String stamp;
    private String content;

    @Override
    public String toString() {
        return "CommentInfo{" +
                "cid=" + cid +
                ", uid='" + uid + '\'' +
                ", portroit='" + portroit + '\'' +
                ", stamp='" + stamp + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortroit() {
        return portroit;
    }

    public void setPortroit(String portroit) {
        this.portroit = portroit;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
