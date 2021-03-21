package jwviewer.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import jwviewer.dao.IFavoriteDao;
import jwviewer.dto.Favorite;
import jwviewer.dto.Star;
import jwviewer.dto.Tag;
import jwviewer.http.HTTP;
import jwviewer.json.Json;
import jwviewer.ormlite.SQLite;
import jwviewer.website.Api;

public class FavoriteDao extends BaseDaoImpl<Favorite, String> implements IFavoriteDao {

	protected FavoriteDao(ConnectionSource connectionSource, Class<Favorite> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public FavoriteDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Favorite.class);
	}

	public static void main(String[] args) {
		// tag(3571)
		// 3,5,15,19,22,32,,44,70,123,158,226,229
//		processTag();
		// ser
//		downloadBookmarkByBookmarkId(3570, 1, 2);

		// star
//		downloadBookmarkByBookmarkId(3569, 1, 0);
//		downloadBookmarkByBookmarkId(3569, 2, 0);

		// star2
//		downloadBookmarkByBookmarkId(3763, 1, 0);
//		downloadBookmarkByBookmarkId(3763, 2, 0);

//		updateTag();
		
		//video

		List<Favorite> favoriteStar = getFavoriteStar();
		for (Favorite favorite : favoriteStar) {
			int starId = favorite.getTypeId();
			VideoMapDao.wupdateVideoMapByStarId(starId);
			VideoMapDao.updateVideoMapByStarId(starId);
		}
	}

	public static void updateTag() {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("type", 1);
		Dao<Tag, String> tagDao = SQLite.getDao(Tag.class);
		Dao<Favorite, String> fDao = SQLite.getDao(Favorite.class);
		try {
			List<Favorite> list = fDao.queryForFieldValues(hashMap);
			System.out.println();
			for (Favorite favorite : list) {
				Tag tag = tagDao.queryForId(favorite.getTypeId() + "");
				favorite.setName(tag.getName());
				favorite.setUpdateTime(tag.getUpdateTime());
				fDao.update(favorite);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static List<Favorite> getFavoriteStar() {

		Dao<Favorite, String> fdao = SQLite.getDao(Favorite.class);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("type", 0);
		try {
			List<Favorite> list = fdao.queryForFieldValues(hashMap);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Favorite> downloadBookmarkByBookmarkId(int bookmarkId, int pageNo, int type) {
		List<Favorite> list = new ArrayList<Favorite>();
		String api = Api.BOOKMARK_GETINFO;
		api += bookmarkId;
		api += "/";
		api += pageNo;
		api += "/";
		api += Api.FIFTY;
		HttpGet httpGet = new HttpGet(api);
		try {
			CloseableHttpResponse resp = HTTP.getClient().execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
			if (HttpStatus.SC_OK == statusCode) {
//				{"code": 0,"msg": "Success","data": {"metadatas": []}}
				JsonNode jsonNode = Json.getInstance().readTree(res);
				if (jsonNode.get("code").asInt() == 0) {

					JsonNode jsonNode2 = jsonNode.get("data");
					if (jsonNode2.has("metadatas")) {
						JsonNode metadatas = jsonNode2.get("metadatas");

						if (0 == type) {
							processStar(metadatas, type);
						}
						if (2 == type) {
							processSeries(metadatas, type);
						}

					}

				}

			} else {
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void processStar(JsonNode metadatas, int type) {
		List<Favorite> list = new ArrayList<Favorite>();
		Iterator<JsonNode> eles = metadatas.elements();
		while (eles.hasNext()) {
			JsonNode entry = eles.next();
			int starId = entry.get("stars").get(0).get("id").asInt();
			System.out.println(starId);
			Favorite favorite = new Favorite();
			favorite.setType(type);
			favorite.setTypeId(starId);
			favorite.setName(entry.get("stars").get(0).get("name").asText());
			favorite.setUpdateTime(entry.get("stars").get(0).get("updateTime").asText());
			list.add(favorite);
		}

		try {
			SQLite.getDao(Favorite.class).create(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void processTag() {
		List<Favorite> list = new ArrayList<Favorite>();
		String[] tags = "3,5,15,19,22,32,44,70,123,158,226,229".split(",");
		for (int i = 0; i < tags.length; i++) {
			Favorite favorite = new Favorite();
			favorite.setType(1);
			favorite.setTypeId(Integer.valueOf(tags[i]));
			list.add(favorite);
		}

		try {
			SQLite.getDao(Favorite.class).create(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void processSeries(JsonNode metadatas, int type) {
		List<Favorite> list = new ArrayList<Favorite>();
		Iterator<JsonNode> iterator = metadatas.iterator();
		while (iterator.hasNext()) {
			JsonNode jsonNode = (JsonNode) iterator.next();
			Favorite favorite = new Favorite();
			favorite.setType(type);
			favorite.setTypeId(jsonNode.get("series").get("id").asInt());
			favorite.setName(jsonNode.get("series").get("name").asText());
			favorite.setUpdateTime(jsonNode.get("series").get("updateTime").asText());
			list.add(favorite);
		}
		try {
			SQLite.getDao(Favorite.class).create(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
