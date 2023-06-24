(ns battlesnake-harness.http
  (:require [clojure.spec.alpha :as s]
            [cheshire.core :as json]
            [clj-http.client :as http]))

(s/def ::apiversion string?)
(s/def ::author string?)
(s/def ::head string?)
(s/def ::tail string?)
(s/def ::version string?)
(s/def ::shout string?)
(s/def ::squad string?)
(s/def ::name string?)
(s/def ::x int?)
(s/def ::y int?)
(s/def ::id string?)
(s/def ::turn int?)
(s/def ::latency string?)
(s/def ::length int?)
(s/def ::health int?)
(s/def ::head string?)
(s/def ::map string?)
(s/def ::timeout int?)
(s/def ::source string?)
(s/def ::height int?)
(s/def ::width int?)
(s/def ::move #{"up" "down" "left" "right"})

(s/def ::position
  (s/keys :req-un [::x
                   ::y]))

(s/def :snake/head ::position)
(s/def ::body (s/coll-of ::position))
(s/def ::food (s/coll-of ::position))
(s/def ::hazards (s/coll-of ::position))

(s/def ::color
  (s/and string?
         #(= 7 (count %))
         #(= \# (first %))))

(s/def :customizations/head string?)
(s/def :customizations/tail string?)

(s/def ::customizations
  (s/keys :opt [::color
                :customizations/head
                :customizations/tail]))

(s/def ::ruleset
  (s/keys :opt-un [::name
                   ::version]))

(s/def ::game
  (s/keys :req-un [::id
                   ::ruleset
                   ::map
                   ::timeout
                   ::source]))

(s/def ::battlesnake-object
  (s/keys :req-un [::shout
                   ::squad
                   ::name
                   :snake/head
                   ::id
                   ::customizations
                   ::latency
                   ::length
                   ::health
                   ::body]))

(s/def ::snakes (s/coll-of ::battlesnake-object))

(s/def ::board
  (s/keys :req-un [::height
                   ::width
                   ::food
                   ::hazards
                   ::snakes]))

(s/def ::you ::battlesnake-object)

(s/def :battlesnake/start-request
  (s/keys :req-un [::game
                   ::turn
                   ::board
                   ::you]))

(s/def :battlesnake/move-request
  (s/keys :req-un [::game
                   ::turn
                   ::board
                   ::you]))

(s/def :battlesnake/end-request
  (s/keys :req-un [::game
                   ::turn
                   ::board
                   ::you]))

(s/def :battlesnake/move-response
  (s/keys :req-un [::move
                   ::shout]))

(s/def :battlesnake/get-response
  (s/keys :req-un [::apiversion
                   ::author
                   ::color
                   ::head
                   ::tail
                   ::version]))

;; example data

(def example-snake {:shout "why are we shouting??"
                    :squad "1"
                    :name "Sneky McSnek Face"
                    :head {:x 0 :y 0}
                    :id "totally-unique-snake-id"
                    :customizations {:color "#26CF04" :head "smile" :tail "bolt"}
                    :latency "123"
                    :length 3
                    :health 54
                    :body [{:x 0 :y 0} {:x 1 :y 0} {:x 2 :y 0}]})

(def example-game {:id "totally-unique-game-id"
                   :ruleset {:name "standard" :version "v1.2.3"}
                   :map "standard"
                   :timeout 500
                   :source "league"})

(def example-board {:height 11
                    :width 11
                    :food [{:x 5 :y 5} {:x 9 :y 0} {:x 2 :y 6}]
                    :hazards [{:x 6 :y 6}]
                    :snakes [example-snake
                             (assoc example-snake
                                    :id "2"
                                    :body [{:x 2 :y 2} {:x 3 :y 2} {:x 4 :y 2}])
                             (assoc example-snake
                                    :id "3"
                                    :body [{:x 4 :y 4} {:x 5 :y 4} {:x 6 :y 4}])]})

;; example requests

(def example-start {:you example-snake
                    :game example-game
                    :board example-board
                    :turn 2})
(def example-start-json (json/generate-string example-start))

(def example-move {:you example-snake
                   :game example-game
                   :board example-board
                   :turn 2})
(def example-move-json (json/generate-string example-move))

(def example-end {:you example-snake
                  :game example-game
                  :board example-board
                  :turn 2})
(def example-end-json (json/generate-string example-end))

;; example responses

(def example-get-response {:apiversion "1"
                           :author "MyUsername"
                           :color "#888888"
                           :head "default"
                           :tail "default"
                           :version "0.0.1-beta"})

(def example-move-response {:move "up"
                            :shout "Moving up!"})

(defn send-get
  [{:keys [host port]}]
  (http/get (str "http://" host ":" port)))

(defn send-move
  [{:keys [host port]} move-request]
  (http/post (str "http://" host ":" port "/move")
             {:content-type :json
              :body (json/generate-string move-request)}))



(comment


  [(s/valid? :battlesnake/start-request example-start)
   (s/valid? :battlesnake/move-request example-move)
   (s/valid? :battlesnake/end-request example-end)

   (s/valid? :battlesnake/get-response example-get-response)
   (s/valid? :battlesnake/move-response example-move-response)]


  ,)
