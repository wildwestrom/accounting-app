(ns app.frontend.core
  (:require [reagent.dom :as r.dom]
            [re-frame.core :as rf]
            [app.frontend.events :as events]
            [app.frontend.views :as views]))

(defn ^{:after-load true, :dev/after-load true} start
  []
  (.log js/console "start")
  (r.dom/render [views/app] (js/document.getElementById "app")))

(defn ^:export init
  []
  (.log js/console "init")
  (rf/dispatch-sync [::events/initialise-db])
  (start))

(defn ^:dev/before-load stop
  []
  (.log js/console "stop"))
