from PIL import Image, ImageDraw, ImageFont
import time
import re
import sys

header = '[云打印抽奖] QQ互动'
title = '2019年4月QQ互动获奖名单'
chapter = ['昵称', 'QQ号', '奖品']
string = '恭喜以上获奖的同学，我们将在近期发出本次活动的奖励，请有获奖的同学注意关注本平台抽奖动态，感谢您的参与，谢谢！'
n = 19
foot = [string[i:i + n] for i in range(0, len(string), n)]

# font and color
font_type = r'font\my_font.ttc'
header_font = ImageFont.truetype(font_type, 40)
title_font = ImageFont.truetype(font_type, 23)
chapter_font = ImageFont.truetype(font_type, 25)
email_font = ImageFont.truetype(font_type, 18)
list_font = ImageFont.truetype(font_type, 24)
foot_font = ImageFont.truetype(font_type, 20)
header_color = '#FFFFFF'
title_color = '#EE0000'
chapter_color = '#CD3333'
list_color = '#EE2C2C'
foot_color = '#EE3B3B'

# picture
img = 'img/mode.png'  # 图片模板
new_img = 'img/scholarship.png'  # 生成的图片
image = Image.open(img)
draw = ImageDraw.Draw(image)
width, height = image.size

# header
header_x = 38
header_y = 880
draw.text((header_x, height - header_y), u'%s' % header, header_color, header_font)

# title
title_x = header_x + 30
title_y = header_y - 140
draw.text((title_x, height - title_y), u'%s' % title, title_color, title_font)

# chapter
chapter_x = title_x - 20
chapter_y = title_y - 40
draw.text((chapter_x, height - chapter_y), u'%s' % chapter[0], chapter_color, chapter_font)
draw.text((chapter_x + 140, height - chapter_y), u'%s' % chapter[1], chapter_color, chapter_font)
draw.text((chapter_x + 270, height - chapter_y), u'%s' % chapter[2], chapter_color, chapter_font)

# student_list
data = sys.argv[1]
contents = data.split('\\r\\n')
student_list = []
size = len(contents) - 1
for i in range(0, size):
    item = []
    if contents[i].__contains__('):'):
        nick_name = re.findall(r'(.*?)\(', contents[i])
    elif contents[i].__contains__('>:'):
        nick_name = re.findall(r'(.*?)<', contents[i])
    if contents[i].__contains__('):'):
        qq = re.findall(r'\((.*?)\)', contents[i])
    elif contents[i].__contains__('>:'):
        qq = re.findall(r'<(.*?)>', contents[i])
    reward = re.findall(r':(.*?),', contents[i])
    item.append(nick_name[0])
    item.append(qq[0])
    item.append(reward[0])
    student_list.append(item)
# list
list_x = chapter_x - 20
list_y = chapter_y - 40
for student in student_list:
    for i in range(0, len(student)):
        if student[i].__contains__('@'):
            draw.text((list_x + i * 140, height - list_y), u'%s' % student[i], list_color, email_font)
        else:
            draw.text((list_x + i * 140, height - list_y), u'%s' % student[i], list_color, list_font)
    list_y = list_y - 40
#foot
foot_x = chapter_x - 30
foot_y = list_y - 40
for i in range(0, len(foot)):
    foot_y = foot_y - 40
    draw.text((foot_x, height - foot_y), u'%s' % foot[i], foot_color, foot_font)
draw.text((chapter_x + 30, height - (foot_y - 40)), u'%s(云打印)' % time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()), foot_color, foot_font)

image.save(new_img, 'png')

