from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import requests
from transformers import T5Tokenizer, T5ForConditionalGeneration

app = FastAPI()

origins = [
    "http://localhost.tiangolo.com",
    "https://localhost.tiangolo.com",
    "http://localhost",
    "http://localhost:8080",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/ai/generate")
def ai_generate(query: str):
    tokenizer = T5Tokenizer.from_pretrained("google/flan-t5-small")
    model = T5ForConditionalGeneration.from_pretrained("google/flan-t5-small")

    input_text = query
    print(f"Received query: {input_text}")
    input_ids = tokenizer(input_text, return_tensors="pt").input_ids

    outputs = model.generate(
        input_ids,
        max_new_tokens=100   # ⭐关键修复
    )

    return {"text": tokenizer.decode(outputs[0], skip_special_tokens=True)}