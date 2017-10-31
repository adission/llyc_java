package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posConstructionSite 实体类
    * Thu Sep 14 16:04:42 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_constructionSite")
public class PosConstructionSite  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String name;

	private String conLongth;

	private String introduction;

	private String investAmount;

	private Date startDate;

	private Date planComDate;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setName(String name){
	this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setConLongth(String conLongth){
	this.conLongth=conLongth;
	}
	public String getConLongth(){
		return conLongth;
	}
	public void setIntroduction(String introduction){
	this.introduction=introduction;
	}
	public String getIntroduction(){
		return introduction;
	}
	public void setInvestAmount(String investAmount){
	this.investAmount=investAmount;
	}
	public String getInvestAmount(){
		return investAmount;
	}
	public void setStartDate(Date startDate){
	this.startDate=startDate;
	}
	public Date getStartDate(){
		return startDate;
	}
	public void setPlanComDate(Date planComDate){
	this.planComDate=planComDate;
	}
	public Date getPlanComDate(){
		return planComDate;
	}
}

