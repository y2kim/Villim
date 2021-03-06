package kh.spring.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.spring.dto.DetailDTO;
import kh.spring.dto.GuestMsgDTO;
import kh.spring.dto.HomeDTO;
import kh.spring.dto.MemberDTO;
import kh.spring.dto.MessageDTO;
import kh.spring.dto.MessageRoomDTO;
import kh.spring.dto.ReservationDTO;
import kh.spring.interfaces.MessageDAO;
import kh.spring.interfaces.MessageService;

@Service
public class MessageServiceImpl implements MessageService{

	@Autowired
	MessageDAO dao;

	@Override
	public int countReview(int home_seq) {
		return dao.countReview(home_seq);
	}

	@Override
	public int messageInsert(MessageDTO dto) {
		return dao.messageInsert(dto);
	}

	@Override
	public HomeDTO getHomeInfo(int home_seq) {
		return dao.getHomeInfo(home_seq);
	}

	@Override
	public int getRoomSeq() {
		return dao.getRoomSeq();
	}

	@Override
	public MessageRoomDTO messageRoomSeqExist(MessageRoomDTO dto) {
		return dao.messageRoomSeqExist(dto);
	}

	@Override
	public int messageRoomInsert(MessageRoomDTO roomdto) {
		return dao.messageRoomInsert(roomdto);
	}

	@Override
	public List<GuestMsgDTO> guestMessageMain(String userId) {
		return dao.guestMessageMain(userId);
	}

	@Override
	public int guestMsgAllCount(String userId) {
		return dao.guestMsgAllCount(userId);
	}

	@Override
	public List<MemberDTO> memberInfo(List<String> hostId) {
	    return dao.memberInfo(hostId);
	}

	@Override
	public MessageRoomDTO msgRoomInfo(int message_room_seq) {
		return dao.msgRoomInfo(message_room_seq);
	}

	@Override
	public List<MessageDTO> getMessage(int message_room_seq) {
		return dao.getMessage(message_room_seq);
	}

	@Override
	public MessageDTO getOneMessage(int message_seq) {
		return dao.getOneMessage(message_seq);
	}

	@Override
	public int getMessageSeq() {
		return dao.getMessageSeq();
	}

	@Override
	public List<GuestMsgDTO> hostMessageMain(String userId) {
		return dao.hostMessageMain(userId);
	}

	@Override
	public int hostMsgAllCount(String userId) {
		return dao.hostMsgAllCount(userId);
	}

	@Override
	public List<HomeDTO> getHomeNames(String userId) {
		return dao.getHomeNames(userId);
	}

	@Override
	public List<ReservationDTO> reservCheck(ReservationDTO dto2) {
		return dao.reservCheck(dto2);
	}

	@Override
	public int guestMsgUnreadCount(String userId) {
		return dao.guestMsgUnreadCount(userId);
	}

	@Override
	public List<GuestMsgDTO> guestUnreadMsg(String userId) {
		return dao.guestUnreadMsg(userId);
	}

	@Override
	public List<GuestMsgDTO> hostUnreadMsg(String userId) {
		return dao.hostUnreadMsg(userId);
	}

	@Override
	public int hostMsgUnreadCount(String userId) {
		return dao.hostMsgUnreadCount(userId);
	}

	@Override
	public int ReadUpdate(int message_seq, String member_email, String userId) {
		return dao.ReadUpdate(message_seq, member_email, userId);
	}

	@Override
	public DetailDTO getMsgAfterSend(int message_seq) {
		return dao.getMsgAfterSend(message_seq);
	}

	@Override
	public MessageDTO getMessageOne(int message_room_seq) {
		return dao.getMessageOne(message_room_seq);
	}

	
	
	
}
