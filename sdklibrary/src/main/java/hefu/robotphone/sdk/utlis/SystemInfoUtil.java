package hefu.robotphone.sdk.utlis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取系统信息
 * @author dc
 *
 */
public class SystemInfoUtil {
	private static Context mContext = null;

	public SystemInfoUtil(Context contxt){
		mContext = contxt;
	}

	/**
	 * @descriptoin	获取mac地址
	 * @return	mac地址
	 * @author	dc
	 * @date 2015-9-7 下午3:19:39
	 */
	public static String getMac(Context mContext){
		WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress().replace(":", "");
//		return "1223678898b3"; //测试使用
		return mac;
	}


	//获取当前连接网络的网卡的mac地址
	private static String parseByte(byte b) {
		String s = "00" + Integer.toHexString(b)+":";
		return s.substring(s.length() - 3);
	}

	/**
	 * 获取当前系统连接网络的网卡的mac地址
	 * @return
	 */
	@SuppressLint("NewApi")
	public static final String getMac() {
		byte[] mac = null;
		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();

				while (address.hasMoreElements()) {
					InetAddress ip = address.nextElement();
					if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) || ip.isLoopbackAddress())
						continue;
					if (ip.isSiteLocalAddress())
						mac = ni.getHardwareAddress();
					else if (!ip.isLinkLocalAddress()) {
						mac = ni.getHardwareAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		if(mac != null){
			for(int i=0 ;i<mac.length ;i++){
				sb.append(parseByte(mac[i]));
			}
//			return sb.substring(0, sb.length()-1);
			String res = sb.toString();
			res = res.replaceAll(":","");
			return res;
		}else{
			return "";
		}
	}

	public static String getPhoneIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

}
