
package com.llyc.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tJZJBXX complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取bjjg属性的值。
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
     * 设置bjjg属性的值。
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
     * 获取decayrate属性的值。
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
     * 设置decayrate属性的值。
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
     * 获取enddt属性的值。
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
     * 设置enddt属性的值。
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
     * 获取geox属性的值。
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
     * 设置geox属性的值。
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
     * 获取geoy属性的值。
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
     * 设置geoy属性的值。
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
     * 获取id属性的值。
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
     * 设置id属性的值。
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
     * 获取ip属性的值。
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
     * 设置ip属性的值。
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
     * 获取layerid属性的值。
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
     * 设置layerid属性的值。
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
     * 获取mc属性的值。
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
     * 设置mc属性的值。
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
     * 获取onemetrerssi属性的值。
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
     * 设置onemetrerssi属性的值。
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
     * 获取port属性的值。
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
     * 设置port属性的值。
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
     * 获取repairrssi属性的值。
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
     * 设置repairrssi属性的值。
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
     * 获取startdt属性的值。
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
     * 设置startdt属性的值。
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
     * 获取stationid属性的值。
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
     * 设置stationid属性的值。
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
     * 获取type属性的值。
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
     * 设置type属性的值。
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
     * 获取xgjz属性的值。
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
     * 设置xgjz属性的值。
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
     * 获取xzjl属性的值。
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
     * 设置xzjl属性的值。
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
     * 获取yxjl属性的值。
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
     * 设置yxjl属性的值。
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
