package cn.cdahua.databasesUtil;

public interface DatabasesUtils {
	/**
	 * 备份数据库
	 * 
	 * @return true表示备份成功；false表示备份失败。
	 */
	public boolean backup();

	/**
	 * 恢复数据库
	 * 
	 * @param fileName
	 *            备份文件的地址
	 * @return true表示备份成功；false表示备份失败。
	 */
	public boolean restore(String fileName);
}
