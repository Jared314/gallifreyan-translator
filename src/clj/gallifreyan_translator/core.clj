(ns gallifreyan-translator.core
  (:import [processing.core PApplet])
  (:gen-class))

(defn -main []
  (PApplet/main (into-array String ["--bgcolor=#FFFFFF" "gallifreyan_translator.base.gallifreyan"])))