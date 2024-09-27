import serial
import time

# Configurações da porta serial
port = '/dev/ttyUSB0'  # Substitua pelo nome da sua porta serial
baudrate = 115200  # Velocidade da comunicação, ajuste conforme o ESP32

# Lista de números a serem enviados
numeros = [45, 46, 47, 44, 40, 43, 45, 46, 47, 44, 40, 43, 45, 46, 47, 44, 40, 43, 45, 46, 47, 44, 40, 43]

try:
    # Abre a conexão serial
    with serial.Serial(port, baudrate, timeout=1) as ser:
        print(f"Conectado à porta {port} a {baudrate} bps")
        
        # Envia os números com delay de 2 segundos
        for numero in numeros:
            ser.write(f"{numero}\n".encode())  # Envia o número como string seguida de uma nova linha
            print(f"Enviado: {numero}")
            time.sleep(2)  # Delay de 2 segundos

except serial.SerialException as e:
    print(f"Erro ao abrir a porta serial: {e}")
