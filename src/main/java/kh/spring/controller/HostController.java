package kh.spring.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kh.spring.dto.AccountDTO;
import kh.spring.dto.BedDTO;
import kh.spring.dto.GuestReviewDTO;
import kh.spring.dto.HomeDTO;
import kh.spring.dto.HomeDescDTO;
import kh.spring.dto.HomePicDTO;
import kh.spring.dto.MessageDTO;
import kh.spring.dto.PaymentDTO;
import kh.spring.dto.ReservationDTO;
import kh.spring.interfaces.HomeService;

@Controller
public class HostController {

	@Autowired
	private HomeService homeService;

	@RequestMapping("/hostMain.do")
	public ModelAndView toHostMain(HttpServletRequest request, HttpSession session) throws Exception {
		System.out.println("/homeMain.do:");
		session.setAttribute("login_email", "sksksrff@gmail.com");
		String member_email = (String) session.getAttribute("login_email");

		System.out.println("member_email::" + member_email);

		List<HomeDTO> homeList = homeService.getAllHomeData(member_email);
		HomeDTO hdto = homeService.getOldestHomeData(member_email);
		List<ReservationDTO> rlist = homeService.getAllReservation(member_email);
		List<MessageDTO> mlist = homeService.getAllMessage(member_email);

		int cnt = homeService.guestReviewAllCount(member_email);

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

		Calendar cal = new GregorianCalendar(Locale.KOREA);
		int month = cal.get(Calendar.MONTH) + 1;

		Map<String, Object> map2 = new HashMap<>();
		map2.put("member_email", member_email);
		map2.put("reserv_state", 1);

		List<ReservationDTO> rlist2 = homeService.getApprovalReserve(map2);
		double allTotal = getTotal(0, member_email);
		System.out.println("alltotal::" + allTotal);
		System.out.println("rlist2.size::" + rlist2.size());
		System.out.println(hdto.getHome_main_pic());

		List<HomePicDTO> hplist = homeService.getHomePicData(hdto.getHome_seq());

		// 조회수
		int view = 0;
		for (HomeDTO h : homeList) {
			view += h.getHome_view();
		}
		// 예약 & 월 수입
		HashMap<String, Object> map = new HashMap<>();
		map.put("member_email", member_email);
		map.put("home_seq", 0);
		List<PaymentDTO> plist = homeService.getAllPayment(map);
		String date = "";

		for (PaymentDTO p : plist) {
			date += p.getCheckIn() + ",";
		}

		if (date.endsWith(",")) {
			date = date.substring(0, date.length() - 1);
		}

		System.out.println("date::" + date);
		String[] dateArr = {};

		if (date != null) {
			dateArr = date.split(",");
		}

		int dateCnt = 0;
		int amount = 0;

		if (!date.equals("")) {
			for (int i = 0; i < dateArr.length; i++) {
				if (dateArr.length != 0 && dateArr != null) {
					if (Integer.parseInt(dateArr[i].split("-")[1].split("-")[0]) == month) {
						System.out.println("9월이용::" + Integer.parseInt(dateArr[i].split("-")[1].split("-")[0]));
						dateCnt++;
						amount += Integer.parseInt(plist.get(i).getPayment_amount());
					}
				}
			}
		}
		// 숙소호감도
		List<HomeDTO> hlist = homeService.getAllHomeDataMain();
		int homePrice = 0;
		int standard = 0;
		for (HomeDTO h : hlist) {
			homePrice += h.getHome_price();
		}
		standard = homePrice / hlist.size(); // 모든 숙소 가격 기준

		int ntot = 0;
		if (hdto.getHome_amenities() == null) {
			ntot++;
		}
		if (hdto.getHome_details() == null) {
			ntot++;
		}
		if (hdto.getHome_safety() == null) {
			ntot++;
		}
		if (hdto.getHome_rules() == null) {
			ntot++;
		}
		if (hdto.getHome_guest_access() == null) {
			ntot++;
		}
		if (hdto.getHome_public() == null) {
			ntot++;
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("mlist", mlist);
		mav.addObject("rlist", rlist);
		mav.addObject("rlist2", rlist2);
		mav.addObject("homeList", homeList);
		mav.addObject("hplist", hplist);
		mav.addObject("hdto", hdto);
		mav.addObject("view", view);
		mav.addObject("standard", standard);
		mav.addObject("amount", amount);
		mav.addObject("dateCnt", dateCnt);
		mav.addObject("cnt", cnt);
		mav.addObject("ntot", ntot);
		mav.addObject("month", month);
		mav.addObject("allTotal", allTotal);
		mav.setViewName("/host/hostMain");

		return mav;

	}

	@RequestMapping("/summary.do")
	public void toSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("/summary.do:" + 1111);
		int seq = Integer.parseInt(request.getParameter("seq"));
		System.out.println(seq);
		List<HomePicDTO> hplist = homeService.getHomePicData(seq);

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

		// 숙소호감도
		List<HomeDTO> hlist = homeService.getAllHomeDataMain();
		int homePrice = 0;
		int standard = 0;
		for (HomeDTO h : hlist) {
			homePrice += h.getHome_price();
		}
		standard = homePrice / hlist.size(); // 모든 숙소 가격 기준

		int ntot = 0;
		if (hdto.getHome_amenities() == null) {
			ntot++;
		}
		if (hdto.getHome_details() == null) {
			ntot++;
		}
		if (hdto.getHome_safety() == null) {
			ntot++;
		}
		if (hdto.getHome_rules() == null) {
			ntot++;
		}
		if (hdto.getHome_guest_access() == null) {
			ntot++;
		}
		if (hdto.getHome_public() == null) {
			ntot++;
		}

		Map<String, Object> map = new HashMap<>();
		map.put("hdto", hdto);
		map.put("hplist", hplist);
		map.put("homePrice", homePrice);
		map.put("standard", standard);
		map.put("ntot", ntot);
		Gson gson = new Gson();
		gson.toJson(map, response.getWriter());

	}

	@RequestMapping("/hostHomeTab.do")
	public ModelAndView toHostHomeTab(int seq) throws Exception {
		System.out.println("/hostHomeTab.do : " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		List<String> list = new ArrayList<String>();
		List<HomePicDTO> hplist = homeService.getHomePicData(seq);
		String bstr = "";
		BedDTO bdto = homeService.getBedData(seq);

		if (bdto != null) {
			bstr = bdto.getBed_single() + "," + bdto.getBed_double() + "," + bdto.getBed_queen();
		}

		if (!bstr.equals(null) && !bstr.equals("")) {
			// bstr = bstr.replace(null, "");
			if (bstr.startsWith(",")) {
				bstr.substring(1);
			}
			if (bstr.endsWith(",")) {
				bstr.substring(0, bstr.length() - 1);
			}
		}

		System.out.println("bstr::" + bstr);
		String[] barr = null;

		if (bstr != null && !bstr.equals("")) {
			bstr = bstr.replace(" ", "");
			barr = bstr.split(",");
			System.out.println("bstr::" + bstr);
		}

		int bedCnt = 0;

		if (barr != null) {
			System.out.println("아오 짜증나진짜");
			for (int i = 0; i < barr.length; i++) {
				// if (barr[i] == null) {
				// barr[i].replace(null, "0");
				System.out.println("수박::" + barr[i]);
				bedCnt += Integer.parseInt(barr[i]);
				// }

			}
		}

		String[] rarr = {};

		if (bdto != null) {
			System.out.println("bdto::" + bdto.getBed_single());
			if (bdto.getBed_single() != null) {
				// bdto.setBed_single(bdto.getBed_single().replaceAll(null, ""));
				System.out.println("bdto::" + bdto.getBed_single());
				rarr = bdto.getBed_single().split(",");
			}

		}

		int roomCnt = rarr.length;
		System.out.println("bedCnt::" + bedCnt);

		String[] amenities = {};
		String[] safety = {};
		String[] guest_access = {};

		System.out.println(hdto.getHome_amenities());
		System.out.println(hdto.getHome_safety());
		System.out.println(hdto.getHome_guest_access());

		if (hdto.getHome_amenities() != null) {
			amenities = hdto.getHome_amenities().split(",");
		}
		if (hdto.getHome_safety() != null) {
			safety = hdto.getHome_safety().split(",");
		}
		if (hdto.getHome_guest_access() != null) {
			guest_access = hdto.getHome_guest_access().split(",");
		}

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
		mav.addObject("bedCnt", bedCnt);
		mav.addObject("roomCnt", roomCnt);
		mav.addObject("hdto", hdto);
		mav.addObject("list", list);
		mav.addObject("hplist", hplist);
		mav.setViewName("/host/hostHomeTab");

		return mav;
	}

	@RequestMapping("/hostReserveTab.do")
	public ModelAndView toHostReserveTab(int seq) throws Exception {
		System.out.println("/hostReserveTab.do : " + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		String[] rules = {};
		String[] details = {};
		String tmp = "";
		if (hdto.getHome_rules() != null) {
			rules = hdto.getHome_rules().split(",");
		} else {
		}

		if (hdto.getHome_details() != null) {
			details = hdto.getHome_details().split(",");
			for (int i = 0; i < details.length; i++) {
				tmp += details[i].split(":")[0];
			}
		} else {
		}

		System.out.println("tmp::" + tmp);
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
		int seq = Integer.parseInt(mr.getParameter("seq"));
		System.out.println("멀티파트 seq::" + seq);
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
			String file = request.getParameter("file").split(";")[0];
			System.out.println("sepsarate1::file::" + file);
			int seq = Integer.parseInt(request.getParameter("seq"));

			String realPath = request.getSession().getServletContext().getRealPath("/files/");

			File f = new File(realPath);
			f.mkdir();
			File[] innerFile = f.listFiles();
			String filename = null;
			if (file != null) {
				filename = file.split("/")[1];
			}

			System.out.println("filename : " + filename);

			int delResult = homeService.deleteHomeMainPicData(filename, seq);

			for (int i = 0; i < innerFile.length; i++) {
				System.out.println(innerFile[i].getName().equals(filename));

				if (innerFile[i].getName().equals(filename)) {
					innerFile[i].delete();
				}
			}
			System.out.println("separate1::delresult::" + delResult);
			json.put("result", delResult);

			response.getWriter().print(json);
			response.getWriter().flush();
			response.getWriter().close();

		} else if (separate == 2) {
			System.out.println("deletePhoto2: " + request.getParameter("file"));
			String file = request.getParameter("file").split(";")[0];

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
			System.out.println("deletePhoto3:" + request.getParameter("file"));
			System.out.println("toMainPic :" + request.getParameter("toMainPic"));

			String file = request.getParameter("file").split(";")[0];
			int seq = Integer.parseInt(request.getParameter("seq"));
			String toMainPic = request.getParameter("toMainPic");
			System.out.println("toMainPic::" + toMainPic);

			if (toMainPic == null) {
			} else if (toMainPic != null) {
				System.out.println("null이라매");
				toMainPic = toMainPic.split(";")[0];
				String homePic = toMainPic.split("/")[1];

				System.out.println("homePic::" + homePic);

				int delResult = homeService.deleteHomePicData(homePic);
				int upMainPicResult = homeService.addHomeRepresentData(homePic, seq);
				System.out.println("separate3::디비 결과: " + delResult + " :: " + upMainPicResult);
				json.put("result", delResult);

				response.getWriter().print(json);
				response.getWriter().flush();
				response.getWriter().close();
			}

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

		String[] accarr = {};
		String[] amenarr = {};
		String[] safearr = {};

		if (acc != null) {
			accarr = acc.split(",");
		}
		if (amen != null) {
			amenarr = amen.split(",");
		}
		if (safe != null) {
			safearr = safe.split(",");
		}

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

		String facility = "";
		String safety = "";
		String guest_acc = "";

		System.out.println("s_facility:" + s_facility);

		if (fac == null) {
			facility = "";
		} else {
			facility = s_facility.substring(1, s_facility.length() - 1).replace(" ", "");
		}

		if (safe == null) {
			safety = "";
		} else {
			safety = s_safety.substring(1, s_safety.length() - 1).replace(" ", "");
		}
		if (guest == null) {
			guest_acc = "";
		} else {
			guest_acc = s_guest.substring(1, s_guest.length() - 1).replace(" ", "");
		}

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

	@RequestMapping("/hostHomePolicyModifyTab.do")
	public ModelAndView toHostHomePolicyModifyTab(int seq) throws Exception {
		System.out.println("hostHomePolicyModifyTab:" + seq);

		HomeDTO hdto = homeService.getHomeData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostHomePolicyModifyTab");
		return mav;
	}

	@RequestMapping("/hostHomePolicyModifyProc.do")
	public ModelAndView toHostHomePolicyModifyProc(HttpServletRequest request, int seq) throws Exception {
		System.out.println("hostHomePolicyModifyProc:" + seq);

		String policy = request.getParameter("policy");
		System.out.println(policy);

		HomeDTO hdto = homeService.getHomeData(seq);
		hdto.setHome_policy(policy);

		int result = homeService.modifyPolicy(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("seq", hdto.getHome_seq());
		mav.addObject("result", result);
		mav.setViewName("/host/hostHomePolicyModifyProc");
		return mav;
	}

	@RequestMapping("/hostHomeRoomModifyTab.do")
	public ModelAndView toHostHomeRoomModifyTab(int seq) throws Exception {
		System.out.println("hostHomeRoomModifyTab:" + seq);

		HomeDTO hdto = homeService.getHomeData(seq);
		BedDTO bdto = homeService.getBedData(seq);
		int bcnt = 0;
		System.out.println("bdto::" + bdto);

		ModelAndView mav = new ModelAndView();

		System.out.println(bdto);

		if (bdto == null) {
			bcnt = 0;
		} else {
			if (bdto.getBed_single() != null) {
				String[] arr = bdto.getBed_single().split(",");
			}
		}

		if (bdto != null && bdto.getBed_single() == null) {
			bdto.setBed_single("0");
		} else if (bdto != null && bdto.getBed_single() != null) {
			bdto.setBed_single(bdto.getBed_single().replace(" ", ""));
		}
		if (bdto != null && bdto.getBed_double() == null) {
			bdto.setBed_double("0");
		} else if (bdto != null && bdto.getBed_double() == null) {
			bdto.setBed_double(bdto.getBed_double().replace(" ", ""));
		}
		if (bdto != null && bdto.getBed_queen() == null) {
			bdto.setBed_queen("0");
		} else if (bdto != null && bdto.getBed_queen() == null) {
			bdto.setBed_queen(bdto.getBed_queen().replace(" ", ""));
		}

		mav.addObject("bdto", bdto);
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostHomeRoomModifyTab");
		return mav;
	}

	@RequestMapping("/hostHomeRoomModifyProc.do")
	public ModelAndView toHostHomeRoomModifyProc(HttpServletRequest request, int cnt, int seq, HomeDTO getHdto)
			throws Exception {
		System.out.println("hostHomeRoomModifyProc:" + seq);
		System.out.println("hostHomeRoomModifyProc:" + cnt);

		int bresult = 0;
		int hresult = 0;
		String sofaCnt = "0";
		String mattCnt = "0";
		String bathCnt = "0";

		HomeDTO hdto = new HomeDTO();
		BedDTO bdto = homeService.getBedData(seq);
		System.out.println("bdto::" + bdto);

		if (cnt == 0) {
			System.out.println("cnt == 0::");
			if (bdto != null) {
				homeService.deleteBed(seq);
			}
		} else {
			System.out.println("cnt != 0::");
			if (bdto == null) {
				homeService.insertBed(seq, bdto);
			}

			if (request.getParameter("sofacount") != null) {
				sofaCnt = request.getParameter("sofacount");
			}
			if (request.getParameter("mattcount") != null) {
				mattCnt = request.getParameter("mattcount");
			}
			if (request.getParameter("bathcount") != null) {
				bathCnt = request.getParameter("bathcount");
			}

			System.out.println(getHdto.getHome_buildingType());
			System.out.println(getHdto.getHome_type());
			System.out.println(sofaCnt);
			System.out.println(mattCnt);
			System.out.println(bathCnt);

			String home_public = sofaCnt + "," + mattCnt + "," + bathCnt;

			String[] sarr = new String[cnt];
			String[] darr = new String[cnt];
			String[] qarr = new String[cnt];

			System.out.println("sarr::" + sarr.length);
			System.out.println("darr::" + darr.length);
			System.out.println("qarr::" + qarr.length);

			for (int i = 0; i < cnt; i++) {
				sarr[i] = request.getParameter("single-count" + (i + 1) + "");
				darr[i] = request.getParameter("double-count" + (i + 1) + "");
				qarr[i] = request.getParameter("queen-count" + (i + 1) + "");
			}

			hdto = homeService.getHomeData(seq);

			if (sarr != null) {
				bdto.setBed_single(Arrays.toString(sarr));
				System.out.println("bdto.getBed_single::"+bdto.getBed_single());
				if (bdto.getBed_single() != null) {
					bdto.setBed_single(bdto.getBed_single().substring(1, bdto.getBed_single().length() - 1));
				}
			}
			if (darr != null) {
				bdto.setBed_double(Arrays.toString(darr));
				if (bdto.getBed_double() != null) {
					bdto.setBed_double(bdto.getBed_double().substring(1, bdto.getBed_double().length() - 1));
				}
			}
			if (qarr != null) {
				bdto.setBed_queen(Arrays.toString(qarr));
				if (bdto.getBed_queen() != null) {
					bdto.setBed_queen(bdto.getBed_queen().substring(1, bdto.getBed_queen().length() - 1));
				}
			}

			bdto.setHome_seq(seq);
			getHdto.setHome_seq(seq);
			getHdto.setHome_public(home_public);

			System.out.println("bdto::" + bdto.getBed_single());
			System.out.println(bdto.getBed_double());
			System.out.println(bdto.getBed_queen());
			System.out.println(bdto.getHome_seq());

			bdto.setBed_single(bdto.getBed_single().replace(" ", ""));
			bdto.setBed_double(bdto.getBed_double().replace(" ", ""));
			bdto.setBed_queen(bdto.getBed_queen().replace(" ", ""));

			bresult = homeService.modifybed(bdto);
			hresult = homeService.modifyHomeType(getHdto);
			System.out.println("result:: " + bresult + "::" + hresult);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("seq", hdto.getHome_seq());
		mav.addObject("bresult", bresult);
		mav.addObject("hresult", hresult);
		mav.setViewName("/host/hostHomeRoomModifyProc");
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
	public ModelAndView toHostReserveModifyNightProc(int seq, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("hostReserveModifyNightProc: " + seq);
		System.out.println(request.getParameter("home_min_stay"));

		HomeDTO hdto = new HomeDTO();
		hdto.setHome_seq(seq);

		if (request.getParameter("home_min_stay") == null) {
			hdto.setHome_min_stay(0);
		} else {
			hdto.setHome_min_stay(Integer.parseInt(request.getParameter("home_min_stay")));
		}
		if (request.getParameter("home_max_stay") == null) {
			hdto.setHome_max_stay(0);
		} else {
			hdto.setHome_max_stay(Integer.parseInt(request.getParameter("home_max_stay")));
		}

		int result = homeService.modifyReserveNightData(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("seq", seq);
		mav.addObject("result", result);
		mav.setViewName("/host/hostReserveModifyNightProc");

		return mav;
	}

	@RequestMapping("/fullCalendar.do")
	public ModelAndView toFullCalendar(HttpSession session) throws Exception {
		System.out.println("fullCalendar.do: ");

		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";
		System.out.println("member_email::" + member_email);
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
		JSONArray jarr = new JSONArray();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		int seq = Integer.parseInt(request.getParameter("seq"));
		System.out.println("eventAjax::seq::" + seq);
		String possible = "";

		String date = homeService.getBlockedDate(seq);
		System.out.println("eventsAjax::date::" + date);

		Map<String, Object> map = new HashMap<>();
		map.put("home_seq", seq);
		map.put("reserv_state", 1);

		List<ReservationDTO> rlist = homeService.getCalReservation(map);

		// String[] str_arr = {};
		// String[] sarr = {};
		// str_arr = date.split(",");
		//
		// Date d = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD");
		// sdf.format(d);
		// System.out.println("d::::"+d);
		//
		//
		// for(int i=0; i<str_arr.length; i++) {
		// Date tmp = new Date(str_arr[i]);
		// sarr[i] = tmp.toString();
		// System.out.println(sarr[i]);
		// }

		jarr.add(date);
		for (int i = 0; i < rlist.size(); i++) {
			JSONObject tmp = new JSONObject();
			tmp.put("member_email", rlist.get(i).getMember_email());
			tmp.put("reserv_checkin", rlist.get(i).getReserv_checkin());
			tmp.put("reserv_checkout", rlist.get(i).getReserv_checkout());
			tmp.put("member_name", rlist.get(i).getMember_name());
			tmp.put("totalamount", rlist.get(i).getTotalAmount());
			tmp.put("population", rlist.get(i).getPopulation());
			jarr.add(tmp);
		}

		json.put("jarr", jarr);

		// json.put("date", date);
		System.out.println("파싱::" + json.toJSONString());

		response.getWriter().print(json);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("/hostHomeAchievement.do")
	public ModelAndView toHostHomeAchievement(HttpSession session, HttpServletRequest request) throws Exception {
		System.out.println("hostHomeAchievement: ");
		String currentPageString = request.getParameter("currentPage");
		System.out.println("currentPageString::" + currentPageString);

		int home_seq = 0;

		if (request.getParameter("seq") == null) {
			home_seq = 0;
		} else if (request.getParameter("seq") != null) {
			home_seq = Integer.parseInt(request.getParameter("seq"));
		}

		System.out.println("seq::" + home_seq);

		// session에서 member_email을 통해 내 호스트의 모든 후기를 가져온다
		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";

		List<GuestReviewDTO> satisList = homeService.getSatisfaction(home_seq);
		List<GuestReviewDTO> accList = homeService.getAccuracy(home_seq);
		List<GuestReviewDTO> cleanList = homeService.getCleanLiness(home_seq);
		List<GuestReviewDTO> checkList = homeService.getCheckin(home_seq);
		List<GuestReviewDTO> ameniList = homeService.getAmenities(home_seq);
		List<GuestReviewDTO> commList = homeService.getCommunication(home_seq);
		List<GuestReviewDTO> locList = homeService.getLocation(home_seq);
		List<GuestReviewDTO> valList = homeService.getValue(home_seq);

		List<Integer> list_satis = new ArrayList<>();
		List<Integer> list_acc = new ArrayList<>();
		List<Integer> list_clean = new ArrayList<>();
		List<Integer> list_check = new ArrayList<>();
		List<Integer> list_ameni = new ArrayList<>();
		List<Integer> list_comm = new ArrayList<>();
		List<Integer> list_loc = new ArrayList<>();
		List<Integer> list_val = new ArrayList<>();

		List<Integer> numList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
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
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// accuracy
		for (int i = 0; i < accList.size(); i++) {
			list_acc.add(accList.get(i).getG_review_accuracy());
		}
		numList.removeAll(list_acc);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_accuracy(numList.get(i));
			dto.setCount(0);
			accList.add(dto);
			System.out.println("getcount::"+dto.getCount());
		}
		// accuracy
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// clean
		for (int i = 0; i < cleanList.size(); i++) {
			list_clean.add(cleanList.get(i).getG_review_cleanliness());
		}
		numList.removeAll(list_clean);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_cleanliness(numList.get(i));
			dto.setCount(0);
			cleanList.add(dto);
		}
		// clean
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		
		// check
		for (int i = 0; i < checkList.size(); i++) {
			list_check.add(checkList.get(i).getG_review_checkIn());
		}
		numList.removeAll(list_check);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_checkIn(numList.get(i));
			dto.setCount(0);
			checkList.add(dto);
		}
		// check
		
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		
		// amenities
		for (int i = 0; i < ameniList.size(); i++) {
			list_ameni.add(ameniList.get(i).getG_review_amenities());
		}
		numList.removeAll(list_ameni);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_amenities(numList.get(i));
			dto.setCount(0);
			ameniList.add(dto);
		}
		// amenities
		
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		
		// comm
		for (int i = 0; i < commList.size(); i++) {
			list_comm.add(commList.get(i).getG_review_communication());
		}
		numList.removeAll(list_comm);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_communication(numList.get(i));
			dto.setCount(0);
			commList.add(dto);
		}
		// comm
		
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		
		// local
		for (int i = 0; i < locList.size(); i++) {
			list_loc.add(locList.get(i).getG_review_location());
		}
		numList.removeAll(list_loc);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_location(numList.get(i));
			dto.setCount(0);
			locList.add(dto);
		}
		// local
		
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		
		// val
		for (int i = 0; i < valList.size(); i++) {
			list_val.add(valList.get(i).getG_review_value());
		}
		numList.removeAll(list_val);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_value(numList.get(i));
			dto.setCount(0);
			valList.add(dto);
		}
		// val
		
		// 반복끝

		// review paging
		int currentPage = 0;

		if (currentPageString == null) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(currentPageString);
		}

		HashMap<String, Object> map = new HashMap<>();
		map.put("member_email", member_email);
		map.put("home_seq", home_seq);
		map.put("startNum", currentPage * 5 - 4);
		map.put("endNum", currentPage * 5);

		List<GuestReviewDTO> listGR = homeService.getAllGuestReview(map);
		// int allCnt = homeService.guestReviewAllCount(member_email);
		int cnt = homeService.guestReviewCount(map);
		String paging = homeService.getReviewPageNavi(currentPage, home_seq, map);

		// 정렬
		Collections.sort(satisList);
		Collections.sort(accList);
		Collections.sort(cleanList);
		Collections.sort(checkList);
		Collections.sort(ameniList);
		Collections.sort(commList);
		Collections.sort(locList);
		Collections.sort(valList);

		// 평균 구하기
		double avg1 = 0;
		double avg2 = 0;
		double avg3 = 0;
		double avg4 = 0;
		double avg5 = 0;
		double avg6 = 0;
		double avg7 = 0;
		double avg8 = 0;

		for (GuestReviewDTO g : satisList) {
			System.out.println(g.getG_review_satisfaction());
			avg1 += g.getG_review_satisfaction() * g.getCount();
			System.out.println("a:"+avg1);
			System.out.println(g.getCount());
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
		
		System.out.println("cnt::"+cnt);
		
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
		
		System.out.println("avg1::"+avg1);
		System.out.println("avg1::"+avg2);
		System.out.println("avg1::"+avg3);
		System.out.println("avg1::"+avg4);
		System.out.println("avg1::"+avg5);
		System.out.println("avg1::"+avg6);
		System.out.println("avg1::"+avg7);
		System.out.println("avg1::"+avg8);
		
		allTotal = Double.parseDouble(String.format("%.1f", allTotal));
		System.out.println("allTotal::"+allTotal);

		
		List<HomeDTO> hlist = homeService.getAllHomeData(member_email);

		ModelAndView mav = new ModelAndView();
		mav.addObject("paging", paging);
		mav.addObject("hlist", hlist);
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
	public ModelAndView toHosHomePayment(HttpSession session) throws Exception {
		System.out.println("hostHomeManage: ");

		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";
		List<AccountDTO> alist = homeService.getAllAccount(member_email);

		ModelAndView mav = new ModelAndView();
		mav.addObject("alist", alist);
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
	public ModelAndView toHostHomePaymentBreakdown(HttpSession session, HttpServletRequest request) throws Exception {
		System.out.println("hostHomePaymentBreakdown: ");

		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";
		int seq = 0;

		if (request.getParameter("seq") == null) {
			seq = 0;
		} else {
			seq = Integer.parseInt(request.getParameter("seq"));
		}
		System.out.println("seq:::" + seq);

		Map<String, Object> map = new HashMap<>();
		map.put("member_email", member_email);
		map.put("home_seq", seq);
		List<PaymentDTO> plist = homeService.getAllPayment(map);
		List<HomeDTO> hlist = homeService.getAllHomeData(member_email);
		HomeDTO hdto = homeService.getHomeData(seq);
		int price = 0;

		// 날짜 검색
		String sm = request.getParameter("startmon");
		String sy = request.getParameter("startyear");
		String em = request.getParameter("endmon");
		String ey = request.getParameter("endyear");

		if (sm != null) {
			if (Integer.parseInt(sm) < 10) {
				sm = "0" + sm;
			}
		}
		if (em != null) {
			if (Integer.parseInt(em) < 10) {
				em = "0" + em;
			}
		}
		String start = sy + sm + "01";
		String end = ey + em + "30";
		System.out.println("start::end" + start + "::" + end);

		Map<String, Object> dmap = new HashMap<>();
		dmap.put("start", start);
		dmap.put("end", end);
		dmap.put("home_seq", seq);
		dmap.put("host_email", member_email);

		List<PaymentDTO> dateList = new ArrayList<>();
		dateList = homeService.getGapDate(dmap);
		for (PaymentDTO p : dateList) {
			System.out.println("dateList::" + p.getPayment_amount());
		}

		// 년월일 만들기
		SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy년 MM월 dd일");

		Calendar cal = Calendar.getInstance();

		for (PaymentDTO p : plist) {
			Date to1 = fm1.parse(p.getCheckOut());
			cal.setTime(to1);
			cal.add(Calendar.DATE, 1);

			String str1 = fm2.format(cal.getTime());
			p.setReceiveDate(str1);

			Date to2 = fm1.parse(p.getCheckIn());
			String str2 = fm2.format(to2);
			p.setCheckIn(str2);

			Date to3 = fm1.parse(p.getCheckOut());
			String str3 = fm2.format(to3);
			p.setCheckOut(str3);

			System.out.println("앙배불띠띠::" + p.getCheckOut());
			for (PaymentDTO p1 : plist) {
				System.out.println("payseq:::" + p1.getPayment_seq());
			}
			price += Integer.parseInt(p.getPayment_amount());
		}
		String amount = String.valueOf(price);
		ModelAndView mav = new ModelAndView();
		mav.addObject("amount", amount);
		mav.addObject("plist", plist);
		mav.addObject("hlist", hlist);
		mav.addObject("dateList", dateList);
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostHomePaymentBreakdown");
		return mav;
	}

	@RequestMapping("/hostReserveModifyRule.do")
	public ModelAndView toHostReserveModifyRule(int seq, HomeDTO hdto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("hostReserveModifyRule: " + seq);

		hdto = homeService.getHomeData(seq);

		String str = hdto.getHome_details();
		System.out.println(str);
		String arr[] = {};

		String tmp1 = "";
		String tmp2 = "";
		String tmp3 = "";
		String tmp4 = "";
		String tmp5 = "";

		if (str != null) {
			arr = str.split(",");

			for (int i = 0; i < arr.length; i++) {
				if (arr[i].contains("소음이 발생할")) {
					tmp1 = arr[i].split(":")[1];
				} else if (arr[i].contains("숙소에 반려동물 ")) {
					tmp2 = arr[i].split(":")[1];
				} else if (arr[i].contains("주차 불가")) {
					tmp3 = arr[i].split(":")[1];
				} else if (arr[i].contains("공용 공간")) {
					tmp4 = arr[i].split(":")[1];
				} else if (arr[i].contains("편의시설")) {
					tmp5 = arr[i].split(":")[1];
				}
			}
			System.out.println("tmp1:" + tmp1);
			System.out.println("tmp2:" + tmp2);
			System.out.println("tmp3:" + tmp3);
			System.out.println("tmp4:" + tmp4);
			System.out.println("tmp5:" + tmp5);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("tmp1", tmp1);
		mav.addObject("tmp2", tmp2);
		mav.addObject("tmp3", tmp3);
		mav.addObject("tmp4", tmp4);
		mav.addObject("tmp5", tmp5);
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostReserveModifyRule");

		return mav;
	}

	@RequestMapping("/hostReserveModifyRuleProc.do")
	public ModelAndView toHostReserveModifyRuleProc(HomeDTO hdto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("hostReserveModifyRuleProc: " + hdto.getHome_seq());

		// 라디오
		String rules1 = "";
		String rules2 = "";
		String rules3 = "";
		String rules4 = "";
		String rules5 = "";

		if (request.getParameter("rules1") == null) {

		} else {
			rules1 = request.getParameter("rules1");
		}
		if (request.getParameter("rules2") == null) {

		} else {
			rules2 = request.getParameter("rules2");
		}
		if (request.getParameter("rules3") == null) {

		} else {
			rules3 = request.getParameter("rules3");
		}
		if (request.getParameter("rules4") == null) {

		} else {
			rules4 = request.getParameter("rules4");
		}
		if (request.getParameter("rules5") == null) {

		} else {
			rules5 = request.getParameter("rules5");
		}

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
		String check2 = "";
		String check3 = "";
		String check4 = "";
		String check5 = "";
		String check6 = "";

		String check2_text = "";
		String check3_text = "";
		String check4_text = "";
		String check5_text = "";
		String check6_text = "";

		if (request.getParameter("check2") == null) {
		} else {
			check2 = request.getParameter("check2");
		}

		if (request.getParameter("check3") == null) {
		} else {
			check3 = request.getParameter("check3");
		}

		if (request.getParameter("check4") == null) {
		} else {
			check4 = request.getParameter("check4");
		}

		if (request.getParameter("check5") == null) {
		} else {
			check5 = request.getParameter("check5");
		}

		if (request.getParameter("check6") == null) {
		} else {
			check6 = request.getParameter("check6");
		}

		if (request.getParameter("check2-text") == null) {
		} else {
			check2_text = request.getParameter("check2-text");
		}
		if (request.getParameter("check3-text") == null) {
		} else {
			check3_text = request.getParameter("check3-text");
		}

		if (request.getParameter("check4-text") == null) {
		} else {
			check4_text = request.getParameter("check4-text");
		}

		if (request.getParameter("check5-text") == null) {
		} else {
			check5_text = request.getParameter("check5-text");
		}
		if (request.getParameter("check6-text") == null) {
		} else {
			check6_text = request.getParameter("check6-text");
		}

		System.out.println("check-text2::" + check2 + "::" + check2_text);
		System.out.println("check-text3::" + check3 + "::" + check3_text);
		System.out.println("check-text4::" + check4 + "::" + check4_text);
		System.out.println("check-text5::" + check5 + "::" + check5_text);
		System.out.println("check-text6::" + check6 + "::" + check6_text);

		ArrayList<String> list2 = new ArrayList<>();

		if (check2 == "") {
			check2 = "";
		} else if (check2 != "" && check2_text != "") {
			check2 = check2 + ":" + check2_text;
			list2.add(check2);
		} else if (check2 != "" && check2_text == "") {
			list2.add(check2);
		}

		if (check3 == "") {
			check3 = "";
		} else if (check3 != "" && check3_text != "") {
			check3 = check3 + ":" + check3_text;
			list2.add(check3);
		} else if (check3 != "" && check3_text == "") {
			list2.add(check3);
		}

		if (check4 == "") {
			check4 = "";
		} else if (check4 != "" && check4_text != "") {
			check4 = check4 + ":" + check4_text;
			list2.add(check4);
		} else if (check4 != "" && check4_text == "") {
			list2.add(check4);
		}

		if (check5 == "") {
			check5 = "";
		} else if (check5 != "" && check5_text != "") {
			check5 = check5 + ":" + check5_text;
			list2.add(check5);
		} else if (check5 != "" && check5_text == "") {
			list2.add(check5);
		}

		if (check6 == "") {
			check6 = "";
		} else if (check6 != "" && check6_text != "") {
			check6 = check6 + ":" + check6_text;
			list2.add(check6);
		} else if (check6 != "" && check6_text == "") {
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
	public ModelAndView toHostHomeList(HttpSession session) throws Exception {
		System.out.println("/hostHomeList:");

		// 세션에서 아이디 꺼내기
		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";
		List<HomeDTO> list = homeService.getAllHomeData(member_email);

		System.out.println("list.size::" + list.size());

		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.setViewName("/host/hostHomeList");
		return mav;
	}

	@RequestMapping("/hostReserveAllManaging.do")
	public ModelAndView toHostReserveAllManaging(HttpSession session) throws Exception {
		System.out.println("/hostReserveAllManaging:");

		// 세션에서 아이디 꺼내기
		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";
		int state = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("member_email", member_email);
		map.put("reserv_state", state);

		List<ReservationDTO> rlist = homeService.getAllReservation(member_email);
		List<ReservationDTO> wlist = homeService.getWaitReserve(map);

		Date date = new Date();

		for (int i = 0; i < wlist.size(); i++) {
			long gap = date.getTime() - wlist.get(i).getReserv_countdown();
			for (int j = 0; j < rlist.size(); j++) {
				if (rlist.get(j).getReservation_seq() == wlist.get(i).getReservation_seq()) {
					rlist.get(j).setReserv_countdown(gap);
				}
			}
		}

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
	public ModelAndView toHostReserveManaging(HttpSession session) throws Exception {
		System.out.println("/hostReserveManaging:");

		// 세션에서 아이디 꺼내기
		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";
		int state = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("member_email", member_email);
		map.put("reserv_state", state);

		List<ReservationDTO> rlist = homeService.getAllReservation(member_email);
		List<ReservationDTO> wlist = homeService.getWaitReserve(map);

		Date date = new Date();

		for (int i = 0; i < wlist.size(); i++) {
			long gap = date.getTime() - wlist.get(i).getReserv_countdown();
			for (int j = 0; j < rlist.size(); j++) {
				if (rlist.get(j).getReservation_seq() == wlist.get(i).getReservation_seq()) {
					rlist.get(j).setReserv_countdown(gap);
				}
			}
		}

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

	@RequestMapping("savetime.do")
	public void toSavetiome(int seq, HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("/savetime.do:" + seq);

		String member_email = (String) session.getAttribute("login_email");
		int state = 0;
		int result = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("member_email", member_email);
		map.put("reserv_state", state);

		List<ReservationDTO> rlist = homeService.getWaitReserve(map);

		Date date = new Date();
		long getTime = date.getTime();
		System.out.println("getTime::" + getTime);

		for (int i = 0; i < rlist.size(); i++) {
			result = homeService.modifyCountdown(getTime, rlist.get(i).getReservation_seq());
		}

		JSONObject json = new JSONObject();
		json.put("result", result);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print(json);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("modifyReservState.do")
	public void toModifyReservState(int seq, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("/modifyReservState.do:" + seq);

		String member_email = (String) session.getAttribute("login_email");

		int result = homeService.modifyReservState(seq);

		JSONObject json = new JSONObject();
		json.put("result", result);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print(json);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("hostHits.do")
	public ModelAndView toHostHits(HttpServletRequest request, HttpSession session) throws Exception {
		System.out.println("hostHists.do/");

		int home_seq = 0;
		String member_email = (String) session.getAttribute("login_email");
		member_email = "sksksrff@gmail.com";
		System.out.println("member::" + member_email);

		Calendar cal = new GregorianCalendar(Locale.KOREA);
		int month = cal.get(Calendar.MONTH) + 1;

		HomeDTO hdto = new HomeDTO();
		List<HomeDTO> hlist = homeService.getAllHomeData(member_email);

		if (request.getParameter("seq") == null) {
			hdto = homeService.getOldestHomeData(member_email);
			System.out.println("아래::" + hdto.getHome_seq());
		} else if (Integer.parseInt(request.getParameter("seq")) != 0) {
			home_seq = Integer.parseInt(request.getParameter("seq"));
			hdto = homeService.getHomeData(home_seq);
			System.out.println("위::" + home_seq);
		}

		if(hdto.getHome_addr2() != null) {
			hdto.setHome_addr2(hdto.getHome_addr2().split(" ")[0]);
		}
		List<HomeDTO> siList = homeService.getSimilarHome(hdto);
		System.out.println("asd::" + hdto.getHome_addr2());
		for (HomeDTO dto : siList) {
			System.out.println("dto::" + dto);
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.addObject("month", month);
		mav.addObject("siList", siList);
		mav.addObject("hlist", hlist);
		mav.setViewName("/host/hostHits");
		return mav;
	}

	@RequestMapping("hostModifyPriceTab.do")
	public ModelAndView toHostModifyPriceTab(int seq, HttpSession session) throws Exception {
		System.out.println("hostModifyPriceTab::");

		HomeDTO hdto = homeService.getHomeData(seq);

		ModelAndView mav = new ModelAndView();
		mav.addObject("hdto", hdto);
		mav.setViewName("/host/hostModifyPriceTab");
		return mav;
	}

	@RequestMapping("hostModifyPriceProc.do")
	public ModelAndView toHostModifyPriceProc(int seq, HomeDTO hdto) throws Exception {
		System.out.println("hostModifyPriceProc::");
		System.out.println(hdto.getHome_seq());
		System.out.println(hdto.getHome_price());

		hdto.setHome_seq(seq);
		int result = homeService.modifyHomePrice(hdto);

		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.addObject("seq", seq);
		mav.setViewName("/host/hostModifyPriceProc");
		return mav;
	}

	@RequestMapping("hostLogout.do")
	public ModelAndView toHostLogout(HttpSession session) throws Exception {
		System.out.println("logout::");

		session.removeAttribute("login_email");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("/host/hostLogout");
		return mav;
	}

	// total 함수
	public double getTotal(int home_seq, String member_email) {
		System.out.println("getTotal:::" + home_seq + " , " + member_email);
		List<GuestReviewDTO> satisList = homeService.getSatisfaction(home_seq);
		List<GuestReviewDTO> accList = homeService.getAccuracy(home_seq);
		List<GuestReviewDTO> cleanList = homeService.getCleanLiness(home_seq);
		List<GuestReviewDTO> checkList = homeService.getCheckin(home_seq);
		List<GuestReviewDTO> ameniList = homeService.getAmenities(home_seq);
		List<GuestReviewDTO> commList = homeService.getCommunication(home_seq);
		List<GuestReviewDTO> locList = homeService.getLocation(home_seq);
		List<GuestReviewDTO> valList = homeService.getValue(home_seq);

		List<Integer> list_satis = new ArrayList<>();
		List<Integer> list_acc = new ArrayList<>();
		List<Integer> list_clean = new ArrayList<>();
		List<Integer> list_check = new ArrayList<>();
		List<Integer> list_ameni = new ArrayList<>();
		List<Integer> list_comm = new ArrayList<>();
		List<Integer> list_loc = new ArrayList<>();
		List<Integer> list_val = new ArrayList<>();

		List<Integer> numList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
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
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// accuracy
		for (int i = 0; i < accList.size(); i++) {
			list_acc.add(accList.get(i).getG_review_accuracy());
		}
		numList.removeAll(list_acc);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_accuracy(numList.get(i));
			dto.setCount(0);
			accList.add(dto);
		}
		// accuracy
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// clean
		for (int i = 0; i < cleanList.size(); i++) {
			list_clean.add(cleanList.get(i).getG_review_cleanliness());
		}
		numList.removeAll(list_clean);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_cleanliness(numList.get(i));
			;
			dto.setCount(0);
			cleanList.add(dto);
		}
		// clean
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// check
		for (int i = 0; i < checkList.size(); i++) {
			list_check.add(checkList.get(i).getG_review_checkIn());
		}
		numList.removeAll(list_check);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_checkIn(numList.get(i));
			dto.setCount(0);
			checkList.add(dto);
		}
		// check
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// amenities
		for (int i = 0; i < ameniList.size(); i++) {
			list_ameni.add(ameniList.get(i).getG_review_amenities());
		}
		numList.removeAll(list_ameni);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_amenities(numList.get(i));
			dto.setCount(0);
			ameniList.add(dto);
		}
		// amenities
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// comm
		for (int i = 0; i < commList.size(); i++) {
			list_comm.add(commList.get(i).getG_review_communication());
		}
		numList.removeAll(list_comm);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_communication(numList.get(i));
			dto.setCount(0);
			commList.add(dto);
		}
		// comm
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// local
		for (int i = 0; i < locList.size(); i++) {
			list_loc.add(locList.get(i).getG_review_location());
		}
		numList.removeAll(list_loc);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_location(numList.get(i));
			dto.setCount(0);
			locList.add(dto);
		}
		// local
		numList.clear();
		for (int i = 0; i < 5; i++) {
			numList.add(i + 1);
		}
		// val
		for (int i = 0; i < valList.size(); i++) {
			list_val.add(valList.get(i).getG_review_value());
		}
		numList.removeAll(list_val);
		for (int i = 0; i < numList.size(); i++) {
			GuestReviewDTO dto = new GuestReviewDTO();
			dto.setG_review_value(numList.get(i));
			dto.setCount(0);
			valList.add(dto);
		}

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

		HashMap<String, Object> map = new HashMap<>();
		map.put("member_email", member_email);
		map.put("home_seq", home_seq);

		int cnt = homeService.guestReviewCount(map);

		// 정렬
		Collections.sort(satisList);
		Collections.sort(accList);
		Collections.sort(cleanList);
		Collections.sort(checkList);
		Collections.sort(ameniList);
		Collections.sort(commList);
		Collections.sort(locList);
		Collections.sort(valList);

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

		return allTotal;
	}

}