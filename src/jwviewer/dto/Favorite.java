package jwviewer.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import jwviewer.dao.impl.FavoriteDao;
import jwviewer.jackson.DateTimeConverter;

@DatabaseTable(tableName = "favorite", daoClass = FavoriteDao.class)
public class Favorite {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int typeId;
	@DatabaseField
	private int type;
	@DatabaseField
	private String name;
	/**
	 * 1595493209263
	 */
	@DatabaseField
	private String updateTime;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}



}
