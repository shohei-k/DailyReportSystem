package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportReactionServlet
 */
@WebServlet("/reports/reaction")
public class ReportReactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportReactionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //DBに接続
        EntityManager em=DBUtil.createEntityManager();

        Report r=em.find(Report.class, Integer.parseInt(request.getParameter("reaction")));
        Like l=new Like();

        //リアクションに値をセット
        int reaction=r.getReaction()+1;
        r.setReaction(reaction);

        l.setReport(r);
        l.setEmployee((Employee)request.getSession().getAttribute("login_employee"));

        Timestamp currentTime=new Timestamp(System.currentTimeMillis());

        l.setCreated_at(currentTime);
        l.setUpdated_at(currentTime);

        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();

        request.getSession().setAttribute("flush", "いいねしました！");

        response.sendRedirect(request.getContextPath()+"/reports/index");

    }
}
