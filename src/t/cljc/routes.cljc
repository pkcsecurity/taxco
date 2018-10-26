(ns t.cljc.routes
  (:require [schema.core :as s]))

(def create-user-request-schema
  {:name s/Str})

(def create-user-response-schema
  {:id s/Int
   :name s/Str
   :claims-foo [s/Str]})

(def routes
  ["/"
   {"" {:get :t.clj.routes.index/index}
    "user/" {:post :t.clj.routes.user/create-user}
    true :t.clj.routes.impl/not-found}])

(def schemas
  {:t.clj.routes.user/create-user [create-user-request-schema
                                         create-user-response-schema]})
