# Non-Invasive Cholesterol Detection (NICD)

![Socialify Image](https://socialify.git.ci/xn-coder/NICD/image?font=Bitter&language=1&name=1&owner=1&pattern=Circuit+Board&theme=Dark)

## Description

The Non-Invasive Cholesterol Detection (NICD) project aims to provide a seamless and non-invasive method to measure cholesterol levels using IoT devices. The system utilizes a combination of hardware components and software applications to gather user data, process sensor inputs, and predict cholesterol levels using machine learning models.

## Shield.io Badges

![GitHub issues](https://img.shields.io/github/issues/xn-coder/NICD)
![GitHub forks](https://img.shields.io/github/forks/xn-coder/NICD)
![GitHub stars](https://img.shields.io/github/stars/xn-coder/NICD)
![GitHub license](https://img.shields.io/github/license/xn-coder/NICD)

## Project Demo

A demo of the project can be accessed [here](#). *(Replace with actual link)*

## Project Screenshots

![Screenshot 1](#) *(Replace with actual image link)*
![Screenshot 2](#) *(Replace with actual image link)*

## Features

- **User Information Input**: Start by scanning a QR code to input user details such as name, age, gender, weight, and lifestyle factors (drinker, smoker, blood pressure, hypertension).
- **Sensor Integration**: Utilizes TCS34725 and TCS3200 color sensors to gather data from the user.
- **Machine Learning Prediction**: Processes sensor data through a machine learning model to predict cholesterol levels.
- **Cross-Platform Compatibility**: Android app for user interaction and data transmission to Raspberry Pi.
- **User-Friendly Interface**: Display results on an LCD screen for easy reading.

## Hardware Components

- **Color Sensors**: TCS34725 & TCS3200
- **Raspberry Pi 4**
- **Arduino Uno**
- **LCD Screen**

## Software Components

- **Frontend**: Python Django
- **Machine Learning**: Python
- **Mobile App**: Java

## Getting Started

1. **Clone the Repository**: 
   ```bash
   git clone https://github.com/xn-coder/NICD.git
   ```

2. **Set Up Hardware**: Connect the sensors and LCD screen to the Raspberry Pi and Arduino Uno as per the circuit diagram.

3. **Install Dependencies**: 
   - For Python: 
     ```bash
     pip install -r requirements.txt
     ```
   - For Java: Ensure you have the necessary Android development environment set up.

4. **Run the Application**: 
   - Start the Django server for the frontend.
   - Deploy the Android app on a compatible device.

5. **Usage**: 
   - Scan the QR code to input user data.
   - Follow on-screen instructions to place your finger on the sensors.
   - View the predicted cholesterol level on the LCD screen.

## Contributing

Contributions are welcome! Please read the [contributing guidelines](CONTRIBUTING.md) for more details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries, please contact [your-email@example.com](mailto:your-email@example.com).
