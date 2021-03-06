package il.ac.tau.cs.smlab.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;




//@WebServlet(name = "DifferenceMiningImageServlet", urlPatterns = { "/dmis" })
public class DifferenceMiningImageServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. 
	 */
	public DifferenceMiningImageServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if (request.getParameter("log") != null) 
		{
			response.setContentType("text/html");
//			
			String pathToWeb = getServletContext().getRealPath(File.separator);
			String fileName = request.getParameter("log");
			File f = new File(fileName);
			
			if (!f.exists()) {
				f = new File(pathToWeb + File.separator + "log" + File.separator + fileName);
			}
				
			PrintWriter out = response.getWriter();
			byte[] encoded = Files.readAllBytes(f.toPath());
			String s = new String(encoded); 
			out.println(s);
			
			out.close();
		}
		else if (request.getParameter("image") != null) 
		{
			ServletContext cntx =  getServletContext();
			// Get the absolute path of the image
			String filename = cntx.getRealPath(request.getParameter("image"));
			//retrieve mimeType dynamically
			
			String mime = cntx.getMimeType(filename);
		   
			if (mime == null) 
			{
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
			
			response.setContentType("image/png");

			/*response.setContentType(mime);
		    File file = new File(filename);
		    response.setContentLength((int)file.length());

		    FileInputStream in = new FileInputStream(file);
		    OutputStream out = response.getOutputStream();

		    //Copy the contents of the file to the output stream
		    byte[] buf = new byte[1024];
		    int count = 0;
		    
		    while ((count = in.read(buf)) >= 0) 
		    {
		    	out.write(buf, 0, count);
		    }
		    out.close();
		    in.close();*/
			
			
			BufferedImage originalImage = ImageIO.read(new File(filename));
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(originalImage, "png", baos);
	        baos.flush();
	        byte[] imageInByte = baos.toByteArray();
	        String encoded = DatatypeConverter.printBase64Binary(imageInByte);
	        response.getOutputStream().print(encoded);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
