(ns gallifreyan-translator.window
  (:gen-class
   :extends gallifreyan_translator.base.gallifreyan))

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