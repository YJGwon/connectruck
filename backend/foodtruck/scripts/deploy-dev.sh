#!/usr/bin/env bash
PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/connectruck.jar"

APP_LOG="$PROJECT_ROOT/application.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 기존 app 종료
echo "$TIME_NOW > 실행중인 app 종료" > $DEPLOY_LOG
fuser -k -n tcp 8080

# 기존 jar 파일 삭제
echo "$TIME_NOW > $JAR_FILE 파일 삭제" >> $DEPLOY_LOG
rm $JAR_FILE

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp  $PROJECT_ROOT/*.jar $JAR_FILE

# jar 실행 권한 설정
echo "> $JAR_FILE 에 실행권한 추가"
sudo chmod +x $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar -Dspring.profiles.active=dev $JAR_FILE > $APP_LOG 2>&1 &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
