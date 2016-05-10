package Servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Classes.Review;
import Indexing.BuildIndex;
import Indexing.MyIndexReader;
import Indexing.OriginReviewReader;
import Search.RetrievalModel;

/**
 * Servlet implementation class QueryServlet
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String query = request.getParameter("query");
		BuildIndex process = new BuildIndex(); 
		System.out.println(query);
		MyIndexReader ixreader = new MyIndexReader();
		RetrievalModel model = new RetrievalModel(ixreader);

		OriginReviewReader oir = new OriginReviewReader();
		
		List<Review> rreviews = null;
		try {
			rreviews = model.retrieveReview(process.queryProcess(query), 1000);
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		int size = rreviews.size()>20?20:rreviews.size();
		String[] rbusinesses=model.retrieveBusiness(rreviews, query, size);
		HttpSession session = request.getSession();
		Map<String, List<String>> result = new HashMap<>();

		if (rbusinesses != null) {
			for (int i=0; i<rbusinesses.length;i++) 
				result.put(rbusinesses[i], model.getReviewidList(rbusinesses[i]));
		}
		
		session.setAttribute("business", result);
		session.setAttribute("q", query);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		/*for(String r: model.getReviewidList(rbusinesses[i])){
			System.out.println(r+" "+oir.getReviewContent(Integer.parseInt(r))+"\n");
		}*/
		
	}

}
