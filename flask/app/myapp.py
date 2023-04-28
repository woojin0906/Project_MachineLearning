from flask import Flask, request, jsonify
import pickle

app = Flask(__name__)

# 피클 파일에서 훈련된 모델을 로드
with open('simplelinear.pickle', 'rb') as f:
    model = pickle.load(f)


@app.route('/') #루트
def index():
    hi = 'hello world'
    return hi

@app.route("/sign", methods=['POST']) #포스트 방식 테스트
def sign():
    temp = request.form.get("user")
    return temp

# 간단한 선형 회귀 모델을 통한 api
@app.route('/predict', methods=['POST'])
def predict():
    # 요청 데이터
    data = request.get_json(force=True)

    # 입력 값이 단일 숫자인 경우 => 배열로 변환
    input_data = [data['input']] if isinstance(data['input'], (int, float)) else data['input']

    # 예측 수행
    prediction = model.predict([input_data])[0]

    # JSON 반환
    return jsonify({'output': prediction})

if __name__ == '__main__':
    app.run(debug=True)