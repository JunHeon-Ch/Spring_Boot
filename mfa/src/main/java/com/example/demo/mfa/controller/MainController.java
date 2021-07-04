package com.example.demo.mfa.controller;

import com.example.demo.mfa.data.dto.CustomUserDetails;
import com.example.demo.mfa.data.dto.MfaDto;
import com.example.demo.mfa.data.dto.MfaInitDto;
import com.example.demo.mfa.service.MfaService;
import com.example.demo.mfa.util.OTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Controller
public class MainController {

    private final MfaService mfaService;

    @Autowired
    public MainController(MfaService mfaService) {
        this.mfaService = mfaService;
    }

    @GetMapping(value = {"", "/", "/prelogin", "/login"})
    public String loginPage(HttpSession httpSession) {
        return "prelogin";
    }

    @GetMapping("/mfactor")
    public String mfactor() {
        return "mfactor";
    }

    @GetMapping("/purelogin")
    public String pureLogin() {
        return "purelogin";
    }

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession httpSession) {
        CustomUserDetails customUserDetails = (CustomUserDetails) httpSession.getAttribute("userInfo");
        MfaDto mfaDto = mfaService.getMfa(customUserDetails.getUsername());
        model.addAttribute("mfa", mfaDto);

        return "main";
    }

    @PostMapping("generate/secret")
    public ModelAndView generateSecretKey(HttpSession httpSession, @RequestBody MfaDto mfaDto) {
        ModelAndView modelAndView = new ModelAndView("jsonView");

        CustomUserDetails customUserDetails = (CustomUserDetails) httpSession.getAttribute("userInfo");
        modelAndView.addAllObjects(OTPUtil.generate(mfaDto.getType(), customUserDetails.getUsername()));

        return modelAndView;
    }

    @PostMapping("/initialize/secret")
    public ModelAndView saveSecretKey(HttpSession httpSession, @RequestBody MfaDto mfaDto, HttpServletResponse httpServletResponse) {
        ModelAndView modelAndView = new ModelAndView("jsonView");
        boolean result = OTPUtil.checkCode(mfaDto.getOtpNumber(), mfaDto.getSecretKey());
        if (result) {
            CustomUserDetails customUserDetails = (CustomUserDetails) httpSession.getAttribute("userInfo");
            MfaDto _mfaDto = mfaService.getMfa(customUserDetails.getUsername());
            if (Optional.ofNullable(_mfaDto.getUsername()).isPresent()) {
                mfaService.setMfa(MfaDto.builder()
                        .id(_mfaDto.getId())
                        .username(_mfaDto.getUsername())
                        .secretKey(mfaDto.getSecretKey())
                        .type(mfaDto.getType())
                        .build());
            } else {
                mfaService.setMfa(MfaInitDto.builder()
                        .username(customUserDetails.getUsername())
                        .secretKey(mfaDto.getSecretKey())
                        .type(_mfaDto.getType())
                        .build());
            }
            modelAndView.addObject("success", true);
        } else {
            httpServletResponse.setStatus(httpServletResponse.SC_INTERNAL_SERVER_ERROR);
            modelAndView.addObject("success", false);
        }

        return modelAndView;
    }

    @DeleteMapping("/delete/mfa")
    public ModelAndView deleteMfa(HttpSession httpSession, HttpServletResponse httpServletResponse) {
        ModelAndView modelAndView = new ModelAndView("jsonView");
        modelAndView.addObject("success", false);
        CustomUserDetails customUserDetails = (CustomUserDetails) httpSession.getAttribute("userInfo");
        mfaService.deleteMfa(customUserDetails.getUsername());
        modelAndView.addObject("success", true);
        return modelAndView;
    }
}
