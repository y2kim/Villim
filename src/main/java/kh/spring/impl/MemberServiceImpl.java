package kh.spring.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kh.spring.dto.HomeDTO;
import kh.spring.dto.HomePicDTO;
import kh.spring.dto.MemberDTO;
import kh.spring.dto.ProfileHomePicDTO;
import kh.spring.interfaces.MemberDAO;
import kh.spring.interfaces.MemberService;

@Component
public class MemberServiceImpl implements MemberService{

	@Autowired
	public MemberDAO dao;

	
	@Override
	public MemberDTO printProfile(String userId) {
		return dao.printProfile(userId);
	}


	@Override
	public String editProfile(MemberDTO dto) {
		 dao.editProfile(dto);
		return dto.getMember_email();
	}


	@Override
	public int editPhoto(String systemName, String userId) {
		return dao.editPhoto(systemName, userId);
	}


	@Override
	public MemberDTO getPhoto(String userId) {
		return dao.getPhoto(userId);
	}


	@Override
	public int countHouse(String userId) {
		return dao.countHouse(userId);
	}


	@Override
	public List<ProfileHomePicDTO> getHouse(String userId) {
		return dao.getHouse(userId);
	}


	
	
	
}