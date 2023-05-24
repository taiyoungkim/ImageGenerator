# 놓친 케이스 추가

## 기술명세서
1. 유저가 특수 문자나 텍스트가 아닌 문자를 입력한 경우
    1. 한글, 영어, 숫자만 허용
2. ChatGPT의 Reponse가 잘못된 경우
    1. 프롬프트를 가져오는데 문제가 발생했다는 에러 메세지 보여주기
3. Response 기다리는 동안 loading 표시하기
    1. request를 하면 loading 표시
    2. response를 받으면 loading 숨기기
4. 이미지가 4개 미만인 경우
    1. maximum을 4개로 해서 1~4개는 reponse 받는 경우 그대로 보여주기
    2. 0개인 경우 에러가 발생 했다는 메세지 보여주기
5. 이미지 응답이 손상되었거나 렌더링할 수 없는 경우 에러가 발생 했다는 메세지 보여주기

## 테스트 케이스
```kotlin
@Test
fun acceptUserInput_IfSpecialOrNonTextCharacters_OnlyAllowKoreanEnglishNumeric() {}

@Test
fun handleResponse_IfResponseIsIncorrect_ShowsPromptRetrievalErrorMessage() {}

@Test
fun showLoadingIndicator_OnRequest_SubmitsAndHidesAfterResponse() {}

@Test
fun handleImageResponse_IfLessThanFourImages_DisplayReceivedImages() {}
@Test
fun handleImageResponse_IfNoImagesReceived_ShowsErrorMessage() {}

@Test
fun handleImageResponse_IfImageCorruptOrCannotBeRendered_ShowsErrorMessage() {}
```