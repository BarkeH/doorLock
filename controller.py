import numpy as np
from io import BytesIO

from photos import photoCapture
from train import retrain
from recogniseWithName import recognise

import serial

import pickle

data = pickle.loads(open("encodings.pickle", "rb").read())

arduino = serial.Serial(port='/dev/ttyACM0')
arduino.flush()

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
            send_string = "l\n"
            arduino.write(send_string.encode('utf-8'))
            socket.send(b"done")
        elif message == b"unlock":
            send_string = "u\n"
            arduino.write(send_string.encode('utf-8'))
            socket.send(b"done")
        elif message == b"camera":
            frame = recognise(data)
            np_bytes = BytesIO()
            np.save(np_bytes, frame, allow_pickle=True)

            socket.send(np_bytes.getvalue())
        else:
            socket.send(b"failed")
    except zmq.Again:
        print("timeout")
