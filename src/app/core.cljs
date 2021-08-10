(ns app.core
  (:require [reagent-material-ui.components :as mui]
            [reagent.core :as r]
            [reagent.dom :as r.dom]))

(defonce dummy-data (r/atom [["fuckass" "15" "12/18/1969"]
                             ["anotherfucker" "350" "1/1/-1000"]
                             ["Acme Co. Ltd." "123599" "10/20/2021"]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn table
  [data]
  (letfn [(table-body [data]
            [mui/table-body
             (for [row data]
               [mui/table-row {:key (random-uuid)}
                (for [cell row]
                  [mui/table-cell {:key (random-uuid)} cell])])])]
    [mui/table-container
     [mui/table
      [mui/table-head
       [mui/table-row
        [mui/table-cell
         [mui/text-field {:type        "string"
                          :required    true
                          :placeholder "Payable to..."}]]
        [mui/table-cell
         [mui/text-field {:type        "number"
                          :required    true
                          :step        1
                          :placeholder "Amount..."}]]
        [mui/table-cell
         [mui/text-field {:type     "date"
                          :required true}]
         [mui/text-field {:type "time"}]]]]
      [mui/table-head
       [mui/table-row
        [mui/table-cell "Payable to:"]
        [mui/table-cell "Amount:"]
        [mui/table-cell "Date:"]]]
      [table-body data]]]))

(defn app
  []
  [:<>
   [mui/css-baseline]
   [mui/container
    [:h1 "Accounting App Demo"]
    [mui/paper {:elevation 3}
     [mui/card [mui/card-header {:title "Accounts Payable:"}]]
     [table @dummy-data]]]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Render
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn mount-root
  [component]
  (r.dom/render component (.getElementById js/document "app")))

(defn ^:dev/after-load start
  []
  (.log js/console "start")
  (mount-root [app]))

(defn ^:export init
  []
  (.log js/console "init")
  (start))

(defn ^:dev/before-load stop
  []
  (.log js/console "stop"))
