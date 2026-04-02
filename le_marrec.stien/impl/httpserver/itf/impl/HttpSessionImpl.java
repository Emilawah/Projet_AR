package httpserver.itf.impl;

import java.util.HashMap;

import httpserver.itf.HttpSession;

public class HttpSessionImpl implements HttpSession {

	private String m_id;
	private HashMap<String, Object> m_data;

	
	public HttpSessionImpl(String id) {
		this.m_id = id;
		this.m_data = new HashMap<>();
	}
	
	@Override
	public String getId() {
		return m_id;
	}

	@Override
	public Object getValue(String key) {
		return m_data.get(key);
	}

	@Override
	public void setValue(String key, Object value) {
		m_data.put(key, value);

	}

}
