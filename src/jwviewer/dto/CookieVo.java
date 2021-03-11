package jwviewer.dto;

import org.apache.http.impl.cookie.BasicClientCookie;

public class CookieVo extends BasicClientCookie {

	private static final long serialVersionUID = 1L;

	public CookieVo() {
		super("", "");
	}

	public CookieVo(String name, String value) {
		super(name, value);

	}

}
