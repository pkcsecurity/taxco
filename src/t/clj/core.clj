(ns t.clj.core
  (:gen-class)
  (:require [immutant.web :as server]
            [t.clj.routes :as r]))

(defn -main [& args]
  (server/run r/app 
              :host "127.0.0.1" 
              :port 8080))
