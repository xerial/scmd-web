package lab.cb.scmd.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.web.log.ErrorLog;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class ErrorCatchFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response= (HttpServletResponse)res;
		Exception ex  = null;
		//	�T�[�u���b�g����̗�O�������󂯎��(OutofMemory�Ȃ�)
		try{
			chain.doFilter(request,response);
		} catch(ServletException e) {
			ErrorLog.insert(""+e.getMessage(),"ErrorCatchFilter���󂯎�����d�v�ȃG���[",e);
			ex = e;
			e.printStackTrace();
//			throw e;
		} finally {
			//	�G���[���L���b�`�����ꍇ��500�Ԃ�Internal Server Error���o��
			if(ex != null) {
				if(ex instanceof ServletException) {
//					StackTraceElement[] stes = ex.getStackTrace();
//					for(StackTraceElement ste : stes) {
//						System.out.println(ste.getFileName());
//					}

					response.setStatus(500);
					request.setAttribute("error",ex.getLocalizedMessage());
					request.getRequestDispatcher("500.jsp").include(req,res);
				}
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
}


//	-------------------------
//	$log: $
//	-------------------------