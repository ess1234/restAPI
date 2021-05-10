package com.navercorp.micro;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class ParametersUtil {
		
	public HashMap<String, String> generateParameter(HttpServletRequest httpServletRequest) {
		HttpSession session = httpServletRequest.getSession();
		
		HashMap<String, String> map = new HashMap<String, String>();
		Enumeration enumeration = httpServletRequest.getParameterNames();
		
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String value = new String(httpServletRequest.getParameter(name));	
			
			try {
				System.out.println(name + "  ::  " + value + " :: " + URLDecoder.decode(value, "UTF-8")	);
				value = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			map.put(name, value);
		}

		// 로케이션 변수
		map.put("locationUrl", httpServletRequest.getRequestURL().toString());
		
		String remoteIP = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (remoteIP == null) remoteIP = httpServletRequest.getRemoteAddr();
        map.put("authorIP", remoteIP);
		
        // 세션에서 사용자정보 가져오기
     	HashMap<String, String> userInfo = new HashMap<String, String>();
     	userInfo = (HashMap<String, String>)session.getAttribute("userInfo");
     	
     	if(userInfo != null && !userInfo.isEmpty()) {
     		map.put("regId", String.valueOf(userInfo.get("user_seq")));
    		map.put("regNm", userInfo.get("user_sns_name"));
    		map.put("udtId", String.valueOf(userInfo.get("user_seq")));
    		map.put("auth", String.valueOf(userInfo.get("user_auth")));
     	}
		
		// 페이지 변수 세팅		
		String currentPage = map.containsKey("currentPage") ? map.get("currentPage") : "1"; 
		String viewRows = map.containsKey("viewRows") ? map.get("viewRows") : "100";
		String totalCnt = map.containsKey("totalCnt") ? map.get("totalCnt") : "100";
				
		map.put("startNum", (Integer.parseInt(currentPage) - 1) * Integer.parseInt(viewRows) + "");
		map.put("endNum", viewRows);
		map.put("currentPage", currentPage);
		map.put("viewRows", viewRows);
		map.put("totalCnt", totalCnt);
		return map;
	}
	
	public boolean isMobile(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		boolean mobile1 = userAgent.matches(".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
		boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
		if (mobile1 || mobile2) {
			return true;
		}
		return false;
	}
	
	public String getDayOfWeek(){
		Calendar cal = Calendar.getInstance() ;
		return cal.get(Calendar.DAY_OF_WEEK)+"";
	}

}
