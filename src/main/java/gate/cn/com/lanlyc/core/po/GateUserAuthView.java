package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_v_gate_auth 实体类
    * Tue Sep 05 11:25:47 CST 2017 黎洋富
    */ 


@TableMapper("gate_v_gate_auth")

public class GateUserAuthView  implements Serializable{
	private String id;
	private String gate_id;
	private String user_id;
	private String SN;
	private String gate_no;
	private String location;
	private String connect_ip;
	private String connect_port;
	private String password;
	private String connect_id;
	private String type;
	private String type_value;
	private String gate_name;
	private String common;
	private String id_card;
	private String name;
	private String gonghao;
	private String age;
	private String mobile;
	private String gender;
	private String sate;
	private String avatar_img;
	private String workers_type;
	private String team;
	private String punish_record;
	private String class_name;
	private String worker_type;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public String getGate_id() {
		return gate_id;
	}
	public void setGate_id(String gate_id) {
		this.gate_id = gate_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public void setSN(String SN){
	this.SN=SN;
	}
	public String getSN(){
		return SN;
	}
	public void setGate_no(String gate_no){
	this.gate_no=gate_no;
	}
	public String getGate_no(){
		return gate_no;
	}
	public void setLocation(String location){
	this.location=location;
	}
	public String getLocation(){
		return location;
	}
	public void setConnect_port(String connect_port){
	this.connect_port=connect_port;
	}
	public String getConnect_ip() {
		return connect_ip;
	}
	public void setConnect_ip(String connect_ip) {
		this.connect_ip = connect_ip;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConnect_port(){
		return connect_port;
	}
	public void setConnect_id(String connect_id){
	this.connect_id=connect_id;
	}
	public String getConnect_id(){
		return connect_id;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setGate_name(String gate_name){
	this.gate_name=gate_name;
	}
	public String getGate_name(){
		return gate_name;
	}
	public void setCommon(String common){
	this.common=common;
	}
	public String getCommon(){
		return common;
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
	public String getId_card() {
		return id_card;
	}
	public String getGonghao() {
		return gonghao;
	}
	public void setGonghao(String gonghao) {
		this.gonghao = gonghao;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getType_value() {
		return type_value;
	}
	public void setType_value(String type_value) {
		this.type_value = type_value;
	}
}

