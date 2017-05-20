package zxzq.xiaobai.dailynews.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */
//{"message":"OK","status":0,"data":
// {"uid":"gjba","integration":0,
// "loginlog":[
// {"time":"2017-01-17 00:00:00.0","address":"上海","device":0},
// {"time":"2017-01-17 00:00:00.0","address":"北京","device":0}],
// "comnum":0,
// "portrait":"http:\/\/118.244.212.82:9092\/Images\/image.png"}
// }
public class UserInfo implements Serializable{

    /**
     * “data”:{
     “uid”:用户名
     “portrait”:用户图标
     “integration”:用户积分票总数
     “comnum”:评论总数
     “loginlog”:[
     {
     “time”:登录时间
     “address”:北京市朝阳区
     “device”:0
     }
     ……
     ]
     */
    //用户名
    private String uid;
    //积分
    private int integration;
    //头像
    private String portrait;
    //跟帖数量
    private int comnum;
    //登录日志
    private List<Loginlog> loginlog;

    public class Loginlog implements Serializable{
              //登录时间
        private String time;
        //登录地址
        private String address;
        //登录设备
        private String device;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        @Override
        public String toString() {
            return "Loginlog{" +
                    "time='" + time + '\'' +
                    ", address='" + address + '\'' +
                    ", device='" + device + '\'' +
                    '}';
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getComnum() {
        return comnum;
    }

    public void setComnum(int comnum) {
        this.comnum = comnum;
    }

    public List<Loginlog> getLoginlog() {
        return loginlog;
    }

    public void setLoginlog(List<Loginlog> loginlog) {
        this.loginlog = loginlog;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", integration=" + integration +
                ", portrait='" + portrait + '\'' +
                ", comnum=" + comnum +
                ", loginlog=" + loginlog +
                '}';
    }
}
