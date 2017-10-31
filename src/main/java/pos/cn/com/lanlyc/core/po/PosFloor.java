package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posFloor 实体类
    * Thu Sep 14 16:02:24 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_floor")
public class PosFloor  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String constructId;

	private String dm;

	private String min_x;

	private String min_y;

	private String max_x;

	private String max_y;

	private String url;

	private String layer_name;

	private String datas_name;

	private String space_name;

	private String scale;

	private String bjtx;

	private String no_use_mapserver;

	private String jz_color;

	private String dsid;

	private String hly;

	private String xh;
	
	private String user_count; //图层人员个数
	
	public String getUser_count() {
		return user_count;
	}
	public void setUser_count(String user_count) {
		this.user_count = user_count;
	}
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setConstructId(String constructId){
	this.constructId=constructId;
	}
	public String getConstructId(){
		return constructId;
	}
	public void setDm(String dm){
	this.dm=dm;
	}
	public String getDm(){
		return dm;
	}
	public void setMin_x(String min_x){
	this.min_x=min_x;
	}
	public String getMin_x(){
		return min_x;
	}
	public void setMin_y(String min_y){
	this.min_y=min_y;
	}
	public String getMin_y(){
		return min_y;
	}
	public void setMax_x(String max_x){
	this.max_x=max_x;
	}
	public String getMax_x(){
		return max_x;
	}
	public void setMax_y(String max_y){
	this.max_y=max_y;
	}
	public String getMax_y(){
		return max_y;
	}
	public void setUrl(String url){
	this.url=url;
	}
	public String getUrl(){
		return url;
	}
	public void setLayer_name(String layer_name){
	this.layer_name=layer_name;
	}
	public String getLayer_name(){
		return layer_name;
	}
	public void setDatas_name(String datas_name){
	this.datas_name=datas_name;
	}
	public String getDatas_name(){
		return datas_name;
	}
	public void setSpace_name(String space_name){
	this.space_name=space_name;
	}
	public String getSpace_name(){
		return space_name;
	}
	public void setScale(String scale){
	this.scale=scale;
	}
	public String getScale(){
		return scale;
	}
	public void setBjtx(String bjtx){
	this.bjtx=bjtx;
	}
	public String getBjtx(){
		return bjtx;
	}
	public void setNo_use_mapserver(String no_use_mapserver){
	this.no_use_mapserver=no_use_mapserver;
	}
	public String getNo_use_mapserver(){
		return no_use_mapserver;
	}
	public void setJz_color(String jz_color){
	this.jz_color=jz_color;
	}
	public String getJz_color(){
		return jz_color;
	}
	public void setDsid(String dsid){
	this.dsid=dsid;
	}
	public String getDsid(){
		return dsid;
	}
	public void setHly(String hly){
	this.hly=hly;
	}
	public String getHly(){
		return hly;
	}
	public void setXh(String xh){
	this.xh=xh;
	}
	public String getXh(){
		return xh;
	}
}

