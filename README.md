# ✉️ Buddy

<h1>
  1. 프로젝트 소개
</h1>
<p align="center">
  <img src="https://user-images.githubusercontent.com/115390448/227765979-de2f2b0c-ed45-4717-8324-30960c43e65b.png", width="150px", height="100px" />
  <p align="center", font-size="10px"> 
     Buddy는 메신저, 파일 드라이브, 일정관리를 제공하는 사이트 입니다.<br>
  </p>
</p>
<hr>

### 1-1. 프로젝트 개요<br>
- 개발기간 : 2022.12.12 ~ 2022.01.05 (약 3.5주)<br>
- 개발인원 : 5명<br>
- 주요 구현기능 : 그룹 채팅 / 일정관리 / 파일 드라이브
- 팀원 구성
  1. 강예찬(팀원) : 파일 드라이브(업로드, 다운로드, 공유) / 회원가입,로그인(이메일,문자 인증) / 그룹 초대 (기여도 100%)
  2. 박세진(팀장) : 프로젝트 총괄 / 팀 관련 기능 / 채팅 기본 기능 / 관리자(문의,공지)
  3. 정택구(팀원) : 일정 관리(캘린더 API) / 웹 페이지 디자인
  4. 차석호(팀원) : AWS 구축 / 채팅 세부 기능(파일 첨부) / 유저(문의,공지)
  5. 신유진(팀원) : DB 관리 / 채팅 세부 기능(이모티콘 첨부, 참여자 목록 출력) / 프로필변경
  
- 주제 선정 이유 
  - 웹 소켓을 이용하여 그룹 채팅을 구현하고자 함
  - 캘린더 API를 이용한 일정관리 프로그램을 구현하고자 함
  - 개인 / 그룹 간 사용 및 공유할 수 있는 드라이브를 구현하고자 함

### 1-2. 개발 환경<br>
- Java 11, JavaScript(Jquery), HTML5, CSS
- JDK 11
- IDE : IntelliJ, SQL Developer
- Framework : SpringBoot(2.x)
- Database : Oracle 11
- ORM : Mybatis 2.3.0
- Library : NAVER / KAKAO LOGIN API, NAVER SMS API
- Template Engine : Thymeleaf

### 1-3. ERD
<p align="center">
  <img width="964" alt="ERD 최종본" src="https://user-images.githubusercontent.com/115390448/227840706-0bd830d6-e018-42cd-ae9d-aafa72f3e3de.png">
</p>

### 1-4. 작업 일정
<p align="center">
  <img width="1040" alt="일정" src="https://user-images.githubusercontent.com/115390448/228231934-0b67c38c-71a4-49a0-bea6-9477e7a3ee33.png">
</p>
<h1>
  2. 구현 화면
</h1>
### 로그인 화면
  <img width="1250" alt="메인 로그인 1" src="https://user-images.githubusercontent.com/115390448/228262219-abeec74e-cb49-4949-822b-86eeacfc8cca.png">
  
### 회원가입
 - SMS / 이메일 인증 및 유효성 검사
<img width="1238" alt="회원가입" src="https://user-images.githubusercontent.com/115390448/228262447-7d99fe2a-7394-4c80-9f75-98384d0bbbe3.png">

### 로그인 후 메인화면
 - 개인 정보 수정, 팀 리스트, 팀 생성, 초대코드 입력
<img width="1127" alt="로그인 후 메인" src="https://user-images.githubusercontent.com/115390448/228262598-5bfb0c38-1b78-45f6-ac2d-e90c8d609edd.png">

### 마이페이지
 - 프로필사진, 휴대폰번호, 비밀번호를 변경할 수 있음. 회원탈퇴 가
<img width="1253" alt="프로필수정" src="https://user-images.githubusercontent.com/115390448/228264352-356abeda-1438-4cac-8833-b57d5a314a84.png">

### 그룹 초대
 - 그룹에 속한 그룹원은 초대코드 발송(이메일)을 통해 다른 사람을 팀에 초대할 수 있음
 - 메일로 수신된 코드를 입력해 팀에 입장가능
<img width="373" alt="팀 초대 메일" src="https://user-images.githubusercontent.com/115390448/228263426-1f2619ec-316a-410f-8a8f-120c9db92fe1.png">
<img width="715" alt="초대코드 입력" src="https://user-images.githubusercontent.com/115390448/228263271-44553aaf-5790-4bf1-9f5b-a2b0e02db683.png">
 
### 채팅
 - 채팅에 참가할 팀원을 추가해 채팅방을 생성할 수 있음
<img width="1271" alt="채팅시작" src="https://user-images.githubusercontent.com/115390448/228264044-30aa931a-be3a-48cb-9511-3d3bb06c4c2b.png">
 - 채팅 및 파일전송, 이모티콘 기능 사용 가능
<img width="1276" alt="채팅하기" src="https://user-images.githubusercontent.com/115390448/228264019-cec614c1-9964-4ebd-b361-c0a9a7e0bccf.png">

### 파일 드라이브 화면
 - 파일 업로드(드래그 앤 드롭) / 다운로드 / 폴더 공유 기능 (개인 / 팀)
<img width="1269" alt="드라이브" src="https://user-images.githubusercontent.com/115390448/228264555-63a79b7c-60ae-4f2f-b323-74a3a31863b3.png">

### 일정 관리 캘린더 화면
 - 일정 추가, 수정, 삭제 
<img width="1273" alt="일정" src="https://user-images.githubusercontent.com/115390448/228264715-afce99d0-9f6e-4137-8605-2d756d11e4ef.png">








