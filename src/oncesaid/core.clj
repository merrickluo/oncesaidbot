(ns oncesaid.core
  (:require [morse.handlers :as h]
            [morse.api :as t]
            [morse.polling :as p]
            [oncesaid.image :as image])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def token (System/getenv "ONCESAID_TG_TOKEN"))

(defn get-message-avatar
  "get avatar from message, forwarded first"
  [msg]
  (let [user-id (:id (or (:forward_from msg) (:from msg)))
        photos (t/get-profile-photos token user-id)
        pf-id (:file_id (first (first (:photos photos))))
        file (t/get-file token pf-id)]
    (println photos)
    (t/download-file token (:file_path file))))

(h/defhandler handler
  (h/command "start" {{id :id username :username} :chat}
             (println username "starting use bto")
             (t/send-text token id "Welcome!"))
  (h/message {{id :id} :chat :as msg}
             (let [avatar (get-message-avatar msg)
                   text (:text msg)
                   username (:username (or (:forward_from msg) (:from msg)))
                   img-data (image/create avatar text username)]
               (println img-data)
               (t/send-photo token id img-data))))

(def channel (p/start token handler))
(p/stop channel)
