(ns battlesnake-harness.scenes.config
  (:require [clojure.spec.alpha :as s]
            [cheshire.core :as json]
            [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.sprites.button :as qpbutton]
            [quip.scene :as qpscene]
            [quip.utils :as qpu]
            [battlesnake-harness.common :as common]
            [battlesnake-harness.sprites.config-option :as co]))

(defn on-click-menu
  [state e]
  (qpscene/transition state :menu :transition-length 30))

(defn on-click-test
  [{:keys [host port] :as state} e]
  (let [resp (json/parse-string (slurp (str "http://" host ":" port)) keyword)
        valid? (s/valid? :battlesnake/get-response resp)]
    (if valid?
      (do
        (newline)
        (prn "Valid response!")
        (newline)
        (clojure.pprint/pprint resp)
        (newline))
      (do
        (newline)
        (prn "Invalid response:")
        (newline)
        (clojure.pprint/pprint resp)
        (newline)
        (prn (s/explain :battlesnake/get-response resp)))))
  state)

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(co/config-option :host
                     [(* (q/width) 0.6) 200]
                     "snake ip"
                     :host
                     "localhost"
                     "Enter new snake hostname or IP address:")
   (co/config-option :port
                     [(* (q/width) 0.6) 300]
                     "snake port"
                     :port
                     "8080"
                     "Enter new snake port number:")
   (qpbutton/button-sprite "Menu"
                           [(* 0.35 (q/width))
                            (* 0.8 (q/height))]
                           :color common/grey
                           :content-color common/white
                           :on-click on-click-menu)
   (qpbutton/button-sprite "Test"
                           [(* 0.65 (q/width))
                            (* 0.8 (q/height))]
                           :color common/grey
                           :content-color common/white
                           :on-click on-click-test)])

(defn draw-config
  "Called each frame, draws the current scene to the screen"
  [state]
  (qpu/background common/dark-green)
  (qpsprite/draw-scene-sprites state))

(defn update-config
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      qpsprite/update-scene-sprites))

(defn classic-rf
  [{ex :x ey :y :as e}]
  (fn [acc-state {:keys [collision-detection-fn
                         on-click]
                  :as   b}]
    (if (collision-detection-fn {:pos [ex ey]} b)
      (-> acc-state
          (on-click e)
          (qpbutton/update-held b))
      acc-state)))

(defn child-rf
  [{ex :x ey :y :as e}]
  (fn foobar [acc-state {:keys [button-child] :as co}]
    (let [{:keys [collision-detection-fn
                  on-click]
           :as   b} button-child]
      (if (collision-detection-fn {:pos [ex ey]} b)
        (-> acc-state
            (on-click e))
        acc-state))))

(defn handle-mouse-pressed
  [{:keys [current-scene] :as state} e]
  (let [sprites (get-in state [:scenes current-scene :sprites])
        buttons (filter #(#{:button :config-option} (:sprite-group %)) sprites)]
    (reduce (fn [acc b]
              (if (:collision-detection-fn b)
                ((classic-rf e) acc b)
                ((child-rf e) acc b)))
            state
            buttons)))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-config
   :update-fn update-config
   :mouse-pressed-fns [handle-mouse-pressed]
   :mouse-released-fns [qpbutton/handle-buttons-released]})
