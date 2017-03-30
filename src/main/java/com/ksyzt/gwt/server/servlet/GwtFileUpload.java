package com.ksyzt.gwt.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.nutz.json.Json;

import com.ksyzt.gwt.server.common.Thumbnail;
import com.ksyzt.gwt.server.common.Util;

// TODO: Auto-generated Javadoc
/**
 * 文件上传 返回JSON格式.
 *
 * @author zhangjianshe@gmail.com
 */
@SuppressWarnings("deprecation")
public class GwtFileUpload extends HttpServlet {
	
	/** 序列值. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public GwtFileUpload() {
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

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		UploadReturn r = new UploadReturn();

		// 设置保存上传文件的目录

		String err = "";
		String immediate = "";
		String attach_dir;
		String attach_subdir;
		String filename = "";
		String extension = "";
		String target = "";
		String thmnbfilename = "";

		// 上传文件路径
		String uploadDir = getServletContext().getRealPath("upload");
		File fUploadDir = new File(uploadDir);

		if (!fUploadDir.exists()) {
			fUploadDir.mkdirs();
		}
		try {
			if (true) {
				FileItemFactory factory = new DiskFileItemFactory();

				ServletFileUpload upload = new ServletFileUpload(factory);
				// 磁盘上传对象

				// 最多上传10M数据
				upload.setSizeMax(1024 * 1024 * 100);
				// 超过1M的字段数据采用临时文件缓存
				// 采用默认的临时文件存储位置
				// fu.setRepositoryPath(...);
				// 设置上传的普通字段的名称和文件字段的文件名所采用的字符集编码
				upload.setHeaderEncoding("UTF-8");

				// 得到所有表单字段对象的集合
				List<FileItem> fileItems = null;

				fileItems = (List<FileItem>) upload.parseRequest(request);

				// 处理每个表单字段
				if (fileItems != null) {
					Iterator<FileItem> i = fileItems.iterator();
					while (i.hasNext()) {
						FileItem fi = (FileItem) i.next();
						if (fi.isFormField()) {
							String content = new String(fi.getString("UTF-8"));
							String fieldName = fi.getFieldName();
							request.setAttribute(fieldName, content);
						}
					}
				}
				// 确定必要的变量 缩略图的宽度 和高度
				String width = (String) request.getAttribute("width");
				String height = (String) request.getAttribute("height");

				int nwidth = 140;
				int nheight = 115;
				if (width != null && width.length() > 0) {
					nwidth = Integer.parseInt(width);
				}
				if (height != null && height.length() > 0) {
					nheight = Integer.parseInt(height);
				}

				// 处理上传的文件
				if (fileItems != null) {
					// 处理每个表单字段
					Iterator i = fileItems.iterator();
					while (i.hasNext()) {
						FileItem fi = (FileItem) i.next();
						if (fi.isFormField()) {

						} else {
							try {
								String pathSrc = fi.getName();
								if (pathSrc.trim().equals("")) {
									continue;
								}
								// 取上载文件后缀名
								extension = pathSrc.substring(pathSrc
										.lastIndexOf(".") + 1);
								extension = extension.toLowerCase();
								if (("," + upext + ",").indexOf("," + extension
										+ ",") < 0) {
									err = "上传文件扩展名必需为：" + upext;
								} else {
									Date now = new Date();
									attach_subdir = "day_"
											+ Util.getDateString(now.getTime(),
													"yyMMdd");

									attach_dir = fUploadDir + "/"
											+ attach_subdir + "/";
									File tempFile = new File(attach_dir);
									if (!tempFile.exists()) {
										tempFile.mkdirs();
									}

									// 生成随机文件名
									filename = Util.randomString(8) + "."
											+ extension;

									// 缩略图文件名
									thmnbfilename = filename.substring(0,
											filename.lastIndexOf("."))
											+ "_s.jpg";
									// 上传到磁盘的原始文件
									target = "upload/" + attach_subdir + "/"
											+ filename;

									r.data.path = target;

									File pathDest = new File(attach_dir
											+ filename);
									fi.write(pathDest);
									if (isPicture(extension)) {
										// 缩略图
										Thumbnail nail = new Thumbnail(
												attach_dir + filename,
												attach_dir + thmnbfilename);
										nail.resize(nwidth, nheight);
										// String target_thumb = "upload/" +
										// attach_subdir + "/"
										// + thmnbfilename;
									}
								}
							} catch (Exception e) {

								err = "存储文件时出现如下问题：" + e.getMessage();
								e.printStackTrace();
							} finally // 总是立即删除保存表单字段内容的临时文件
							{
								fi.delete();
							}
						}
					}
				}
			} else {
				err = "不支持上传的格式";
			}
		} catch (FileUploadException e1) {
			err = e1.getMessage();
			e1.printStackTrace();
		}
		int msgtype = 2; // 返回上传参数的格式：1，只返回url，2，返回参数数组
		if (immediate != null && immediate.equals("1")) {
			target = "!" + target;
		}

		if (msgtype == 1) {
			r.msg = target;
		} else {
			r.data.url = target;
			r.data.id = "1";
			r.msg = target;
		}
		r.err = err;
		out.print(Json.toJson(r));

		out.flush();
		out.close();
	}

	/**
	 * 是否是图片.
	 *
	 * @param extension the extension
	 * @return true, if is picture
	 */
	private boolean isPicture(String extension) {
		if (extension != null) {
			String ext = extension.toLowerCase();
			if (ext.equals("bmp") || ext.equals("jpg") || ext.equals("png")
					|| ext.equals("jpeg")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		String ext = this.getInitParameter("upext");
		if (ext != null && ext.length() > 0) {
			upext = ext;
		}
	}

	/**
	 * Gets the base path.
	 *
	 * @param request the request
	 * @return the base path
	 */
	public String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		String port = "";
		if (request.getServerPort() != 80) {
			port = ":" + request.getServerPort();
		}
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ port + path + "/";
		return basePath;
	}

	/** The upext. */
	private String upext = "txt,rar,zip,jpg,jpeg,gif,png,swf,wmv,avi,wma,mp3,mid";

}
