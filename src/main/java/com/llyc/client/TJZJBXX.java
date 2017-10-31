
package com.llyc.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tJZJBXX complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tJZJBXX">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BJJG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DECAY_RATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENDDT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GEO_X" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GEO_Y" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LAYER_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ONE_METRE_RSSI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="REPAIRRSSI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STARTDT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STATION_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XGJZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XZJL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="YXJL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tJZJBXX", propOrder = {
    "bjjg",
    "decayrate",
    "enddt",
    "geox",
    "geoy",
    "id",
    "ip",
    "layerid",
    "mc",
    "onemetrerssi",
    "port",
    "repairrssi",
    "startdt",
    "stationid",
    "type",
    "xgjz",
    "xzjl",
    "yxjl"
})
public class TJZJBXX {

    @XmlElement(name = "BJJG")
    protected String bjjg;
    @XmlElement(name = "DECAY_RATE")
    protected String decayrate;
    @XmlElement(name = "ENDDT")
    protected String enddt;
    @XmlElement(name = "GEO_X")
    protected String geox;
    @XmlElement(name = "GEO_Y")
    protected String geoy;
    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "IP")
    protected String ip;
    @XmlElement(name = "LAYER_ID")
    protected String layerid;
    @XmlElement(name = "MC")
    protected String mc;
    @XmlElement(name = "ONE_METRE_RSSI")
    protected String onemetrerssi;
    @XmlElement(name = "PORT")
    protected String port;
    @XmlElement(name = "REPAIRRSSI")
    protected String repairrssi;
    @XmlElement(name = "STARTDT")
    protected String startdt;
    @XmlElement(name = "STATION_ID")
    protected String stationid;
    @XmlElement(name = "TYPE")
    protected String type;
    @XmlElement(name = "XGJZ")
    protected String xgjz;
    @XmlElement(name = "XZJL")
    protected String xzjl;
    @XmlElement(name = "YXJL")
    protected String yxjl;

    /**
     * ��ȡbjjg���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBJJG() {
        return bjjg;
    }

    /**
     * ����bjjg���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBJJG(String value) {
        this.bjjg = value;
    }

    /**
     * ��ȡdecayrate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDECAYRATE() {
        return decayrate;
    }

    /**
     * ����decayrate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDECAYRATE(String value) {
        this.decayrate = value;
    }

    /**
     * ��ȡenddt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDDT() {
        return enddt;
    }

    /**
     * ����enddt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDDT(String value) {
        this.enddt = value;
    }

    /**
     * ��ȡgeox���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGEOX() {
        return geox;
    }

    /**
     * ����geox���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGEOX(String value) {
        this.geox = value;
    }

    /**
     * ��ȡgeoy���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGEOY() {
        return geoy;
    }

    /**
     * ����geoy���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGEOY(String value) {
        this.geoy = value;
    }

    /**
     * ��ȡid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * ����id���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * ��ȡip���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIP() {
        return ip;
    }

    /**
     * ����ip���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIP(String value) {
        this.ip = value;
    }

    /**
     * ��ȡlayerid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLAYERID() {
        return layerid;
    }

    /**
     * ����layerid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLAYERID(String value) {
        this.layerid = value;
    }

    /**
     * ��ȡmc���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMC() {
        return mc;
    }

    /**
     * ����mc���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMC(String value) {
        this.mc = value;
    }

    /**
     * ��ȡonemetrerssi���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getONEMETRERSSI() {
        return onemetrerssi;
    }

    /**
     * ����onemetrerssi���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setONEMETRERSSI(String value) {
        this.onemetrerssi = value;
    }

    /**
     * ��ȡport���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPORT() {
        return port;
    }

    /**
     * ����port���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPORT(String value) {
        this.port = value;
    }

    /**
     * ��ȡrepairrssi���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREPAIRRSSI() {
        return repairrssi;
    }

    /**
     * ����repairrssi���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREPAIRRSSI(String value) {
        this.repairrssi = value;
    }

    /**
     * ��ȡstartdt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTARTDT() {
        return startdt;
    }

    /**
     * ����startdt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTARTDT(String value) {
        this.startdt = value;
    }

    /**
     * ��ȡstationid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTATIONID() {
        return stationid;
    }

    /**
     * ����stationid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTATIONID(String value) {
        this.stationid = value;
    }

    /**
     * ��ȡtype���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTYPE() {
        return type;
    }

    /**
     * ����type���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTYPE(String value) {
        this.type = value;
    }

    /**
     * ��ȡxgjz���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXGJZ() {
        return xgjz;
    }

    /**
     * ����xgjz���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXGJZ(String value) {
        this.xgjz = value;
    }

    /**
     * ��ȡxzjl���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXZJL() {
        return xzjl;
    }

    /**
     * ����xzjl���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXZJL(String value) {
        this.xzjl = value;
    }

    /**
     * ��ȡyxjl���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYXJL() {
        return yxjl;
    }

    /**
     * ����yxjl���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYXJL(String value) {
        this.yxjl = value;
    }

}
