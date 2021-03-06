package kh.spring.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kh.spring.dto.HomeDTO;
import kh.spring.dto.HomePicDTO;
import kh.spring.dto.LikeyDTO;
import kh.spring.dto.LikeyListDTO;
import kh.spring.interfaces.HomeService;
import kh.spring.interfaces.LikeyService;

@Controller
public class HomeMainController {
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private LikeyService likeyService;
	
	@RequestMapping("/homeMain.do")
	public ModelAndView homeMain(HttpServletRequest req,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<HomeDTO> homeList = homeService.getAllHomeDataMain();
		List<HomeDTO> markerList = homeService.getAllHomeDataMain();
		List<HomeDTO> getParis = homeService.getParis();
		List<HomeDTO> getNewyork = homeService.getNewyork();
		List<HomeDTO> getRome = homeService.getRome();
		List<HomeDTO> getLondon = homeService.getLondon();
		List<HomeDTO> getPraha = homeService.getPraha();
		List<HomeDTO> getMadrid = homeService.getMadrid();
		List<HomePicDTO> homePic = homeService.getHomePic();
		
		//하트 버튼
		String member_email = null;
		List<LikeyDTO> likeyList = null;
		List<LikeyListDTO> likeyListLikey = null;
		List<LikeyDTO> likey = null;
		
		if(req.getSession().getAttribute("login_email") != null) {
			member_email = req.getSession().getAttribute("login_email").toString();
			likeyList = likeyService.getLikeyData(member_email);
			likeyListLikey = likeyService.getAlldata(member_email);
			likey = likeyService.getLikeyData(member_email);
		}
		
		session.setAttribute("homeType", "0");
		session.setAttribute("people", 0);
		session.setAttribute("startDate", "0");
		session.setAttribute("endDate", "0");
		List dates = new ArrayList<>();
		dates.add("0");
		session.setAttribute("dates",dates);
		session.setAttribute("dateIsChecked", "0");
		session.setAttribute("minMoney", 0);
		session.setAttribute("maxMoney", 1001000);
		session.setAttribute("whole", "");
		session.setAttribute("one",  "");
		session.setAttribute("many",  "");
		mav.addObject("homeList", homeList);
		mav.addObject("pic", homePic);
		mav.addObject("likeyList", likeyList);
		mav.addObject("getParis", getParis);
		mav.addObject("getNewyork", getNewyork);
		mav.addObject("getRome", getRome);
		mav.addObject("getLondon", getLondon);
		mav.addObject("getPraha", getPraha);
		mav.addObject("getMadrid", getMadrid);
		mav.addObject("markerList", markerList);
		mav.addObject("likeyListLikey", likeyListLikey);
		mav.addObject("likey", likey);
		mav.setViewName("home_main");
		return mav;
	}
	
	
	@RequestMapping("/search.do")
	public ModelAndView search(HttpServletRequest request, HttpSession session, String homeType, int people, String lat, String lng, String startDate, String endDate) throws Exception  {
		session.setAttribute("homeType", homeType);
		session.setAttribute("people", people);
		session.setAttribute("minMoney", 0);
		session.setAttribute("maxMoney", 1001000);
		session.setAttribute("startDate", startDate);
		session.setAttribute("endDate", endDate);
		session.setAttribute("whole", "");
		session.setAttribute("one",  "");
		session.setAttribute("many",  "");
		System.out.println("==============================================");
		System.out.println("homeType : "+homeType);
		System.out.println("people : "+people);
		System.out.println("startDate : "+startDate);
		System.out.println("endDate : "+endDate);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("mapOn", "mapOn");
		mav.addObject("lat", lat);
		mav.addObject("lng", lng);
		
		List homeTypeList = new ArrayList<>();
		homeTypeList.add(homeType);
		System.out.println(homeTypeList);
		
		String homeTypeIsChecked = "0";
		session.setAttribute("homeTypeList", homeTypeList);
		
		if(!homeType.equals("0")) {
//			집 유형을 선택했을 때
			homeTypeIsChecked = "1";
		}
		
		session.setAttribute("homeTypeIsChecked", homeTypeIsChecked);
		
		List dates = new ArrayList<>();
		String dateIsChecked = "0";
		
		if(!startDate.equals("0")&&!endDate.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
	        Date FirstDate = null;
	        Date SecondDate = null;
		      try {
		         FirstDate = format.parse(startDate);
		         SecondDate = format.parse(endDate);
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		      
		    //두 날짜 사이의 날짜 구하기
		      StringBuilder sb = new StringBuilder();

		      Date currentDate = FirstDate;
		      while (currentDate.compareTo(SecondDate) <= 0) {
		         dates.add(format.format(currentDate));
		         Calendar c = Calendar.getInstance();
		         c.setTime(currentDate);
		         c.add(Calendar.DAY_OF_MONTH, 1);
		         currentDate = c.getTime();
		      }
			
		    System.out.println(dates);
		    session.setAttribute("dates", dates);
		    dateIsChecked = "1";
		}
		
		session.setAttribute("dateIsChecked", dateIsChecked);
		
		System.out.println("집 체크됐니? "+(String)session.getAttribute("homeTypeIsChecked"));
		System.out.println("날짜 체크됐니? "+(String)session.getAttribute("dateIsChecked"));
		System.out.println("startDate 세션값 들어감?? "+(String)session.getAttribute("startDate"));
		
		List<HomePicDTO> homePic = homeService.getHomePic();
		
		//////////////////////////////////////////////////////
		Double currentLat = Double.parseDouble(lat);
		Double currentLng = Double.parseDouble(lng);
		Double swLat = currentLat-0.024;
		Double neLat = currentLat+0.024;
		Double swLng = currentLng-0.27396753;
		Double neLng = currentLng+0.27396753;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("homeType", (String) session.getAttribute("homeType"));
		param.put("people", session.getAttribute("people"));
		param.put("dates", (List) session.getAttribute("dates"));
		param.put("dateIsChecked", (String) session.getAttribute("dateIsChecked"));
		param.put("minMoney", (int) session.getAttribute("minMoney"));
		param.put("maxMoney", (int) session.getAttribute("maxMoney"));
		
		param.put("swLat", swLat);
		param.put("neLat", neLat);
		param.put("swLng", swLng);
		param.put("neLng", neLng);
		
		List<HomeDTO> homeList = homeService.getHomeOnMap(param);
		List<HomeDTO> markerList = homeService.getAllHomeDataMain();
		
		//////////////////////////////////////////////////
		
		mav.addObject("homeList", homeList);
		mav.addObject("markerList", markerList);
		mav.addObject("pic", homePic);
		mav.setViewName("home_main");
		return mav;
	}
	
	@RequestMapping("/mapMove.do")
	public void mapMove(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		
		Double swLat = Double.parseDouble(request.getParameter("swLat"));
		Double neLat = Double.parseDouble(request.getParameter("neLat"));
		Double swLng = Double.parseDouble(request.getParameter("swLng"));
		Double neLng = Double.parseDouble(request.getParameter("neLng"));
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("homeType", (String) session.getAttribute("homeType"));
		param.put("people", session.getAttribute("people"));
		param.put("dates", (List) session.getAttribute("dates"));
		param.put("dateIsChecked", (String) session.getAttribute("dateIsChecked"));
		param.put("minMoney", (int) session.getAttribute("minMoney"));
		param.put("maxMoney", (int) session.getAttribute("maxMoney"));
		
		param.put("swLat", swLat);
		param.put("neLat", neLat);
		param.put("swLng", swLng);
		param.put("neLng", neLng);
		
		List<HomeDTO> homeList = homeService.getHomeOnMap(param);
		List<HomePicDTO> homePic = homeService.getHomePic();
		
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		
		Map<String, Object> homeMapChange = new HashMap<String, Object>();
	      
		homeMapChange.put("home", homeList);
		homeMapChange.put("pic", homePic);
		
		new Gson().toJson(homeMapChange ,response.getWriter());
		
	}
	
	@RequestMapping("/modalPeople.do")
	public ModelAndView modalPeopleChange(HttpSession session, HttpServletRequest request, int modalPeople) {
		session.setAttribute("people", modalPeople);
		List dates = (List) session.getAttribute("dates");
		
		String homeType = (String) session.getAttribute("homeType");
		List homeTypeList = (List) session.getAttribute("homeTypeList");
		System.out.println("사람수 입력후 homeType: "+homeType);
		System.out.println("사람수 입력후 homeType세션값 :"+(String) session.getAttribute("homeType"));
		System.out.println("사람수 입력후 homeTypeList: "+homeTypeList);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("homeType", (String) session.getAttribute("homeType"));
		param.put("homeTypeList", (List) session.getAttribute("homeTypeList"));
		param.put("homeTypeIsChecked", (String) session.getAttribute("homeTypeIsChecked"));
		param.put("people", session.getAttribute("people"));
		param.put("dates", (List) session.getAttribute("dates"));
		param.put("dateIsChecked", (String) session.getAttribute("dateIsChecked"));
		param.put("minMoney", (int) session.getAttribute("minMoney"));
		param.put("maxMoney", (int) session.getAttribute("maxMoney"));
		
		System.out.println("모달 피플 바꾼뒤"+session.getAttribute("people"));
		
		List<HomeDTO> homeList = homeService.modalHomeData(param);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("homeList", homeList);
		mav.addObject("mapOn", "mapOn");
		mav.setViewName("home_main");
		return mav;
		
	}
	
	@RequestMapping("/modalHomeType.do")
	public ModelAndView modalHomeTypeChange(HttpSession session, HttpServletRequest request, String whole, String one, String many) {
		
		session.setAttribute("homeTypeIsChecked", "0");
		
		if(whole!=null||one!=null||many!=null) {
			session.setAttribute("homeTypeIsChecked", "1");
		}
		
		List homeTypeList = new ArrayList<>();

		if(whole!=null) {
			homeTypeList.add(whole);
			session.setAttribute("whole", whole);
		} else {
			session.setAttribute("whole", "");
		}
		
		if(one!=null) {
			homeTypeList.add(one);
			session.setAttribute("one", one);
		} else {
			session.setAttribute("one", "");
		}
		
		if(many!=null) {
			homeTypeList.add(many);
			session.setAttribute("many", many);
		} else {
			session.setAttribute("many", "");
		}
		
		System.out.println("whole "+whole);
		System.out.println("one "+one);
		System.out.println("many "+many);
		System.out.println("homeTypeList " + homeTypeList);
		
		
		session.setAttribute("homeType", "숙소 유형 ·"+ homeTypeList.size());
		session.setAttribute("homeTypeList", homeTypeList);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("homeTypeList", (List) session.getAttribute("homeTypeList"));
		param.put("homeTypeIsChecked", (String) session.getAttribute("homeTypeIsChecked"));
		param.put("people", session.getAttribute("people"));
		param.put("dates", (List) session.getAttribute("dates"));
		param.put("dateIsChecked", (String) session.getAttribute("dateIsChecked"));
		param.put("minMoney", (int) session.getAttribute("minMoney"));
		param.put("maxMoney", (int) session.getAttribute("maxMoney"));
		
		List<HomeDTO> homeList = homeService.modalHomeData(param);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("homeList", homeList);
		mav.addObject("mapOn", "mapOn");
		mav.setViewName("home_main");
		return mav;
		
	}
	
	@RequestMapping("/modalDate.do")
	public ModelAndView modalDateChange(HttpSession session, HttpServletRequest request, String startDate, String endDate) {
		String dateIsChecked = "0";
		
		if(startDate==null || endDate==null) {
			startDate="0";
			endDate ="0";
			dateIsChecked = "0";
			session.setAttribute("startDate", startDate);
			session.setAttribute("endDate", endDate);
		}
		
		List dates = new ArrayList<>();
		session.setAttribute("startDate", startDate);
		session.setAttribute("endDate", endDate);
		
		
		if(!startDate.equals("0")&&!endDate.equals("0")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
	        Date FirstDate = null;
	        Date SecondDate = null;
		      try {
		         FirstDate = format.parse(startDate);
		         SecondDate = format.parse(endDate);
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		      
		    //두 날짜 사이의 날짜 구하기
		      StringBuilder sb = new StringBuilder();

		      Date currentDate = FirstDate;
		      while (currentDate.compareTo(SecondDate) <= 0) {
		         dates.add(format.format(currentDate));
		         Calendar c = Calendar.getInstance();
		         c.setTime(currentDate);
		         c.add(Calendar.DAY_OF_MONTH, 1);
		         currentDate = c.getTime();
		      }
			
		    System.out.println(dates);
		    session.setAttribute("dates", dates);
		    dateIsChecked = "1";
		}
		
		System.out.println("===============================================");
		System.out.println("날짜바꿔보았스 : "+(List) session.getAttribute("dates"));
		
		session.setAttribute("homeType", "숙소 유형 ·"+ ((List) session.getAttribute("homeTypeList")).size());
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("homeType", session.getAttribute("homeType"));
		param.put("homeTypeList", (List) session.getAttribute("homeTypeList"));
		param.put("homeTypeIsChecked", (String) session.getAttribute("homeTypeIsChecked"));
		param.put("people", session.getAttribute("people"));
		param.put("dates", (List) session.getAttribute("dates"));
		param.put("dateIsChecked", (String) session.getAttribute("dateIsChecked"));
		param.put("minMoney", (int) session.getAttribute("minMoney"));
		param.put("maxMoney", (int) session.getAttribute("maxMoney"));
		
		List<HomeDTO> homeList = homeService.modalHomeData(param);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("homeList", homeList);
		mav.addObject("mapOn", "mapOn");
		mav.setViewName("home_main");
		return mav;
		
	}
	
	@RequestMapping("/headerSearch.do")
	public ModelAndView headerSearch(HttpSession session, HttpServletRequest request) {
		String lat = request.getParameter("lat");
		String lng = request.getParameter("lng");
		System.out.println(lat);
		System.out.println(lng);
		
		session.setAttribute("homeType", "0");
		session.setAttribute("people", 0);
		session.setAttribute("minMoney", 0);
		session.setAttribute("maxMoney", 1001000);
		session.setAttribute("startDate", "0");
		session.setAttribute("endDate", "0");
		session.setAttribute("whole", "");
		session.setAttribute("one",  "");
		session.setAttribute("many",  "");

		ModelAndView mav = new ModelAndView();
		
		mav.addObject("mapOn", "mapOn");
		mav.addObject("lat", lat);
		mav.addObject("lng", lng);

		List homeTypeList = new ArrayList<>();
		homeTypeList.add("0");
		String homeTypeIsChecked = "0";
		session.setAttribute("homeTypeList", homeTypeList);
		session.setAttribute("homeTypeIsChecked", homeTypeIsChecked);

		List dates = new ArrayList<>();
		dates.add("0");
		String dateIsChecked = "0";
		session.setAttribute("dates", dates);
		session.setAttribute("dateIsChecked", dateIsChecked);

		

		Double currentLat = Double.parseDouble(lat);
		Double currentLng = Double.parseDouble(lng);
		Double swLat = currentLat-0.024;
		Double neLat = currentLat+0.024;
		Double swLng = currentLng-0.27396753;
		Double neLng = currentLng+0.27396753;

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("homeType", (String) session.getAttribute("homeType"));
		param.put("people", session.getAttribute("people"));
		param.put("dates", (List) session.getAttribute("dates"));
		param.put("dateIsChecked", (String) session.getAttribute("dateIsChecked"));
		param.put("minMoney", (int) session.getAttribute("minMoney"));
		param.put("maxMoney", (int) session.getAttribute("maxMoney"));
		
		param.put("swLat", swLat);
		param.put("neLat", neLat);
		param.put("swLng", swLng);
		param.put("neLng", neLng);
		
		List<HomeDTO> homeList = homeService.getHomeOnMap(param);
		List<HomeDTO> markerList = homeService.getHomeOnMap(param);
		List<HomePicDTO> homePic = homeService.getHomePic();

		mav.addObject("homeList", homeList);
		mav.addObject("markerList", markerList);
		mav.addObject("pic", homePic);
		mav.setViewName("home_main");
		return mav;
	}
	
	
}
