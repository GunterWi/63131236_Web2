
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class AboutMe
 */
public class AboutMe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AboutMe() {
		super();
	}

    //http://localhost:8080/DoGetAndDoPost/AboutMe?Name=Nguyen Quoc Thai&Lop=63CNTTCLC&MSSV=63131236
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>About Me</title>");
		out.println("<style>");
		out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; }");
		out.println(
				".container { width: 50%; margin: auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }");
		out.println("h1 { color: #333; }");
		out.println("p { color: #666; }");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div class='container'>");
		out.println("<h1>About Me</h1>");

		String name = request.getParameter("Name");
		String lop = request.getParameter("Lop");
		String mssv = request.getParameter("MSSV");

		if (name != null && lop != null) {
			out.println("<p><strong>Tên:</strong> " + name + "</p>");
			out.println("<p><strong>Lớp:</strong> " + lop + "</p>");
			out.println("<p><strong>MSSV:</strong> " + mssv + "</p>");
		} else {
			out.println("<p>Chưa cung cấp thông tin cho GET Request</p>");
		}

		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
