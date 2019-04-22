import re
import matplotlib.pyplot as plt
import jieba
from wordcloud import WordCloud
import seaborn as sns
import operator

class Data_analysis:
    def __init__(self):
        self.data = []
        self.filename = "record\PlusA.txt"

    #读取聊天记录文件
    def readFile(self):
        with open(self.filename, encoding="UTF-8") as file:
            self.data = file.read()

    # 各时间段发言统计
    def get_time(self):
        times = re.findall(r'\d{2}:\d{2}:\d{2}', self.data)
        Xi = [time.split(":")[0] for time in times]
        sns.countplot(Xi, order=[str(i) for i in range(0, 24)])
        plt.plot()
        plt.rcParams['font.sans-serif'] = ['SimHei']
        plt.title("各时间段发言统计")
        plt.xlabel("时间00:00—24:00")
        plt.ylabel("发言次数/次")
        plt.savefig(r"img\hour.png", format='png')
        plt.close()

    # 活跃用户统计
    def get_active(self):
        str_list = re.findall(r'\d{2}:\d{2}:\d{2} .*?\n', self.data)
        chat = {}
        i = 0
        for string in str_list:
            size = len(string) - 1
            dict2 = {}
            if string[9:size] != "系统消息(10000)":
                if not chat.__contains__(string[9:size]):
                    i = i + 1
                    dict2[string[0:2]] = 1
                    chat[string[9:size]] = dict2
                else:
                    if not chat[string[9:size]].__contains__(string[0:2]):
                        chat[string[9:size]][string[0:2]] = 1
                    else:
                        chat[string[9:size]][string[0:2]] = chat[string[9:size]][string[0:2]] + 1
        dict3 = {}
        for key, dic in chat.items():
            count = 0
            for val in dic.values():
                count += val
            dict3[key] = count
        result = dict(sorted(dict3.items(), key=operator.itemgetter(1), reverse=True))
        colors = ['red', 'green', 'blue', 'orange', 'black']
        Xi = [str(k) for k in range(0, 24)]
        i = 0
        for key in result.keys():
            if i >= 5:
                break
            Yi = []
            for j in range(0, 24):
                Yi.append(0)
            for key2 in chat[key].keys():
                Yi[int(key2)] = chat[key][key2]
            plt.plot(Xi, Yi, color=colors[i], label=key)
            i = i + 1
        plt.rcParams['font.sans-serif'] = ['SimHei']
        plt.xticks(range(len(Xi)))
        plt.legend()
        plt.title("活跃用户统计")
        plt.xlabel("时间00:00—24:00")
        plt.ylabel("发言次数/次")
        plt.savefig(r"img\active.png", format='png')
        plt.close()

    # 词云制作
    def get_wordcloud(self):
        pattern = re.compile(r'\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2} .*?\(\d+\)\n(.*?)\n', re.DOTALL)
        contents = re.findall(pattern, self.data)
        word_list = []
        for sentence in contents:
            sentence = sentence.replace("[表情]", "").replace("[图片]", "").replace("@全体成员", "")
            if sentence != "" and not sentence.__contains__("撤回了一条消息") and not sentence.__contains__("加入本群。") and \
                not sentence.__contains__('长按复制此消息，打开最新版支付宝就能领取！') and not sentence.__contains__('请使用新版手机QQ查收红.'):
                word_list.append(" ".join(jieba.cut(sentence.strip())))
        new_text = " ".join(word_list)
        wordcloud = WordCloud(background_color="white",
                              width=1200,
                              height=1000,
                              min_font_size=50,
                              font_path="simhei.ttf",
                              random_state=50,
                              )
        my_wordcloud = wordcloud.generate(new_text)
        plt.imshow(my_wordcloud)
        plt.axis("off")
        wordcloud.to_file(r'img\wordcloud.png')

def main():
    data_analysis = Data_analysis()
    data_analysis.readFile()
    data_analysis.get_time()
    data_analysis.get_active()
    data_analysis.get_wordcloud()

if __name__ == '__main__':
    main()
