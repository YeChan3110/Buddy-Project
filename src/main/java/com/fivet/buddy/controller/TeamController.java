package com.fivet.buddy.controller;

import com.fivet.buddy.dto.*;
import com.fivet.buddy.services.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    @Autowired
    private CalService calService;

    @Autowired
    private BasicFolderService basicFolderService;

    @Autowired
    private PersonalFolderService personalFolderService;

    @Autowired
    private PersonalFileService personalFileService;

    @Autowired
    private MemberService memberService;

    //팀 생성 페이지로 이동
    @RequestMapping("add")
    public String add() {
        return "team/teamAdd";
    }

    //팀 생성
    @Transactional
    @PostMapping("createTeam")
    public String create(TeamDTO teamDto, ChatRoomDTO chatRoomDto) throws Exception {
        teamDto.setTeamOwnerSeq((Integer) session.getAttribute("memberSeq"));
        Map<String, String> param = new HashMap<>();
        param.put("memberSeq", session.getAttribute("memberSeq").toString());
        // session값인 이름만 닉네임에 담아 service에 전송.
        param.put("teamMemberNickname", String.valueOf(session.getAttribute("memberName")));
        teamService.insertTeam(teamDto, param);

        // 팀 생성시 팀 기본폴더 생성
        basicFolderService.newTeamBasicFolder(teamDto);
        // 팀 생성시 팀 폴더 생성
        personalFolderService.newTeamSubFolder(teamDto);

        return "redirect:/member/loginIndex";
    }

        //팀 이동
        @Transactional
        @PostMapping("goTeam")
        public String goTeam(TeamMemberDTO teamMemberDto, Model model) throws Exception {
            teamMemberDto.setMemberSeq((int)session.getAttribute("memberSeq"));
            // 회원 번호를 이용하여 팀 DTO값을 불러옴.
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
            // 팀 토픽 갯수 카운트
            int topicCount = chatRoomService.countTopic(param.get("teamSeq"));
            //팀 입장 시, 팀 멤버 출력
            List<TeamMemberListDTO> teamMemberDtoList =  teamMemberService.selectTeamMember(session.getAttribute("teamSeq").toString());
            List<ChatRoomDTO> topicList = chatRoomService.selectTopic(param.get("teamSeq"));

            //프로필 이미지 출력
            String memberImgSysName = memberService.selectProfileImg(String.valueOf(session.getAttribute("memberSeq")));
            if(memberImgSysName.equals("/static/img/defaultProfileImg.png")){
                 model.addAttribute("memberImgSysName",memberImgSysName);
            }else{
                memberImgSysName = "/member/selectProfileImg/"+memberImgSysName;
                memberImgSysName=memberImgSysName.replaceAll("\\s", "");
                model.addAttribute("memberImgSysName",memberImgSysName);
            }
            model.addAttribute("teamMemberDtoList", teamMemberDtoList);
            model.addAttribute("chatRoomList", chatRoomList);
            model.addAttribute("topicList", topicList);
            model.addAttribute("topicCount", topicCount);
            model.addAttribute("teamMemberInfo", teamMemberDto);
            return "team/team";
        }

    //팀 관리 이동
    @RequestMapping("goTeamSetting")
    public String goTeamSetting(int teamSeq, Model model) throws Exception{
        List<TeamMemberListDTO> teamMemberDtoList =  teamMemberService.selectTeamMember(String.valueOf(teamSeq));
        TeamDTO teamDto =teamService.selectTeamOne(String.valueOf(teamSeq));
        model.addAttribute("teamMemberDtoList", teamMemberDtoList);
        model.addAttribute("teamDto",teamDto);
        model.addAttribute("sessionMemberSeq",session.getAttribute("memberSeq"));
        return "team/teamSetting";
    }

    //팀 관리 팀 이름 수정
    @ResponseBody
    @RequestMapping("updateTeamName")
    public void updateManagementTeamName(TeamDTO teamDto) throws Exception{
        teamService.updateManagementTeamName(teamDto);
    }

    //팀 삭제
    @Transactional
    @RequestMapping("deleteTeam")
    public String deleteTeam(int teamSeq) throws Exception {
        String teamBasicKey = basicFolderService.myTeamFolderKey(teamSeq);
        // 기본 폴더 삭제(db + 실제)
        basicFolderService.teamOut(teamSeq);
        // 모든 하위 폴더 키 가져오기
        List<Map<String,String>> allKeys = personalFolderService.allTeamKeys(teamBasicKey);
        // 팀 폴더들 삭제(db)
        personalFolderService.teamOut(allKeys);
        // 팀 파일들 삭제(db)
        personalFileService.teamOut(allKeys);
        // 팀 캘린더 일정 삭제
        calService.deleteTeamAllEvent(teamSeq);

        teamService.deleteTeam(teamSeq);
        return "redirect:/member/loginIndex";
    }

    //멤버 등급 변경
    @Transactional
    @ResponseBody
    @PostMapping("updateTeamMemberGrade")
    public void updateTeamMemberGrade(TeamMemberDTO teamMemberDto) {
        teamMemberService.updateTeamMemberGrade(teamMemberDto);

        //만약에 매니저가 다른 팀원에게 매니저를 이양한다면
        if(teamMemberDto.getGrade().equals("매니저")){
            // 이전 dto값은 변경되는 회원의 memberSeq값이 들어있음
            Map<String, Integer> param = new HashMap<>();
            param.put("memberSeq",teamMemberDto.getMemberSeq());
            param.put("teamSeq", teamMemberDto.getTeamSeq());
            param.put("newManagerSeq", teamMemberDto.getMemberSeq());
            param.put("manageSeq", (Integer) session.getAttribute("memberSeq"));
            //원래 매니저를 팀원으로 등급 변경
            teamMemberService.updateTeamMemberManager(param);
            //team 테이블의 team_owner_seq 컬럼 값을 새로 변경된 매니저 member_seq로 변경
            teamService.updateTeamOwnerSeq(param);
        }
    }

    //팀 관리 페이지에서 팀원 강퇴
    @ResponseBody
    @RequestMapping("deleteTeamMember")
    public void deleteTeamMember(TeamMemberDTO teamMemberDto){
        teamMemberService.deleteTeamMember(teamMemberDto);
    }

    //팀 화면에서 나가기
    @RequestMapping("teamOut")
    public String teamOut() {
        // 팀 관련 session값 제거.
        session.removeAttribute("teamSeq");
        session.removeAttribute("teamMemberNickname");
        session.removeAttribute("teamName");
        return "redirect:/member/loginIndex";
    }

    //회원이 가입한 팀 리스트 출력 (ajax)
    @ResponseBody
    @PostMapping("teamList")
    public String selectTeamList() {
        Gson g = new Gson();
        return  g.toJson(teamService.selectMemberTeam((int)session.getAttribute("memberSeq")));
    }

    //팀원 목록 출력
    @ResponseBody
    @RequestMapping("teamMemberList")
    public String teamMemberList (){
        List<TeamMemberListDTO> teamMemberDtoList =  teamMemberService.selectTeamMember(String.valueOf(session.getAttribute("teamSeq")));
        Gson g = new Gson();
        return g.toJson(teamMemberDtoList);
    }

    //팀원 수가 넘어가면 추가 못하게 하는 로직
    @ResponseBody
    @RequestMapping("teamMemberCount")
    public int TeamMemberCount(int teamSeq){
        return teamMemberService.selectTeamMember(teamSeq);
    }

    //부매니저 수 체크
    @ResponseBody
    @RequestMapping("subManagerMemberCount")
    public int subManagerCount(int teamSeq){
        return teamMemberService.subManagerCount(teamSeq);
    }

    //회원 (자발적) 팀 탈퇴
    @Transactional
    @ResponseBody
    @PostMapping("teamSelfOut")
    public void teamSelfOut(TeamMemberDTO teamMemberDto) throws Exception {
        teamMemberDto.setTeamSeq((int)session.getAttribute("teamSeq"));
        teamMemberDto.setMemberSeq((int)session.getAttribute("memberSeq"));
        String teamMemberNickname = session.getAttribute("teamMemberNickname").toString();
        calService.deleteTeamMemberEvent(teamMemberNickname);
        teamMemberService.deleteTeamMember(teamMemberDto);
        teamService.updateMinusTeamCount(teamMemberDto.getTeamSeq());
        chatRoomService.teamSelfOut(teamMemberDto);
        // 팀 관련 session값 제거.
        session.removeAttribute("teamSeq");
        session.removeAttribute("teamMemberNickname");
        session.removeAttribute("teamName");
    }

    //팀원 닉네임 변경
    @Transactional
    @ResponseBody
    @PostMapping("updateTeamMemberNickName")
    public String updateTeamMemberNickName(TeamMemberDTO teamMemberDto ,CalDTO calDto) throws Exception{
        teamMemberDto.setMemberSeq((int) session.getAttribute("memberSeq"));
        teamMemberService.updateTeamMemberNickName(teamMemberDto);
        String eventWriter = teamMemberDto.getTeamMemberNickname();
        int memberSeq = (int) session.getAttribute("memberSeq");
        int teamSeq = (int)session.getAttribute("teamSeq");
        calService.updateNickname(memberSeq,teamSeq,eventWriter);
        return teamMemberDto.getTeamMemberNickname();
    }

    //2인 이상의 팀 매니저여부를 체크
    @ResponseBody
    @PostMapping("memberManagerCheck")
    public int memberManagerCheck(int memberSeq) {
        return teamService.memberManagerCheck(memberSeq);
    }

    // Exception Handler
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e){
        e.printStackTrace();
        return "error";
    }
}
