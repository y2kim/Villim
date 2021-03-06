package kh.spring.controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kh.spring.dto.DetailDTO;
import kh.spring.dto.GuestMsgDTO;
import kh.spring.dto.HomeDTO;
import kh.spring.dto.MailSendDTO;
import kh.spring.dto.MemberDTO;
import kh.spring.dto.MessageDTO;
import kh.spring.dto.MessageRoomDTO;
import kh.spring.dto.ReservationDTO;
import kh.spring.interfaces.HomeService;
import kh.spring.interfaces.MemberService;
import kh.spring.interfaces.MessageService;

@Controller
public class MessageController {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	MessageService service;

	@Autowired
	MemberService m_service;

	@Autowired
	private HomeService homeService;

	StringBuilder builder = new StringBuilder();

	@RequestMapping("/messageMain.msg")
	public ModelAndView main(HttpSession session,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);
		System.out.println("messageMain");
	
		String userId = (String) session.getAttribute("login_email");

		System.out.println("아이디 :" + userId);

		// 여행
		List<GuestMsgDTO> guestMessage = this.service.guestMessageMain(userId);
		List<String> host_email = new ArrayList<>();
		System.out.println("총개수 :" + guestMessage.size());
		if (!guestMessage.isEmpty()) {
			for (GuestMsgDTO tmp : guestMessage) {
				System.out.println("메세지방번호 :  " + tmp.getMessage_room_seq() + "메세지 시퀸스 : " + tmp.getMessage_seq()
						+ "메세지 내용 : " + tmp.getMessage_content() + "메일 : " + tmp.getHost_email() + "날짜 :"
						+ tmp.getMessage_time() + " fromID : " + tmp.getFromID() + "toID : " + tmp.getToID());

				if (today.equals(tmp.getMessage_time().substring(0, 13))) {
					System.out.println("근꼐 오늘 같다는겨?");
					tmp.setMessage_time(tmp.getMessage_time().substring(15, 21));
				} else {
					tmp.setMessage_time(tmp.getMessage_time().substring(7, 21));
				}
				

				host_email.add(tmp.getHost_email());
			}

			List<MemberDTO> hostMemberInfo = this.service.memberInfo(host_email);
           
			mav.addObject("guestMessage", guestMessage);
			mav.addObject("hostMemberInfo", hostMemberInfo);
			for (MemberDTO tmp : hostMemberInfo) {
				
				/*if(tmp.getMember_location().equals("null")) {
					tmp.setMember_location("");
				}*/
				System.out.println("멤버이름: " + tmp.getMember_name() + "멤버 사진 : " + tmp.getMember_picture()+"위치 : "+tmp.getMember_location());
			}

			// 모든개수
			int guestMsgAllCount = this.service.guestMsgAllCount(userId);
			if (guestMsgAllCount > 0) {
				System.out.println("모든개수 :" + guestMsgAllCount);
				mav.addObject("guestMsgAllCount", guestMsgAllCount);
			} else {
				System.out.println("모든개수 없음");
				mav.addObject("guestMsgAllCount", 0);
			}
			// 읽지않은개수
			int guestMsgUnreadCount = this.service.guestMsgUnreadCount(userId);
			if (guestMsgUnreadCount > 0) {
				System.out.println("읽지않은 개수 :" + guestMsgUnreadCount);
				mav.addObject("guestMsgUnreadCount", guestMsgUnreadCount);
			} else {
				System.out.println("읽지않은개수 없음");
				mav.addObject("guestMsgUnreadCount", 0);
			}

		} else {
			System.out.println("guest메세지 없음 !!!!!!!!!!");
			mav.addObject("guestMsgAllCount", 0);
			mav.addObject("guestMsgUnreadCount", 0);
		}

		// 호스팅
		List<GuestMsgDTO> hostMessage = this.service.hostMessageMain(userId);
		List<String> guest_email = new ArrayList<>();

		if (!hostMessage.isEmpty()) {
			for (GuestMsgDTO tmp : hostMessage) {
				System.out.println("호스트메세지방번호 :  " + tmp.getMessage_room_seq() + "메세지 시퀸스 : " + tmp.getMessage_seq()
						+ "메세지 내용 : " + tmp.getMessage_content() + "메일 : " + tmp.getHost_email() + "게스트메일:"
						+ tmp.getGuest_email() + "날짜 :" + tmp.getMessage_time() + "읽음 여부 :" + tmp.getMessage_read());

				guest_email.add(tmp.getGuest_email());

				String messageDate = tmp.getMessage_time();
				System.out.println("날짜 : " + messageDate.substring(0, 13));
				if (today.equals(messageDate.substring(0, 13))) {
					tmp.setMessage_time(tmp.getMessage_time().substring(15, 21));
					System.out.println("수정된 날짜 : " + tmp.getMessage_time());
				} else {

					tmp.setMessage_time(messageDate.substring(7, 21));

					System.out.println("수정된 날짜 : " + tmp.getMessage_time());
				}

			}

			List<MemberDTO> guestMemberInfo = this.service.memberInfo(guest_email);

			mav.addObject("hostMessage", hostMessage);
			mav.addObject("guestMemberInfo", guestMemberInfo);
			for (MemberDTO tmp : guestMemberInfo) {
				
				System.out.println("멤버이름: " + tmp.getMember_name() + "멤버 사진 : " + tmp.getMember_picture()+"위치 : "+tmp.getMember_location());
			}

			// 모든 개수
			int hostMsgAllCount = this.service.hostMsgAllCount(userId);
			if (hostMsgAllCount > 0) {
				System.out.println("모든개수 :" + hostMsgAllCount);
				mav.addObject("hostMsgAllCount", hostMsgAllCount);
			} else {
				mav.addObject("hostMsgAllCount", 0);
			}

			// 읽지않은개수
			int hostMsgUnreadCount = this.service.hostMsgUnreadCount(userId);
			if (hostMsgUnreadCount > 0) {
				System.out.println("읽지않은개수 :" + hostMsgUnreadCount);
				mav.addObject("hostMsgUnreadCount", hostMsgUnreadCount);
			} else {
				System.out.println("읽지않은개수 없음");
				mav.addObject("hostMsgUnreadCount", 0);
			}

		} else {
			System.out.println("host메세지 없음 !!!!!!!!!!");
			mav.addObject("hostMsgAllCount", 0);
			mav.addObject("hostMsgUnreadCount", 0);
		}

		mav.addObject("userId", userId);
		mav.setViewName("/message/messageMain");
		return mav;
	}

	@RequestMapping("/messageSend.msg")
	public ModelAndView messageSend(HttpSession session,HttpServletRequest req) {
		System.out.println("messageSend");
		String userId = (String) session.getAttribute("login_email");
		int home_seq =Integer.parseInt(req.getParameter("home_seq"));
		String host_name = req.getParameter("host_name");
		String host_picture = req.getParameter("host_picture");
		int home_price =Integer.parseInt(req.getParameter("home_price"));
		String home_type = req.getParameter("home_type");
		String home_main_pic = req.getParameter("home_main_pic");
          System.out.println("home_seq : "+home_seq+" /host_name : "+host_name+" /host_picture :"+host_picture+" /home_price : "+home_price+" / home_type : "+home_type + " /home_main_pic : "+home_main_pic);
		// review 갯수
		int reviewCount = this.service.countReview(home_seq);

		String Q1 = req.getParameter("home_guest_access");
		String Q2 = req.getParameter("home_details")+req.getParameter("home_rules");
		String Q3 = req.getParameter("home_policy");

		ModelAndView mav = new ModelAndView();
		mav.addObject("host_name", host_name);
		mav.addObject("host_picture", host_picture);
		mav.addObject("home_price", home_price);
		mav.addObject("home_type", home_type);
		mav.addObject("home_main_pic", home_main_pic);
		mav.addObject("reviewCount", reviewCount);
		mav.addObject("Q1", Q1);
		mav.addObject("Q2", Q2);
		mav.addObject("Q3", Q3);
		mav.setViewName("/message/messageSend");
		return mav;
	}

	@RequestMapping("/messageInsertDB.msg")
	public ModelAndView messageInsertDB(HttpSession session, HttpServletResponse response, String host_name,
			MessageDTO dto, MessageRoomDTO roomdto, String seq, String time, String number)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		System.out.println("messageInsertDB");
		System.out.println("내용 : " + dto.getMessage_content());
		String userId = (String) session.getAttribute("login_email");
        System.out.println("날짜 : "+time);
        System.out.println("인원수" +number);
		int home_seq = Integer.parseInt(seq);
		HomeDTO getHomeInfo = this.service.getHomeInfo(home_seq);
		String host_email = getHomeInfo.getMember_email();
     
		// 1. 메세지 룸 seq 가 존재하는지 여부 판단후 있을 경우 기존의 seq 넣어주고, 없을 경우 새로운 seq 넣어주기
		roomdto.setHost_email(host_email);
		roomdto.setGuest_email(userId);
		roomdto.setHome_seq(home_seq);
		MessageRoomDTO messageRoomSeqExist = this.service.messageRoomSeqExist(roomdto);
		int message_room_seq = 0;
		if (messageRoomSeqExist != null) {
			message_room_seq = messageRoomSeqExist.getMessage_room_seq();
			System.out.println("msgroom정보 이미 존재");
		} else {
			int messageRoomSeq = this.service.getRoomSeq();
			message_room_seq = messageRoomSeq;

			roomdto.setMessage_room_seq(message_room_seq);
			roomdto.setHome_seq(home_seq);
			roomdto.setHost_email(host_email);
			roomdto.setGuest_email(userId);
			
			
			System.out.println("checkin1 : "+time.split(" ~ ")[0]);
			System.out.println("checkout1 : "+time.split(" ~ ")[1]);
			String in=time.split(" ~ ")[0];
			String out=time.split(" ~ ")[1];
			mav.addObject("checkin1", time.split(" ~ ")[0]);
			mav.addObject("checkout1", time.split(" ~ ")[1]);
			String checkin=in.split("-")[1]+"월"+" "+in.split("-")[2]+"일";
			String checkOut=out.split("-")[1]+"월"+" "+out.split("-")[2]+"일";
			System.out.println("checkin2 : "+checkin);
			System.out.println("checkout2 : "+checkOut);
			roomdto.setCheckIn(checkin);
			roomdto.setCheckOut(checkOut);
			roomdto.setTotalNumber(Integer.parseInt(number));
			int messageInfoInsert = this.service.messageRoomInsert(roomdto);
			if (messageInfoInsert > 0) {
				System.out.println("msgroom정보 입력에 성공!");
			}

		}

		System.out.println("message_room_seq= " + message_room_seq);
        
		dto.setMessage_room_seq(message_room_seq);
		dto.setHome_seq(home_seq);
		dto.setFromID(userId);
		dto.setToID(host_email);

		// 2. 얻어낸 메세지 룸 seq와 함께 메세지테이블에 데이터 넣기
		int messageInsertResult = this.service.messageInsert(dto);

		if (messageInsertResult > 0) {
			System.out.println("메세지 전송 완료!");

			// 실제 메세지 보내기
			MemberDTO mGuest = this.m_service.printProfile(userId);
			MemberDTO mHost = this.m_service.printProfile(host_email);

			
			MailSendDTO mailDto = new MailSendDTO(mailSender);
			String mail = mHost.getMember_email();
			System.out.println(mail);
			System.out.println("멤버 사진 : "+mGuest.getMember_picture());
			String urls = "<div style=\"heigh:100%;width:100%;height:45vw;\">" + 
					"<img src=\"logo2.png/>\" style=\"position:relative;left:6vw;top:4vh;\">" + 
					"<div style=\"position:relative;color:#515151;width:100%;height:auto;top:5vh;\">" + 
					"<h3 style=\"position:relative;left:6vw; \">"+mGuest.getMember_name()+"님의 문의에 답하세요</h3>" + 
					"<img style=\"width:4vw;height:8.5vh;margin: 0 auto 10px;display: block;-moz-border-radius: 50%;-webkit-border-radius: 50%;border-radius: 50%;\" src=\"files/"+mGuest.getMember_picture()+" class=\"img-circle img-responsive\">" + 
					"<h4 style=\"position:relative;left:12vw;top:-10vh;\">"+mGuest.getMember_name()+"</h4>" + 
					"<h4 style=\"position:relative;left:12vw;top:-11.4vh;font-weight:400;\">"+mGuest.getMember_location()+"</h4>" + 
					"<div style=\"position:relative; min-height:7vh;display: block;left:6vw;padding-bottom:9vh;height:100%;top:-8vh;width:75%;background:#f4f4f4;border:1px solid #f4f4f4; border-radius: 8px;\">" + 
					"<h4 style=\"position:relative;font-weight:500;width:33vw;height:auto;top:5vh;left:2vw;line-height:3vh;margin:0;\">"+dto.getMessage_content()+"</h4>" + 
					"</div>" + 
					"<h4 style=\"position:relative;top:-7vh;left:7vw;font-weight:100;\">빌림을 통해서는 절대 직접 송금하실 필요가 없습니다. </h4><a href=\"https://www.airbnb.co.kr/help/article/209/why-should-i-pay-and-communicate-through-airbnb-directly\" style=\"color:#ff5a5f;font-weight:500;text-decoration:none;position:relative;left:33vw;top:-12.8vh;\">자세히 알아보기</a>" + 
					/*"<button style=\"position:relative;width:75%;top:-10vh;left:5vw;height:7vh;box-sizing: border-box;appearance: none;background-color: transparent;" + 
					"  border: 2px solid #ff5a5f;" + 
					"  border-radius: 0.6em;" + 
					"  color: #ff5a5f;" + 
					"  cursor: pointer;" + 
					"  display: flex;" + 
					"  align-self: center;" + 
					"  font-size: 1rem;" + 
					"  font-weight: 400;" + 
					"  line-height: 1;" + 
					"  margin: 20px;" + 
					"  padding: 1.2em 2.8em;" + 
					"  text-decoration: none;" + 
					"  text-align: center;" + 
					"  text-transform: uppercase;" + 
					"  font-weight: 700; onclick=\"window.location.href='http://localhost:8080/messageMain.msg?loginId="+dto.getToID()+"';><span style=\"position:relative;left:20vw;top:-0.5vh;\">답장 보내기</span></button>" + */
					"<h6 style=\"font-size:7px;font-weight:500;position:relative;left:7vw;top:-8vh;\">"+mGuest.getMember_name()+"님께 메시지를 보내려면 본 이메일에 회신하세요. </h6>" + 
					"<hr style=\"margin-top:0;margin-left:0;padding:0;width:68%;color:#d6d4d4;background:#d6d4d4;border:0.1px solid #d6d4d4;size:0.1;\">" + 
					"<h5 style=\"color:#d6d4d4;position:relative;left:7vw;\">" + 
					"빌림 드림 ♥<br>" + 
					"‌서울특별시 영등포구 선유동2로 57 이레빌딩‌</h5>" + 
					"</div>" +"</div>";
			

			
			try {
			
			mailDto.setSubject("[Villim] "+mGuest.getMember_name()+"님의 문의 입니다.");
			mailDto.setText(urls);
			mailDto.setFrom("villim.cf", "villim.cf");
			mailDto.setTo(mail);
			mailDto.send();
			System.out.println("메일보내기 성공");

			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			
			
			DetailDTO getMessageAfterSend = this.service.getMsgAfterSend(dto.getMessage_seq());
             System.out.println("message_seq : "+dto.getMessage_seq());
			String to = "82" + mHost.getMember_phone();
			String from = "33644643087";
			String message = URLEncoder.encode("[Villim] : " + mGuest.getMember_name() + ", "
					+ getMessageAfterSend.getCheckIn() + " - " + getMessageAfterSend.getCheckOut() + ", '"
					+ getMessageAfterSend.getMessage_content() + "'", "UTF-8");
			String sendUrl = "https://www.proovl.com/api/send.php?user=6394162&token=mZJb0hlGqKxlgbpx4GqNTH4lX0aNAQ04";

			StringBuilder sb = new StringBuilder();
			sb.append(sendUrl);
			sb.append("&to=" + to);
			sb.append("&from=" + from);
			sb.append("&text=" + message);

			System.out.println(sb.toString());

			try {
				URL url = new URL(sb.toString());
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				int result = con.getResponseCode();
				System.out.println(result);
				con.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//

			mav.addObject("host_name", host_name);
			mav.setViewName("/message/messageInsertConfirm");
		} else {
			mav.setViewName("error");
		}

		return mav;
	}

	@RequestMapping("/messageRoomEnter.msg")
	public ModelAndView messageRoomEnter(HttpSession session, int message_room_seq, int home_seq,String member_email, int message_seq) {
		ModelAndView mav = new ModelAndView();
		System.out.println("messageRoomEnter");
		System.out.println("room_seq : " + message_room_seq);
		System.out.println("message_seq : " + message_seq);
		System.out.println("member_email : " + member_email);
		String userId = (String) session.getAttribute("login_email");

		 int readUpdate=this.service.ReadUpdate(message_seq, member_email, userId);

		MemberDTO guestInfo = this.m_service.getPhoto(userId);
		MemberDTO hostInfo = this.m_service.getPhoto(member_email);
		mav.addObject("userId", userId);
		mav.addObject("message_room_seq", message_room_seq);
		mav.addObject("home_seq", home_seq);
		mav.addObject("guest_picture", guestInfo.getMember_picture());
		mav.addObject("host_picture", hostInfo.getMember_picture());
		mav.addObject("host_name", hostInfo.getMember_name());
		HomeDTO hdto = homeService.getHomeData(home_seq);
		mav.addObject("home_location", hdto.getHome_nation() + " " + hdto.getHome_addr1() + " " + hdto.getHome_addr3());
		mav.addObject("home_price", hdto.getHome_price());

		mav.addObject("host_email", member_email);
		MessageRoomDTO dto = this.service.msgRoomInfo(message_room_seq);
		String cI = "2018" + dto.getCheckIn().split("월")[0] + dto.getCheckIn().split("일")[0].split("월")[1];
		String cO = "2018" + dto.getCheckOut().split("월")[0] + dto.getCheckOut().split("일")[0].split("월")[1];
		String transCI = "2018-" + dto.getCheckIn().split("월")[0] + "-" + dto.getCheckIn().split("일")[0].split("월")[1];
		String transCO = "2018-" + dto.getCheckOut().split("월")[0] + "-"
				+ dto.getCheckOut().split("일")[0].split("월")[1];
		System.out.println("체크인 시간 : " + cI + " 체크아웃시간: " + cO);
		mav.addObject("splitCheckIn", transCI);
		mav.addObject("splitCheckOut", transCO);
		int amount = hdto.getHome_price();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);
		
		 //개수
		int guestMsgUnreadCount = this.service.guestMsgUnreadCount(userId);
		if (guestMsgUnreadCount > 0) {
			System.out.println("읽지않은 개수 :" + guestMsgUnreadCount);
			mav.addObject("guestMsgUnreadCount", guestMsgUnreadCount);
		} else {
			System.out.println("읽지않은개수 없음");
			mav.addObject("guestMsgUnreadCount", 0);
		}

int hostMsgUnreadCount = this.service.hostMsgUnreadCount(userId);
		if (hostMsgUnreadCount > 0) {
			System.out.println("읽지않은개수 :" + hostMsgUnreadCount);
			mav.addObject("hostMsgUnreadCount", hostMsgUnreadCount);
		} else {
			System.out.println("읽지않은개수 없음");
			mav.addObject("hostMsgUnreadCount", 0);
		}
		
		try {
			Date checkIn = sdf2.parse(cI);
			Date checkOut = sdf2.parse(cO);
			long diffDay = (checkOut.getTime() - checkIn.getTime()) / (24 * 60 * 60 * 1000);
			int stayPrice = (int) (amount * diffDay);
			int home_servicefee = (int) (stayPrice * 0.05);
			int home_cleaningfee = (int) (stayPrice * 0.1);
			System.out.println(diffDay + "박");

			int totalPrice = (int) (stayPrice + (hdto.getHome_price() * 0.05) + (hdto.getHome_price() * 0.1));
			mav.addObject("home_servicefee", home_servicefee);
			mav.addObject("home_cleaningfee", home_cleaningfee);
			mav.addObject("totalPrice", totalPrice);
			mav.addObject("stayPrice", stayPrice);
			mav.addObject("diffDay", diffDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<MessageDTO> message = this.service.getMessage(message_room_seq);

		for (MessageDTO tmp : message) {
			if (today.equals(tmp.getMessage_time().substring(0, 13))) {
				tmp.setMessage_time(tmp.getMessage_time().substring(14, 20) + "분");
			} else {
				tmp.setMessage_time(tmp.getMessage_time().substring(7, 21));
			}
		}

		mav.addObject("message", message);
		for (MessageDTO tmp : message) {
			System.out.println(tmp.getMessage_content() + " / " + tmp.getMessage_time());
		}

		ReservationDTO dto2 = new ReservationDTO();
		dto2.setMember_email(userId);
		dto2.setHome_seq(home_seq);
        dto2.setHost_email(member_email);
       
		List<ReservationDTO> reservCheck = this.service.reservCheck(dto2);
		if (!reservCheck.isEmpty()) {
			for (ReservationDTO tmp : reservCheck) {
				System.out.println("예약이미 신청 = " + tmp.getReserv_state()+"total Amount : "+tmp.getTotalAmount());
			}

			mav.addObject("reservCheck", reservCheck);
		} else {
			System.out.println("예약을 안함 아직");
		}

		mav.addObject("msgRoom", dto);
		mav.addObject("messageRoomInfo", dto);
		mav.setViewName("/message/messageRoom");
		return mav;

	}

	@RequestMapping("/messageSendInRoom.msg")
	public void messageSendInRoom(MessageDTO dto, HttpServletResponse response,HttpServletRequest req) throws Exception {
		System.out.println("messageSendInRoom");
		System.out.println("메세지내용 : " + dto.getMessage_content());
         
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);

		int messageInsertResult = this.service.messageInsert(dto);

		System.out.println("시퀸스 : " + dto.getMessage_seq());

		MessageDTO message = this.service.getOneMessage(dto.getMessage_seq());

		System.out.println("시간 자르기 : " + message.getMessage_time().substring(0, 13));
		if (today.equals(message.getMessage_time().substring(0, 13))) {
			message.setMessage_time(message.getMessage_time().substring(14, 20).split("분")[0] + "분");
			System.out.println("message시간 : " + message.getMessage_time());
		} else {
			message.setMessage_time(message.getMessage_time().substring(7, 20));
			System.out.println("message시간 : " + message.getMessage_time());
		}

		System.out.println(message.getMessage_content() + " / " + message.getMessage_time());

        
		// 실제 메세지,메일 보내기
		MemberDTO mGuest = this.m_service.printProfile(dto.getFromID());
		MemberDTO mHost = this.m_service.printProfile(dto.getToID());

		MailSendDTO mailDto = new MailSendDTO(mailSender);
		String mail = mHost.getMember_email();
		System.out.println(mail);
		System.out.println("멤버 사진 : "+mGuest.getMember_picture());
		String urls = "<div style=\"heigh:100%;width:100%;height:45vw;\">" + 
				"<img src=\"logo2.png/>\" style=\"position:relative;left:6vw;top:4vh;\">" + 
				"<div style=\"position:relative;color:#515151;width:100%;height:auto;top:5vh;\">" + 
				"<h3 style=\"position:relative;left:6vw; \">"+mGuest.getMember_name()+"님의 메세지에 답하세요</h3>" + 
				"<img style=\"width:4vw;height:8.5vh;margin: 0 auto 10px;display: block;-moz-border-radius: 50%;-webkit-border-radius: 50%;border-radius: 50%;\" src=\"files/"+mGuest.getMember_picture()+" class=\"img-circle img-responsive\">" + 
				"<h4 style=\"position:relative;left:12vw;top:-10vh;\">"+mGuest.getMember_name()+"</h4>" + 
				"<h4 style=\"position:relative;left:12vw;top:-11.4vh;font-weight:400;\">"+mGuest.getMember_location()+"</h4>" + 
				"<div style=\"position:relative; min-height:7vh;display: block;left:6vw;padding-bottom:9vh;height:100%;top:-8vh;width:75%;background:#f4f4f4;border:1px solid #f4f4f4; border-radius: 8px;\">" + 
				"<h4 style=\"position:relative;font-weight:500;width:33vw;height:auto;top:5vh;left:2vw;line-height:3vh;margin:0;\">"+dto.getMessage_content()+"</h4>" + 
				"</div>" + 
				"<h4 style=\"position:relative;top:-7vh;left:7vw;font-weight:100;\">빌림을 통해서는 절대 직접 송금하실 필요가 없습니다. </h4><a href=\"https://www.airbnb.co.kr/help/article/209/why-should-i-pay-and-communicate-through-airbnb-directly\" style=\"color:#ff5a5f;font-weight:500;text-decoration:none;position:relative;left:33vw;top:-12.8vh;\">자세히 알아보기</a>" + 
				/*"<button style=\"position:relative;width:75%;top:-10vh;left:5vw;height:7vh;box-sizing: border-box;appearance: none;background-color: transparent;" + 
				"  border: 2px solid #ff5a5f;" + 
				"  border-radius: 0.6em;" + 
				"  color: #ff5a5f;" + 
				"  cursor: pointer;" + 
				"  display: flex;" + 
				"  align-self: center;" + 
				"  font-size: 1rem;" + 
				"  font-weight: 400;" + 
				"  line-height: 1;" + 
				"  margin: 20px;" + 
				"  padding: 1.2em 2.8em;" + 
				"  text-decoration: none;" + 
				"  text-align: center;" + 
				"  text-transform: uppercase;" + 
				"  font-weight: 700; onclick=\"window.location.href='http://localhost:8080/messageMain.msg?loginId="+dto.getToID()+"';><span style=\"position:relative;left:20vw;top:-0.5vh;\">답장 보내기</span></button>" + */
				"<h6 style=\"font-size:7px;font-weight:500;position:relative;left:7vw;top:-8vh;\">"+mGuest.getMember_name()+"님께 메시지를 보내려면 본 이메일에 회신하세요. </h6>" + 
				"<hr style=\"margin-top:0;margin-left:0;padding:0;width:68%;color:#d6d4d4;background:#d6d4d4;border:0.1px solid #d6d4d4;size:0.1;\">" + 
				"<h5 style=\"color:#d6d4d4;position:relative;left:7vw;\">" + 
				"빌림 드림 ♥<br>" + 
				"‌서울특별시 영등포구 선유동2로 57 이레빌딩‌</h5>" + 
				"</div>" +"</div>";
		

		
		try {
		
		mailDto.setSubject("[Villim] "+mGuest.getMember_name()+"님의 메세지 입니다.");
		mailDto.setText(urls);
		mailDto.setFrom("villim.cf", "villim.cf");
		mailDto.setTo(mail);
		mailDto.send();
		System.out.println("메일보내기 성공");

		}catch(Exception e) {
			e.printStackTrace();
		}

	    String to = "82" + mHost.getMember_phone();
		String from = "33644643087";
		String messages = URLEncoder.encode("[Villim]: " + mGuest.getMember_name() + ", '" + dto.getMessage_content() + "'", "UTF-8");
		String sendUrl = "https://www.proovl.com/api/send.php?user=6394162&token=mZJb0hlGqKxlgbpx4GqNTH4lX0aNAQ04";

		StringBuilder sb = new StringBuilder();
		sb.append(sendUrl);
		sb.append("&to=" + to);
		sb.append("&from=" + from);
		sb.append("&text=" + messages);

		System.out.println(sb.toString());

		try {
			URL url = new URL(sb.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int result = con.getResponseCode();
			System.out.println(result);
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		new Gson().toJson(message, response.getWriter());

	}

	@RequestMapping("/message.msg")
	public String message() {
		return "/message/NewFile";
	}
	@RequestMapping("/messageHostRoomEnter.msg")
	public ModelAndView messageHostRoomEnter(HttpSession session, int message_room_seq, int home_seq,
			String member_email, int message_seq) {
		ModelAndView mav = new ModelAndView();
		System.out.println("messageHostRoomEnter");
		System.out.println("room_seq : " + message_room_seq);
		System.out.println("home_seq : " + home_seq);
		System.out.println("message_seq" + message_seq);
		String userId = (String) session.getAttribute("login_email");

		int readUpdate=this.service.ReadUpdate(message_seq, member_email, userId);
		List<HomeDTO> getHomeNames = this.service.getHomeNames(userId);

		System.out.println("호스트 이메일 : " + userId + " / 게스트 이메일 : " + member_email);
		MemberDTO hostInfo = this.m_service.getPhoto(userId);
		MemberDTO guestInfo = this.m_service.getPhoto(member_email);
		HomeDTO hdto = homeService.getHomeData(home_seq);
		mav.addObject("message_room_seq", message_room_seq);
		mav.addObject("home_seq", home_seq);
		mav.addObject("userId", userId);
		mav.addObject("guest_email", member_email);
		mav.addObject("guest_picture", guestInfo.getMember_picture());
		mav.addObject("guest_name", guestInfo.getMember_name());
		mav.addObject("guest_location", guestInfo.getMember_location());
		mav.addObject("host_picture", hostInfo.getMember_picture());
		mav.addObject("getHomeNames", getHomeNames);
		mav.addObject("home_name", hdto.getHome_name());
		System.out.println("가격" + hdto.getHome_price());
		mav.addObject("guest_regdate", guestInfo.getMember_date());
		mav.addObject("home_price", hdto.getHome_price());

		MessageRoomDTO dto = this.service.msgRoomInfo(message_room_seq);
		String cI = "2018" + dto.getCheckIn().split("월")[0] + dto.getCheckIn().split("일")[0].split("월")[1];
		String cO = "2018" + dto.getCheckOut().split("월")[0] + dto.getCheckOut().split("일")[0].split("월")[1];
		String transCI = "2018-" + dto.getCheckIn().split("월")[0] + "-" + dto.getCheckIn().split("일")[0].split("월")[1];
		String transCO = "2018-" + dto.getCheckOut().split("월")[0] + "-"
				+ dto.getCheckOut().split("일")[0].split("월")[1];
		System.out.println("체크인 시간 : " + cI + " 체크아웃시간: " + cO);
		mav.addObject("splitCheckIn", transCI);
		mav.addObject("splitCheckOut", transCO);
		long amount = hdto.getHome_price();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);
		

        //개수
		int guestMsgUnreadCount = this.service.guestMsgUnreadCount(userId);
		if (guestMsgUnreadCount > 0) {
			System.out.println("읽지않은 개수 :" + guestMsgUnreadCount);
			mav.addObject("guestMsgUnreadCount", guestMsgUnreadCount);
		} else {
			System.out.println("읽지않은개수 없음");
			mav.addObject("guestMsgUnreadCount", 0);
		}

int hostMsgUnreadCount = this.service.hostMsgUnreadCount(userId);
		if (hostMsgUnreadCount > 0) {
			System.out.println("읽지않은개수 :" + hostMsgUnreadCount);
			mav.addObject("hostMsgUnreadCount", hostMsgUnreadCount);
		} else {
			System.out.println("읽지않은개수 없음");
			mav.addObject("hostMsgUnreadCount", 0);
		}

		
		// 날짜
		String checkinDate = "2018-" + dto.getCheckIn().split("월")[0] + "-"
				+ dto.getCheckIn().split("일")[0].split("월")[1];
		String checkoutDate = "2018-" + dto.getCheckOut().split("월")[0] + "-"
				+ dto.getCheckOut().split("일")[0].split("월")[1];

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
		Date FirstDate = null;
		Date SecondDate = null;
		try {
			FirstDate = format.parse(checkinDate);
			SecondDate = format.parse(checkoutDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> dates = new ArrayList<String>();
		Date currentDate = FirstDate;
		while (currentDate.compareTo(SecondDate) <= 0) {
			dates.add(format.format(currentDate));
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DAY_OF_MONTH, 1);
			currentDate = c.getTime();
		}
		System.out.println("지혜언니가 준 날짜     >        " + dates);
		String date = dates.toString().replaceAll("[\\[\\]]", "");
		System.out.println("자른거 : " + date);

		String[] str = date.split(", ");
		List<String> datess = new ArrayList<>();
		for (String tmp : str) {
			datess.add(tmp);
			System.out.println(tmp);
		}

		for (String tmp : datess) {
			System.out.println("최종 보낼 것" + tmp);
		}
		mav.addObject("date", datess);
		try {
			Date checkIn = sdf2.parse(cI);
			Date checkOut = sdf2.parse(cO);
			long diffDay = (checkOut.getTime() - checkIn.getTime()) / (24 * 60 * 60 * 1000);
			int stayPrice = (int) (amount * diffDay);
			int home_servicefee = (int) (stayPrice * 0.05);
			int home_cleaningfee = (int) (stayPrice * 0.1);
			int totalPrice = (int) (stayPrice - (hdto.getHome_price() * 0.05) - (hdto.getHome_price() * 0.1));
			mav.addObject("home_servicefee", home_servicefee);
			mav.addObject("home_cleaningfee", home_cleaningfee);
			mav.addObject("totalPrice", totalPrice);
			mav.addObject("stayPrice", stayPrice);

			String tp = Long.toString(totalPrice);
			System.out.println(diffDay + "박");

			mav.addObject("totalPrice", tp);
			mav.addObject("diffDay", diffDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<MessageDTO> message = this.service.getMessage(message_room_seq);

		for (MessageDTO tmp : message) {
			if (today.equals(tmp.getMessage_time().substring(0, 13))) {
				tmp.setMessage_time(tmp.getMessage_time().substring(14, 20) + "분");
			} else {
				tmp.setMessage_time(tmp.getMessage_time().substring(7, 21));
			}
		}
		mav.addObject("msgRoom", dto);
		mav.addObject("message", message);
		for (MessageDTO tmp : message) {
			System.out.println(tmp.getMessage_content() + " / " + tmp.getMessage_time());
		}

		ReservationDTO dto2 = new ReservationDTO();
		dto2.setMember_email(member_email);
		dto2.setHome_seq(home_seq);
		dto2.setHost_email(userId);
		System.out.println("이메일 : " + dto2.getMember_email() + " / 홈시퀸스 : " + dto2.getHome_seq());
		List<ReservationDTO> reservCheck = this.service.reservCheck(dto2);

		if (!reservCheck.isEmpty()) {

			for (ReservationDTO tmp : reservCheck) {
				System.out.println("예약이미 신청 = " + tmp.getReserv_state() + " 누가 예약했뉘 ? " + tmp.getMember_email()
						+ " 예약 시퀸스 :" + tmp.getReservation_seq());
			}

			mav.addObject("reservCheck", reservCheck);
			mav.addObject("reservChecks", 0);
		} else {

			System.out.println("예약을 안함 아직");
		}

		mav.setViewName("/message/messageHostRoom");
		return mav;

	}

	@RequestMapping("/msgMainGuestAllRead.msg")
	public void msgMainGuestAllRead(HttpSession session, HttpServletResponse response) throws Exception {
		System.out.println("msgMainGuestAllRead");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);
		String userId = (String) session.getAttribute("login_email");
		List<GuestMsgDTO> guestAllMessage = this.service.guestMessageMain(userId);
		List<String> host_email = new ArrayList<>();

		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();

		if (!guestAllMessage.isEmpty()) {
			for (GuestMsgDTO tmp : guestAllMessage) {
				System.out.println("메세지방번호 :  " + tmp.getMessage_room_seq() + "메세지 시퀸스 : " + tmp.getMessage_seq()
						+ "메세지 내용 : " + tmp.getMessage_content() + "메일 : " + tmp.getHost_email() + "날짜 :"
						+ tmp.getMessage_time());

				if (today.equals(tmp.getMessage_time().substring(0, 13))) {
					System.out.println("근꼐 오늘 같다는겨?");
					tmp.setMessage_time(tmp.getMessage_time().substring(15, 21));
				} else {
					tmp.setMessage_time(tmp.getMessage_time().substring(7, 21));
				}

				host_email.add(tmp.getHost_email());
			}

			for (int i = 0; i < guestAllMessage.size(); i++) {
				JSONObject gmI = new JSONObject();
				gmI.put("message_room_seq", guestAllMessage.get(i).getMessage_room_seq());
				gmI.put("message_seq", guestAllMessage.get(i).getMessage_seq());
				gmI.put("home_seq", guestAllMessage.get(i).getHome_seq());
				gmI.put("message_time", guestAllMessage.get(i).getMessage_time());
				gmI.put("message_content", guestAllMessage.get(i).getMessage_content());
				gmI.put("checkIn", guestAllMessage.get(i).getCheckIn());
				gmI.put("checkOut", guestAllMessage.get(i).getCheckOut());
				gmI.put("message_read", guestAllMessage.get(i).getMessage_read());
				gmI.put("host_email", guestAllMessage.get(i).getHost_email());
				gmI.put("fromID", guestAllMessage.get(i).getFromID());
				gmI.put("toID", guestAllMessage.get(i).getToID());
				jArray.add(gmI);
			}
			obj.put("guestAllMsg", jArray);
			
			List<MemberDTO> guestMemberInfo = this.service.memberInfo(host_email);

			JSONArray jArray2 = new JSONArray();

			for (int i = 0; i < guestMemberInfo.size(); i++) {
				JSONObject gmI = new JSONObject();
				
				gmI.put("member_picture", guestMemberInfo.get(i).getMember_picture());
				gmI.put("member_name", guestMemberInfo.get(i).getMember_name());
				gmI.put("member_location", guestMemberInfo.get(i).getMember_location());
				gmI.put("member_email", guestMemberInfo.get(i).getMember_email());
				jArray2.add(gmI);
			}
			obj.put("guestAllMemberInfo", jArray2);

			System.out.println(obj);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			new Gson().toJson(obj, response.getWriter());

		} else {
			System.out.println("게스트가 모든 메세지가 없다니 이럴수가!!!!!!!!");
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			new Gson().toJson(obj, response.getWriter());
			
			
		}
		

	}

	@RequestMapping("/msgMainGuestUnRead.msg")
	public void msgMainGuestUnRead(HttpSession session, HttpServletResponse response) throws Exception {
		System.out.println("msgMainGuestUnRead");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);
		String userId = (String) session.getAttribute("login_email");

		List<GuestMsgDTO> guestUnreadMsg = this.service.guestUnreadMsg(userId);
		List<String> host_email = new ArrayList<>();

		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();

		if (!guestUnreadMsg.isEmpty()) {
			for (GuestMsgDTO tmp : guestUnreadMsg) {
				System.out.println("읽지않은 내용" + tmp.getMessage_content() + "안 읽었니 메세지 리드 : " + tmp.getMessage_read());

				if (today.equals(tmp.getMessage_time().substring(0, 13))) {
					System.out.println("근꼐 오늘 같다는겨?");
					tmp.setMessage_time(tmp.getMessage_time().substring(15, 21));
				} else {

					tmp.setMessage_time(tmp.getMessage_time().substring(7, 21));
					System.out.println(tmp.getMessage_time());
				}

				host_email.add(tmp.getHost_email());

			}
			for (int i = 0; i < guestUnreadMsg.size(); i++) {
				JSONObject gmI = new JSONObject();
				gmI.put("message_room_seq", guestUnreadMsg.get(i).getMessage_room_seq());
				gmI.put("message_seq", guestUnreadMsg.get(i).getMessage_seq());
				gmI.put("home_seq", guestUnreadMsg.get(i).getHome_seq());
				gmI.put("message_time", guestUnreadMsg.get(i).getMessage_time());
				gmI.put("message_content", guestUnreadMsg.get(i).getMessage_content());
				gmI.put("checkIn", guestUnreadMsg.get(i).getCheckIn());
				gmI.put("checkOut", guestUnreadMsg.get(i).getCheckOut());
				gmI.put("message_read", guestUnreadMsg.get(i).getMessage_read());
				gmI.put("host_email", guestUnreadMsg.get(i).getHost_email());
				gmI.put("fromID", guestUnreadMsg.get(i).getFromID());
				gmI.put("toID", guestUnreadMsg.get(i).getToID());
				jArray.add(gmI);
			}
			obj.put("guestUnreadMsg", jArray);
			List<MemberDTO> guestMemberInfo = this.service.memberInfo(host_email);

			JSONArray jArray2 = new JSONArray();

			for (int i = 0; i < guestMemberInfo.size(); i++) {
				JSONObject gmI = new JSONObject();
				gmI.put("member_picture", guestMemberInfo.get(i).getMember_picture());
				gmI.put("member_name", guestMemberInfo.get(i).getMember_name());
				gmI.put("member_location", guestMemberInfo.get(i).getMember_location());
				gmI.put("member_email", guestMemberInfo.get(i).getMember_email());
				jArray2.add(gmI);
			}
			obj.put("guestMemberInfo", jArray2);

			System.out.println(obj);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			new Gson().toJson(obj, response.getWriter());
		} else {

			System.out.println("게스트가 안 읽은 메세지가 없다니 이럴수가!!!!!!!!");
			new Gson().toJson(obj, response.getWriter());
		}
		

	}

	@RequestMapping("/msgMainhostUnRead.msg")
	public void msgMainhostUnRead(HttpSession session, HttpServletResponse response) throws Exception {
		System.out.println("msgMainhostUnRead");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);
		String userId = (String) session.getAttribute("login_email");

		List<GuestMsgDTO> hostUnreadMsg = this.service.hostUnreadMsg(userId);
		List<String> guest_email = new ArrayList<>();

		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();

		if (!hostUnreadMsg.isEmpty()) {
			for (GuestMsgDTO tmp : hostUnreadMsg) {
				System.out.println("읽지않은 내용" + tmp.getMessage_content() + "안 읽었니 메세지 리드 : " + tmp.getMessage_read());

				if (today.equals(tmp.getMessage_time().substring(0, 13))) {
					System.out.println("근꼐 오늘 같다는겨?");
					tmp.setMessage_time(tmp.getMessage_time().substring(15, 21));
				} else {

					tmp.setMessage_time(tmp.getMessage_time().substring(7, 21));
					System.out.println(tmp.getMessage_time());
				}

				guest_email.add(tmp.getGuest_email());

			}
			for (int i = 0; i < hostUnreadMsg.size(); i++) {
				JSONObject gmI = new JSONObject();
				gmI.put("message_room_seq", hostUnreadMsg.get(i).getMessage_room_seq());
				gmI.put("message_seq", hostUnreadMsg.get(i).getMessage_seq());
				gmI.put("home_seq", hostUnreadMsg.get(i).getHome_seq());
				gmI.put("message_time", hostUnreadMsg.get(i).getMessage_time());
				gmI.put("message_content", hostUnreadMsg.get(i).getMessage_content());
				gmI.put("checkIn", hostUnreadMsg.get(i).getCheckIn());
				gmI.put("checkOut", hostUnreadMsg.get(i).getCheckOut());
				gmI.put("message_read", hostUnreadMsg.get(i).getMessage_read());
				gmI.put("host_email", hostUnreadMsg.get(i).getHost_email());
				gmI.put("fromID", hostUnreadMsg.get(i).getFromID());
				gmI.put("toID", hostUnreadMsg.get(i).getToID());
				jArray.add(gmI);
			}
			obj.put("hostUnreadMsg", jArray);

			List<MemberDTO> guestMemberInfo = this.service.memberInfo(guest_email);

			JSONArray jArray2 = new JSONArray();

			for (int i = 0; i < guestMemberInfo.size(); i++) {
				JSONObject gmI = new JSONObject();
				gmI.put("member_picture", guestMemberInfo.get(i).getMember_picture());
				gmI.put("member_name", guestMemberInfo.get(i).getMember_name());
				gmI.put("member_location", guestMemberInfo.get(i).getMember_location());
				gmI.put("member_email", guestMemberInfo.get(i).getMember_email());
				jArray2.add(gmI);
			}
			obj.put("guestMemberInfo", jArray2);

			System.out.println(obj);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			new Gson().toJson(obj, response.getWriter());

		} else {

			System.out.println("게스트가 안 읽은 메세지가 없다니 이럴수가!!!!!!!!");
			
			new Gson().toJson(obj, response.getWriter());
		}

	}

	@RequestMapping("/msgMainHostAllRead.msg")
	public void msgMainHostAllRead(HttpSession session, HttpServletResponse response) throws Exception {
		System.out.println("msgMainHostAllRead");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String today = sdf.format(new Date());
		System.out.println("오늘 날짜: " + today);
		String userId = (String) session.getAttribute("login_email");

		List<GuestMsgDTO> hostMessage = this.service.hostMessageMain(userId);
		List<String> guest_email = new ArrayList<>();

		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();

		if (!hostMessage.isEmpty()) {
			for (GuestMsgDTO tmp : hostMessage) {
				System.out.println("읽지않은 내용" + tmp.getMessage_content() + "안 읽었니 메세지 리드 : " + tmp.getMessage_read());

				if (today.equals(tmp.getMessage_time().substring(0, 13))) {
					System.out.println("근꼐 오늘 같다는겨?");
					tmp.setMessage_time(tmp.getMessage_time().substring(15, 21));
				} else {

					tmp.setMessage_time(tmp.getMessage_time().substring(7, 21));
					System.out.println(tmp.getMessage_time());
				}

				guest_email.add(tmp.getGuest_email());
				for(String tmp2:guest_email) {
				System.out.println("guest_email list에 넣은것 : "+tmp2);
				}

			}
			for (int i = 0; i < hostMessage.size(); i++) {
				JSONObject gmI = new JSONObject();
				gmI.put("message_room_seq", hostMessage.get(i).getMessage_room_seq());
				gmI.put("message_seq", hostMessage.get(i).getMessage_seq());
				gmI.put("home_seq", hostMessage.get(i).getHome_seq());
				gmI.put("message_time", hostMessage.get(i).getMessage_time());
				gmI.put("message_content", hostMessage.get(i).getMessage_content());
				gmI.put("checkIn", hostMessage.get(i).getCheckIn());
				gmI.put("checkOut", hostMessage.get(i).getCheckOut());
				gmI.put("message_read", hostMessage.get(i).getMessage_read());
				gmI.put("host_email", hostMessage.get(i).getHost_email());
				gmI.put("guest_email", hostMessage.get(i).getGuest_email());
				gmI.put("fromID", hostMessage.get(i).getFromID());
				gmI.put("toID", hostMessage.get(i).getToID());
				jArray.add(gmI);
			}
			obj.put("hostAllMessage", jArray);

			List<MemberDTO> guestMemberInfo = this.service.memberInfo(guest_email);

			JSONArray jArray2 = new JSONArray();

			for (int i = 0; i < guestMemberInfo.size(); i++) {
				System.out.println("멤버 이메일 : : "+guestMemberInfo.get(i).getMember_email());
				JSONObject gmI = new JSONObject();
				gmI.put("member_picture", guestMemberInfo.get(i).getMember_picture());
				gmI.put("member_name", guestMemberInfo.get(i).getMember_name());
				gmI.put("member_location", guestMemberInfo.get(i).getMember_location());
				gmI.put("member_email", guestMemberInfo.get(i).getMember_email());
				jArray2.add(gmI);
			}
			obj.put("guestAllMemberInfo", jArray2);

			System.out.println(obj);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			new Gson().toJson(obj, response.getWriter());
		} else {

			System.out.println("게스트가 받은 메세지가 없다니 이럴수가!!!!!!!!");
		}
		

	}

}
