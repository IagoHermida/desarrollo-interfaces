import threading
import time
import random
import datetime
from recursos import descargar_imagen

class GameModel:
    def __init__(self, difficulty, player_name, cell_size=100):
        self.difficulty = difficulty
        self.player_name = player_name
        self.cell_size = cell_size
        self.board = []
        self.images = {}
        self.hidden_image = None
        self.pairs_found = 0
        self.start_time = None
        self.moves = 0
        self.images_loaded = threading.Event()
        self._generate_board()
        self._load_images()

    def _generate_board(self):
        # Crear un conjunto de pares de identificadores de imágenes
        num_pairs = {
            "fácil": 8,
            "medio": 18,
            "difícil": 32
        }[self.difficulty]

        image_ids = list(range(num_pairs)) * 2
        random.shuffle(image_ids)
        self.board = image_ids

    def _load_images(self):
        # Iniciar la carga de imágenes en un hilo separado
        def load():
            url_base = "https://raw.githubusercontent.com/IagoHermida/di/refs/heads/master/imagenes/"
            self.hidden_image = descargar_imagen("https://raw.githubusercontent.com/IagoHermida/di/refs/heads/master/imagen.jpg", (self.cell_size, self.cell_size))
            for image_id in set(self.board):
                self.images[image_id] = descargar_imagen(url_base + f"{image_id+1}.png", (self.cell_size, self.cell_size))
            self.images_loaded.set()

        threading.Thread(target=load).start()

    def images_are_loaded(self):
        return self.images_loaded.is_set()

    def start_timer(self):
        self.start_time = time.time()

    def get_time(self):
        return int(time.time() - self.start_time)

    def check_match(self, pos1, pos2):
        self.moves += 1
        if self.board[pos1] == self.board[pos2]:
            self.pairs_found += 1
            return True
        return False

    def is_game_complete(self):
        return self.pairs_found == len(self.board) // 2

    def save_score(self):
        # Guardar la puntuación en un archivo
        score = {
            "name": self.player_name,
            "difficulty": self.difficulty,
            "moves": self.moves,
            "date": str(datetime.date.today())
        }
        # Cargar, actualizar y guardar las mejores puntuaciones
        scores = self.load_scores()
        scores[self.difficulty].append(score)
        scores[self.difficulty] = sorted(scores[self.difficulty], key=lambda x: x["moves"])[:3]
        with open("ranking.txt", "w") as file:
            file.write(str(scores))

    def load_scores(self):
        # Cargar las puntuaciones desde un archivo
        try:
            with open("ranking.txt", "r") as file:
                return eval(file.read())
        except FileNotFoundError:
            return {"fácil": [], "medio": [], "difícil": []}