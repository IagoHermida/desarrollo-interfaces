import os

class NotasModel:
    def __init__(self):
        self.notas = []

    def agregar_nota(self, nueva_nota):
        #Añade una nueva nota a la lista.
        self.notas.append(nueva_nota)

    def eliminar_nota(self, indice):
        #Elimina la nota en el índice especificado.
        if 0 <= indice < len(self.notas):
            del self.notas[indice]

    def obtener_notas(self):
        #Devuelve la lista de notas.
        return self.notas

    def guardar_notas(self):
        #Guarda las notas en un archivo de texto.
        with open('notas.txt', 'w') as file:
            for nota in self.notas:
                file.write(nota + '\n')

    def cargar_notas(self):
        #Carga las notas desde un archivo de texto.
        if os.path.exists('notas.txt'):
            with open('notas.txt', 'r') as file:
                self.notas = [line.strip() for line in file.readlines()]