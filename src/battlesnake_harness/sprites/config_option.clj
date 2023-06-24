(ns battlesnake-harness.sprites.config-option
  (:require [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.sprites.button :as qpbutton]
            [quip.utils :as qpu]
            [battlesnake-harness.common :as common]))

(defn update-config-options
  [state]
  (qpsprite/update-sprites-by-pred
   state
   (qpsprite/group-pred :config-option)
   (fn [{:keys [value-key description] :as co}]
     (let [new (value-key state)]
       (-> co
           (assoc :current-value new)
           (assoc-in [:text-child :content] (str description " : " new)))))))

(defn set-value
  [{:keys [current-scene] :as state} id]
  (let [{:keys [value-key
                edit-message]
         :as co}
        (->> (get-in state [:scenes current-scene :sprites])
             (filter #(= id (:id %)))
             first)]
    (if-let [new (common/get-input-dialog edit-message (value-key state))]
      (-> state
          (assoc value-key new)
          update-config-options)
      state)))

(defn draw-config-option
  [{:keys [text-child button-child] :as o}]
  (qpsprite/draw-text-sprite text-child)
  (qpbutton/draw-button-sprite button-child))

(defn config-option
  [id pos description value-key initial-value edit-message]
  {:sprite-group :config-option
   :id id
   :pos pos
   :description description
   :value-key value-key
   :current-value initial-value
   :edit-message edit-message
   :update-fn identity
   :draw-fn draw-config-option
   :text-child (qpsprite/text-sprite
                (str description " : " initial-value)
                pos
                :offsets [:right :center])
   :button-child (qpbutton/button-sprite
                  "change"
                  (map + pos [100 0])
                  :size [100 50]
                  :font-size qpu/default-text-size
                  :content-pos [50 25]
                  :color common/grey
                  :content-color common/white
                  :on-click (fn [state _e]
                              (set-value state id)))})
