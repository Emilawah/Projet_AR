package httpserver.itf.impl;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpRicmletResponse;

public class HttpRicmletResponseImpl extends HttpResponseImpl implements HttpRicmletResponse {

	private HashMap<String,String> m_setCookies = new HashMap<>();
	
	protected HttpRicmletResponseImpl(HttpServer hs, HttpRequest req, PrintStream ps) {
		super(hs, req, ps);
	}

	@Override
	public void setCookie(String name, String value) {
		m_setCookies.put(name, value);
	}

	
	@Override
    public PrintStream beginBody() {
        for(String cookieName : m_setCookies.keySet()) {
        	String cookieValue = m_setCookies.get(cookieName);
        	m_ps.println("Set-Cookie: "+ cookieName + "=" + cookieValue);
        }
		return super.beginBody();
    }
	


}
