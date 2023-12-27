const express = require('express');
const axios = require('axios');

const app = express();
const PORT = 3000;

app.use(express.json());

app.get('/kakao-map-api', async (req, res) => {
    const keyword = req.query.query;
    const kakaoApiKey = 'f32e49f5dedd2c37722a3d4f1ada6317'; // 실제 사용하는 API 키로 변경

    // 지역 검색 API URL로 수정
    const apiUrl = `https://dapi.kakao.com/v2/local/search/keyword.json?query=${encodeURIComponent(keyword)}`;
    
    try {
        const response = await axios.get(apiUrl, {
            headers: { 'Authorization': `KakaoAK ${kakaoApiKey}` }
        });
        res.send(response.data);
    } catch (error) {
      console.error(error);
      if (error.response) {
          // 서버가 응답한 상태 코드와 메시지가 있다면 그것을 사용
          res.status(error.response.status).send(error.response.data);
      } else {
          // 기타 네트워크 오류 등의 경우
          res.status(500).send('Internal Server Error');
      }
  }
});

app.listen(PORT, () => {
    console.log(`서버가 http://localhost:${PORT}에서 실행 중입니다.`);
});
