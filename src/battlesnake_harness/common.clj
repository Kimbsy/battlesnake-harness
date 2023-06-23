(ns battlesnake-harness.common
  (:import [javax.swing JOptionPane]))

(def white [230 230 230])
(def grey [57 57 58])
(def dark-green [41 115 115])
(def light-green [133 255 199])

(defn get-input-dialog
  ([message]
   (JOptionPane/showInputDialog nil message))
  ([message current]
   (JOptionPane/showInputDialog nil message current)))
