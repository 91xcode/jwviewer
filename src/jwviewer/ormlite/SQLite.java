package jwviewer.ormlite;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import jwviewer.dto.Cookie;

public class SQLite {

	public static ConnectionSource connectionSource;

	static {
		try {
			connectionSource = new JdbcConnectionSource("jdbc:sqlite:database.db");
			System.out.println("sqlite init");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Dao<jwviewer.dto.Cookie, String> cookieDao =
		// DaoManager.createDao(connectionSource, jwviewer.dto.Cookie.class);

	}

	public static <T> Dao<T, String> getDao(Class<T> clazz) {
		Dao<T, String> dao = null;
		try {
			TableUtils.createTableIfNotExists(connectionSource, clazz);
			dao = DaoManager.createDao(connectionSource, clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dao;
	}

	public static <T> void createTable(Class<T> clazz) {
//		TableUtils.createTable(dao);
		try {
			TableUtils.createTable(connectionSource, Cookie.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			Dao<jwviewer.dto.Cookie, String> cookieDao = DaoManager.createDao(connectionSource,
					jwviewer.dto.Cookie.class);
//			TableUtils.createTable(cookieDao);
			for (int i = 0; i < 10; i++) {

				jwviewer.dto.Cookie cookie = new jwviewer.dto.Cookie();
				cookie.setName("test" + i);
				cookieDao.create(cookie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
