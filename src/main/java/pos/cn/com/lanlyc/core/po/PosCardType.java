package pos.cn.com.lanlyc.core.po;

import java.util.Date;
import java.io.Serializable;
import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;
   /**
    * posCardType 实体类
    * Sat Sep 23 16:17:10 CST 2017 程锦涛
    */ 

@TableMapper("pos_t_cardType")
public class PosCardType  implements Serializable{

	private static final long serialVersionUID = 1L; 

   /**
    * 唯一ID
    */ 
	@PrimaryKeyMapper
	private int id;

	private String typeName;

	private String color;
	public void setId(int id){
	this.id=id;
	}
	public int getId(){
		return id;
	}
	public void setTypeName(String typeName){
	this.typeName=typeName;
	}
	public String getTypeName(){
		return typeName;
	}
	public void setColor(String color){
	this.color=color;
	}
	public String getColor(){
		return color;
	}
}

