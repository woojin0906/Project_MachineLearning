from flask import Flask

app = Flask(__name__)

@app.route('/')
def index():
    hi = 'hello world'
    return hi

if __name__ == '__main__':
    app.run(debug=True)