(ns app.frontend.core
  (:require [reagent.dom :as r.dom]
            [re-frame.core :as rf]
            [app.frontend.events :as events]
            [app.frontend.views :as views]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Render
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn mount-root
  [component]
  (r.dom/render component (.getElementById js/document "app")))

(defn ^:dev/after-load start
  []
  (.log js/console "start")
  (mount-root [views/app]))

(defn ^:export init
  []
  (.log js/console "init")
  (rf/dispatch-sync [::events/initialize-db])
  (start))

(defn ^:dev/before-load stop
  []
  (.log js/console "stop"))
