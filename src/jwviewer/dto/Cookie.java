package jwviewer.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import jwviewer.dao.impl.CookieDao;

@DatabaseTable(tableName = "cookie", daoClass = CookieDao.class)
public class Cookie {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String value;
	@DatabaseField
	private String creationDate;
	@DatabaseField
	private String path;
	@DatabaseField(index = true)
	private String domain;
	@DatabaseField
	private String expiryDate;
	@DatabaseField
	private int size;
	@DatabaseField
	private boolean httpOnly;
	@DatabaseField
	private boolean secure;
	@DatabaseField
	private boolean persistent;
	@DatabaseField
	private String sameSite;
	@DatabaseField
	private String priority;

	private int version;
	private String comment;
	private String ports;
	private String commentURL;

//	"creationDate":1612768717417,
//	"expiryDate":1629933485417,
//	"version":0,
//	"comment":null,
//	"ports":null,
//	"commentURL":null

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public String getSameSite() {
		return sameSite;
	}

	public void setSameSite(String sameSite) {
		this.sameSite = sameSite;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getCommentURL() {
		return commentURL;
	}

	public void setCommentURL(String commentURL) {
		this.commentURL = commentURL;
	}

}
