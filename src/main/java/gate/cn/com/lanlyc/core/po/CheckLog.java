package gate.cn.com.lanlyc.core.po;

import java.util.Date;

import cn.com.lanlyc.base.persistence.PrimaryKeyMapper;
import cn.com.lanlyc.base.persistence.TableMapper;

import java.io.Serializable;
   /**
    * check_log 实体类
    * Wed Aug 16 15:31:02 CST 2017 黎洋富
    */ 


@TableMapper("gate_t_check_log")

public class CheckLog  implements Serializable{
	/**
     * 唯一ID
     */
    @PrimaryKeyMapper
	private String id;
	private String id_card;
	private String card_no;
	private String gate_no;
	private String check_date;
	private String cross_flag;
	private String check_type;
	private String is_shangbao;
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
	public void setCheck_date(String check_date){
	this.check_date=check_date;
	}
	public String getCheck_date(){
		return check_date;
	}
	public void setCross_flag(String cross_flag){
	this.cross_flag=cross_flag;
	}
	public String getCross_flag(){
		return cross_flag;
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
}

