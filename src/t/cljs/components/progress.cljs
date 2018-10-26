(ns t.cljs.components.progress
  (:require [reagent.core :as r]
            [t.cljs.components :as c]))

(def css 
  {::progress {:position :absolute
               :top 0
               :left 0}})

(defn progress-item [height src]
  [:img.animated.fadeIn {:style (assoc (css ::progress) :height height)
                         :src src}])

(defn progress [height]
  (let [timer-id (atom nil)
        bar-count (r/atom 0)]
    (r/create-class
      {:component-did-mount
       (fn []
         (reset! timer-id(js/setInterval #(swap! bar-count inc) 600)))

       :component-will-unmount
       (fn []
         (js/clearInterval @timer-id))

       :reagent-render
       (fn []
         (let [mcount (mod @bar-count 5)]
           [:div {:style {:position :relative
                          :height height
                          :width height}
                  :class (when (= 4 mcount) "animated fadeOut")}
            (when (<= 1 mcount)
              [progress-item height "bar1.png"])
            (when (<= 2 mcount)
              [progress-item height "bar2.png"])
            (when (<= 3 mcount)
              [progress-item height "bar3.png"])]))})))

