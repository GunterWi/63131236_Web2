
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class BMI
 */
public class BMI extends HttpServlet {
	public BMI() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.println("<html>");
		writer.println("<body>");
		writer.println("<form action='BMI' method='POST'>");
		writer.println("Chiều cao (cm): <input type='number' name='height'><br>");
		writer.println("Cân nặng (kg): <input type='number' name='weight'><br>");
		writer.println("<input type='submit' value='Tính BMI'>");
		writer.println("</form>");
		writer.println("</body>");
		writer.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
		double height = Double.parseDouble(request.getParameter("height")) / 100; // chuyển đổi sang mét
		double weight = Double.parseDouble(request.getParameter("weight"));
		double bmi = weight / (height * height);

		PrintWriter writer = response.getWriter();
		writer.println("<html><body>");
		writer.println("<h3>Chỉ số BMI của bạn là: " + bmi + "</h3>");
		writer.println("</body></html>");
	}

}
