package com.fivet.buddy.controller;

import com.fivet.buddy.dto.ChatRoomDTO;
import com.fivet.buddy.dto.TeamDTO;
import com.fivet.buddy.dto.TeamMemberDTO;
import com.fivet.buddy.services.ChatRoomService;
import com.fivet.buddy.services.TeamMemberService;
import com.fivet.buddy.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/team/")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private HttpSession session;

    @Autowired
    private TeamMemberService teamMemberService;

    //팀 생성 페이지로 이동
    @RequestMapping("add")
    public String add() {
        return "team/teamAdd";
    }

    //팀 생성
    @PostMapping("createTeam")
    public String create(TeamDTO teamDto, ChatRoomDTO chatRoomDto) throws Exception {
        teamDto.setTeamOwnerSeq((Integer) session.getAttribute("memberSeq"));
        Map<String, String> param = new HashMap<>();
        param.put("memberSeq", session.getAttribute("memberSeq").toString());
        // session값인 이름만 닉네임에 담아 service에 전송.
        param.put("teamMemberNickname", session.getAttribute("memberName").toString());
        teamService.insertTeam(teamDto, param);

        return "redirect:/member/loginIndex";
    }

    //팀 이동
    @PostMapping("goTeam")
    public String goTeam(TeamMemberDTO teamMemberDto, Model model) {

        teamMemberDto.setMemberSeq((int)session.getAttribute("memberSeq"));
        teamMemberDto = teamMemberService.selectOne(teamMemberDto);
        // 팀 번호 session 부여
        session.setAttribute("teamSeq", teamMemberDto.getTeamSeq());
        // 회원의 팀내 닉네임 session 부여
        session.setAttribute("teamMemberNickname", teamMemberDto.getTeamMemberNickname());
        // 팀 이름 session 부여
        session.setAttribute("teamName", teamService.selectTeamName(teamMemberDto.getTeamSeq()));
        //teamSeq와 memberSeq를 담아 서비스 및 sql문에 전달할 Map
        Map<String, Integer> param = new HashMap<>();
        param.put("teamSeq", teamMemberDto.getTeamSeq());
        param.put("memberSeq", teamMemberDto.getMemberSeq());
        // 팀 입장시, 해당 팀 해당 회원의 채팅방 목록 출력
        List<ChatRoomDTO> chatRoomList = chatRoomService.chatRoomList(param);
        model.addAttribute("chatRoomList", chatRoomList);
        return "team/team";
    }

    //팀 관리 이동
    @RequestMapping("goTeamSetting")
    public String managementTeamSelectTeam(int teamSeq, Model model) throws Exception{
        List<TeamMemberDTO> teamMemberDtoList =  teamService.managementTeamSelectTeamMember(String.valueOf(teamSeq));
        TeamDTO teamDto =teamService.managementTeamSelectTeam(String.valueOf(teamSeq));
        model.addAttribute("teamMemberDtoList", teamMemberDtoList);
        model.addAttribute("teamDto",teamDto);
        return "team/teamSetting";
    }

    //팀 관리 팀 이름 수정
    @ResponseBody
    @RequestMapping("updateTeamName")
    public void managementUpdateTeamName(TeamDTO teamDto) throws Exception{
        teamService.managementUpdateTeamName(teamDto);
    }

    @RequestMapping("deleteTeam")
    //팀 삭제
    public String deleteTeam(int teamSeq){
        System.out.println(teamSeq);
        teamService.deleteTeam(teamSeq);
        return "redirect:/member/loginIndex";
    }

    // Exception Handler
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e){
        e.printStackTrace();
        return "error";
    }
}
