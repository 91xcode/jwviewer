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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import jwviewer.dao.IScreenshotDao;
import jwviewer.dto.Screenshot;
import jwviewer.http.HTTP;
import jwviewer.json.Json;
import jwviewer.ormlite.SQLite;
import jwviewer.website.Api;

public class ScreenshotDao extends BaseDaoImpl<Screenshot, String> implements IScreenshotDao {

	protected ScreenshotDao(ConnectionSource connectionSource, Class<Screenshot> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public ScreenshotDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Screenshot.class);
	}

	public static void main(String[] args) {
		for (int i = 1; i <= 9; i++) {

			getTag(i);
		}
	}

	public void getCount() {

	}

	public static void getTag(int pageNo) {

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
						List<Screenshot> list = new ArrayList<Screenshot>();
						while (eles.hasNext()) {
							JsonNode entry = eles.next();
							// TODO 使用instrospector监控或者filter
							JsonNode ut = entry.get("updateTime");
							String asText = ut.asText();
							LocalDateTime localDate = LocalDateTime
									.ofInstant(Instant.ofEpochMilli(Long.parseLong(asText)), ZoneId.systemDefault());
							String fut = localDate.format(formatter);
							Screenshot tag = Json.getInstance().convertValue(entry, Screenshot.class);
							tag.setUpdateTime(fut);
							list.add(tag);

						}

						SQLite.getDao(Screenshot.class).create(list);
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
