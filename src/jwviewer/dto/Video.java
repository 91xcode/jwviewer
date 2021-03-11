package jwviewer.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import jwviewer.dao.impl.VideoDao;

@DatabaseTable(tableName = "video", daoClass = VideoDao.class)
public class Video {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int infoFileId;
	@DatabaseField
	private boolean isHiden;
	@DatabaseField
	private int metadataId;
//storyboardFileIdSet;//[]
	@DatabaseField
	private String updateTime;
	@DatabaseField
	private int version;
	@DatabaseField
	private int videoFileId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInfoFileId() {
		return infoFileId;
	}

	public void setInfoFileId(int infoFileId) {
		this.infoFileId = infoFileId;
	}

	public boolean isHiden() {
		return isHiden;
	}

	public void setHiden(boolean isHiden) {
		this.isHiden = isHiden;
	}

	public int getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(int metadataId) {
		this.metadataId = metadataId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVideoFileId() {
		return videoFileId;
	}

	public void setVideoFileId(int videoFileId) {
		this.videoFileId = videoFileId;
	}

}
