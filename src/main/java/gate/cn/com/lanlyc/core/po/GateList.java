package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * gate_list 实体类
    * Wed Aug 23 14:52:45 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_list")

public class GateList  implements Serializable{
	/**
     * 唯一ID
     */
	   @PrimaryKeyMapper
	private String id;
	private String sn;
	private String gate_no;
	private String location;
	private String connect_port;
	private String connect_id;
	private String type;
	private String connect_ip;
	private String gate_name;
	private String is_kaoqin;
	private String is_use;
	private String cross_flag;
	private String group_id;
	private String common;
	private String read_bytes;
	private String is_master;
	private String password;
	private String last_uploadrecord_time;
	private String last_queryrecord_time;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setSN(String sn){
	this.sn=sn;
	}
	public String getSN(){
		return sn;
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
	public void setConnect_ip(String connect_ip){
	this.connect_ip=connect_ip;
	}
	public String getConnect_ip(){
		return connect_ip;
	}
	public void setGate_name(String gate_name){
	this.gate_name=gate_name;
	}
	public String getGate_name(){
		return gate_name;
	}
	public void setIs_kaoqin(String is_kaoqin){
	this.is_kaoqin=is_kaoqin;
	}
	public String getIs_kaoqin(){
		return is_kaoqin;
	}
	public void setIs_use(String is_use){
	this.is_use=is_use;
	}
	public String getIs_use(){
		return is_use;
	}
	public void setCross_flag(String cross_flag){
	this.cross_flag=cross_flag;
	}
	public String getCross_flag(){
		return cross_flag;
	}
	public void setGroup_id(String group_id){
	this.group_id=group_id;
	}
	public String getGroup_id(){
		return group_id;
	}
	public void setCommon(String common){
	this.common=common;
	}
	public String getCommon(){
		return common;
	}
	public void setRead_bytes(String read_bytes){
	this.read_bytes=read_bytes;
	}
	public String getRead_bytes(){
		return read_bytes;
	}
	public void setIs_master(String is_master){
	this.is_master=is_master;
	}
	public String getIs_master(){
		return is_master;
	}
	public void setPassword(String password){
	this.password=password;
	}
	public String getPassword(){
		return password;
	}
	public String getLast_uploadrecord_time() {
		return last_uploadrecord_time;
	}
	public void setLast_uploadrecord_time(String last_uploadrecord_time) {
		this.last_uploadrecord_time = last_uploadrecord_time;
	}
	public String getLast_queryrecord_time() {
		return last_queryrecord_time;
	}
	public void setLast_queryrecord_time(String last_queryrecord_time) {
		this.last_queryrecord_time = last_queryrecord_time;
	}
}

