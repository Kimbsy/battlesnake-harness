(ns battlesnake-harness.scenes.sim
  (:require [quip.sprite :as qpsprite]
            [quip.utils :as qpu]
            [battlesnake-harness.common :as common]))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [])

(defn draw-sim
  "Called each frame, draws the current scene to the screen"
  [state]
  (qpu/background common/light-green)
  (qpsprite/draw-scene-sprites state))

(defn update-sim
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      qpsprite/update-scene-sprites))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-sim
   :update-fn update-sim})
