import serial
import time

# Configura a porta serial e a taxa de transmissão (baud rate)
ser = serial.Serial('/dev/ttyACM1', 115200)  # Altere 'COM3' pela sua porta serial (em Linux ou MacOS, pode ser algo como '/dev/ttyUSB0')

# Aguarda um momento para garantir que a conexão está estabelecida
time.sleep(2)

print("Conectado ao Arduino!")

try:
    while True:
        if ser.in_waiting > 0:  # Verifica se há dados na fila de leitura
            # Lê a linha de dados enviada pelo Arduino
            linha = ser.readline().decode('utf-8').strip()
            print(f"-> {linha}")

except KeyboardInterrupt:
    print("Encerrando a leitura serial...")

finally:
    # Fecha a conexão serial
    ser.close()
    print("Conexão serial encerrada.")
