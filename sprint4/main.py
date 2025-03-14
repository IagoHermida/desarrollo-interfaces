import tkinter as tk
from controlador import GameController
from modelo import GameModel

if __name__ == "__main__":
    # Crear la ventana raíz de la aplicación
    root = tk.Tk()
    root.title("Juego de Memoria")

    # Crear el modelo del juego con configuración inicial
    model = GameModel(difficulty="fácil", player_name="Jugador")

    # Crear el controlador y pasar la ventana raíz
    controller = GameController(root, model)

    # Iniciar el bucle principal de la interfaz
    root.mainloop()