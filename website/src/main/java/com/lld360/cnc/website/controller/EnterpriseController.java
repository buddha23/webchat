package com.lld360.cnc.website.controller;

import java.awt.Rectangle;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.gonvan.kaptcha.Constants;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.EnterpriseCertification;
import com.lld360.cnc.model.EnterpriseInfo;
import com.lld360.cnc.service.EnterpriseService;
import com.lld360.cnc.service.FileUtilService;
import com.lld360.cnc.website.SiteController;

@Controller
@RequestMapping("enterprise")
public class EnterpriseController extends SiteController{
	
	@Autowired
	private EnterpriseService enterpriseService;
	
	@Autowired
	private FileUtilService fileUtilService;

	@GetMapping("settled")
	public String settled(Model model){
		model.addAttribute("certification", enterpriseService.getCertificationByUserId(getRequiredCurrentUser().getId()));
		return "wEnterprise/settled";
	}
	
	@GetMapping("info")
	public String info(Model model){
		EnterpriseCertification certification = enterpriseService.getCertificationByUserId(getRequiredCurrentUser().getId());
		if (null == certification ||certification.getState() != Const.ENTERPRISE_CERTIFICATION_STATE_PASS){
			model.addAttribute("errMsg","请先申请入驻并审核通过后再来填写企业信息！");
			return "wEnterprise/info";
		}
		model.addAttribute("info", enterpriseService.getInfoByUserId(getRequiredCurrentUser().getId()));
		return "wEnterprise/info";
	}
	
	@PostMapping("settled")
	public String settledPost(EnterpriseCertification certification,String captcha){
	    String scaptcha = getSessionStringValue(Constants.KAPTCHA_SESSION_KEY);
	    if (scaptcha == null || !scaptcha.equalsIgnoreCase(captcha)) {
            throw new ServerException(HttpStatus.BAD_REQUEST);
        }
	    UserDto user = getRequiredCurrentUser();
		if (!volidateCertification(certification)){
			throw new ServerException(HttpStatus.BAD_REQUEST);
		}
		EnterpriseCertification orign = enterpriseService.getCertificationByUserId(user.getId());
		if (null != orign){
			certification.setId(orign.getId());
			certification.setUserId(user.getId());
			enterpriseService.updateCertification(certification,orign.getState());
			return "redirect:/enterprise/settled";
		}
		certification.setUserId(user.getId());
		certification.setState(Const.ENTERPRISE_CERTIFICATION_STATE_NEW);
		enterpriseService.settled(certification);
		return "redirect:/enterprise/settled";
	}
	
	@PostMapping("info")
	public String infoPost(EnterpriseInfo info,@RequestParam(required = false)MultipartFile qrCodeFile){
		UserDto user = getRequiredCurrentUser();
		if (qrCodeFile !=null && qrCodeFile.getSize()>0){
			checkFileType(qrCodeFile, Const.COURSE_NOT_GEN_PIC_TYPES);
			String qrCodePath = enterpriseService.uploadFile(qrCodeFile,FilenameUtils.getExtension(qrCodeFile.getOriginalFilename()),Const.ENTERPRISE_UPLOADPIC_TYPE_QRCODE);
			info.setQrCode(qrCodePath);
		}
		if (!volidateInfo(info)){
			throw new ServerException(HttpStatus.BAD_REQUEST);
		}
		EnterpriseCertification certification = enterpriseService.getCertificationByUserId(user.getId());
		info.setId(certification.getId());
		info.setUserId(user.getId());
		enterpriseService.setOrModifyEnterpriseInfo(info);
		return "redirect:/enterprise/info";
	}
	
	@PostMapping("settled/{id}")
	public ResponseEntity<String> setCertification(@PathVariable("id")Long id,EnterpriseCertification certification){
		UserDto user = getRequiredCurrentUser();
		if (!volidateCertification(certification)){
			throw new ServerException(HttpStatus.BAD_REQUEST);
		}
		EnterpriseCertification orign = enterpriseService.getCertificationByUserId(user.getId());
		if (!id.equals(orign.getId()) || orign.getState().byteValue() != Const.ENTERPRISE_CERTIFICATION_STATE_NEW){
			throw new ServerException(HttpStatus.BAD_REQUEST);
		}
		certification.setId(id);
		certification.setUserId(user.getId());
		enterpriseService.updateCertification(certification,orign.getState());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PostMapping("uploadPic")
	public ResponseEntity<String> uploadPic(MultipartFile file,byte type){
		String imgType = file.getContentType();
		int index = imgType.indexOf("image/");
		if (index == -1){
	       throw new ServerException(HttpStatus.BAD_REQUEST, M.E10107);
		}
		if (type !=Const.ENTERPRISE_UPLOADPIC_TYPE_LOGO && type != Const.ENTERPRISE_UPLOADPIC_TYPE_QRCODE && type != Const.ENTERPRISE_UPLOADPIC_TYPE_REGISTRATION){
			throw new ServerException(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(enterpriseService.uploadFile(file,imgType.substring("image/".length()),Const.ENTERPRISE_UPLOADPIC_TYPE_REGISTRATION),HttpStatus.OK);
	}
	
    @RequestMapping(value = "uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<String> uploadAvatar(MultipartFile file, @RequestParam Double x, @RequestParam Double y, @RequestParam Double w, @RequestParam Double h) {
        if (file == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("文件不存在");
        }
        UserDto currentUser = getRequiredCurrentUser();
        checkFileType(file, Const.COURSE_NOT_GEN_PIC_TYPES);
        Rectangle rectangle = new Rectangle(x.intValue(), y.intValue(), w.intValue(), h.intValue());
        String fileName = fileUtilService.cutFile(file, rectangle);

        EnterpriseInfo info = enterpriseService.getInfoByUserId(currentUser.getId());
        String filePath = fileUtilService.moveUploadedFile(fileName, Const.FILE_ENTERPRISE, currentUser.getId(), info==null?null:info.getLogoImg());
        return new ResponseEntity<>(filePath,HttpStatus.OK);
    }
	
	private boolean volidateInfo(EnterpriseInfo info){
		return !(StringUtils.isEmpty(info.getIntroduce())
				|| StringUtils.isEmpty(info.getLogoImg())
				|| StringUtils.isEmpty(info.getQrCode())
				|| StringUtils.isEmpty(info.getService())
				|| StringUtils.isEmpty(info.getSlogan())
				|| StringUtils.isEmpty(info.getTelephone())
				|| !info.getTelephone().matches("1\\d{10}")
				|| info.getSlogan().length()>28
				|| info.getService().length()>100
				|| info.getIntroduce().length()>200);
	}
	
	private boolean volidateCertification(EnterpriseCertification certification){
		return !(StringUtils.isEmpty(certification.getLinkman()) 
				|| StringUtils.isEmpty(certification.getMobile()) 
				|| !certification.getMobile().matches("1\\d{10}")
				|| StringUtils.isEmpty(certification.getAddress())
				|| StringUtils.isEmpty(certification.getName())
				|| certification.getName().length() > 24
				|| certification.getRegistrationNo().length() >24
				|| certification.getLinkman().length() > 7
				|| StringUtils.isEmpty(certification.getRegistrationImg())
				|| StringUtils.isEmpty(certification.getRegistrationNo()));
	}
}
