package kh.spring.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kh.spring.dto.GuestReviewDTO;
import kh.spring.dto.HomeDTO;
import kh.spring.dto.HomeDescDTO;
import kh.spring.dto.HomePicDTO;
import kh.spring.dto.MessageDTO;
import kh.spring.dto.ReservationDTO;
import kh.spring.interfaces.HomeService;

@Controller
public class HostController {

	@Autowired
	private HomeService homeService;

	@RequestMapping("/hostMain.do")
	public ModelAndView toHostMain() throws Exception {

		// 세션에서 member_email 꺼내기
		String member_email = "sksksrff@gmail.com";
		List<HomeDTO> homeList = homeService.getAllHomeData(member_email);
		HomeDTO hdto = homeService.getOldestHomeData();
		List<HomePicDTO> hplist = homeService.getHomePicData(homeList.get(0).getHome_seq());

		List<ReservationDTO> rlist = homeService.getAllReservation(member_email);

		int hpsize = hplist.size();

		System.out.println("homeList.get(0).getHome_seq()::" + homeList.get(0).getHome_seq());

		List<GuestReviewDTO> listGR = homeService.getAllGuestReview(member_email);
		int cnt = homeService.guestReviewCount(member_email);
		
		List<MessageDTO> mlist = homeService.getAllMessage(hdto.getHome_seq());

		SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy년 MM월 dd일");

		for (int i = 0; i < rlist.size(); i++) {
			Date to1 = fm1.parse(rlist.get(i).getReserv_checkin());
			String str1 = fm2.format(to1);
			rlist.get(i).setReserv_checkin(str1);

			Date to2 = fm1.parse(rlist.get(i).getReserv_checkout());
			String str2 = fm2.format(to2);
			rlist.get(i).setReserv_checkout(str2);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("mlist", mlist);
		mav.addObject("rlist", rlist);
		mav.addObject("homeList", homeList);
		mav.addObject("hdto", hdto);
		mav.addObject("listGR", listGR);
		mav.addObject("cnt", cnt);
		mav.addObject("hplist", hplist);
		mav.addObject("hpsize", hpsize);
		mav.setViewName("/host/hostMain");

		return mav;

	}

	@RequestMapping("/summary.do")
	public void toSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("/summary.do:" + 1111);
		int seq = Integer.parseInt(request.getParameter("seq"));
		System.out.println(seq);
		List<HomePicDTO> hplist = homeService.getHomePicData(seq);
		int hpsize = hplist.size();
		JSONObject json = new JSONObject();

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		HomeDTO hdto = homeService.getHomeData(seq);

		if (hdto.getHome_addr1() == null) {
			hdto.setHome_addr1("");
		}
		if (hdto.getHome_addr2() == null) {
			hdto.setHome_addr2("");
		}
		if (hdto.getHome_addr3() == null) {
			hdto.setHome_addr3("");
		}
		if (hdto.getHome_addr4() == null) {
			hdto.setHome_addr4("");
		}

		json.put("seq", hdto.getHome_seq());
		json.put("name", hdto.getHome_name());
		json.put("pic", hdto.getHome_main_pic());
		json.put("addr1", hdto.getHome_addr1());
		json.put("addr2", hdto.getHome_addr2());
		json.put("addr3", hdto.getHome_addr3());
		json.put("addr4", hdto.getHome_addr4());
		json.put("state", hdto.getHome_state());
		json.put("price", hdto.getHome_price());
		json.put("hpsize", hpsize);

		response.getWriter().print(json);
		response.getWriter().flush();
		response.getWriter().close();

	}

	@RequestMapping("/hostHomeTab.do")
	public ModelAndView toHostHomeTab(int seq) throws Exception {
		System.out.println("/hostHomeTab.do : " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		List<String> list = new ArrayList<String>();

		String[] amenities = hdto.getHome_amenities().split(",");
		String[] safety = hdto.getHome_safety().split(",");
		String[] guest_access = hdto.getHome_guest_access().split(",");

		for (int i = 0; i < amenities.length; i++) {
			list.add(amenities[i]);
		}
		for (int i = 0; i < safety.length; i++) {
			list.add(safety[i]);
		}
		for (int i = 0; i < guest_access.length; i++) {
			list.add(guest_access[i]);
		}

		for (String str : list) {
			System.out.println("split: " + str);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.addObject("list", list);
		mav.setViewName("/host/hostHomeTab");

		return mav;

	}

	@RequestMapping("/hostReserveTab.do")
	public ModelAndView toHostReserveTab(int seq) throws Exception {
		System.out.println("/hostReserveTab.do : " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		String[] rules = hdto.getHome_rules().split(",");
		String[] details = hdto.getHome_details().split(",");

		List<String> ruleList = new ArrayList<String>();
		List<String> detailsList = new ArrayList<String>();

		for (int i = 0; i < rules.length; i++) {
			ruleList.add(rules[i]);
		}

		for (int i = 0; i < details.length; i++) {
			detailsList.add(details[i]);
		}

		for (String str : ruleList) {
			System.out.println("split: " + str);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("ruleList", ruleList);
		mav.addObject("detailsList", detailsList);
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostReserveTab");

		return mav;
	}

	@RequestMapping("/hostPriceTab.do")
	public ModelAndView toHostPriceTab(int seq) throws Exception {
		System.out.println("/hostPriceTab.do : " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);
		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostPriceTab");

		return mav;
	}

	@RequestMapping("/hostReservePossibleTab.do")
	public ModelAndView tohostReservePossibleTab(int seq) throws Exception {
		System.out.println("/hostReservePossibleTab.do : " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostReservePossibleTab");

		return mav;
	}

	@RequestMapping("/hostHomePhotoModifyTab.do")
	public ModelAndView tohostHomePictureModifyTab(int seq) throws Exception {
		System.out.println("/hostHomePhotoModifyTab.do : " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);
		List<HomePicDTO> hplist = homeService.getHomePicData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.addObject("hplist", hplist);
		
		System.out.println("### : " + hplist.size() + " : " + hdto);
		
		mav.setViewName("/host/hostHomePhotoModifyTab");

		return mav;
	}

	@RequestMapping("/uploadPhoto.do")
	public void toUploadPhoto(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("/uploadPhoto.do : ");
		System.out.println(request.getParameter("seq"));

		int seq = 5;

		String realPath = request.getSession().getServletContext().getRealPath("/files/");
		System.out.println(realPath);

		File f = new File(realPath);
		if (!f.exists()) {
			f.mkdir();
		}

		File[] innerFile = f.listFiles();

		for (int i = 0; i < innerFile.length; i++) {
			String name = innerFile[i].getName();
			// System.out.println(innerFile[i].getName());
			System.out.println("name : " + name);
		}

		int maxSize = 1024 * 1024 * 100;
		String enc = "UTF-8";

		int addHomePicResult = 0;
		int addHomeResult = 0;

		MultipartRequest mr = new MultipartRequest(request, realPath, maxSize, enc, new DefaultFileRenamePolicy());
		Enumeration<String> names = mr.getFileNames();

		String filename = null;

		if (names != null) {
			String paramName = names.nextElement();
			String systemName = mr.getFilesystemName(paramName);
			filename = systemName;

			System.out.println("5 : " + seq + " : " + systemName + " : ");

			if (homeService.getHomeData(seq).getHome_main_pic() == null) {
				addHomeResult = homeService.addHomeRepresentData(systemName, seq);
			} else {
				addHomePicResult = homeService.addHomePicData(new HomePicDTO(0, seq, systemName));
			}

			System.out.println("addHomePicResult : " + addHomePicResult);
		}

		HomeDTO hdto = homeService.getHomeData(seq);
		List<HomePicDTO> hplist = homeService.getHomePicData(seq);
		System.out.println("hplist: " + hplist.size());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hplist", hplist);
		map.put("hdto", hdto);
		map.put("filename", filename);
	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		new Gson().toJson(map, response.getWriter());

	}

	@RequestMapping("/deletePhoto.do")
	public void deletePhoto(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int separate = Integer.parseInt(request.getParameter("separate"));
		System.out.println("seperate:" + separate);

		JSONObject json = new JSONObject();

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (separate == 1) {
			System.out.println("deletePhoto1: " + request.getParameter("file") + " : " + request.getParameter("seq"));
			String file = request.getParameter("file");
			int seq = Integer.parseInt(request.getParameter("seq"));

			String realPath = request.getSession().getServletContext().getRealPath("/files/");

			File f = new File(realPath);
			f.mkdir();
			File[] innerFile = f.listFiles();

			String filename = file.split("/")[1];

			System.out.println("filename : " + filename);

			int delResult = homeService.deleteHomeMainPicData(filename, seq);

			for (int i = 0; i < innerFile.length; i++) {
				System.out.println(innerFile[i].getName().equals(filename));

				if (innerFile[i].getName().equals(filename)) {
					innerFile[i].delete();
				}
			}
			json.put("result", delResult);

			response.getWriter().print(json);
			response.getWriter().flush();
			response.getWriter().close();

		} else if (separate == 2) {
			System.out.println("deletePhoto2: " + request.getParameter("file"));
			String file = request.getParameter("file");

			String realPath = request.getSession().getServletContext().getRealPath("/files/");

			File f = new File(realPath);
			f.mkdir();
			File[] innerFile = f.listFiles();

			String filename = file.split("/")[1];

			System.out.println("filename : " + filename);

			for (int i = 0; i < innerFile.length; i++) {
				System.out.println(innerFile[i].getName().equals(filename));

				if (innerFile[i].getName().equals(filename)) {
					innerFile[i].delete();
				}
			}

			int delResult = homeService.deleteHomePicData(filename);
			System.out.println("디비삭제: " + delResult);

			json.put("result", delResult);

			response.getWriter().print(json);
			response.getWriter().flush();
			response.getWriter().close();

		} else if (separate == 3) {
			System.out.println("deletePhoto3: " + request.getParameter("file"));
			System.out.println("toMainPic :" + request.getParameter("toMainPic"));

			String file = request.getParameter("file");
			String toMainPic = request.getParameter("toMainPic");
			int seq = Integer.parseInt(request.getParameter("seq"));

			if (toMainPic == null) {
				System.out.println("null");
			} else {
				String homePic = toMainPic.split("/")[1];
				// String mainPic = file.split("/")[1];
				// System.out.println("mainPic :" + mainPic);

				int delResult = homeService.deleteHomePicData(homePic);
				int upMainPicResult = homeService.addHomeRepresentData(homePic, seq);
				System.out.println("디비삭제: " + delResult);
				json.put("result", delResult);
			}

			response.getWriter().print(json);
			response.getWriter().flush();
			response.getWriter().close();
		}

	}

	@RequestMapping("/hostHomeTitleModifyTab.do")
	public ModelAndView toHostHomeTitleModifyTab(int seq) throws Exception {
		System.out.println("hostHomeTitleModifyTab: " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);
		HomeDescDTO hddto = homeService.getHomeDescData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.addObject("hddto", hddto);
		mav.setViewName("/host/hostHomeTitleModifyTab");
		return mav;
	}

	@RequestMapping("/hostHomeTitleModifyProc.do")
	public ModelAndView toHostHomeTitleModifyProc(HomeDescDTO hddto, HomeDTO hdto) throws Exception {
		System.out.println("hostHomeTitleModifyProc: ");

		hddto.setHome_Seq(hdto.getHome_seq());

		int result1 = homeService.modifyHomeDescData(hddto);
		int result2 = homeService.modifyTitleHomeData(hdto);

		System.out.println("결과 : " + result1 + " : " + result2);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.addObject("result1", result1);
		mav.addObject("result2", result2);
		mav.setViewName("/host/hostHomeTitleModifyProc");
		return mav;
	}

	@RequestMapping("/hostHomeModifyFacilityTab.do")
	public ModelAndView toHomeModifyFacilityTab(int seq) throws Exception {
		System.out.println("homeModifyFacilityTab: " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		String acc = hdto.getHome_guest_access();
		String amen = hdto.getHome_amenities();
		String safe = hdto.getHome_safety();

		String[] accarr = acc.split(",");
		String[] amenarr = amen.split(",");
		String[] safearr = safe.split(",");

		ModelAndView mav = new ModelAndView();
		mav.addObject("accarr", accarr);
		mav.addObject("amenarr", amenarr);
		mav.addObject("safearr", safearr);
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostHomeModifyFacilityTab");
		return mav;
	}

	@RequestMapping("/hostHomeModifyFacilityProc.do")
	public ModelAndView toHostHomeModifyFacilityProc(int seq, HttpServletRequest request) throws Exception {
		System.out.println("homeModifyFacilityTab: " + seq);

		String[] fac = request.getParameterValues("fac");
		String[] safe = request.getParameterValues("secure");
		String[] guest = request.getParameterValues("acc");

		String s_facility = Arrays.toString(fac);
		String s_safety = Arrays.toString(safe);
		String s_guest = Arrays.toString(guest);

		System.out.println("s_facility:" + s_facility);

		String facility = s_facility.substring(1, s_facility.length() - 1).replace(" ", "");
		String safety = s_safety.substring(1, s_safety.length() - 1).replace(" ", "");
		String guest_acc = s_guest.substring(1, s_guest.length() - 1).replace(" ", "");

		System.out.println("facfac:" + facility);

		HomeDTO hdto = new HomeDTO();
		hdto.setHome_seq(seq);
		hdto.setHome_amenities(facility);
		hdto.setHome_safety(safety);
		hdto.setHome_guest_access(guest_acc);

		int result = homeService.modifyHomeFacSecAccData(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("seq", seq);
		mav.addObject("result", result);
		mav.setViewName("/host/hostHomeModifyFacilityProc");
		return mav;
	}

	@RequestMapping("/hostHomeModifyLocationTab.do")
	public ModelAndView toHostHomeModifyLocationTab(int seq, HttpServletRequest request, HttpServletRequest response)
			throws Exception {
		System.out.println("homeModifyLocationTab: " + seq);
		HomeDTO hdto = homeService.getHomeData(seq);

		System.out.println(hdto.getHome_checkin_end());
		System.out.println(hdto.getHome_name());
		System.out.println(hdto.getHome_lat());
		System.out.println(hdto.getHome_lng());

		double lat = hdto.getHome_lat();
		double lng = hdto.getHome_lng();

		ModelAndView mav = new ModelAndView();
		mav.addObject("seq", seq);
		mav.addObject("lat", lat);
		mav.addObject("lng", lng);
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostHomeModifyLocationTab");
		return mav;
	}

	@RequestMapping("/hostHomeModifyLocationProc.do")
	public ModelAndView toHosthomeModifyLocationProc(HomeDTO dto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("hosthomeModifyLocationProc: " + dto.getHome_seq());
		System.out.println(dto.getHome_lat());
		System.out.println(dto.getHome_lng());

		int result = homeService.modifyHomeLocData(dto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.addObject("seq", dto.getHome_seq());
		mav.setViewName("/host/hostHomeModifyLocationProc");
		return mav;
	}

	@RequestMapping("/hostHomeModifyStateTab.do")
	public ModelAndView toHostHomeModifyStateTab(int seq) throws Exception {
		System.out.println("hostHomeModifyStateTab: " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostHomeModifyStateTab");

		return mav;
	}

	@RequestMapping("/hostHomeModifyStateProc.do")
	public ModelAndView toHostHomeModifyStateProc(int seq, HomeDTO hdto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("hostHomeModifyStateTab: " + seq);

		String start = hdto.getHome_rest_start();
		String end = hdto.getHome_rest_end();

		System.out.println(hdto.getHome_state() + "," + start + "," + end);

		if (start.contains(",") || end.contains(",")) {
			System.out.println(",가 있다.");
			start = start.replace(",", "");
			end = end.replace(",", "");
		}
		if (start == null && end == null) {
			start = "";
			end = "";
			System.out.println("빈start::" + start);
			System.out.println("빈end::" + end);
		}

		hdto.setHome_rest_start(start);
		hdto.setHome_rest_end(end);
		hdto.setHome_seq(seq);
		int result = homeService.modifyHomeStateData(hdto);
		System.out.println("result:" + result);

		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.addObject("seq", seq);
		mav.setViewName("/host/hostHomeModifyStateProc");

		return mav;
	}

	@RequestMapping("/hostReserveModifyCheckin.do")
	public ModelAndView toHostReserveModifyCheckin(int seq) throws Exception {
		System.out.println("hostReserveModifyCheckin: " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostReserveModifyCheckin");

		return mav;
	}

	@RequestMapping("/hostReserveModifyCheckinProc.do")
	public ModelAndView toHostReserveModifyCheckinProc(int seq, HomeDTO hdto) throws Exception {
		System.out.println("hostReserveModifyCheckinProc: " + seq);
		System.out.println(
				hdto.getHome_checkin_start() + ":" + hdto.getHome_checkin_end() + ":" + hdto.getHome_checkout());

		hdto.setHome_seq(seq);

		int result = homeService.modifyReserveCheckinData(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("seq", seq);
		mav.addObject("result", result);
		mav.setViewName("/host/hostReserveModifyCheckinProc");

		return mav;
	}

	@RequestMapping("/hostReserveModifyNight.do")
	public ModelAndView toHostReserveModifyNight(int seq) throws Exception {
		System.out.println("hostReserveModifyNight: " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostReserveModifyNight");

		return mav;
	}

	@RequestMapping("/hostReserveModifyNightProc.do")
	public ModelAndView toHostReserveModifyNightProc(int seq, HomeDTO hdto) throws Exception {
		System.out.println("hostReserveModifyNightProc: " + seq);

		hdto.setHome_seq(seq);

		int result = homeService.modifyReserveNightData(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("seq", seq);
		mav.addObject("result", result);
		mav.setViewName("/host/hostReserveModifyNightProc");

		return mav;
	}

	@RequestMapping("/fullCalendar.do")
	public ModelAndView toFullCalendar() throws Exception {
		System.out.println("fullCalendar.do: ");

		String member_email = "sksksrff@gmail.com";
		List<HomeDTO> list = homeService.getAllHomeData(member_email);

		System.out.println("list.get(0).getHome_seq()" + list.get(0).getHome_seq());

		HomeDTO hdto = homeService.getHomeData(list.get(0).getHome_seq());

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.addObject("list", list);
		mav.setViewName("/host/fullCalendar");

		return mav;
	}

	@RequestMapping("/modifyCalendar.do")
	public ModelAndView toModifyCalendar(int seq, HomeDTO hdto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("modifyCalendar.do: " + seq);

		String start = request.getParameter("start");
		String end = request.getParameter("end");

		Map<String, String> map = new HashMap<String, String>();
		map.put("start", start);
		map.put("end", end);

		List<String> getCalDL = homeService.getCalendarDate(map);
		List<String> getBlockDL = new ArrayList<>();
		String getDate = homeService.getBlockedDate(seq);

		String[] arr1 = null;
		String[] arr2 = null;
		String str1 = "";
		String inputBlock = "";
		int tracking1 = 0;
		int tracking2 = 0;
		String pos = hdto.getHome_reserve_possible();

		for (String s : getCalDL) {
			System.out.println("str1:" + str1);

			tracking2++;

			if (getCalDL.size() == tracking2) {
				// inputBlock += s.split(" ")[0];
				str1 += s.split(" ")[0];
			} else {
				// inputBlock += s.split(" ")[0] + ",";
				str1 += s.split(" ")[0] + ",";
			}

			System.out.println("str1::" + str1);
		}

		// 예약가능 처리하는 경우
		if (pos.equals("예약 가능")) {
			System.out.println("pos::" + pos);
			List<String> list = new ArrayList<>();
			List<String> gcList = new ArrayList<>();
			String[] s = null;
			String[] gcarr = null;

			for (int i = 0; i < str1.length(); i++) {
				gcarr = str1.split(",");
			}
			for (int i = 0; i < gcarr.length; i++) {
				gcList.add(gcarr[i]);
				Collections.sort(gcList);
			}
			for (String a : gcList) {
				System.out.println("gcList::" + a);
			}

			for (int i = 0; i < getDate.length(); i++) {
				s = getDate.split(",");
				Arrays.sort(s);
			}

			for (int i = 0; i < s.length; i++) {
				list.add(s[i]);
				Collections.sort(list);
			}

			for (String a : list) {
				System.out.println("예약가능::" + a);
			}

			for (String a : gcList) {
				System.out.println("gcList::" + a);
			}

			for (String a : list) {
				System.out.println("removeall 전::" + a);
			}

			list.removeAll(gcList);

			for (String a : list) {
				System.out.println("removeall 후::" + a);
			}

			String str3 = "";
			int tracking3 = 0;
			for (String a : list) {
				System.out.println("str3:" + str3);
				tracking3++;

				if (list.size() == tracking3) {
					// inputBlock += s.split(" ")[0];
					str3 += a.split(" ")[0];
				} else {
					// inputBlock += s.split(" ")[0] + ",";
					str3 += a.split(" ")[0] + ",";
				}

				System.out.println("str3::" + str3);

			}

			if (str3.endsWith(",")) {
				str3 = str3.substring(0, str3.length() - 1);
			}

			hdto.setHome_blocked_date(str3);

		} else if (pos.equals("예약 불가")) {

			// 예약불가 처리하는 경우
			if (getDate != null) {
				// home_blocked_date 가져와서 자르고 List에 담기
				System.out.println("home_blocked_date가 null이 아닐때");
				for (int i = 0; i < getDate.length(); i++) {
					arr1 = getDate.split(",");
					Arrays.sort(arr1);
				}

				for (int i = 0; i < arr1.length; i++) {
					getBlockDL.add(arr1[i]);
					System.out.println(getBlockDL.get(i));
					Collections.sort(getBlockDL);
				}

				for (int i = 0; i < str1.length(); i++) {
					arr2 = str1.split(",");
					Arrays.sort(arr2);
				}

				getCalDL.clear();

				for (int i = 0; i < arr2.length; i++) {
					getCalDL.add(arr2[i]);
				}

				for (String s : getCalDL) {
					System.out.println("getCaldate::::::" + s);
				}

				for (String s : getBlockDL) {
					System.out.println("getblockdate: " + s);
				}

				// getCalendar하고 getBlockedDate List 서로 값 비교
				for (String s : getCalDL) {
					if (!getBlockDL.contains(s)) {
						getBlockDL.add(s);
					}
				}

				Collections.sort(getBlockDL);

				for (String s : getBlockDL) {
					tracking1++;

					if (getBlockDL.size() == tracking1) {
						// inputBlock += s.split(" ")[0];
						inputBlock += s;
					} else {
						// inputBlock += s.split(" ")[0] + ",";
						inputBlock += s + ",";
					}

					System.out.println("inputBlock: " + inputBlock);
				}

				if (inputBlock.endsWith(",")) {
					inputBlock = inputBlock.substring(0, inputBlock.length() - 1);
				}

			} else if (getDate == null) {
				System.out.println("home_blocked_date가 null때");

				Collections.sort(getCalDL);

				for (String s : getCalDL) {
					tracking1++;

					if (getCalDL.size() == tracking1) {
						inputBlock += s.split(" ")[0];
					} else {
						inputBlock += s.split(" ")[0] + ",";
					}

					System.out.println("inputBlock: " + inputBlock);
				}
				if (inputBlock.endsWith(",")) {
					inputBlock = inputBlock.substring(0, inputBlock.length() - 1);
				}
			}

			hdto.setHome_blocked_date(inputBlock);

		}

		hdto.setHome_seq(seq);

		int result = homeService.modifyCalendar(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.addObject("seq", seq);
		mav.setViewName("/host/modifyCalendar");
		return mav;
	}

	@RequestMapping("/calendarAjax.do")
	public void toCalendarAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("calenderAjax: ");
		JSONObject json = new JSONObject();

		int seq = Integer.parseInt(request.getParameter("seq"));
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		System.out.println(start + " : " + end);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String possible = "";

		String date = homeService.getBlockedDate(seq);

		if (date != null) {
			System.out.println("date::" + date.replaceAll("-", "/"));
			if (date.replaceAll("-", "/").contains(start)) {
				System.out.println("있음");
				possible = "예약 불가";
			} else {
				System.out.println("없음");
				possible = "예약 가능";
			}
		}
		json.put("start", start);
		json.put("end", end);
		json.put("possible", possible);

		response.getWriter().print(json);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("/eventsAjax.do")
	public void toEventsAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("eventsAjax: ");
		JSONObject json = new JSONObject();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		int seq = Integer.parseInt(request.getParameter("seq"));
		System.out.println("eventAjax::seq::" + seq);
		String possible = "";

		String date = homeService.getBlockedDate(seq);
		System.out.println("eventsAjax::date::" + date);
		json.put("date", date);

		response.getWriter().print(json);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("/hostHomeAchievement.do")
	public ModelAndView toHostHomeAchievement() throws Exception {
		System.out.println("hostHomeAchievement: ");

		// session에서 member_email을 통해 내 호스트의 모든 후기를 가져온다
		String member_email = "sksksrff@gmail.com";
		// int home_seq = 5;

		List<GuestReviewDTO> satisList = homeService.getSatisfaction();
		List<GuestReviewDTO> accList = homeService.getAccuracy();
		List<GuestReviewDTO> cleanList = homeService.getCleanLiness();
		List<GuestReviewDTO> checkList = homeService.getCheckin();
		List<GuestReviewDTO> ameniList = homeService.getAmenities();
		List<GuestReviewDTO> commList = homeService.getCommunication();
		List<GuestReviewDTO> locList = homeService.getLocation();
		List<GuestReviewDTO> valList = homeService.getValue();

		List<Integer> list_satis = new ArrayList<>();
		List<Integer> list_acc = new ArrayList<>();
		List<Integer> list_clean = new ArrayList<>();
		List<Integer> list_check = new ArrayList<>();
		List<Integer> list_ameni = new ArrayList<>();
		List<Integer> list_comm = new ArrayList<>();
		List<Integer> list_loc = new ArrayList<>();
		List<Integer> list_val = new ArrayList<>();

		List<Integer> numList = new ArrayList<>();
		List<Integer> numList1 = new ArrayList<>();
		List<Integer> numList2 = new ArrayList<>();
		List<Integer> numList3 = new ArrayList<>();
		List<Integer> numList4 = new ArrayList<>();
		List<Integer> numList5 = new ArrayList<>();
		List<Integer> numList6 = new ArrayList<>();
		List<Integer> numList7 = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
			numList1.add(i + 1);
			numList2.add(i + 1);
			numList3.add(i + 1);
			numList4.add(i + 1);
			numList5.add(i + 1);
			numList6.add(i + 1);
			numList7.add(i + 1);
		}

		// 여기서부터 반복하기
		// satisfaction
		for (int i = 0; i < satisList.size(); i++) {
			list_satis.add(satisList.get(i).getG_review_satisfaction());
		}
		numList.removeAll(list_satis);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_satisfaction(numList.get(i));
			dto.setCount(0);
			satisList.add(dto);
		}
		// satisfaction
		// accuracy
		for (int i = 0; i < accList.size(); i++) {
			list_acc.add(accList.get(i).getG_review_accuracy());
		}
		numList1.removeAll(list_acc);
		for (int i = 0; i < numList1.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_accuracy(numList1.get(i));
			dto.setCount(0);
			accList.add(dto);
		}
		// accuracy
		// clean
		for (int i = 0; i < cleanList.size(); i++) {
			list_clean.add(cleanList.get(i).getG_review_cleanliness());
		}
		numList2.removeAll(list_clean);
		for (int i = 0; i < numList2.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_cleanliness(numList2.get(i));
			;
			dto.setCount(0);
			cleanList.add(dto);
		}
		// clean
		// check
		for (int i = 0; i < checkList.size(); i++) {
			list_check.add(checkList.get(i).getG_review_checkIn());
		}
		numList3.removeAll(list_check);
		for (int i = 0; i < numList3.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_checkIn(numList3.get(i));
			dto.setCount(0);
			checkList.add(dto);
		}
		// check
		// amenities
		for (int i = 0; i < ameniList.size(); i++) {
			list_ameni.add(ameniList.get(i).getG_review_amenities());
		}
		numList4.removeAll(list_ameni);
		for (int i = 0; i < numList4.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_amenities(numList4.get(i));
			dto.setCount(0);
			ameniList.add(dto);
		}
		// amenities
		// comm
		for (int i = 0; i < commList.size(); i++) {
			list_comm.add(commList.get(i).getG_review_communication());
		}
		numList5.removeAll(list_comm);
		for (int i = 0; i < numList5.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_communication(numList5.get(i));
			dto.setCount(0);
			commList.add(dto);
		}
		// comm
		// local
		for (int i = 0; i < locList.size(); i++) {
			list_loc.add(locList.get(i).getG_review_location());
		}
		numList6.removeAll(list_loc);
		for (int i = 0; i < numList6.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_location(numList6.get(i));
			dto.setCount(0);
			locList.add(dto);
		}
		// local
		// val
		for (int i = 0; i < valList.size(); i++) {
			list_val.add(valList.get(i).getG_review_value());
		}
		numList7.removeAll(list_val);
		for (int i = 0; i < numList7.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_value(numList7.get(i));
			dto.setCount(0);
			valList.add(dto);
		}
		// val
		// 반복끝
		List<GuestReviewDTO> listGR = homeService.getAllGuestReview(member_email);
		int cnt = homeService.guestReviewCount(member_email);

		Collections.sort(satisList);
		Collections.sort(accList);
		Collections.sort(cleanList);
		Collections.sort(checkList);
		Collections.sort(ameniList);
		Collections.sort(commList);
		Collections.sort(locList);
		Collections.sort(valList);

		double avg1 = 0;
		double avg2 = 0;
		double avg3 = 0;
		double avg4 = 0;
		double avg5 = 0;
		double avg6 = 0;
		double avg7 = 0;
		double avg8 = 0;

		for (GuestReviewDTO g : satisList) {
			avg1 += g.getG_review_satisfaction() * g.getCount();
		}
		for (GuestReviewDTO g : accList) {
			avg2 += g.getG_review_accuracy() * g.getCount();
		}
		for (GuestReviewDTO g : cleanList) {
			avg3 += g.getG_review_cleanliness() * g.getCount();
		}
		for (GuestReviewDTO g : checkList) {
			avg4 += g.getG_review_checkIn() * g.getCount();
		}
		for (GuestReviewDTO g : ameniList) {
			avg5 += g.getG_review_amenities() * g.getCount();
		}
		for (GuestReviewDTO g : commList) {
			avg6 += g.getG_review_communication() * g.getCount();
		}
		for (GuestReviewDTO g : locList) {
			avg7 += g.getG_review_location() * g.getCount();
		}
		for (GuestReviewDTO g : valList) {
			avg8 += g.getG_review_value() * g.getCount();
		}
		avg1 = avg1 / cnt;
		avg1 = Double.parseDouble(String.format("%.1f", avg1));
		avg2 = avg2 / cnt;
		avg2 = Double.parseDouble(String.format("%.1f", avg2));
		avg3 = avg3 / cnt;
		avg3 = Double.parseDouble(String.format("%.1f", avg3));
		avg4 = avg4 / cnt;
		avg4 = Double.parseDouble(String.format("%.1f", avg4));
		avg5 = avg5 / cnt;
		avg5 = Double.parseDouble(String.format("%.1f", avg5));
		avg6 = avg6 / cnt;
		avg6 = Double.parseDouble(String.format("%.1f", avg6));
		avg7 = avg7 / cnt;
		avg7 = Double.parseDouble(String.format("%.1f", avg7));
		avg8 = avg8 / cnt;
		avg8 = Double.parseDouble(String.format("%.1f", avg8));
		double allTotal = (avg1 + avg2 + avg3 + avg4 + avg5 + avg6 + avg7 + avg8) / 8;
		allTotal = Double.parseDouble(String.format("%.1f", allTotal));
		ModelAndView mav = new ModelAndView();
		mav.addObject("avg1", avg1);
		mav.addObject("avg2", avg2);
		mav.addObject("avg3", avg3);
		mav.addObject("avg4", avg4);
		mav.addObject("avg5", avg5);
		mav.addObject("avg6", avg6);
		mav.addObject("avg7", avg7);
		mav.addObject("avg8", avg8);
		mav.addObject("allTotal", allTotal);
		mav.addObject("listGR", listGR);
		mav.addObject("cnt", cnt);
		mav.addObject("satList", satisList);
		mav.addObject("accList", accList);
		mav.addObject("cleanList", cleanList);
		mav.addObject("checkList", checkList);
		mav.addObject("ameniList", ameniList);
		mav.addObject("commList", commList);
		mav.addObject("locList", locList);
		mav.addObject("valList", valList);
		mav.setViewName("/host/hostHomeAchievement");
		return mav;
	}

	@RequestMapping("/hostHomeManage.do")
	public ModelAndView toHostHomeManage() throws Exception {
		System.out.println("hostHomeManage: ");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/host/hostHomeManage");
		return mav;
	}

	@RequestMapping("/hostHomePayment.do")
	public ModelAndView toHosHomePayment() throws Exception {
		System.out.println("hostHomeManage: ");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/host/hostHomePayment");
		return mav;
	}

	@RequestMapping("/hostHomePaymentSelect.do")
	public ModelAndView toHostHomePaymentSelect() throws Exception {
		System.out.println("hostHomeManageSelect: ");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/host/hostHomePaymentSelect");
		return mav;
	}

	@RequestMapping("/hostHomePaymentAddress.do")
	public ModelAndView toHostHomePaymentAddAddress() throws Exception {
		System.out.println("hostHomePaymentAddress: ");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/host/hostHomePaymentAddress");
		return mav;
	}

	@RequestMapping("/hostHomePaymentBreakdown.do")
	public ModelAndView toHostHomePaymentBreakdown() throws Exception {
		System.out.println("hostHomePaymentBreakdown: ");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/host/hostHomePaymentBreakdown");
		return mav;
	}

	@RequestMapping("/hostReserveModifyRule.do")
	public ModelAndView toHostReserveModifyRule(HomeDTO hdto, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("hostReserveModifyRule: " + hdto.getHome_seq());

		hdto = homeService.getHomeData(hdto.getHome_seq());

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostReserveModifyRule");
		return mav;
	}

	@RequestMapping("/hostReserveModifyRuleProc.do")
	public ModelAndView toHostReserveModifyRuleProc(HomeDTO hdto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("hostReserveModifyRuleProc: " + hdto.getHome_seq());

		// 라디오
		String rules1 = request.getParameter("rules1");
		String rules2 = request.getParameter("rules2");
		String rules3 = request.getParameter("rules3");
		String rules4 = request.getParameter("rules4");
		String rules5 = request.getParameter("rules5");

		System.out.println("rules::" + rules1);
		System.out.println("rules::" + rules2);
		System.out.println("rules::" + rules3);
		System.out.println("rules::" + rules4);
		System.out.println("rules::" + rules5);

		if (rules1.equals("예")) {
			rules1 = "어린이(만 2~12세)에게 적합함";
		} else {
			rules1 = "";
		}

		if (rules2.equals("예")) {
			rules2 = "유아(만 2세 미만)에게 적합함";
		} else {
			rules2 = "";
		}

		if (rules3.equals("예")) {
			rules3 = "반려동물 입실 가능";
		} else {
			rules3 = "";
		}

		if (rules4.equals("예")) {
			rules4 = "흡연 가능";
		} else {
			rules4 = "";
		}

		if (rules5.equals("예")) {
			rules5 = "파티나 이벤트 가능";
		} else {
			rules5 = "";
		}

		System.out.println("rules::" + rules1);
		System.out.println("rules::" + rules2);
		System.out.println("rules::" + rules3);
		System.out.println("rules::" + rules4);
		System.out.println("rules::" + rules5);

		List<String> list1 = new ArrayList<>();

		list1.add(rules1);
		list1.add(rules2);
		list1.add(rules3);
		list1.add(rules4);
		list1.add(rules5);

		String setRules = "";

		for (String s : list1) {
			if (!s.equals(""))
				setRules += s + ",";
		}

		if (setRules.endsWith(",")) {
			setRules = setRules.substring(0, setRules.length() - 1);
		}

		System.out.println("setReules::" + setRules);

		// 체크텍스트
		String check2_text = request.getParameter("check2-text");
		String check3_text = request.getParameter("check3-text");
		String check4_text = request.getParameter("check4-text");
		String check5_text = request.getParameter("check5-text");
		String check6_text = request.getParameter("check6-text");

		String check2 = request.getParameter("check2");
		String check3 = request.getParameter("check3");
		String check4 = request.getParameter("check4");
		String check5 = request.getParameter("check5");
		String check6 = request.getParameter("check6");

		System.out.println("check-text2::" + check2 + "::" + check2_text);
		System.out.println("check-text3::" + check3 + "::" + check3_text);
		System.out.println("check-text4::" + check4 + "::" + check4_text);
		System.out.println("check-text5::" + check5 + "::" + check5_text);
		System.out.println("check-text6::" + check6 + "::" + check6_text);

		ArrayList<String> list2 = new ArrayList<>();

		if (check2 == null) {
			check2 = "";
		} else if (check2 != null && check2_text != null) {
			check2 = check2 + ":" + check2_text;
			list2.add(check2);
		} else if (check2 != null && check2_text == null) {
			list2.add(check2);
		}

		if (check3 == null) {
			check3 = "";
		} else if (check3 != null && check3_text != null) {
			check3 = check3 + ":" + check3_text;
			list2.add(check3);
		} else if (check3 != null && check3_text == null) {
			list2.add(check3);
		}

		if (check4 == null) {
			check4 = "";
		} else if (check4 != null && check4_text != null) {
			check4 = check4 + ":" + check4_text;
			list2.add(check4);
		} else if (check4 != null && check4_text == null) {
			list2.add(check4);
		}

		if (check5 == null) {
			check5 = "";
		} else if (check5 != null && check5_text != null) {
			check5 = check5 + ":" + check5_text;
			list2.add(check5);
		} else if (check5 != null && check5_text == null) {
			list2.add(check5);
		}

		if (check6 == null) {
			check6 = "";
		} else if (check6 != null && check6_text != null) {
			check6 = check6 + ":" + check6_text;
			list2.add(check6);
		} else if (check6 != null && check6_text == null) {
			list2.add(check6);
		}

		String setCheck = "";

		for (String s : list2) {
			setCheck += s + ",";
		}
		if (setCheck.endsWith(",")) {
			setCheck = setCheck.substring(0, setCheck.length() - 1);
		}

		System.out.println("setCheck::" + setCheck);

		hdto.setHome_rules(setRules);
		hdto.setHome_details(setCheck);

		int result = homeService.modifyHomeRulesDetails(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.addObject("seq", hdto.getHome_seq());
		mav.setViewName("/host/hostReserveModifyRuleProc");
		return mav;
	}

	@RequestMapping("/hostHomeList.do")
	public ModelAndView toHostHomeList() throws Exception {
		System.out.println("/hostHomeList:");

		// 세션에서 아이디 꺼내기
		String member_email = "sksksrff@gmail.com";
		List<HomeDTO> list = homeService.getAllHomeData(member_email);

		System.out.println("list.size::" + list.size());

		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.setViewName("/host/hostHomeList");
		return mav;
	}

	@RequestMapping("/hostReserveAllManaging.do")
	public ModelAndView toHostReserveAllManaging() throws Exception {
		System.out.println("/hostReserveAllManaging:");

		// 세션에서 아이디 꺼내기
		String host_email = "sksksrff@gmail.com";
		List<ReservationDTO> rlist = homeService.getAllReservation(host_email);

		System.out.println("rlist.size::" + rlist.size());

		SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy년 MM월 dd일");

		for (int i = 0; i < rlist.size(); i++) {
			Date to1 = fm1.parse(rlist.get(i).getReserv_checkin());
			String str1 = fm2.format(to1);
			rlist.get(i).setReserv_checkin(str1);

			Date to2 = fm1.parse(rlist.get(i).getReserv_checkout());
			String str2 = fm2.format(to2);
			rlist.get(i).setReserv_checkout(str2);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("rlist", rlist);
		mav.setViewName("/host/hostReserveAllManaging");
		return mav;
	}

	@RequestMapping("/hostReserveManaging.do")
	public ModelAndView toHostReserveManaging() throws Exception {
		System.out.println("/hostReserveManaging:");

		// 세션에서 아이디 꺼내기
		String host_email = "sksksrff@gmail.com";
		List<ReservationDTO> rlist = homeService.getAllReservation(host_email);

		System.out.println("rlist.size::" + rlist.size());

		SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy년 MM월 dd일");

		for (int i = 0; i < rlist.size(); i++) {
			Date to1 = fm1.parse(rlist.get(i).getReserv_checkin());
			String str1 = fm2.format(to1);
			rlist.get(i).setReserv_checkin(str1);

			Date to2 = fm1.parse(rlist.get(i).getReserv_checkout());
			String str2 = fm2.format(to2);
			rlist.get(i).setReserv_checkout(str2);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("rlist", rlist);
		mav.setViewName("/host/hostReserveManaging");
		return mav;
	}

	@RequestMapping("hostHits.do")
	public ModelAndView toHostHits() throws Exception {
		System.out.println("hostHists.do/");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/host/hostHits");
		return mav;
	}

}