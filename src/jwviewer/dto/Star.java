package jwviewer.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import jwviewer.dao.impl.StarDao;
import jwviewer.jackson.DateTimeConverter;

@DatabaseTable(tableName = "star", daoClass = StarDao.class)
@JsonFilter(value = "updateTime")
public class Star {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String photoURL;
	/**
	 * 1595493209263
	 */
	@DatabaseField
//	@JsonFilter(value = "updateTime")
	@DateTimeConverter(formatter = "yyyy-MM-dd HH:mm:ss")
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

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
