package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_user 实体类
    * Wed Aug 16 15:33:06 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_user")

public class GateUser  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String cid;
	private String name;
	private String mobile;
	private String gender;
	private String sate;
	private String avatar_img;
	private String workers_type;
	private String team;
	private String punish_record;
	private String user_class_id;
	private String birthday;
	private int gonghao;
	private String card_sn;
	private String staff_visitor;
	private String hometown;
	
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public int getGonghao() {
		return gonghao;
	}
	public void setGonghao(int gonghao) {
		this.gonghao = gonghao;
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
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCard_sn() {
		return card_sn;
	}
	public void setCard_sn(String card_sn) {
		this.card_sn = card_sn;
	}
	public String getStaff_visitor() {
		return staff_visitor;
	}
	public void setStaff_visitor(String staff_visitor) {
		this.staff_visitor = staff_visitor;
	}
	
}

