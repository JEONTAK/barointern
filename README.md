## Baro Intern 백엔드 개발 과제

사용자 회원가입, 로그인, 권한 변경 기능을 목표로하는 Spring Boot 프로젝트입니다.  
JWT 기반 인증/인가 방식을 사용합니다.

### Tech Spec 문서
과제를 진행하기 전, Tech Spec 문서를 통해 어떻게 진행할지 정리해보았습니다.
https://github.com/JEONTAK/barointern/wiki/TechSpec

### ✅ 주요 기능
- 회원가입 (USER,ADMIN)
- 로그인 (JWT 발급)
- 사용자 권한 변경

### 🚀 Swagger API Docs 실행 방법

http://52.79.197.253:8080/docs 접속
![img.png](https://github.com/user-attachments/assets/a4287c03-bf22-4dc4-80cb-f1fb296c18a9)

### 📁 프로젝트 클론 실행 방법

1️⃣ **프로젝트 클론**

```bash
git clone https://github.com/JEONTAK/barointern.git
```

2️⃣ **필수 환경 변수 설정**

${JWT_SECRET_KEY} -> JWT 토큰 서명에 사용할 비밀키
- 인텔리제이 환경변수 편집을 이용하여 ${JWT_SECRET_KEY} 환경변수 설정
- 혹은 개인 환경에 맞추어 환경변수 세팅

3️⃣ **프로젝트 실행**

### 📌 API의 엔트 포인트 URL

# 사용자 인증 시스템 및 AWS 배포 정보

| 구분                  | 항목                              | 설명                                                                 |
|-----------------------|-----------------------------------|---------------------------------------------------------------------|
| **회원가입 (유저) API** | **URL**                          | `http://52.79.197.253:8080/api/v1/auth/signup/user`                                         |
|                       | **Request**                      | `email`, `password` (영어와 숫자 포함, 8글자 이상)                   |
|                       | **설명**                         | 일반 사용자 회원가입 API                                            |
| **회원가입 (관리자) API** | **URL**                       | `http://52.79.197.253:8080/api/v1/auth/signup/admin`                                        |
|                       | **Request**                      | `email`, `password` (영어와 숫자 포함, 8글자 이상), `adminPassword`  |
|                       | **설명**                         | 관리자만 회원가입 가능 (adminPassword로 제한)                        |
| **로그인 API**        | **URL**                          | `http://52.79.197.253:8080/api/v1/auth/signin`                                              |
|                       | **Request**                      | `email`, `password`                                                |
|                       | **Response**                     | `token`                                                            |
|                       | **설명**                         | 사용자 인증 후 토큰 발급                                           |
| **역할 변경 API**     | **URL**                          | `http://52.79.197.253:8080/api/v1/admin/changeroles`                                        |
|                       | **설명**                         | 관리자만 사용자의 역할 변경 가능                                    |
| **AWS 배포**          | **EC2 인스턴스 생성**            | AWS EC2 인스턴스 생성                                              |
|                       | **Elastic IP**                   | IP 변경 방지를 위해 Elastic IP 설정                                |
|                       | **JAR 파일 업로드**              | Spring Boot Application의 빌드된 JAR 파일을 EC2에 업로드            |
|                       | **애플리케이션 실행**            | EC2에서 JAR 파일 빌드 및 실행                                      |

### ✅ 요청 헤더 (회원가입/로그인 외 API 요청 시 필요)
| 헤더 이름       | 설명                           |
|----------------|------------------------------|
| Authorization  | `Bearer ...` (로그인 후 발급된 토큰 사용) |
