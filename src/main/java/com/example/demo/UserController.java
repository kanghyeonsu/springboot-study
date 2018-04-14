package com.example.demo;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/login")
	public String loginForm() {
		return "/user/loginForm";
	}
	
	@PostMapping("/login")
	public String login(String id, String pw, HttpSession session) {
		User loginUser = userRepository.findById(id);
		
		if(loginUser == null || !loginUser.matchPw(pw)) {
			System.out.println("로그인 실패");
			return "redirect:/user/list";
		}
		session.setAttribute("me",loginUser);
		System.out.println("로그인 성공");
		return "redirect:/user/list"; //그냥 바로 리스트로 이동해야 되기 때문에 redirect로 해야된다.
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("me");
		
		return "redirect:/user/list";
	}
	
	@GetMapping("/signin")
	public String signin() {
		return "/user/signInForm";
	}
	
	@PostMapping("/signin")
	public String signin(String id, String pw, String name) {
		User newUser = new User(id, pw, name);
		userRepository.save(newUser);
		return "redirect:/user/list";    //url을 실제로 쳤다는 것.
	}
	
	@GetMapping("/list")
	public String list(Model model, Integer pageNo) {
		
		if(pageNo == null) {
			pageNo = 0; //0이 1페이지
		}
		//현재페이지, 3개씩, 정렬방법, 어떤 인덱스로 정렬하냐
		Pageable pageable = new PageRequest(pageNo, 3, Sort.Direction.DESC, "no");
		Page<User> page = userRepository.findAll(pageable);
		
        int nowPage = ((int) page.getNumber()); // 현재 페이지 ex) 0 => 1페이지
        int blockPage = 3; // 한 블록당 페이지 수
        int totalPage = (int) page.getTotalPages(); // 전체 페이지 수
        int nowBlock = (int) Math.floor((float) nowPage/blockPage); // 현재 블록 ex) 0 => 1블록

        int firstPageBlock = nowBlock*blockPage; // 현재 블록의 첫페이지
        if(firstPageBlock < 0 ){ firstPageBlock = 0; } // 시작 페이지가 1이하일 경우 1 페이지로 초기화

        int lastPageBlock = (nowBlock*blockPage) + (blockPage-1); // 블록의 끝페이지
        if( totalPage <= lastPageBlock+1 ){ lastPageBlock = totalPage-1; }; // 최대범위를 넘지 않도록 설정
        // 총 페이지 개수가 블록의 끝페이지 보다 작거나 같으면
        // 블록의 끝페이지를 총 페이지 개수(개수니깐 -1)로 초기화

        model.addAttribute("firstPageBlock",firstPageBlock);
        model.addAttribute("lastPageBlock",lastPageBlock);
        model.addAttribute("nowPage",page.getNumber());
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("users", page);
		return "/user/list";             // 템플릿 파일을 찾아서 맵핑
	}
	
	@GetMapping("/{no}") 
	public String detail(@PathVariable Long no, Model model, HttpSession session) {
		User user = (User)session.getAttribute("me");
		if(user == null) {
			return "redirect:/user/login";
		}
		
		model.addAttribute("user", userRepository.findOne(no));
		return "/user/detail";
	}
	
	@PostMapping("/{no}")
	public String update(@PathVariable Long no, User updatedUser) {
		//findOne은 프라이머리키로 찾겠다는 뜻
		User user = userRepository.findOne(no);
		user.update(updatedUser);
		userRepository.save(user);
		return "redirect:/user/list";
	}
	
	@GetMapping("delete/{no}")
	public String delete(@PathVariable Long no) {
		userRepository.delete(no);
	
		return "redirect:/user/list";
	}
}
