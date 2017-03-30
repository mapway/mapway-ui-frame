package com.ksyzt.gwt.server.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.nutz.json.Json;
import org.nutz.lang.Streams;

import com.ksyzt.gwt.client.site.urlrewrite.RewriteData;
import com.ksyzt.gwt.server.listener.KsyztServer;
import com.ksyzt.gwt.shared.exception.AdminLoginException;
import com.ksyzt.gwt.shared.module.AdminUser;
import com.ksyzt.gwt.shared.module.SiteInformation;

// TODO: Auto-generated Javadoc
/**
 * The Class SiteUtil.
 */
public class SiteUtil {
	
	/** The tag. */
	private static String tag = "SiteUtil";

	/** The request. */
	public HttpServletRequest request;
	
	/** The response. */
	public HttpServletResponse response;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {

	}

	/**
	 * Instantiates a new site util.
	 *
	 * @param request the request
	 * @param response the response
	 */
	public SiteUtil(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;

	}

	/**
	 * Gets the site info file.
	 *
	 * @param context the context
	 * @return the site info file
	 */
	public static String getSiteInfoFile(ServletContext context) {
		return getWebInfoPath(context) + "site.json";
	}

	/**
	 * Gets the from memory.
	 *
	 * @param context the context
	 * @param reload the reload
	 * @return the from memory
	 */
	public static SiteInformation getFromMemory(ServletContext context,
			boolean reload) {
		SiteInformation info = KsyztServer.SITEINFO;

		if (info != null) {
			return info;
		} else {
			File f = new File(getSiteInfoFile(context));
			if (f.exists()) {

				info = Json.fromJson(SiteInformation.class, Streams.fileInr(f));

				KsyztServer.SITEINFO = info;
			} else {
				info = new SiteInformation();
				KsyztServer.SITEINFO = info;
			}
		}
		return info;
	}

	/**
	 * Write xml file.
	 *
	 * @param doc the doc
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeXmlFile(Document doc, String file) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		FileOutputStream fos = new FileOutputStream(new File(file));
		XMLWriter xmlwriter = new XMLWriter(fos, format);
		xmlwriter.write(doc);
		xmlwriter.close();
		fos.close();
	}

	/**
	 * Gets the web info path.
	 *
	 * @param context the context
	 * @return the web info path
	 */
	public static String getWebInfoPath(ServletContext context) {
		String path = context.getRealPath("/");
		path += "WEB-INF" + File.separator;
		return path;
	}

	/**
	 * Write site info.
	 *
	 * @param context the context
	 * @param info the info
	 */
	public static void writeSiteInfo(ServletContext context,
			SiteInformation info) {

		info.setWhen(new Date());
		String json = Json.toJson(info);
		Util.WriteStringToFile(getSiteInfoFile(context), json);
		KsyztServer.SITEINFO = info;

	}

	/**
	 * Gets the url rewtire configure file.
	 *
	 * @param context the context
	 * @return the url rewtire configure file
	 */
	public static String getUrlRewtireConfigureFile(ServletContext context) {
		return getWebInfoPath(context) + "urlrewrite.xml";
	}

	/**
	 * Gets the admin user file.
	 *
	 * @param context the context
	 * @return the admin user file
	 */
	public static String getAdminUserFile(ServletContext context) {
		return getWebInfoPath(context) + "admin.json";
	}

	/**
	 * Confirm admin user.
	 *
	 * @param context the context
	 * @return the admin user
	 * @throws AdminLoginException the admin login exception
	 */
	public static AdminUser confirmAdminUser(ServletContext context)
			throws AdminLoginException {
		AdminUser u = null;

		File f = new File(getAdminUserFile(context));
		if (f.exists()) {

			u = Json.fromJson(AdminUser.class, Streams.fileInr(f));

		} else {
			throw new AdminLoginException(AdminLoginException.AE_NOFILE,
					"第一次使用本系统，请设置用户名和密码");
		}

		return u;
	}

	/**
	 * Write admin user.
	 *
	 * @param context the context
	 * @param user the user
	 * @return the admin user
	 * @throws AdminLoginException the admin login exception
	 */
	public static AdminUser writeAdminUser(ServletContext context,
			AdminUser user) throws AdminLoginException {

		String json = Json.toJson(user);

		Util.WriteStringToFile(getAdminUserFile(context), json);

		return user;
	}

	/**
	 * Gets the rewrite data from memory.
	 *
	 * @param context the context
	 * @return the rewrite data from memory
	 */
	public static List<RewriteData> getRewriteDataFromMemory(
			ServletContext context) {
		List<RewriteData> info = KsyztServer.REWRITEDATA;
		if (info != null) {
			return info;
		} else {
			info = new ArrayList<RewriteData>();
			File f = new File(getUrlRewtireConfigureFile(context));
			if (f.exists()) {
				String text = "";
				try {
					text = Util.readTextFile(f);
				} catch (IOException e1) {
				}
				try {
					Document doc = DocumentHelper.parseText(text);

					Element root = doc.getRootElement();
					List<Element> rules = (List<Element>) root.elements("rule");

					for (Element e : rules) {
						String note = e.elementTextTrim("note");
						String from = e.elementText("from");
						String to = e.elementText("to");
						RewriteData d = new RewriteData();
						d.desc = note;
						d.from = from;
						d.to = to;
						info.add(d);
					}
				} catch (Exception e) {
					System.out.println("SystemConsole urlrewrite>"
							+ e.getMessage());
				}
			}

		}
		return info;
	}

	/**
	 * Write rewrite data.
	 *
	 * @param context the context
	 * @param list the list
	 */
	public static void writeRewriteData(ServletContext context,
			List<RewriteData> list) {
		File f = new File(getUrlRewtireConfigureFile(context));

		try {
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("UTF-8");
			doc.addDocType("urlrewrite",
					"-//tuckey.org//DTD UrlRewrite 4.0//EN",
					"http://www.tuckey.org/res/dtds/urlrewrite4.0.dtd");

			Element root = DocumentHelper.createElement("urlrewrite");
			doc.setRootElement(root);
			for (RewriteData d : list) {
				Element e = root.addElement("rule");
				Element n = e.addElement("note");
				n.setText(d.desc);
				n = e.addElement("from");
				n.setText(d.from);
				n = e.addElement("to");
				n.setText(d.to);
			}
			OutputFormat format = OutputFormat.createPrettyPrint();

			FileOutputStream fos = new FileOutputStream(f);

			XMLWriter xmlwriter = new XMLWriter(fos, format);
			xmlwriter.write(doc);
			xmlwriter.close();
			fos.close();

		} catch (Exception e) {
			System.out.println("SystemConsole urlrewrite>" + e.getMessage());
		}

	}
}
