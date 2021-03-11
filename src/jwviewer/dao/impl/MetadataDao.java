package jwviewer.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import jwviewer.dao.IMetadataDao;
import jwviewer.dto.Metadata;
import jwviewer.dto.Screenshot;
import jwviewer.http.HTTP;
import jwviewer.json.Json;
import jwviewer.ormlite.SQLite;
import jwviewer.website.Api;

public class MetadataDao extends BaseDaoImpl<Metadata, String> implements IMetadataDao {

	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	protected MetadataDao(ConnectionSource connectionSource, Class<Metadata> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public MetadataDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Metadata.class);
	}

	public static void main(String[] args) {

//		int count = getCount();
//		System.out.println(count);
//		int c = count / Api.FIFTY;
//		int m = count % Api.FIFTY;
//		if (0 != m) {
//			++c;
//		}
//
//		for (int i = 1; i <= c; i++) {
//			System.out.println("第" + i + "页,还剩" + (c - i) + "页," + "第" + (((i - 1) * Api.FIFTY) + 1) + "--"
//					+ (i * Api.FIFTY) + "条");
//			getMetadata(i);
//		}
		try {
			List<Metadata> list = SQLite.getDao(Metadata.class).queryForAll();
			List<Integer> ids = new ArrayList<Integer>();
			List<Integer> miss = new ArrayList<Integer>();

			for (Metadata metadata : list) {
				int metadataId = metadata.getMetadataId();
				ids.add(metadataId);
			}
			for (int i = 1; i <= 81473; i++) {
				if (!ids.contains(i)) {
//					System.out.println(i);
					miss.add(i);
				}
			}

			List<Metadata> ml = new ArrayList<Metadata>();

			for (int i = 0; i < miss.size(); i++) {
				Metadata info = getInfo(miss.get(i));
				if (null != info) {
					ml.add(info);
				}
			}
			System.out.println(ml.size());
			SQLite.getDao(Metadata.class).create(ml);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 17,
//		getInfo(17);

	}

	public static List<Metadata> getMetadataByStarId(int starId) {
		List<Metadata> list = new ArrayList<Metadata>();
		Dao<Metadata, ?> mdao = SQLite.getDao(Metadata.class);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("starId", starId);
		try {
			list = mdao.queryForFieldValues(params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Metadata getInfo(int metadataId) {
		Metadata metadata = null;
		String api = Api.METADATA_GETINFO;
		api += metadataId;
//		System.out.println(api);
		HttpGet httpGet = new HttpGet(api);
		try {
			CloseableHttpResponse resp = HTTP.getClient().execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
//			System.out.println(res);
			if (HttpStatus.SC_OK == statusCode) {

//				{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{}]}}
				JsonNode jsonNode = Json.getInstance().readTree(res);
				JsonNode data = jsonNode.get("data");
				metadata = Json.getInstance().convertValue(data, Metadata.class);
				if (metadata != null) {
					System.out.println(metadataId + "," + metadata.getMetadataId());
					System.out.println(data);
				}
			}
		} catch (IOException e) {
		}
		return metadata;
	}

	public static int getCount() {
		int count = 0;
		String starApi = Api.METADATA + "1/1";
		HttpGet httpGet = new HttpGet(starApi);
		CloseableHttpResponse resp;
		try {
			resp = HTTP.getClient().execute(httpGet);

			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
			if (HttpStatus.SC_OK == statusCode) {
//				{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{}]}}
				JsonNode jsonNode = Json.getInstance().readTree(res);
				if (jsonNode.get("code").asInt() == 0) {
					JsonNode data = jsonNode.get("data");
					if (data.has("total")) {
						count = data.get("total").asInt();
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;

	}

	public static List<Metadata> getMetadata(int pageNo) {

		List<Metadata> list = new ArrayList<Metadata>();
		String api = Api.METADATA;
		api += pageNo;
		api += "/";
		api += Api.FIFTY;
		System.out.println(api);
		HttpGet httpGet = new HttpGet(api);
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
						while (eles.hasNext()) {
							JsonNode entry = eles.next();
							// TODO 使用instrospector监控或者filter
							JsonNode ut = entry.get("updateTime");
							String asText = ut.asText();
							LocalDateTime localDate = LocalDateTime
									.ofInstant(Instant.ofEpochMilli(Long.parseLong(asText)), ZoneId.systemDefault());
							String fut = localDate.format(formatter);
							Metadata metadata = Json.getInstance().convertValue(entry, Metadata.class);
							metadata.setUpdateTime(fut);
							list.add(metadata);
						}

						SQLite.getDao(Metadata.class).create(list);
					}
				}

			} else {
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(api);
		}
		return list;
	}

	@Deprecated
	public static void degetMetadata(int pageNo) {

		String api = Api.METADATA;
		api += pageNo;
		api += "/";// 21
		api += Api.METADATAPAGESIZE;
		HttpGet httpGet = new HttpGet(api);
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
						List<Metadata> list = new ArrayList<Metadata>();
						List<Screenshot> screenshots = new ArrayList<Screenshot>();
						while (eles.hasNext()) {
							JsonNode entry = eles.next();
							System.out.println(entry);
							// TODO 使用instrospector监控或者filter
							JsonNode ut = entry.get("updateTime");
							String asText = ut.asText();
							LocalDateTime localDate = LocalDateTime
									.ofInstant(Instant.ofEpochMilli(Long.parseLong(asText)), ZoneId.systemDefault());
							String fut = localDate.format(formatter);
							Metadata metadata = Json.getInstance().convertValue(entry, Metadata.class);
							metadata.setUpdateTime(fut);
							list.add(metadata);

							JsonNode picArray = entry.get(Api.SCREENSHOTFILESURL);
							for (int i = 0; i < picArray.size(); i++) {
								JsonNode pic = picArray.get(i);
								Screenshot screenshot = new Screenshot();
								String url = pic.asText();
								screenshot.setUrl(url);
								screenshot.setMetadataId(metadata.getMetadataId());
								if (url.endsWith(".jpg") || url.endsWith(".png")) {
									screenshot.setName(url.substring(url.lastIndexOf("/") + 1));
								}
								screenshots.add(screenshot);
							}

						}

						SQLite.getDao(Screenshot.class).create(screenshots);
						SQLite.getDao(Metadata.class).create(list);
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
