package cn.cdahua.databasesUtil.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import cn.cdahua.databasesUtil.DatabasesUtils;

public class MysqlUtils implements DatabasesUtils {

	public static MysqlUtils instence = null;
	private static Properties properties = new Properties();
	private static String username = null;
	private static String password = null;
	private static String database = null;
	static {
		try (InputStream io = MysqlUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");){
			if (properties == null)
				properties = new Properties();
			properties.load(io);
			username = properties.getProperty("jdbc.username");
			password = properties.getProperty("jdbc.password");
			database = properties.getProperty("jdbc.database");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MysqlUtils() {

	}

	public static MysqlUtils getInstence() {
		if (instence == null) {
			synchronized (MysqlUtils.class) {
				if (instence == null) {
					instence = new MysqlUtils();
				}
			}
		}
		return instence;
	}

	@Override
	public boolean backup() {
		checkProperties();
		try {
			// 获取保存备份文件保存位置
			String backupPath = properties.getProperty("jdbc.BackupPath")
					+ (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + ".sql";
			// 获取命令字符串
			String command = "mysqldump -u" + username + " -p" + password + " --set-charset=utf8 " + database;

			// 获取Process
			Process process = Runtime.getRuntime().exec(command);

			// 将结果保存到文件中

			// 1、获取输入流和创建输出流
			InputStream in = process.getInputStream();
			FileOutputStream out = new FileOutputStream(backupPath);
			// 2、从输入流获取信息并写入输出流
			inputStream2OutputStream(in, out);

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean restore(String fileName) {
		try {
			checkProperties();
			String backupPath = properties.getProperty("jdbc.BackupPath") + fileName;
			String command = "mysql -u" + username + " -p" + password + " " + database;

			File file = new File(backupPath);
			if (file.exists() && file.isFile()) {
				// 1、获取输入输出流
				FileInputStream in = new FileInputStream(file);
				OutputStream out = Runtime.getRuntime().exec(command).getOutputStream();
				// 2、从输入流获取信息并写入输出流
				inputStream2OutputStream(in, out);
				return true;
			} else {
				try {
					throw new IOException("该文件不存在");
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 检查properties是否为空
	private void checkProperties() {
		try {
			if (properties == null) {
				properties = new Properties();
				properties.load(this.getClass().getResourceAsStream("backup.properties"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 2、从输入流获取信息并写入输出流
	private void inputStream2OutputStream(InputStream in, OutputStream out) {
		try {
			// 2、输入流转换成String
			InputStreamReader isr = new InputStreamReader(in, "utf-8");
			BufferedReader br = new BufferedReader(isr);

			String inStr;
			StringBuffer sb = new StringBuffer("");
			String outStr;

			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();
			// 3、向输出流写入数据
			OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
			writer.write(outStr);
			writer.flush();
			// 释放资源
			writer.close();
			br.close();
			isr.close();
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
