import logging
import telegram
import io

import config
import maker


from telegram.ext import Updater, MessageHandler, Filters

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)

updater = Updater(token=config.TOKEN)
dispatcher = updater.dispatcher
bot = telegram.Bot(token=config.TOKEN)

def unsupported(bot, update):
    bot.send_message(chat_id=update.message.chat_id,
                     text='I can only deal with forwarded text message.')

def xstr(s):
    return '' if s is None else s

def oncesaid(bot, update):
    user = update.message.forward_from
    name = '{} {}'.format(xstr(user.first_name), xstr(user.last_name))
    profile_photos = user.get_profile_photos(limit=1)
    if profile_photos.total_count == 0:
        bot.send_message(chat_id=update.message.chat_id,
                         text='I cant work with user has no profile phone.')
    photo = profile_photos.photos[0][-1]
    photo_file = io.BytesIO()
    bot.getFile(photo.file_id).download(out=photo_file)
    photo_file.seek(0)

    oncesaid_photo = maker.make_said(
        photo_file,
        update.message.text,
        name
    )
    bot.send_photo(chat_id=update.message.chat_id,
                   photo=oncesaid_photo)

dispatcher.add_handler(
    MessageHandler(Filters.forwarded & Filters.text, oncesaid))
dispatcher.add_handler(MessageHandler(Filters.all, unsupported))

updater.start_polling()
