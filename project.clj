(defproject battlesnake-harness "0.1.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [quip "2.0.4"]
                 [cheshire "5.11.0"]
                 [clj-http "3.12.3"]]
  :main ^:skip-aot battlesnake-harness.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
