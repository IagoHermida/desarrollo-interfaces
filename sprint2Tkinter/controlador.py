import threading
import requests
from PIL import Image, ImageTk
import io
import tkinter as tk

class ControladorNotas:
    def __init__(self, vista, modelo):
        self.vista = vista
        self.modelo = modelo

        # Asignar funciones a los botones
        self.vista.boton_agregar.config(command=self.agregar_nota)
        self.vista.boton_eliminar.config(command=self.eliminar_nota)
        self.vista.boton_guardar.config(command=self.guardar_notas)
        self.vista.boton_cargar.config(command=self.cargar_notas)
        self.vista.boton_descargar_imagen.config(command=self.descargar_imagen)

        # Cargar notas al iniciar
        self.cargar_notas()

    def agregar_nota(self):
        #Agrega una nueva nota.
        nueva_nota = self.vista.entry_nota.get()
        if nueva_nota:
            self.modelo.agregar_nota(nueva_nota)
            self.vista.actualizar_listbox(self.modelo.obtener_notas())
            self.vista.entry_nota.delete(0, tk.END)

    def eliminar_nota(self):
        #Elimina la nota seleccionada.
        try:
            indice = self.vista.listbox.curselection()[0]
            self.modelo.eliminar_nota(indice)
            self.vista.actualizar_listbox(self.modelo.obtener_notas())
        except IndexError:
            self.vista.mostrar_mensaje("Selecciona una nota para eliminar.")

    def guardar_notas(self):
        #Guarda las notas en el archivo de texto.
        self.modelo.guardar_notas()
        self.vista.mostrar_mensaje("Notas guardadas con éxito.")

    def cargar_notas(self):
        #Carga las notas desde el archivo de texto.
        self.modelo.cargar_notas()
        self.vista.actualizar_listbox(self.modelo.obtener_notas())

    def descargar_imagen(self):
        #Descarga una imagen desde GitHub en un hilo separado.
        def descargar():
            url = "https://github.com/IagoHermida/di/blob/master/imagen.jpg?raw=true"
            try:
                respuesta = requests.get(url)
                respuesta.raise_for_status()
                imagen_bytes = io.BytesIO(respuesta.content)
                imagen = Image.open(imagen_bytes)
                imagen = imagen.resize((200, 200))
                imagen_tk = ImageTk.PhotoImage(imagen)
                self.vista.label_imagen.config(image=imagen_tk)
                self.vista.label_imagen.image = imagen_tk  # Evita que la imagen sea recolectada por el GC
            except Exception as e:
                self.vista.mostrar_mensaje(f"Error al descargar la imagen: {e}")

        hilo = threading.Thread(target=descargar)
        hilo.start()