import torch
from torch import nn
from torch.utils.data import Dataset
from transformers import BertModel, BertTokenizer
import numpy as np
import gluonnlp as nlp
from kobert_tokenizer import KoBERTTokenizer
from transformers import BertModel
from transformers import AdamW
from transformers.optimization import get_cosine_schedule_with_warmup




model_path = 'SeungwanKOBERT/models/7emotions_model.pt'
state_dict_path = 'SeungwanKOBERT/models/7emotions_model_state_dict.pt'
checkpoint_path = 'SeungwanKOBERT/models/7emotions_all.tar'

model1 = torch.load('7emotions_model.pt')  # 전체 모델을 통째로 불러옴, 클래스 선언 필수
model1.load_state_dict(torch.load('7emotions_model_state_dict.pt'))  # state_dict를 불러 온 후, 모델에 저장

checkpoint = torch.load('7emotions_all.tar')   # dict 불러오기
model1.load_state_dict(checkpoint['model'])
optimizer.load_state_dict(checkpoint['optimizer'])

#토큰화
tokenizer = get_tokenizer()
tok = nlp.data.BERTSPTokenizer(tokenizer, vocab, lower=False)

def predict(predict_sentence):

    data = [predict_sentence, '0']
    dataset_another = [data]

    another_test = BERTDataset(dataset_another, 0, 1, tok, max_len, True, False)
    test_dataloader = torch.utils.data.DataLoader(another_test, batch_size=batch_size, num_workers=5)
    
    model1.eval()

    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(tqdm_notebook(test_dataloader)):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)

        valid_length= valid_length
        label = label.long().to(device)

        out = model1(token_ids, valid_length, segment_ids)


        test_eval=[]
        for i in out:
            logits=i
            logits = logits.detach().cpu().numpy()

            if np.argmax(logits) == 0:
                test_eval.append("공포가")
            elif np.argmax(logits) == 1:
                test_eval.append("놀람이")
            elif np.argmax(logits) == 2:
                test_eval.append("분노가")
            elif np.argmax(logits) == 3:
                test_eval.append("슬픔이")
            elif np.argmax(logits) == 4:
                test_eval.append("중립이")
            elif np.argmax(logits) == 5:
                test_eval.append("행복이")
            elif np.argmax(logits) == 6:
                test_eval.append("혐오가")

            #test_eval.append(np.argmax(logits))

        print(">> 입력하신 내용에서 " + test_eval[0] + " 느껴집니다.")

predict_sentence = '영화에 나오는 귀신이 너무 무섭네요'

predict(predict_sentence)