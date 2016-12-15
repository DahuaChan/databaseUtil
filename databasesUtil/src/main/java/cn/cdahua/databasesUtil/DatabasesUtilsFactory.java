package cn.cdahua.databasesUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class DatabasesUtilsFactory {
	public static DatabasesUtils getDatabasesUtils(DatabasesType type){
		String temp = type.toString();
		String packageName = DatabasesUtilsFactory.class.getPackage().getName()+temp.toLowerCase()+".";
		String instenceName =packageName + temp.substring(0, 1)+temp.substring(1).toLowerCase()+"Utils";
		try {
			Class<?> clazz = Class.forName(instenceName);
			Method method = clazz.getMethod("getInstence");
			return (DatabasesUtils) method.invoke(null);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
}
