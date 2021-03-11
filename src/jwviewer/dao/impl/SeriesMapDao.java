package jwviewer.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import jwviewer.dao.ISeriesMapDao;
import jwviewer.dto.Favorite;
import jwviewer.dto.Metadata;
import jwviewer.dto.Series;
import jwviewer.dto.SeriesMap;
import jwviewer.http.HTTP;
import jwviewer.json.Json;
import jwviewer.ormlite.SQLite;
import jwviewer.website.Api;

public class SeriesMapDao extends BaseDaoImpl<SeriesMap, String> implements ISeriesMapDao {

	protected SeriesMapDao(ConnectionSource connectionSource, Class<SeriesMap> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public SeriesMapDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, SeriesMap.class);
	}

	public static void main(String[] args) {
		for (int i = 1; i <= 249; i++) {
			System.out.println("pageNo:" + i);
			getSeries(i);
		}
	}

	public static void updateMetadataByFavoriteStar() {
		ArrayList<Integer> stars = new ArrayList<Integer>();
		Dao<Favorite, ?> fdao = SQLite.getDao(Favorite.class);
		for (Favorite favorite : fdao) {
			if (0 == favorite.getType()) {
				int starId = favorite.getTypeId();
				stars.add(starId);
			}
		}
		for (Integer starId : stars) {

			int metadataCount = getMetadataCountByStarId(starId);
			int pageNo = metadataCount / Api.TWENTY;
			int pm = metadataCount % Api.TWENTY;
			if (pm > 0) {
				++pageNo;
			}
			for (int i = 1; i <= pageNo; i++) {
				getMetadataByStarId(starId, i);
			}
		}
	}

	public static int getMetadataCountByStarId(int starId) {
		int count = 0;
		String api = Api.STAR_GETLIST + starId + "/1/1";
		HttpGet httpGet = new HttpGet(api);
		CloseableHttpResponse resp;
		try {
			resp = HTTP.getClient().execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
//			System.out.println(res);
			if (HttpStatus.SC_OK == statusCode) {
//			{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{metadata}],metaInfo:{star}}}
				JsonNode jsonNode = Json.getInstance().readTree(res);
				if (jsonNode.get("code").asInt() == 0) {
					JsonNode jsonNode2 = jsonNode.get("data");
					count = jsonNode2.get("total").asInt();
					System.out.println("count:" + count);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}

	public static void getMetadataByStarId(int starId, int pageNo) {
		Dao<Metadata, ?> mdao = SQLite.getDao(Metadata.class);
		String api = Api.STAR_GETLIST + starId + "/" + pageNo + "/" + Api.TWENTY;
		System.out.println(api);
		HttpGet httpGet = new HttpGet(api);
		CloseableHttpResponse resp;
		try {
			resp = HTTP.getClient().execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
//			System.out.println(res);
			if (HttpStatus.SC_OK == statusCode) {
//				{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{metadata}],metaInfo:{star}}}
				JsonNode jsonNode = Json.getInstance().readTree(res);
				if (jsonNode.get("code").asInt() == 0) {
					JsonNode data = jsonNode.get("data");
					JsonNode metadataArray = data.get("data");
					List<Metadata> list = Json.getInstance().convertValue(metadataArray,
							new TypeReference<List<Metadata>>() {
							});
					for (Metadata metadata : list) {
						System.out.println(
								"starId:" + starId + ",metadataId:" + metadata.getMetadataId() + "," + metadata.getjAVID());
//						System.out.println(Json.getInstance().writeValueAsString(metadata));
						metadata.setStarId(starId);
						mdao.update(metadata);
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void getSeries(int pageNo) {

		String starApi = Api.SERIES;
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
						List<Series> list = new ArrayList<Series>();
						while (eles.hasNext()) {
							JsonNode entry = eles.next();
							// TODO 使用instrospector监控或者filter
							JsonNode ut = entry.get("updateTime");
							String asText = ut.asText();
							LocalDateTime localDate = LocalDateTime
									.ofInstant(Instant.ofEpochMilli(Long.parseLong(asText)), ZoneId.systemDefault());
							String fut = localDate.format(formatter);
							Series series = Json.getInstance().convertValue(entry, Series.class);
							series.setUpdateTime(fut);
							list.add(series);

						}

						SQLite.getDao(Series.class).create(list);
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
