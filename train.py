import zmq

def retrain():
    print("im a monkey")
    return "success"

def lock():
    context = zmq.Context()
    socket = context.socket(zmq.REQ)
    socket.connect("tcp://localhost:4444")
    request = b'lock'
    print("sending request {} ...".format(request))
    socket.send(request)
    message = socket.recv()
    print("received reply {} [ {} ]".format(request,message))

    socket.close()
    context.term()

def unlock():
    context = zmq.Context()
    socket = context.socket(zmq.REQ)
    socket.connect("tcp://localhost:4444")
    request = b'unlock'
    print("sending request {} ...".format(request))
    socket.send(request)
    message = socket.recv()
    print("received reply {} [ {} ]".format(request,message))

    socket.close()
    context.term()

#def getImage():

