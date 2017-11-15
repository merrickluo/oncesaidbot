import io
import cjkwrap

from PIL import Image, ImageOps, ImageDraw, ImageFont

font = ImageFont.truetype('res/SourceHanSerif-Bold.ttc', 24)

def circle_avatar(avatar_file):
    img = Image.open(avatar_file)
    size = (220, 260)
    mask = Image.new('L', size, 0)
    draw = ImageDraw.Draw(mask)
    draw.ellipse((0, 0) + size, fill=255)

    output = ImageOps.fit(img, mask.size, centering=(0.5, 0))
    output.putalpha(mask)
    return output

# 'A' draw size is (18, 28)
# '我' draw size is (24, 31)
def name_width(name):
    width = 0
    for c in name:
        width += 24 if cjkwrap.is_wide(c) else 18
    return width

# background size 589x800

def make_said(avatar_file, text, name):
    bg = Image.open('res/background.png')
    avatar = circle_avatar(avatar_file)
    bg.paste(avatar, (179, 128), avatar)

    draw = ImageDraw.Draw(bg)

    text_y = 420 - 36
    for idx, t in enumerate(cjkwrap.wrap(text, 24)):
        text_y += 36
        draw.text((140, text_y), t, 0, font)

    name_x = 589 - 160 - name_width(name)
    name_y = text_y + 60
    draw.text((name_x, name_y), '—— ' + name, 0, font)

    bio = io.BytesIO()
    bio.name = 'oncesaid.png'
    bg.save(bio, format='PNG')
    bio.seek(0)

    return bio
