import tkinter as tk
from tkinter import simpledialog, Toplevel

class MainMenu:
    def __init__(self, root, start_game_callback, show_stats_callback, quit_callback):
        self.root = root
        self.root.title("Menú Principal")

        # Botones del menú
        tk.Button(root, text="Jugar", command=start_game_callback).pack()
        tk.Button(root, text="Estadísticas", command=show_stats_callback).pack()
        tk.Button(root, text="Salir", command=quit_callback).pack()

    def ask_player_name(self):
        return simpledialog.askstring("Nombre del Jugador", "Introduce tu nombre:")

    def show_stats(self, stats):
        # Mostrar las estadísticas en una ventana nueva
        stats_window = Toplevel(self.root)
        stats_window.title("Estadísticas")
        for difficulty, scores in stats.items():
            tk.Label(stats_window, text=f"{difficulty.capitalize()}:").pack()
            for score in scores:
                tk.Label(stats_window, text=f"{score['name']}: {score['moves']} movimientos").pack()

class GameView:
    def __init__(self, on_card_click_callback, update_move_count_callback, update_time_callback):
        self.window = None
        self.labels = []
        self.on_card_click_callback = on_card_click_callback
        self.update_move_count_callback = update_move_count_callback
        self.update_time_callback = update_time_callback
        self.hidden_image = None  # Asegúrate de definir el atributo aquí

    def create_board(self, model):
        self.window = Toplevel()
        self.window.title("Juego de Memoria")

        self.hidden_image = model.hidden_image  # Asignar la imagen oculta desde el modelo

        # Calcular el número de filas y columnas
        total_cards = len(model.board)
        columns = int(total_cards ** 0.5)
        if columns * columns < total_cards:
            columns += 1
        rows = (total_cards + columns - 1) // columns

        # Crear el tablero de etiquetas (cartas)
        for i, image_id in enumerate(model.board):
            row = i // columns
            col = i % columns
            label = tk.Label(self.window, image=self.hidden_image)
            label.grid(row=row, column=col)
            label.bind("<Button-1>", lambda e, pos=i: self.on_card_click_callback(pos))
            self.labels.append(label)

        # Crear y mostrar los contadores usando grid
        self.move_count_label = tk.Label(self.window, text="Movimientos: 0")
        self.move_count_label.grid(row=rows, column=0, columnspan=columns)
        self.timer_label = tk.Label(self.window, text="Tiempo: 0s")
        self.timer_label.grid(row=rows + 1, column=0, columnspan=columns)

    def update_board(self, pos, image_id):
        self.labels[pos].config(image=image_id)

    def reset_cards(self, pos1, pos2):
        self.labels[pos1].config(image=self.hidden_image)
        self.labels[pos2].config(image=self.hidden_image)

    def update_move_count(self, moves):
        self.move_count_label.config(text=f"Movimientos: {moves}")

    def update_time(self, time):
        # Verificar si la ventana sigue abierta antes de actualizar el temporizador
        if self.window and self.window.winfo_exists():
            self.timer_label.config(text=f"Tiempo: {time}s")

    def destroy(self):
        if self.window:
            self.window.destroy()
            self.labels = []