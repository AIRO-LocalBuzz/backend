# AIRO
지역 활성화를 위해 소상공인 홍보 플랫폼 API 서버입니다. <br>

## 목차
[0. 팀원 소개](#팀원-소개) <br>
[1. 주요 기능](#주요-기능) <br>
[2. 아키텍처](#아키텍처) <br>
[3. 기술스택](#기술스택) <br>
[4. 협업](#협업) <br>
<br>

## BE 팀원 소개
| 이종원 | 김윤영 |
|:------:|:------:|
| <img src="https://github.com/user-attachments/assets/b54dfe23-3600-4a08-919e-4e8329dd4649" alt="이종원" width="150"> | <img src="https://github.com/user-attachments/assets/a7060d86-3eea-470a-8508-e65de99c1ea9" alt="김윤영" width="150"> |
| BE | BE |
| [GitHub](https://github.com/LJW22222) | [GitHub](https://github.com/yunrry) |
<br>

## 주요 기능
- 각 지역의 문화/축제/소상공인/농업 체험을 확인할 수 있습니다.
  - 지역별 필터링을 통해 빠르게 정보를 찾아볼 수 있습니다.
- 게시글을 작성하여 사용자가 해당 문화/축제/소상공인/농업 체험을 홍보 할 수 있습니다.
  - 작성된 게시글을 통해 사용자에게 100점의 포인트를 적립하고 이력을 기록합니다.
  - 작성된 게시글은 AI기능을 활용하여 간편하게 썸네일로 변환할 수 있습니다.
<br>

## 아키텍처
### BE Server
![image](https://github.com/user-attachments/assets/9c846780-07c9-4313-bbf4-6194227bb8b2)
<br>

### ERD
#### 문화/축제/소상공인 ERD
![image](https://github.com/user-attachments/assets/9d78f4f2-d08a-4009-a498-330c2a9095eb)

#### 사용자 관련 ERD
![image](https://github.com/user-attachments/assets/183849d5-66c8-4168-8163-c113ee838c5f)

#### 게시글 관련 ERD
![image](https://github.com/user-attachments/assets/612a228d-2b07-4f4b-8efb-7197ff92c597)
<br>

## 기술스택
### Backend
- **언어 및 프레임워크**: Java 21, Spring Boot 3.3.7
- **데이터 처리**: Spring Data JPA
- **RDB**: MySQL 8.0.41
- **NoSQL**: Redis latest [ 7(7.4.5) ]
- **테스트**: JMter
- **협업 Tools**: Linear, WBS
<br>

### Infrastructure
- **컨테이너화**: Docker
- **CI/CD**: GitActions
- **Server**: 라즈베리파이4
<br>

## 협업
### 기여 가이드
- 브랜치 전략: GitHub Flow
- PR 리뷰 프로세스: PR후 서로 코드리뷰 후, 이상이 없으면 머지
<br>

### Linear
<p align="center">
  <img src="https://github.com/user-attachments/assets/b5c377d5-3be7-4e8f-8a9a-33c5474ae48f" alt="image1" width="100%"/>
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/478a045d-545c-424a-b3fc-e7236be105d1" alt="image2" width="100%"/>
</p>

