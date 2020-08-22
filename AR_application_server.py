import socket
import base64
from os import makedirs
from os.path import exists
import io
import time
import PIL
from PIL import Image
from PIL import ImageDraw
from PIL import ImageFont
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True

HOST = socket.gethostbyname(socket.gethostname()) # Standard loopback interface address (localhost)
PORT = 2530       # Port to listen on (non-privileged ports are > 1023)
TIME = time.time() + 60 * 15
BUFFER_SIZE = 960 * 1280

def decode(to_decode):
    return base64.b64decode(to_decode)

#socket.AF_INET: the internet address family for IPv4
#SOCK_STREAM: the socket type for TCP
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server:
    print('Waiting for connection...')
    server.bind((HOST, PORT))
    server.listen()
    client, addr = server.accept()
    with client:
        print('Connected by', addr)
        while time.time() < TIME:
            data = client.recv(BUFFER_SIZE)
            data_str = data.decode()
            # print("data data type:",type(data))
            #check if the data received is text or image
            header = data_str[:4]
            if header == "0000": #text
                text = data_str[4:]
                print("Text Received! \n>", text)
            elif header == "0001": #image
                print("Image Received! Decoding...")
                image = data_str[4:]
                print("image length:",len(image))
                image = decode(image)
                print("image data type:",type(image))

                
                dir = './received'

                if not exists(dir):
                    makedirs(dir)

                path = dir+'/'+time.strftime("%Y%m%d-%H%M%S")+'.jpg'
                stream = io.BytesIO(image)
                img = Image.open(stream)
                img.save(path)

                img.show()
                
            if not data:
                break
            client.sendall(data)


