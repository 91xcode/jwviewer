package jwviewer.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import jwviewer.dao.impl.VideoMapDao;

@DatabaseTable(tableName = "videomap", daoClass = VideoMapDao.class)
public class VideoMap {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int starId;
	@DatabaseField
	private String starName;
	@DatabaseField
	private String JAVID;
	@DatabaseField
	private int metadataId;
	@DatabaseField
	private int infoFileId;
	@DatabaseField
	private int videoFileId;
	@DatabaseField
	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStarName() {
		return starName;
	}

	public void setStarName(String starName) {
		this.starName = starName;
	}

	public String getjAVID() {
		return JAVID;
	}

	public void setjAVID(String jAVID) {
		JAVID = jAVID;
	}

	public int getInfoFileId() {
		return infoFileId;
	}

	public void setInfoFileId(int infoFileId) {
		this.infoFileId = infoFileId;
	}

	public int getStarId() {
		return starId;
	}

	public void setStarId(int starId) {
		this.starId = starId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVideoFileId() {
		return videoFileId;
	}

	public void setVideoFileId(int videoFileId) {
		this.videoFileId = videoFileId;
	}

	public int getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(int metadataId) {
		this.metadataId = metadataId;
	}

}
