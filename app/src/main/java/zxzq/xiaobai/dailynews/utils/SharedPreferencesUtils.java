package zxzq.xiaobai.dailynews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences存储工具类
 * @author qinqy
 *
 */
public class SharedPreferencesUtils {


	
	/**
	 * 清除用户数据
	 * @param context
	 */
	public static void clearUser(Context context){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
	
	/**
	 * 获取token
	 * @param context
	 * @return
	 */
	public static String getToken(Context context){
		SharedPreferences sp = context.getSharedPreferences("register", Context.MODE_PRIVATE);
		return sp.getString("token", "");
	}
	
	/**
	 * 获取用户名和用户头像地址ַ
	 * @param context
	 * @return  String����  0 --- username 1 --photo
	 */
	public static String[] getUserNameAndPhoto(Context context){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return new String []{sp.getString("userName", ""),sp.getString("headImage", "")};
	}
	
	/**
	 * 保存用户头像本地路径
	 * @param context
	 * @param path
	 */
	public static void saveUserLocalIcon(Context context ,String path){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("imagePath", path);
		editor.commit();
	}
	
	/**
	 * 获取保存的本地头像路劲
	 * @param context
	 * @return 
	 */
	public static String getUserLocalIcon(Context context){
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return sp.getString("imagePath", null);
	}
}
