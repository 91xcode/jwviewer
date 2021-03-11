package jwviewer;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.j256.ormlite.dao.Dao;

import jwviewer.dto.CookieVo;
import jwviewer.dto.Star;
import jwviewer.ormlite.SQLite;

public class App {

	public static HttpClientBuilder httpClientBuilder = HttpClients.custom();
	public static CloseableHttpClient httpClient;
	public static BasicCookieStore cookieStore;
	public static ObjectMapper objectMapper = new ObjectMapper();
	static {

		// objectmapper
		// 序列化的时候序列对象的所有属性
		objectMapper.setSerializationInclusion(Include.ALWAYS);
		// 反序列化的时候如果多了其他属性,不抛出异常
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 如果是空对象的时候,不抛异常
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		// ClientCookie
		cookieStore = new BasicCookieStore();
		httpClientBuilder.setDefaultCookieStore(cookieStore);
		Builder configBuilder = RequestConfig.custom();
		configBuilder.setCookieSpec(CookieSpecs.STANDARD);
		RequestConfig requestConfig = configBuilder.build();
		httpClientBuilder.setDefaultRequestConfig(requestConfig);
		httpClient = httpClientBuilder.build();

		// init cookie
//		HashMap<String, Object> hashMap = new HashMap<String, Object>();
//		hashMap.put("domain", "fucklo.li");
//		Dao<jwviewer.dto.Cookie, ?> dao = SQLite.getDao(jwviewer.dto.Cookie.class);
//		try {
//			List<jwviewer.dto.Cookie> list = dao.queryForFieldValues(hashMap);
//			for (jwviewer.dto.Cookie cookie : list) {
//				String json = objectMapper.writeValueAsString(cookie);
////				System.out.println("cookie json:\n" + json);
//				CookieVo extCookie = objectMapper.readValue(json, CookieVo.class);
//				cookieStore.addCookie(extCookie);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}

	}

	public static void main(String[] args) {

//		login();
//		int count = getStarCount();
//		System.out.println(count);
//		int c = count / 50;
//		system.out.println(c);
//		int m = count % 50;
//		if (m > 0) {
//			++c;
//		}
//		system.out.println(c);
//		for (int i = 0; i < c; i++) {
//			getStar(i);
//		}

//		HashMap<String, Object> hashMap = new HashMap<String, Object>();
//		hashMap.put("name", "JULIA");
//		List<Star> values;
//		try {
//			Dao<Star, ?> dao = SQLite.getDao(Star.class);
//			values = dao.queryForFieldValues(hashMap);
//			System.out.println(values.size());
//			dao.delete(values.subList(1, values.size()));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}21250

//		truncateStar();

//		getStar(424,50);
		String vurl="https://repl.fucklo.li/VTJGc2RHVmtYMThaNldNTzJUZnJtRDNJYVByMEhKQ0VjWXRGaEx0c3NMbnE1WTlhQ1laTVJiYlM0UXgvNUZVUlBiQUp0dGVvRjArTXpuZnhuYm9OZXRZby9TT0ZXSWpTd3pXdzJySEJVenM9";
		HttpGet httpGet = new HttpGet(vurl);
		CloseableHttpResponse resp;
		try {
			resp = httpClient.execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
			System.out.println(res);
			System.out.println();
//			if (HttpStatus.SC_OK == statusCode) {
////			{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{}]}}
//				JsonNode jsonNode = objectMapper.readTree(res);
//				if (jsonNode.get("code").asInt() == 0) {
//					JsonNode jsonNode2 = jsonNode.get("data");
//					System.out.println("count:" + jsonNode2.get("total").asInt());
//					return jsonNode2.get("total").asInt();
//				}
//			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void truncateStar() {
		try {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			Dao<Star, ?> dao = SQLite.getDao(Star.class);
			List<Star> values = dao.queryForAll();
			for (int i = 0; i < values.size(); i++) {
				hashMap.put("name", values.get(i).getName());
				List<Star> list = dao.queryForFieldValues(hashMap);
				if (list.size() > 1) {
					System.out.println(list.get(0).getName() + ":" + list.size());
					dao.delete(list.subList(1, list.size()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void login() {
		String login = "https://fucklo.li/api/auth/login";
		String up = "{\"username\":\"bangbangyou\",\"password\":\"abc123456\"}";
		ContentType contentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), Charsets.UTF_8);
		StringEntity reqEntity = new StringEntity(up, contentType);
		HttpPost httpPost = new HttpPost(login);
		httpPost.setEntity(reqEntity);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == statusCode) {
//				HttpEntity entity = response.getEntity();
//				String res = EntityUtils.toString(entity, Charsets.UTF_8);
//				System.out.println("res:" + res);
//				 {"code":0,"msg":"Success","data":{"token":"drbV8AdMq3apE6cmJdFsQgG2kdBt8bsX"}}
//				JsonNode jsonNode = objectMapper.readTree(res);
//				if (jsonNode.get("code").asInt() == 0) {
//					JsonNode data = jsonNode.get("data");
//				}
				insertCookie();

			} else {

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertCookie() {
		// 读取cookie信息
//		try {
//			Thread.sleep(30000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		List<Cookie> cookielist = cookieStore.getCookies();
		try {
			for (Cookie cookie : cookielist) {
//				System.out.println(cookie);
				String writeValueAsString;
				writeValueAsString = objectMapper.writeValueAsString(cookie);
//				System.out.println(writeValueAsString);
//			String name = cookie.getName();
//			String value = cookie.getValue();
//			System.out.println(name + "=" + value);
//			System.out.println(cookie.getDomain());
//			System.out.println(cookie.getPath());
//			Date expiryDate = cookie.getExpiryDate();
//			LocalDateTime localDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//			String format = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
//			System.out.println(format);

				jwviewer.dto.Cookie dcookie = objectMapper.readValue(writeValueAsString, jwviewer.dto.Cookie.class);
				Dao<jwviewer.dto.Cookie, ?> dao = SQLite.getDao(jwviewer.dto.Cookie.class);
				dao.create(dcookie);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getStarCount() {
		String starApi = "https://fucklo.li/api/metadata/getMetaList/star/1";
		HttpGet httpGet = new HttpGet(starApi);
		CloseableHttpResponse resp;
		try {
			resp = httpClient.execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
			System.out.println(res);
			if (HttpStatus.SC_OK == statusCode) {
//			{"code": 0,"msg": "Success","data": {"total": 21180,"data": [{}]}}
				JsonNode jsonNode = objectMapper.readTree(res);
				if (jsonNode.get("code").asInt() == 0) {
					JsonNode jsonNode2 = jsonNode.get("data");
					System.out.println("count:" + jsonNode2.get("total").asInt());
					return jsonNode2.get("total").asInt();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

//	public static void getStar(int pageno) {//, int pagesize) {
	public static void getStar(int pageno,int pagesize) {

		String starApi = "https://fucklo.li/api/metadata/getMetaList/star/";
		starApi += pageno;
		starApi += "/";// 50
		starApi += pagesize;
		System.out.println(starApi);
		HttpGet httpGet = new HttpGet(starApi);
		try {
			CloseableHttpResponse resp = httpClient.execute(httpGet);
			int statusCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			String res = EntityUtils.toString(entity, Charsets.UTF_8);
			System.out.println(res);
			if (HttpStatus.SC_OK == statusCode) {
//				System.out.println(res);
//				{
//					"code": 0,
//					"msg": "Success",
//					"data": {
//						"total": 21180,
//						"data": [{}]
//					}
//				}
				JsonNode jsonNode = objectMapper.readTree(res);
				if (jsonNode.get("code").asInt() == 0) {
					JsonNode jsonNode2 = jsonNode.get("data");
					if (jsonNode2.has("data")) {
						JsonNode jsonNode3 = jsonNode2.get("data");

						Iterator<JsonNode> eles = jsonNode3.elements();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						List<Star> list = new ArrayList<Star>();
						while (eles.hasNext()) {
							JsonNode entry = eles.next();
							// TODO 使用instrospector监控或者filter
							JsonNode ut = entry.get("updateTime");
							String asText = ut.asText();
							LocalDateTime localDate = LocalDateTime
									.ofInstant(Instant.ofEpochMilli(Long.parseLong(asText)), ZoneId.systemDefault());
							String fut = localDate.format(formatter);
							Star star = objectMapper.convertValue(entry, Star.class);
							star.setUpdateTime(fut);
							list.add(star);
							System.out.println();

						}

//						SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("updateTime",new JacksonBeanPropertyFilter());
//						objectMapper.setFilterProvider(filterProvider);
//						AnnotationIntrospector sai = objectMapper.getSerializerProvider().getAnnotationIntrospector();
//						AnnotationIntrospector dai = objectMapper.getDeserializationConfig().getAnnotationIntrospector();
//						AnnotationIntrospector psai = AnnotationIntrospector.pair(sai, new JacksonAnnotationIntrospector());
//						AnnotationIntrospector.pair(dai, a2)
//						objectMapper.setAnnotationIntrospector(new  FieldConvertIntrospector());
//						objectMapper.setAnnotationIntrospectors(serializerAI, deserializerAI)
//						List<Star> list = objectMapper.convertValue(jsonNode3, new TypeReference<List<Star>>() {
//						});
//						System.out.println();
						SQLite.getDao(Star.class).create(list);
					}
				}

			} else {
				System.out.println("no:\n" + res);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//	public static <T> EntityWrapper<T> getEntityWrapper(String jsonString, Class<T> prototype) {
//	    ObjectMapper objectMapper = new ObjectMapper();
//	    EntityWrapper<T> wrapper = new EntityWrapper<T>();
//	    try {
//	      JsonNode root = objectMapper.readTree(jsonString);
//	      Iterator<Entry<String, JsonNode>> elements = root.fields();
//
//	      while (elements.hasNext()) {
//	        Entry<String, JsonNode> node = elements.next();
//	        String key = node.getKey();
//	        T element = objectMapper.readValue(node.getValue().toString(), prototype);
//	        wrapper.addEntry(key, element);
//	      }
//
//	      return wrapper;
//	    } catch (IOException e) {
//	      e.printStackTrace();
//	    }
//
//	    return null;
//	  }
}
