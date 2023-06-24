(ns battlesnake-harness.core
  (:gen-class)
  (:require [quip.core :as qp]
            [battlesnake-harness.common :as common]
            [battlesnake-harness.scenes.menu :as menu]
            [battlesnake-harness.scenes.sim :as sim]
            [battlesnake-harness.scenes.config :as config]))

(defn setup
  "The initial state of the game"
  []
  {:host "localhost"
   :port "8080"})

(defn init-scenes
  "Map of scenes in the game"
  []
  {:menu (menu/init)
   :sim  (sim/init)
   :config (config/init)})

;; Configure the game
(def battlesnake-harness-game
  (qp/game {:title          "battlesnake-harness"
            :size           [800 600]
            :setup          setup
            :init-scenes-fn init-scenes
            :current-scene  :menu}))

(defn -main
  "Run the game"
  [& args]
  (qp/run battlesnake-harness-game))
