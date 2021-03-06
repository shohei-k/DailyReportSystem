package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //DBに接続
        EntityManager em=DBUtil.createEntityManager();

        Report r=em.find(Report.class,Integer.parseInt(request.getParameter("id")));
        Employee e=(Employee)request.getSession().getAttribute("login_employee");

        //reportクラスのクエリを選択
        long likesCount=(long)em.createNamedQuery("getMyLikes",Long.class)
                .setParameter("report", r)
                .setParameter("employee", e)
                .getSingleResult();

        em.close();

        //リクエストスコープにセット
        request.setAttribute("report", r);
        request.setAttribute("likesCount", likesCount);
        request.setAttribute("_token", request.getSession().getId());

        RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

}
