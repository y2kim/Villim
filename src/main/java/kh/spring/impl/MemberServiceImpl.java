package kh.spring.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.spring.dto.MemberDTO;
import kh.spring.interfaces.MemberDAO;
import kh.spring.interfaces.MemberService;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	MemberDAO dao;

	@Override
	public int signup(MemberDTO dto) {
		
		return dao.signup(dto);
	
	}

	@Override
	public boolean isMember(MemberDTO dto) {
		
		
		return dao.isMember(dto);
		
		
	}

}
