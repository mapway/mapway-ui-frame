package com.ksyzt.gwt.server.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ksyzt.gwt.server.common.Util;

// TODO: Auto-generated Javadoc
/**
 * The Class postimage.
 */
public class postimage extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public postimage() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Save image.
	 *
	 * @param dstImage the dst image
	 * @param dstName the dst name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static void saveImage(BufferedImage dstImage, String dstName)
			throws IOException {
		String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
		// FileOutputStream out = new FileOutputStream(dstName);
		// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		// encoder.encode(dstImage);
		ImageIO.write(dstImage, /* "GIF" */formatName /* format desired */,
				new File(dstName) /* target */);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");
		response.setCharacterEncoding("GB2312");
		PrintWriter out = response.getWriter();

		String loc = request.getParameter("loc");
		String id = request.getParameter("id");
		StringBuilder sb = new StringBuilder(id.length() * 2);
		for (int i = 0; i < id.length() - 1; i++) {
			sb.append(id.charAt(i)).append(File.separator);
		}
		sb.append(id.charAt(id.length() - 1));
		String path = request.getContextPath();

		path = request.getSession().getServletContext().getRealPath("/");

		String dir = path + loc + File.separator + sb.toString();
		File f = new File(dir);
		f.mkdirs();

		BufferedInputStream ins = new BufferedInputStream(
				request.getInputStream());

		byte[] b = new byte[2046];
		byte[] size = new byte[4];
		ins.read(size);
		int ssize = toInt(size);
		int c = ins.read(b);

		String fileName = dir + File.separator + ssize + ".jpg";

		File f1 = new File(fileName);
		java.io.FileOutputStream os = new FileOutputStream(f1);

		while (c > 0) {
			os.write(b, 0, c);
			c = ins.read(b);
		}

		ins.close();
		os.flush();
		os.close();

		String randomfile = "";
		if (ssize == 128) {
			randomfile = Util.randomString(8) + ".jpg";
			copy(f1, new File(dir + File.separator + randomfile));
		}

		BufferedImage src = javax.imageio.ImageIO.read(f1);

		BufferedImage tag = new BufferedImage(64, 64,
				BufferedImage.TYPE_INT_RGB);

		tag.getGraphics().drawImage(src, 0, 0, 64, 64, null);
		String file64 = dir + File.separator + 64 + ".jpg";

		saveImage(tag, file64);

		tag = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(src, 0, 0, 32, 32, null);
		String file32 = dir + File.separator + 32 + ".jpg";
		saveImage(tag, file32);

		tag = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(src, 0, 0, 16, 16, null);
		String file16 = dir + File.separator + 16 + ".jpg";
		saveImage(tag, file16);

		out.print("{returncode:'1',msg:'" + randomfile + "'}");
		out.flush();
		out.close();
	}

	/**
	 * To int.
	 *
	 * @param bRefArr the b ref arr
	 * @return the int
	 */
	public static int toInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < 4; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * (3 - i));
		}

		return iOutcome;
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	/**
	 * Copy.
	 *
	 * @param source the source
	 * @param dest the dest
	 */
	public void copy(File source, File dest) {
		try {
			int byteread = 0;

			if (source.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(source); // 读入原文件
				@SuppressWarnings("resource")
				FileOutputStream fout = new FileOutputStream(dest);
				byte[] buffer = new byte[2048];

				while ((byteread = inStream.read(buffer)) != -1) {
					fout.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}
	}
}
