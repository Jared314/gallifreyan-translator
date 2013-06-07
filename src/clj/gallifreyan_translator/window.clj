(ns gallifreyan-translator.window
  (:import [processing.core PApplet]
           [gallifreyan_translator.base gallifreyan])
  (:gen-class
   :extends processing.core.PApplet
   :exposes-methods {keyPressed parentKeyPressed}
   :init init2
   :post-init postinit
   :state state))

(def defaultEnglish "Enter text here and press return.")
(def defaultState {:text "Enter text here and press return."
                   :bg 0
                   :fg 0
                   :count (float 0)
                   :sentenceRadius 256})

(defn -init2 []
  [[] (ref defaultState)])

(defn -postinit [^gallifreyan_translator.window this]
  (let [bg (.color this 255)
        fg (.color this 0)
        s (.state this)]
    (dosync (alter s assoc :bg bg :fg fg))))

(defn -setup [^gallifreyan_translator.window this]
  (let [{^int fg :fg
         ^int bg :bg
         ^String english :text} @(.state this)
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

(defn -draw [^gallifreyan_translator.window this]
  (if (and (gallifreyan/getKeyPressed this)
           (= PApplet/CONTROL (.keyCode this)))
    (let [s (.state this)
          {^String english :text
           ^float c :count
           bg :bg
           fg :fg
           sr :sentenceRadius} @s]
      (gallifreyan/transliterate this english fg bg sr c)
      (.text this english (float 15) (float 30))
      (dosync (alter s assoc :count (+ c (float 0.02)))))))

(defn -keyPressed
  ([^gallifreyan_translator.window this keyevent] (.parentKeyPressed this keyevent))
  ([^gallifreyan_translator.window this]
   (let [keycode (.keyCode this)
         newkey (.key this)
         s (.state this)
         {^String e :text
          fg :fg
          ^int bg :bg
          sr :sentenceRadius
          ^float c :count} @s]
     (cond
      (= PApplet/SHIFT keycode) nil
      (= PApplet/CONTROL keycode) nil

      (= (int PApplet/TAB) keycode)
      (doto this
        (.stroke bg)
        (.strokeWeight 400)
        (.ellipse (/ (.getWidth this) 2)
                  (/ (.getHeight this) 2)
                  (* (+ sr 222) 2)
                  (* (+ sr 222) 2))
        (.saveFrame (str e " ####.png"))
        (.text "Your image has been saved to the" (float 15) (float 30))
        (.text "folder that contains this program." (float 15) (float 50)))

      (= PApplet/ALT keycode)
      (let [newbg (.color this (.random this 255) (.random this 255) (.random this 255))
            newfg (.color this (.random this 255) (.random this 255) (.random this 255))]
        (gallifreyan/transliterate this e newfg newbg sr c)
        (.text this e (float 15) (float 30))
        (dosync (alter s assoc :bg newbg :fg newfg)))

      (or (= (int PApplet/DELETE) keycode)
          (= (int PApplet/BACKSPACE) keycode))
      (let [newe (if (empty? e) e (subs e 0 (dec (count e))))]
        (.background this bg)
        (.text this newe (float 15) (float 30))
        (dosync (alter s assoc :text newe)))

      (or (= (int PApplet/RETURN) keycode)
          (= (int PApplet/ENTER) keycode))
      (doto this
        (gallifreyan/transliterate e fg bg sr c)
        (.text e (float 15) (float 30)))

      :else
      (let [prefix (if (= defaultEnglish e) "" e)
            newe (str prefix newkey)]
        (doto this
          (.background bg)
          (.text newe (float 15) (float 30))
          (dosync (alter s assoc :text newe))))))))

