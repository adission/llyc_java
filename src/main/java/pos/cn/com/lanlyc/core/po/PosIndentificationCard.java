package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posIndentificationCard 实体类
    * Thu Sep 14 16:02:41 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_indentificationCard")
public class PosIndentificationCard  implements Serializable{

	private static final long serialVersionUID = 1L; 
	
	public PosIndentificationCard(String card_sn) {
		this.card_sn = card_sn;
	}
	
	public PosIndentificationCard() {
		// TODO Auto-generated method stub
	}

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String geo_x;

	private String geo_y;

	private String card_sn;

	private String layer_id;

	private String dt;

	private String inside_qyids;

	private String inside_qymcs;

	private String one_station_id;

	private String one_station_distance;

	private String nearly_station;

	private String direction;

	private String direction_change;

	private String description;
	
	private int cardType;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setGeo_x(String geo_x){
	this.geo_x=geo_x;
	}
	public String getGeo_x(){
		return geo_x;
	}
	public void setGeo_y(String geo_y){
	this.geo_y=geo_y;
	}
	public String getGeo_y(){
		return geo_y;
	}
	public void setCard_sn(String card_sn){
	this.card_sn=card_sn;
	}
	public String getCard_sn(){
		return card_sn;
	}
	public void setLayer_id(String layer_id){
	this.layer_id=layer_id;
	}
	public String getLayer_id(){
		return layer_id;
	}
	public void setDt(String dt){
	this.dt=dt;
	}
	public String getDt(){
		return dt;
	}
	public void setInside_qyids(String inside_qyids){
	this.inside_qyids=inside_qyids;
	}
	public String getInside_qyids(){
		return inside_qyids;
	}
	public void setInside_qymcs(String inside_qymcs){
	this.inside_qymcs=inside_qymcs;
	}
	public String getInside_qymcs(){
		return inside_qymcs;
	}
	public void setOne_station_id(String one_station_id){
	this.one_station_id=one_station_id;
	}
	public String getOne_station_id(){
		return one_station_id;
	}
	public void setOne_station_distance(String one_station_distance){
	this.one_station_distance=one_station_distance;
	}
	public String getOne_station_distance(){
		return one_station_distance;
	}
	public void setNearly_station(String nearly_station){
	this.nearly_station=nearly_station;
	}
	public String getNearly_station(){
		return nearly_station;
	}
	public void setDirection(String direction){
	this.direction=direction;
	}
	public String getDirection(){
		return direction;
	}
	public void setDirection_change(String direction_change){
	this.direction_change=direction_change;
	}
	public String getDirection_change(){
		return direction_change;
	}
	public void setDescription(String description){
	this.description=description;
	}
	public String getDescription(){
		return description;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
}

