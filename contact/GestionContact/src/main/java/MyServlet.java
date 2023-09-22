import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name="test", value="/test")
public class MyServlet extends HttpServlet {

    private static Connection conn;
    public static void openConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/contact","root","");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        MyServlet.openConnection();
        this.afficherContact(resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
    public static void afficherContact(ServletResponse resp) {
        try {
            String query = "SELECT id,name, email, tel FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            PrintWriter writer = resp.getWriter();
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\">");
            writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            writer.println("<title>Contact Table</title>");
            writer.println("<style>");
            writer.println("table {");
            writer.println("    width: 50%;");
            writer.println("    border-collapse: collapse;");
            writer.println("    margin: 20px 0;");
            writer.println("}");
            writer.println("th, td {");
            writer.println("    border: 1px solid #ccc;");
            writer.println("    padding: 10px;");
            writer.println("    text-align: left;");
            writer.println("}");
            writer.println("th {");
            writer.println("    background-color: #f0f0f0;");
            writer.println("}");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<h2>Contact List</h2>");
            writer.println("<table>");
            writer.println("    <thead>");
            writer.println("        <tr>");
            writer.println("            <th>Name</th>");
            writer.println("            <th>Email</th>");
            writer.println("            <th>Telephone</th>");
            writer.println("            <th>Action</th>");
            writer.println("        </tr>");
            writer.println("    </thead>");
            writer.println("    <tbody>");
            while (rs.next()) {
                writer.println("        <tr>");
                writer.println("            <td>"+rs.getString("name")+"</td>");
                writer.println("            <td>"+rs.getString("email")+"</td>");
                writer.println("            <td>"+rs.getString("tel")+"</td>");
                writer.write("<td><button><a href=\"delete?num="+rs.getString("id")+"\"> Supprimer </a></button> <button><a href=\"updateContact.jsp?num="+rs.getString("id")+"\"> Modifier</a></button>  </td>");
                writer.println("        </tr>");
            }
            writer.println("    </tbody>");
            writer.println("</table>");
            writer.println("</body>");
            writer.println("</html>");

            writer.write("<button><a href=\"addContact.jsp\"> Ajouter un Contact</a></button>");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
