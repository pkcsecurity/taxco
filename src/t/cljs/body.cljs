(ns t.cljs.body
  (:require [reagent.core :as r]
            [t.cljs.components :as c]
            [t.cljs.components.button :as btn]
            [t.cljs.components.gauge :as g]
            [t.cljs.components.nav :as nav]
            [t.cljs.components.progress :as p]
            [t.cljs.components.table :as t]
            [t.cljs.xhr :as xhr]))

(def css
  {::body {:height "100%"}})

(defn card [title progress]
  (let [timer-id (atom nil)
        show? (r/atom false)]
    (r/create-class
      {:component-did-mount
       (fn [this]
         (js/setTimeout (c/on show?) 5000))

       :reagent-render
       (fn [title progress]
         [:div {:style {:margin "1rem 0"
                        :display :inline-block
                        :padding "1rem 2rem"
                        :background-color :white
                        :text-align :center
                        :border-width "1px"
                        :border-style "solid"
                        :border-color (c/pkc-colors :light-gray)}}
          [:h5 {:style {:margin "1rem 0 2rem"}} title]
          (if @show?
            [g/gauge
             progress
             {:height-px 150
              :stroke-width-px 20
              :font-size (c/text-sizes :h3)
              :color (c/pkc-colors (cond
                                     (> progress 0.75) :success
                                     (> progress 0.50) :warning
                                     :else :error))
              :background-color (c/pkc-colors :light-gray)}]
            [:div
             [p/progress 150]])])})))

(defn data-filter [data name-fn ks]
  (reduce
    (fn [acc x]
      (assoc acc
             (name-fn x)
             (select-keys x ks)))
    {}
    data))

(defn device [{[{:keys [hardware_model computer_name] :as si}] :system_info 
               [{:keys [security_type network_name country_code] :as ws}] :wifi_status
               nps :networked_processes
               bps :browser_plugins
               cexs :chrome_extensions
               kexs :kernel_extensions
               :as state}]
  [:div
   {:style {:max-width "900px"
            :width "100%"
            :margin "0 auto"
            :padding-top "3rem"
            :text-align :center}}
   [:div {:style {:display :flex
                  :align-items :center
                  :justify-content :center}}
    [:h3 computer_name]
    [:div {:style {:margin-left "0.5rem"}}
     [c/icon :laptop]]]
   [:h5 "This " hardware_model " is " [:span {:style {:color (c/pkc-colors :success)}} "on"] 
    " and "
    [:span {:style {:color (c/pkc-colors :warning)}} "slightly at risk"]
    "."]
   [:h6 "WiFi: " network_name " (" country_code ", " security_type ")"]
   [:div {:style {:margin-top "2rem"
                  :display :flex
                  :justify-content :space-around
                  :align-items :center}}
    [card  "Device Strength" 0.8]
    [card  "Street Smarts" 0.65]
    [card  "Invisibility" 0.3]
    [card  "Overall Security" 0.9]]
   [t/table "System Information" (select-keys si [:hostname 
                                                  :cpu_logical_cores
                                                  :hardware_model
                                                  :cpu_subtype
                                                  :uuid])]
   [t/table "Network Activity" (data-filter
                                 nps
                                 (fn [{:keys [local_address local_port]}] (str local_address ":" local_port))
                                 [:local_address
                                  :local_port
                                  :name
                                  :path
                                  :remote_address
                                  :remote_port])]
   [t/table "Browser Plugins" (data-filter
                                bps
                                (comp str :name)
                                [:description
                                 :path
                                 :name])]
   (when (seq cexs)
     [t/table "Chrome Extensions" 
      (data-filter cexs
                   (comp str :name)
                   [:description
                    :path
                    :name])])
   [t/table "Kernel Extensions" 
    (data-filter kexs
                 (comp str :name)
                 [:name
                  :path
                  :size])]])

  (def state (r/atom nil))

  (defn body []
    (r/create-class
      {:component-will-mount
       (fn []
         (xhr/fetch :t.clj.routes.device/read-device
                    :get
                    nil
                    #(reset! state %)))


       :reagent-render
       (fn []
         [:div {:style (css ::body)}
          [nav/nav 
           "Devices"
           "Risks"
           "Help"]
          (for [[k v] @state]
            ^{:key k} [device v])])}))
