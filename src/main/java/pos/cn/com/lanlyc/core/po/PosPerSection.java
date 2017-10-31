package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posPerSection 实体类
    * Thu Sep 14 16:05:21 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_perSection")
public class PosPerSection  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private String id;

	private String perId;

	private String sectionId;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setPerId(String perId){
	this.perId=perId;
	}
	public String getPerId(){
		return perId;
	}
	public void setSectionId(String sectionId){
	this.sectionId=sectionId;
	}
	public String getSectionId(){
		return sectionId;
	}
}

