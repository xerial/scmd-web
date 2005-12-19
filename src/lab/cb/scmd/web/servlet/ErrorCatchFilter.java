package lab.cb.scmd.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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

		//	�T�[�u���b�g����̗�O�������󂯎��(OutofMemory�Ȃ�)
		try{
			chain.doFilter(request,response);
		} catch(Exception e) {
			ErrorLog.insert(e.getMessage(),"ErrorCatchFilter���󂯎�����d�v�ȃG���[",e);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
}


//	-------------------------
//	$log: $
//	-------------------------