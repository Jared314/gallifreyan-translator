(ns gallifreyan-translator.window
  (:import [processing.core PApplet])
  (:gen-class
   :extends gallifreyan_translator.base.gallifreyan
   :exposes-methods {keyPressed parentKeyPressed}))

(def defaultEnglish "Enter text here and press return.")

(defn -setup [this]
  (let [fg (.getFg this)
        bg (.getBg this)
        english (.getEnglish this)
        font (.loadFont this "Futura-Medium-15.vlw")]
    (doto this
      (.smooth)
      (.size 1024 600)
      (.background bg)
      (.fill fg)
      (.textFont font)
      (.text english (float 15) (float 30))
      (.stroke fg)
      (.strokeWeight 1)
      (.noFill)
      (.frameRate 30))))

(defn -draw [this]
  (if (and (.getKeyPressed this)
           (= PApplet/CONTROL (.getKeyCode this)))
    (let [english (.getEnglish this)
          c (.getCount this)
          bg (.getBg this)
          fg (.getFg this)]
      (.transliterate this english fg bg (.getSentenceRadius this))
      (.text this english (float 15) (float 30))
      (.setCount this (+ c (float 0.02))))))

(defn -keyPressed
  ([this keyevent] (.parentKeyPressed this keyevent))
  ([this]
   (let [keycode (.getKeyCode this)
         newkey (.getKey this)
         e (.getEnglish this)
         fg (.getFg this)
         bg (.getBg this)]
     (cond
      (= PApplet/SHIFT keycode) nil
      (= PApplet/CONTROL keycode) nil

      (= (int PApplet/TAB) keycode)
      (doto this
        (.stroke bg)
        (.strokeWeight 400)
        (.ellipse (/ (.getWidth this) 2)
                  (/ (.getHeight this) 2)
                  (* (+ (.getSentenceRadius this) 222) 2)
                  (* (+ (.getSentenceRadius this) 222) 2))
        (.saveFrame (str e " ####.png"))
        (.text "Your image has been saved to the" (float 15) (float 30))
        (.text "folder that contains this program." (float 15) (float 50)))

      (= PApplet/ALT keycode)
      (doto this
        (.setBg (.color this (.random this 255) (.random this 255) (.random this 255)))
        (.setFg (.color this (.random this 255) (.random this 255) (.random this 255)))
        (.transliterate e (.getFg this) (.getBg this) (.getSentenceRadius this))
        (.text e (float 15) (float 30)))

      (or (= (int PApplet/DELETE) keycode)
          (= (int PApplet/BACKSPACE) keycode))
      (doto this
        (.setEnglish (subs e 0 (dec (count e))))
        (.background (.getBg this))
        (.text (.getEnglish this) (float 15) (float 30)))

      (or (= (int PApplet/RETURN) keycode)
          (= (int PApplet/ENTER) keycode))
      (doto this
        (.transliterate e fg bg (.getSentenceRadius this))
        (.text e (float 15) (float 30)))

      :else
      (let [prefix (if (= defaultEnglish e) "" e)
            newe (str prefix newkey)]
        (doto this
          (.setEnglish newe)
          (.background bg)
          (.text newe (float 15) (float 30))))))))
