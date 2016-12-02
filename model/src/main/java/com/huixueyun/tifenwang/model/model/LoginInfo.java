package com.huixueyun.tifenwang.model.model;

public class LoginInfo {
    private String user_id = "";                                         //用户id
    private String xueduan = "";                                         //高中还是初中  3 高中   2 初中
    private String type = "";
    private String student_name = "";                                    //学生真实姓名
    private String student_phone = "";                                   //学生电话
    private String qq = "";                                              //qq号
    private String parent_phone = "";                                    //家长电话
    private String defaultlsSet = "";

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDefaultlsSet() {
        return defaultlsSet;
    }

    public void setDefaultlsSet(String defaultlsSet) {
        this.defaultlsSet = defaultlsSet;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getStudent_phone() {
        return student_phone;
    }

    public void setStudent_phone(String student_phone) {
        this.student_phone = student_phone;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXueduan() {
        return xueduan;
    }

    public void setXueduan(String xueduan) {
        this.xueduan = xueduan;
    }
}