package com.ksyzt.gwt.server.listener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ksyzt.gwt.client.site.urlrewrite.RewriteData;
import com.ksyzt.gwt.server.common.Logger;
import com.ksyzt.gwt.server.common.SiteUtil;
import com.ksyzt.gwt.server.common.ZipUtility;
import com.ksyzt.gwt.shared.module.SiteInformation;

// TODO: Auto-generated Javadoc
/**
 * The Class KsyztServer.
 */
public class KsyztServer implements ServletContextListener {

	/** The rewritedata. */
	public static List<RewriteData> REWRITEDATA = null;

	/** The m timer. */
	private Timer m_timer;

	/**
	 * Info.
	 *
	 * @param info the info
	 */
	private void info(String info) {
		Logger.LOG.info(tag, info);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		m_timer.cancel();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {

		info("=====Server started (v1.0)==========");
		ServletContext context = event.getServletContext();
		long reloadtime = 10 * 60 * 60 * 1000;
		String v = event.getServletContext().getInitParameter(
				"site-configure-reload-time");
		if (v != null && v.length() > 0) {
			reloadtime = Long.valueOf(v);
		}
		m_timer = new Timer(true);

		String siteFile = SiteUtil.getSiteInfoFile(context);
		System.out.println("Site File:" + siteFile);
		File f = new File(siteFile);
		if (!f.exists()) {
			info(" X site configure file doesnt exist");
			info("create site configure @" + f.getAbsolutePath());
			SITEINFO = new SiteInformation();
			SITEINFO.setName("站点名称");
			SiteUtil.writeSiteInfo(context, SITEINFO);
		}

		info("extract console resource....");
		try {
			extractConsoleResource(context.getRealPath("/"));
			info("extract console resource ok");
		} catch (IOException e) {
			info("error to extract console resourceX");
		}

	}

	/** The siteinfo. */
	public static SiteInformation SITEINFO;

	/** The tag. */
	private static String tag = "SiteServer";

	/**
	 * Extract console resource.
	 *
	 * @param path the path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void extractConsoleResource(String path) throws IOException {
		String pathjar;
		pathjar = ZipUtility.getClassFile(getClass());

		try {
			ZipUtility.decompress(pathjar, path, "war/", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
