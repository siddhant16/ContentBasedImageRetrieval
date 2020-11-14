
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class UploadImage
 */
@WebServlet("/UploadImageTexture")
public class UploadImageTexture extends HttpServlet {
	//private static final long serialVersionUID = 1L;
	//private static final int MEMORY_THRESHOLD = 0;
	//private static final long MAX_FILE_SIZE = 0;
	//private static final long MAX_REQUEST_SIZE = 0;
	private static final String UPLOAD_DIRECTORY = "imageTexture";

	/**
	 * Default constructor.
	 */
	public UploadImageTexture() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println(ServletFileUpload.isMultipartContent(request)		);			//returns true if multipart else false.
		DiskFileItemFactory factory = new DiskFileItemFactory();		//create temporary directory for files.
		// sets memory threshold - beyond which files are stored in disk
		// factory.setSizeThreshold(MEMORY_THRESHOLD);
		// sets temporary location to store files
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);
		// upload.setFileSizeMax(MAX_FILE_SIZE);

		// sets maximum size of request (include file + form data)
		// upload.setSizeMax(MAX_REQUEST_SIZE);

		// constructs the directory path to store upload file
		// this path is relative to application's directory
		// --ADD database connection
		
		String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        
		System.out.println("Path is"+uploadPath);
		
		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		try {
			// parses the request's content to extract file data
			@SuppressWarnings("unchecked")

			List<FileItem> formItems = upload.parseRequest(request);

			if (formItems != null && formItems.size() > 0) {
				// iterates over form's fields
				for (FileItem item : formItems) {
					// processes only fields that are not form fields
					if (!item.isFormField()) {
						String fileName = new File(item.getName()).getName();
						String filePath = uploadPath + File.separator + fileName;
						File storeFile = new File(filePath);

						// saves the file on disk
						item.write(storeFile);
						//request.setAttribute("message", "Upload has been done successfully!");

						response.setContentType("application/xhtml+xml");
						String docType =
							      "<!doctype html public \"-//w3c//dtd html 4.0 " +
							      "transitional//en\">\n";

						PrintWriter out = response.getWriter();
						System.out.println( getServletContext().getRealPath(""));
						String htmBody = createBody(textureBased.processImage(filePath, getServletContext().getRealPath("")));
						System.out.print(htmBody);
						out.println(docType+htmBody);
						/*request.setAttribute("message", message); // This will be available as ${message}
				        request.getRequestDispatcher("/WEB-INF/page.jsp").forward(request, response);*/
					}
				}
			}
		}catch (Exception ex) {
			request.setAttribute("message", "There was an error: " + ex.getMessage());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		 System.out.println("Inside Post");
		doGet(request, response);
	}

	protected String createBody(List<String> imglist) {

		
		
		
		
		
		
		String DivTmplt =		
				 "                <div class=\"col-md-3 col-sm-6\">"
				+ "                    <div class=\"single-shop-product\">"
				+ "                        <div class=\"product-upper\">"
				+ "                            <img src=\"textureData/__imageName\" alt=\"\"  />" 
				+ "                        </div>" + "<h2>__ImageDispName</h2>"
				+ "                    </div>" + "</div> ";

		String compDiv = "";
		String imgName="";
        //int n=imglist.size();
		for (int n=0;n<imglist.size();n++) {
			imgName=imglist.get(n);
			String tmpDiv = DivTmplt;
			tmpDiv = tmpDiv.replaceAll("__imageName", imgName);
			
			tmpDiv = tmpDiv.replaceAll("__ImageDispName", imgName.split("\\.")[0].toUpperCase());
			compDiv = compDiv + tmpDiv;
		}
		
		String found="<p>"+imglist.size()+"images found";
		
		

		String tmp = "<!DOCTYPE html>" + "<html lang=\"en\">" + "  <head>" + "    <meta charset=\"utf-8\">"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
				+ "    <title>T-Shirt Baazar</title>" + "    <!-- Google Fonts -->"
				+ "    <link href='http://fonts.googleapis.com/css?family=Titillium+Web:400,200,300,700,600' rel='stylesheet' type='text/css'>"
				+ "    <link href='http://fonts.googleapis.com/css?family=Roboto+Condensed:400,700,300' rel='stylesheet' type='text/css'>"
				+ "    <link href='http://fonts.googleapis.com/css?family=Raleway:400,100' rel='stylesheet' type='text/css'>"
				+ "	<link href='css/titalium.css' rel='stylesheet' type='text/css'>"
				+ "	<link href='css/roboto.css' rel='stylesheet' type='text/css'>"
				+ "	<link href='css/raleway.css' rel='stylesheet' type='text/css'>" + "    <!-- Bootstrap -->"
				+ "    <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">"
				+ "    <link rel=\"stylesheet\" href=\"css/bootstrap.css\">"
				+ "    <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css\">"
				+ "    <link rel=\"stylesheet\" href=\"font-awesome-4.6.3/css/font-awesome.css\">"
				+ "    <link rel=\"stylesheet\" href=\"css/owl.carousel.css\">"
				+ "    <link rel=\"stylesheet\" href=\"style.css\">"
				+ "    <link rel=\"stylesheet\" href=\"css/responsive.css\">" + "  </head>" + "  <body>"
				+ "    </div--> <!-- End header area -->" + "    <div class=\"site-branding-area\">"
				+ "        <div class=\"container\">" + "            <div class=\"row\">"
				+ "                <div class=\"col-sm-6\">" + "                    <div class=\"logo\">"
				+ "                        <h1><a href=\"index.html\">T-Shirt<span>Baazar</span></a></h1>"
				+ "                    </div>" + "                </div>" + "            </div>" + "        </div>"
				+ "    </div> <!-- End site branding area -->" + "    <div class=\"mainmenu-area\">"
				+ "        <div class=\"container\">" + "            <div class=\"row\">"
				+ "                <div class=\"navbar-header\">"
				+ "                    <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">"
				+ "                        <span class=\"sr-only\">Toggle navigation</span>"
				+ "                        <span class=\"icon-bar\"></span>"
				+ "                        <span class=\"icon-bar\"></span>"
				+ "                        <span class=\"icon-bar\"></span>" + "                    </button>"
				+ "                </div> " + "                <div class=\"navbar-collapse collapse\">"
				+ "                    <ul class=\"nav navbar-nav\">"
				+ "                        <li><a href=\"index.html\">Home</a></li>"
				+ "                        <li><a href=\"shop.html\">Shop page</a></li>"
				+ "                        <li><a href=\"dragUpload.html\">TEMPLATE</a></li>"
				+ "                        <li class=\"active\"><a href=\"gabor.html\">TEXTURE</a></li>"
				+ "                        <li><a href=\"#\">Others</a></li>"
				+ "                        <li><a href=\"#\">Contact</a></li>" + "                    </ul>"
				+ "                </div>  " + "            </div>" + "        </div>" + "    </div> " 
				+  " <div class=\"single-product-area\"> <div class=\"container\">  <div class=\"row\">" 
			    + compDiv + "</div> " + found+
			    
				/*
				 * "    <div class=\"single-product-area\">"+
				 * "        <div class=\"zigzag-bottom\"></div>"+
				 * "        <div class=\"container\">"+
				 * "            <div class=\"row\">"+
				 * "                <div class=\"col-md-3 col-sm-6\">"+
				 * "                    <div class=\"single-shop-product\">"+
				 * "                        <div class=\"product-upper\">"+
				 * "                            <img src=\"img/wrogn1.jpg\" alt=\"\">"
				 * + "                        </div>"+
				 * "                        <h2><a href=\"\">Wrogn Printed T-Shirt</a></h2>"
				 * + "                    </div>"+ "                </div>"+
				 * "            </div>"+
				 */
				"            <div class=\"row\">" + "                <div class=\"col-md-12\">"
				+ "                    <div class=\"product-pagination text-center\">" + "                        <nav>"
				+ "                          <ul class=\"pagination\">" + "                            <li>"
				+ "                              <a href=\"#\" aria-label=\"Previous\">"
				+ "                                <span aria-hidden=\"true\">&laquo;</span>"
				+ "                              </a>" + "                            </li>"
				+ "                            <li><a href=\"#\">1</a></li>"
				+ "                            <li><a href=\"#\">2</a></li>"
				+ "                            <li><a href=\"#\">3</a></li>"
				+ "                            <li><a href=\"#\">4</a></li>"
				+ "                            <li><a href=\"#\">5</a></li>" + "                            <li>"
				+ "                              <a href=\"#\" aria-label=\"Next\">"
				+ "                                <span aria-hidden=\"true\">&raquo;</span>"
				+ "                              </a>" + "                            </li>"
				+ "                          </ul>" + "                        </nav> " + "                    </div>"
				+ "                </div>" + "            </div>" + "        </div>" + "    </div>"
				+ "    <div class=\"footer-top-area\">" + "        <div class=\"zigzag-bottom\"></div>"
				+ "        <div class=\"container\">" + "            <div class=\"row\">"
				+ "                <div class=\"col-md-3 col-sm-6\">"
				+ "                    <div class=\"footer-about-us\">"
				+ "                        <h2>T-Shirt<span>Baazar</span></h2>"
				+ "                        <p>No more wasting time for searching the right product. T-Shirt Baazar provides the facility for direct search by t-shirt picture.</p>"
				+ "                        <div class=\"footer-social\">"
				+ "                            <a href=\"#\" target=\"_blank\"><i class=\"fa fa-facebook\"></i></a>"
				+ "                            <a href=\"#\" target=\"_blank\"><i class=\"fa fa-twitter\"></i></a>"
				+ "                            <a href=\"#\" target=\"_blank\"><i class=\"fa fa-youtube\"></i></a>"
				+ "                            <a href=\"#\" target=\"_blank\"><i class=\"fa fa-linkedin\"></i></a>"
				+ "                        </div>" + "                    </div>" + "                </div>"
				+ "                <div class=\"col-md-3 col-sm-6\">"
				+ "                    <div class=\"footer-menu\">"
				+ "                        <h2 class=\"footer-wid-title\">User Navigation </h2>"
				+ "                        <ul>" + "                            <li><a href=\"\">My account</a></li>"
				+ "                            <li><a href=\"\">Wishlist</a></li>"
				+ "                            <li><a href=\"\">Contact</a></li>" + "                        </ul>  "
				+ "                    </div>" + "                </div>"
				+ "                <div class=\"col-md-3 col-sm-6\">"
				+ "                    <div class=\"footer-menu\">"
				+ "                        <h2 class=\"footer-wid-title\">Categories</h2>"
				+ "                        <ul>" + "                            <li><a href=\"\">Casuals</a></li>"
				+ "                            <li><a href=\"\">Formals</a></li>"
				+ "                            <li><a href=\"\">Party wear</a></li>" + "                        </ul>"
				+ "                    </div>" + "                </div>"
				+ "                <div class=\"col-md-3 col-sm-6\">"
				+ "                    <div class=\"footer-newsletter\">"
				+ "                        <h2 class=\"footer-wid-title\">Newsletter</h2>"
				+ "                        <p>Sign up to our newsletter and get exclusive deals you wont find anywhere else straight to your inbox!</p>"
				+ "                        <div class=\"newsletter-form\">"
				+ "                            <input type=\"email\" placeholder=\"Type your email\">"
				+ "                            <input type=\"submit\" value=\"Subscribe\">"
				+ "                        </div>" + "                    </div>" + "                </div>"
				+ "            </div>" + "        </div>" + "    </div>" + "    <div class=\"footer-bottom-area\">"
				+ "        <div class=\"container\">" + "            <div class=\"row\">"
				+ "                <div class=\"col-md-8\">" + "                    <div class=\"copyright\">"
				+ "                        <p>&copy; 2015 T-Shirt Baazar. All Rights Reserved. </p>"
				+ "                    </div>" + "                </div>" + "                <div class=\"col-md-4\">"
				+ "                    <div class=\"footer-card-icon\">"
				+ "                        <i class=\"fa fa-cc-discover\"></i>"
				+ "                        <i class=\"fa fa-cc-mastercard\"></i>"
				+ "                        <i class=\"fa fa-cc-paypal\"></i>"
				+ "                        <i class=\"fa fa-cc-visa\"></i>" + "                    </div>"
				+ "                </div>" + "            </div>" + "        </div>" + "    </div>"
				+ "    <!-- Latest jQuery form server -->"
				+ "    <script src=\"https://code.jquery.com/jquery.min.js\"></script>"
				+ "    <script src=\"js/jquerymin.js\"></script>" + "	<!-- Bootstrap JS form CDN -->"
				+ "    <script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js\"></script>"
				+ "    <script src=\"js/bootstrap.js\"></script>" + "    <!-- jQuery sticky menu -->"
				+ "    <script src=\"js/owl.carousel.min.js\"></script>"
				+ "    <script src=\"js/jquery.sticky.js\"></script>" + "    <!-- jQuery easing -->"
				+ "    <script src=\"js/jquery.easing.1.3.min.js\"></script>" + "    <!-- Main Script -->"
				+ "    <script src=\"js/main.js\"></script>" + "  </body>" + "</html>";

		return tmp;
	}
}
