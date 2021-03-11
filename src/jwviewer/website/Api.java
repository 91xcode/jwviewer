package jwviewer.website;

public class Api {
	public static final String SCREENSHOTFILESURL = "screenshotFilesURL";

	/**starid/pageno/pagesize(21)*/
	public static String STAR_GETLIST	="https://fucklo.li/api/metadata/getListByMeta/star/";
	
	public static final String PAGENO = "pageNo";

	public static final int FIFTY = 50;// max
	public static final int TWENTY = 20;
	
	//bookmark
	public static String BOOKMARK_GETLIST="https://fucklo.li/api/bookmark/getList";
	/** /bookmarkid/pageNo/pageSize*/
	public static String BOOKMARK_GETINFO = "https://fucklo.li/api/bookmark/getInfo/";

	public static String METADATA_GETINFO = "https://fucklo.li/api/metadata/getInfo/";// metadataId

	public static String METADATA = "https://fucklo.li/api/metadata/getList/";
	public static final int METADATAPAGESIZE = 21;// default

	// .../pageno/pagesize
	public static String TAG = "https://fucklo.li/api/metadata/getMetaList/tag/";

	public static String SERIES = "https://fucklo.li/api/metadata/getMetaList/series/";

	// video
	public static String VIDEO_GETLIST = "https://fucklo.li/api/video/getList/";

	public static String FILE_GETURL = "https://fucklo.li/api/file/getURL/";// videoFileId


}
