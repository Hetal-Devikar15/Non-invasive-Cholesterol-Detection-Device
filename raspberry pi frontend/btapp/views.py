from django.shortcuts import render
from django.http import JsonResponse, StreamingHttpResponse
import qrcode
import base64
import uuid
from io import BytesIO
import time
import RPi.GPIO as GPIO
import math
from sklearn.linear_model import LinearRegression
import numpy as np

cholesterol_conc_mmol_L = [2.5, 5.0, 7.5, 10.0]
cholesterol_conc_mg_dL = [x * 38.67 for x in cholesterol_conc_mmol_L]
absorbances = [0.295, 0.526, 0.714, 0.949]

# Reshape data for sklearn
X = np.array(cholesterol_conc_mg_dL).reshape(-1, 1)
y = np.array(absorbances)

# Create and fit the model
inverse_model = LinearRegression()
inverse_model.fit(y.reshape(-1, 1), X)

S0 = 21
S1 = 20
S2 = 16
S3 = 26
sensorOut = 19

GPIO.setmode(GPIO.BCM)
GPIO.setup(S0, GPIO.OUT)
GPIO.setup(S1, GPIO.OUT)
GPIO.setup(S2, GPIO.OUT)
GPIO.setup(S3, GPIO.OUT)
GPIO.setup(sensorOut, GPIO.IN)

GPIO.output(S0, GPIO.HIGH)
GPIO.output(S1, GPIO.LOW)

def index(request):
    return render(request, 'index.html')

def get_bt_mac():
    mac = uuid.getnode()
    mac_address = ''.join(("%012X" % mac) [i:i+2] for i in range(0, 12, 2))
    return mac_address

def qr_code(request):
    pairing_info = get_bt_mac()
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    qr.add_data(pairing_info)
    qr.make(fit=True)

    img = qr.make_image(fill='black', back_color='white')
    
    buffered = BytesIO()
    img.save(buffered, format="PNG")
    img_str = base64.b64encode(buffered.getvalue()).decode()

    return JsonResponse({'qr_code': img_str, 'code': pairing_info})

def get_pulse_width(S2_state, S3_state):
    GPIO.output(S2, S2_state)
    GPIO.output(S3, S3_state)
    start_time = time.time()
    while GPIO.input(sensorOut) == GPIO.LOW:
        pass
    while GPIO.input(sensorOut) == GPIO.HIGH:
        pass
    pulse_width = time.time() - start_time
    return pulse_width

def get_red_pw():
    return get_pulse_width(GPIO.LOW, GPIO.LOW)

def get_green_pw():
    return get_pulse_width(GPIO.HIGH, GPIO.HIGH)

def get_blue_pw():
    return get_pulse_width(GPIO.LOW, GPIO.HIGH)

def sse_view(request):
    def event_stream():
        while True:

            red_pw = get_red_pw()
            time.sleep(0.2)
            green_pw = get_green_pw()
            time.sleep(0.2)
            blue_pw = get_blue_pw()
            time.sleep(0.2)

            A = -math.log10((0.299 * red_pw + 0.587 * green_pw + 0.114 * blue_pw) / 255.0)
            C = inverse_model.predict(np.array([[A]]))[0]

            data = f"Your cholesterol concentration is {C} mg/dL."
            yield data
    response = StreamingHttpResponse(event_stream(), content_type='text/event-stream')
    response['Cache-Control'] = 'no-cache'
    return response

    
