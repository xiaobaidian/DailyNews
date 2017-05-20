package zxzq.xiaobai.dailynews.entity;

import java.util.List;

/**
 * Created by 蔡传飞 on 2017-05-03.
 */

public  class NewsType {
    /*
    “gid”：分类号
			“group”:分类名
			“subgrp”:[
				{
“subid”:子分类号，
				“subgroup”:子分类名
     */
    //分类号
    private int gid;
    //分类名
    private String group;

    public List<SubType> getSubgrp() {
        return subgrp;
    }

    public void setSubgrp(List<SubType> subgrp) {
        this.subgrp = subgrp;
    }

    //子类对象
    private List<SubType> subgrp;//存放子类对象的集合

    @Override
    public String toString() {
        return "NewsType{" +
                "gid=" + gid +
                ", group='" + group + '\'' +
                '}';
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public NewsType(int gid, String group) {

        this.gid = gid;
        this.group = group;
    }
    public static class SubType{
//子分类号
@Override
public String toString() {
    return "SubType{" +
            "subid=" + subid +
            ", subgroup='" + subgroup + '\'' +
            '}';
}private int subid;
        //子分类名

        public int getSubid() {
            return subid;
        }

        public void setSubid(int subid) {
            this.subid = subid;
        }

        public String getSubgroup() {
            return subgroup;
        }

        public void setSubgroup(String subgroup) {
            this.subgroup = subgroup;
        }

        public SubType(int subid, String subgroup) {

            this.subid = subid;
            this.subgroup = subgroup;
        }

        private String subgroup;
    }
}
