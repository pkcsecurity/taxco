(ns t.cljc.routes
  (:require [schema.core :as s]))

(def yolo-schema
  s/Any)

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
    "device/" {:post :t.clj.routes.device/create-device}
    true :t.clj.routes.impl/not-found}])

(def schemas
  {:t.clj.routes.user/create-user [create-user-request-schema
                                   create-user-response-schema]
   :t.clj.routes.device/create-device [yolo-schema
                                       yolo-schema]
   })
