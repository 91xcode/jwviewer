package jwviewer.dao.impl;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import jwviewer.dao.ICookieDao;
import jwviewer.dto.Cookie;

public class CookieDao extends BaseDaoImpl<Cookie, String> implements ICookieDao {

	protected CookieDao(ConnectionSource connectionSource, Class<Cookie> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public CookieDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Cookie.class);
	}
}
