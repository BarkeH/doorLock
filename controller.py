import time
import zmq
import cv2

from photos import photoCapture
from train import retrain
from recogniseWithName import recognise

import pickle

data = pickle.loads(open("encodings.pickle", "rb").read())


context = zmq.Context()
socket = context.socket(zmq.REP)
socket.bind("tcp://*:4444")
socket.RCVTIMEO = 1000
while True:
    try:
        message = socket.recv()
        print("Received request: {}".format(message))
        if message == b"take photo":
            photoCapture("bob")
            socket.send(b"done")
        elif message == b"retrain":
            retrain()
            socket.send(b"done")
        elif message == b"lock":
            print("TODO Lock")
            socket.send(b"done")
        elif message == b"unlock":
            print("TODO unlock")
            socket.send(b"done")
        else:
            socket.send(b"failed")
    except zmq.Again:
        print("timeout")

    recognise(data,cv2)
