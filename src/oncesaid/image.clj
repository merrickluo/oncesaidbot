(ns oncesaid.image
  (:gen-class)
  (:import [java.awt.image BufferedImage BufferedImageOp]
           [java.awt Color Font]
           [java.awt.geom.Ellipse2D]
           [javax.imageio ImageIO]

           [java.io File]))

(def IMAGE_WIDTH 289)
(def IMAGE_HEIGHT 400)
(def AVATAR_SIZE 140)
(def PADDING 60)

(def FONT (let [f (Font/createFont
                   Font/TRUETYPE_FONT
                   (File. "SourceHanSerif-Bold.ttc"))]
            (. f deriveFont 16.0)))

(defn circle-avatar
  "make a circle"
  [img]
  (let*  [w AVATAR_SIZE
          h AVATAR_SIZE
          new-img (BufferedImage. w h BufferedImage/TYPE_INT_ARGB)
          g (. new-img getGraphics)]
    (. g setClip (java.awt.geom.Ellipse2D$Float. 0 0 w h))
    (. g drawImage img 0 0 w h nil)
    new-img))

(defn split-text
  "splite text into lines base on length"
  [text charw]
  (let* [limit (- IMAGE_WIDTH (* PADDING 2))
         count (int (/ limit charw))]
    (map (fn [s] (apply str s)) (partition count count nil text))))

(split-text "A wise man once said, go fuck your self" 10)

(defn create
  "create new image"
  [avatar text name]
  (let* [image (ImageIO/read (File. "background.png"))
         file (File. "image.png")
         a (circle-avatar avatar)
         g (. image getGraphics)]

    ;; draw a
    (. g drawImage a (- (/ IMAGE_WIDTH 2) (/ AVATAR_WIDTH 2)) 70 nil)

    ;; draw text
    (. g setColor Color/BLACK)
    (. g setFont FONT)

    (let [texts (split-text text (. (. g getFontMetrics) stringWidth "太"))]
      (println texts)
      (doseq [[s i] (map vector texts (range))]
        (println s)
        (. g drawString s PADDING (+ 240 (* i 20)))))

    (. g drawString (str "────  " name) 120 320)

    ;; dispose
    (. g dispose)
    (ImageIO/write image "png" file)))
