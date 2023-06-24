(ns battlesnake-harness.common
  (:require [clojure.spec.alpha :as s]
            [cheshire.core :as json])
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

(s/def ::apiversion string?)
(s/def ::author string?)
(s/def ::color (s/and string?
                      #(= 7 (count %))
                      #(= \# (first %))))
(s/def ::head string?)
(s/def ::tail string?)
(s/def ::version string?)

(s/def :battlesnake/get-request
  (s/keys :req-un [::apiversion
                   ::author
                   ::color
                   ::head
                   ::tail
                   ::version]))

(s/def :battlesnake/start-request
  (s/keys :req-un []))

(s/def :battlesnake/move-request
  (s/keys :req-un []))

(s/def :battlesnake/end-request
  (s/keys :req-un []))






(def example-get {:apiversion "1",
                  :author "MyUsername",
                  :color "#888888",
                  :head "default",
                  :tail "default",
                  :version "0.0.1-beta"})
(def example-get-json (json/generate-string example-get))

;; @TODO:
;; - implement specs for other requests
;; - create generators for requests
;; - move this into an http ns?

(def example-start {})
(def example-start-json (json/generate-string example-start))

(def example-move {})
(def example-move-json (json/generate-string example-move))

(def example-end {})
(def example-end-json (json/generate-string example-end))

(comment

  (assert
   (and
    (s/valid? :battlesnake/get-request example-get)
    (s/valid? :battlesnake/start-request example-start)
    (s/valid? :battlesnake/move-request example-move)
    (s/valid? :battlesnake/end-request example-end)))

  ,)
