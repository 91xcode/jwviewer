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

import jwviewer.dao.IBookmarkDao;
import jwviewer.dao.ITagDao;
import jwviewer.dto.Bookmark;
import jwviewer.dto.Tag;
import jwviewer.http.HTTP;
import jwviewer.json.Json;
import jwviewer.ormlite.SQLite;
import jwviewer.website.Api;

public class BookmarkDao extends BaseDaoImpl<Bookmark, String> implements IBookmarkDao {

	protected BookmarkDao(ConnectionSource connectionSource, Class<Bookmark> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public BookmarkDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Bookmark.class);
	}

	public static void main(String[] args) {
	 getList();
	}

	
	public static void getList() {

		HttpGet httpGet = new HttpGet(Api.BOOKMARK_GETLIST);
		try {
			CloseableHttpResponse resp = HTTP.getClient().execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
			System.out.println(res);
			System.out.println(statusCode);
			if (HttpStatus.SC_OK == statusCode) {
//				{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{}]}}
				JsonNode jsonNode = Json.getInstance().readTree(res);
				System.out.println(jsonNode.get("code").asInt());
				if (jsonNode.get("code").asInt() == 0) {

					if (jsonNode.has("data")) {
						JsonNode jsonNode3 = jsonNode.get("data");
						Iterator<JsonNode> eles = jsonNode3.elements();
						List<Bookmark> list = new ArrayList<Bookmark>();
						while (eles.hasNext()) {
							JsonNode entry = eles.next();
							Bookmark bookmark = Json.getInstance().convertValue(entry, Bookmark.class);
							list.add(bookmark);
						}

						SQLite.getDao(Bookmark.class).create(list);
					}
				}else {
					System.out.println("n0");
				}

			} else {
				System.out.println("nok");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
