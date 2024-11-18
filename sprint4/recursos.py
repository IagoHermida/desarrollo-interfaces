import requests
from PIL import Image, ImageTk
import io

def descargar_imagen(url, size):
    try:
        # Realizar la solicitud HTTP para descargar la imagen
        response = requests.get(url)
        response.raise_for_status()

        # Abrir la imagen desde el flujo de bytes
        image = Image.open(io.BytesIO(response.content))
        image = image.resize(size, Image.LANCZOS)

        # Convertir la imagen a un formato que Tkinter pueda usar
        return ImageTk.PhotoImage(image)
    except requests.RequestException as e:
        print(f"Error al descargar la imagen: {url} - {e}")
        return None