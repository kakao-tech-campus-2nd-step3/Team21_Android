# Team21_Android
21조 안드로이드

## 코드 리뷰 받고 싶은 부분 (개발 질문): 6주차

### todlf
[이슈 #20](https://github.com/kakao-tech-campus-2nd-step3/Team21_Android/issues/29)
[이슈 #25](https://github.com/kakao-tech-campus-2nd-step3/Team21_Android/issues/30)
- FCM으로 알림을 받을 때, 백그라운드에서도 remoteView를 이용한 커스텀 알림이 가능한지 궁금합니다.
- 앱이 종료되었는데 알림이 올 경우, 알림에서 이모지를 눌러서 백엔드에 데이터를 보낼 수 있는지 궁금합니다.(이모지 상호작용 알림은 포그라운드 알림에서 처리하는게 나을까요?)
- 백엔드에서 알림 API를 받아와서 FCM으로 핸드폰으로 알림을 보내주는 걸 구현하려고 하는데, 
- 이 과정을 어떤 식으로 처리하는지 궁금합니다.(어떻게 해야할지 감이 안 잡힙니다 ㅠㅠ)
- #29,30 이슈 해결방법이 궁금합니다.

---

### arieum
- 이번주 PR에는 서버주소랑 jwtToken이 하드코딩되어있습니다! 다음 PR에는 추상화해서 올리겠습니다
- friend_item.xml 에서 모든 뷰들을 layout_gravity를 center로 설정해도 이미지와 텍스트뷰가 가운데정렬이 되지 않습니다. vertical_center로 설정해도 안되는데 어떻게 처리해야하는지 궁금합니다
- 5주차 코드리뷰 이제 확인해서 그부분도 다음 PR때 반영해서 올리겠습니다

---

### settle54
[이슈 #36](https://github.com/kakao-tech-campus-2nd-step3/Team21_Android/issues/20)
- 이슈 36에 대한 해결방법을 알고 싶습니다.
- 불필요하거나 비효율적인 코드가 있으면 개선받고 싶습니다!
