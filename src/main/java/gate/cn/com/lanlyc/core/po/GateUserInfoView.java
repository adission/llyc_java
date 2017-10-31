package gate.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;


   /**
    * gate_v_user_info 实体类
    * Wed Sep 06 16:13:14 CST 2017 黎洋富
    */ 


@TableMapper("gate_v_user_info")

public class GateUserInfoView  implements Serializable{
	private String id;
	private String cid;
	private String name;
	private String mobile;
	private String gender;
	private String sate;
	private String sate_value;
	private String age;
	private String avatar_img;
	private String workers_type;
	private String team;
	private String birthday;
	private String gonghao;
	
	private String punish_record;
	private String user_class_id;
	private String class_name;
	private String worker_type;
	private String user_card;
	private String card_sn;
	
	private String geo_x; //人员x坐标
	
	private String geo_y; //人员y坐标
	private String layer_id;  //图层id
	private String maxtime; //最大时间
	private String aperId;//人员id
	private String staff_visitor ;//区分工作人员1与访客0
	
	public String getStaff_visitor() {
		return staff_visitor;
	}
	public void setStaff_visitor(String staff_visitor) {
		this.staff_visitor = staff_visitor;
	}
	public String getMaxtime() {
		return maxtime;
	}
	public void setMaxtime(String maxtime) {
		this.maxtime = maxtime;
	}
	public String getGeo_x() {
		return geo_x;
	}
	public String getAperId() {
		return aperId;
	}
	public void setAperId(String aperId) {
		this.aperId = aperId;
	}
	public void setGeo_x(String geo_x) {
		this.geo_x = geo_x;
	}
	public String getGeo_y() {
		return geo_y;
	}
	public void setGeo_y(String geo_y) {
		this.geo_y = geo_y;
	}
	public String getLayer_id() {
		return layer_id;
	}
	public void setLayer_id(String layer_id) {
		this.layer_id = layer_id;
	}
	public String getCard_sn() {
		return card_sn;
	}
	public void setCard_sn(String card_sn) {
		this.card_sn = card_sn;
	}
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setCid(String cid){
	this.cid=cid;
	}
	public String getCid(){
		return cid;
	}
	public void setName(String name){
	this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setMobile(String mobile){
	this.mobile=mobile;
	}
	public String getMobile(){
		return mobile;
	}
	public void setGender(String gender){
	this.gender=gender;
	}
	public String getGender(){
		return gender;
	}
	public void setSate(String sate){
	this.sate=sate;
	}
	public String getSate(){
		return sate;
	}
	public String getSate_value() {
		return sate_value;
	}
	public void setSate_value(String sate_value) {
		this.sate_value = sate_value;
	}
	public void setAge(String age){
	this.age=age;
	}
	public String getAge(){
		return age;
	}
	public void setAvatar_img(String avatar_img){
	this.avatar_img=avatar_img;
	}
	public String getAvatar_img(){
		return avatar_img;
	}
	public void setWorkers_type(String workers_type){
	this.workers_type=workers_type;
	}
	public String getWorkers_type(){
		return workers_type;
	}
	public void setTeam(String team){
	this.team=team;
	}
	public String getTeam(){
		return team;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGonghao() {
		return gonghao;
	}
	public void setGonghao(String gonghao) {
		this.gonghao = gonghao;
	}
	public void setPunish_record(String punish_record){
	this.punish_record=punish_record;
	}
	public String getPunish_record(){
		return punish_record;
	}
	public void setUser_class_id(String user_class_id){
	this.user_class_id=user_class_id;
	}
	public String getUser_class_id(){
		return user_class_id;
	}
	public void setClass_name(String class_name){
	this.class_name=class_name;
	}
	public String getClass_name(){
		return class_name;
	}
	public void setWorker_type(String worker_type){
	this.worker_type=worker_type;
	}
	public String getWorker_type(){
		return worker_type;
	}
	public void setuser_card(String user_card){
	this.user_card=user_card;
	}
	public String getuser_card(){
		return user_card;
	}
}

