
package com.llyc.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tWHXTSZ complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tWHXTSZ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SJS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tWHXTSZ", propOrder = {
    "bm",
    "dm",
    "mc",
    "sjs",
    "sm"
})
public class TWHXTSZ {

    @XmlElement(name = "BM")
    protected String bm;
    @XmlElement(name = "DM")
    protected String dm;
    @XmlElement(name = "MC")
    protected String mc;
    @XmlElement(name = "SJS")
    protected String sjs;
    @XmlElement(name = "SM")
    protected String sm;

    /**
     * 获取bm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBM() {
        return bm;
    }

    /**
     * 设置bm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBM(String value) {
        this.bm = value;
    }

    /**
     * 获取dm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDM() {
        return dm;
    }

    /**
     * 设置dm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDM(String value) {
        this.dm = value;
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
     * 获取sjs属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSJS() {
        return sjs;
    }

    /**
     * 设置sjs属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSJS(String value) {
        this.sjs = value;
    }

    /**
     * 获取sm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSM() {
        return sm;
    }

    /**
     * 设置sm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSM(String value) {
        this.sm = value;
    }

}
