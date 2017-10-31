/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HCNetSDK.java
 *
 * Created on 2009-9-14, 19:31:34
 */

/**
 *
 * @author Xubinfeng
 */

package video.cn.com.lanlyc.core.sdk;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.examples.win32.GDI32.RECT;
import com.sun.jna.examples.win32.W32API;
import com.sun.jna.examples.win32.W32API.HWND;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.ShortByReference;

//SDK�ӿ�˵��,HCNetSDK.dll
public interface HCNetSDK extends StdCallLibrary {

    HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary("HCNetSDK",HCNetSDK.class);
    /***�궨��***/

    public static final int MAX_NAMELEN = 16;	//DVR���ص�½��
    public static final int MAX_RIGHT = 32;	//�豸֧�ֵ�Ȩ�ޣ�1-12��ʾ����Ȩ�ޣ�13-32��ʾԶ��Ȩ�ޣ�
    public static final int NAME_LEN = 32;    //�û����
    public static final int PASSWD_LEN = 16;    //���볤��
    public static final int SERIALNO_LEN = 48;   //���кų���
    public static final int MACADDR_LEN = 6;      //mac��ַ����
    public static final int MAX_ETHERNET = 2;   //�豸������̫����
    public static final int PATHNAME_LEN = 128;   //·������
    public static final int MAX_TIMESEGMENT_V30 = 8;    //9000�豸���ʱ�����
    public static final int MAX_TIMESEGMENT = 4;   //8000�豸���ʱ�����
    public static final int MAX_SHELTERNUM = 4;   //8000�豸����ڵ�������
    public static final int MAX_DAYS = 7;      //ÿ������
    public static final int PHONENUMBER_LEN = 32;   //pppoe���ź�����󳤶�
    public static final int MAX_DISKNUM_V30 = 33;		//9000�豸���Ӳ����/* ���33��Ӳ��(����16������SATAӲ�̡�1��eSATAӲ�̺�16��NFS��) */
    public static final int MAX_DISKNUM = 16;     //8000�豸���Ӳ����
    public static final int MAX_DISKNUM_V10 = 8;   //1.2�汾֮ǰ�汾
    public static final int MAX_WINDOW_V30 = 32; //9000�豸������ʾ��󲥷Ŵ�����
    public static final int MAX_WINDOW = 16;    //8000�豸���Ӳ����
    public static final int MAX_VGA_V30 = 4;     //9000�豸���ɽ�VGA��
    public static final int MAX_VGA = 1;    //8000�豸���ɽ�VGA��
    public static final int MAX_USERNUM_V30 = 32;  //9000�豸����û���
    public static final int MAX_USERNUM = 16;  //8000�豸����û���
    public static final int MAX_EXCEPTIONNUM_V30 = 32;  //9000�豸����쳣������
    public static final int MAX_EXCEPTIONNUM = 16;   //8000�豸����쳣������
    public static final int MAX_LINK = 6;    //8000�豸��ͨ�������Ƶ��������
    public static final int MAX_DECPOOLNUM = 4;   //��·������ÿ������ͨ������ѭ��������
    public static final int MAX_DECNUM = 4;    //��·��������������ͨ����ʵ��ֻ��һ�����������������
    public static final int MAX_TRANSPARENTNUM = 2;   //��·���������������͸��ͨ����
    public static final int MAX_CYCLE_CHAN = 16;   //��·�����������ѭͨ����
    public static final int MAX_DIRNAME_LENGTH = 80;   //���Ŀ¼����
    public static final int MAX_STRINGNUM_V30 = 8;		//9000�豸���OSD�ַ�������
    public static final int MAX_STRINGNUM = 4;   //8000�豸���OSD�ַ�������
    public static final int MAX_STRINGNUM_EX = 8;   //8000������չ
    public static final int MAX_AUXOUT_V30 = 16;   //9000�豸����������
    public static final int MAX_AUXOUT = 4;      //8000�豸����������
    public static final int MAX_HD_GROUP = 16;   //9000�豸���Ӳ������
    public static final int MAX_NFS_DISK = 8;    //8000�豸���NFSӲ����
    public static final int IW_ESSID_MAX_SIZE = 32;    //WIFI��SSID�ų���
    public static final int IW_ENCODING_TOKEN_MAX = 32;   //WIFI��������ֽ���
    public static final int MAX_SERIAL_NUM = 64;    //���֧�ֵ�͸��ͨ��·��
    public static final int MAX_DDNS_NUMS = 10;   //9000�豸������ddns��
    public static final int MAX_DOMAIN_NAME = 64;	/* �������� */

    public static final int MAX_EMAIL_ADDR_LEN = 48;  //���email��ַ����
    public static final int MAX_EMAIL_PWD_LEN = 32;     //���email���볤��
    public static final int MAXPROGRESS = 100;  //�ط�ʱ�����ٷ���
    public static final int MAX_SERIALNUM = 2;    //8000�豸֧�ֵĴ����� 1-232�� 2-485
    public static final int CARDNUM_LEN = 20;    //���ų���
    public static final int MAX_VIDEOOUT_V30 = 4;      //9000�豸����Ƶ�����
    public static final int MAX_VIDEOOUT = 2;      //8000�豸����Ƶ�����
    public static final int MAX_PRESET_V30 = 256;	/* 9000�豸֧�ֵ���̨Ԥ�õ��� */
    public static final int MAX_TRACK_V30 = 256;	/* 9000�豸֧�ֵ���̨�켣�� */
    public static final int MAX_CRUISE_V30 = 256;	/* 9000�豸֧�ֵ���̨Ѳ���� */
    public static final int MAX_PRESET = 128;	/* 8000�豸֧�ֵ���̨Ԥ�õ��� */
    public static final int MAX_TRACK = 128;	/* 8000�豸֧�ֵ���̨�켣�� */
    public static final int MAX_CRUISE = 128;	/* 8000�豸֧�ֵ���̨Ѳ���� */
    public static final int CRUISE_MAX_PRESET_NUMS = 32;    /* һ��Ѳ������Ѳ���� */
    public static final int MAX_SERIAL_PORT = 8;    //9000�豸֧��232������
    public static final int MAX_PREVIEW_MODE = 8;    /* �豸֧�����Ԥ��ģʽ��Ŀ 1����,4����,9����,16����.... */
    public static final int MAX_MATRIXOUT = 16;  /* ���ģ������������ */
    public static final int LOG_INFO_LEN = 11840; /* ��־������Ϣ */
    public static final int DESC_LEN = 16;    /* ��̨�����ַ��� */
    public static final int PTZ_PROTOCOL_NUM = 200;   /* 9000���֧�ֵ���̨Э���� */
    public static final int MAX_AUDIO = 1;    //8000�����Խ�ͨ����
    public static final int MAX_AUDIO_V30 = 2;   //9000�����Խ�ͨ����
    public static final int MAX_CHANNUM = 16;   //8000�豸���ͨ����
    public static final int MAX_ALARMIN = 16;  //8000�豸��󱨾�������
    public static final int MAX_ALARMOUT = 4;    //8000�豸��󱨾������
//9000 IPC����
    public static final int MAX_ANALOG_CHANNUM = 32;    //���32��ģ��ͨ��
    public static final int MAX_ANALOG_ALARMOUT = 32;    //���32·ģ�ⱨ�����
    public static final int MAX_ANALOG_ALARMIN = 32;    //���32·ģ�ⱨ������
    public static final int MAX_IP_DEVICE = 32;    //�����������IP�豸��
    public static final int MAX_IP_CHANNEL = 32;   //�����������IPͨ����
    public static final int MAX_IP_ALARMIN = 128;   //����������౨��������
    public static final int MAX_IP_ALARMOUT = 64; //����������౨�������

    /* ���֧�ֵ�ͨ���� ���ģ��������IP֧�� */
    public static final int MAX_CHANNUM_V30 = (MAX_ANALOG_CHANNUM + MAX_IP_CHANNEL);//64
    public static final int MAX_ALARMOUT_V30 = (MAX_ANALOG_ALARMOUT + MAX_IP_ALARMOUT);//96
    public static final int MAX_ALARMIN_V30 = (MAX_ANALOG_ALARMIN + MAX_IP_ALARMIN);//160

    /*******************ȫ�ִ����� begin**********************/
    public static final int NET_DVR_NOERROR = 0;	//û�д���
    public static final int NET_DVR_PASSWORD_ERROR = 1;	//�û����������
    public static final int NET_DVR_NOENOUGHPRI = 2;//Ȩ�޲���
    public static final int NET_DVR_NOINIT = 3;//û�г�ʼ��
    public static final int NET_DVR_CHANNEL_ERROR = 4;	//ͨ���Ŵ���
    public static final int NET_DVR_OVER_MAXLINK = 5;	//���ӵ�DVR�Ŀͻ��˸�������
    public static final int NET_DVR_VERSIONNOMATCH = 6;	//�汾��ƥ��
    public static final int NET_DVR_NETWORK_FAIL_CONNECT = 7;//���ӷ�����ʧ��
    public static final int NET_DVR_NETWORK_SEND_ERROR = 8;	//�����������ʧ��
    public static final int NET_DVR_NETWORK_RECV_ERROR = 9;	//�ӷ������������ʧ��
    public static final int NET_DVR_NETWORK_RECV_TIMEOUT = 10;	//�ӷ�����������ݳ�ʱ
    public static final int NET_DVR_NETWORK_ERRORDATA = 11;	//���͵��������
    public static final int NET_DVR_ORDER_ERROR = 12;	//���ô������
    public static final int NET_DVR_OPERNOPERMIT = 13;	//�޴�Ȩ��
    public static final int NET_DVR_COMMANDTIMEOUT = 14;	//DVR����ִ�г�ʱ
    public static final int NET_DVR_ERRORSERIALPORT = 15;	//���ںŴ���
    public static final int NET_DVR_ERRORALARMPORT = 16;	//�����˿ڴ���
    public static final int NET_DVR_PARAMETER_ERROR = 17;//�������
    public static final int NET_DVR_CHAN_EXCEPTION = 18;	//������ͨ�����ڴ���״̬
    public static final int NET_DVR_NODISK = 19;	//û��Ӳ��
    public static final int NET_DVR_ERRORDISKNUM = 20;	//Ӳ�̺Ŵ���
    public static final int NET_DVR_DISK_FULL = 21;	//������Ӳ����
    public static final int NET_DVR_DISK_ERROR = 22;//������Ӳ�̳���
    public static final int NET_DVR_NOSUPPORT = 23;//��������֧��
    public static final int NET_DVR_BUSY = 24;//������æ
    public static final int NET_DVR_MODIFY_FAIL = 25;//�������޸Ĳ��ɹ�
    public static final int NET_DVR_PASSWORD_FORMAT_ERROR = 26;//���������ʽ����ȷ
    public static final int NET_DVR_DISK_FORMATING = 27;	//Ӳ�����ڸ�ʽ����������������
    public static final int NET_DVR_DVRNORESOURCE = 28;	//DVR��Դ����
    public static final int NET_DVR_DVROPRATEFAILED = 29; //DVR����ʧ��
    public static final int NET_DVR_OPENHOSTSOUND_FAIL = 30; //��PC����ʧ��
    public static final int NET_DVR_DVRVOICEOPENED = 31; //�����������Խ���ռ��
    public static final int NET_DVR_TIMEINPUTERROR = 32; //ʱ�����벻��ȷ
    public static final int NET_DVR_NOSPECFILE = 33;  //�ط�ʱ������û��ָ�����ļ�
    public static final int NET_DVR_CREATEFILE_ERROR = 34;	//�����ļ�����
    public static final int NET_DVR_FILEOPENFAIL = 35; //���ļ�����
    public static final int NET_DVR_OPERNOTFINISH = 36; //�ϴεĲ�����û�����
    public static final int NET_DVR_GETPLAYTIMEFAIL = 37; //��ȡ��ǰ���ŵ�ʱ�����
    public static final int NET_DVR_PLAYFAIL = 38; //���ų���
    public static final int NET_DVR_FILEFORMAT_ERROR = 39;//�ļ���ʽ����ȷ
    public static final int NET_DVR_DIR_ERROR = 40;	//·������
    public static final int NET_DVR_ALLOC_RESOURCE_ERROR = 41;//��Դ�������
    public static final int NET_DVR_AUDIO_MODE_ERROR = 42;	//��ģʽ����
    public static final int NET_DVR_NOENOUGH_BUF = 43;	//������̫С
    public static final int NET_DVR_CREATESOCKET_ERROR = 44;	//����SOCKET����
    public static final int NET_DVR_SETSOCKET_ERROR = 45;	//����SOCKET����
    public static final int NET_DVR_MAX_NUM = 46;	//����ﵽ���
    public static final int NET_DVR_USERNOTEXIST = 47;	//�û�������
    public static final int NET_DVR_WRITEFLASHERROR = 48;//дFLASH����
    public static final int NET_DVR_UPGRADEFAIL = 49;//DVR��ʧ��
    public static final int NET_DVR_CARDHAVEINIT = 50; //���뿨�Ѿ���ʼ����
    public static final int NET_DVR_PLAYERFAILED = 51;	//���ò��ſ���ĳ������ʧ��
    public static final int NET_DVR_MAX_USERNUM = 52; //�豸���û���ﵽ���
    public static final int NET_DVR_GETLOCALIPANDMACFAIL = 53;//��ÿͻ��˵�IP��ַ�������ַʧ��
    public static final int NET_DVR_NOENCODEING = 54;	//��ͨ��û�б���
    public static final int NET_DVR_IPMISMATCH = 55;	//IP��ַ��ƥ��
    public static final int NET_DVR_MACMISMATCH = 56;//MAC��ַ��ƥ��
    public static final int NET_DVR_UPGRADELANGMISMATCH = 57;//���ļ����Բ�ƥ��
    public static final int NET_DVR_MAX_PLAYERPORT = 58;//������·��ﵽ���
    public static final int NET_DVR_NOSPACEBACKUP = 59;//�����豸��û���㹻�ռ���б���
    public static final int NET_DVR_NODEVICEBACKUP = 60;	//û���ҵ�ָ���ı����豸
    public static final int NET_DVR_PICTURE_BITS_ERROR = 61;	//ͼ����λ�����24ɫ
    public static final int NET_DVR_PICTURE_DIMENSION_ERROR = 62;//ͼƬ��*�?�ޣ� ��128*256
    public static final int NET_DVR_PICTURE_SIZ_ERROR = 63;	//ͼƬ��С���ޣ���100K
    public static final int NET_DVR_LOADPLAYERSDKFAILED = 64;	//���뵱ǰĿ¼��Player Sdk����
    public static final int NET_DVR_LOADPLAYERSDKPROC_ERROR = 65;	//�Ҳ���Player Sdk��ĳ���������
    public static final int NET_DVR_LOADDSSDKFAILED = 66;	//���뵱ǰĿ¼��DSsdk����
    public static final int NET_DVR_LOADDSSDKPROC_ERROR = 67;	//�Ҳ���DsSdk��ĳ���������
    public static final int NET_DVR_DSSDK_ERROR = 68;	//����Ӳ�����DsSdk��ĳ������ʧ��
    public static final int NET_DVR_VOICEMONOPOLIZE = 69;	//����ռ
    public static final int NET_DVR_JOINMULTICASTFAILED = 70;	//����ಥ��ʧ��
    public static final int NET_DVR_CREATEDIR_ERROR = 71;	//������־�ļ�Ŀ¼ʧ��
    public static final int NET_DVR_BINDSOCKET_ERROR = 72;	//���׽���ʧ��
    public static final int NET_DVR_SOCKETCLOSE_ERROR = 73;	//socket�����жϣ��˴���ͨ�������������жϻ�Ŀ�ĵز��ɴ�
    public static final int NET_DVR_USERID_ISUSING = 74;	//ע��ʱ�û�ID���ڽ���ĳ����
    public static final int NET_DVR_SOCKETLISTEN_ERROR = 75;	//����ʧ��
    public static final int NET_DVR_PROGRAM_EXCEPTION = 76;	//�����쳣
    public static final int NET_DVR_WRITEFILE_FAILED = 77;	//д�ļ�ʧ��
    public static final int NET_DVR_FORMAT_READONLY = 78;//��ֹ��ʽ��ֻ��Ӳ��
    public static final int NET_DVR_WITHSAMEUSERNAME = 79;//�û����ýṹ�д�����ͬ���û���
    public static final int NET_DVR_DEVICETYPE_ERROR = 80; /*�������ʱ�豸�ͺŲ�ƥ��*/
    public static final int NET_DVR_LANGUAGE_ERROR = 81; /*�������ʱ���Բ�ƥ��*/
    public static final int NET_DVR_PARAVERSION_ERROR = 82; /*�������ʱ����汾��ƥ��*/
    public static final int NET_DVR_IPCHAN_NOTALIVE = 83; /*Ԥ��ʱ���IPͨ��������*/
    public static final int NET_DVR_RTSP_SDK_ERROR = 84;	/*���ظ���IPCͨѶ��StreamTransClient.dllʧ��*/
    public static final int NET_DVR_CONVERT_SDK_ERROR = 85;	/*����ת���ʧ��*/
    public static final int NET_DVR_IPC_COUNT_OVERFLOW = 86; /*��������ip����ͨ����*/
    public static final int NET_PLAYM4_NOERROR = 500;	//no error
    public static final int NET_PLAYM4_PARA_OVER = 501;//input parameter is invalid;
    public static final int NET_PLAYM4_ORDER_ERROR = 502;//The order of the function to be called is error.
    public static final int NET_PLAYM4_TIMER_ERROR = 503;//Create multimedia clock failed;
    public static final int NET_PLAYM4_DEC_VIDEO_ERROR = 504;//Decode video data failed.
    public static final int NET_PLAYM4_DEC_AUDIO_ERROR = 505;//Decode audio data failed.
    public static final int NET_PLAYM4_ALLOC_MEMORY_ERROR = 506;	//Allocate memory failed.
    public static final int NET_PLAYM4_OPEN_FILE_ERROR = 507;	//Open the file failed.
    public static final int NET_PLAYM4_CREATE_OBJ_ERROR = 508;//Create thread or event failed
    public static final int NET_PLAYM4_CREATE_DDRAW_ERROR = 509;//Create DirectDraw object failed.
    public static final int NET_PLAYM4_CREATE_OFFSCREEN_ERROR = 510;//failed when creating off-screen surface.
    public static final int NET_PLAYM4_BUF_OVER = 511;	//buffer is overflow
    public static final int NET_PLAYM4_CREATE_SOUND_ERROR = 512;	//failed when creating audio device.
    public static final int NET_PLAYM4_SET_VOLUME_ERROR = 513;//Set volume failed
    public static final int NET_PLAYM4_SUPPORT_FILE_ONLY = 514;//The function only support play file.
    public static final int NET_PLAYM4_SUPPORT_STREAM_ONLY = 515;//The function only support play stream.
    public static final int NET_PLAYM4_SYS_NOT_SUPPORT = 516;//System not support.
    public static final int NET_PLAYM4_FILEHEADER_UNKNOWN = 517;	//No file header.
    public static final int NET_PLAYM4_VERSION_INCORRECT = 518;	//The version of decoder and encoder is not adapted.
    public static final int NET_PALYM4_INIT_DECODER_ERROR = 519;	//Initialize decoder failed.
    public static final int NET_PLAYM4_CHECK_FILE_ERROR = 520;	//The file data is unknown.
    public static final int NET_PLAYM4_INIT_TIMER_ERROR = 521;	//Initialize multimedia clock failed.
    public static final int NET_PLAYM4_BLT_ERROR = 522;//Blt failed.
    public static final int NET_PLAYM4_UPDATE_ERROR = 523;//Update failed.
    public static final int NET_PLAYM4_OPEN_FILE_ERROR_MULTI = 524; //openfile error, streamtype is multi
    public static final int NET_PLAYM4_OPEN_FILE_ERROR_VIDEO = 525; //openfile error, streamtype is video
    public static final int NET_PLAYM4_JPEG_COMPRESS_ERROR = 526; //JPEG compress error
    public static final int NET_PLAYM4_EXTRACT_NOT_SUPPORT = 527;	//Don't support the version of this file.
    public static final int NET_PLAYM4_EXTRACT_DATA_ERROR = 528;	//extract video data failed.
    /*******************ȫ�ִ����� end**********************/
    /*************************************************
    NET_DVR_IsSupport()����ֵ
    1��9λ�ֱ��ʾ������Ϣ��λ����TRUE)��ʾ֧�֣�
     **************************************************/
    public static final int NET_DVR_SUPPORT_DDRAW = 0x01;//֧��DIRECTDRAW�����֧�֣��򲥷������ܹ�����
    public static final int NET_DVR_SUPPORT_BLT = 0x02;//�Կ�֧��BLT���������֧�֣��򲥷������ܹ�����
    public static final int NET_DVR_SUPPORT_BLTFOURCC = 0x04;//�Կ�BLT֧����ɫת�������֧�֣��������������������RGBת����
    public static final int NET_DVR_SUPPORT_BLTSHRINKX = 0x08;//�Կ�BLT֧��X����С�����֧�֣�ϵͳ�����������ת����
    public static final int NET_DVR_SUPPORT_BLTSHRINKY = 0x10;//�Կ�BLT֧��Y����С�����֧�֣�ϵͳ�����������ת����
    public static final int NET_DVR_SUPPORT_BLTSTRETCHX = 0x20;//�Կ�BLT֧��X��Ŵ����֧�֣�ϵͳ�����������ת����
    public static final int NET_DVR_SUPPORT_BLTSTRETCHY = 0x40;//�Կ�BLT֧��Y��Ŵ����֧�֣�ϵͳ�����������ת����
    public static final int NET_DVR_SUPPORT_SSE = 0x80;//CPU֧��SSEָ�Intel Pentium3����֧��SSEָ�
    public static final int NET_DVR_SUPPORT_MMX = 0x100;//CPU֧��MMXָ���Intel Pentium3����֧��SSEָ�
    /**********************��̨�������� begin*************************/
    public static final int LIGHT_PWRON = 2;	/* ��ͨ�ƹ��Դ */
    public static final int WIPER_PWRON = 3;	/* ��ͨ��ˢ���� */
    public static final int FAN_PWRON = 4;	/* ��ͨ���ȿ��� */
    public static final int HEATER_PWRON = 5;	/* ��ͨ���������� */
    public static final int AUX_PWRON1 = 6;	/* ��ͨ�����豸���� */
    public static final int AUX_PWRON2 = 7;	/* ��ͨ�����豸���� */
    public static final int SET_PRESET = 8;	/* ����Ԥ�õ� */
    public static final int CLE_PRESET = 9;	/* ���Ԥ�õ� */
    public static final int ZOOM_IN = 11;	/* �������ٶ�SS���(���ʱ��) */
    public static final int ZOOM_OUT = 12;	/* �������ٶ�SS��С(���ʱ�С) */
    public static final int FOCUS_NEAR = 13; /* �������ٶ�SSǰ�� */
    public static final int FOCUS_FAR = 14; /* �������ٶ�SS��� */
    public static final int IRIS_OPEN = 15; /* ��Ȧ���ٶ�SS���� */
    public static final int IRIS_CLOSE = 16; /* ��Ȧ���ٶ�SS��С */
    public static final int TILT_UP = 21;	/* ��̨��SS���ٶ����� */
    public static final int TILT_DOWN = 22;	/* ��̨��SS���ٶ��¸� */
    public static final int PAN_LEFT = 23;	/* ��̨��SS���ٶ���ת */
    public static final int PAN_RIGHT = 24;	/* ��̨��SS���ٶ���ת */
    public static final int UP_LEFT = 25;	/* ��̨��SS���ٶ���������ת */
    public static final int UP_RIGHT = 26;	/* ��̨��SS���ٶ���������ת */
    public static final int DOWN_LEFT = 27;	/* ��̨��SS���ٶ��¸�����ת */
    public static final int DOWN_RIGHT = 28;	/* ��̨��SS���ٶ��¸�����ת */
    public static final int PAN_AUTO = 29;	/* ��̨��SS���ٶ������Զ�ɨ�� */
    public static final int FILL_PRE_SEQ = 30;	/* ��Ԥ�õ����Ѳ������ */
    public static final int SET_SEQ_DWELL = 31;	/* ����Ѳ����ͣ��ʱ�� */
    public static final int SET_SEQ_SPEED = 32;	/* ����Ѳ���ٶ� */
    public static final int CLE_PRE_SEQ = 33;/* ��Ԥ�õ��Ѳ��������ɾ�� */
    public static final int STA_MEM_CRUISE = 34;/* ��ʼ��¼�켣 */
    public static final int STO_MEM_CRUISE = 35;/* ֹͣ��¼�켣 */
    public static final int RUN_CRUISE = 36;	/* ��ʼ�켣 */
    public static final int RUN_SEQ = 37;	/* ��ʼѲ�� */
    public static final int STOP_SEQ = 38;	/* ֹͣѲ�� */
    public static final int GOTO_PRESET = 39;	/* ����ת��Ԥ�õ� */

    /**********************��̨�������� end*************************/
    /*************************************************
    �ط�ʱ���ſ�������궨��
    NET_DVR_PlayBackControl
    NET_DVR_PlayControlLocDisplay
    NET_DVR_DecPlayBackCtrl�ĺ궨��
    ����֧�ֲ鿴����˵���ʹ���
     **************************************************/
    public static final int NET_DVR_PLAYSTART = 1;//��ʼ����
    public static final int NET_DVR_PLAYSTOP = 2;//ֹͣ����
    public static final int NET_DVR_PLAYPAUSE = 3;//��ͣ����
    public static final int NET_DVR_PLAYRESTART = 4;//�ָ�����
    public static final int NET_DVR_PLAYFAST = 5;//���
    public static final int NET_DVR_PLAYSLOW = 6;//���
    public static final int NET_DVR_PLAYNORMAL = 7;//���ٶ�
    public static final int NET_DVR_PLAYFRAME = 8;//��֡��
    public static final int NET_DVR_PLAYSTARTAUDIO = 9;//������
    public static final int NET_DVR_PLAYSTOPAUDIO = 10;//�ر�����
    public static final int NET_DVR_PLAYAUDIOVOLUME = 11;//��������
    public static final int NET_DVR_PLAYSETPOS = 12;//�ı��ļ��طŵĽ��
    public static final int NET_DVR_PLAYGETPOS = 13;//��ȡ�ļ��طŵĽ��
    public static final int NET_DVR_PLAYGETTIME = 14;//��ȡ��ǰ�Ѿ����ŵ�ʱ��(���ļ��طŵ�ʱ����Ч)
    public static final int NET_DVR_PLAYGETFRAME = 15;//��ȡ��ǰ�Ѿ����ŵ�֡��(���ļ��طŵ�ʱ����Ч)
    public static final int NET_DVR_GETTOTALFRAMES = 16;//��ȡ��ǰ�����ļ��ܵ�֡��(���ļ��طŵ�ʱ����Ч)
    public static final int NET_DVR_GETTOTALTIME = 17;//��ȡ��ǰ�����ļ��ܵ�ʱ��(���ļ��طŵ�ʱ����Ч)
    public static final int NET_DVR_THROWBFRAME = 20;//��B֡
    public static final int NET_DVR_SETSPEED = 24;//���������ٶ�
    public static final int NET_DVR_KEEPALIVE = 25;//�������豸������(���ص�������2�뷢��һ��)
//Զ�̰��������£�
/* key value send to CONFIG program */
    public static final int KEY_CODE_1 = 1;
    public static final int KEY_CODE_2 = 2;
    public static final int KEY_CODE_3 = 3;
    public static final int KEY_CODE_4 = 4;
    public static final int KEY_CODE_5 = 5;
    public static final int KEY_CODE_6 = 6;
    public static final int KEY_CODE_7 = 7;
    public static final int KEY_CODE_8 = 8;
    public static final int KEY_CODE_9 = 9;
    public static final int KEY_CODE_0 = 10;
    public static final int KEY_CODE_POWER = 11;
    public static final int KEY_CODE_MENU = 12;
    public static final int KEY_CODE_ENTER = 13;
    public static final int KEY_CODE_CANCEL = 14;
    public static final int KEY_CODE_UP = 15;
    public static final int KEY_CODE_DOWN = 16;
    public static final int KEY_CODE_LEFT = 17;
    public static final int KEY_CODE_RIGHT = 18;
    public static final int KEY_CODE_EDIT = 19;
    public static final int KEY_CODE_ADD = 20;
    public static final int KEY_CODE_MINUS = 21;
    public static final int KEY_CODE_PLAY = 22;
    public static final int KEY_CODE_REC = 23;
    public static final int KEY_CODE_PAN = 24;
    public static final int KEY_CODE_M = 25;
    public static final int KEY_CODE_A = 26;
    public static final int KEY_CODE_F1 = 27;
    public static final int KEY_CODE_F2 = 28;

    /* for PTZ control */
    public static final int KEY_PTZ_UP_START = KEY_CODE_UP;
    public static final int KEY_PTZ_UP_STO = 32;
    public static final int KEY_PTZ_DOWN_START = KEY_CODE_DOWN;
    public static final int KEY_PTZ_DOWN_STOP = 33;
    public static final int KEY_PTZ_LEFT_START = KEY_CODE_LEFT;
    public static final int KEY_PTZ_LEFT_STOP = 34;
    public static final int KEY_PTZ_RIGHT_START = KEY_CODE_RIGHT;
    public static final int KEY_PTZ_RIGHT_STOP = 35;
    public static final int KEY_PTZ_AP1_START = KEY_CODE_EDIT;/* ��Ȧ+ */
    public static final int KEY_PTZ_AP1_STOP = 36;
    public static final int KEY_PTZ_AP2_START = KEY_CODE_PAN;/* ��Ȧ- */
    public static final int KEY_PTZ_AP2_STOP = 37;
    public static final int KEY_PTZ_FOCUS1_START = KEY_CODE_A;/* �۽�+ */
    public static final int KEY_PTZ_FOCUS1_STOP = 38;
    public static final int KEY_PTZ_FOCUS2_START = KEY_CODE_M ;/* �۽�- */
    public static final int KEY_PTZ_FOCUS2_STOP = 39;
    public static final int KEY_PTZ_B1_START = 40;/* �䱶+ */
    public static final int KEY_PTZ_B1_STOP = 41;
    public static final int KEY_PTZ_B2_START = 42;/* �䱶- */
    public static final int KEY_PTZ_B2_STOP = 43;
//9000����
    public static final int KEY_CODE_11 = 44;
    public static final int KEY_CODE_12 = 45;
    public static final int KEY_CODE_13 = 46;
    public static final int KEY_CODE_14 = 47;
    public static final int KEY_CODE_15 = 48;
    public static final int KEY_CODE_16 = 49;
    /*************************������������ begin*******************************/
//����NET_DVR_SetDVRConfig��NET_DVR_GetDVRConfig,ע�����Ӧ�����ýṹ
    public static final int NET_DVR_GET_DEVICECFG = 100;	//��ȡ�豸����
    public static final int NET_DVR_SET_DEVICECFG = 101;	//�����豸����
    public static final int NET_DVR_GET_NETCFG = 102;	//��ȡ�������
    public static final int NET_DVR_SET_NETCFG = 103;	//�����������
    public static final int NET_DVR_GET_PICCFG = 104;	//��ȡͼ�����
    public static final int NET_DVR_SET_PICCFG = 105;	//����ͼ�����
    public static final int NET_DVR_GET_COMPRESSCFG = 106;	//��ȡѹ������
    public static final int NET_DVR_SET_COMPRESSCFG = 107;	//����ѹ������
    public static final int NET_DVR_GET_RECORDCFG = 108;	//��ȡ¼��ʱ�����
    public static final int NET_DVR_SET_RECORDCFG = 109;	//����¼��ʱ�����
    public static final int NET_DVR_GET_DECODERCFG = 110;	//��ȡ����������
    public static final int NET_DVR_SET_DECODERCFG = 111;	//���ý���������
    public static final int NET_DVR_GET_RS232CFG = 112;	//��ȡ232���ڲ���
    public static final int NET_DVR_SET_RS232CFG = 113;	//����232���ڲ���
    public static final int NET_DVR_GET_ALARMINCFG = 114;	//��ȡ�����������
    public static final int NET_DVR_SET_ALARMINCFG = 115;	//���ñ����������
    public static final int NET_DVR_GET_ALARMOUTCFG = 116;	//��ȡ�����������
    public static final int NET_DVR_SET_ALARMOUTCFG = 117;	//���ñ����������
    public static final int NET_DVR_GET_TIMECFG = 118;	//��ȡDVRʱ��
    public static final int NET_DVR_SET_TIMECFG = 119;		//����DVRʱ��
    public static final int NET_DVR_GET_PREVIEWCFG = 120;	//��ȡԤ������
    public static final int NET_DVR_SET_PREVIEWCFG = 121;	//����Ԥ������
    public static final int NET_DVR_GET_VIDEOOUTCFG = 122;	//��ȡ��Ƶ�������
    public static final int NET_DVR_SET_VIDEOOUTCFG = 123;	//������Ƶ�������
    public static final int NET_DVR_GET_USERCFG = 124;	//��ȡ�û�����
    public static final int NET_DVR_SET_USERCFG = 125;	//�����û�����
    public static final int NET_DVR_GET_EXCEPTIONCFG = 126;	//��ȡ�쳣����
    public static final int NET_DVR_SET_EXCEPTIONCFG = 127;	//�����쳣����
    public static final int NET_DVR_GET_ZONEANDDST = 128;	//��ȡʱ�����ʱ�Ʋ���
    public static final int NET_DVR_SET_ZONEANDDST = 129;	//����ʱ�����ʱ�Ʋ���
    public static final int NET_DVR_GET_SHOWSTRING = 130;	//��ȡ�����ַ����
    public static final int NET_DVR_SET_SHOWSTRING = 131;	//���õ����ַ����
    public static final int NET_DVR_GET_EVENTCOMPCFG = 132;	//��ȡ�¼�����¼�����
    public static final int NET_DVR_SET_EVENTCOMPCFG = 133;	//�����¼�����¼�����
    public static final int NET_DVR_GET_AUXOUTCFG = 140;	//��ȡ�������������������(HS�豸�������2006-02-28)
    public static final int NET_DVR_SET_AUXOUTCFG = 141;	//���ñ������������������(HS�豸�������2006-02-28)
    public static final int NET_DVR_GET_PREVIEWCFG_AUX = 142;	//��ȡ-sϵ��˫���Ԥ������(-sϵ��˫���2006-04-13)
    public static final int NET_DVR_SET_PREVIEWCFG_AUX = 143;	//����-sϵ��˫���Ԥ������(-sϵ��˫���2006-04-13)
    public static final int NET_DVR_GET_PICCFG_EX = 200;	//��ȡͼ�����(SDK_V14��չ����)
    public static final int NET_DVR_SET_PICCFG_EX = 201;	//����ͼ�����(SDK_V14��չ����)
    public static final int NET_DVR_GET_USERCFG_EX = 202;	//��ȡ�û�����(SDK_V15��չ����)
    public static final int NET_DVR_SET_USERCFG_EX = 203;	//�����û�����(SDK_V15��չ����)
    public static final int NET_DVR_GET_COMPRESSCFG_EX = 204;	//��ȡѹ������(SDK_V15��չ����2006-05-15)
    public static final int NET_DVR_SET_COMPRESSCFG_EX = 205;	//����ѹ������(SDK_V15��չ����2006-05-15)
    public static final int NET_DVR_GET_NETAPPCFG = 222;	//��ȡ����Ӧ�ò��� NTP/DDNS/EMAIL
    public static final int NET_DVR_SET_NETAPPCFG = 223;	//��������Ӧ�ò��� NTP/DDNS/EMAIL
    public static final int NET_DVR_GET_NTPCFG = 224;	//��ȡ����Ӧ�ò��� NTP
    public static final int NET_DVR_SET_NTPCFG = 225;	//��������Ӧ�ò��� NTP
    public static final int NET_DVR_GET_DDNSCFG = 226;	//��ȡ����Ӧ�ò��� DDNS
    public static final int NET_DVR_SET_DDNSCFG = 227;		//��������Ӧ�ò��� DDNS
//��ӦNET_DVR_EMAILPARA
    public static final int NET_DVR_GET_EMAILCFG = 228;	//��ȡ����Ӧ�ò��� EMAIL
    public static final int NET_DVR_SET_EMAILCFG = 229;	//��������Ӧ�ò��� EMAIL
    public static final int NET_DVR_GET_NFSCFG = 230;	/* NFS disk config */
    public static final int NET_DVR_SET_NFSCFG = 231;	/* NFS disk config */
    public static final int NET_DVR_GET_SHOWSTRING_EX = 238;	//��ȡ�����ַ������չ(֧��8���ַ�)
    public static final int NET_DVR_SET_SHOWSTRING_EX = 239;	//���õ����ַ������չ(֧��8���ַ�)
    public static final int NET_DVR_GET_NETCFG_OTHER = 244;	//��ȡ�������
    public static final int NET_DVR_SET_NETCFG_OTHER = 245;	//�����������
//��ӦNET_DVR_EMAILCFG�ṹ
    public static final int NET_DVR_GET_EMAILPARACFG = 250;	//Get EMAIL parameters
    public static final int NET_DVR_SET_EMAILPARACFG = 251;	//Setup EMAIL parameters
    public static final int NET_DVR_GET_DDNSCFG_EX = 274;//��ȡ��չDDNS����
    public static final int NET_DVR_SET_DDNSCFG_EX = 275;//������չDDNS����
    public static final int NET_DVR_SET_PTZPOS = 292;	//��̨����PTZλ��
    public static final int NET_DVR_GET_PTZPOS = 293;		//��̨��ȡPTZλ��
    public static final int NET_DVR_GET_PTZSCOPE = 294;//��̨��ȡPTZ��Χ
    /***************************DS9000��������(_V30) begin *****************************/
//����(NET_DVR_NETCFG_V30�ṹ)
    public static final int NET_DVR_GET_NETCFG_V30 = 1000;	//��ȡ�������
    public static final int NET_DVR_SET_NETCFG_V30 = 1001;	//�����������
//ͼ��(NET_DVR_PICCFG_V30�ṹ)
    public static final int NET_DVR_GET_PICCFG_V30 = 1002;	//��ȡͼ�����
    public static final int NET_DVR_SET_PICCFG_V30 = 1003;	//����ͼ�����
//¼��ʱ��(NET_DVR_RECORD_V30�ṹ)
    public static final int NET_DVR_GET_RECORDCFG_V30 = 1004;	//��ȡ¼�����
    public static final int NET_DVR_SET_RECORDCFG_V30 = 1005;	//����¼�����
//�û�(NET_DVR_USER_V30�ṹ)
    public static final int NET_DVR_GET_USERCFG_V30 = 1006;	//��ȡ�û�����
    public static final int NET_DVR_SET_USERCFG_V30 = 1007;	//�����û�����
//9000DDNS��������(NET_DVR_DDNSPARA_V30�ṹ)
    public static final int NET_DVR_GET_DDNSCFG_V30 = 1010;	//��ȡDDNS(9000��չ)
    public static final int NET_DVR_SET_DDNSCFG_V30 = 1011;	//����DDNS(9000��չ)
//EMAIL����(NET_DVR_EMAILCFG_V30�ṹ)
    public static final int NET_DVR_GET_EMAILCFG_V30 = 1012;//��ȡEMAIL����
    public static final int NET_DVR_SET_EMAILCFG_V30 = 1013;//����EMAIL����
//Ѳ������ (NET_DVR_CRUISE_PARA�ṹ)
    public static final int NET_DVR_GET_CRUISE = 1020;
    public static final int NET_DVR_SET_CRUISE = 1021;
//��������ṹ���� (NET_DVR_ALARMINCFG_V30�ṹ)
    public static final int NET_DVR_GET_ALARMINCFG_V30 = 1024;
    public static final int NET_DVR_SET_ALARMINCFG_V30 = 1025;
//��������ṹ���� (NET_DVR_ALARMOUTCFG_V30�ṹ)
    public static final int NET_DVR_GET_ALARMOUTCFG_V30 = 1026;
    public static final int NET_DVR_SET_ALARMOUTCFG_V30 = 1027;
//��Ƶ����ṹ���� (NET_DVR_VIDEOOUT_V30�ṹ)
    public static final int NET_DVR_GET_VIDEOOUTCFG_V30 = 1028;
    public static final int NET_DVR_SET_VIDEOOUTCFG_V30 = 1029;
//�����ַ�ṹ���� (NET_DVR_SHOWSTRING_V30�ṹ)
    public static final int NET_DVR_GET_SHOWSTRING_V30 = 1030;
    public static final int NET_DVR_SET_SHOWSTRING_V30 = 1031;
//�쳣�ṹ���� (NET_DVR_EXCEPTION_V30�ṹ)
    public static final int NET_DVR_GET_EXCEPTIONCFG_V30 = 1034;
    public static final int NET_DVR_SET_EXCEPTIONCFG_V30 = 1035;
//����232�ṹ���� (NET_DVR_RS232CFG_V30�ṹ)
    public static final int NET_DVR_GET_RS232CFG_V30 = 1036;
    public static final int NET_DVR_SET_RS232CFG_V30 = 1037;
//ѹ������ (NET_DVR_COMPRESSIONCFG_V30�ṹ)
    public static final int NET_DVR_GET_COMPRESSCFG_V30 = 1040;
    public static final int NET_DVR_SET_COMPRESSCFG_V30 = 1041;
//��ȡ485���������� (NET_DVR_DECODERCFG_V30�ṹ)
    public static final int NET_DVR_GET_DECODERCFG_V30 = 1042;	//��ȡ����������
    public static final int NET_DVR_SET_DECODERCFG_V30 = 1043;	//���ý���������
//��ȡԤ������ (NET_DVR_PREVIEWCFG_V30�ṹ)
    public static final int NET_DVR_GET_PREVIEWCFG_V30 = 1044;	//��ȡԤ������
    public static final int NET_DVR_SET_PREVIEWCFG_V30 = 1045;	//����Ԥ������
//����Ԥ������ (NET_DVR_PREVIEWCFG_AUX_V30�ṹ)
    public static final int NET_DVR_GET_PREVIEWCFG_AUX_V30 = 1046;	//��ȡ����Ԥ������
    public static final int NET_DVR_SET_PREVIEWCFG_AUX_V30 = 1047;	//���ø���Ԥ������
//IP�������ò��� ��NET_DVR_IPPARACFG�ṹ��
    public static final int NET_DVR_GET_IPPARACFG = 1048;    //��ȡIP����������Ϣ
    public static final int NET_DVR_SET_IPPARACFG = 1049;    //����IP����������Ϣ
//IP��������������ò��� ��NET_DVR_IPALARMINCFG�ṹ��
    public static final int NET_DVR_GET_IPALARMINCFG = 1050;    //��ȡIP�����������������Ϣ
    public static final int NET_DVR_SET_IPALARMINCFG = 1051;   //����IP�����������������Ϣ
//IP��������������ò��� ��NET_DVR_IPALARMOUTCFG�ṹ��
    public static final int NET_DVR_GET_IPALARMOUTCFG = 1052;   //��ȡIP�����������������Ϣ
    public static final int NET_DVR_SET_IPALARMOUTCFG = 1053;  //����IP�����������������Ϣ
//Ӳ�̹���Ĳ����ȡ (NET_DVR_HDCFG�ṹ)
    public static final int NET_DVR_GET_HDCFG = 1054;    //��ȡӲ�̹������ò���
    public static final int NET_DVR_SET_HDCFG = 1055;    //����Ӳ�̹������ò���
//�������Ĳ����ȡ (NET_DVR_HDGROUP_CFG�ṹ)
    public static final int NET_DVR_GET_HDGROUP_CFG = 1056;    //��ȡ����������ò���
    public static final int NET_DVR_SET_HDGROUP_CFG = 1057;    //��������������ò���
//�豸������������(NET_DVR_COMPRESSION_AUDIO�ṹ)
    public static final int NET_DVR_GET_COMPRESSCFG_AUD = 1058;     //��ȡ�豸�����Խ��������
    public static final int NET_DVR_SET_COMPRESSCFG_AUD = 1059;     //�����豸�����Խ��������
    /***************************DS9000��������(_V30) end *****************************/
    /*************************������������ end*******************************/
    /*******************�����ļ�����־�����ֵ*************************/
    public static final int NET_DVR_FILE_SUCCESS = 1000;	//����ļ���Ϣ
    public static final int NET_DVR_FILE_NOFIND = 1001;	//û���ļ�
    public static final int NET_DVR_ISFINDING = 1002;//���ڲ����ļ�
    public static final int NET_DVR_NOMOREFILE = 1003;//�����ļ�ʱû�и����ļ�
    public static final int NET_DVR_FILE_EXCEPTION = 1004;//�����ļ�ʱ�쳣
    /*********************�ص��������� begin************************/
    public static final int COMM_ALARM = 0x1100;	//8000������Ϣ�����ϴ�
    public static final int COMM_TRADEINFO = 0x1500;  //ATMDVR�����ϴ�������Ϣ
    public static final int COMM_ALARM_V30 = 0x4000;//9000������Ϣ�����ϴ�
    public static final int COMM_IPCCFG = 0x4001;//9000�豸IPC�������øı䱨����Ϣ�����ϴ�
    /*************�����쳣����(��Ϣ��ʽ, �ص���ʽ(����))****************/
    public static final int EXCEPTION_EXCHANGE = 0x8000;//�û�����ʱ�쳣
    public static final int EXCEPTION_AUDIOEXCHANGE = 0x8001;//�����Խ��쳣
    public static final int EXCEPTION_ALARM = 0x8002;//�����쳣
    public static final int EXCEPTION_PREVIEW = 0x8003;//����Ԥ���쳣
    public static final int EXCEPTION_SERIAL = 0x8004;//͸��ͨ���쳣
    public static final int EXCEPTION_RECONNECT = 0x8005;	//Ԥ��ʱ����
    public static final int EXCEPTION_ALARMRECONNECT = 0x8006;//����ʱ����
    public static final int EXCEPTION_SERIALRECONNECT = 0x8007;//͸��ͨ������
    public static final int EXCEPTION_PLAYBACK = 0x8010;//�ط��쳣
    public static final int EXCEPTION_DISKFMT = 0x8011;//Ӳ�̸�ʽ��
    /********************Ԥ���ص�����*********************/
    public static final int NET_DVR_SYSHEAD = 1;//ϵͳͷ���
    public static final int NET_DVR_STREAMDATA = 2;//��Ƶ����ݣ�����������������Ƶ�ֿ�����Ƶ����ݣ�
    public static final int NET_DVR_AUDIOSTREAMDATA = 3;//��Ƶ�����
    public static final int NET_DVR_STD_VIDEODATA = 4;//��׼��Ƶ�����
    public static final int NET_DVR_STD_AUDIODATA = 5;//��׼��Ƶ�����
//�ص�Ԥ���е�״̬����Ϣ
    public static final int NET_DVR_REALPLAYEXCEPTION = 111;//Ԥ���쳣
    public static final int NET_DVR_REALPLAYNETCLOSE = 112;//Ԥ��ʱ���ӶϿ�
    public static final int NET_DVR_REALPLAY5SNODATA = 113;//Ԥ��5sû���յ����
    public static final int NET_DVR_REALPLAYRECONNECT = 114;//Ԥ������
    /********************�طŻص�����*********************/
    public static final int NET_DVR_PLAYBACKOVER = 101;//�ط���ݲ������
    public static final int NET_DVR_PLAYBACKEXCEPTION = 102;//�ط��쳣
    public static final int NET_DVR_PLAYBACKNETCLOSE = 103;//�ط�ʱ�����ӶϿ�
    public static final int NET_DVR_PLAYBACK5SNODATA = 104;	//�ط�5sû���յ����
    /*********************�ص��������� end************************/
//�豸�ͺ�(DVR����)
/* �豸���� */
    public static final int DVR = 1;			/*����δ�����dvr���ͷ���NETRET_DVR*/
    public static final int ATMDVR = 2;		/*atm dvr*/
    public static final int DVS = 3;			/*DVS*/
    public static final int DEC = 4;			/* 6001D */
    public static final int ENC_DEC = 5;			/* 6001F */
    public static final int DVR_HC = 6;			/*8000HC*/
    public static final int DVR_HT = 7;			/*8000HT*/
    public static final int DVR_HF = 8;			/*8000HF*/
    public static final int DVR_HS = 9;			/* 8000HS DVR(no audio) */
    public static final int DVR_HTS = 10;         /* 8016HTS DVR(no audio) */
    public static final int DVR_HB = 11;         /* HB DVR(SATA HD) */
    public static final int DVR_HCS = 12;         /* 8000HCS DVR */
    public static final int DVS_A = 13;         /* ��ATAӲ�̵�DVS */
    public static final int DVR_HC_S = 14;         /* 8000HC-S */
    public static final int DVR_HT_S = 15;         /* 8000HT-S */
    public static final int DVR_HF_S = 16;         /* 8000HF-S */
    public static final int DVR_HS_S = 17;         /* 8000HS-S */
    public static final int ATMDVR_S = 18;         /* ATM-S */
    public static final int LOWCOST_DVR = 19;			/*7000Hϵ��*/
    public static final int DEC_MAT = 20;         /*��·������*/
    public static final int DVR_MOBILE = 21;			/* mobile DVR */
    public static final int DVR_HD_S = 22;        /* 8000HD-S */
    public static final int DVR_HD_SL = 23;			/* 8000HD-SL */
    public static final int DVR_HC_SL = 24;			/* 8000HC-SL */
    public static final int DVR_HS_ST = 25;			/* 8000HS_ST */
    public static final int DVS_HW = 26;         /* 6000HW */
    public static final int IPCAM = 30;			/*IP �����*/
    public static final int MEGA_IPCAM = 31;			/*X52MFϵ��,752MF,852MF*/
    public static final int IPCAM_X62MF = 32;			/*X62MFϵ�пɽ���9000�豸,762MF,862MF*/
    public static final int IPDOME = 40;			/*IP�������*/
    public static final int MEGA_IPDOME = 41;     /*IP�������*/
    public static final int IPMOD = 50;			/*IP ģ��*/
    public static final int DS71XX_H = 71;			/* DS71XXH_S */
    public static final int DS72XX_H_S = 72;			/* DS72XXH_S */
    public static final int DS73XX_H_S = 73;			/* DS73XXH_S */
    public static final int DS81XX_HS_S = 81;			/* DS81XX_HS_S */
    public static final int DS81XX_HL_S = 82;			/* DS81XX_HL_S */
    public static final int DS81XX_HC_S = 83;			/* DS81XX_HC_S */
    public static final int DS81XX_HD_S = 84;			/* DS81XX_HD_S */
    public static final int DS81XX_HE_S = 85;			/* DS81XX_HE_S */
    public static final int DS81XX_HF_S = 86;			/* DS81XX_HF_S */
    public static final int DS81XX_AH_S = 87;			/* DS81XX_AH_S */
    public static final int DS81XX_AHF_S = 88;			/* DS81XX_AHF_S */
    public static final int DS90XX_HF_S = 90;       /*DS90XX_HF_S*/
    public static final int DS91XX_HF_S = 91;             /*DS91XX_HF_S*/
    public static final int DS91XX_HD_S = 92;            /*91XXHD-S(MD)*/

    /* ���� */
//������
    public static final int MAJOR_OPERATION = 0x3;
//������
    public static final int MINOR_START_DVR = 0x41; /* ���� */
    public static final int MINOR_STOP_DVR = 0x42;/* �ػ� */
    public static final int MINOR_STOP_ABNORMAL = 0x43;/* �쳣�ػ� */
    public static final int MINOR_REBOOT_DVR = 0x44;   /*���������豸*/
    public static final int MINOR_LOCAL_LOGIN = 0x50; /* ���ص�½ */
    public static final int MINOR_LOCAL_LOGOUT = 0x51; /* ����ע���½ */
    public static final int MINOR_LOCAL_CFG_PARM = 0x52; /* �������ò��� */
    public static final int MINOR_LOCAL_PLAYBYFILE = 0x53; /* ���ذ��ļ��طŻ����� */
    public static final int MINOR_LOCAL_PLAYBYTIME = 0x54; /* ���ذ�ʱ��طŻ�����*/
    public static final int MINOR_LOCAL_START_REC = 0x55; /* ���ؿ�ʼ¼�� */
    public static final int MINOR_LOCAL_STOP_REC = 0x56; /* ����ֹͣ¼�� */
    public static final int MINOR_LOCAL_PTZCTRL = 0x57; /* ������̨���� */
    public static final int MINOR_LOCAL_PREVIEW = 0x58;/* ����Ԥ�� (������ʹ��)*/
    public static final int MINOR_LOCAL_MODIFY_TIME = 0x59;/* �����޸�ʱ��(������ʹ��) */
    public static final int MINOR_LOCAL_UPGRADE = 0x5a;/* ������ */
    public static final int MINOR_LOCAL_RECFILE_OUTPUT = 0x5b;   /* ���ر���¼���ļ� */
    public static final int MINOR_LOCAL_FORMAT_HDD = 0x5c;  /* ���س�ʼ��Ӳ�� */
    public static final int MINOR_LOCAL_CFGFILE_OUTPUT = 0x5d;  /* �������������ļ� */
    public static final int MINOR_LOCAL_CFGFILE_INPUT = 0x5e;  /* ���뱾�������ļ� */
    public static final int MINOR_LOCAL_COPYFILE = 0x5f;  /* ���ر����ļ� */
    public static final int MINOR_LOCAL_LOCKFILE = 0x60;  /* ������¼���ļ� */
    public static final int MINOR_LOCAL_UNLOCKFILE = 0x61;   /* ���ؽ���¼���ļ� */
    public static final int MINOR_LOCAL_DVR_ALARM = 0x62;  /* �����ֶ����ʹ�������*/
    public static final int MINOR_IPC_ADD = 0x63;  /* �������IPC */
    public static final int MINOR_IPC_DEL = 0x64;  /* ����ɾ��IPC */
    public static final int MINOR_IPC_SET = 0x65;  /* ��������IPC */
    public static final int MINOR_LOCAL_START_BACKUP = 0x66;	/* ���ؿ�ʼ���� */
    public static final int MINOR_LOCAL_STOP_BACKUP = 0x67;/* ����ֹͣ����*/
    public static final int MINOR_LOCAL_COPYFILE_START_TIME = 0x68;/* ���ر��ݿ�ʼʱ��*/
    public static final int MINOR_LOCAL_COPYFILE_END_TIME = 0x69;	/* ���ر��ݽ���ʱ��*/
    public static final int MINOR_REMOTE_LOGIN = 0x70;/* Զ�̵�¼ */
    public static final int MINOR_REMOTE_LOGOUT = 0x71;/* Զ��ע���½ */
    public static final int MINOR_REMOTE_START_REC = 0x72;/* Զ�̿�ʼ¼�� */
    public static final int MINOR_REMOTE_STOP_REC = 0x73;/* Զ��ֹͣ¼�� */
    public static final int MINOR_START_TRANS_CHAN = 0x74;/* ��ʼ͸������ */
    public static final int MINOR_STOP_TRANS_CHAN = 0x75; /* ֹͣ͸������ */
    public static final int MINOR_REMOTE_GET_PARM = 0x76;/* Զ�̻�ȡ���� */
    public static final int MINOR_REMOTE_CFG_PARM = 0x77;/* Զ�����ò��� */
    public static final int MINOR_REMOTE_GET_STATUS = 0x78;/* Զ�̻�ȡ״̬ */
    public static final int MINOR_REMOTE_ARM = 0x79; /* Զ�̲��� */
    public static final int MINOR_REMOTE_DISARM = 0x7a;/* Զ�̳��� */
    public static final int MINOR_REMOTE_REBOOT = 0x7b; /* Զ������ */
    public static final int MINOR_START_VT = 0x7c;/* ��ʼ�����Խ� */
    public static final int MINOR_STOP_VT = 0x7d;/* ֹͣ�����Խ� */
    public static final int MINOR_REMOTE_UPGRADE = 0x7e; /* Զ���� */
    public static final int MINOR_REMOTE_PLAYBYFILE = 0x7f; /* Զ�̰��ļ��ط� */
    public static final int MINOR_REMOTE_PLAYBYTIME = 0x80; /* Զ�̰�ʱ��ط� */
    public static final int MINOR_REMOTE_PTZCTRL = 0x81; /* Զ����̨���� */
    public static final int MINOR_REMOTE_FORMAT_HDD = 0x82;  /* Զ�̸�ʽ��Ӳ�� */
    public static final int MINOR_REMOTE_STOP = 0x83;  /* Զ�̹ػ� */
    public static final int MINOR_REMOTE_LOCKFILE = 0x84;/* Զ�����ļ� */
    public static final int MINOR_REMOTE_UNLOCKFILE = 0x85;/* Զ�̽����ļ� */
    public static final int MINOR_REMOTE_CFGFILE_OUTPUT = 0x86;   /* Զ�̵��������ļ� */
    public static final int MINOR_REMOTE_CFGFILE_INTPUT = 0x87;   /* Զ�̵��������ļ� */
    public static final int MINOR_REMOTE_RECFILE_OUTPUT = 0x88;   /* Զ�̵���¼���ļ� */
    public static final int MINOR_REMOTE_DVR_ALARM = 0x89;    /* Զ���ֶ����ʹ�������*/
    public static final int MINOR_REMOTE_IPC_ADD = 0x8a;  /* Զ�����IPC */
    public static final int MINOR_REMOTE_IPC_DEL = 0x8b;/* Զ��ɾ��IPC */
    public static final int MINOR_REMOTE_IPC_SET = 0x8c; /* Զ������IPC */
    public static final int MINOR_REBOOT_VCA_LIB = 0x8d;		/*�������ܿ�*/

    /*��־������Ϣ*/
//������
    public static final int MAJOR_INFORMATION = 0x4;   /*������Ϣ*/
//������
    public static final int MINOR_HDD_INFO = 0xa1;/*Ӳ����Ϣ*/
    public static final int MINOR_SMART_INFO = 0xa2;   /*SMART��Ϣ*/
    public static final int MINOR_REC_START = 0xa3;   /*��ʼ¼��*/
    public static final int MINOR_REC_STOP = 0xa4;/*ֹͣ¼��*/
    public static final int MINOR_REC_OVERDUE = 0xa5;/*����¼��ɾ��*/
    public static final int MINOR_LINK_START = 0xa6; // ivms����·������������ǰ���豸
    public static final int MINOR_LINK_STOP = 0xa7;// ivms����·�������ȶϿ�ǰ���豸��
//����־��������ΪMAJOR_OPERATION=03��������ΪMINOR_LOCAL_CFG_PARM=0x52����MINOR_REMOTE_GET_PARM=0x76����MINOR_REMOTE_CFG_PARM=0x77ʱ��dwParaType:����������Ч���京�����£�
    public static final int PARA_VIDEOOUT = 0x1;
    public static final int PARA_IMAGE = 0x2;
    public static final int PARA_ENCODE = 0x4;
    public static final int PARA_NETWORK = 0x8;
    public static final int PARA_ALARM = 0x10;
    public static final int PARA_EXCEPTION = 0x20;
    public static final int PARA_DECODER = 0x40; /*������*/
    public static final int PARA_RS232 = 0x80;
    public static final int PARA_PREVIEW = 0x100;
    public static final int PARA_SECURITY = 0x200;
    public static final int PARA_DATETIME = 0x400;
    public static final int PARA_FRAMETYPE = 0x800;  /*֡��ʽ*/
    public static final int PARA_VCA_RULE = 0x1000;    //��Ϊ����
//SDK_V222
//�����豸����
    public static final int DS6001_HF_B = 60;//��Ϊ������DS6001-HF/B
    public static final int DS6001_HF_P = 61;//����ʶ��DS6001-HF/P
    public static final int DS6002_HF_B = 62;//˫����٣�DS6002-HF/B
    public static final int DS6101_HF_B = 63;//��Ϊ������DS6101-HF/B
    public static final int IVMS_2000 = 64;//���ܷ�����
    public static final int DS9000_IVS = 65;//9000ϵ������DVR
    public static final int DS8004_AHL_A = 66;//����ATM, DS8004AHL-S/A
    public static final int DS6101_HF_P = 67;//����ʶ��DS6101-HF/P
//������ȡ����
    public static final int VCA_DEV_ABILITY = 0x100;//�豸���ܷ�����������
    public static final int VCA_CHAN_ABILITY = 0x110;//��Ϊ��������
//��ȡ/���ô�ӿڲ�����������
//����ʶ��NET_VCA_PLATE_CFG��;
    public static final int NET_DVR_SET_PLATECFG = 150 ;//���ó���ʶ�����

    public static final int NET_DVR_GET_PLATECFG = 151;	//��ȡ����ʶ�����
//��Ϊ��Ӧ��NET_VCA_RULECFG��
    public static final int NET_DVR_SET_RULECFG = 152;	//������Ϊ��������
    public static final int NET_DVR_GET_RULECFG = 153;//��ȡ��Ϊ��������,
//˫�����궨����NET_DVR_LF_CFG��
    public static final int NET_DVR_SET_LF_CFG = 160;//����˫���������ò���
    public static final int NET_DVR_GET_LF_CFG = 161;//��ȡ˫���������ò���
//���ܷ�����ȡ�����ýṹ
    public static final int NET_DVR_SET_IVMS_STREAMCFG = 162;	//�������ܷ�����ȡ������
    public static final int NET_DVR_GET_IVMS_STREAMCFG = 163;	//��ȡ���ܷ�����ȡ������
//���ܿ��Ʋ���ṹ
    public static final int NET_DVR_SET_VCA_CTRLCFG = 164; //�������ܿ��Ʋ���
    public static final int NET_DVR_GET_VCA_CTRLCFG = 165;	 //��ȡ���ܿ��Ʋ���
//��������NET_VCA_MASK_REGION_LIST
    public static final int NET_DVR_SET_VCA_MASK_REGION = 166;	 //���������������
    public static final int NET_DVR_GET_VCA_MASK_REGION = 167;	 //��ȡ�����������
//ATM�������� NET_VCA_ENTER_REGION
    public static final int NET_DVR_SET_VCA_ENTER_REGION = 168; //���ý����������
    public static final int NET_DVR_GET_VCA_ENTER_REGION = 169;	 //��ȡ�����������
//�궨������NET_VCA_LINE_SEGMENT_LIST
    public static final int NET_DVR_SET_VCA_LINE_SEGMENT = 170;	 //���ñ궨��
    public static final int NET_DVR_GET_VCA_LINE_SEGMENT = 171;	 //��ȡ�궨��
// ivms��������NET_IVMS_MASK_REGION_LIST
    public static final int NET_DVR_SET_IVMS_MASK_REGION = 172;	 //����IVMS�����������
    public static final int NET_DVR_GET_IVMS_MASK_REGION = 173;	 //��ȡIVMS�����������
// ivms����������NET_IVMS_ENTER_REGION
    public static final int NET_DVR_SET_IVMS_ENTER_REGION = 174; //����IVMS�����������
    public static final int NET_DVR_GET_IVMS_ENTER_REGION = 175; //��ȡIVMS�����������
    public static final int NET_DVR_SET_IVMS_BEHAVIORCFG = 176;//�������ܷ�������Ϊ�������
    public static final int NET_DVR_GET_IVMS_BEHAVIORCFG = 177;	//��ȡ���ܷ�������Ϊ�������



    /**********************�豸���� end***********************/

/*************************************************
�������ýṹ������(����_V30Ϊ9000����)
**************************************************/

/////////////////////////////////////////////////////////////////////////
//Уʱ�ṹ����
    public static class NET_DVR_TIME extends Structure {//Уʱ�ṹ����
        public int dwYear;		//��
        public int dwMonth;		//��
        public int dwDay;		//��
        public int dwHour;		//ʱ
        public int dwMinute;		//��
        public int dwSecond;		//��

      public String toString() {
            return "NET_DVR_TIME.dwYear: " + dwYear + "\n" + "NET_DVR_TIME.dwMonth: \n" + dwMonth + "\n" + "NET_DVR_TIME.dwDay: \n" + dwDay + "\n" + "NET_DVR_TIME.dwHour: \n" + dwHour + "\n" + "NET_DVR_TIME.dwMinute: \n" + dwMinute + "\n" + "NET_DVR_TIME.dwSecond: \n" + dwSecond;
        }

        //�����б�����ʾ
        public String toStringTime()
        {
            return  String.format("%02d/%02d/%02d%02d:%02d:%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }

        //�洢�ļ���ʹ��
         public String toStringTitle()
        {
            return  String.format("Time%02d%02d%02d%02d%02d%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }
    }

    public static class NET_DVR_SCHEDTIME extends Structure {
        public byte byStartHour;	//��ʼʱ��
        public byte byStartMin;
        public byte byStopHour;	        //����ʱ��
        public byte byStopMin;
    }

  public static class NET_DVR_HANDLEEXCEPTION_V30 extends Structure {
	public int dwHandleType;	/*���?ʽ,���?ʽ��"��"���*//*0x00: ����Ӧ*//*0x01: �������Ͼ���*//*0x02: ��������*//*0x04: �ϴ�����*/	/*0x08: �����������*//*0x20: ����ץͼ*/  //(JPEG����)
	public byte[] byRelAlarmOut = new byte[MAX_ALARMOUT_V30];  //�������������ͨ��,�������������,Ϊ1��ʾ���������
}

//�������쳣����ṹ(�ӽṹ)(�ദʹ��)
  public static class NET_DVR_HANDLEEXCEPTION extends Structure {
	public int	dwHandleType;			/*���?ʽ,���?ʽ��"��"���*//*0x00: ����Ӧ*//*0x01: �������Ͼ���*//*0x02: ��������*//*0x04: �ϴ�����*/	/*0x08: �����������*//*0x20: ����ץͼ*/  //(JPEG����)
	public byte[]  byRelAlarmOut = new byte[MAX_ALARMOUT];  //�������������ͨ��,�������������,Ϊ1��ʾ���������
}

//DVR�豸����
  public static class NET_DVR_DEVICECFG extends Structure {
        public int dwSize;
        public byte[] sDVRName = new byte[NAME_LEN];     //DVR���
        public int dwDVRID;				 //DVR ID,����ң���� //V1.4(0-99), V1.5(0-255)
        public int dwRecycleRecord;		         //�Ƿ�ѭ��¼��,0:����; 1:��
        //���²��ɸ��
        public byte[] sSerialNumber = new byte[SERIALNO_LEN];  //���к�
        public int dwSoftwareVersion;			       //����汾��,��16λ�����汾,��16λ�Ǵΰ汾
        public int dwSoftwareBuildDate;			        //����������,0xYYYYMMDD
        public int dwDSPSoftwareVersion;		        //DSP����汾,��16λ�����汾,��16λ�Ǵΰ汾
        public int dwDSPSoftwareBuildDate;		        // DSP����������,0xYYYYMMDD
        public int dwPanelVersion;				// ǰ���汾,��16λ�����汾,��16λ�Ǵΰ汾
        public int dwHardwareVersion;	        // Ӳ���汾,��16λ�����汾,��16λ�Ǵΰ汾
        public byte byAlarmInPortNum;		//DVR�����������
        public byte byAlarmOutPortNum;		//DVR�����������
        public byte byRS232Num;			//DVR 232���ڸ���
        public byte byRS485Num;			//DVR 485���ڸ���
        public byte byNetworkPortNum;		//����ڸ���
        public byte byDiskCtrlNum;			//DVR Ӳ�̿���������
        public byte byDiskNum;				//DVR Ӳ�̸���
        public byte byDVRType;				//DVR����, 1:DVR 2:ATM DVR 3:DVS ......
        public byte byChanNum;				//DVR ͨ������
        public byte byStartChan;			//��ʼͨ����,����DVS-1,DVR - 1
        public byte byDecordChans;			//DVR ����·��
        public byte byVGANum;				//VGA�ڵĸ���
        public byte byUSBNum;				//USB�ڵĸ���
        public byte byAuxoutNum;			//���ڵĸ���
        public byte byAudioNum;			        //�����ڵĸ���
        public byte byIPChanNum;			//�������ͨ����
    }

public static class NET_DVR_IPADDR extends Structure {
        public byte[] sIpV4 = new byte[16];
        public byte[] byRes = new byte[128];

        public String toString() {
            return "NET_DVR_IPADDR.sIpV4: " + new String(sIpV4) + "\n" + "NET_DVR_IPADDR.byRes: " + new String(byRes) + "\n";
        }
    }


//������ݽṹ(�ӽṹ)(9000��չ)
    public static class NET_DVR_ETHERNET_V30 extends Structure {
        public NET_DVR_IPADDR struDVRIP;
        public NET_DVR_IPADDR struDVRIPMask;
        public int dwNetInterface;
        public short wDVRPort;
        public short wMTU;
        public byte[] byMACAddr = new byte[6];

        public String toString() {
            return "NET_DVR_ETHERNET_V30.struDVRIP: \n" + struDVRIP + "\n" + "NET_DVR_ETHERNET_V30.struDVRIPMask: \n" + struDVRIPMask + "\n" + "NET_DVR_ETHERNET_V30.dwNetInterface: " + dwNetInterface + "\n" + "NET_DVR_ETHERNET_V30.wDVRPort: " + wDVRPort + "\n" + "NET_DVR_ETHERNET_V30.wMTU: " + wMTU + "\n" + "NET_DVR_ETHERNET_V30.byMACAddr: " + new String(byMACAddr) + "\n";
        }
    }

    public static class NET_DVR_ETHERNET extends Structure {//������ݽṹ(�ӽṹ)
	public byte[]  sDVRIP = new byte[16];                    //DVR IP��ַ
	public byte[]  sDVRIPMask = new byte[16];                //DVR IP��ַ����
	public int dwNetInterface;               //����ӿ� 1-10MBase-T 2-10MBase-Tȫ˫�� 3-100MBase-TX 4-100Mȫ˫�� 5-10M/100M����Ӧ
	public short wDVRPort;		                //�˿ں�
	public byte[]  byMACAddr = new byte[MACADDR_LEN];		//�������������ַ
}

    public static class NET_DVR_PPPOECFG extends Structure {//PPPoe
        public int dwPPPoE;
        public byte[] sPPPoEUser = new byte[32];
        public byte[] sPPPoEPassword = new byte[16];
        public NET_DVR_IPADDR struPPPoEIP;
    }

 public static class NET_DVR_NETCFG_V30 extends Structure {
        public int dwSize;
        public NET_DVR_ETHERNET_V30[] struEtherNet = new NET_DVR_ETHERNET_V30[2];
        public NET_DVR_IPADDR[] struRes1 = new NET_DVR_IPADDR[2];
        public NET_DVR_IPADDR struAlarmHostIpAddr;
        public short[] wRes2 = new short[2];
        public short wAlarmHostIpPort;
        public byte byUseDhcp;
        public byte byRes3;
        public NET_DVR_IPADDR struDnsServer1IpAddr;
        public NET_DVR_IPADDR struDnsServer2IpAddr;
        public byte[] byIpResolver = new byte[64];
        public short wIpResolverPort;
        public short wHttpPortNo;
        public NET_DVR_IPADDR struMulticastIpAddr;
        public NET_DVR_IPADDR struGatewayIpAddr;
        public NET_DVR_PPPOECFG struPPPoE;
        public byte[] byRes = new byte[64];

        public String toString() {
            return "NET_DVR_NETCFG_V30.dwSize: " + dwSize + "\n" + "NET_DVR_NETCFG_V30.struEtherNet[0]: \n" + struEtherNet[0] + "\n" + "NET_DVR_NETCFG_V30.struAlarmHostIpAddr: \n" + struAlarmHostIpAddr + "\n" + "NET_DVR_NETCFG_V30.wAlarmHostIpPort: " + wAlarmHostIpPort + "\n" + "NET_DVR_NETCFG_V30.wHttpPortNo: " + wHttpPortNo + "\n" + "NET_DVR_NETCFG_V30.struGatewayIpAddr: \n" + struGatewayIpAddr + "\n";
        }
    }


 public static class NET_DVR_NETCFG extends Structure {//�������ýṹ
	public int dwSize;
	public NET_DVR_ETHERNET[] struEtherNet = new NET_DVR_ETHERNET[MAX_ETHERNET];		/* ��̫��� */
	public byte[] sManageHostIP = new byte[16];		    //Զ�̹��������ַ
	public short wManageHostPort;		    //Զ�̹�������˿ں�
	public byte[] sIPServerIP = new byte[16];           //IPServer��������ַ
	public byte[] sMultiCastIP = new byte[16];          //�ಥ���ַ
	public byte[] sGatewayIP = new byte[16];       	    //��ص�ַ
	public byte[] sNFSIP = new byte[16];			    //NFS����IP��ַ
	public byte[] sNFSDirectory = new byte[PATHNAME_LEN];//NFSĿ¼
	public int dwPPPOE;				    //0-������,1-����
	public byte[] sPPPoEUser = new byte[NAME_LEN];	    //PPPoE�û���
	public byte[] sPPPoEPassword = new byte[PASSWD_LEN];// PPPoE����
	public byte[] sPPPoEIP = new byte[16];			    //PPPoE IP��ַ(ֻ��)
}

//ͨ��ͼ��ṹ
    public static class NET_DVR_SCHEDTIMEWEEK extends Structure {
        public NET_DVR_SCHEDTIME[] struAlarmTime = new NET_DVR_SCHEDTIME[8];
    }

     public static class byte96 extends Structure {
        public byte[] byMotionScope = new byte[96];
    }
     
  public static class NET_DVR_MOTION_V30 extends Structure {//�ƶ����(�ӽṹ)(9000��չ)
        public byte96[] byMotionScope = new byte96[64];						/*�������,0-96λ,��ʾ64��,����96*64��С���,Ϊ1��ʾ���ƶ��������,0-��ʾ����*/
        public byte byMotionSensitive;							/*�ƶ����������, 0 - 5,Խ��Խ����,oxff�ر�*/
        public byte byEnableHandleMotion;						/* �Ƿ����ƶ���� 0���� 1����*/
        public byte byPrecision;							/* �ƶ�����㷨�Ľ��: 0--16*16, 1--32*32, 2--64*64 ... */
        public byte reservedData;
        public NET_DVR_HANDLEEXCEPTION_V30 struMotionHandleType;			/* ���?ʽ */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; /*����ʱ��*/
        public byte[] byRelRecordChan = new byte[64];					/* ����������¼��ͨ��*/
    }

  public static class NET_DVR_MOTION extends Structure {//�ƶ����(�ӽṹ)
	byte[][] byMotionScope = new byte[18][22];	/*�������,����22*18��С���,Ϊ1��ʾ�ĺ�����ƶ��������,0-��ʾ����*/
	byte byMotionSensitive;		/*�ƶ����������, 0 - 5,Խ��Խ����,0xff�ر�*/
	byte byEnableHandleMotion;	/* �Ƿ����ƶ���� */
        byte[]  reservedData = new byte[2];
        NET_DVR_HANDLEEXCEPTION strMotionHandleType;	/* ���?ʽ */
	byte[] byRelRecordChan = new byte[MAX_CHANNUM]; //����������¼��ͨ��,Ϊ1��ʾ������ͨ��
}

  public static class NET_DVR_HIDEALARM_V30 extends Structure {//�ڵ�����
        public int dwEnableHideAlarm;				/* �Ƿ������ڵ����� ,0-��,1-�������� 2-�������� 3-��������*/
        public short wHideAlarmAreaTopLeftX;			/* �ڵ������x��� */
        public short wHideAlarmAreaTopLeftY;			/* �ڵ������y��� */
        public short wHideAlarmAreaWidth;				/* �ڵ�����Ŀ� */
        public short wHideAlarmAreaHeight;				/*�ڵ�����ĸ�*/
        public NET_DVR_HANDLEEXCEPTION_V30 strHideAlarmHandleType;	/* ���?ʽ */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS];//����ʱ��
    }

  public static class NET_DVR_HIDEALARM extends Structure {//�ڵ�����(�ӽṹ)  �����С704*576
	public int dwEnableHideAlarm;				/* �Ƿ������ڵ����� ,0-��,1-�������� 2-�������� 3-��������*/
	public short wHideAlarmAreaTopLeftX;			/* �ڵ������x��� */
	public short wHideAlarmAreaTopLeftY;			/* �ڵ������y��� */
	public short wHideAlarmAreaWidth;				/* �ڵ�����Ŀ� */
	public short wHideAlarmAreaHeight;				/*�ڵ�����ĸ�*/
	public NET_DVR_HANDLEEXCEPTION strHideAlarmHandleType;	/* ���?ʽ */
}

    public static class NET_DVR_VILOST_V30 extends Structure {    //�źŶ�ʧ����(�ӽṹ)(9000��չ)
        public byte byEnableHandleVILost;	                     /* �Ƿ����źŶ�ʧ���� */
        public NET_DVR_HANDLEEXCEPTION_V30 strVILostHandleType;	     /* ���?ʽ */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS];//����ʱ��
    }

public static class NET_DVR_VILOST extends Structure {    //�źŶ�ʧ����(�ӽṹ)
	byte byEnableHandleVILost;	/* �Ƿ����źŶ�ʧ���� */
	NET_DVR_HANDLEEXCEPTION strVILostHandleType;	/* ���?ʽ */
}

    public static class NET_DVR_SHELTER extends Structure {  //�ڵ�����(�ӽṹ)
        public short wHideAreaTopLeftX;				/* �ڵ������x��� */
        public short wHideAreaTopLeftY;				/* �ڵ������y��� */
        public short wHideAreaWidth;				/* �ڵ�����Ŀ� */
        public short wHideAreaHeight;				/* �ڵ�����ĸ�*/
    }

    public static class NET_DVR_COLOR extends Structure {
        public byte byBrightness;  	/*����,0-255*/
        public byte byContrast;    	/*�Աȶ�,0-255*/
        public byte bySaturation;  	/*���Ͷ�,0-255*/
        public byte byHue;    		/*ɫ��,0-255*/
    }

    public static class NET_DVR_VICOLOR extends Structure {
        public NET_DVR_COLOR[] struColor = new NET_DVR_COLOR[MAX_TIMESEGMENT_V30];/*ͼ�����(��һ����Ч�������������)*/
        public NET_DVR_SCHEDTIME[] struHandleTime = new NET_DVR_SCHEDTIME[MAX_TIMESEGMENT_V30];/*����ʱ���(����)*/
    };

    public static class NET_DVR_PICCFG_V30 extends Structure {
        public int dwSize;
        public byte[] sChanName = new byte[NAME_LEN];
        public int dwVideoFormat;	            /* ֻ�� ��Ƶ��ʽ 1-NTSC 2-PAL*/
        public NET_DVR_VICOLOR struViColor;        // ͼ�����ʱ�������
        public int dwShowChanName;               // Ԥ����ͼ�����Ƿ���ʾͨ�����,0-����ʾ,1-��ʾ �����С704*576
        public short wShowNameTopLeftX;				/* ͨ�������ʾλ�õ�x��� */
        public short wShowNameTopLeftY;				/* ͨ�������ʾλ�õ�y��� */
        public NET_DVR_VILOST_V30 struVILost;      //��Ƶ�źŶ�ʧ����
        public NET_DVR_VILOST_V30 struAULost;	/*��Ƶ�źŶ�ʧ����(����)*/
        public NET_DVR_MOTION_V30 struMotion;      //�ƶ����
        public NET_DVR_HIDEALARM_V30 struHideAlarm;//�ڵ�����
        public int dwEnableHide;		            /* �Ƿ������ڸ�(�����С704*576) ,0-��,1-��*/
        public NET_DVR_SHELTER[] struShelter = new NET_DVR_SHELTER[4];
        public int dwShowOsd;                //Ԥ����ͼ�����Ƿ���ʾOSD,0-����ʾ,1-��ʾ �����С704*576
        public short wOSDTopLeftX;				/* OSD��x��� */
        public short wOSDTopLeftY;				/* OSD��y��� */
        public byte byOSDType;					/* OSD����(��Ҫ�������ո�ʽ) */
        public byte byDispWeek;				/* �Ƿ���ʾ���� */
        public byte byOSDAttrib;				/* OSD����:͸������˸ */
        public byte byHourOSDType;				/* OSDСʱ��:0-24Сʱ��,1-12Сʱ�� */
        public byte[] byRes = new byte[64];
    }

    public static class NET_DVR_PICCFG_EX extends Structure {//ͨ��ͼ��ṹSDK_V14��չ
	public int dwSize;
	 public byte[] sChanName = new byte[NAME_LEN];
	 public int dwVideoFormat;	/* ֻ�� ��Ƶ��ʽ 1-NTSC 2-PAL*/
	 public byte byBrightness;  	/*����,0-255*/
	 public byte byContrast;    	/*�Աȶ�,0-255*/
	 public byte bySaturation;  	/*���Ͷ�,0-255 */
	 public byte byHue;    			/*ɫ��,0-255*/
	//��ʾͨ����
	 public int dwShowChanName; // Ԥ����ͼ�����Ƿ���ʾͨ�����,0-����ʾ,1-��ʾ �����С704*576
	 public short wShowNameTopLeftX;				/* ͨ�������ʾλ�õ�x��� */
	 public short wShowNameTopLeftY;				/* ͨ�������ʾλ�õ�y��� */
	//�źŶ�ʧ����
	 public NET_DVR_VILOST struVILost;
	//�ƶ����
	 public NET_DVR_MOTION struMotion;
	//�ڵ�����
	 public NET_DVR_HIDEALARM struHideAlarm;
	//�ڵ�  �����С704*576
	 public int dwEnableHide;		/* �Ƿ������ڵ� ,0-��,1-��*/
	 public NET_DVR_SHELTER[] struShelter = new NET_DVR_SHELTER[MAX_SHELTERNUM];
	//OSD
	 public int dwShowOsd;// Ԥ����ͼ�����Ƿ���ʾOSD,0-����ʾ,1-��ʾ �����С704*576
	 public short wOSDTopLeftX;				/* OSD��x��� */
	 public short wOSDTopLeftY;				/* OSD��y��� */
	 public byte byOSDType;					/* OSD����(��Ҫ�������ո�ʽ) */
	/* 0: XXXX-XX-XX ������ */
	/* 1: XX-XX-XXXX ������ */
	/* 2: XXXX��XX��XX�� */
	/* 3: XX��XX��XXXX�� */
	/* 4: XX-XX-XXXX ������*/
	/* 5: XX��XX��XXXX�� */
	 public byte byDispWeek;				/* �Ƿ���ʾ���� */
	 public byte byOSDAttrib;				/* OSD����:͸������˸ */
	/* 0: ����ʾOSD */
	/* 1: ͸��,��˸ */
	/* 2: ͸��,����˸ */
	/* 3: ��˸,��͸�� */
	/* 4: ��͸��,����˸ */
	 public byte byHourOsdType;	//Сʱ�ƣ�0��ʾ24Сʱ�ƣ�1-12Сʱ�ƻ�am/pm
}


 public static class NET_DVR_PICCFG extends Structure { //ͨ��ͼ��ṹ(SDK_V13��֮ǰ�汾)
	 public int dwSize;
	 public byte[] sChanName = new byte[NAME_LEN];
	 public int dwVideoFormat;	/* ֻ�� ��Ƶ��ʽ 1-NTSC 2-PAL*/
	 public byte byBrightness;  	/*����,0-255*/
	 public byte byContrast;    	/*�Աȶ�,0-255*/
	 public byte bySaturation;  	/*���Ͷ�,0-255 */
	 public byte byHue;    			/*ɫ��,0-255*/
	//��ʾͨ����
	 public int dwShowChanName; // Ԥ����ͼ�����Ƿ���ʾͨ�����,0-����ʾ,1-��ʾ �����С704*576
	 public short wShowNameTopLeftX;				/* ͨ�������ʾλ�õ�x��� */
	 public short wShowNameTopLeftY;				/* ͨ�������ʾλ�õ�y��� */
	//�źŶ�ʧ����
	 public NET_DVR_VILOST struVILost;
	//�ƶ����
	 public NET_DVR_MOTION struMotion;
	//�ڵ�����
	 public NET_DVR_HIDEALARM struHideAlarm;
	//�ڵ�  �����С704*576
	 public int dwEnableHide;		/* �Ƿ������ڵ� ,0-��,1-��*/
	 public short wHideAreaTopLeftX;				/* �ڵ������x��� */
	 public short wHideAreaTopLeftY;				/* �ڵ������y��� */
	 public short wHideAreaWidth;				/* �ڵ�����Ŀ� */
	 public short wHideAreaHeight;				/*�ڵ�����ĸ�*/
	//OSD
	 public int dwShowOsd;// Ԥ����ͼ�����Ƿ���ʾOSD,0-����ʾ,1-��ʾ �����С704*576
	 public short wOSDTopLeftX;				/* OSD��x��� */
	 public short wOSDTopLeftY;				/* OSD��y��� */
	 public byte byOSDType;					/* OSD����(��Ҫ�������ո�ʽ) */
	/* 0: XXXX-XX-XX ������ */
	/* 1: XX-XX-XXXX ������ */
	/* 2: XXXX��XX��XX�� */
	/* 3: XX��XX��XXXX�� */
	/* 4: XX-XX-XXXX ������*/
	/* 5: XX��XX��XXXX�� */
	byte byDispWeek;				/* �Ƿ���ʾ���� */
	byte byOSDAttrib;				/* OSD����:͸������˸ */
	/* 0: ����ʾOSD */
	/* 1: ͸��,��˸ */
	/* 2: ͸��,����˸ */
	/* 3: ��˸,��͸�� */
	/* 4: ��͸��,����˸ */
	 public byte reservedData2;
}

    //����ѹ������(�ӽṹ)(9000��չ)
    public static class NET_DVR_COMPRESSION_INFO_V30 extends Structure {
        public byte byStreamType;		//�������� 0-��Ƶ��, 1-������
        public byte byResolution;  	//�ֱ���0-DCIF 1-CIF, 2-QCIF, 3-4CIF, 4-2CIF 5��������16-VGA��640*480�� 17-UXGA��1600*1200�� 18-SVGA ��800*600��19-HD720p��1280*720��20-XVGA  21-HD900p
        public byte byBitrateType;		//�������� 0:�����ʣ�1:������
        public byte byPicQuality;		//ͼ������ 0-��� 1-�κ� 2-�Ϻ� 3-һ�� 4-�ϲ� 5-��
        public int dwVideoBitrate; 	//��Ƶ���� 0-���� 1-16K 2-32K 3-48k 4-64K 5-80K 6-96K 7-128K 8-160k 9-192K 10-224K 11-256K 12-320K 13-384K 14-448K 15-512K 16-640K 17-768K 18-896K 19-1024K 20-1280K 21-1536K 22-1792K 23-2048���λ(31λ)�ó�1��ʾ���Զ�������, 0-30λ��ʾ����ֵ��
        public int dwVideoFrameRate;	//֡�� 0-ȫ��; 1-1/16; 2-1/8; 3-1/4; 4-1/2; 5-1; 6-2; 7-4; 8-6; 9-8; 10-10; 11-12; 12-16; 13-20; V2.0�汾���¼�14-15; 15-18; 16-22;
        public short wIntervalFrameI;  //I֡���
        public byte byIntervalBPFrame;//0-BBP֡; 1-BP֡; 2-��P֡
        public byte byENumber;        //E֡������������
        public byte byVideoEncType;//��Ƶ�������� 0 hik264;1��׼h264; 2��׼mpeg4;
        public byte byAudioEncType;//��Ƶ�������� 0 G722
        public byte[] byres = new byte[10];
    }

    //ͨ��ѹ������(9000��չ)
    public static class NET_DVR_COMPRESSIONCFG_V30 extends Structure {
        public int dwSize;
        public NET_DVR_COMPRESSION_INFO_V30 struNormHighRecordPara;    //¼�� ��Ӧ8000����ͨ
        public NET_DVR_COMPRESSION_INFO_V30 struRes;   //���� String[28];
        public NET_DVR_COMPRESSION_INFO_V30 struEventRecordPara;       //�¼�����ѹ������
        public NET_DVR_COMPRESSION_INFO_V30 struNetPara;               //��(������)
    }


    public static class NET_DVR_COMPRESSION_INFO extends Structure {//����ѹ������(�ӽṹ)
	public byte byStreamType;		//��������0-��Ƶ��,1-������,��ʾѹ������ʱ���λ��ʾ�Ƿ�����ѹ������
	public byte byResolution;  	//�ֱ���0-DCIF 1-CIF, 2-QCIF, 3-4CIF, 4-2CIF, 5-2QCIF(352X144)(����ר��)
	public byte byBitrateType;		//��������0:�����ʣ�1:������
	public byte  byPicQuality;		//ͼ������ 0-��� 1-�κ� 2-�Ϻ� 3-һ�� 4-�ϲ� 5-��
	public int dwVideoBitrate; 	//��Ƶ���� 0-���� 1-16K(����) 2-32K 3-48k 4-64K 5-80K 6-96K 7-128K 8-160k 9-192K 10-224K 11-256K 12-320K
							// 13-384K 14-448K 15-512K 16-640K 17-768K 18-896K 19-1024K 20-1280K 21-1536K 22-1792K 23-2048K
							//���λ(31λ)�ó�1��ʾ���Զ�������, 0-30λ��ʾ����ֵ(MIN-32K MAX-8192K)��
	public int dwVideoFrameRate;	//֡�� 0-ȫ��; 1-1/16; 2-1/8; 3-1/4; 4-1/2; 5-1; 6-2; 7-4; 8-6; 9-8; 10-10; 11-12; 12-16; 13-20;
}

    public static class NET_DVR_COMPRESSIONCFG extends Structure {//ͨ��ѹ������
	public int dwSize;
	public NET_DVR_COMPRESSION_INFO struRecordPara; //¼��/�¼�����¼��
	public NET_DVR_COMPRESSION_INFO struNetPara;	//��/����
}


    public static class NET_DVR_COMPRESSION_INFO_EX extends Structure {//����ѹ������(�ӽṹ)(��չ) ����I֡���
	public byte byStreamType;		//��������0-��Ƶ��, 1-������
	public byte byResolution;  	//�ֱ���0-DCIF 1-CIF, 2-QCIF, 3-4CIF, 4-2CIF, 5-2QCIF(352X144)(����ר��)
	public byte byBitrateType;		//��������0:�����ʣ�1:������
	public byte  byPicQuality;		//ͼ������ 0-��� 1-�κ� 2-�Ϻ� 3-һ�� 4-�ϲ� 5-��
	public int dwVideoBitrate; 	//��Ƶ���� 0-���� 1-16K(����) 2-32K 3-48k 4-64K 5-80K 6-96K 7-128K 8-160k 9-192K 10-224K 11-256K 12-320K
	// 13-384K 14-448K 15-512K 16-640K 17-768K 18-896K 19-1024K 20-1280K 21-1536K 22-1792K 23-2048K
	//���λ(31λ)�ó�1��ʾ���Զ�������, 0-30λ��ʾ����ֵ(MIN-32K MAX-8192K)��
	public int dwVideoFrameRate;	//֡�� 0-ȫ��; 1-1/16; 2-1/8; 3-1/4; 4-1/2; 5-1; 6-2; 7-4; 8-6; 9-8; 10-10; 11-12; 12-16; 13-20, //V2.0����14-15, 15-18, 16-22;
	public short  wIntervalFrameI;  //I֡���
	//2006-08-11 ���ӵ�P֡�����ýӿڣ����Ը���ʵʱ����ʱ����
	public byte  byIntervalBPFrame;//0-BBP֡; 1-BP֡; 2-��P֡
	public byte  byENumber;//E֡����
}

    public static class NET_DVR_COMPRESSIONCFG_EX extends Structure {//ͨ��ѹ������(��չ)
	public int dwSize;
	public NET_DVR_COMPRESSION_INFO_EX struRecordPara; //¼��
	public NET_DVR_COMPRESSION_INFO_EX struNetPara;	//��
}

    public static class NET_DVR_RECCOMPRESSIONCFG_EX extends Structure {//¼��ʱ���ѹ����������(GE����)2006-09-18
	int dwSize;
	NET_DVR_COMPRESSION_INFO_EX[][]  struRecTimePara = new NET_DVR_COMPRESSION_INFO_EX[MAX_DAYS][MAX_TIMESEGMENT]; //¼��ʱ���
}

    public static class NET_DVR_RECORDSCHED extends Structure //ʱ���¼���������(�ӽṹ)
    {
        public  NET_DVR_SCHEDTIME struRecordTime = new NET_DVR_SCHEDTIME() ;
        public byte byRecordType;	//0:��ʱ¼��1:�ƶ���⣬2:����¼��3:����|������4:����&����, 5:�����, 6: ����¼��
        public byte[] reservedData = new byte[3];
    }

    public static class NET_DVR_RECORDDAY extends Structure //ȫ��¼���������(�ӽṹ)
    {
        public short wAllDayRecord;				/* �Ƿ�ȫ��¼�� 0-�� 1-��*/
        public byte byRecordType;				/* ¼������ 0:��ʱ¼��1:�ƶ���⣬2:����¼��3:����|������4:����&���� 5:�����, 6: ����¼��*/
        public byte reservedData;
    }

    public static class NET_DVR_RECORDSCHEDWEEK extends Structure
    {
       public 	NET_DVR_RECORDSCHED[] struRecordSched = new NET_DVR_RECORDSCHED[MAX_TIMESEGMENT_V30];
    }

    public static class NET_DVR_RECORD_V30 extends Structure {    //ͨ��¼���������(9000��չ)
        public int dwSize;
        public int dwRecord;  						/*�Ƿ�¼�� 0-�� 1-��*/
        public NET_DVR_RECORDDAY[] struRecAllDay = new NET_DVR_RECORDDAY[MAX_DAYS];
        public NET_DVR_RECORDSCHEDWEEK[] struRecordSched = new NET_DVR_RECORDSCHEDWEEK[MAX_DAYS];
        public int dwRecordTime;					/* ¼����ʱ���� 0-5�룬 1-20�룬 2-30�룬 3-1���ӣ� 4-2���ӣ� 5-5���ӣ� 6-10����*/
        public int dwPreRecordTime;				/* Ԥ¼ʱ�� 0-��Ԥ¼ 1-5�� 2-10�� 3-15�� 4-20�� 5-25�� 6-30�� 7-0xffffffff(������Ԥ¼) */
        public int dwRecorderDuration;				/* ¼�񱣴���ʱ�� */
        public byte byRedundancyRec;	/*�Ƿ�����¼��,��Ҫ���˫���ݣ�0/1*/
        public byte byAudioRec;		/*¼��ʱ����������ʱ�Ƿ��¼��Ƶ��ݣ������д˷���*/
        public byte[] byReserve = new byte[10];
    }

 public static class NET_DVR_RECORD extends Structure { //ͨ��¼���������
	 public int dwSize;
	 public int dwRecord;  /*�Ƿ�¼�� 0-�� 1-��*/
	 public NET_DVR_RECORDDAY[]  struRecAllDay = new NET_DVR_RECORDDAY[MAX_DAYS];
         public NET_DVR_RECORDSCHEDWEEK[] struRecordSched = new NET_DVR_RECORDSCHEDWEEK[MAX_DAYS];
	 public int dwRecordTime;	/* ¼��ʱ�䳤�� 0-5�룬 1-20�룬 2-30�룬 3-1���ӣ� 4-2���ӣ� 5-5���ӣ� 6-10����*/
	 public int dwPreRecordTime;	/* Ԥ¼ʱ�� 0-��Ԥ¼ 1-5�� 2-10�� 3-15�� 4-20�� 5-25�� 6-30�� 7-0xffffffff(������Ԥ¼) */
}

//��̨Э���ṹ����
 public static class NET_DVR_PTZ_PROTOCOL extends Structure {
       public int dwType;               /*����������ֵ����1��ʼ�������*/
       public byte[]  byDescribe = new byte[DESC_LEN]; /*���������������8000�е�һ��*/
}

 public static class NET_DVR_PTZCFG extends Structure {
       public  int   dwSize;
       public  NET_DVR_PTZ_PROTOCOL[] struPtz = new NET_DVR_PTZ_PROTOCOL[PTZ_PROTOCOL_NUM];/*���200��PTZЭ��*/
       public  int   dwPtzNum;           /*��Ч��ptzЭ����Ŀ����0��ʼ(������ʱ��1)*/
       public  byte[]  byRes = new byte[8];
}
/***************************��̨����(end)******************************/
 public static class NET_DVR_DECODERCFG_V30 extends Structure {//ͨ��������(��̨)��������(9000��չ)
	public int dwSize;
	public int dwBaudRate;       //������(bps)��0��50��1��75��2��110��3��150��4��300��5��600��6��1200��7��2400��8��4800��9��9600��10��19200�� 11��38400��12��57600��13��76800��14��115.2k;
	public byte byDataBit;         // ����м�λ 0��5λ��1��6λ��2��7λ��3��8λ;
	public byte byStopBit;         // ֹͣλ 0��1λ��1��2λ;
	public byte byParity;          // У�� 0����У�飬1����У�飬2��żУ��;
	public byte byFlowcontrol;     // 0���ޣ�1��������,2-Ӳ����
	public short wDecoderType;      //����������, 0��YouLi��1��LiLin-1016��2��LiLin-820��3��Pelco-p��4��DM DynaColor��5��HD600��6��JC-4116��7��Pelco-d WX��8��Pelco-d PICO
	public short wDecoderAddress;	/*��������ַ:0 - 255*/
	public byte[] bySetPreset = new byte[MAX_PRESET_V30];		/* Ԥ�õ��Ƿ�����,0-û������,1-����*/
	public byte[] bySetCruise = new byte[MAX_CRUISE_V30];		/* Ѳ���Ƿ�����: 0-û������,1-���� */
	public byte[] bySetTrack = new byte[MAX_TRACK_V30];		    /* �켣�Ƿ�����,0-û������,1-����*/
}

 public static class NET_DVR_DECODERCFG extends Structure {//ͨ��������(��̨)��������
	public int dwSize;
	public int dwBaudRate;       //������(bps)��0��50��1��75��2��110��3��150��4��300��5��600��6��1200��7��2400��8��4800��9��9600��10��19200�� 11��38400��12��57600��13��76800��14��115.2k;
	public byte byDataBit;         // ����м�λ 0��5λ��1��6λ��2��7λ��3��8λ;
	public byte byStopBit;         // ֹͣλ 0��1λ��1��2λ;
	public byte byParity;          // У�� 0����У�飬1����У�飬2��żУ��;
	public byte byFlowcontrol;     // 0���ޣ�1��������,2-Ӳ����
	public short wDecoderType;      //����������, 0��YouLi��1��LiLin-1016��2��LiLin-820��3��Pelco-p��4��DM DynaColor��5��HD600��6��JC-4116��7��Pelco-d WX��8��Pelco-d PICO
	public short wDecoderAddress;	/*��������ַ:0 - 255*/
	public byte[] bySetPreset = new byte[MAX_PRESET];		/* Ԥ�õ��Ƿ�����,0-û������,1-����*/
	public byte[] bySetCruise = new byte[MAX_CRUISE];		/* Ѳ���Ƿ�����: 0-û������,1-���� */
	public byte[] bySetTrack = new byte[MAX_TRACK];		    /* �켣�Ƿ�����,0-û������,1-����*/
}

public static class NET_DVR_PPPCFG_V30 extends Structure {//ppp��������(�ӽṹ)
	public NET_DVR_IPADDR struRemoteIP;	//Զ��IP��ַ
	public NET_DVR_IPADDR struLocalIP;		//����IP��ַ
	public byte[] sLocalIPMask = new byte[16];			//����IP��ַ����
	public byte[] sUsername = new byte[NAME_LEN];		/* �û��� */
	public byte[] sPassword = new byte[PASSWD_LEN];		/* ���� */
	public byte byPPPMode;					//PPPģʽ, 0��������1������
	public byte byRedial;					//�Ƿ�ز� ��0-��,1-��
	public byte byRedialMode;				//�ز�ģʽ,0-�ɲ�����ָ��,1-Ԥ�ûز�����
	public byte byDataEncrypt;				//��ݼ���,0-��,1-��
	public int dwMTU;					//MTU
	public byte[] sTelephoneNumber = new byte[PHONENUMBER_LEN];   //�绰����
}

public static class NET_DVR_PPPCFG extends Structure {//ppp��������(�ӽṹ)
	public byte[] sRemoteIP = new byte[16];				//Զ��IP��ַ
	public byte[] sLocalIP = new byte[16];				//����IP��ַ
	public byte[] sLocalIPMask = new byte[16];			//����IP��ַ����
	public byte[] sUsername = new byte[NAME_LEN];		/* �û��� */
	public byte[] sPassword = new byte[PASSWD_LEN];		/* ���� */
	public byte byPPPMode;					//PPPģʽ, 0��������1������
	public byte byRedial;					//�Ƿ�ز� ��0-��,1-��
	public byte byRedialMode;				//�ز�ģʽ,0-�ɲ�����ָ��,1-Ԥ�ûز�����
	public byte byDataEncrypt;				//��ݼ���,0-��,1-��
	public int dwMTU;					//MTU
	public byte[] sTelephoneNumber = new byte[PHONENUMBER_LEN];   //�绰����
}


public static class NET_DVR_SINGLE_RS232 extends Structure {//RS232���ڲ�������(9000��չ)
       public int dwBaudRate;   /*������(bps)��0��50��1��75��2��110��3��150��4��300��5��600��6��1200��7��2400��8��4800��9��9600��10��19200�� 11��38400��12��57600��13��76800��14��115.2k;*/
       public byte byDataBit;     /* ����м�λ 0��5λ��1��6λ��2��7λ��3��8λ */
       public byte byStopBit;     /* ֹͣλ 0��1λ��1��2λ */
       public byte byParity;      /* У�� 0����У�飬1����У�飬2��żУ�� */
       public byte byFlowcontrol; /* 0���ޣ�1��������,2-Ӳ���� */
       public int dwWorkMode;   /* ����ģʽ��0��232��������PPP���ţ�1��232�������ڲ�����ƣ�2��͸��ͨ�� */
}

public static class NET_DVR_RS232CFG_V30 extends Structure {//RS232���ڲ�������(9000��չ)
	public int dwSize;
        public NET_DVR_SINGLE_RS232 struRs232;/*Ŀǰֻ�е�һ������������Ч�������豸��ֻ֧��һ�����ڣ������߸�����*/
	public byte[] byRes = new byte[84];
        public NET_DVR_PPPCFG_V30 struPPPConfig;/*ppp����*/
}

public static class NET_DVR_RS232CFG extends Structure {//RS232���ڲ�������
	public int dwSize;
	public int dwBaudRate;//������(bps)��0��50��1��75��2��110��3��150��4��300��5��600��6��1200��7��2400��8��4800��9��9600��10��19200�� 11��38400��12��57600��13��76800��14��115.2k;
	public byte byDataBit;// ����м�λ 0��5λ��1��6λ��2��7λ��3��8λ;
	public byte byStopBit;// ֹͣλ 0��1λ��1��2λ;
	public byte byParity;// У�� 0����У�飬1����У�飬2��żУ��;
	public byte byFlowcontrol;// 0���ޣ�1��������,2-Ӳ����
	public int dwWorkMode;// ����ģʽ��0��խ����(232��������PPP����)��1������̨(232�������ڲ������)��2��͸��ͨ��
	public NET_DVR_PPPCFG struPPPConfig;
}

public static class NET_DVR_ALARMINCFG_V30 extends Structure {//���������������(9000��չ)
        public 	int dwSize;
        public 	byte[] sAlarmInName = new byte[NAME_LEN];	/* ��� */
        public 	byte byAlarmType;	            //����������,0������,1������
        public 	byte byAlarmInHandle;	        /* �Ƿ��� 0-������ 1-����*/
        public    byte[] reservedData = new byte[2];
	public NET_DVR_HANDLEEXCEPTION_V30 struAlarmHandleType;	/* ���?ʽ */
	public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS];//����ʱ��
	public byte[] byRelRecordChan = new byte[MAX_CHANNUM_V30]; //����������¼��ͨ��,Ϊ1��ʾ������ͨ��
	public byte[] byEnablePreset = new byte[MAX_CHANNUM_V30];		/* �Ƿ����Ԥ�õ� 0-��,1-��*/
	public byte[] byPresetNo = new byte[MAX_CHANNUM_V30];			/* ���õ���̨Ԥ�õ����,һ������������Ե��ö��ͨ������̨Ԥ�õ�, 0xff��ʾ������Ԥ�õ㡣*/
	public byte[] byEnablePresetRevert = new byte[MAX_CHANNUM_V30]; /* �Ƿ�ָ�������Ԥ�õ�ǰ��λ��(����) */
	public short[] wPresetRevertDelay = new short[MAX_CHANNUM_V30];	/* �ָ�Ԥ�õ���ʱ(����) */
	public byte[] byEnableCruise = new byte[MAX_CHANNUM_V30];		/* �Ƿ����Ѳ�� 0-��,1-��*/
	public byte[] byCruiseNo = new byte[MAX_CHANNUM_V30];			/* Ѳ�� */
	public byte[] byEnablePtzTrack = new byte[MAX_CHANNUM_V30];		/* �Ƿ���ù켣 0-��,1-��*/
	public byte[] byPTZTrack = new byte[MAX_CHANNUM_V30];			/* ���õ���̨�Ĺ켣��� */
        public   byte[] byRes = new byte[16];
}

public static class NET_DVR_ALARMINCFG extends Structure {//���������������
	public int dwSize;
	public byte[] sAlarmInName = new byte[NAME_LEN];	/* ��� */
	public byte byAlarmType;	//����������,0������,1������
	public byte byAlarmInHandle;	/* �Ƿ��� 0-������ 1-����*/
	public NET_DVR_HANDLEEXCEPTION struAlarmHandleType;	/* ���?ʽ */
        public  NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS];//����ʱ��
	public byte[] byRelRecordChan = new byte[MAX_CHANNUM]; //����������¼��ͨ��,Ϊ1��ʾ������ͨ��
	public byte[] byEnablePreset = new byte[MAX_CHANNUM];		/* �Ƿ����Ԥ�õ� 0-��,1-��*/
	public byte[] byPresetNo = new byte[MAX_CHANNUM];			/* ���õ���̨Ԥ�õ����,һ������������Ե��ö��ͨ������̨Ԥ�õ�, 0xff��ʾ������Ԥ�õ㡣*/
	public byte[] byEnableCruise = new byte[MAX_CHANNUM];		/* �Ƿ����Ѳ�� 0-��,1-��*/
	public byte[] byCruiseNo = new byte[MAX_CHANNUM];			/* Ѳ�� */
	public byte[] byEnablePtzTrack = new byte[MAX_CHANNUM];		/* �Ƿ���ù켣 0-��,1-��*/
	public byte[] byPTZTrack = new byte[MAX_CHANNUM];			/* ���õ���̨�Ĺ켣��� */
}

public static class NET_DVR_ADDIT_POSITION extends Structure {//����GPS��Ϣ�ṹ(2007-12-27)
	public byte[]	sDevName = new byte[32];		/* �豸��� */
	public int	dwSpeed;			/*�ٶ�*/
	public int	dwLongitude;		/* ����*/
	public int	dwLatitude;       /* γ��*/
	public byte[]	direction = new byte[2];   /* direction[0]:'E'or'W'(����/����), direction[1]:'N'or'S'(��γ/��γ) */
	public byte[]  res = new byte[2];              /* ����λ */
}

public static class NET_DVR_ALARMINFO_V30 extends Structure {//�ϴ�������Ϣ(9000��չ)
	public int dwAlarmType;/*0-�ź�������,1-Ӳ����,2-�źŶ�ʧ,3���ƶ����,4��Ӳ��δ��ʽ��,5-��дӲ�̳���,6-�ڵ�����,7-��ʽ��ƥ��, 8-�Ƿ�����, 0xa-GPS��λ��Ϣ(���ض���)*/
	public int dwAlarmInputNumber;/*��������˿�*/
	public byte[]  byAlarmOutputNumber = new byte[MAX_ALARMOUT_V30];/*����������˿ڣ�Ϊ1��ʾ��Ӧ���*/
	public byte[]  byAlarmRelateChannel= new byte[MAX_CHANNUM_V30];/*������¼��ͨ����Ϊ1��ʾ��Ӧ¼��, dwAlarmRelateChannel[0]��Ӧ��1��ͨ��*/
	public byte[]  byChannel= new byte[MAX_CHANNUM_V30];/*dwAlarmTypeΪ2��3,6ʱ����ʾ�ĸ�ͨ����dwChannel[0]��Ӧ��1��ͨ��*/
	public byte[]  byDiskNumber= new byte[MAX_DISKNUM_V30];/*dwAlarmTypeΪ1,4,5ʱ,��ʾ�ĸ�Ӳ��, dwDiskNumber[0]��Ӧ��1��Ӳ��*/
}

public static class NET_DVR_ALARMINFO extends Structure {
	public int dwAlarmType;/*0-�ź�������,1-Ӳ����,2-�źŶ�ʧ,3���ƶ����,4��Ӳ��δ��ʽ��,5-��дӲ�̳���,6-�ڵ�����,7-��ʽ��ƥ��, 8-�Ƿ�����, 9-����״̬, 0xa-GPS��λ��Ϣ(���ض���)*/
	public int dwAlarmInputNumber;/*��������˿�, ����������Ϊ9ʱ�ñ�����ʾ����״̬0��ʾ�� -1��ʾ����*/
	public int[] dwAlarmOutputNumber = new int[MAX_ALARMOUT];/*����������˿ڣ�Ϊ1��ʾ��Ӧ��һ�����*/
	public int[] dwAlarmRelateChannel = new int[MAX_CHANNUM];/*������¼��ͨ����dwAlarmRelateChannel[0]Ϊ1��ʾ��1��ͨ��¼��*/
	public int[] dwChannel = new int[MAX_CHANNUM];/*dwAlarmTypeΪ2��3,6ʱ����ʾ�ĸ�ͨ����dwChannel[0]λ��Ӧ��1��ͨ��*/
	public int[] dwDiskNumber = new int[MAX_DISKNUM];/*dwAlarmTypeΪ1,4,5ʱ,��ʾ�ĸ�Ӳ��, dwDiskNumber[0]λ��Ӧ��1��Ӳ��*/
}

public static class NET_DVR_ALARMINFO_EX extends Structure {//�ϴ�������Ϣ(���ݾ��춨�� 2006-07-28)
	public int dwAlarmType;/*0-�ź�������,1-Ӳ����,2-�źŶ�ʧ,3���ƶ����,4��Ӳ��δ��ʽ��,5-��дӲ�̳���,6-�ڵ�����,7-��ʽ��ƥ��, 8-�Ƿ�����*/
	public int dwAlarmInputNumber;/*��������˿�*/
	public int[] dwAlarmOutputNumber = new int[MAX_ALARMOUT];/*��������˿ڶ�Ӧ������˿ڣ���һλΪ1��ʾ��Ӧ��һ�����*/
	public int[] dwAlarmRelateChannel = new int[MAX_CHANNUM];/*��������˿ڶ�Ӧ��¼��ͨ������һλΪ1��ʾ��Ӧ��һ·¼��,dwAlarmRelateChannel[0]��Ӧ��1��ͨ��*/
	public int[] dwChannel = new int[MAX_CHANNUM];/*dwAlarmTypeΪ2��3,6ʱ����ʾ�ĸ�ͨ����dwChannel[0]λ��Ӧ��0��ͨ��*/
	public int[] dwDiskNumber = new int[MAX_DISKNUM];/*dwAlarmTypeΪ1,4,5ʱ,��ʾ�ĸ�Ӳ��*/
	public byte[] sSerialNumber = new byte[SERIALNO_LEN];  //���к�
	public byte[]  sRemoteAlarmIP = new byte[16];			//Զ�̱���IP��ַ��
}

//////////////////////////////////////////////////////////////////////////////////////
//IPC�����������
public static class NET_DVR_IPDEVINFO extends Structure {/* IP�豸�ṹ */
       public   int dwEnable;				    /* ��IP�豸�Ƿ����� */
       public   byte[] sUserName = new byte[NAME_LEN];		/* �û��� */
       public   byte[] sPassword = new byte[PASSWD_LEN];	    /* ���� */
       public   NET_DVR_IPADDR struIP = new NET_DVR_IPADDR();			/* IP��ַ */
       public   short wDVRPort;			 	    /* �˿ں� */
       public   byte[] byres = new byte[34];				/* ���� */
}

public static class NET_DVR_IPCHANINFO extends Structure {/* IPͨ��ƥ����� */
       public   byte byEnable;					/* ��ͨ���Ƿ����� */
       public  byte byIPID;					/* IP�豸ID ȡֵ1- MAX_IP_DEVICE */
       public  byte byChannel;					/* ͨ���� */
       public   byte[] byres = new byte[33];					/* ���� */
}

public static class NET_DVR_IPPARACFG extends Structure {/* IP�������ýṹ */
       public  int dwSize;			                            /* �ṹ��С */
       public  NET_DVR_IPDEVINFO[]  struIPDevInfo = new NET_DVR_IPDEVINFO[MAX_IP_DEVICE];    /* IP�豸 */
       public   byte[] byAnalogChanEnable = new byte[MAX_ANALOG_CHANNUM];        /* ģ��ͨ���Ƿ����ã��ӵ͵��߱�ʾ1-32ͨ����0��ʾ��Ч 1��Ч */
       public NET_DVR_IPCHANINFO[] struIPChanInfo = new NET_DVR_IPCHANINFO[MAX_IP_CHANNEL];	/* IPͨ�� */
}

public static class NET_DVR_IPALARMOUTINFO extends Structure {/* ����������� */
       public  byte byIPID;					/* IP�豸IDȡֵ1- MAX_IP_DEVICE */
       public  byte byAlarmOut;				/* ��������� */
       public  byte[] byRes = new byte[18];					/* ���� */
}

public static class NET_DVR_IPALARMOUTCFG extends Structure {/* IP����������ýṹ */
       public  int dwSize;			                        /* �ṹ��С */
       public  NET_DVR_IPALARMOUTINFO[] struIPAlarmOutInfo = new NET_DVR_IPALARMOUTINFO[MAX_IP_ALARMOUT];/* IP������� */
}

public static class NET_DVR_IPALARMININFO extends Structure {/* ����������� */
       public  byte byIPID;					/* IP�豸IDȡֵ1- MAX_IP_DEVICE */
       public  byte byAlarmIn;					/* ��������� */
       public  byte[] byRes = new byte[18];					/* ���� */
}

public static class NET_DVR_IPALARMINCFG extends Structure {/* IP�����������ýṹ */
       public  int dwSize;			                        /* �ṹ��С */
       public NET_DVR_IPALARMININFO[] struIPAlarmInInfo = new NET_DVR_IPALARMININFO[MAX_IP_ALARMIN];/* IP�������� */
}

public static class NET_DVR_IPALARMINFO extends Structure {//ipc alarm info
       public  NET_DVR_IPDEVINFO[]  struIPDevInfo = new NET_DVR_IPDEVINFO[MAX_IP_DEVICE];            /* IP�豸 */
       public  byte[] byAnalogChanEnable = new byte[MAX_ANALOG_CHANNUM];                /* ģ��ͨ���Ƿ����ã�0-δ���� 1-���� */
       public  NET_DVR_IPCHANINFO[] struIPChanInfo = new NET_DVR_IPCHANINFO[MAX_IP_CHANNEL];	        /* IPͨ�� */
       public NET_DVR_IPALARMININFO[] struIPAlarmInInfo = new NET_DVR_IPALARMININFO[MAX_IP_ALARMIN];    /* IP�������� */
       public NET_DVR_IPALARMOUTINFO[] struIPAlarmOutInfo = new NET_DVR_IPALARMOUTINFO[MAX_IP_ALARMOUT]; /* IP������� */
}

public static class NET_DVR_SINGLE_HD extends Structure {//����Ӳ����Ϣ����
       public int dwHDNo;         /*Ӳ�̺�, ȡֵ0~MAX_DISKNUM_V30-1*/
       public int dwCapacity;     /*Ӳ������(��������)*/
       public int dwFreeSpace;    /*Ӳ��ʣ��ռ�(��������)*/
       public int dwHdStatus;     /*Ӳ��״̬(��������) 0-��, 1-δ��ʽ��, 2-����, 3-SMART״̬, 4-��ƥ��, 5-����*/
       public byte  byHDAttr;       /*0-Ĭ��, 1-����; 2-ֻ��*/
       public byte[]  byRes1 = new byte[3];
       public  int dwHdGroup;      /*�����ĸ����� 1-MAX_HD_GROUP*/
       public  byte[]  byRes2 = new byte[120];
}

public static class NET_DVR_HDCFG extends Structure {
       public  int dwSize;
       public  int dwHDCount;          /*Ӳ����(��������)*/
       public  NET_DVR_SINGLE_HD[] struHDInfo = new NET_DVR_SINGLE_HD[MAX_DISKNUM_V30];//Ӳ����ز�������Ҫ����������Ч��
}

public static class NET_DVR_SINGLE_HDGROUP extends Structure {//����������Ϣ����
       public  int dwHDGroupNo;       /*�����(��������) 1-MAX_HD_GROUP*/
       public  byte[] byHDGroupChans = new byte[64]; /*�����Ӧ��¼��ͨ��, 0-��ʾ��ͨ����¼�󵽸����飬1-��ʾ¼�󵽸�����*/
       public  byte[] byRes = new byte[8];
}

public static class NET_DVR_HDGROUP_CFG extends Structure {
       public int dwSize;
       public  int dwHDGroupCount;        /*��������(��������)*/
       public  NET_DVR_SINGLE_HDGROUP[] struHDGroupAttr = new NET_DVR_SINGLE_HDGROUP[MAX_HD_GROUP];//Ӳ����ز�������Ҫ����������Ч��
}

public static class NET_DVR_SCALECFG extends Structure {//�������Ų���Ľṹ
       public  int dwSize;
       public  int dwMajorScale;    /* ����ʾ 0-�����ţ�1-����*/
       public  int dwMinorScale;    /* ����ʾ 0-�����ţ�1-����*/
       public  int[] dwRes = new int[2];
}

public static class NET_DVR_ALARMOUTCFG_V30 extends Structure {//DVR�������(9000��չ)
	public int dwSize;
	public byte[] sAlarmOutName = new byte[NAME_LEN];	/* ��� */
	public int dwAlarmOutDelay;	/* �������ʱ��(-1Ϊ���ޣ��ֶ��ر�) */
	//0-5��,1-10��,2-30��,3-1����,4-2����,5-5����,6-10����,7-�ֶ�
	public NET_DVR_SCHEDTIMEWEEK[] struAlarmOutTime= new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS];/* �����������ʱ��� */
        public     byte[] byRes = new byte[16];
}

public static class NET_DVR_ALARMOUTCFG extends Structure {//DVR�������
	public int dwSize;
	public byte[] sAlarmOutName = new byte[NAME_LEN];	/* ��� */
	public int dwAlarmOutDelay;	/* �������ʱ��(-1Ϊ���ޣ��ֶ��ر�) */
	//0-5��,1-10��,2-30��,3-1����,4-2����,5-5����,6-10����,7-�ֶ�
	public NET_DVR_SCHEDTIMEWEEK[] struAlarmOutTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS];/* �����������ʱ��� */
}

public static class NET_DVR_PREVIEWCFG_V30 extends Structure {//DVR����Ԥ������(9000��չ)
       public  int dwSize;
       public  byte byPreviewNumber;//Ԥ����Ŀ,0-1����,1-4����,2-9����,3-16����, 4-6����, 5-8����, 0xff:�����
       public  byte byEnableAudio;//�Ƿ�����Ԥ��,0-��Ԥ��,1-Ԥ��
       public  short wSwitchTime;//�л�ʱ��,0-���л�,1-5s,2-10s,3-20s,4-30s,5-60s,6-120s,7-300s
       public  byte[][] bySwitchSeq = new byte[MAX_PREVIEW_MODE][MAX_WINDOW_V30];//�л�˳��,���lSwitchSeq[i]Ϊ 0xff��ʾ����
       public  byte[] byRes = new byte[24];
}

public static class NET_DVR_PREVIEWCFG extends Structure {//DVR����Ԥ������
	public int dwSize;
	public byte byPreviewNumber;//Ԥ����Ŀ,0-1����,1-4����,2-9����,3-16����,0xff:�����
	public byte byEnableAudio;//�Ƿ�����Ԥ��,0-��Ԥ��,1-Ԥ��
	public short wSwitchTime;//�л�ʱ��,0-���л�,1-5s,2-10s,3-20s,4-30s,5-60s,6-120s,7-300s
	public byte[] bySwitchSeq = new byte[MAX_WINDOW];//�л�˳��,���lSwitchSeq[i]Ϊ 0xff��ʾ����
}

public static class NET_DVR_VGAPARA extends Structure {//DVR��Ƶ���
	public short wResolution;							/* �ֱ��� */
	public short wFreq;									/* ˢ��Ƶ�� */
	public int dwBrightness;							/* ���� */
}

/*
* MATRIX�������ṹ
*/
public static class NET_DVR_MATRIXPARA_V30 extends Structure {
	public short[]	wOrder = new short[MAX_ANALOG_CHANNUM];		/* Ԥ��˳��, 0xff��ʾ��Ӧ�Ĵ��ڲ�Ԥ�� */
	public short	wSwitchTime;				    /* Ԥ���л�ʱ�� */
	public byte[]	res = new byte[14];
}

public static class NET_DVR_MATRIXPARA extends Structure {
	public short wDisplayLogo;						/* ��ʾ��Ƶͨ����(����) */
	public short wDisplayOsd;						/* ��ʾʱ��(����) */
}

public static class NET_DVR_VOOUT extends Structure {
	public byte byVideoFormat;						/* �����ʽ,0-PAL,1-NTSC */
	public byte byMenuAlphaValue;					/* �˵��뱳��ͼ��Աȶ� */
	public short wScreenSaveTime;					/* ��Ļ����ʱ�� 0-�Ӳ�,1-1����,2-2����,3-5����,4-10����,5-20����,6-30���� */
	public short wVOffset;							/* ��Ƶ���ƫ�� */
	public short wBrightness;						/* ��Ƶ������� */
	public byte byStartMode;						/* ��������Ƶ���ģʽ(0:�˵�,1:Ԥ��)*/
	public byte byEnableScaler;                    /* �Ƿ��������� (0-������, 1-����)*/
}

public static class NET_DVR_VIDEOOUT_V30 extends Structure {//DVR��Ƶ���(9000��չ)
	public int dwSize;
	public NET_DVR_VOOUT[] struVOOut = new NET_DVR_VOOUT[MAX_VIDEOOUT_V30];
	public NET_DVR_VGAPARA[] struVGAPara = new NET_DVR_VGAPARA[MAX_VGA_V30];	/* VGA���� */
	public NET_DVR_MATRIXPARA_V30[] struMatrixPara = new NET_DVR_MATRIXPARA_V30[MAX_MATRIXOUT];		/* MATRIX���� */
        public   byte[] byRes = new byte[16];
}

public static class NET_DVR_VIDEOOUT extends Structure {//DVR��Ƶ���
	public int dwSize;
	public NET_DVR_VOOUT[] struVOOut = new NET_DVR_VOOUT[MAX_VIDEOOUT];
	public NET_DVR_VGAPARA[] struVGAPara = new NET_DVR_VGAPARA[MAX_VGA];	/* VGA���� */
	public NET_DVR_MATRIXPARA struMatrixPara;		/* MATRIX���� */
}

public static class NET_DVR_USER_INFO_V30 extends Structure {//���û�����(�ӽṹ)(9000��չ)
	public byte[] sUserName = new byte[NAME_LEN];		/* �û��� */
	public byte[] sPassword = new byte[PASSWD_LEN];		/* ���� */
        public byte[] byLocalRight = new byte[MAX_RIGHT];	/* ����Ȩ�� */
	/*����0: ���ؿ�����̨*/
	/*����1: �����ֶ�¼��*/
	/*����2: ���ػط�*/
	/*����3: �������ò���*/
	/*����4: ���ز鿴״̬����־*/
	/*����5: ���ظ߼�����(���ʽ�����������ػ�)*/
        /*����6: ���ز鿴���� */
        /*����7: ���ع���ģ���IP camera */
        /*����8: ���ر��� */
        /*����9: ���عػ�/���� */
	public byte[] byRemoteRight = new byte[MAX_RIGHT];/* Զ��Ȩ�� */
	/*����0: Զ�̿�����̨*/
	/*����1: Զ���ֶ�¼��*/
	/*����2: Զ�̻ط� */
	/*����3: Զ�����ò���*/
	/*����4: Զ�̲鿴״̬����־*/
	/*����5: Զ�̸߼�����(���ʽ�����������ػ�)*/
	/*����6: Զ�̷��������Խ�*/
	/*����7: Զ��Ԥ��*/
	/*����8: Զ�����󱨾��ϴ����������*/
	/*����9: Զ�̿��ƣ��������*/
	/*����10: Զ�̿��ƴ���*/
        /*����11: Զ�̲鿴���� */
        /*����12: Զ�̹���ģ���IP camera */
        /*����13: Զ�̹ػ�/���� */
	public byte[] byNetPreviewRight = new byte[MAX_CHANNUM_V30];		/* Զ�̿���Ԥ����ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public byte[] byLocalPlaybackRight = new byte[MAX_CHANNUM_V30];	    /* ���ؿ��Իطŵ�ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public byte[] byNetPlaybackRight = new byte[MAX_CHANNUM_V30];	    /* Զ�̿��Իطŵ�ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public byte[] byLocalRecordRight = new byte[MAX_CHANNUM_V30];		/* ���ؿ���¼���ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public byte[] byNetRecordRight = new byte[MAX_CHANNUM_V30];		    /* Զ�̿���¼���ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public byte[] byLocalPTZRight = new byte[MAX_CHANNUM_V30];		    /* ���ؿ���PTZ��ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public byte[] byNetPTZRight = new byte[MAX_CHANNUM_V30];			/* Զ�̿���PTZ��ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public byte[] byLocalBackupRight = new byte[MAX_CHANNUM_V30];		/* ���ر���Ȩ��ͨ�� 0-��Ȩ�ޣ�1-��Ȩ��*/
	public NET_DVR_IPADDR struUserIP;		/* �û�IP��ַ(Ϊ0ʱ��ʾ�����κε�ַ) */
	public byte[] byMACAddr = new byte[MACADDR_LEN];	/* �����ַ */
	public byte byPriority;				/* ���ȼ���0xff-�ޣ�0--�ͣ�1--�У�2--�� */
                                    /*
                                    �ޡ�����ʾ��֧�����ȼ�������
                                    �͡���Ĭ��Ȩ��:�������غ�Զ�̻ط�,���غ�Զ�̲鿴��־��״̬,���غ�Զ�̹ػ�/����
                                    �С����������غ�Զ�̿�����̨,���غ�Զ���ֶ�¼��,���غ�Զ�̻ط�,�����Խ���Զ��Ԥ��
                                          ���ر���,����/Զ�̹ػ�/����
                                    �ߡ�������Ա
                                    */
	public byte[] byRes = new byte[17];
}

public static class NET_DVR_USER_INFO_EX extends Structure {//���û�����(SDK_V15��չ)(�ӽṹ)
	public byte[] sUserName = new byte[NAME_LEN];		/* �û��� */
	public byte[] sPassword = new byte[PASSWD_LEN];		/* ���� */
	public int[] dwLocalRight = new int[MAX_RIGHT];	/* Ȩ�� */
	/*����0: ���ؿ�����̨*/
	/*����1: �����ֶ�¼��*/
	/*����2: ���ػط�*/
	/*����3: �������ò���*/
	/*����4: ���ز鿴״̬����־*/
	/*����5: ���ظ߼�����(���ʽ�����������ػ�)*/
	public int dwLocalPlaybackRight;		/* ���ؿ��Իطŵ�ͨ�� bit0 -- channel 1*/
	public int[] dwRemoteRight = new int[MAX_RIGHT];	/* Ȩ�� */
	/*����0: Զ�̿�����̨*/
	/*����1: Զ���ֶ�¼��*/
	/*����2: Զ�̻ط� */
	/*����3: Զ�����ò���*/
	/*����4: Զ�̲鿴״̬����־*/
	/*����5: Զ�̸߼�����(���ʽ�����������ػ�)*/
	/*����6: Զ�̷��������Խ�*/
	/*����7: Զ��Ԥ��*/
	/*����8: Զ�����󱨾��ϴ����������*/
	/*����9: Զ�̿��ƣ��������*/
	/*����10: Զ�̿��ƴ���*/
	public int dwNetPreviewRight;		/* Զ�̿���Ԥ����ͨ�� bit0 -- channel 1*/
	public int dwNetPlaybackRight;		/* Զ�̿��Իطŵ�ͨ�� bit0 -- channel 1*/
	public byte[] sUserIP = new byte[16];				/* �û�IP��ַ(Ϊ0ʱ��ʾ�����κε�ַ) */
	public byte[] byMACAddr = new byte[MACADDR_LEN];	/* �����ַ */
}

public static class NET_DVR_USER_INFO extends Structure {//���û�����(�ӽṹ)
	public byte[] sUserName = new byte[NAME_LEN];		/* �û��� */
	public byte[] sPassword = new byte[PASSWD_LEN];		/* ���� */
	public int[] dwLocalRight = new int[MAX_RIGHT];	/* Ȩ�� */
	/*����0: ���ؿ�����̨*/
	/*����1: �����ֶ�¼��*/
	/*����2: ���ػط�*/
	/*����3: �������ò���*/
	/*����4: ���ز鿴״̬����־*/
	/*����5: ���ظ߼�����(���ʽ�����������ػ�)*/
	public int[] dwRemoteRight = new int[MAX_RIGHT];	/* Ȩ�� */
	/*����0: Զ�̿�����̨*/
	/*����1: Զ���ֶ�¼��*/
	/*����2: Զ�̻ط� */
	/*����3: Զ�����ò���*/
	/*����4: Զ�̲鿴״̬����־*/
	/*����5: Զ�̸߼�����(���ʽ�����������ػ�)*/
	/*����6: Զ�̷��������Խ�*/
	/*����7: Զ��Ԥ��*/
	/*����8: Զ�����󱨾��ϴ����������*/
	/*����9: Զ�̿��ƣ��������*/
	/*����10: Զ�̿��ƴ���*/
	public byte[] sUserIP = new byte[16];				/* �û�IP��ַ(Ϊ0ʱ��ʾ�����κε�ַ) */
	public byte[] byMACAddr = new byte[MACADDR_LEN];	/* �����ַ */
}

public static class NET_DVR_USER_V30 extends Structure {//DVR�û�����(9000��չ)
	public int dwSize;
	public NET_DVR_USER_INFO_V30[] struUser = new NET_DVR_USER_INFO_V30[MAX_USERNUM_V30];
}

public static class NET_DVR_USER_EX extends Structure {//DVR�û�����(SDK_V15��չ)
	public int dwSize;
	public NET_DVR_USER_INFO_EX[] struUser = new NET_DVR_USER_INFO_EX[MAX_USERNUM];
}

public static class NET_DVR_USER extends Structure {//DVR�û�����
	public int dwSize;
	public NET_DVR_USER_INFO[] struUser = new NET_DVR_USER_INFO[MAX_USERNUM];
}

public static class NET_DVR_EXCEPTION_V30 extends Structure {//DVR�쳣����(9000��չ)
	public int dwSize;
	public NET_DVR_HANDLEEXCEPTION_V30[] struExceptionHandleType = new NET_DVR_HANDLEEXCEPTION_V30[MAX_EXCEPTIONNUM_V30];
	/*����0-����,1- Ӳ�̳���,2-���߶�,3-��������IP ��ַ��ͻ,4-�Ƿ�����, 5-����/�����Ƶ��ʽ��ƥ��, 6-�г�����(����ר��), 7-��Ƶ�ź��쳣(9000)*/
}

public static class NET_DVR_EXCEPTION extends Structure {//DVR�쳣����
	public int dwSize;
	public NET_DVR_HANDLEEXCEPTION[] struExceptionHandleType = new NET_DVR_HANDLEEXCEPTION[MAX_EXCEPTIONNUM];
	/*����0-����,1- Ӳ�̳���,2-���߶�,3-��������IP ��ַ��ͻ,4-�Ƿ�����, 5-����/�����Ƶ��ʽ��ƥ��, 6-�г�����(����ר��)*/
}

public static class NET_DVR_CHANNELSTATE_V30 extends Structure {//ͨ��״̬(9000��չ)
	public byte byRecordStatic; //ͨ���Ƿ���¼��,0-��¼��,1-¼��
	public byte bySignalStatic; //���ӵ��ź�״̬,0-��,1-�źŶ�ʧ
	public byte byHardwareStatic;//ͨ��Ӳ��״̬,0-��,1-�쳣,����DSP����
	public byte reservedData;		//����
	public int dwBitRate;//ʵ������
	public int dwLinkNum;//�ͻ������ӵĸ���
	public NET_DVR_IPADDR[] struClientIP = new NET_DVR_IPADDR[MAX_LINK];//�ͻ��˵�IP��ַ
        public  int dwIPLinkNum;//����ͨ��ΪIP���룬��ô��ʾIP���뵱ǰ��������
        public  byte[] byRes = new byte[12];
}

public static class NET_DVR_CHANNELSTATE extends Structure {//ͨ��״̬
	public byte byRecordStatic; //ͨ���Ƿ���¼��,0-��¼��,1-¼��
	public byte bySignalStatic; //���ӵ��ź�״̬,0-��,1-�źŶ�ʧ
	public byte byHardwareStatic;//ͨ��Ӳ��״̬,0-��,1-�쳣,����DSP����
	public byte reservedData;		//����
	public int dwBitRate;//ʵ������
	public int dwLinkNum;//�ͻ������ӵĸ���
	public int[] dwClientIP = new int[MAX_LINK];//�ͻ��˵�IP��ַ
}

public static class NET_DVR_DISKSTATE extends Structure {//Ӳ��״̬
	public int dwVolume;//Ӳ�̵�����
	public int dwFreeSpace;//Ӳ�̵�ʣ��ռ�
	public int dwHardDiskStatic; //Ӳ�̵�״̬,��λ:1-����,2-����,3-����Ӳ�̳���
}

public static class NET_DVR_WORKSTATE_V30 extends Structure {//DVR����״̬(9000��չ)
	public int dwDeviceStatic; 	//�豸��״̬,0-��,1-CPUռ����̫��,����85%,2-Ӳ������,���紮������
	public NET_DVR_DISKSTATE[]  struHardDiskStatic = new NET_DVR_DISKSTATE[MAX_DISKNUM_V30];
	public NET_DVR_CHANNELSTATE_V30[] struChanStatic = new NET_DVR_CHANNELSTATE_V30[MAX_CHANNUM_V30];//ͨ����״̬
	public byte[]  byAlarmInStatic = new byte[MAX_ALARMIN_V30]; //�����˿ڵ�״̬,0-û�б���,1-�б���
	public byte[]  byAlarmOutStatic = new byte[MAX_ALARMOUT_V30]; //��������˿ڵ�״̬,0-û�����,1-�б������
	public int  dwLocalDisplay;//������ʾ״̬,0-��,1-����
        public  byte [] byAudioChanStatus = new byte[MAX_AUDIO_V30];//��ʾ����ͨ����״̬ 0-δʹ�ã�1-ʹ����, 0xff��Ч
        public  byte[]  byRes = new byte[10];
}

public static class NET_DVR_WORKSTATE extends Structure {//DVR����״̬
	public int dwDeviceStatic; 	//�豸��״̬,0-��,1-CPUռ����̫��,����85%,2-Ӳ������,���紮������
	public NET_DVR_DISKSTATE[]  struHardDiskStatic = new NET_DVR_DISKSTATE[MAX_DISKNUM];
	public NET_DVR_CHANNELSTATE[] struChanStatic = new NET_DVR_CHANNELSTATE[MAX_CHANNUM];//ͨ����״̬
	public byte[]  byAlarmInStatic = new byte[MAX_ALARMIN]; //�����˿ڵ�״̬,0-û�б���,1-�б���
	public byte[]  byAlarmOutStatic = new byte[MAX_ALARMOUT]; //��������˿ڵ�״̬,0-û�����,1-�б������
	public int  dwLocalDisplay;//������ʾ״̬,0-��,1-����
}

public static class NET_DVR_LOG_V30 extends Structure {//��־��Ϣ(9000��չ)
	public NET_DVR_TIME strLogTime;
	public int	dwMajorType;	//������ 1-����; 2-�쳣; 3-����; 0xff-ȫ��
	public int	dwMinorType;//������ 0-ȫ��;
	public byte[]	sPanelUser = new byte[MAX_NAMELEN]; //���������û���
	public byte[]	sNetUser = new byte[MAX_NAMELEN];//����������û���
	public NET_DVR_IPADDR	struRemoteHostAddr;//Զ�������ַ
	public int	dwParaType;//��������
	public int	dwChannel;//ͨ����
	public int	dwDiskNumber;//Ӳ�̺�
	public int	dwAlarmInPort;//��������˿�
	public int	dwAlarmOutPort;//��������˿�
       public  int     dwInfoLen;
       public  byte[]    sInfo = new byte[LOG_INFO_LEN];
}

//��־��Ϣ
public static class NET_DVR_LOG extends Structure {
	public NET_DVR_TIME strLogTime;
	public int	dwMajorType;	//������ 1-����; 2-�쳣; 3-����; 0xff-ȫ��
	public int	dwMinorType;//������ 0-ȫ��;
	public byte[]	sPanelUser = new byte[MAX_NAMELEN]; //���������û���
	public byte[]	sNetUser = new byte[MAX_NAMELEN];//����������û���
	public byte[]	sRemoteHostAddr = new byte[16];//Զ�������ַ
	public int	dwParaType;//��������
	public int	dwChannel;//ͨ����
	public int	dwDiskNumber;//Ӳ�̺�
	public int	dwAlarmInPort;//��������˿�
	public int	dwAlarmOutPort;//��������˿�
}

/************************DVR��־ end***************************/
public static class NET_DVR_ALARMOUTSTATUS_V30 extends Structure {//�������״̬(9000��չ)
	public byte[] Output = new byte[MAX_ALARMOUT_V30];
}

public static class NET_DVR_ALARMOUTSTATUS extends Structure {//�������״̬
	public byte[] Output = new byte[MAX_ALARMOUT];
}

public static class NET_DVR_TRADEINFO extends Structure {//������Ϣ
	public short m_Year;
	public short m_Month;
	public short m_Day;
	public short m_Hour;
	public short m_Minute;
	public short m_Second;
	public byte[] DeviceName = new byte[24];	//�豸���
	public int dwChannelNumer;	//ͨ����
	public byte[] CardNumber = new byte[32];	//����
	public byte[] cTradeType = new byte[12];	//��������
	public int dwCash;			//���׽��
}

public static class NET_DVR_FRAMETYPECODE extends Structure {/*֡��ʽ*/
	public byte[] code = new byte[12];		/* ���� */
}

public static class NET_DVR_FRAMEFORMAT_V30 extends Structure {//ATM����(9000��չ)
	public int	dwSize;
	public NET_DVR_IPADDR	struATMIP;               	/* ATM IP��ַ */
	public int	dwATMType;							/* ATM���� */
	public int	dwInputMode;						/* ���뷽ʽ	0-�������� 1-������� 2-����ֱ������ 3-����ATM��������*/
	public int	dwFrameSignBeginPos;				/* ���ı�־λ����ʼλ��*/
	public int	dwFrameSignLength;					/* ���ı�־λ�ĳ��� */
	public byte[]	byFrameSignContent = new byte[12];				/* ���ı�־λ������ */
	public int	dwCardLengthInfoBeginPos;			/* ���ų�����Ϣ����ʼλ�� */
	public int	dwCardLengthInfoLength;				/* ���ų�����Ϣ�ĳ��� */
	public int	dwCardNumberInfoBeginPos;			/* ������Ϣ����ʼλ�� */
	public int	dwCardNumberInfoLength;				/* ������Ϣ�ĳ��� */
	public int	dwBusinessTypeBeginPos;				/* �������͵���ʼλ�� */
	public int	dwBusinessTypeLength;				/* �������͵ĳ��� */
	public NET_DVR_FRAMETYPECODE[]	frameTypeCode = new NET_DVR_FRAMETYPECODE[10];	/* ���� */
	public short	wATMPort;							/* ���Ų�׽�˿ں�(����Э�鷽ʽ) (����)0xffff��ʾ��ֵ��Ч*/
	public short	wProtocolType;						/* ����Э������(����) 0xffff��ʾ��ֵ��Ч*/
        public byte[]   byRes = new byte[24];
}

public static class NET_DVR_FRAMEFORMAT extends Structure {//ATM����
	public int dwSize;
	public byte[] sATMIP = new byte[16];						/* ATM IP��ַ */
	public int dwATMType;						/* ATM���� */
	public int dwInputMode;						/* ���뷽ʽ	0-�������� 1-������� 2-����ֱ������ 3-����ATM��������*/
	public int dwFrameSignBeginPos;              /* ���ı�־λ����ʼλ��*/
	public int dwFrameSignLength;				/* ���ı�־λ�ĳ��� */
	public byte[]  byFrameSignContent = new byte[12];			/* ���ı�־λ������ */
	public int dwCardLengthInfoBeginPos;			/* ���ų�����Ϣ����ʼλ�� */
	public int dwCardLengthInfoLength;			/* ���ų�����Ϣ�ĳ��� */
	public int dwCardNumberInfoBeginPos;			/* ������Ϣ����ʼλ�� */
	public int dwCardNumberInfoLength;			/* ������Ϣ�ĳ��� */
	public int dwBusinessTypeBeginPos;           /* �������͵���ʼλ�� */
	public int dwBusinessTypeLength;				/* �������͵ĳ��� */
	public NET_DVR_FRAMETYPECODE[] frameTypeCode = new NET_DVR_FRAMETYPECODE[10];/* ���� */
}

public static class NET_DVR_FTPTYPECODE extends Structure {
	public byte[] sFtpType = new byte[32];     /*�ͻ�����Ĳ�������*/
	public byte[] sFtpCode = new byte[8];      /*�ͻ�����Ĳ������͵Ķ�Ӧ����*/
}

public static class NET_DVR_FRAMEFORMAT_EX extends Structure {//ATM�������FTP�ϴ�����, ����˹���ж���, 2006-11-17
	public int dwSize;
	public byte[] sATMIP = new byte[16];						/* ATM IP��ַ */
	public int dwATMType;						/* ATM���� */
	public int dwInputMode;						/* ���뷽ʽ	0-�������� 1-������� 2-����ֱ������ 3-����ATM��������*/
	public int dwFrameSignBeginPos;              /* ���ı�־λ����ʼλ��*/
	public int dwFrameSignLength;				/* ���ı�־λ�ĳ��� */
	public byte[]  byFrameSignContent = new byte[12];			/* ���ı�־λ������ */
	public int dwCardLengthInfoBeginPos;			/* ���ų�����Ϣ����ʼλ�� */
	public int dwCardLengthInfoLength;			/* ���ų�����Ϣ�ĳ��� */
	public int dwCardNumberInfoBeginPos;			/* ������Ϣ����ʼλ�� */
	public int dwCardNumberInfoLength;			/* ������Ϣ�ĳ��� */
	public int dwBusinessTypeBeginPos;           /* �������͵���ʼλ�� */
	public int dwBusinessTypeLength;				/* �������͵ĳ��� */
	public NET_DVR_FRAMETYPECODE[] frameTypeCode = new NET_DVR_FRAMETYPECODE[10];/* ���� */
	public byte[] sFTPIP = new byte[16];						/* FTP IP */
	public byte[] byFtpUsername = new byte[NAME_LEN];			/* �û��� */
	public byte[] byFtpPasswd = new byte[PASSWD_LEN];			/* ���� */
	public byte[] sDirName = new byte[NAME_LEN];				/*������Ŀ¼��*/
	public int dwATMSrvType;						/*ATM���������ͣ�0--wincor ��1--diebold*/
	public int dwTimeSpace;						/*ȡֵΪ1.2.3.4.5.10*/
	public NET_DVR_FTPTYPECODE[] sFtpTypeCodeOp = new NET_DVR_FTPTYPECODE[300];    /*�¼ӵ�*/
	public int dwADPlay;    /* 1 ��ʾ�ڲ��Ź�棬0 ��ʾû�в��Ź��*/
	public int dwNewPort;  //�˿�
}
/****************************ATM(end)***************************/

/*****************************DS-6001D/F(begin)***************************/
//DS-6001D Decoder
public static class NET_DVR_DECODERINFO extends Structure {
	public byte[] byEncoderIP = new byte[16];		//�����豸���ӵķ�����IP
	public byte[] byEncoderUser = new byte[16];		//�����豸���ӵķ��������û���
	public byte[] byEncoderPasswd = new byte[16];	//�����豸���ӵķ�����������
	public byte bySendMode;			//�����豸���ӷ�����������ģʽ
	public byte byEncoderChannel;		//�����豸���ӵķ�������ͨ����
	public short wEncoderPort;			//�����豸���ӵķ������Ķ˿ں�
	public byte[] reservedData = new byte[4];		//����
}

public static class NET_DVR_DECODERSTATE extends Structure {
	public byte[] byEncoderIP = new byte[16];		//�����豸���ӵķ�����IP
	public byte[] byEncoderUser = new byte[16];		//�����豸���ӵķ��������û���
	public byte[] byEncoderPasswd = new byte[16];	//�����豸���ӵķ�����������
	public byte byEncoderChannel;		//�����豸���ӵķ�������ͨ����
	public byte bySendMode;			//�����豸���ӵķ�����������ģʽ
	public short wEncoderPort;			//�����豸���ӵķ������Ķ˿ں�
	public int dwConnectState;		//�����豸���ӷ�������״̬
	public byte[] reservedData = new byte[4];		//����
}

public static class NET_DVR_DECCHANINFO extends Structure {
	public byte[] sDVRIP = new byte[16];				/* DVR IP��ַ */
	public short wDVRPort;			 		/* �˿ں� */
	public byte[] sUserName = new byte[NAME_LEN];		/* �û��� */
	public byte[] sPassword = new byte[PASSWD_LEN];		/* ���� */
	public byte byChannel;					/* ͨ���� */
	public byte byLinkMode;				/* ����ģʽ */
	public byte byLinkType;				/* �������� 0�������� 1�������� */
}

public static class NET_DVR_DECINFO extends Structure {/*ÿ������ͨ��������*/
	public byte	byPoolChans;			/*ÿ·����ͨ���ϵ�ѭ��ͨ������, ���4ͨ�� 0��ʾû�н���*/
	public NET_DVR_DECCHANINFO[] struchanConInfo = new NET_DVR_DECCHANINFO[MAX_DECPOOLNUM];
	public byte	byEnablePoll;			/*�Ƿ���Ѳ 0-�� 1-��*/
	public byte	byPoolTime;				/*��Ѳʱ�� 0-���� 1-10�� 2-15�� 3-20�� 4-30�� 5-45�� 6-1���� 7-2���� 8-5���� */
}

public static class NET_DVR_DECCFG extends Structure {/*����豸��������*/
	public int	dwSize;
	public int	dwDecChanNum; 		/*����ͨ��������*/
	public NET_DVR_DECINFO[] struDecInfo = new NET_DVR_DECINFO[MAX_DECNUM];
}

//2005-08-01
public static class NET_DVR_PORTINFO extends Structure {/* �����豸͸��ͨ������ */
	public int dwEnableTransPort;	/* �Ƿ�����͸��ͨ�� 0�������� 1������*/
	public byte[] sDecoderIP = new byte[16];		/* DVR IP��ַ */
	public short wDecoderPort;			/* �˿ں� */
	public short wDVRTransPort;			/* ����ǰ��DVR�Ǵ�485/232�����1��ʾ232����,2��ʾ485���� */
	public byte[] cReserve = new byte[4];
}

public static class NET_DVR_PORTCFG extends Structure {
	public int dwSize;
	public NET_DVR_PORTINFO[] struTransPortInfo = new NET_DVR_PORTINFO[MAX_TRANSPARENTNUM]; /* ����0��ʾ232 ����1��ʾ485 */
}

/*https://jna.dev.java.net/javadoc/com/sun/jna/Union.html#setType(java.lang.Class)  see how to use the JNA Union*/
public static class NET_DVR_PLAYREMOTEFILE extends Structure {/* ���������ļ��ط� */
	public int dwSize;
	public byte[] sDecoderIP = new byte[16];		/* DVR IP��ַ */
	public short wDecoderPort;			/* �˿ں� */
	public short wLoadMode;				/* �ط�����ģʽ 1�������� 2����ʱ�� */
        public   byte[] byFile = new byte[100];
        public static class mode_size extends Union
	{
		public byte[] byFile = new byte[100];		// �طŵ��ļ���
		public static class bytime extends Structure
		{
			public int dwChannel;
			public byte[] sUserName = new byte[NAME_LEN];	//������Ƶ�û���
			public byte[] sPassword = new byte[PASSWD_LEN];	// ����
			public NET_DVR_TIME struStartTime;	//��ʱ��طŵĿ�ʼʱ��
			public NET_DVR_TIME struStopTime;	// ��ʱ��طŵĽ���ʱ��
		}
	}
}

public static class NET_DVR_DECCHANSTATUS extends Structure {/*��ǰ�豸��������״̬*/
	public int dwWorkType;		/*������ʽ��1����Ѳ��2����̬���ӽ��롢3���ļ��ط����� 4����ʱ��ط�����*/
	public byte[] sDVRIP = new byte[16];		/*���ӵ��豸ip*/
	public short wDVRPort;			/*���Ӷ˿ں�*/
	public byte byChannel;			/* ͨ���� */
	public byte byLinkMode;		/* ����ģʽ */
	public int	dwLinkType;		/*�������� 0�������� 1��������*/
	public byte[] sUserName = new byte[NAME_LEN];	/*������Ƶ�û���*/
	public byte[] sPassword = new byte[PASSWD_LEN];	/* ���� */
	public byte[] cReserve = new byte[52];
        public static class objectInfo extends Union
	{
		public static class userInfo extends Structure
		{
			public byte[] sUserName = new byte[NAME_LEN];	//������Ƶ�û���
			public byte[] sPassword = new byte[PASSWD_LEN];	// ����
			public byte[] cReserve = new byte[52];
		}
		public static class fileInfo extends Structure
		{
			public byte[]  fileName = new byte[100];
		}
		public static class timeInfo extends Structure
		{
			public int	dwChannel;
			public byte[] sUserName = new byte[NAME_LEN];	//������Ƶ�û���
			public byte[] sPassword = new byte[PASSWD_LEN];	// ����
			public NET_DVR_TIME struStartTime;		// ��ʱ��طŵĿ�ʼʱ��
			public NET_DVR_TIME struStopTime;		//��ʱ��طŵĽ���ʱ��
		}
	}
}

public static class NET_DVR_DECSTATUS extends Structure {
	public int   dwSize;
	public NET_DVR_DECCHANSTATUS[] struDecState = new NET_DVR_DECCHANSTATUS[MAX_DECNUM];
}
/*****************************DS-6001D/F(end)***************************/

public static class NET_DVR_SHOWSTRINGINFO extends Structure {//���ַ����(�ӽṹ)
	public short wShowString;				// Ԥ����ͼ�����Ƿ���ʾ�ַ�,0-����ʾ,1-��ʾ �����С704*576,�����ַ�Ĵ�СΪ32*32
	public short wStringSize;				/* �����ַ�ĳ��ȣ����ܴ���44���ַ� */
	public short wShowStringTopLeftX;		/* �ַ���ʾλ�õ�x��� */
	public short wShowStringTopLeftY;		/* �ַ������ʾλ�õ�y��� */
	public byte[] sString = new byte[44];				/* Ҫ��ʾ���ַ����� */
}

//�����ַ�(9000��չ)
public static class NET_DVR_SHOWSTRING_V30 extends Structure {
	public int dwSize;
	public NET_DVR_SHOWSTRINGINFO[] struStringInfo = new NET_DVR_SHOWSTRINGINFO[MAX_STRINGNUM_V30];				/* Ҫ��ʾ���ַ����� */
}

//�����ַ���չ(8���ַ�)
public static class NET_DVR_SHOWSTRING_EX extends Structure {
	public int dwSize;
	public NET_DVR_SHOWSTRINGINFO[] struStringInfo = new NET_DVR_SHOWSTRINGINFO[MAX_STRINGNUM_EX];				/* Ҫ��ʾ���ַ����� */
}

//�����ַ�
public static class NET_DVR_SHOWSTRING extends Structure {
	public int dwSize;
	public NET_DVR_SHOWSTRINGINFO[] struStringInfo = new NET_DVR_SHOWSTRINGINFO[MAX_STRINGNUM];				/* Ҫ��ʾ���ַ����� */
}

/****************************DS9000�����ṹ(begin)******************************/

/*
EMAIL����ṹ
*/
    public static class NET_DVR_SENDER extends Structure {
       public  byte[] sName = new byte[NAME_LEN];				/* ���������� */
       public   byte[] sAddress = new byte[MAX_EMAIL_ADDR_LEN];		/* �����˵�ַ */
    }
       public static class NET_DVRRECEIVER extends Structure {
       public  byte[]	sName = new byte[NAME_LEN];				/* �ռ������� */
       public  byte[]	sAddress = new byte[MAX_EMAIL_ADDR_LEN];		/* �ռ��˵�ַ */
    }

    public static class NET_DVR_EMAILCFG_V30 extends Structure {
	public int		dwSize;
	public byte[]		sAccount = new byte[NAME_LEN];				/* �˺�*/
	public byte[]		sPassword = new byte[MAX_EMAIL_PWD_LEN];			/*���� */
        public   NET_DVR_SENDER struSender;
	public byte[]		sSmtpServer  = new byte[MAX_EMAIL_ADDR_LEN];	/* smtp������ */
	public byte[]		sPop3Server = new byte[MAX_EMAIL_ADDR_LEN];	/* pop3������ */
	public NET_DVRRECEIVER[] struReceiver = new NET_DVRRECEIVER[3];							/* ����������3���ռ��� */
	public byte		byAttachment;					/* �Ƿ��� */
	public byte		bySmtpServerVerify;				/* ���ͷ�����Ҫ�������֤ */
        public  byte        byMailInterval;                 /* mail interval */
        public  byte[]        res = new byte[77];
}

/*
DVRʵ��Ѳ����ݽṹ
*/
    public static class NET_DVR_CRUISE_PARA extends Structure {
	public int 	dwSize;
	public byte[]	byPresetNo = new byte[CRUISE_MAX_PRESET_NUMS];		/* Ԥ�õ�� */
	public byte[] 	byCruiseSpeed = new byte[CRUISE_MAX_PRESET_NUMS];	/* Ѳ���ٶ� */
	public short[]	wDwellTime = new short[CRUISE_MAX_PRESET_NUMS];		/* ͣ��ʱ�� */
	public byte[]	byEnableThisCruise;						/* �Ƿ����� */
	public byte[]	res = new byte[15];
}

    /****************************DS9000�����ṹ(end)******************************/

//ʱ���
    public static class NET_DVR_TIMEPOINT extends Structure {
	public int dwMonth;		//�� 0-11��ʾ1-12����
	public int dwWeekNo;		//�ڼ��� 0����1�� 1����2�� 2����3�� 3����4�� 4�����һ��
	public int dwWeekDate;	//���ڼ� 0�������� 1������һ 2�����ڶ� 3�������� 4�������� 5�������� 6��������
	public int dwHour;		//Сʱ	��ʼʱ��0��23 ����ʱ��1��23
	public int dwMin;		//��	0��59
}

//����ʱ����
    public static class NET_DVR_ZONEANDDST extends Structure {
	public int dwSize;
	public byte[] byRes1 = new byte[16];			//����
	public int dwEnableDST;		//�Ƿ�������ʱ�� 0�������� 1������
	public byte byDSTBias;	//����ʱƫ��ֵ��30min, 60min, 90min, 120min, �Է��Ӽƣ�����ԭʼ��ֵ
	public byte[] byRes2 = new byte[3];
	public NET_DVR_TIMEPOINT struBeginPoint;	//��ʱ�ƿ�ʼʱ��
	public NET_DVR_TIMEPOINT struEndPoint;	//��ʱ��ֹͣʱ��
}

//ͼƬ����
    public static class NET_DVR_JPEGPARA extends Structure {
	/*ע�⣺��ͼ��ѹ���ֱ���ΪVGAʱ��֧��0=CIF, 1=QCIF, 2=D1ץͼ��
	���ֱ���Ϊ3=UXGA(1600x1200), 4=SVGA(800x600), 5=HD720p(1280x720),6=VGA,7=XVGA, 8=HD900p
	��֧�ֵ�ǰ�ֱ��ʵ�ץͼ*/
	public short	wPicSize;				/* 0=CIF, 1=QCIF, 2=D1 3=UXGA(1600x1200), 4=SVGA(800x600), 5=HD720p(1280x720),6=VGA*/
	public short	wPicQuality;			/* ͼƬ����ϵ�� 0-��� 1-�Ϻ� 2-һ�� */
    }

/* aux video out parameter */
//���������������
    public static class NET_DVR_AUXOUTCFG extends Structure {
	public int dwSize;
	public int dwAlarmOutChan;                       /* ѡ�񱨾������󱨾�ͨ���л�ʱ�䣺1��������ͨ��: 0:�����/1:��1/2:��2/3:��3/4:��4 */
	public int dwAlarmChanSwitchTime;                /* :1�� - 10:10�� */
	public int[] dwAuxSwitchTime = new int[MAX_AUXOUT];			/* ��������л�ʱ��: 0-���л�,1-5s,2-10s,3-20s,4-30s,5-60s,6-120s,7-300s */
	public byte[][]  byAuxOrder = new byte[MAX_AUXOUT][MAX_WINDOW];	/* �������Ԥ��˳��, 0xff��ʾ��Ӧ�Ĵ��ڲ�Ԥ�� */
}

//ntp
    public static class NET_DVR_NTPPARA extends Structure {
	public byte[] sNTPServer = new byte[64];   /* Domain Name or IP addr of NTP server */
	public short wInterval;		 /* adjust time interval(hours) */
	public byte byEnableNTP;    /* enable NPT client 0-no��1-yes*/
        public byte cTimeDifferenceH; /* ���ʱ�׼ʱ��� Сʱƫ��-12 ... +13 */
	public byte cTimeDifferenceM;/* ���ʱ�׼ʱ��� ����ƫ��0, 30, 45*/
	public byte res1;
       public   short wNtpPort;         /* ntp server port 9000���� �豸Ĭ��Ϊ123*/
       public   byte[] res2 = new byte[8];
}

//ddns
    public static class NET_DVR_DDNSPARA extends Structure {
	public byte[] sUsername = new byte[NAME_LEN];  /* DDNS�˺��û���/���� */
	public byte[] sPassword = new byte[PASSWD_LEN];
	public byte[] sDomainName = new byte[64];       /* ���� */
	public byte byEnableDDNS;			/*�Ƿ�Ӧ�� 0-��1-��*/
	public byte[] res = new byte[15];
}

   public static class NET_DVR_DDNSPARA_EX extends Structure {
	public byte byHostIndex;					/* 0-Hikvision DNS 1��Dyndns 2��PeanutHull(�����), 3-ϣ��3322*/
	public byte byEnableDDNS;					/*�Ƿ�Ӧ��DDNS 0-��1-��*/
	public short wDDNSPort;						/* DDNS�˿ں� */
	public byte[] sUsername = new byte[NAME_LEN];			/* DDNS�û���*/
	public byte[] sPassword = new byte[PASSWD_LEN];			/* DDNS���� */
	public byte[] sDomainName = new byte[MAX_DOMAIN_NAME];	/* �豸�䱸�������ַ */
	public byte[] sServerName = new byte[MAX_DOMAIN_NAME];	/* DDNS ��Ӧ�ķ�������ַ��������IP��ַ������ */
	public byte[] byRes = new byte[16];
}

   public static class NET_DVR_DDNS extends Structure {
       public  byte[] sUsername = new byte[NAME_LEN];			/* DDNS�˺��û���*/
       public  byte[] sPassword = new byte[PASSWD_LEN];			/* ���� */
       public  byte[] sDomainName = new byte[MAX_DOMAIN_NAME];	/* �豸�䱸�������ַ */
       public  byte[] sServerName = new byte[MAX_DOMAIN_NAME];	/* DDNSЭ���Ӧ�ķ�������ַ��������IP��ַ������ */
       public  short wDDNSPort;						/* �˿ں� */
       public   byte[] byRes = new byte[10];
   }
//9000��չ
public static class NET_DVR_DDNSPARA_V30 extends Structure {
  public   byte byEnableDDNS;
  public   byte byHostIndex;/* 0-Hikvision DNS(����) 1��Dyndns 2��PeanutHull(�����) 3��ϣ��3322 */
  public  byte[] byRes1 = new byte[2];
  public   NET_DVR_DDNS[] struDDNS = new NET_DVR_DDNS[MAX_DDNS_NUMS];//9000Ŀǰֻ֧��ǰ3�����ã��������ñ���
  public   byte[] byRes2 = new byte[16];
}

//email
public static class NET_DVR_EMAILPARA extends Structure {
	public byte[] sUsername = new byte[64];  /* �ʼ��˺�/���� */
	public byte[] sPassword = new byte[64];
	public byte[] sSmtpServer = new byte[64];
	public byte[] sPop3Server = new byte[64];
	public byte[] sMailAddr = new byte[64];   /* email */
	public byte[] sEventMailAddr1 = new byte[64];  /* �ϴ�����/�쳣�ȵ�email */
	public byte[] sEventMailAddr2 = new byte[64];
	public byte[] res = new byte[16];
}

public static class NET_DVR_NETAPPCFG extends Structure {//�����������
	public int  dwSize;
	public byte[]  sDNSIp = new byte[16];                /* DNS��������ַ */
	public NET_DVR_NTPPARA  struNtpClientParam;      /* NTP���� */
	public NET_DVR_DDNSPARA struDDNSClientParam;     /* DDNS���� */
	//NET_DVR_EMAILPARA struEmailParam;       /* EMAIL���� */
	public byte[] res = new byte[464];			/* ���� */
}

public static class NET_DVR_SINGLE_NFS extends Structure {//nfs�ṹ����
    public byte[] sNfsHostIPAddr = new byte[16];
    public byte[] sNfsDirectory = new byte[PATHNAME_LEN];        // PATHNAME_LEN = 128
}

public static class NET_DVR_NFSCFG extends Structure {
	public int  dwSize;
        public NET_DVR_SINGLE_NFS[] struNfsDiskParam = new NET_DVR_SINGLE_NFS[MAX_NFS_DISK];
}

//Ѳ��������(HIK IP����ר��)
public static class NET_DVR_CRUISE_POINT extends Structure {
  public   byte	PresetNum;	//Ԥ�õ�
  public  byte	Dwell;		//ͣ��ʱ��
  public   byte	Speed;		//�ٶ�
  public   byte	Reserve;	//����
}

public static class NET_DVR_CRUISE_RET extends Structure {
	public NET_DVR_CRUISE_POINT[] struCruisePoint = new NET_DVR_CRUISE_POINT[32];			//���֧��32��Ѳ����
}

/************************************��·������(begin)***************************************/
//��·��������չ added by zxy 2007-05-23
public static class NET_DVR_NETCFG_OTHER extends Structure {
	public int	dwSize;
	public byte[]	sFirstDNSIP = new byte[16];
	public byte[]	sSecondDNSIP = new byte[16];
	public byte[]	sRes = new byte[32];
}

public static class NET_DVR_MATRIX_DECINFO extends Structure {
	public byte[] 	sDVRIP = new byte[16];				/* DVR IP��ַ */
	public short 	wDVRPort;			 	/* �˿ں� */
	public byte 	byChannel;				/* ͨ���� */
	public byte	byTransProtocol;			/* ����Э������ 0-TCP 1-UDP */
	public byte	byTransMode;				/* ��������ģʽ 0�������� 1��������*/
	public byte[]	byRes = new byte[3];
	public byte[]	sUserName = new byte[NAME_LEN];			/* ��������½�ʺ� */
	public byte[]	sPassword = new byte[PASSWD_LEN];			/* ����������� */
}

public static class NET_DVR_MATRIX_DYNAMIC_DEC extends Structure {//����/ֹͣ��̬����
	public int	dwSize;
	public NET_DVR_MATRIX_DECINFO struDecChanInfo;		/* ��̬����ͨ����Ϣ */
}

public static class NET_DVR_MATRIX_DEC_CHAN_STATUS extends Structure {//2007-12-13 modified by zxy �޸Ķ�·��������NET_DVR_MATRIX_DEC_CHAN_STATUS�ṹ
   public  int   dwSize;//2008-1-16 modified by zxy dwIsLinked��״̬��ԭ����0��δ���� 1�������޸ĳ���������״̬��
   public  int   dwIsLinked;         /* ����ͨ��״̬ 0������ 1���������� 2�������� 3-���ڽ��� */
   public  int   dwStreamCpRate;     /* Stream copy rate, X kbits/second */
   public  byte[]    cRes = new byte[64];		/* ���� */
}
//end 2007-12-13 modified by zxy

public static class NET_DVR_MATRIX_DEC_CHAN_INFO extends Structure {
	public int	dwSize;
	public NET_DVR_MATRIX_DECINFO struDecChanInfo;		/* ����ͨ����Ϣ */
	public int	dwDecState;	/* 0-��̬���� 1��ѭ������ 2����ʱ��ط� 3�����ļ��ط� */
	public NET_DVR_TIME StartTime;		/* ��ʱ��طſ�ʼʱ�� */
	public NET_DVR_TIME StopTime;		/* ��ʱ��ط�ֹͣʱ�� */
	public byte[]    sFileName = new byte[128];		/* ���ļ��ط��ļ��� */
}

//���ӵ�ͨ������ 2007-11-05
public static class NET_DVR_MATRIX_DECCHANINFO extends Structure {
	public int dwEnable;					/* �Ƿ����� 0���� 1������*/
	public NET_DVR_MATRIX_DECINFO struDecChanInfo;		/* ��ѭ����ͨ����Ϣ */
}

//2007-11-05 ����ÿ������ͨ��������
public static class NET_DVR_MATRIX_LOOP_DECINFO extends Structure {
	public int	dwSize;
	public int	dwPoolTime;			/*��Ѳʱ�� */
	public NET_DVR_MATRIX_DECCHANINFO[] struchanConInfo = new NET_DVR_MATRIX_DECCHANINFO[MAX_CYCLE_CHAN];
}

//2007-05-25  ��·���������־�������
//��������Ϣ 2007-12-28
public static class NET_DVR_MATRIX_ROW_ELEMENT extends Structure {
	public byte[]	sSurvChanName = new byte[128];			/* ���ͨ����ƣ�֧������ */
	public int	dwRowNum;				/* �к� */
	public NET_DVR_MATRIX_DECINFO struDecChanInfo;		/* ��������Ϣ */
}

public static class NET_DVR_MATRIX_ROW_INDEX extends Structure {
	public byte[]	sSurvChanName = new byte[128];			/* ���ͨ����ƣ�֧������ */
	public int	dwRowNum;				/* �к� */
}

//��������Ϣ 2007-12-28
public static class NET_DVR_MATRIX_COLUMN_ELEMENT extends Structure {
	public int  dwLocalDispChanNum;	/* ������ʾͨ���� */
	public int  dwGlobalDispChanNum;	/* ȫ����ʾͨ���� */
	public int  dwRes;			/* ���� */
}

public static class NET_DVR_MATRIX_GLOBAL_COLUMN_ELEMENT extends Structure {
	public int		dwConflictTag;		/* ��ͻ��ǣ�0���޳�ͻ��1����ͻ */
	public int		dwConflictGloDispChan;	/* ��֮��ͻ��ȫ��ͨ���� */
	public NET_DVR_MATRIX_COLUMN_ELEMENT  struColumnInfo;/* ������Ԫ�ؽṹ�� */
}

//�ֶ��鿴 2007-12-28
public static class NET_DVR_MATRIX_ROW_COLUMN_LINK extends Structure {
	public int	dwSize;
	/*
	*	�����������ֻ��Ҫָ������һ�����ָ�����־������ĳһ��
	*	�����Զ�̼��ͨ����
	*	���ָ���˶�����г�ͻ���豸����������Ⱥ�˳��Ϊ׼ȡ���ȶ����ߡ�
	*/
	public int	dwRowNum;			/* -1�����Ч��,����0�߷�Ϊ��Ч�ľ����к� */
	public byte[]	sSurvChanName = new byte[128];	/* ���ͨ����,�Ƿ���Ч���ַ����Ч���ж� */
	public int	dwSurvNum;			/* ���ͨ����,���������б��˳��ָ����һ����������к�һ�� */
								/*
								*	��������ֻ��Ҫָ������һ���ɣ���������ЧĬ��ѡ���һ��
	*/
	public int	dwGlobalDispChanNum;			/* ����ǽ�ϵĵ��ӻ��� */
	public int	dwLocalDispChanNum;
	/*
	*	0��?�ż�ʱ������
	*	1��ʾ��ʱ��ط�Զ�̼���豸���ļ�
	*	2��ʾ���ļ���ط�
	*/
	public int	dwTimeSel;
	public NET_DVR_TIME StartTime;
	public NET_DVR_TIME StopTime;
	public byte[]	sFileName = new byte[128];
}

public static class NET_DVR_MATRIX_PREVIEW_DISP_CHAN extends Structure {
	public int		dwSize;
	public int		dwGlobalDispChanNum;		/* ����ǽ�ϵĵ��ӻ��� */
	public int		dwLocalDispChanNum;		/* ����ͨ�� */
}

public static class NET_DVR_MATRIX_LOOP_PLAY_SET extends Structure {//��ѭ���� 2007-12-28
	public int	dwSize;
	/* ����ָ��һ��,-1Ϊ��Ч,���ָ������LocalDispChanNumΪ׼ */
	public int	dwLocalDispChanNum;	/* ����ͨ�� */
	public int	dwGlobalDispChanNum;  	/* ����ǽ�ϵĵ��ӻ��� */
	public int	dwCycTimeInterval;	/* ��ѭʱ���� */
}

public static class NET_DVR_MATRIX_LOCAL_HOST_INFO extends Structure {//������������ 2007-12-28
	public int	dwSize;
	public int	dwLocalHostProperty;  	/* ������������ 0�������� 1���ͻ���*/
	public int	dwIsIsolated;		/* ���������Ƿ������ϵͳ��0������1������ */
	public int	dwLocalMatrixHostPort;	/* ����������ʶ˿� */
	public byte[]	byLocalMatrixHostUsrName = new byte[NAME_LEN];		/* ���������¼�û��� */
	public byte[]	byLocalMatrixHostPasswd = new byte[PASSWD_LEN];		/* ���������¼���� */
	public int	dwLocalMatrixCtrlMedia;				/* ���Ʒ�ʽ 0x1���ڼ��̿��� 0x2������̿��� 0x4�������Ŀ��� 0x8PC�ͻ��˿���*/
	public byte[]	sMatrixCenterIP = new byte[16];		/* ��������IP��ַ */
	public int	dwMatrixCenterPort;	 	/* �������Ķ˿ں� */
	public byte[]	byMatrixCenterUsrName = new byte[NAME_LEN];	/* �������ĵ�¼�û��� */
	public byte[]	byMatrixCenterPasswd = new byte[PASSWD_LEN];	/* �������ĵ�¼���� */
}

//2007-12-22
public static class TTY_CONFIG extends Structure {
	public byte	baudrate; 	/* ������ */
	public byte	databits;		/* ���λ */
	public byte	stopbits;		/* ֹͣλ */
	public byte	parity;		/* ��żУ��λ */
	public byte	flowcontrol;	/* ���� */
	public byte[]	res = new byte[3];
}

public static class NET_DVR_MATRIX_TRAN_CHAN_INFO extends Structure {
	public byte byTranChanEnable;	/* ��ǰ͸��ͨ���Ƿ�� 0���ر� 1���� */
         /*
	 *	��·������������1��485���ڣ�1��232���ڶ�������Ϊ͸��ͨ��,�豸�ŷ������£�
	 *	0 RS485
	 *	1 RS232 Console
	 */
	public byte	byLocalSerialDevice;			/* Local serial device */
         /*
	 *	Զ�̴��������������,һ��RS232��һ��RS485
	 *	1��ʾ232����
	 *	2��ʾ485����
	 */
	public byte	byRemoteSerialDevice;			/* Remote output serial device */
	public byte	res1;							/* ���� */
	public byte[]	sRemoteDevIP= new byte[16];				/* Remote Device IP */
	public short	wRemoteDevPort;				/* Remote Net Communication Port */
	public byte[]	res2= new byte[2];						/* ���� */
	public TTY_CONFIG RemoteSerialDevCfg;
}

public static class NET_DVR_MATRIX_TRAN_CHAN_CONFIG extends Structure {
        public 	int dwSize;
	public byte 	by232IsDualChan; /* ������·232͸��ͨ����ȫ˫���� ȡֵ1��MAX_SERIAL_NUM */
	public byte	by485IsDualChan; /* ������·485͸��ͨ����ȫ˫���� ȡֵ1��MAX_SERIAL_NUM */
	public byte[]	res = new byte[2];	/* ���� */
	public NET_DVR_MATRIX_TRAN_CHAN_INFO[] struTranInfo = new NET_DVR_MATRIX_TRAN_CHAN_INFO[MAX_SERIAL_NUM];/*ͬʱ֧�ֽ���MAX_SERIAL_NUM��͸��ͨ��*/
}

//2007-12-24 Merry Christmas Eve...
public static class NET_DVR_MATRIX_DEC_REMOTE_PLAY extends Structure {
	public int	dwSize;
	public byte[]	sDVRIP = new byte[16];		/* DVR IP��ַ */
	public short	wDVRPort;			/* �˿ں� */
	public byte	byChannel;			/* ͨ���� */
	public byte 	byReserve;
	public byte[]	sUserName = new byte[NAME_LEN];		/* �û��� */
	public byte[]	sPassword = new byte[PASSWD_LEN];		/* ���� */
	public int	dwPlayMode;   	/* 0�����ļ� 1����ʱ��*/
	public NET_DVR_TIME StartTime;
	public NET_DVR_TIME StopTime;
	public byte[]    sFileName = new byte[128];
}


public static class NET_DVR_MATRIX_DEC_REMOTE_PLAY_CONTROL extends Structure {
	public int	dwSize;
	public int	dwPlayCmd;		/* �������� ���ļ���������*/
	public int	dwCmdParam;		/* ����������� */
}

public static class NET_DVR_MATRIX_DEC_REMOTE_PLAY_STATUS extends Structure {
	public int dwSize;
	public int dwCurMediaFileLen; /* ��ǰ���ŵ�ý���ļ����� */
	public int dwCurMediaFilePosition; /* ��ǰ�����ļ��Ĳ���λ�� */
	public int dwCurMediaFileDuration; /* ��ǰ�����ļ�����ʱ�� */
	public int dwCurPlayTime; /* ��ǰ�Ѿ����ŵ�ʱ�� */
	public int dwCurMediaFIleFrames; /* ��ǰ�����ļ�����֡�� */
	public int dwCurDataType; /* ��ǰ�����������ͣ�19-�ļ�ͷ��20-����ݣ� 21-���Ž����־ */
        public  byte[] res = new byte[72];
}

public static class NET_DVR_MATRIX_PASSIVEMODE extends Structure {
	public short	wTransProtol;		//����Э�飬0-TCP, 1-UDP, 2-MCAST
	public short	wPassivePort;		//TCP,UDPʱΪTCP,UDP�˿�, MCASTʱΪMCAST�˿�
	public byte[]	sMcastIP = new byte[16];		//TCP,UDPʱ��Ч, MCASTʱΪ�ಥ��ַ
	public byte[]	res = new byte[8];
}
/************************************��·������(end)***************************************/

/************************************��·������(end)***************************************/

public static class NET_DVR_EMAILCFG  extends Structure
{	/* 12 bytes */
	public int	dwSize;
	public byte[]	sUserName = new byte[32];
	public byte[]	sPassWord = new byte[32];
	public byte[] 	sFromName = new byte[32];			/* Sender *///�ַ��еĵ�һ���ַ�����һ���ַ�����"@",�����ַ���Ҫ��"@"�ַ�
	public byte[] 	sFromAddr = new byte[48];			/* Sender address */
	public byte[] 	sToName1 = new byte[32];			/* Receiver1 */
	public byte[] 	sToName2 = new byte[32];			/* Receiver2 */
	public byte[] 	sToAddr1 = new byte[48];			/* Receiver address1 */
	public byte[] 	sToAddr2 = new byte[48];			/* Receiver address2 */
	public byte[]	sEmailServer = new byte[32];		/* Email server address */
        public byte	byServerType;			/* Email server type: 0-SMTP, 1-POP, 2-IMTP��*/
	public byte	byUseAuthen;			/* Email server authentication method: 1-enable, 0-disable */
	public byte	byAttachment;			/* enable attachment */
	public byte	byMailinterval;			/* mail interval 0-2s, 1-3s, 2-4s. 3-5s*/
}

public static class NET_DVR_COMPRESSIONCFG_NEW extends Structure
{
	public int dwSize;
	public NET_DVR_COMPRESSION_INFO_EX  struLowCompression;	//��ʱ¼��
	public NET_DVR_COMPRESSION_INFO_EX  struEventCompression;	//�¼�����¼��
}

//���λ����Ϣ
public static class NET_DVR_PTZPOS extends Structure
{
   public   short wAction;//��ȡʱ���ֶ���Ч
   public  short wPanPos;//ˮƽ����
   public  short wTiltPos;//��ֱ����
   public short wZoomPos;//�䱶����
}

//���Χ��Ϣ
public static class NET_DVR_PTZSCOPE extends Structure
{
   public  short wPanPosMin;//ˮƽ����min
   public  short wPanPosMax;//ˮƽ����max
   public  short wTiltPosMin;//��ֱ����min
   public  short wTiltPosMax;//��ֱ����max
   public   short wZoomPosMin;//�䱶����min
   public   short wZoomPosMax;//�䱶����max
}

//rtsp���� ipcameraר��
public static class NET_DVR_RTSPCFG extends Structure
{
 public    int dwSize;         //����
 public    short  wPort;          //rtsp����������˿�
 public    byte[]  byReserve = new byte[54];  //Ԥ��
}

/********************************�ӿڲ���ṹ(begin)*********************************/

//NET_DVR_Login()����ṹ
public static class NET_DVR_DEVICEINFO extends Structure
{
	public byte[] sSerialNumber = new byte[SERIALNO_LEN];   //���к�
	public byte byAlarmInPortNum;		        //DVR�����������
	public byte byAlarmOutPortNum;		        //DVR�����������
	public byte byDiskNum;				        //DVRӲ�̸���
	public byte byDVRType;				        //DVR����, 1:DVR 2:ATM DVR 3:DVS ......
	public byte byChanNum;				        //DVR ͨ������
	public byte byStartChan;			        //��ʼͨ����,����DVS-1,DVR - 1
}

//NET_DVR_Login_V30()����ṹ
public static class NET_DVR_DEVICEINFO_V30 extends Structure
{
   public  byte[] sSerialNumber = new byte[SERIALNO_LEN];  //���к�
   public  byte byAlarmInPortNum;		        //�����������
   public  byte byAlarmOutPortNum;		        //�����������
   public  byte byDiskNum;				    //Ӳ�̸���
   public  byte byDVRType;				    //�豸����, 1:DVR 2:ATM DVR 3:DVS ......
   public  byte byChanNum;				    //ģ��ͨ������
   public  byte byStartChan;			        //��ʼͨ����,����DVS-1,DVR - 1
   public  byte byAudioChanNum;                //����ͨ����
   public  byte byIPChanNum;					//�������ͨ������
   public  byte[] byRes1 = new byte[24];					//����
}

//sdk���绷��ö�ٱ���������Զ����
 enum _SDK_NET_ENV
{
    LOCAL_AREA_NETWORK ,
    WIDE_AREA_NETWORK
}

//��ʾģʽ
 enum DISPLAY_MODE
{
	NORMALMODE ,
	OVERLAYMODE
}

//����ģʽ
 enum SEND_MODE
{
	PTOPTCPMODE,
	PTOPUDPMODE,
	MULTIMODE,
	RTPMODE,
	RESERVEDMODE
};

//ץͼģʽ
 enum CAPTURE_MODE
{
	BMP_MODE,		//BMPģʽ
	JPEG_MODE		//JPEGģʽ
};

//ʵʱ����ģʽ
 enum REALSOUND_MODE
{
	NONE,                   //SDK���޴�ģʽ,ֻ��Ϊ���0���λ��
        MONOPOLIZE_MODE ,       //��ռģʽ 1
	SHARE_MODE 		//����ģʽ 2
};

//�����Ԥ������
    public static class NET_DVR_CLIENTINFO extends Structure {
        public NativeLong lChannel;
        public NativeLong lLinkMode;
        public HWND hPlayWnd;
        public String sMultiCastIP;
    }

//SDK״̬��Ϣ(9000����)
public static class NET_DVR_SDKSTATE extends Structure
{
    public int dwTotalLoginNum;		//��ǰlogin�û���
    public int dwTotalRealPlayNum;	//��ǰrealplay·��
    public int dwTotalPlayBackNum;	//��ǰ�طŻ�����·��
    public int dwTotalAlarmChanNum;	//��ǰ��������ͨ��·��
    public int dwTotalFormatNum;		//��ǰӲ�̸�ʽ��·��
    public  int dwTotalFileSearchNum;	//��ǰ��־���ļ�����·��
    public  int dwTotalLogSearchNum;	//��ǰ��־���ļ�����·��
    public  int dwTotalSerialNum;	    //��ǰ͸��ͨ��·��
    public int dwTotalUpgradeNum;	//��ǰ��·��
    public int dwTotalVoiceComNum;	//��ǰ����ת��·��
    public int dwTotalBroadCastNum;	//��ǰ�����㲥·��
    public int[] dwRes = new int[10];
}

//SDK����֧����Ϣ(9000����)
public static class NET_DVR_SDKABL extends Structure
{
    public int dwMaxLoginNum;		//���login�û��� MAX_LOGIN_USERS
    public int dwMaxRealPlayNum;		//���realplay·�� WATCH_NUM
    public int dwMaxPlayBackNum;		//���طŻ�����·�� WATCH_NUM
    public int dwMaxAlarmChanNum;	//���������ͨ��·�� ALARM_NUM
    public int dwMaxFormatNum;		//���Ӳ�̸�ʽ��·�� SERVER_NUM
    public int dwMaxFileSearchNum;	//����ļ�����·�� SERVER_NUM
    public int dwMaxLogSearchNum;	//�����־����·�� SERVER_NUM
    public int dwMaxSerialNum;	    //���͸��ͨ��·�� SERVER_NUM
    public int dwMaxUpgradeNum;	    //�����·�� SERVER_NUM
    public int dwMaxVoiceComNum;		//�������ת��·�� SERVER_NUM
    public int dwMaxBroadCastNum;	//��������㲥·�� MAX_CASTNUM
    public int[] dwRes = new int[10];
}

//�����豸��Ϣ
public static class NET_DVR_ALARMER extends Structure
{
   public  byte byUserIDValid;                 /* userid�Ƿ���Ч 0-��Ч��1-��Ч */
   public  byte bySerialValid;                 /* ���к��Ƿ���Ч 0-��Ч��1-��Ч */
   public  byte byVersionValid;                /* �汾���Ƿ���Ч 0-��Ч��1-��Ч */
   public  byte byDeviceNameValid;             /* �豸�����Ƿ���Ч 0-��Ч��1-��Ч */
   public byte byMacAddrValid;                /* MAC��ַ�Ƿ���Ч 0-��Ч��1-��Ч */
   public   byte byLinkPortValid;               /* login�˿��Ƿ���Ч 0-��Ч��1-��Ч */
   public    byte byDeviceIPValid;               /* �豸IP�Ƿ���Ч 0-��Ч��1-��Ч */
   public   byte bySocketIPValid;               /* socket ip�Ƿ���Ч 0-��Ч��1-��Ч */
   public   NativeLong lUserID;                       /* NET_DVR_Login()����ֵ, ����ʱ��Ч */
   public   byte[] sSerialNumber = new byte[SERIALNO_LEN];	/* ���к� */
   public  int dwDeviceVersion;			    /* �汾��Ϣ ��16λ��ʾ���汾����16λ��ʾ�ΰ汾*/
   public   byte[] sDeviceName = new byte[NAME_LEN];		    /* �豸���� */
   public    byte[] byMacAddr = new byte[MACADDR_LEN];		/* MAC��ַ */
   public   short wLinkPort;                     /* link port */
   public   byte[] sDeviceIP = new byte[128];    			/* IP��ַ */
   public   byte[] sSocketIP = new byte[128];    			/* ���������ϴ�ʱ��socket IP��ַ */
   public  byte byIpProtocol;                  /* IpЭ�� 0-IPV4, 1-IPV6 */
   public    byte[] byRes2 = new byte[11];
}

//Ӳ������ʾ�������(�ӽṹ)
public static class NET_DVR_DISPLAY_PARA extends Structure
{
	public NativeLong bToScreen;
	public NativeLong bToVideoOut;
	public NativeLong nLeft;
	public NativeLong nTop;
	public NativeLong nWidth;
	public NativeLong nHeight;
	public NativeLong nReserved;
}

//Ӳ����Ԥ������
public static class NET_DVR_CARDINFO extends Structure
{
	public NativeLong lChannel;//ͨ����
	public NativeLong lLinkMode; //���λ(31)Ϊ0��ʾ��������Ϊ1��ʾ�ӣ�0��30λ��ʾ�������ӷ�ʽ:0��TCP��ʽ,1��UDP��ʽ,2���ಥ��ʽ,3 - RTP��ʽ��4-�绰�ߣ�5��128k���6��256k���7��384k���8��512k���
	public String sMultiCastIP;
	public NET_DVR_DISPLAY_PARA struDisplayPara;
}

//¼���ļ�����
public static class NET_DVR_FIND_DATA extends Structure
{
	public byte[] sFileName = new byte[100];//�ļ���
	public NET_DVR_TIME struStartTime;//�ļ��Ŀ�ʼʱ��
	public NET_DVR_TIME struStopTime;//�ļ��Ľ���ʱ��
	public int dwFileSize;//�ļ��Ĵ�С
}

//¼���ļ�����(9000)
  public static class NET_DVR_FINDDATA_V30 extends Structure {
        public byte[] sFileName = new byte[100];//�ļ���
        public NET_DVR_TIME struStartTime;//�ļ��Ŀ�ʼʱ��
        public NET_DVR_TIME struStopTime;//�ļ��Ľ���ʱ��
        public int dwFileSize;//�ļ��Ĵ�С
        public byte[] sCardNum = new byte[32];
        public byte byLocked;//9000�豸֧��,1��ʾ���ļ��Ѿ�����,0��ʾ����ļ�
        public byte[] byRes = new byte[3];
    }

//¼���ļ�����(���)
public static class NET_DVR_FINDDATA_CARD extends Structure
{
	public byte[] sFileName = new byte[100];//�ļ���
	public NET_DVR_TIME struStartTime;//�ļ��Ŀ�ʼʱ��
	public NET_DVR_TIME struStopTime;//�ļ��Ľ���ʱ��
	public int dwFileSize;//�ļ��Ĵ�С
	public byte[] sCardNum = new byte[32];
}


 public static class NET_DVR_FILECOND extends Structure //¼���ļ����������ṹ
    {
        public NativeLong lChannel;//ͨ����
        public int dwFileType;//¼���ļ�����0xff��ȫ����0����ʱ¼��,1-�ƶ���� ��2������������3-����|�ƶ���� 4-����&�ƶ���� 5-����� 6-�ֶ�¼��
        public int dwIsLocked;//�Ƿ��� 0-���ļ�,1-���ļ�, 0xff��ʾ�����ļ�
        public int dwUseCardNo;//�Ƿ�ʹ�ÿ���
        public byte[] sCardNumber = new byte[32];//����
        public NET_DVR_TIME struStartTime;//��ʼʱ��
        public NET_DVR_TIME struStopTime;//����ʱ��
    }


//��̨����ѡ��Ŵ���С(HIK ����ר��)
public static class NET_DVR_POINT_FRAME extends Structure
{
	public int xTop;     //������ʼ���x���
	public int yTop;     //���������y���
	public int xBottom;  //���������x���
	public int yBottom;  //���������y���
	public int bCounter; //����
}

//�����Խ�����
public static class NET_DVR_COMPRESSION_AUDIO extends Structure
{
	public byte  byAudioEncType;   //��Ƶ�������� 0-G722; 1-G711
	public byte[] byres= new byte [7];//���ﱣ����Ƶ��ѹ������
}

//���ڽ��ձ�����Ϣ�Ļ�����
public static class RECV_ALARM extends Structure{
    public byte[] RecvBuffer = new byte[400];//�˴���400Ӧ��С����󱨾����ĳ���
}

public static class LPNET_DVR_STD_ABILITY extends Structure{
	  public byte[] lpCondBuffer = new byte[400];
	  int     dwCondSize;
	  public byte[] lpOutBuffer = new byte[400];
	  int     dwOutSize;
	  public byte[] lpStatusBuffer = new byte[400];
	  int     dwStatusSize;
	  int     dwRetSize;
	  byte      byRes;
}

public static class NET_DVR_CORRIDOR_MODE extends Structure{
	public int dwSize;
	public byte byEnableCorridorMode;
	public byte byMirrorMode;
	public byte byRes;

}

 /***API��������,��ϸ˵����API�ֲ�***/
   public static interface FRealDataCallBack_V30 extends StdCallCallback {
        public void invoke(NativeLong lRealHandle, int dwDataType,
                ByteByReference pBuffer, int dwBufSize, Pointer pUser);
    }

   public static interface FMSGCallBack extends StdCallCallback {
        public void invoke(NativeLong lCommand, NET_DVR_ALARMER pAlarmer, HCNetSDK.RECV_ALARM  pAlarmInfo, int dwBufLen,Pointer pUser);
    }

   public static interface FMessCallBack extends StdCallCallback {
        public boolean invoke(NativeLong lCommand,String sDVRIP,String pBuf,int dwBufLen);
    }

   public static interface FMessCallBack_EX extends StdCallCallback {
        public boolean invoke(NativeLong lCommand,NativeLong lUserID,String pBuf,int dwBufLen);
    }

   public static interface FMessCallBack_NEW extends StdCallCallback {
        public boolean invoke(NativeLong lCommand,String sDVRIP,String pBuf,int dwBufLen, short dwLinkDVRPort);
    }

   public static interface FMessageCallBack extends StdCallCallback {
        public boolean invoke(NativeLong lCommand,String sDVRIP,String pBuf,int dwBufLen, int dwUser);
    }

      public static interface FExceptionCallBack extends StdCallCallback {
        public void invoke(int dwType, NativeLong lUserID, NativeLong lHandle, Pointer pUser);
    }
      public static interface FDrawFun extends StdCallCallback {
        public void invoke(NativeLong lRealHandle,W32API.HDC hDc,int dwUser);
      }

    public static interface FStdDataCallBack extends StdCallCallback {
        public void invoke(NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer,int dwBufSize,int dwUser);
      }

    public static interface FPlayDataCallBack extends StdCallCallback {
        public void invoke(NativeLong lPlayHandle, int dwDataType, ByteByReference pBuffer,int dwBufSize,int dwUser);
      }

    public static interface FVoiceDataCallBack extends StdCallCallback {
        public void invoke(NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, int dwUser);
      }

    public static interface FVoiceDataCallBack_V30 extends StdCallCallback {
        public void invoke(NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag,Pointer pUser);
      }

    public static interface FVoiceDataCallBack_MR extends StdCallCallback {
        public void invoke(NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, int dwUser);
      }

    public static interface FVoiceDataCallBack_MR_V30 extends StdCallCallback {
        public void invoke(NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, String pUser);
      }

    public static interface FVoiceDataCallBack2 extends StdCallCallback {
        public void invoke(String pRecvDataBuffer, int dwBufSize, Pointer pUser);
      }

   public static interface FSerialDataCallBack extends StdCallCallback {
        public void invoke(NativeLong lSerialHandle,String pRecvDataBuffer,int dwBufSize,int dwUser);
      }

    public static interface FRowDataCallBack extends StdCallCallback {
        public void invoke(NativeLong lUserID,String  sIPAddr, NativeLong lRowAmout, String pRecvDataBuffer,int dwBufSize,int dwUser);
      }

    public static interface FColLocalDataCallBack extends StdCallCallback {
        public void invoke(NativeLong lUserID, String sIPAddr, NativeLong lColumnAmout, String pRecvDataBuffer,int dwBufSize,int dwUser);
      }

     public static interface FColGlobalDataCallBack extends StdCallCallback {
        public void invoke(NativeLong lUserID, String sIPAddr, NativeLong lColumnAmout, String pRecvDataBuffer,int dwBufSize,int dwUser);
      }

   public static interface FJpegdataCallBack extends StdCallCallback {
        public int invoke(NativeLong lCommand, NativeLong lUserID, String sDVRIP, String sJpegName, String pJpegBuf,int dwBufLen, int dwUser);
      }

    public static interface FPostMessageCallBack extends StdCallCallback {
        public int invoke(int dwType, NativeLong lIndex);
      }

 boolean  NET_DVR_Init();
 boolean  NET_DVR_Cleanup();
 boolean  NET_DVR_SetDVRMessage(int nMessage,int hWnd);
//NET_DVR_SetDVRMessage����չ
 boolean  NET_DVR_SetExceptionCallBack_V30(int nMessage, int hWnd, FExceptionCallBack fExceptionCallBack, Pointer pUser);

 boolean  NET_DVR_SetDVRMessCallBack(FMessCallBack fMessCallBack);
 boolean  NET_DVR_SetDVRMessCallBack_EX(FMessCallBack_EX fMessCallBack_EX);
 boolean  NET_DVR_SetDVRMessCallBack_NEW(FMessCallBack_NEW fMessCallBack_NEW);
 boolean  NET_DVR_SetDVRMessageCallBack(FMessageCallBack fMessageCallBack, int dwUser);

 boolean  NET_DVR_SetDVRMessageCallBack_V30(FMSGCallBack fMessageCallBack, Pointer pUser);

 boolean  NET_DVR_SetConnectTime(int dwWaitTime, int dwTryTimes );
 boolean  NET_DVR_SetReconnect(int dwInterval, boolean bEnableRecon );
 int  NET_DVR_GetSDKVersion();
 int  NET_DVR_GetSDKBuildVersion();
 int  NET_DVR_IsSupport();
 boolean  NET_DVR_StartListen(String sLocalIP,short wLocalPort);
 boolean  NET_DVR_StopListen();

 NativeLong  NET_DVR_StartListen_V30(String sLocalIP, short wLocalPort, FMSGCallBack DataCallback , Pointer pUserData );
 boolean  NET_DVR_StopListen_V30(NativeLong lListenHandle);
 NativeLong  NET_DVR_Login(String sDVRIP,short wDVRPort,String sUserName,String sPassword,NET_DVR_DEVICEINFO lpDeviceInfo);
 NativeLong  NET_DVR_Login_V30(String sDVRIP, short wDVRPort, String sUserName, String sPassword, NET_DVR_DEVICEINFO_V30 lpDeviceInfo);
 boolean  NET_DVR_Logout(NativeLong lUserID);
 boolean  NET_DVR_Logout_V30(NativeLong lUserID);
 int  NET_DVR_GetLastError();
 String   NET_DVR_GetErrorMsg(NativeLongByReference pErrorNo );
 boolean  NET_DVR_SetShowMode(int dwShowType,int colorKey);
 boolean  NET_DVR_GetDVRIPByResolveSvr(String sServerIP, short wServerPort, String sDVRName,short wDVRNameLen,String sDVRSerialNumber,short wDVRSerialLen,String sGetIP);
 boolean   NET_DVR_GetDVRIPByResolveSvr_EX(String sServerIP, short wServerPort,  String sDVRName, short wDVRNameLen, String sDVRSerialNumber, short wDVRSerialLen,String sGetIP, IntByReference dwPort);
 boolean   NET_DVR_GetSTDAbility(NativeLong lUserID,int dwAbilityType,LPNET_DVR_STD_ABILITY lpAbilityParam);

//Ԥ����ؽӿ�
 NativeLong  NET_DVR_RealPlay(NativeLong lUserID,NET_DVR_CLIENTINFO lpClientInfo);
 NativeLong  NET_DVR_RealPlay_V30(NativeLong lUserID, NET_DVR_CLIENTINFO lpClientInfo, FRealDataCallBack_V30 fRealDataCallBack_V30, Pointer pUser , boolean bBlocked );
 boolean  NET_DVR_StopRealPlay(NativeLong lRealHandle);
 boolean  NET_DVR_RigisterDrawFun(NativeLong lRealHandle,FDrawFun fDrawFun,int dwUser);
 boolean  NET_DVR_SetPlayerBufNumber(NativeLong lRealHandle,int dwBufNum);
 boolean  NET_DVR_ThrowBFrame(NativeLong lRealHandle,int dwNum);
 boolean  NET_DVR_SetAudioMode(int dwMode);
 boolean  NET_DVR_OpenSound(NativeLong lRealHandle);
 boolean  NET_DVR_CloseSound();
 boolean  NET_DVR_OpenSoundShare(NativeLong lRealHandle);
 boolean  NET_DVR_CloseSoundShare(NativeLong lRealHandle);
 boolean  NET_DVR_Volume(NativeLong lRealHandle,short wVolume);
 boolean  NET_DVR_SaveRealData(NativeLong lRealHandle,String sFileName);
 boolean  NET_DVR_StopSaveRealData(NativeLong lRealHandle);
 boolean  NET_DVR_SetRealDataCallBack(NativeLong lRealHandle,FRowDataCallBack fRealDataCallBack,int dwUser);
 boolean  NET_DVR_SetStandardDataCallBack(NativeLong lRealHandle,FStdDataCallBack fStdDataCallBack,int dwUser);
 boolean  NET_DVR_CapturePicture(NativeLong lRealHandle,String sPicFileName);//bmp

//��̬���I֡
 boolean  NET_DVR_MakeKeyFrame(NativeLong lUserID, NativeLong lChannel);//������
 boolean  NET_DVR_MakeKeyFrameSub(NativeLong lUserID, NativeLong lChannel);//������

//��̨������ؽӿ�
 boolean  NET_DVR_PTZControl(NativeLong lRealHandle,int dwPTZCommand,int dwStop);
 boolean  NET_DVR_PTZControl_Other(NativeLong lUserID,NativeLong lChannel,int dwPTZCommand,int dwStop);
 boolean  NET_DVR_TransPTZ(NativeLong lRealHandle,String pPTZCodeBuf,int dwBufSize);
 boolean  NET_DVR_TransPTZ_Other(NativeLong lUserID,NativeLong lChannel,String pPTZCodeBuf,int dwBufSize);
 boolean  NET_DVR_PTZPreset(NativeLong lRealHandle,int dwPTZPresetCmd,int dwPresetIndex);
 boolean  NET_DVR_PTZPreset_Other(NativeLong lUserID,NativeLong lChannel,int dwPTZPresetCmd,int dwPresetIndex);
 boolean  NET_DVR_TransPTZ_EX(NativeLong lRealHandle,String pPTZCodeBuf,int dwBufSize);
 boolean  NET_DVR_PTZControl_EX(NativeLong lRealHandle,int dwPTZCommand,int dwStop);
 boolean  NET_DVR_PTZPreset_EX(NativeLong lRealHandle,int dwPTZPresetCmd,int dwPresetIndex);
 boolean  NET_DVR_PTZCruise(NativeLong lRealHandle,int dwPTZCruiseCmd,byte byCruiseRoute, byte byCruisePoint, short wInput);
 boolean  NET_DVR_PTZCruise_Other(NativeLong lUserID,NativeLong lChannel,int dwPTZCruiseCmd,byte byCruiseRoute, byte byCruisePoint, short wInput);
 boolean  NET_DVR_PTZCruise_EX(NativeLong lRealHandle,int dwPTZCruiseCmd,byte byCruiseRoute, byte byCruisePoint, short wInput);
 boolean  NET_DVR_PTZTrack(NativeLong lRealHandle, int dwPTZTrackCmd);
 boolean  NET_DVR_PTZTrack_Other(NativeLong lUserID, NativeLong lChannel, int dwPTZTrackCmd);
 boolean  NET_DVR_PTZTrack_EX(NativeLong lRealHandle, int dwPTZTrackCmd);
 boolean  NET_DVR_PTZControlWithSpeed(NativeLong lRealHandle, int dwPTZCommand, int dwStop, int dwSpeed);
 boolean  NET_DVR_PTZControlWithSpeed_Other(NativeLong lUserID, NativeLong lChannel, int dwPTZCommand, int dwStop, int dwSpeed);
 boolean  NET_DVR_PTZControlWithSpeed_EX(NativeLong lRealHandle, int dwPTZCommand, int dwStop, int dwSpeed);
 boolean  NET_DVR_GetPTZCruise(NativeLong lUserID,NativeLong lChannel,NativeLong lCruiseRoute, NET_DVR_CRUISE_RET lpCruiseRet);
 boolean  NET_DVR_PTZMltTrack(NativeLong lRealHandle,int dwPTZTrackCmd, int dwTrackIndex);
 boolean  NET_DVR_PTZMltTrack_Other(NativeLong lUserID,NativeLong lChannel,int dwPTZTrackCmd, int dwTrackIndex);
 boolean  NET_DVR_PTZMltTrack_EX(NativeLong lRealHandle,int dwPTZTrackCmd, int dwTrackIndex);

//�ļ�������ط�
 NativeLong  NET_DVR_FindFile(NativeLong lUserID,NativeLong lChannel,int dwFileType, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime);
 NativeLong  NET_DVR_FindNextFile(NativeLong lFindHandle,NET_DVR_FIND_DATA lpFindData);
 boolean  NET_DVR_FindClose(NativeLong lFindHandle);
 NativeLong  NET_DVR_FindNextFile_V30(NativeLong lFindHandle, NET_DVR_FINDDATA_V30 lpFindData);
 NativeLong  NET_DVR_FindFile_V30(NativeLong lUserID, NET_DVR_FILECOND pFindCond);
 boolean  NET_DVR_FindClose_V30(NativeLong lFindHandle);
//2007-04-16���Ӳ�ѯ����ŵ��ļ�����
 NativeLong  NET_DVR_FindNextFile_Card(NativeLong lFindHandle, NET_DVR_FINDDATA_CARD lpFindData);
 NativeLong  NET_DVR_FindFile_Card(NativeLong lUserID, NativeLong lChannel, int dwFileType, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime);
 boolean  NET_DVR_LockFileByName(NativeLong lUserID, String sLockFileName);
 boolean  NET_DVR_UnlockFileByName(NativeLong lUserID, String sUnlockFileName);
 NativeLong  NET_DVR_PlayBackByName(NativeLong lUserID,String sPlayBackFileName, HWND hWnd);
 NativeLong  NET_DVR_PlayBackByTime(NativeLong lUserID,NativeLong lChannel, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime, HWND hWnd);
 boolean  NET_DVR_PlayBackControl(NativeLong lPlayHandle,int dwControlCode,int dwInValue,IntByReference LPOutValue);
 boolean  NET_DVR_StopPlayBack(NativeLong lPlayHandle);
 boolean  NET_DVR_SetPlayDataCallBack(NativeLong lPlayHandle,FPlayDataCallBack fPlayDataCallBack,int dwUser);
 boolean  NET_DVR_PlayBackSaveData(NativeLong lPlayHandle,String sFileName);
 boolean  NET_DVR_StopPlayBackSave(NativeLong lPlayHandle);
 boolean  NET_DVR_GetPlayBackOsdTime(NativeLong lPlayHandle, NET_DVR_TIME lpOsdTime);
 boolean  NET_DVR_PlayBackCaptureFile(NativeLong lPlayHandle,String sFileName);
 NativeLong  NET_DVR_GetFileByName(NativeLong lUserID,String sDVRFileName,String sSavedFileName);
 NativeLong  NET_DVR_GetFileByTime(NativeLong lUserID,NativeLong lChannel, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime, String sSavedFileName);
 boolean  NET_DVR_StopGetFile(NativeLong lFileHandle);
 int  NET_DVR_GetDownloadPos(NativeLong lFileHandle);
 int	 NET_DVR_GetPlayBackPos(NativeLong lPlayHandle);

//��
 NativeLong  NET_DVR_Upgrade(NativeLong lUserID, String sFileName);
 int  NET_DVR_GetUpgradeState(NativeLong lUpgradeHandle);
 int  NET_DVR_GetUpgradeProgress(NativeLong lUpgradeHandle);
 boolean  NET_DVR_CloseUpgradeHandle(NativeLong lUpgradeHandle);
 boolean  NET_DVR_SetNetworkEnvironment(int dwEnvironmentLevel);
//Զ�̸�ʽ��Ӳ��
 NativeLong  NET_DVR_FormatDisk(NativeLong lUserID,NativeLong lDiskNumber);
 boolean  NET_DVR_GetFormatProgress(NativeLong lFormatHandle, NativeLongByReference pCurrentFormatDisk,NativeLongByReference pCurrentDiskPos,NativeLongByReference pFormatStatic);
 boolean  NET_DVR_CloseFormatHandle(NativeLong lFormatHandle);
//����
 NativeLong  NET_DVR_SetupAlarmChan(NativeLong lUserID);
 boolean  NET_DVR_CloseAlarmChan(NativeLong lAlarmHandle);
 NativeLong  NET_DVR_SetupAlarmChan_V30(NativeLong lUserID);
 boolean  NET_DVR_CloseAlarmChan_V30(NativeLong lAlarmHandle);
//�����Խ�
 NativeLong  NET_DVR_StartVoiceCom(NativeLong lUserID, FVoiceDataCallBack fVoiceDataCallBack, int dwUser);
 NativeLong  NET_DVR_StartVoiceCom_V30(NativeLong lUserID, int dwVoiceChan, boolean bNeedCBNoEncData, FVoiceDataCallBack_V30 fVoiceDataCallBack, Pointer pUser);
 boolean  NET_DVR_SetVoiceComClientVolume(NativeLong lVoiceComHandle, short wVolume);
 boolean  NET_DVR_StopVoiceCom(NativeLong lVoiceComHandle);
//����ת��
 NativeLong  NET_DVR_StartVoiceCom_MR(NativeLong lUserID, FVoiceDataCallBack_MR fVoiceDataCallBack, int dwUser);
 NativeLong  NET_DVR_StartVoiceCom_MR_V30(NativeLong lUserID, int dwVoiceChan, FVoiceDataCallBack_MR_V30 fVoiceDataCallBack, Pointer pUser);
 boolean  NET_DVR_VoiceComSendData(NativeLong lVoiceComHandle, String pSendBuf, int dwBufSize);

//�����㲥
 boolean  NET_DVR_ClientAudioStart();
 boolean  NET_DVR_ClientAudioStart_V30(FVoiceDataCallBack2 fVoiceDataCallBack2, Pointer pUser);
 boolean  NET_DVR_ClientAudioStop();
 boolean  NET_DVR_AddDVR(NativeLong lUserID);
 NativeLong  NET_DVR_AddDVR_V30(NativeLong lUserID, int dwVoiceChan);
 boolean  NET_DVR_DelDVR(NativeLong lUserID);
 boolean  NET_DVR_DelDVR_V30(NativeLong lVoiceHandle);
////////////////////////////////////////////////////////////
//͸��ͨ������
 NativeLong  NET_DVR_SerialStart(NativeLong lUserID,NativeLong lSerialPort,FSerialDataCallBack fSerialDataCallBack,int dwUser);
//485��Ϊ͸��ͨ��ʱ����Ҫָ��ͨ���ţ���Ϊ��ͬͨ����485�����ÿ��Բ�ͬ(���粨����)
 boolean  NET_DVR_SerialSend(NativeLong lSerialHandle, NativeLong lChannel, String pSendBuf,int dwBufSize);
 boolean  NET_DVR_SerialStop(NativeLong lSerialHandle);
 boolean  NET_DVR_SendTo232Port(NativeLong lUserID, String pSendBuf, int dwBufSize);
 boolean  NET_DVR_SendToSerialPort(NativeLong lUserID, int dwSerialPort, int dwSerialIndex, String pSendBuf, int dwBufSize);

//���� nBitrate = 16000
 Pointer  NET_DVR_InitG722Decoder(int nBitrate);
 void  NET_DVR_ReleaseG722Decoder(Pointer pDecHandle);
 boolean  NET_DVR_DecodeG722Frame(Pointer pDecHandle, String pInBuffer, String pOutBuffer);
//����
 Pointer  NET_DVR_InitG722Encoder();
 boolean  NET_DVR_EncodeG722Frame(Pointer pEncodeHandle,String pInBuff,String pOutBuffer);
 void  NET_DVR_ReleaseG722Encoder(Pointer pEncodeHandle);

//Զ�̿��Ʊ�����ʾ
 boolean  NET_DVR_ClickKey(NativeLong lUserID, NativeLong lKeyIndex);
//Զ�̿����豸���ֶ�¼��
 boolean  NET_DVR_StartDVRRecord(NativeLong lUserID,NativeLong lChannel,NativeLong lRecordType);
 boolean  NET_DVR_StopDVRRecord(NativeLong lUserID,NativeLong lChannel);
//���뿨
 boolean  NET_DVR_InitDevice_Card(NativeLongByReference pDeviceTotalChan);
 boolean  NET_DVR_ReleaseDevice_Card();
 boolean  NET_DVR_InitDDraw_Card(int hParent,int colorKey);
 boolean  NET_DVR_ReleaseDDraw_Card();
 NativeLong  NET_DVR_RealPlay_Card(NativeLong lUserID,NET_DVR_CARDINFO lpCardInfo,NativeLong lChannelNum);
 boolean  NET_DVR_ResetPara_Card(NativeLong lRealHandle,NET_DVR_DISPLAY_PARA lpDisplayPara);
 boolean  NET_DVR_RefreshSurface_Card();
 boolean  NET_DVR_ClearSurface_Card();
 boolean  NET_DVR_RestoreSurface_Card();
 boolean  NET_DVR_OpenSound_Card(NativeLong lRealHandle);
 boolean  NET_DVR_CloseSound_Card(NativeLong lRealHandle);
 boolean  NET_DVR_SetVolume_Card(NativeLong lRealHandle,short wVolume);
 boolean  NET_DVR_AudioPreview_Card(NativeLong lRealHandle,boolean bEnable);
 NativeLong  NET_DVR_GetCardLastError_Card();
 Pointer  NET_DVR_GetChanHandle_Card(NativeLong lRealHandle);
 boolean  NET_DVR_CapturePicture_Card(NativeLong lRealHandle, String sPicFileName);
//��ȡ���뿨���кŴ˽ӿ���Ч������GetBoardDetail�ӿڻ��(2005-12-08֧��)
 boolean  NET_DVR_GetSerialNum_Card(NativeLong lChannelNum,IntByReference pDeviceSerialNo);
//��־
 NativeLong  NET_DVR_FindDVRLog(NativeLong lUserID, NativeLong lSelectMode, int dwMajorType,int dwMinorType, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime);
 NativeLong  NET_DVR_FindNextLog(NativeLong lLogHandle, NET_DVR_LOG lpLogData);
 boolean  NET_DVR_FindLogClose(NativeLong lLogHandle);
 NativeLong  NET_DVR_FindDVRLog_V30(NativeLong lUserID, NativeLong lSelectMode, int dwMajorType,int dwMinorType, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime, boolean bOnlySmart );
 NativeLong  NET_DVR_FindNextLog_V30(NativeLong lLogHandle, NET_DVR_LOG_V30 lpLogData);
 boolean  NET_DVR_FindLogClose_V30(NativeLong lLogHandle);
//��ֹ2004��8��5��,��113���ӿ�
//ATM DVR
 NativeLong  NET_DVR_FindFileByCard(NativeLong lUserID,NativeLong lChannel,int dwFileType, int nFindType, String sCardNumber, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime);
//��ֹ2004��10��5��,��116���ӿ�

//2005-09-15
 boolean  NET_DVR_CaptureJPEGPicture(NativeLong lUserID, NativeLong lChannel, NET_DVR_JPEGPARA lpJpegPara, String sPicFileName);
//JPEGץͼ���ڴ�
 boolean  NET_DVR_CaptureJPEGPicture_NEW(NativeLong lUserID, NativeLong lChannel, NET_DVR_JPEGPARA lpJpegPara, String sJpegPicBuffer, int dwPicSize,  IntByReference lpSizeReturned);


//2006-02-16
 int  NET_DVR_GetRealPlayerIndex(NativeLong lRealHandle);
 int  NET_DVR_GetPlayBackPlayerIndex(NativeLong lPlayHandle);

//2006-08-28 704-640 ��������
 boolean  NET_DVR_SetScaleCFG(NativeLong lUserID, int dwScale);
 boolean  NET_DVR_GetScaleCFG(NativeLong lUserID, IntByReference lpOutScale);
 boolean  NET_DVR_SetScaleCFG_V30(NativeLong lUserID, NET_DVR_SCALECFG pScalecfg);
 boolean  NET_DVR_GetScaleCFG_V30(NativeLong lUserID, NET_DVR_SCALECFG pScalecfg);
//2006-08-28 ATM��˿�����
 boolean  NET_DVR_SetATMPortCFG(NativeLong lUserID, short wATMPort);
 boolean  NET_DVR_GetATMPortCFG(NativeLong lUserID, ShortByReference LPOutATMPort);

//2006-11-10 ֧���Կ��������
 boolean  NET_DVR_InitDDrawDevice();
 boolean  NET_DVR_ReleaseDDrawDevice();
 NativeLong  NET_DVR_GetDDrawDeviceTotalNums();
 boolean  NET_DVR_SetDDrawDevice(NativeLong lPlayPort, int nDeviceNum);

 boolean  NET_DVR_PTZSelZoomIn(NativeLong lRealHandle, NET_DVR_POINT_FRAME pStruPointFrame);
 boolean  NET_DVR_PTZSelZoomIn_EX(NativeLong lUserID, NativeLong lChannel, NET_DVR_POINT_FRAME pStruPointFrame);

//�����豸DS-6001D/DS-6001F
 boolean  NET_DVR_StartDecode(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECODERINFO lpDecoderinfo);
 boolean  NET_DVR_StopDecode(NativeLong lUserID, NativeLong lChannel);
 boolean  NET_DVR_GetDecoderState(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECODERSTATE lpDecoderState);
//2005-08-01
 boolean  NET_DVR_SetDecInfo(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCFG lpDecoderinfo);
 boolean  NET_DVR_GetDecInfo(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCFG lpDecoderinfo);
 boolean  NET_DVR_SetDecTransPort(NativeLong lUserID, NET_DVR_PORTCFG lpTransPort);
 boolean  NET_DVR_GetDecTransPort(NativeLong lUserID, NET_DVR_PORTCFG lpTransPort);
 boolean  NET_DVR_DecPlayBackCtrl(NativeLong lUserID, NativeLong lChannel, int dwControlCode, int dwInValue,IntByReference LPOutValue, NET_DVR_PLAYREMOTEFILE lpRemoteFileInfo);
 boolean  NET_DVR_StartDecSpecialCon(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCHANINFO lpDecChanInfo);
 boolean  NET_DVR_StopDecSpecialCon(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCHANINFO lpDecChanInfo);
 boolean  NET_DVR_DecCtrlDec(NativeLong lUserID, NativeLong lChannel, int dwControlCode);
 boolean  NET_DVR_DecCtrlScreen(NativeLong lUserID, NativeLong lChannel, int dwControl);
 boolean  NET_DVR_GetDecCurLinkStatus(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECSTATUS lpDecStatus);

//��·������
//2007-11-30 V211֧�����½ӿ� //11
 boolean  NET_DVR_MatrixStartDynamic(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DYNAMIC_DEC lpDynamicInfo);
 boolean  NET_DVR_MatrixStopDynamic(NativeLong lUserID, int dwDecChanNum);
 boolean  NET_DVR_MatrixGetDecChanInfo(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_CHAN_INFO lpInter);
 boolean  NET_DVR_MatrixSetLoopDecChanInfo(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_LOOP_DECINFO lpInter);
 boolean  NET_DVR_MatrixGetLoopDecChanInfo(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_LOOP_DECINFO lpInter);
 boolean  NET_DVR_MatrixSetLoopDecChanEnable(NativeLong lUserID, int dwDecChanNum, int dwEnable);
 boolean  NET_DVR_MatrixGetLoopDecChanEnable(NativeLong lUserID, int dwDecChanNum, IntByReference lpdwEnable);
 boolean  NET_DVR_MatrixGetLoopDecEnable(NativeLong lUserID, IntByReference lpdwEnable);
 boolean  NET_DVR_MatrixSetDecChanEnable(NativeLong lUserID, int dwDecChanNum, int dwEnable);
 boolean  NET_DVR_MatrixGetDecChanEnable(NativeLong lUserID, int dwDecChanNum, IntByReference lpdwEnable);
 boolean  NET_DVR_MatrixGetDecChanStatus(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_CHAN_STATUS lpInter);
//2007-12-22 ����֧�ֽӿ� //18
 boolean  NET_DVR_MatrixSetTranInfo(NativeLong lUserID, NET_DVR_MATRIX_TRAN_CHAN_CONFIG lpTranInfo);
 boolean  NET_DVR_MatrixGetTranInfo(NativeLong lUserID, NET_DVR_MATRIX_TRAN_CHAN_CONFIG lpTranInfo);
 boolean  NET_DVR_MatrixSetRemotePlay(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_REMOTE_PLAY lpInter);
 boolean  NET_DVR_MatrixSetRemotePlayControl(NativeLong lUserID, int dwDecChanNum, int dwControlCode, int dwInValue, IntByReference LPOutValue);
 boolean  NET_DVR_MatrixGetRemotePlayStatus(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_REMOTE_PLAY_STATUS lpOuter);
//end
 boolean  NET_DVR_RefreshPlay(NativeLong lPlayHandle);
//�ָ�Ĭ��ֵ
 boolean  NET_DVR_RestoreConfig(NativeLong lUserID);
//�������
 boolean  NET_DVR_SaveConfig(NativeLong lUserID);
//����
 boolean  NET_DVR_RebootDVR(NativeLong lUserID);
//�ر�DVR
 boolean  NET_DVR_ShutDownDVR(NativeLong lUserID);
//�������� begin
 boolean  NET_DVR_GetDVRConfig(NativeLong lUserID, int dwCommand,NativeLong lChannel, Pointer lpOutBuffer, int dwOutBufferSize, IntByReference lpBytesReturned);
 boolean  NET_DVR_SetDVRConfig(NativeLong lUserID, int dwCommand,NativeLong lChannel, Pointer lpInBuffer, int dwInBufferSize);
 boolean  NET_DVR_GetDVRWorkState_V30(NativeLong lUserID, NET_DVR_WORKSTATE_V30 lpWorkState);
 boolean  NET_DVR_GetDVRWorkState(NativeLong lUserID, NET_DVR_WORKSTATE lpWorkState);
 boolean  NET_DVR_SetVideoEffect(NativeLong lUserID, NativeLong lChannel, int dwBrightValue, int dwContrastValue, int dwSaturationValue, int dwHueValue);
 boolean  NET_DVR_GetVideoEffect(NativeLong lUserID, NativeLong lChannel, IntByReference pBrightValue, IntByReference pContrastValue, IntByReference pSaturationValue, IntByReference pHueValue);
 boolean  NET_DVR_ClientGetframeformat(NativeLong lUserID, NET_DVR_FRAMEFORMAT lpFrameFormat);
 boolean  NET_DVR_ClientSetframeformat(NativeLong lUserID, NET_DVR_FRAMEFORMAT lpFrameFormat);
 boolean  NET_DVR_ClientGetframeformat_V30(NativeLong lUserID, NET_DVR_FRAMEFORMAT_V30 lpFrameFormat);
 boolean  NET_DVR_ClientSetframeformat_V30(NativeLong lUserID, NET_DVR_FRAMEFORMAT_V30 lpFrameFormat);
 boolean  NET_DVR_GetAlarmOut_V30(NativeLong lUserID, NET_DVR_ALARMOUTSTATUS_V30 lpAlarmOutState);
 boolean  NET_DVR_GetAlarmOut(NativeLong lUserID, NET_DVR_ALARMOUTSTATUS lpAlarmOutState);
 boolean  NET_DVR_SetAlarmOut(NativeLong lUserID, NativeLong lAlarmOutPort,NativeLong lAlarmOutStatic);

//��Ƶ�������
 boolean  NET_DVR_ClientSetVideoEffect(NativeLong lRealHandle,int dwBrightValue,int dwContrastValue, int dwSaturationValue,int dwHueValue);
 boolean  NET_DVR_ClientGetVideoEffect(NativeLong lRealHandle,IntByReference pBrightValue,IntByReference pContrastValue, IntByReference pSaturationValue,IntByReference pHueValue);

//�����ļ�
 boolean  NET_DVR_GetConfigFile(NativeLong lUserID, String sFileName);
 boolean  NET_DVR_SetConfigFile(NativeLong lUserID, String sFileName);
 boolean  NET_DVR_GetConfigFile_V30(NativeLong lUserID, String sOutBuffer, int dwOutSize, IntByReference pReturnSize);

 boolean  NET_DVR_GetConfigFile_EX(NativeLong lUserID, String sOutBuffer, int dwOutSize);
 boolean  NET_DVR_SetConfigFile_EX(NativeLong lUserID, String sInBuffer, int dwInSize);

//������־�ļ�д��ӿ�
 boolean  NET_DVR_SetLogToFile(boolean bLogEnable , String  strLogDir, boolean bAutoDel );
 boolean  NET_DVR_GetSDKState( NET_DVR_SDKSTATE pSDKState);
 boolean  NET_DVR_GetSDKAbility( NET_DVR_SDKABL pSDKAbl);
 boolean  NET_DVR_GetPTZProtocol(NativeLong lUserID, NET_DVR_PTZCFG  pPtzcfg);
//ǰ�����
 boolean  NET_DVR_LockPanel(NativeLong lUserID);
 boolean  NET_DVR_UnLockPanel(NativeLong lUserID);

 boolean  NET_DVR_SetRtspConfig(NativeLong lUserID, int dwCommand,  NET_DVR_RTSPCFG lpInBuffer, int dwInBufferSize);
 boolean  NET_DVR_GetRtspConfig(NativeLong lUserID, int dwCommand,  NET_DVR_RTSPCFG lpOutBuffer, int dwOutBufferSize);
}

//���ſ⺯������,PlayCtrl.dll
/*interface PlayCtrl extends StdCallLibrary
{
    PlayCtrl INSTANCE = (PlayCtrl) Native.loadLibrary("PlayCtrl",
            PlayCtrl.class);

    public static final int STREAME_REALTIME = 0;
    public static final int STREAME_FILE = 1;

    boolean PlayM4_GetPort(NativeLongByReference nPort);
    boolean PlayM4_OpenStream(NativeLong nPort, ByteByReference pFileHeadBuf, int nSize, int nBufPoolSize);
    boolean PlayM4_InputData(NativeLong nPort, ByteByReference pBuf, int nSize);
    boolean PlayM4_CloseStream(NativeLong nPort);
    boolean PlayM4_SetStreamOpenMode(NativeLong nPort, int nMode);
    boolean PlayM4_Play(NativeLong nPort, HWND hWnd);
    boolean PlayM4_Stop(NativeLong nPort);
    boolean PlayM4_SetSecretKey(NativeLong nPort, NativeLong lKeyType, String pSecretKey, NativeLong lKeyLen);
}*/

//windows gdi�ӿ�,gdi32.dll in system32 folder, �������ڵ�����,�ƶ��������������ʹ��
interface GDI32 extends W32API
{
    GDI32 INSTANCE = (GDI32) Native.loadLibrary("gdi32", GDI32.class, DEFAULT_OPTIONS);

    public static final int TRANSPARENT = 1;

    int SetBkMode(HDC hdc, int i);

    HANDLE CreateSolidBrush(int icolor);
}

//windows user32�ӿ�,user32.dll in system32 folder, �������ڵ�����,�ƶ��������������ʹ��
interface USER32 extends W32API
{

    USER32 INSTANCE = (USER32) Native.loadLibrary("user32", USER32.class, DEFAULT_OPTIONS);

    public static final int BF_LEFT = 0x0001;
    public static final int BF_TOP = 0x0002;
    public static final int BF_RIGHT = 0x0004;
    public static final int BF_BOTTOM = 0x0008;
    public static final int BDR_SUNKENOUTER = 0x0002;
    public static final int BF_RECT = (BF_LEFT | BF_TOP | BF_RIGHT | BF_BOTTOM);

    boolean DrawEdge(HDC hdc, RECT qrc, int edge, int grfFlags);

    int FillRect(HDC hDC, RECT lprc, HANDLE hbr);
}
