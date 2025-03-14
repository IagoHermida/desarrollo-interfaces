import tkinter as tk
from tkinter import messagebox, simpledialog
from modelo import GameModel
from vista import MainMenu, GameView
import time

class GameController:
    def __init__(self, root, model):
        self.root = root
        self.model = model
        self.selected = []
        self.timer_started = False

        # Crear el menú principal
        self.main_menu = MainMenu(
            root,
            start_game_callback=self.show_difficulty_selection,
            show_stats_callback=self.show_stats,
            quit_callback=root.quit
        )

    def show_difficulty_selection(self):
        # Pedir al jugador que elija una dificultad
        difficulty = simpledialog.askstring("Dificultad", "Elige: fácil, medio o difícil:")
        if difficulty in ["fácil", "medio", "difícil"]:
            # Pedir el nombre aquí
            player_name = self.main_menu.ask_player_name()
            if player_name:
                # Aquí pasamos el nombre directamente sin volver a pedirlo
                self.start_game(difficulty, player_name)

    def start_game(self, difficulty, player_name):
        # Mostrar una ventana de carga y crear el modelo del juego
        self.model = GameModel(difficulty, player_name)
        self.loading_window = tk.Toplevel()
        self.loading_window.title("Cargando...")
        tk.Label(self.loading_window, text="Cargando imágenes...").pack()
        self.root.after(100, self.check_images_loaded)

    def check_images_loaded(self):
        if self.model.images_are_loaded():
            self.loading_window.destroy()
            self.view = GameView(
                on_card_click_callback=self.on_card_click,
                update_move_count_callback=self.update_move_count,
                update_time_callback=self.update_time
            )
            self.view.create_board(self.model)
        else:
            self.root.after(100, self.check_images_loaded)

    def on_card_click(self, pos):
        if not self.timer_started:
            self.model.start_timer()
            self.update_time()
            self.timer_started = True

        self.selected.append(pos)
        if len(self.selected) == 2:
            self.handle_card_selection()

    def handle_card_selection(self):
        pos1, pos2 = self.selected
        # Obtener los identificadores de las imágenes en el tablero
        image_id1 = self.model.board[pos1]
        image_id2 = self.model.board[pos2]

        # Mostrar las imágenes seleccionadas
        self.view.update_board(pos1, self.model.images[image_id1])
        self.view.update_board(pos2, self.model.images[image_id2])

        self.model.moves += 1

        # Verifica si los identificadores coinciden
        if image_id1 == image_id2:
            self.model.pairs_found += 1
            if self.model.is_game_complete():
                messagebox.showinfo("¡Ganaste!",
                                    f"Completaste el juego en {self.model.get_time()} segundos y {self.model.moves} movimientos.")
                self.model.save_score()
                self.view.destroy()
        else:
            self.root.after(1000, lambda: self.view.reset_cards(pos1, pos2))

        # Actualiza el contador de movimientos
        self.update_move_count()
        # Limpiar la lista de seleccionados
        self.selected = []

    def update_move_count(self):
        self.view.update_move_count(self.model.moves)

    def update_time(self):
        self.view.update_time(self.model.get_time())
        if not self.model.is_game_complete():
            self.root.after(1000, self.update_time)

    def show_stats(self):
        stats = self.model.load_scores()
        self.main_menu.show_stats(stats)