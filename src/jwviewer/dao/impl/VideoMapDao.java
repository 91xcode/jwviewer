package jwviewer.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import jwviewer.dao.IVideoMapDao;
import jwviewer.dto.Metadata;
import jwviewer.dto.Star;
import jwviewer.dto.Video;
import jwviewer.dto.VideoMap;
import jwviewer.http.HTTP;
import jwviewer.json.Json;
import jwviewer.ormlite.SQLite;
import jwviewer.website.Api;

public class VideoMapDao extends BaseDaoImpl<VideoMap, String> implements IVideoMapDao {

	protected VideoMapDao(ConnectionSource connectionSource, Class<VideoMap> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public VideoMapDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, VideoMap.class);
	}

	public static void main(String[] args) {

//		wupdateVideoMapByStarId(437);
		updateVideoMapByStarId(437);
	}

	public static void wupdateVideoMapByStarId(int starId) {
		List<Metadata> list = HTTP.wGetMetadataByStarId(437);
		Dao<Metadata, String> metadataDao = SQLite.getDao(Metadata.class);
		try {
		for (Metadata metadata : list) {
			metadata.setStarId(starId);
			metadata.setMetadataId(metadata.getId());
			metadata.setId(0);
		}
		metadataDao.create(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void updateVideoMapByStarId(int starId) {

		List<Metadata> list = MetadataDao.getMetadataByStarId(starId);
		Dao<Video, String> vdao = SQLite.getDao(Video.class);
		Dao<VideoMap, String> vmdao = SQLite.getDao(VideoMap.class);
		Dao<Star, String> starDao = SQLite.getDao(Star.class);
		try {
			for (int i = 0, len = list.size(); i < len; i++) {
				Metadata metadata = list.get(i);
				System.out.println(i + "," + metadata.getMetadataId() + "," + metadata.getjAVID());
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("metadataId", metadata.getMetadataId());
				List<Video> videos = vdao.queryForFieldValues(params);
				List<VideoMap> vms = new ArrayList<VideoMap>();
				for (int j = 0, vlen = videos.size(); j < vlen; j++) {
//					System.out.println("-----j:" + j + "," + videos.get(j).getMetadataId() + ","
//							+ videos.get(j).getVideoFileId() + "," + videos.get(j).getVideoFileId());
					HttpGet videoHttpGet = new HttpGet(Api.FILE_GETURL + "/" + videos.get(j).getVideoFileId());
					CloseableHttpResponse response;
					response = HTTP.getClient().execute(videoHttpGet);
					int videoStatusCode = response.getStatusLine().getStatusCode();
					HttpEntity videoEntity = response.getEntity();
					String videoRes = EntityUtils.toString(videoEntity, Charsets.UTF_8);
					// {videoFileId:url}
					if (HttpStatus.SC_OK == videoStatusCode) {
						JsonNode json = Json.getInstance().readTree(videoRes);
						if (json.get("code").asInt() == 0) {
							JsonNode videoJson = json.get("data");
							String videoUrl = videoJson.get(String.valueOf(videos.get(j).getVideoFileId())).asText();
							System.out.println(videos.get(j).getMetadataId() + "," + videos.get(j).getVideoFileId()
									+ "," + videoUrl);
							VideoMap videoMap = new VideoMap();
							videoMap.setjAVID(metadata.getjAVID());
							videoMap.setStarId(starId);
							Star star = starDao.queryForId(starId + "");
							videoMap.setStarName(star.getName());
							videoMap.setInfoFileId(videos.get(j).getInfoFileId());
							videoMap.setMetadataId(videos.get(j).getMetadataId());
							videoMap.setVideoFileId(videos.get(j).getVideoFileId());
							videoMap.setUrl(videoUrl);
							vms.add(videoMap);
						}
					}
				}
				vmdao.create(vms);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
