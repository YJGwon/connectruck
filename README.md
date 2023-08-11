<div align=center>

# Connectruck

> 행사장 내 푸드트럭 존 주문 시스템

### tech stack

<img src="https://img.shields.io/badge/Java-red?logo=openjdk">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/Flyway-CC0200?logo=flyway&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black">
<img src="https://img.shields.io/badge/React-61DAFB?logo=react&logoColor=black">
<img src="https://img.shields.io/badge/Mui-007FFF?logo=mui&logoColor=white">
<img src="https://img.shields.io/badge/Amazone EC2-FF9900?logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/Nginx-009639?logo=nginx&logoColor=white">
<img src="https://img.shields.io/badge/Github Actions-2088FF?logo=githubactions&logoColor=white">

</div>

## 주요 기능

### 사용자

[사용해보기](https://connectruck.site/events/1)

- 행사에 등록된 푸드트럭 목록 및 메뉴 조회
- 메뉴 주문/취소
- 주문 처리 상태 조회

### 사장님

[사용해보기](https://connectruck.site/owner) - 아이디 owner1, 비밀번호 test1234!

- 회원가입/로그인
- 처리 상태별 주문 목록 조회
- 주문 처리 상태 변경
- 메뉴 관리(품절 처리)
- 새 주문 실시간 업데이트

## System Architecture

![connectruck-architecture](https://github.com/YJGwon/wanted-pre-onboarding-backend/assets/89305335/e9c01533-ee11-44ae-b74b-6be951efe348)

## ERD

![connectruck-erd](https://github.com/YJGwon/programmers-solved/assets/89305335/7a2dba7b-1c59-4dd1-9d11-b96c66b1508c)
[DB Schema description](https://forky-freeky-forky.notion.site/DB-Schema-31ab88d5bd534cdd8dcd14e866e99a76?pvs=4)

## API 명세

[Swagger UI Api Definition](https://connectruck.site/swagger-ui/index.html)

---

## To-Do

### 사용자

- 주문 알림 SMS 발송
- 위치 기반 주변 행사 조회
- 가상 결제 구현

### 사장님

- 회원 정보 수정
- 모바일 주문 push 알림 (FCM)
- thumbnail 업로드

### 행사 담당자

- 행사 참여 푸드트럭 관리

### 서비스 운영자

- 제휴 행사 관리
- 푸드트럭 사장님 계정 발급
- 행사 페이지 접속 QR 발급
