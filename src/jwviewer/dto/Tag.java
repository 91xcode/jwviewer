package jwviewer.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import jwviewer.dao.impl.TagDao;

@DatabaseTable(tableName = "tag", daoClass = TagDao.class)
public class Tag {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String name;
	/**
	 * 1595493209263
	 */
	@DatabaseField
	private String updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
