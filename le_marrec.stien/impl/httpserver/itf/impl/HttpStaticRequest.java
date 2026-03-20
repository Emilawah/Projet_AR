package httpserver.itf.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;

/*
 * This class allows to build an object representing an HTTP static request
 */
public class HttpStaticRequest extends HttpRequest {
	static final String DEFAULT_FILE = "index.html";

	public HttpStaticRequest(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}

	public void process(HttpResponse resp) throws Exception {

		File m_folder = m_hs.getFolder();
		String path = m_ressname;

		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		
		if (path.equals("") || path.endsWith("/")) {
			path += DEFAULT_FILE;
		}

		File downloadFile = new File(m_folder,path);

		if (downloadFile.exists() && downloadFile.isFile()) {
			resp.setReplyOk();
			resp.setContentLength((int) downloadFile.length());
			
			PrintStream ps = resp.beginBody();
			
			FileInputStream fis = new FileInputStream(downloadFile);
			
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				ps.write(buffer, 0, bytesRead);
			}
			
			ps.flush();
			fis.close();
		
		} else {
			resp.setReplyError(404, "File not found");
		}

	}
}
