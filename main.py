import logging
import config
import telegram

from telegram.ext import Updater, MessageHandler, Filters

logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)

updater = Updater(token=config.TOKEN)
dispatcher = updater.dispatcher
bot = telegram.Bot(token=config.TOKEN)

def unsupported(bot, update):
    bot.send_message(chat_id=update.message.chat_id,
                     text='I can only deal with forwarded text message.')

def echo(bot, update):
    bot.send_message(chat_id=update.message.chat_id, text=update.message.text)

dispatcher.add_handler(MessageHandler(Filters.forwarded & Filters.text, echo))
dispatcher.add_handler(MessageHandler(Filters.all, unsupported))

updater.start_polling()
