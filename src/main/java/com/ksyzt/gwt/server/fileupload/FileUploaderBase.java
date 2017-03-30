package com.ksyzt.gwt.server.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ksyzt.gwt.server.common.Util;

// TODO: Auto-generated Javadoc
/**
 * 有进度条的上传.
 *
 * @author zhangjianshe@gmail.com
 */
public class FileUploaderBase extends HttpServlet {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The ext map. */
	// 定义允许上传的文件扩展名
	protected HashMap<String, String> extMap = new HashMap<String, String>();
	
	/** The max size. */
	// 最大文件大小 100 M --测试用
	protected long maxSize = 500 * 1024 * 1024;
	
	/** The config path. */
	// 上传文件的保存路径
	protected String configPath = "upload";

	/** The dir temp. */
	protected String dirTemp = "attached/temp";

	/** The dir name. */
	protected String dirName = "file";

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {

		// 定义允许上传的文件扩展名
		// extMap.put("image", "gif,jpg,jpeg,png,bmp");
		// extMap.put("flash", "swf,flv");
		// extMap.put("media",
		// "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,apk");

	}

	/**
	 * 文档扩展名.
	 *
	 * @param item the item
	 * @return the extension
	 */
	protected String getExtension(FileItem item) {
		String fileName = item.getName();
		// 检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
				.toLowerCase();
		return fileExt;
	}

	/**
	 * 获取文件名.
	 *
	 * @param item the item
	 * @return the file name
	 */
	protected String getFileName(FileItem item) {
		String fileName = item.getName();
		// 出去路径的文件名
		fileName = fileName.replace('\\', '/');
		int index = fileName.lastIndexOf('/');
		if (index >= 0) {
			fileName = fileName.substring(index + 1);
		}
		return fileName;
	}

	/**
	 * 获取保存到服务上文件名.
	 *
	 * @param item the item
	 * @return the save file name
	 */
	protected String getSaveFileName(FileItem item) {
		return Util.randomString(8) + "." + getFileName(item);
	}

	/**
	 * 处理上传文件.
	 *
	 * @param request the request
	 * @param out the out
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public void processFileUpload(HttpServletRequest request, PrintWriter out)
			throws ServletException, IOException {
		
		// 文件保存目录路径
		Date now = new Date();
		String dayPath = "/" + "day_"
				+ Util.getDateString(now.getTime(), "yyMMdd") + "/";

		String rootpath = this.getServletContext().getRealPath("/");
		String savePath = rootpath + "/" + configPath + dayPath;

		// 临时文件目录
		String tempPath = rootpath + dirTemp;

		// 创建文件夹
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		// 创建临时文件夹
		File dirTempFile = new File(tempPath);
		if (!dirTempFile.exists()) {
			dirTempFile.mkdirs();
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(20 * 1024 * 1024); // 设定使用内存超过5M时，将产生临时文件并存储于临时目录中。
		factory.setRepository(new File(tempPath)); // 设定存储临时文件的目录。

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");

		// 创建一个进度监听器
		FileUploadListener progressListener = new FileUploadListener(request);
		upload.setProgressListener(progressListener);

		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();

				long fileSize = item.getSize();
				if (!item.isFormField()) {
					String newFileName = getSaveFileName(item);
					String pathName = configPath + "/" + dayPath + "/"
							+ newFileName;
					// 检查文件大小
					if (item.getSize() > maxSize) {
						setStatusMsg(request, "1", "上传文件大小超过限制。", newFileName);
						return;
					}
					// 检查扩展名
					String fileExt = getExtension(item);
					if (!Arrays.<String> asList(extMap.get(dirName).split(","))
							.contains(fileExt)) {
						setStatusMsg(request, "1", "上传文件扩展名是不允许的扩展名。只允许"
								+ extMap.get(dirName) + "格式。", pathName);
						return;
					}

					try {
						File uploadedFile = new File(savePath, newFileName);

						/*
						 * 第一种方法
						 * 
						 * 好处： 一目了然..简单啊... 弊端： 这种方法会导致上传的文件大小比原来的文件要大
						 * 
						 * 推荐使用第二种
						 */
						// item.write(uploadedFile);
						// --------------------------------------------------------------------
						// 第二种方法
						OutputStream os = new FileOutputStream(uploadedFile);
						InputStream is = item.getInputStream();
						byte buf[] = new byte[4 * 1024];// 可以修改 1024 以提高读取速度
						int length = 0;
						while ((length = is.read(buf)) > 0) {
							os.write(buf, 0, length);
						}
						// 关闭流
						os.flush();
						os.close();
						is.close();

					} catch (Exception e) {
						setStatusMsg(request, "1", "上传文件失败。", pathName);
						return;
					}
					setStatusMsg(request, "2", "文件上传成功！", pathName);
				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 错误信息的处理.
	 *
	 * @param request the request
	 * @param error            -- 1 ： 错误 0 ： 正常 2 : 上传完成
	 * @param message the message
	 * @param filepathname the filepathname
	 */
	private void setStatusMsg(HttpServletRequest request, String error,
			String message, String filepathname) {
		HttpSession session = request.getSession();
		FileUploadStatus status = (FileUploadStatus) session
				.getAttribute("upladeStatus");
		status.setError(error);
		status.setStatusMsg(message);
	}

	/**
	 * 获取状态信息.
	 *
	 * @param request the request
	 * @param out the out
	 * @return the status msg
	 */
	@SuppressWarnings("unused")
	private void getStatusMsg(HttpServletRequest request, PrintWriter out) {
		HttpSession session = request.getSession();
		FileUploadStatus status = (FileUploadStatus) session
				.getAttribute("upladeStatus");
		System.out.println("输出信息对象：" + status);
		out.println(status.toJSon());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();
		// 检查输入请求是否为multipart表单数据。
		@SuppressWarnings("deprecation")
		boolean isMultipart = FileUpload.isMultipartContent(request);
		if (isMultipart) {
			processFileUpload(request, out);
		} else {
			if (request.getParameter("uploadStatus") != null) {
				// response.setContentType("text/xml");
				// response.setHeader("Cache-Control", "no-cache");
				System.out.println("ajax 读取状态····");
				getStatusMsg(request, out);
			}
			else
			{
				
			}
		}

		out.flush();
		out.close();
	}

}
