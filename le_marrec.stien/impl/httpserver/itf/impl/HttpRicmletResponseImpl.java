package httpserver.itf.impl;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpRicmletResponse;

public class HttpRicmletResponseImpl extends HttpResponseImpl implements HttpRicmletResponse {

	private HashMap<String,String> m_setCookies = new HashMap<>();
	private boolean m_headerSent = false;
	
	protected HttpRicmletResponseImpl(HttpServer hs, HttpRequest req, PrintStream ps) {
		super(hs, req, ps);
	}

	@Override
	public void setCookie(String name, String value) {
		m_setCookies.put(name, value);
	}

	public void sendHeaders() {
		
		if(m_headerSent) return;
		
		m_ps.print("HTTP/1.1 200 OK\r\n");
		
		for(String name : m_setCookies.keySet()) {
			String value = m_setCookies.get(name);
			m_ps.print("Set-Cookie: " + name + "=" + value +"\r\n");
		}
		 
		m_ps.print("\r\n");
		m_headerSent = true;
	}
	
	@Override
    public PrintStream beginBody() {
        sendHeaders(); // On s'assure que les cookies partent AVANT le HTML
        return m_ps;
    }
	


}
