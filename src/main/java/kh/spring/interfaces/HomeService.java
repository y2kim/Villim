package kh.spring.interfaces;

import java.util.List;

import kh.spring.dto.HomeDTO;
import kh.spring.dto.HomePicDTO;

public interface HomeService {
	public List<HomeDTO> getAllHomeData();

	public HomeDTO getOldestHomeData();

	public HomeDTO getHomeData(int home_seq);

	public int addHomePicData(HomePicDTO dto);
	
	public List<HomePicDTO> getHomePicData(int seq);
}