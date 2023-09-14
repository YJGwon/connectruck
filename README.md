<div align=center>

# Connectruck

> 행사장 내 푸드트럭 존 주문 시스템

### [Wiki](https://github.com/YJGwon/connectruck/wiki)

### tech stack

<img src="https://img.shields.io/badge/Java-red?logo=openjdk">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Flyway-CC0200?logo=flyway&logoColor=white">
<img src="https://img.shields.io/badge/Firebase Clound Messaging-FFCA28?logo=firebase&logoColor=black">
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
- 새 주문 push 알림

### API 명세

[Swagger UI Api Definition](https://connectruck.site/swagger-ui/index.html)


## System Architecture

![connectruck-architecture](https://github.com/YJGwon/connectruck/assets/89305335/4951218b-c545-4d56-baf2-9e12fce8c5c1)

## Project Structure(Backend)

```plain text
├─auth - 인증 인가 처리
├─common - project 전반에 공통적으로 적용되는 규칙 정의
├─event - 행사 정보
├─menu - 푸드트럭 메뉴 정보
├─notification - 주문 알림
├─order - 푸드트럭 메뉴 주문
├─truck - 푸드트럭 정
└─user - 사용자 정보
```

## ERD

![connectruck-erd](https://github.com/YJGwon/connectruck/assets/89305335/f8a05fa2-db78-4079-886e-9f82be3ff88d)

