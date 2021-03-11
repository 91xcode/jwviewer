package jwviewer.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import jwviewer.dao.impl.MetadataDao;

@DatabaseTable(tableName = "metadata", daoClass = MetadataDao.class)
public class Metadata {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int starId;
	@DatabaseField
	private int metadataId;
	@DatabaseField
	private String JAVID;
	@DatabaseField
	private String companyId;
	@DatabaseField
	private String companyName;
	@DatabaseField
	private String title;
	@DatabaseField
	private String posterFileURL;
	@DatabaseField
	private String releaseDate;

//	private String screenshotFilesURL;// []version==2

//series;//[]db:series_mapping
//stars;//[]
//tags;//[]

	/**
	 * 1595493209263
	 */
	@DatabaseField
	private String updateTime;
	@DatabaseField
	private int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getjAVID() {
		return JAVID;
	}

	public int getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(int metadataId) {
		this.metadataId = metadataId;
	}

	public void setjAVID(String jAVID) {
		this.JAVID = jAVID;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPosterFileURL() {
		return posterFileURL;
	}

	public void setPosterFileURL(String posterFileURL) {
		this.posterFileURL = posterFileURL;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getStarId() {
		return starId;
	}

	public void setStarId(int starId) {
		this.starId = starId;
	}

}
