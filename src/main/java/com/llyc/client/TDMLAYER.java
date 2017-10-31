
package com.llyc.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tDMLAYER complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡbjtx���Ե�ֵ��
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
     * ����bjtx���Ե�ֵ��
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
     * ��ȡdatasname���Ե�ֵ��
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
     * ����datasname���Ե�ֵ��
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
     * ��ȡdistance���Ե�ֵ��
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
     * ����distance���Ե�ֵ��
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
     * ��ȡdm���Ե�ֵ��
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
     * ����dm���Ե�ֵ��
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
     * ��ȡdsid���Ե�ֵ��
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
     * ����dsid���Ե�ֵ��
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
     * ��ȡdtend���Ե�ֵ��
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
     * ����dtend���Ե�ֵ��
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
     * ��ȡdtstart���Ե�ֵ��
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
     * ����dtstart���Ե�ֵ��
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
     * ��ȡhly���Ե�ֵ��
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
     * ����hly���Ե�ֵ��
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
     * ��ȡisssgz���Ե�ֵ��
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
     * ����isssgz���Ե�ֵ��
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
     * ��ȡjzcolor���Ե�ֵ��
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
     * ����jzcolor���Ե�ֵ��
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
     * ��ȡjzid���Ե�ֵ��
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
     * ����jzid���Ե�ֵ��
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
     * ��ȡlayeri���Ե�ֵ��
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
     * ����layeri���Ե�ֵ��
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
     * ��ȡlayername���Ե�ֵ��
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
     * ����layername���Ե�ֵ��
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
     * ��ȡmaxx���Ե�ֵ��
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
     * ����maxx���Ե�ֵ��
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
     * ��ȡmaxy���Ե�ֵ��
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
     * ����maxy���Ե�ֵ��
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
     * ��ȡminx���Ե�ֵ��
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
     * ����minx���Ե�ֵ��
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
     * ��ȡminy���Ե�ֵ��
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
     * ����miny���Ե�ֵ��
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
     * ��ȡnoposcount���Ե�ֵ��
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
     * ����noposcount���Ե�ֵ��
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
     * ��ȡnowmap���Ե�ֵ��
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
     * ����nowmap���Ե�ֵ��
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
     * ��ȡnowtimejz���Ե�ֵ��
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
     * ����nowtimejz���Ե�ֵ��
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
     * ��ȡnousemapserver���Ե�ֵ��
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
     * ����nousemapserver���Ե�ֵ��
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
     * ��ȡoldbjtx���Ե�ֵ��
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
     * ����oldbjtx���Ե�ֵ��
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
     * ��ȡqyid���Ե�ֵ��
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
     * ����qyid���Ե�ֵ��
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
     * ��ȡqymc���Ե�ֵ��
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
     * ����qymc���Ե�ֵ��
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
     * ��ȡrygz���Ե�ֵ��
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
     * ����rygz���Ե�ֵ��
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
     * ��ȡryinfo���Ե�ֵ��
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
     * ����ryinfo���Ե�ֵ��
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
     * ��ȡrymc���Ե�ֵ��
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
     * ����rymc���Ե�ֵ��
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
     * ��ȡscale���Ե�ֵ��
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
     * ����scale���Ե�ֵ��
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
     * ��ȡsn���Ե�ֵ��
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
     * ����sn���Ե�ֵ��
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
     * ��ȡspacename���Ե�ֵ��
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
     * ����spacename���Ե�ֵ��
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
     * ��ȡurl���Ե�ֵ��
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
     * ����url���Ե�ֵ��
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
     * ��ȡxh���Ե�ֵ��
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
     * ����xh���Ե�ֵ��
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
     * ��ȡzp���Ե�ֵ��
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
     * ����zp���Ե�ֵ��
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
