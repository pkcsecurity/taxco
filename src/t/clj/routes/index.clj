(ns t.clj.routes.index
  (:require [t.clj.views.index :as idx]
            [t.clj.routes.impl :as impl]))

(def index (constantly {:status 200
                        :headers {"Content-Type" "text/html"}
                        :body idx/index}))
