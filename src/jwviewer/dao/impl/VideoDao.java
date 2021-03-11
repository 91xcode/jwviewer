package jwviewer.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import jwviewer.dao.IVideoDao;
import jwviewer.dto.Favorite;
import jwviewer.dto.Metadata;
import jwviewer.dto.Star;
import jwviewer.dto.Tag;
import jwviewer.dto.Video;
import jwviewer.dto.VideoMap;
import jwviewer.http.HTTP;
import jwviewer.json.Json;
import jwviewer.ormlite.SQLite;
import jwviewer.website.Api;

public class VideoDao extends BaseDaoImpl<Video, String> implements IVideoDao {

	protected VideoDao(ConnectionSource connectionSource, Class<Video> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public VideoDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Video.class);
	}

	public static List<String> getVideoByMetadataId(int metadataId) {

		String api = Api.VIDEO_GETLIST + "/" + metadataId;
		HttpGet httpGet = new HttpGet(api);
		List<String> videos = new ArrayList<String>();
		{
			CloseableHttpResponse resp;
			try {
				resp = HTTP.getClient().execute(httpGet);

				int statusCode = resp.getStatusLine().getStatusCode();
				HttpEntity entity = resp.getEntity();
				String res = EntityUtils.toString(entity, Charsets.UTF_8);
				if (HttpStatus.SC_OK == statusCode) {
//					{"code": 0,"msg": "Success","data": [{video}]}
					JsonNode jsonNode = Json.getInstance().readTree(res);
					if (jsonNode.get("code").asInt() == 0) {
						JsonNode data = jsonNode.get("data");
						List<Video> list = Json.getInstance().convertValue(data, new TypeReference<List<Video>>() {
						});
						SQLite.getDao(Video.class).create(list);
						System.out.println();
//						for (Video video : list) {
//							int videoFileId = video.getVideoFileId();
//							HttpGet videoHttpGet = new HttpGet(Api.FILE_GETURL + "/" + videoFileId);
//							CloseableHttpResponse response = HTTP.getClient().execute(videoHttpGet);
//
//							int videoStatusCode = response.getStatusLine().getStatusCode();
//							HttpEntity videoEntity = response.getEntity();
//							String videoRes = EntityUtils.toString(videoEntity, Charsets.UTF_8);
//							// {videoFileId:url}
//							if (HttpStatus.SC_OK == videoStatusCode) {
//								JsonNode json = Json.getInstance().readTree(videoRes);
//								if (json.get("code").asInt() == 0) {
//									JsonNode videoJson = json.get("data");
//									String videoUrl = videoJson.get(String.valueOf(videoFileId)).asText();
//									videos.add(videoUrl);
//									System.out.println(videoUrl);
//								}
//							}
//						}
					}
				} else {
					System.out.println();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return videos;

	}

	public static void getVideoByVideoFileId() {
		try {
			Dao<Video, ?> vdao = SQLite.getDao(Video.class);
			List<Video> dvideos = vdao.queryForAll();
			for (int i = 0; i < dvideos.size(); i++) {
				System.out.println("第" + (i + 1) + "个,还剩" + (dvideos.size() - i + 1) + "个");
				Video video = dvideos.get(i);
				int videoFileId = video.getVideoFileId();
				HttpGet videoHttpGet = new HttpGet(Api.FILE_GETURL + "/" + videoFileId);
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
						String videoUrl = videoJson.get(String.valueOf(videoFileId)).asText();
						System.out.println(video.getMetadataId() + "," + videoFileId + "," + videoUrl);
//						video.setVideoUrl(videoUrl);
						vdao.update(video);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		try {
//			List<Metadata> all = SQLite.getDao(Metadata.class).queryForAll();
//			for (Metadata metadata : all) {
//				int id = metadata.getId();
//				getVideoByMetadataId(id);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

//		missVideo();

//		fixv();
//		getVideoByVideoFileId();

	}

	

	public static void getFavorite() {
		try {
			ArrayList<Integer> stars = new ArrayList<Integer>();
			Dao<Favorite, ?> fdao = SQLite.getDao(Favorite.class);
			Dao<Star, ?> sdao = SQLite.getDao(Star.class);
			for (Favorite favorite : fdao) {
				if (0 == favorite.getType()) {
					int starId = favorite.getTypeId();
					stars.add(starId);
				}
			}
			for (Integer starId : stars) {

			}
			Dao<Video, ?> vdao = SQLite.getDao(Video.class);
			List<Video> dvideos = vdao.queryForAll();
			for (int i = 0; i < dvideos.size(); i++) {
				System.out.println("第" + (i + 1) + "个,还剩" + (dvideos.size() - i + 1) + "个");
				Video video = dvideos.get(i);
				int videoFileId = video.getVideoFileId();
				HttpGet videoHttpGet = new HttpGet(Api.FILE_GETURL + "/" + videoFileId);
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
						String videoUrl = videoJson.get(String.valueOf(videoFileId)).asText();
						System.out.println(video.getMetadataId() + "," + videoFileId + "," + videoUrl);
//						video.setVideoUrl(videoUrl);
						vdao.update(video);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void fixv() {
		try {

			Dao<Video, ?> vdao = SQLite.getDao(Video.class);
			List<Video> dvideos = vdao.queryForAll();
			System.out.println("dvideos:" + dvideos.size());
			HashMap<Integer, List<Integer>> hashMap = new HashMap<Integer, List<Integer>>();
			Iterator<Video> iterator = dvideos.iterator();
			while (iterator.hasNext()) {
				Video video = iterator.next();
				int metadataId = video.getMetadataId();
				Iterator<Video> iterator2 = dvideos.iterator();
				while (iterator2.hasNext()) {
					Video video2 = iterator2.next();
					int metadataId2 = video2.getMetadataId();
					if (metadataId == metadataId2) {
						List<Integer> list = hashMap.get(metadataId);
						if (null == list) {
							List<Integer> arrayList = new ArrayList<Integer>();
							arrayList.add(video.getVideoFileId());
							hashMap.put(metadataId, arrayList);
						} else {
							list.add(video.getVideoFileId());
							hashMap.put(metadataId, list);
						}
					}
				}
			}
			Set<Entry<Integer, List<Integer>>> entrySet = hashMap.entrySet();
			for (Entry<Integer, List<Integer>> entry : entrySet) {
				System.out.println(entry.getKey().intValue() + ":" + entry.getValue().toArray());
			}

//			HashSet<Integer> hashSet = new HashSet<Integer>();
//			System.out.println("truncate:" + hashSet.size());
//			for (Integer mid : hashSet) {
//				while (iterator.hasNext()) {
//					Video video = iterator.next();
//					int metadataId = video.getMetadataId();
//					if (mid == metadataId) {
//						List<Integer> list = hashMap.get(mid);
//						if (null == list) {
//							List<Integer> arrayList = new ArrayList<Integer>();
//							arrayList.add(video.getVideoFileId());
//							hashMap.put(mid, arrayList);
//						} else {
//							list.add(video.getVideoFileId());
//							hashMap.put(mid, list);
//						}
//					}
//				}
//			}

//			int i = 1;
//			for (Integer mmid : hashSet) {
//				System.out.println("第" + i + ",次，还剩" + (hashSet.size() - i) + "次");
//				i++;
//				String api = Api.VIDEO_GETLIST + "/" + mmid;
//				HttpGet httpGet = new HttpGet(api);
//				List<String> videos = new ArrayList<String>();
//				{
//
//					CloseableHttpResponse resp;
//					resp = HTTP.getClient().execute(httpGet);
//
//					int statusCode = resp.getStatusLine().getStatusCode();
//					HttpEntity entity = resp.getEntity();
//					String res = EntityUtils.toString(entity, Charsets.UTF_8);
//					if (HttpStatus.SC_OK == statusCode) {
////				{"code": 0,"msg": "Success","data": [{video}]}
////					JsonNode jsonNode = Json.getInstance().readTree(res);
////					if (jsonNode.get("code").asInt() == 0) {
////						JsonNode data = jsonNode.get("data");
////						System.out.println(data);
////							List<Video> list = Json.getInstance().convertValue(data,
////									new TypeReference<List<Video>>() {
////									});
////							vdao.create(list);
////					}
//					} else {
//						System.out.println();
//					}
//				}
//			}
		} catch (SQLException e) {
			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
		}
	}

	public static void missVideo() {
		try {
			Dao<Metadata, ?> mdao = SQLite.getDao(Metadata.class);
			List<Metadata> metadatas;
			metadatas = mdao.queryForAll();
			List<Integer> metadataIds = new ArrayList<Integer>();
			for (Metadata metadata : metadatas) {
				int id = metadata.getMetadataId();
				metadataIds.add(id);
			}
			System.out.println("metadatas:" + metadataIds.size());

			Dao<Video, ?> vdao = SQLite.getDao(Video.class);
			List<Video> dvideos = vdao.queryForAll();
			System.out.println("dvideos:" + dvideos.size());

			HashSet<Integer> hashSet = new HashSet<Integer>();
			Iterator<Video> iterator = dvideos.iterator();
			while (iterator.hasNext()) {
				Video video = iterator.next();
				int metadataId = video.getMetadataId();
				hashSet.add(metadataId);
			}
			System.out.println("truncate:" + hashSet.size());
//		boolean addAll = hashSet.addAll(metadataIds);
//		System.out.println("truncate:"+hashSet.size());

//		metadataIds.stream().distinct().collect(hashSet.toArray())
			for (Integer mid : hashSet) {
				boolean contains = metadataIds.contains(mid);
				if (contains) {
					metadataIds.remove(mid);
				} else {
					System.out.println(mid.intValue());
				}

			}
			System.out.println("metadatas:" + metadataIds.size());
			System.out.println(metadataIds);

//			for (Integer mmid : metadataIds) {
//
//				String api = Api.VIDEO_GETLIST + "/" + mmid;
//				HttpGet httpGet = new HttpGet(api);
//				List<String> videos = new ArrayList<String>();
//				{
//					CloseableHttpResponse resp;
//					resp = HTTP.getClient().execute(httpGet);
//
//					int statusCode = resp.getStatusLine().getStatusCode();
//					HttpEntity entity = resp.getEntity();
//					String res = EntityUtils.toString(entity, Charsets.UTF_8);
//					if (HttpStatus.SC_OK == statusCode) {
////					{"code": 0,"msg": "Success","data": [{video}]}
//						JsonNode jsonNode = Json.getInstance().readTree(res);
//						if (jsonNode.get("code").asInt() == 0) {
//							JsonNode data = jsonNode.get("data");
//							System.out.println(data);
//								List<Video> list = Json.getInstance().convertValue(data,
//										new TypeReference<List<Video>>() {
//										});
//								vdao.create(list);
//						}
//					} else {
//						System.out.println();
//					}
//				}
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateTime(int pageNo) {

		String starApi = Api.TAG;
		starApi += pageNo;
		starApi += "/";// 50
		starApi += Api.FIFTY;
		HttpGet httpGet = new HttpGet(starApi);
		try {
			CloseableHttpResponse resp = HTTP.getClient().execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
			if (HttpStatus.SC_OK == statusCode) {
//				{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{}]}}
				JsonNode jsonNode = Json.getInstance().readTree(res);
				if (jsonNode.get("code").asInt() == 0) {

					JsonNode jsonNode2 = jsonNode.get("data");
					if (jsonNode2.has("data")) {
						JsonNode jsonNode3 = jsonNode2.get("data");

						Iterator<JsonNode> eles = jsonNode3.elements();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						List<Tag> list = new ArrayList<Tag>();
						while (eles.hasNext()) {
							JsonNode entry = eles.next();
							// TODO 使用instrospector监控或者filter
							JsonNode ut = entry.get("updateTime");
							String asText = ut.asText();
							LocalDateTime localDate = LocalDateTime
									.ofInstant(Instant.ofEpochMilli(Long.parseLong(asText)), ZoneId.systemDefault());
							String fut = localDate.format(formatter);
							Tag tag = Json.getInstance().convertValue(entry, Tag.class);
							tag.setUpdateTime(fut);
							list.add(tag);

						}

						SQLite.getDao(Tag.class).create(list);
					}
				}

			} else {
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
