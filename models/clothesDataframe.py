import pandas as pd

class clothesDataframe():
    def __init__(self):
        init_clothes = [0,0,0,0,0,0,0,0,0,0,0,0]
        articles = ['athletic-shorts', 'button-down', 'dress', 'hoodie', 'jeans', 'leggings', 'longsleeve',
                  'pants', 'skirt', 'shorts', 'tshirt', 'sweatshirt']
        data = {'athletic-shorts':init_clothes, 'button-down':init_clothes, 'dress':init_clothes,
                'hoodie':init_clothes, 'jeans':init_clothes, 'leggings':init_clothes, 'longsleeve':init_clothes,
                'pants':init_clothes, 'skirt':init_clothes, 'shorts':init_clothes, 'tshirt':init_clothes,
                'sweatshirt':init_clothes}
        self.data_frame = pd.DataFrame(data, index=articles)

    def add_count(self, cloth1, cloth2):
        self.data_frame.loc[cloth1, cloth2] += 1

    def save_frame(self, path):
        self.data_frame.to_csv(path)
