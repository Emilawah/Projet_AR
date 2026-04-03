package httpserver.itf.impl;

import java.util.HashMap;

import httpserver.itf.HttpSession;

public class HttpSessionImpl implements HttpSession {

	private String m_id;
	private HashMap<String, Object> m_data;
	private long m_lastAccessedTime;
	
	public HttpSessionImpl(String id) {
		this.m_id = id;
		this.m_data = new HashMap<>();
		m_lastAccessedTime = System.currentTimeMillis();
	}
	
	@Override
	public String getId() {
		return m_id;
	}

	@Override
	public Object getValue(String key) {
		m_lastAccessedTime = System.currentTimeMillis();
		return m_data.get(key);
	}

	@Override
	public void setValue(String key, Object value) {
		m_lastAccessedTime = System.currentTimeMillis();
		m_data.put(key, value);

	}
	
	public long getLastAccessedTime() {
        return m_lastAccessedTime;
    }

}
