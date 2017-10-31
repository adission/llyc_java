package gate.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * gate_v_list 实体类
    * Thu Sep 28 11:24:10 CST 2017 黎洋富
    */ 


@TableMapper("gate_v_list")

public class GateListView  implements Serializable{
	private String id;
	private String SN;
	private String groupid;
	private String GROUP_ID;
	private String welcome;
	private String last_queryrecord_time;
	private String password;
	private String gate_no;
	private String gate_name;
	private String location;
	private String connect_id;
	private String connect_ip;
	private String connect_port;
	private String read_bytes;
	private String common;
	private String type_value;
	private String type;
	private String is_master;
	private String Is_kaoqin;
	private String crossflag;
	private String cross_flag;
	private String is_use;
	private String group_name;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setSN(String SN){
	this.SN=SN;
	}
	public String getSN(){
		return SN;
	}
	public void setGROUP_ID(String GROUP_ID){
	this.GROUP_ID=GROUP_ID;
	}
	public String getGROUP_ID(){
		return GROUP_ID;
	}
	public void setWelcome(String welcome){
	this.welcome=welcome;
	}
	public String getWelcome(){
		return welcome;
	}
	public void setLast_queryrecord_time(String last_queryrecord_time){
	this.last_queryrecord_time=last_queryrecord_time;
	}
	public String getLast_queryrecord_time(){
		return last_queryrecord_time;
	}
	public void setPassword(String password){
	this.password=password;
	}
	public String getPassword(){
		return password;
	}
	public void setGate_no(String gate_no){
	this.gate_no=gate_no;
	}
	public String getGate_no(){
		return gate_no;
	}
	public void setGate_name(String gate_name){
	this.gate_name=gate_name;
	}
	public String getGate_name(){
		return gate_name;
	}
	public void setLocation(String location){
	this.location=location;
	}
	public String getLocation(){
		return location;
	}
	public void setConnect_id(String connect_id){
	this.connect_id=connect_id;
	}
	public String getConnect_id(){
		return connect_id;
	}
	public void setConnect_ip(String connect_ip){
	this.connect_ip=connect_ip;
	}
	public String getConnect_ip(){
		return connect_ip;
	}
	public void setConnect_port(String connect_port){
	this.connect_port=connect_port;
	}
	public String getConnect_port(){
		return connect_port;
	}
	public void setRead_bytes(String read_bytes){
	this.read_bytes=read_bytes;
	}
	public String getRead_bytes(){
		return read_bytes;
	}
	public void setCommon(String common){
	this.common=common;
	}
	public String getCommon(){
		return common;
	}
	public void setType_value(String type_value){
	this.type_value=type_value;
	}
	public String getType_value(){
		return type_value;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setIs_master(String is_master){
	this.is_master=is_master;
	}
	public String getIs_master(){
		return is_master;
	}
	public void setIs_kaoqin(String Is_kaoqin){
	this.Is_kaoqin=Is_kaoqin;
	}
	public String getIs_kaoqin(){
		return Is_kaoqin;
	}
	public void setCross_flag(String cross_flag){
	this.cross_flag=cross_flag;
	}
	public String getCross_flag(){
		return cross_flag;
	}
	public void setIs_use(String is_use){
	this.is_use=is_use;
	}
	public String getIs_use(){
		return is_use;
	}
	public void setGroup_name(String group_name){
	this.group_name=group_name;
	}
	public String getGroup_name(){
		return group_name;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getCrossflag() {
		return crossflag;
	}
	public void setCrossflag(String crossflag) {
		this.crossflag = crossflag;
	}
}

