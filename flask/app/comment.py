import torch
from transformers import AutoTokenizer, AutoModelForSequenceClassification
device = torch.device('cuda:0' if torch.cuda.is_available() else 'cpu')


MODEL_NAME = "beomi/KcELECTRA-base"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)

class CurseDataset(torch.utils.data.Dataset):
    def __init__(self, encodings, labels):
        self.encodings = encodings
        self.labels = labels

    def __getitem__(self, idx):
        item = {key: torch.tensor(val[idx]) for key, val in self.encodings.items()}
        item["labels"] = torch.tensor(self.labels[idx])
        return item

    def __len__(self):
        return len(self.labels)
    

model = AutoModelForSequenceClassification.from_pretrained(MODEL_NAME, num_labels=2)
model.to(device)
model.load_state_dict(torch.load('app/comment_test_model_state_dict.pt', map_location='cpu'))

# 0: curse, 1: non_curse
def sentence_predict(sent):
    # 평가모드로 변경
    model.eval()

    # 입력된 문장 토크나이징
    tokenized_sent = tokenizer(
        sent,
        return_tensors="pt",
        truncation=True,
        add_special_tokens=True,
        max_length=128
    )
    
    # 모델이 위치한 GPU로 이동 
    tokenized_sent.to(device)

    # 예측
    with torch.no_grad():
        outputs = model(
            input_ids=tokenized_sent["input_ids"],
            attention_mask=tokenized_sent["attention_mask"],
            token_type_ids=tokenized_sent["token_type_ids"]
            )

    # 결과 return
    logits = outputs[0]
    logits = logits.detach().cpu()
    result = logits.argmax(-1)
    if result == 0:
        result = " >> 악성댓글 👿"
    elif result == 1:
        result = " >> 정상댓글 😀"
    return result


print(sentence_predict("아 진짜 그냥 죽어라"))