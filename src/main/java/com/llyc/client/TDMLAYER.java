
package com.llyc.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tDMLAYER complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tDMLAYER">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BJTX" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATAS_NAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DISTANCE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DSID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSTART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GEO_X" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GEO_Y" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HLY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ISSSGZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="JZCOLOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="JZID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LAYERID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LAYER_I" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LAYER_NAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAX_X" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAX_Y" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MIN_X" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MIN_Y" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOPOSCOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOWMAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOWTIME_JZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NO_USE_MAPSERVER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OLDBJTX" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QYID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QYMC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RYGZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RYINFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RYMC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SPACE_NAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ZP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDMLAYER", propOrder = {
    "bjtx",
    "datasname",
    "distance",
    "dm",
    "dsid",
    "dtend",
    "dtstart",
    "geox",
    "geoy",
    "hly",
    "id",
    "isssgz",
    "jzcolor",
    "jzid",
    "layerid",
    "layeri",
    "layername",
    "maxx",
    "maxy",
    "minx",
    "miny",
    "noposcount",
    "nowmap",
    "nowtimejz",
    "nousemapserver",
    "oldbjtx",
    "qyid",
    "qymc",
    "rygz",
    "ryinfo",
    "rymc",
    "scale",
    "sn",
    "spacename",
    "type",
    "url",
    "xh",
    "zp"
})
public class TDMLAYER {

    @XmlElement(name = "BJTX")
    protected String bjtx;
    @XmlElement(name = "DATAS_NAME")
    protected String datasname;
    @XmlElement(name = "DISTANCE")
    protected String distance;
    @XmlElement(name = "DM")
    protected String dm;
    @XmlElement(name = "DSID")
    protected String dsid;
    @XmlElement(name = "DTEND")
    protected String dtend;
    @XmlElement(name = "DTSTART")
    protected String dtstart;
    @XmlElement(name = "GEO_X")
    protected String geox;
    @XmlElement(name = "GEO_Y")
    protected String geoy;
    @XmlElement(name = "HLY")
    protected String hly;
    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "ISSSGZ")
    protected String isssgz;
    @XmlElement(name = "JZCOLOR")
    protected String jzcolor;
    @XmlElement(name = "JZID")
    protected String jzid;
    @XmlElement(name = "LAYERID")
    protected String layerid;
    @XmlElement(name = "LAYER_I")
    protected String layeri;
    @XmlElement(name = "LAYER_NAME")
    protected String layername;
    @XmlElement(name = "MAX_X")
    protected String maxx;
    @XmlElement(name = "MAX_Y")
    protected String maxy;
    @XmlElement(name = "MIN_X")
    protected String minx;
    @XmlElement(name = "MIN_Y")
    protected String miny;
    @XmlElement(name = "NOPOSCOUNT")
    protected String noposcount;
    @XmlElement(name = "NOWMAP")
    protected String nowmap;
    @XmlElement(name = "NOWTIME_JZ")
    protected String nowtimejz;
    @XmlElement(name = "NO_USE_MAPSERVER")
    protected String nousemapserver;
    @XmlElement(name = "OLDBJTX")
    protected String oldbjtx;
    @XmlElement(name = "QYID")
    protected String qyid;
    @XmlElement(name = "QYMC")
    protected String qymc;
    @XmlElement(name = "RYGZ")
    protected String rygz;
    @XmlElement(name = "RYINFO")
    protected String ryinfo;
    @XmlElement(name = "RYMC")
    protected String rymc;
    @XmlElement(name = "SCALE")
    protected String scale;
    @XmlElement(name = "SN")
    protected String sn;
    @XmlElement(name = "SPACE_NAME")
    protected String spacename;
    @XmlElement(name = "TYPE")
    protected String type;
    @XmlElement(name = "URL")
    protected String url;
    @XmlElement(name = "XH")
    protected String xh;
    @XmlElement(name = "ZP")
    protected String zp;

    /**
     * 获取bjtx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBJTX() {
        return bjtx;
    }

    /**
     * 设置bjtx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBJTX(String value) {
        this.bjtx = value;
    }

    /**
     * 获取datasname属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATASNAME() {
        return datasname;
    }

    /**
     * 设置datasname属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATASNAME(String value) {
        this.datasname = value;
    }

    /**
     * 获取distance属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDISTANCE() {
        return distance;
    }

    /**
     * 设置distance属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDISTANCE(String value) {
        this.distance = value;
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
     * 获取dsid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDSID() {
        return dsid;
    }

    /**
     * 设置dsid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDSID(String value) {
        this.dsid = value;
    }

    /**
     * 获取dtend属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEND() {
        return dtend;
    }

    /**
     * 设置dtend属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEND(String value) {
        this.dtend = value;
    }

    /**
     * 获取dtstart属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSTART() {
        return dtstart;
    }

    /**
     * 设置dtstart属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSTART(String value) {
        this.dtstart = value;
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
     * 获取hly属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHLY() {
        return hly;
    }

    /**
     * 设置hly属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHLY(String value) {
        this.hly = value;
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
     * 获取isssgz属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISSSGZ() {
        return isssgz;
    }

    /**
     * 设置isssgz属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISSSGZ(String value) {
        this.isssgz = value;
    }

    /**
     * 获取jzcolor属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJZCOLOR() {
        return jzcolor;
    }

    /**
     * 设置jzcolor属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJZCOLOR(String value) {
        this.jzcolor = value;
    }

    /**
     * 获取jzid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJZID() {
        return jzid;
    }

    /**
     * 设置jzid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJZID(String value) {
        this.jzid = value;
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
     * 获取layeri属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLAYERI() {
        return layeri;
    }

    /**
     * 设置layeri属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLAYERI(String value) {
        this.layeri = value;
    }

    /**
     * 获取layername属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLAYERNAME() {
        return layername;
    }

    /**
     * 设置layername属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLAYERNAME(String value) {
        this.layername = value;
    }

    /**
     * 获取maxx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAXX() {
        return maxx;
    }

    /**
     * 设置maxx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAXX(String value) {
        this.maxx = value;
    }

    /**
     * 获取maxy属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAXY() {
        return maxy;
    }

    /**
     * 设置maxy属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAXY(String value) {
        this.maxy = value;
    }

    /**
     * 获取minx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMINX() {
        return minx;
    }

    /**
     * 设置minx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMINX(String value) {
        this.minx = value;
    }

    /**
     * 获取miny属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMINY() {
        return miny;
    }

    /**
     * 设置miny属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMINY(String value) {
        this.miny = value;
    }

    /**
     * 获取noposcount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOPOSCOUNT() {
        return noposcount;
    }

    /**
     * 设置noposcount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOPOSCOUNT(String value) {
        this.noposcount = value;
    }

    /**
     * 获取nowmap属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOWMAP() {
        return nowmap;
    }

    /**
     * 设置nowmap属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOWMAP(String value) {
        this.nowmap = value;
    }

    /**
     * 获取nowtimejz属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOWTIMEJZ() {
        return nowtimejz;
    }

    /**
     * 设置nowtimejz属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOWTIMEJZ(String value) {
        this.nowtimejz = value;
    }

    /**
     * 获取nousemapserver属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOUSEMAPSERVER() {
        return nousemapserver;
    }

    /**
     * 设置nousemapserver属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOUSEMAPSERVER(String value) {
        this.nousemapserver = value;
    }

    /**
     * 获取oldbjtx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOLDBJTX() {
        return oldbjtx;
    }

    /**
     * 设置oldbjtx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOLDBJTX(String value) {
        this.oldbjtx = value;
    }

    /**
     * 获取qyid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQYID() {
        return qyid;
    }

    /**
     * 设置qyid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQYID(String value) {
        this.qyid = value;
    }

    /**
     * 获取qymc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQYMC() {
        return qymc;
    }

    /**
     * 设置qymc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQYMC(String value) {
        this.qymc = value;
    }

    /**
     * 获取rygz属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRYGZ() {
        return rygz;
    }

    /**
     * 设置rygz属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRYGZ(String value) {
        this.rygz = value;
    }

    /**
     * 获取ryinfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRYINFO() {
        return ryinfo;
    }

    /**
     * 设置ryinfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRYINFO(String value) {
        this.ryinfo = value;
    }

    /**
     * 获取rymc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRYMC() {
        return rymc;
    }

    /**
     * 设置rymc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRYMC(String value) {
        this.rymc = value;
    }

    /**
     * 获取scale属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCALE() {
        return scale;
    }

    /**
     * 设置scale属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCALE(String value) {
        this.scale = value;
    }

    /**
     * 获取sn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSN() {
        return sn;
    }

    /**
     * 设置sn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSN(String value) {
        this.sn = value;
    }

    /**
     * 获取spacename属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPACENAME() {
        return spacename;
    }

    /**
     * 设置spacename属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPACENAME(String value) {
        this.spacename = value;
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
     * 获取url属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURL() {
        return url;
    }

    /**
     * 设置url属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURL(String value) {
        this.url = value;
    }

    /**
     * 获取xh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXH() {
        return xh;
    }

    /**
     * 设置xh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXH(String value) {
        this.xh = value;
    }

    /**
     * 获取zp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZP() {
        return zp;
    }

    /**
     * 设置zp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZP(String value) {
        this.zp = value;
    }

}
