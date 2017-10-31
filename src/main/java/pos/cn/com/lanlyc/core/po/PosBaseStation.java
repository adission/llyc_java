package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posBaseStation 实体类
    * Thu Sep 14 16:01:39 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_baseStation")
public class PosBaseStation  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String mc;

	private String type;

	private String station_id;

	private String geo_x;

	private String geo_y;

	private String ip;

	private String layer_id;

	private String repairRSSI;

	private String one_metre_RSSI;

	private String decay_rate;

	private String port;

	private String yxjl;

	private String xgjz;

	private String xzjl;

	private String status;
	
	private String base_number;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setMc(String mc){
	this.mc=mc;
	}
	public String getMc(){
		return mc;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setStation_id(String station_id){
	this.station_id=station_id;
	}
	public String getStation_id(){
		return station_id;
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
	public void setIp(String ip){
	this.ip=ip;
	}
	public String getIp(){
		return ip;
	}
	public void setLayer_id(String layer_id){
	this.layer_id=layer_id;
	}
	public String getLayer_id(){
		return layer_id;
	}
	public void setRepairRSSI(String repairRSSI){
	this.repairRSSI=repairRSSI;
	}
	public String getRepairRSSI(){
		return repairRSSI;
	}
	public void setOne_metre_RSSI(String one_metre_RSSI){
	this.one_metre_RSSI=one_metre_RSSI;
	}
	public String getOne_metre_RSSI(){
		return one_metre_RSSI;
	}
	public void setDecay_rate(String decay_rate){
	this.decay_rate=decay_rate;
	}
	public String getDecay_rate(){
		return decay_rate;
	}
	public void setPort(String port){
	this.port=port;
	}
	public String getPort(){
		return port;
	}
	public void setYxjl(String yxjl){
	this.yxjl=yxjl;
	}
	public String getYxjl(){
		return yxjl;
	}
	public void setXgjz(String xgjz){
	this.xgjz=xgjz;
	}
	public String getXgjz(){
		return xgjz;
	}
	public void setXzjl(String xzjl){
	this.xzjl=xzjl;
	}
	public String getXzjl(){
		return xzjl;
	}
	public void setStatus(String status){
	this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public String getBase_number() {
		return base_number;
	}
	public void setBase_number(String base_number) {
		this.base_number = base_number;
	}
	
}

