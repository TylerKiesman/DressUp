import pandas as pd

class colorDataframe():
    def __init__(self):
        init_color = [0,0,0,0,0,0,0,0,0,0,0]
        colors = ['Red', 'Blue', 'Green', 'Yellow', 'Pink', 'Purple', 'Orange', 'Black', 'White', 'Gray', 'Brown']
        data = {'Red':init_color, 'Blue':init_color, 'Green':init_color,
                'Yellow':init_color, 'Pink':init_color, 'Purple':init_color, 'Orange':init_color,
                'Black':init_color, 'White':init_color, 'Gray':init_color, 'Brown':init_color}
        self.data_frame = pd.DataFrame(data, index=colors)

    def add_count(self, color1, color2):
        self.data_frame.loc[color1, color2] += 1

    def save_frame(self, path):
        self.data_frame.to_csv(path)