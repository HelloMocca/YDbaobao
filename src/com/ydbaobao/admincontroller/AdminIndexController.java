package com.ydbaobao.admincontroller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.support.ImageFactoryUtil;
import com.ydbaobao.model.IndexImage;
import com.ydbaobao.service.AdminIndexImageService;

@Controller
@RequestMapping("/admin/indexImages")
public class AdminIndexController {
	private static final Logger logger = LoggerFactory.getLogger(AdminIndexController.class);
	@Resource
	private AdminIndexImageService adminIndexImageService;
	
	/**
	 * 첫화면 이미지 설정 관리 페이지 
	 * 이미지 경로와 저장된 이미지 갯수를 돌려준다.
	 */
	@RequestMapping()
	public String indexImage(Model model) {
		model.addAttribute("indexImages", getIndexImages());
		return "indexManager";
	}
	
	/**
	 * 첫화면 이미지 설정 관리 페이지 
	 * 이미지 추가.
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String indexImageCreate(Model model, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IllegalStateException, IOException {
		if(imageFile.getSize() > 512000) {
			logger.debug("이미지 업로드 실패!, 용량이 500kb 초과되었습니다");
			model.addAttribute("errorMessage", "용량이 500kb 초과되었습니다");
			model.addAttribute("indexImages", getIndexImages());
			return "indexManager";
		}
		// 파일 저장
		String imgFileName = ""+System.currentTimeMillis();
		String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
		String imgFilePath = contextRoot+ImageFactoryUtil.realIndexPath + imgFileName;
		logger.debug("before transfer");
		imageFile.transferTo(new File(imgFilePath));
		logger.debug(imgFilePath);
		
		// 디비 저장
		adminIndexImageService.create(imgFileName);
		model.addAttribute("indexImages", getIndexImages());
		return "indexManager";
	}

	/**
	 * 첫화면 이미지 설정 관리 페이지 
	 * 이미지 삭제.
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value = "/{indexImageId}", method = RequestMethod.POST)
	public String indexImageDelete(Model model, @PathVariable int indexImageId) throws IllegalStateException, IOException {
		adminIndexImageService.delete(indexImageId);
		model.addAttribute("indexImages", getIndexImages());
		return "indexManager";
	}

	private List<IndexImage> getIndexImages() {
		List<IndexImage> indexImages = adminIndexImageService.readIndexImages();
		indexImages.add(new IndexImage());
		return indexImages;
	}
}
