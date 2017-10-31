package video.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import cn.com.lanlyc.base.util.DataUtils;
import video.cn.com.lanlyc.core.dao.BaseSettingDao;
import video.cn.com.lanlyc.core.dao.VideoSettingDao;
import video.cn.com.lanlyc.core.dao.VidiconSettingDao;
import video.cn.com.lanlyc.core.po.BaseSetting;
import video.cn.com.lanlyc.core.po.VideoSetting;
import video.cn.com.lanlyc.core.po.VidiconSetting;
import video.cn.com.lanlyc.core.sdk.HCNetSDK;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_CLIENTINFO;
import video.cn.com.lanlyc.core.sdk.HCNetSDK.NET_DVR_PICCFG_V30;

/***
 * 
 * @author 胡志浩
 * @date 2017年9月11日
 * @version 1.0
 * @Title: 系统设置Service层，
 */
@Service
public class SystemService {


	private boolean flag = false;
	
	HCNetSDK hns = HCNetSDK.INSTANCE; // 必须通过HCNetSDK类调用其他DLL
	@Autowired
	NetSdkLogin nsl;  // 初始化DLL公共方法
	@Autowired
	BaseSettingDao baseSettingDao; //初始化BaseSettingDao
	@Autowired
	VideoSettingDao videoSettingDao; //初始化VideoSettingDao
	@Autowired
	VidiconSettingDao vidiconSettingDao; //初始化VidiconSettingDao
	
	HCNetSDK.NET_DVR_CLIENTINFO clientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();//用户参数
	/***
	 * 设置预览视频显示参数。亮度、对比度、饱和度、色度
	 * 
	 * @param String
	 *            ip ip地址
	 * @param int
	 *            prot 端口号
	 * @param String
	 *            username 用户名
	 * @param String
	 *            password 用户密码
	 * @param int
	 *            dwBrightValue 亮度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
	 * @param int
	 *            dwContrastValue 对比度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
	 * @param int
	 *            dwSaturationValue 饱和度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
	 * @param int
	 *            dwHueValue 色度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为1
	 * 
	 * @return Boolean True
	 * @return Boolean False
	 */
	public boolean ClientSetVideoEffect(String ip, short port, String username, String password, int dwBrightValue,
			int dwContrastValue, int dwSaturationValue, int dwHueValue) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else{
			userId = nsl.loginSDK(ip, port, username, password);
			HCNetSDK.NET_DVR_CLIENTINFO cltentinfo = new NET_DVR_CLIENTINFO(); // 初始化预览参数结构体
			cltentinfo.lChannel = new NativeLong(1); //通道号
			cltentinfo.lLinkMode = new NativeLong(0); //为0表示主码流，为1表示子码流；
			cltentinfo.hPlayWnd = null;
			cltentinfo.sMultiCastIP = null;
			int previewCode = nsl.previewSDK(userId);
			if (previewCode == -1) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换
				NativeLong lpClientInfo = hns.NET_DVR_RealPlay(viewCode, cltentinfo); // TODO:传值为登陆后的id，和预览参数实例
				flag = hns.NET_DVR_ClientSetVideoEffect(lpClientInfo, // NET_DVR_RealPlay或者NET_DVR_RealPlay_V30的返回值
						dwBrightValue, 		// 亮度，取默值范围[1,10]，小于1的值认为1，大于10的值默认为10
						dwContrastValue, 	// 对比度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
						dwSaturationValue, 	// 饱和度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
						dwHueValue);	 	// 色度，取值范围[1,10]，小于1的值默认为1，大于10的值默认为10
				//停止预览
				if (!(hns.NET_DVR_StopRealPlay(viewCode))){
					System.out.println("SDK停止预览失败--错误信息---：" + nsl.errorCode());				
				}
				// 注销SDK
				if (nsl.logoutSDK(previewCode) == -1) {
					System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
				}
				// 释放SDK资源
				if (nsl.cleanSDK() == -1) {
					System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
				}
				if (!flag) {
					System.out.println("----------错误信息----------：" + nsl.errorCode());
				}
			}
		}
		return flag;
	}
	
	
	
	/***
	 * 设置设备的配置信息，初始预览时的数目、是否声音预览，预览切换时间
	 * 
	 * @param ip	IP地址
	 * @param port	端口号
	 * @param username	用户名
	 * @param password	密码
	 * @param byPreviewNumber	预览数目：0-1画面，1-4画面，2-9画面，3-16画面，0xff-最大画面
	 * @param byEnableAudio		是否声音预览：0-不预览，1-预览
	 * @param wSwitchTime		预览切换时间：0-不切换，1-5s，2-10s，3-20s，4-30s，5-60s，6-120s，7-300s 
	 * 
	 * @return Boolean True
	 * @return Boolean False
	 */
	public boolean DVR_SetDVRConfig(String ip, short port, String username, String password, byte byPreviewNumber,
			byte byEnableAudio, byte wSwitchTime) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);
			int previewCode = nsl.previewSDK(userId);
			if (previewCode != 0) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换

				HCNetSDK.NET_DVR_PREVIEWCFG_V30 dvr_preViewCFG = new HCNetSDK.NET_DVR_PREVIEWCFG_V30(); // 初始化
				dvr_preViewCFG.dwSize = 0; // TODO：设置结构体大小
				dvr_preViewCFG.byPreviewNumber = byPreviewNumber; // TODO：预览数目：0-1画面，1-4画面，2-9画面，3-16画面，0xff-最大画面
				dvr_preViewCFG.byEnableAudio = byEnableAudio; // TODO:是否声音预览：0-不预览，1-预览
				dvr_preViewCFG.wSwitchTime = wSwitchTime; // TODO:预览切换时间：0-不切换，1-5s，2-10s，3-20s，4-30s，5-60s，6-120s，7-300s

				NativeLong lChannel = null; // TODO: 通道号，不同的命令对应不同的取值，暂时不知道写啥
				Pointer lpInBuffer = null; 	// TODO:输入数据的缓冲指针，暂时不知道写啥
				int dwInBufferSize = 0; 	// TODO:输入数据的缓冲长度(以字节为单位)，暂时不知道写啥
				flag = hns.NET_DVR_SetDVRConfig(viewCode, 0, lChannel, lpInBuffer, dwInBufferSize);

				// 注销SDK
				if (nsl.logoutSDK(previewCode) == -1) {
					System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
				}
				// 释放SDK资源
				if (nsl.cleanSDK() == -1) {
					System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
				}
				if (!flag) {
					System.out.println("----------错误信息----------：" + nsl.errorCode());
				}
			}
		}
		return flag;
	}

	
	
	/***
	 * 恢复设备默认参数。
	 * 
	 * @param ip		ip地址
	 * @param port		端口号
	 * @param username	用户名
	 * @param password	密码
	 *
	 * @return Boolean True
	 * @return Boolean False
	 */
	public Boolean DVR_RestoreConfig(String ip, short port, String username, String password) {
		Integer userId = null;

		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);

			int previewCode = nsl.previewSDK(userId);
			if (previewCode != 0) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换

				flag = hns.NET_DVR_RestoreConfig(viewCode);
				// 注销SDK
				if (nsl.logoutSDK(previewCode) == -1) {
					System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
				}
				// 释放SDK资源
				if (nsl.cleanSDK() == -1) {
					System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
				}
				if (!flag) {
					System.out.println("----------错误信息----------：" + nsl.errorCode());
				}
			}
		}
		return flag;
	}
	
	
	
	/***
	 * 设置声音播放模式 1－独占声卡，单路音频模式；2－共享声卡，多路音频模式
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param dwMode
	 *            声音播放模式：1－独占声卡，单路音频模式；2－共享声卡，多路音频模式
	 * @return
	 */
	public Boolean DVR_SetAudioMode(String ip, short port, String username, String password, int dwMode) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);

			int previewCode = nsl.previewSDK(userId);
			if (previewCode != 0) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误

				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换
				flag = hns.NET_DVR_SetAudioMode(dwMode);// 声音播放模式：1－独占声卡，单路音频模式；2－共享声卡，多路音频模式
				// 注销SDK
				if (nsl.logoutSDK(previewCode) == -1) {
					System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
				}
				// 释放SDK资源
				if (nsl.cleanSDK() == -1) {
					System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
				}
				if (!flag) {
					System.out.println("----------错误信息----------：" + nsl.errorCode());
				}
			}
		}
		return flag;
	}


	/***
	 * 设置设备播放音量大小
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param wVolume
	 *            音量，取值范围[0,0xffff]
	 * 
	 * @return
	 */
	public Boolean DVR_Volume(String ip, short port, String username, String password, int wVolume) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);

			int previewCode = nsl.previewSDK(userId);
			if (previewCode != 0) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换

				HCNetSDK.NET_DVR_CLIENTINFO clientinfo = new HCNetSDK.NET_DVR_CLIENTINFO(); // 初始化预览参数结构体

				NativeLong lRealHandle = hns.NET_DVR_RealPlay(viewCode, clientinfo);
				flag = hns.NET_DVR_Volume(lRealHandle, (short) wVolume); // 音量：取值范围：[0,0xffff]

				// 注销SDK
				if (nsl.logoutSDK(previewCode) == -1) {
					System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
				}
				// 释放SDK资源
				if (nsl.cleanSDK() == -1) {
					System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
				}
				if (!flag) {
					System.out.println("----------错误信息----------：" + nsl.errorCode());
				}
			}
		}
		return flag;
	}



	/***
	 * 设置图像参数
	 * 视频制式：0-支持，1-NTSC，2-PAL
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean DVR_SetDVRConfig(String ip, short port, String username, String password) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);

			int previewCode = nsl.previewSDK(userId);
			if (previewCode != 0) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换
				HCNetSDK.NET_DVR_PICCFG_V30 dvr_piccfg = new HCNetSDK.NET_DVR_PICCFG_V30(); // 初始化通道图像参数结构体
				// NativeLong dwCommand = new NativeLong(1002);	//TODO:NET_DVR_PICCFG_V30的默认值
				NativeLong lChannel = new NativeLong(previewCode); // TODO:通道号是指设备视频通道号，通过注册设备（NET_DVR_Login_V30）返回的设备信息
				Pointer lpInBuffer = null; // TODO:输入数据的缓冲指针
				int dwInBufferSize = 1; // TODO:输入数据的缓冲长度(以字节为单位)
				flag = hns.NET_DVR_SetDVRConfig(viewCode, hns.NET_DVR_GET_PICCFG_V30, lChannel, lpInBuffer,
						dwInBufferSize);
			}
			// 注销SDK
			if (nsl.logoutSDK(previewCode) == -1) {
				System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
			}
			// 释放SDK资源
			if (nsl.cleanSDK() == -1) {
				System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
			}
			if (!flag) {
				System.out.println("----------错误信息----------：" + nsl.errorCode());
			}
		}
		return flag;

	}
	
	
	
							//*****************摄像头设置*******************//
	
	/***
	 * 云台控制操作摄像头焦点(需先启动图象预览)。
	 * 
	 * @param ip
	 * 
	 * @param port
	 * @param username
	 * @param password
	 * @param int
	 *            dwPTZCommand 云台控制焦点动作：13、焦点前调。14、焦点后调
	 * @param int
	 *            dwStop 云台停止动作或开始动作：0－开始，1－停止
	 * @return
	 */
	public boolean DVR_PTZControl(String ip, short port, String username, String password, int dwPTZCommand) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else {
			userId = nsl.loginSDK(ip, port, username, password);
			int previewCode = nsl.previewSDK(userId);
			if (previewCode == -1) {
				System.out.println("SDK预览失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换
				HCNetSDK.NET_DVR_CLIENTINFO clientinfo = new HCNetSDK.NET_DVR_CLIENTINFO(); // TODO:初始化预览参数结构体
				clientinfo.lChannel = new NativeLong(1); //通道号
				clientinfo.lLinkMode = new NativeLong(0); //为0表示主码流，为1表示子码流；
				clientinfo.hPlayWnd = null;
				clientinfo.sMultiCastIP = null;
				NativeLong lRealHandle = hns.NET_DVR_RealPlay(viewCode, clientinfo); // 调用实时预览
				flag = hns.NET_DVR_PTZControl(lRealHandle, 
						dwPTZCommand, // 云台控制焦点动作：13、焦点前调。14、焦点后调
						0); // 云台停止动作或开始动作：0－开始，1－停止
//				flag = hns.NET_DVR_PTZControl(lRealHandle, 
//						dwPTZCommand, // 云台控制焦点动作：13、焦点前调。14、焦点后调
//						1); // 云台停止动作或开始动作：0－开始，1－停止

				// 注销SDK
				if (nsl.logoutSDK(previewCode) == -1) {
					System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
				}
				// 释放SDK资源
				if (nsl.cleanSDK() == -1) {
					System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
				}
				if (!flag) {
					System.out.println("----------错误信息----------：" + nsl.errorCode());
				}
			}
		}
		return flag;
	}
	
	
	
	/***
	 * 云台预置点操作（需先启动预览）。
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param int
	 *            dwPTZPresetCmd 操作云台预置点命令:8、设置预置点。9、清除预置点。39、转到预置点
	 * @param int
	 *            dwPresetIndex 预置点的序号（从1开始），最多支持300个预置点
	 * @return
	 */
	public boolean DVR_PTZPreset(String ip, short port, String username, String password, int dwPTZPresetCmd,
			int dwPresetIndex) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);
			int previewCode = nsl.previewSDK(userId);
			if (previewCode != 0) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换
				HCNetSDK.NET_DVR_CLIENTINFO clientinfo = new HCNetSDK.NET_DVR_CLIENTINFO(); // TODO:初始化预览参数结构体
				NativeLong lRealHandle = hns.NET_DVR_RealPlay(viewCode, clientinfo); // 调用实时预览
				flag = hns.NET_DVR_PTZPreset(lRealHandle, 
						dwPTZPresetCmd, // 操作云台预置点命令:8、设置预置点。9、清除预置点。39、转到预置点
						dwPresetIndex);// 预置点的序号（从1开始），最多支持300个预置点
			}
			// 注销SDK
			if (nsl.logoutSDK(previewCode) == -1) {
				System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
			}
			// 释放SDK资源
			if (nsl.cleanSDK() == -1) {
				System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
			}
			if (!flag) {
				System.out.println("----------错误信息----------：" + nsl.errorCode());
			}
		}
		return flag;
	}



	/***
	 * 云台巡航操作（需先启动预览）。
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param int
	 *            dwPTZCruiseCmd
	 *            操作云台巡航命令:30、将预置点加入巡航序列。31、设置巡航点停顿时间。32、设置巡航速度。33、将预置点从巡航序列中删除。
	 *            37、开始巡航。38、停止巡航
	 * @param byte
	 *            byCruiseRoute 巡航路径，最多支持32条路径（序号从1开始）
	 * @param byte
	 *            byCruisePoint 巡航点，最多支持32个点（序号从1开始）
	 * @param short
	 *            wInput 不同巡航命令时的值不同，预置点(最大300)、时间(最大255)、速度(最大40)
	 * @return
	 */
	public boolean DVR_PTZCruise(String ip, short port, String username, String password, int dwPTZCruiseCmd,
		Byte byCruiseRoute, Byte byCruisePoint, short wInput) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);
			int previewCode = nsl.previewSDK(userId);
			if (previewCode == -1) {
				System.out.println("SDK预览失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换
				HCNetSDK.NET_DVR_CLIENTINFO clientinfo = new HCNetSDK.NET_DVR_CLIENTINFO(); // TODO:初始化预览参数结构体
				clientinfo.lChannel = new NativeLong(1); //通道号
				clientinfo.lLinkMode = new NativeLong(0); //为0表示主码流，为1表示子码流；
				clientinfo.hPlayWnd = null;
				clientinfo.sMultiCastIP = null;
				NativeLong lRealHandle = hns.NET_DVR_RealPlay(viewCode, clientinfo); // 调用实时预览
				flag = hns.NET_DVR_PTZCruise(lRealHandle, 
						dwPTZCruiseCmd, // 操作云台巡航命令:30、将预置点加入巡航序列。31、设置巡航点停顿时间。32、设置巡航速度。33、将预置点从巡航序列中删除。37、开始巡航。38、停止巡航
						byCruiseRoute, // 巡航路径，最多支持32条路径（序号从1开始）
						byCruisePoint, // 巡航点，最多支持32个点（序号从1开始）
						wInput); // 不同巡航命令时的值不同，预置点(最大300)、时间(最大255)、速度(最大40)
			}
			// 注销SDK
			if (nsl.logoutSDK(previewCode) == -1) {
				System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
			}
			// 释放SDK资源
			if (nsl.cleanSDK() == -1) {
				System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
			}
			if (!flag) {
				System.out.println("----------错误信息----------：" + nsl.errorCode());
			}
		}
		return flag;
	}




	//设置录制视频时保存的格式
	public boolean DVR_PlayBackControl(String ip, short port, String username, String password) {
		Integer userId = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else if (nsl.initSDK() == 200) {
			userId = nsl.loginSDK(ip, port, username, password);
			int previewCode = nsl.previewSDK(userId);
			if (previewCode != 0) {
				System.out.println("SDK登录失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_StopRealPlay函数返回值为0为没有错误
				HCNetSDK.NET_DVR_TIME time = new HCNetSDK.NET_DVR_TIME();
				NativeLong viewCode = new NativeLong(previewCode); // userId类型转换
				NativeLong getFileByTime = hns.NET_DVR_GetFileByTime(viewCode,null,time,time,null); //初始化按时间下载录像文件结构体
				IntByReference LPOutValue = null;
				flag = hns.NET_DVR_PlayBackControl(getFileByTime,32,0,LPOutValue);
			}
		// 注销SDK
		if (nsl.logoutSDK(previewCode) == -1) {
			System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
		}
		// 释放SDK资源
		if (nsl.cleanSDK() == -1) {
			System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
		}
		if (!flag) {
			System.out.println("----------错误信息----------：" + nsl.errorCode());
		}
	}
		return flag;
	}
	
	
	
	
	//设置视频是否显示时间、设备名称
	public boolean DVR_Set_OSD_DVRConfig(String ip, short port, String username, String password, int ShowDname,
			int ShowOsd, String Dname){
		Integer userId = null;
		NativeLong viewCode = null;
		if (nsl.initSDK() == -1) {
			System.out.println("SDK初始化失败--错误信息---：" + nsl.errorCode());
		} else {
			userId = nsl.loginSDK(ip, port, username, password);
			int previewCode = nsl.previewSDK(userId);
			if (previewCode == -1) {
				System.out.println("SDK预览失败--错误信息---：" + nsl.errorCode());
			} else if (previewCode == 0) { // NET_DVR_RealPlay_V40函数返回值为0为没有错误
				viewCode = new NativeLong(previewCode); // userId类型转换
				clientInfo.lChannel = new NativeLong(1);	//通道号
				NET_DVR_PICCFG_V30 DVR_PICCFG = new HCNetSDK.NET_DVR_PICCFG_V30();
//				DVR_PICCFG.dwShowChanName = ShowDname; //预览的图象上是否显示通道名称：0-不显示，1-显示
//				if(ShowDname ==1){
//					DVR_PICCFG.sChanName = Dname.getBytes();
//					for (int i = 0; i < 32; i++) {
//						DVR_PICCFG.sChanName[i] = (byte)0;
//					}
//				} 
//				HCNetSDK.NET_DVR_MOTION_V30 MOTION = new HCNetSDK.NET_DVR_MOTION_V30();
//					MOTION.byEnableHandleMotion = (byte)0;
//				DVR_PICCFG.struMotion = MOTION;
//				HCNetSDK.NET_DVR_VILOST_V30 VILOST = new HCNetSDK.NET_DVR_VILOST_V30();
//					VILOST.byEnableHandleVILost = (byte)0;
//				DVR_PICCFG.struVILost = VILOST;
//				HCNetSDK.NET_DVR_HIDEALARM_V30 HIDEALARM = new HCNetSDK.NET_DVR_HIDEALARM_V30();
//					HIDEALARM.dwEnableHideAlarm = (byte)0;
//				DVR_PICCFG.struHideAlarm = HIDEALARM;
//				HCNetSDK.NET_DVR_SHELTER SHELTER = new HCNetSDK.NET_DVR_SHELTER();
//					SHELTER.wHideAreaTopLeftX = (short)100;
//				HCNetSDK.NET_DVR_SHELTER SHELTER1 = new HCNetSDK.NET_DVR_SHELTER();
//					SHELTER1.wHideAreaTopLeftY = (short)100;
//				HCNetSDK.NET_DVR_SHELTER SHELTER2 = new HCNetSDK.NET_DVR_SHELTER();
//					SHELTER2.wHideAreaWidth = (short)50;
//				HCNetSDK.NET_DVR_SHELTER SHELTER3 = new HCNetSDK.NET_DVR_SHELTER();	
//					SHELTER3.wHideAreaHeight = (short)50;
//				DVR_PICCFG.struShelter[0] = SHELTER;
//				DVR_PICCFG.struShelter[1] = SHELTER1;
//				DVR_PICCFG.struShelter[2] = SHELTER2;
//				DVR_PICCFG.struShelter[3] = SHELTER3;
//				for (int i = 0; i < 64; i++) {
//					DVR_PICCFG.byRes[i] = (byte)0;
//				}
//				DVR_PICCFG.dwShowOsd  = ShowOsd;	// 预览的图象上是否显示日期：0-不显示，1-显示
//				DVR_PICCFG.byOSDType  = (byte)2;			// 2－XXXX年XX月XX日
//				DVR_PICCFG.dwVideoFormat = (byte)0; 		// TODO:只读 视频制式 1-NTSC 2-PAL
//				DVR_PICCFG.wShowNameTopLeftX = (short)10;	// 通道名称显示位置的x坐标 
//				DVR_PICCFG.wShowNameTopLeftY = (short)10;	//通道名称显示位置的y坐标
//				DVR_PICCFG.wOSDTopLeftX = (short)50;		//日期的x坐标
//				DVR_PICCFG.wOSDTopLeftY	= (short)50;		//日期的y坐标
//				DVR_PICCFG.byOSDAttrib	= (byte)4;	//日期属性：不透明，不闪烁
//				DVR_PICCFG.byHourOSDType = (byte)0;	//日期小时制:24小时制
//				DVR_PICCFG.byDispWeek = (byte)0;	//日期是否显示星期
//				DVR_PICCFG.dwEnableHide = 0;		//启用隐私遮蔽：0-否
				
				IntByReference ibrBytesReturned = new IntByReference(0);
				DVR_PICCFG.write();
				Pointer lpOsdConfig = DVR_PICCFG.getPointer();
				flag = hns.NET_DVR_GetDVRConfig(viewCode,1004,clientInfo.lChannel,lpOsdConfig,DVR_PICCFG.size(),ibrBytesReturned);
//				flag = hns.NET_DVR_SetDVRConfig(viewCode,hns.NET_DVR_SET_PICCFG,clientInfo.lChannel,lpOsdConfig,DVR_PICCFG.size());
				DVR_PICCFG.read();
				
				System.out.println(DVR_PICCFG.dwEnableHide+DVR_PICCFG.byHourOSDType+DVR_PICCFG.dwSize);
				if (!flag) {
					System.out.println("----------错误信息----------：" + nsl.errorCode());
					}
				if (!(hns.NET_DVR_StopRealPlay(viewCode))){
					System.out.println("SDK停止预览失败--错误信息---：" + nsl.errorCode());				
					}
				// 注销SDK
				if (nsl.logoutSDK(previewCode) == -1) {
					System.out.println("SDK注销失败--错误信息---：" + nsl.errorCode());
					}
				// 释放SDK资源
				if (nsl.cleanSDK() == -1) {
					System.out.println("SDK释放资源失败--错误信息---：" + nsl.errorCode());
					}
				}
			}
		return flag;
	}

	

	/***
	 *	初始化系统状态(数据库表的操作)
	 *	功能模式：1、监控直播 2、录像回放
	 *	分屏模式： 1、1x1  2、2x2  3、3x3  4、4x4
	 *	轮巡模式：1、一般   点
	 * 
	 */
	public boolean InitBaseSetting(){
		BaseSetting baseSetting = new BaseSetting();
		baseSetting.setId(DataUtils.getUUID());
		baseSetting.setFunction_mode(1);      // 功能模式：1、监控直播 2、录像回放
		baseSetting.setScreen_mode(2);       // 分屏模式： 1、1x1  2、2x2  3、3x3  4、4x4
		baseSetting.setPolling_mode(1);		// 轮巡模式：1、一般  2、重点
		baseSetting.setPolling_time(10);   // 时间间隔：5s、10s、20s
		return baseSettingDao.initBaseSetting(baseSetting);
	}
		
	
	/**
	 * 获取系统状态信息(数据库表的操作)
	 * @return
	 * @author huzhihao
	 * used
	 */
	public List<BaseSetting> getBaseSetting() {
		return baseSettingDao.getBaseSetting();
	}
	
	
	
	/**
	 * 用户自定义设置系统状态信息(数据库表的操作)
	 * @return
	 * @author huzhihao
	 * used
	 */
	public boolean setBaseSetting(BaseSetting baseSetting) {
		return baseSettingDao.setBaseSetting(baseSetting);
	}



	
	/***
	 *	初始化视频设置(数据库表的操作)
	 *	视频亮度：5
	 *	视频对比度：5
	 *	视频饱和度：5
	 *	视频色度：	5
	 * 
	 */
	public boolean InitVideoSetting(){
		VideoSetting videoSetting = new VideoSetting();
		videoSetting.setId(DataUtils.getUUID());
		videoSetting.setBrightness(5);	//视频亮度(1-10)		
		videoSetting.setContrast(5);	//视频对比度(1-10)		
		videoSetting.setSaturation(5);	//视频饱和度(1-10)		
		videoSetting.setChroma(5);		//视频色度(1-10)			
		return videoSettingDao.InitVideoSetting(videoSetting);
	}
	
	
	
	/**
	 * 用户自定义视频设置(数据库表的操作)
	 * @return
	 * @author huzhihao
	 * used
	 */
	public boolean setVideoSetting(VideoSetting videoSetting) {
		return videoSettingDao.setVideoSetting(videoSetting);
	}



	/***
	 * 获取视频设置表中的ID(数据库表的操作)
	 * @param 
	 * @return List<VideoSetting>
	 */
	public List<VideoSetting> getAll(String vidicon_id) {
		
		return videoSettingDao.getAll(vidicon_id);
	}



	/***
	 * 初始化摄像头设置(数据库表的操作)
	 */
	public boolean InitVidiconSetting(){
		VidiconSetting vdiconSetting = new VidiconSetting();
		vdiconSetting.setId(DataUtils.getUUID());
		vdiconSetting.setCruise(1);    
		vdiconSetting.setFocus(1);  
		vdiconSetting.setPreset(1);	
		vdiconSetting.setCruise_time(10);
		return vidiconSettingDao.initVidiconSetting(vdiconSetting);
	}


	
	/**
	 * 用户自定义摄像头设置(数据库表的操作)
	 * @return
	 * @author huzhihao
	 * used
	 */
	public boolean setVidiconSetting(VidiconSetting vidiconSetting) {
		return vidiconSettingDao.setVidiconSetting(vidiconSetting);
	}

	
	
	
	/**
	 * 获取系摄像头设置(数据库表的操作)
	 * @return
	 * @author huzhihao
	 * used
	 */
	public List<VidiconSetting> getVidiconSetting() {
		return vidiconSettingDao.getVidiconSetting();
	}
}



