import tkinter as tk
from modelo import NotasModel
from vista import VistaNotas
from controlador import ControladorNotas

if __name__ == "__main__":
    root = tk.Tk()
    modelo = NotasModel()
    vista = VistaNotas(root)
    controlador = ControladorNotas(vista, modelo)
    root.mainloop()