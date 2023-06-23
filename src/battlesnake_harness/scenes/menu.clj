(ns battlesnake-harness.scenes.menu
  (:require [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.sprites.button :as qpbutton]
            [quip.scene :as qpscene]
            [quip.utils :as qpu]
            [battlesnake-harness.common :as common]))

(defn on-click-play
  [state e]
  (qpscene/transition state :sim :transition-length 30))

(defn on-click-config
  [state e]
  (qpscene/transition state :config :transition-length 30))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(qpbutton/button-sprite "Sim"
                           [(* 0.5 (q/width))
                            (* 0.33 (q/height))]
                           :color common/grey
                           :content-color common/white
                           :on-click on-click-play)
   (qpbutton/button-sprite "Config"
                           [(* 0.5 (q/width))
                            (* 0.67 (q/height))]
                           :color common/grey
                           :content-color common/white
                           :on-click on-click-config)])

(defn draw-menu
  "Called each frame, draws the current scene to the screen"
  [state]
  (qpu/background dark-green)
  (qpsprite/draw-scene-sprites state))

(defn update-menu
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      qpsprite/update-scene-sprites))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-menu
   :update-fn update-menu
   :mouse-pressed-fns [qpbutton/handle-buttons-pressed]
   :mouse-released-fns [qpbutton/handle-buttons-released]})
