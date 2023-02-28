# ZZIGMUG
식단 관리를 위한 음식 이미지 추출 및 섭취 영양소 계산 자동화 앱서비스

## 프로젝트 개요

- 개발기간: 2022. 04 ~ 2022. 09
- 개발인원: 총 3명 (1인 서버 개발)

<br>

## 프로젝트 설명

<img src="https://github.com/ZZIGMUG/ZZIGMUG-Server/blob/main/images/zzigmug_mockup.png" alt="zzigmug-mockup" border="0" width="1000px" />


ZZIGMUG은 음식 이미지 추출을 통해 사용자가 섭취한 칼로리와 영양소를 분석하는 앱서비스입니다. 
일, 주, 월별로 섭취한 영양소를 그래프로 분석하고 팔로잉을 맺은 친구들과 식단을 공유할 수 있습니다. 

<br>

## 구현 사항
- 식사 사진을 촬영하면 YOLOv5 기반 학습 모델을 통해 음식 데이터를 추출합니다.
- 공공 데이터 Open API를 활용해 섭취한 칼로리와 영양소를 자동으로 계산합니다.
- 주차 별로 섭취한 영양소 데이터를 분석해 그래프로 제공합니다.
- 팔로잉한 친구들과 식단을 공유하고 목표 칼로리 달성률을 기준으로 랭킹을 매깁니다.

<br>

## 사용기술

- `Kotlin`, `Spring Boot`, `Spring Data JPA`, `Spring Security`, `Querydsl`, `MySQL` 
- `YOLOv5`, `FastAPI`
- `Docker`, `AWS EC2`, `AWS RDS`, `AWS S3`, `GitHub Action`

<br>

## ERD

<img src="https://github.com/ZZIGMUG/ZZIGMUG-Server/blob/main/images/zzigmug_erd.png" alt="zzigmug-erd" border="0"/>

- `User`: 사용자 정보를 관리합니다.
- `Follow`: 사용자 간의 팔로잉/팔로우 정보를 저장합니다.
- `Food`: 추출할 수 있는 음식 종류 정보를 관리합니다. (음식 이름, 칼로리, 영양소 데이터)
- `Meal`: 사용자가 촬영한 음식 사진의 정보를 관리합니다. (이미지 URL, 촬영일시)
- `Dish`: Meal의 음식 이미지에서 추출된 음식 정보를 관리합니다. (섭취량 등)
