
package com.llyc.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tWHSJJSSZ complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tWHSJJSSZ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SBDETAIL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SBID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SBKEY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SBVALUE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tWHSJJSSZ", propOrder = {
    "id",
    "sbdetail",
    "sbid",
    "sbkey",
    "sbvalue"
})
public class TWHSJJSSZ {

    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "SBDETAIL")
    protected String sbdetail;
    @XmlElement(name = "SBID")
    protected String sbid;
    @XmlElement(name = "SBKEY")
    protected String sbkey;
    @XmlElement(name = "SBVALUE")
    protected String sbvalue;

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
     * 获取sbdetail属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSBDETAIL() {
        return sbdetail;
    }

    /**
     * 设置sbdetail属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSBDETAIL(String value) {
        this.sbdetail = value;
    }

    /**
     * 获取sbid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSBID() {
        return sbid;
    }

    /**
     * 设置sbid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSBID(String value) {
        this.sbid = value;
    }

    /**
     * 获取sbkey属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSBKEY() {
        return sbkey;
    }

    /**
     * 设置sbkey属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSBKEY(String value) {
        this.sbkey = value;
    }

    /**
     * 获取sbvalue属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSBVALUE() {
        return sbvalue;
    }

    /**
     * 设置sbvalue属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSBVALUE(String value) {
        this.sbvalue = value;
    }

}
