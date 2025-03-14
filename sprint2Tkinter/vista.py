import tkinter as tk
from tkinter import messagebox


class VistaNotas:
    def __init__(self, root):
        self.root = root
        self.root.title("Gestor de Notas")
        self.root.geometry("400x600")

        # Título
        self.label_titulo = tk.Label(root, text="Gestor de Notas", font=("Arial", 16))
        self.label_titulo.pack(pady=10)

        # Coordenadas de clic
        self.label_coordenadas = tk.Label(root, text="Coordenadas: (X, Y)", font=("Arial", 10))
        self.label_coordenadas.pack(pady=5)

        # Entrada para nueva nota
        self.entry_nota = tk.Entry(root, width=40)
        self.entry_nota.pack(pady=5)

        # Listbox para mostrar notas
        self.listbox = tk.Listbox(root, height=10, width=40)
        self.listbox.pack(pady=5)

        # Botones
        self.boton_agregar = tk.Button(root, text="Agregar Nota")
        self.boton_agregar.pack(pady=5)

        self.boton_eliminar = tk.Button(root, text="Eliminar Nota")
        self.boton_eliminar.pack(pady=5)

        self.boton_guardar = tk.Button(root, text="Guardar Notas")
        self.boton_guardar.pack(pady=5)

        self.boton_cargar = tk.Button(root, text="Cargar Notas")
        self.boton_cargar.pack(pady=5)

        self.boton_descargar_imagen = tk.Button(root, text="Descargar Imagen")
        self.boton_descargar_imagen.pack(pady=5)

        # Label para la imagen
        self.label_imagen = tk.Label(root)
        self.label_imagen.pack(pady=10)

        # Bind para capturar clics fuera de los botones
        self.root.bind("<Button-1>", self.actualizar_coordenadas)

    def actualizar_coordenadas(self, event):
        #Actualiza el Label con las coordenadas del clic.
        self.label_coordenadas.config(text=f"Coordenadas: ({event.x}, {event.y})")

    def mostrar_mensaje(self, mensaje):
        #Muestra un mensaje de confirmación.
        messagebox.showinfo("Información", mensaje)

    def actualizar_listbox(self, notas):
        #Actualiza el Listbox con las notas.
        self.listbox.delete(0, tk.END)
        for nota in notas:
            self.listbox.insert(tk.END, nota)