package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpRicmletResponse;
import httpserver.itf.HttpSession;

public class HttpRicmletRequestImpl extends HttpRicmletRequest {

	private String classname;
	private HashMap<String, String> parserArgs;

	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
		super(hs, method, ressname, br);
		parseURL();
	}

	@Override
	public HttpSession getSession() {
		return null;
	}

	@Override
	public String getArg(String name) {
		String args = parserArgs.get(name);
		if(args == null) {
			return "";
		}
		return parserArgs.get(name);
	}

	@Override
	public String getCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(HttpResponse resp) throws Exception {
		try {
			HttpRicmlet m_ricmlet = m_hs.getInstance(classname);
			HttpRicmletResponse m_ricmletResp = (HttpRicmletResponse) resp;

			m_ricmlet.doGet(this, m_ricmletResp);
		} catch (Exception e) {
			resp.setReplyError(404, "Ricmlet not found");
		}

	}
	
	public void parseURL() {
		this.parserArgs = new HashMap<>();
		String path; 
		String queryString = null;

		if (m_ressname.indexOf('?') != -1) {
			path = m_ressname.substring(0, m_ressname.indexOf('?'));
			queryString = m_ressname.substring(m_ressname.indexOf('?') + 1);
		} else {
			path = m_ressname;
		}

		if (path.startsWith("/ricmlets/")) {
			path = path.substring(10);
		} else if (path.startsWith("/ricmlets")) {
			path = path.substring(9);
		}

		String tempPath = path;
		if(tempPath.startsWith("/")) {
			tempPath = tempPath.substring(1);
		}
		
		this.classname = tempPath.replace('/', '.');

		if (queryString != null && !queryString.isEmpty()) {
			String[] pairs = queryString.split("&");
			for (String pair : pairs) {
				int separator = pair.indexOf('=');
				if (separator != -1) {
					String key = pair.substring(0, separator);
					String value = pair.substring(separator + 1);
					parserArgs.put(key, value);
				} else {
					parserArgs.put(pair, "");
				}
			}
		}
	}

}
