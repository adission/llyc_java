package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_v_check_log 实体类
    * Tue Sep 05 11:09:07 CST 2017 黎洋富
    */ 


@TableMapper("gate_v_check_log")

public class GateCheckLogView  implements Serializable{
	private String id;
	private String id_card;
	private String card_no;
	private String gate_no;
	private String type;
	private String cross_flag;
	private String check_date;
	private String check_type;
	private String is_shangbao;
	private String avatar_img;
	private String user_id;
	private String cid;
	private String gender;
	private String mobile;
	private String name;
	private String age;
	private String punish_record;
	private String team;
	private String worker_type;
	private String class_name;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setId_card(String id_card){
	this.id_card=id_card;
	}
	public String getId_card(){
		return id_card;
	}
	public void setCard_no(String card_no){
	this.card_no=card_no;
	}
	public String getCard_no(){
		return card_no;
	}
	public void setGate_no(String gate_no){
	this.gate_no=gate_no;
	}
	public String getGate_no(){
		return gate_no;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setCross_flag(String cross_flag){
	this.cross_flag=cross_flag;
	}
	public String getCross_flag(){
		return cross_flag;
	}
	public void setCheck_date(String check_date){
	this.check_date=check_date;
	}
	public String getCheck_date(){
		return check_date;
	}
	public void setCheck_type(String check_type){
	this.check_type=check_type;
	}
	public String getCheck_type(){
		return check_type;
	}
	public void setIs_shangbao(String is_shangbao){
	this.is_shangbao=is_shangbao;
	}
	public String getIs_shangbao(){
		return is_shangbao;
	}
	public void setAvatar_img(String avatar_img){
	this.avatar_img=avatar_img;
	}
	public String getAvatar_img(){
		return avatar_img;
	}
	public void setCid(String cid){
	this.cid=cid;
	}
	public String getCid(){
		return cid;
	}
	public void setGender(String gender){
	this.gender=gender;
	}
	public String getGender(){
		return gender;
	}
	public void setMobile(String mobile){
	this.mobile=mobile;
	}
	public String getMobile(){
		return mobile;
	}
	public void setName(String name){
	this.name=name;
	}
	public String getName(){
		return name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setPunish_record(String punish_record){
	this.punish_record=punish_record;
	}
	public String getPunish_record(){
		return punish_record;
	}
	public void setTeam(String team){
	this.team=team;
	}
	public String getTeam(){
		return team;
	}
	public void setWorker_type(String worker_type){
	this.worker_type=worker_type;
	}
	public String getWorker_type(){
		return worker_type;
	}
	public void setClass_name(String class_name){
	this.class_name=class_name;
	}
	public String getClass_name(){
		return class_name;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}

