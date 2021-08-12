(ns app.frontend.views
  (:require [re-frame.core :as rf]
            [reagent-material-ui.components :as mui]
            [app.frontend.subs :as subs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn input-fields []
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
    [mui/text-field {:type "time"}]]])

(defn table-body
  []
  (let [table (rf/subscribe [::subs/table])]
    [mui/table-body
     (for [row @table]
       [mui/table-row {:key (random-uuid)}
        (for [cell row]
          [mui/table-cell {:key (random-uuid)} cell])])]))

(defn table
  []
  [mui/table-container
   [mui/table
    [mui/table-head
     [input-fields]]
    [mui/table-head
     [mui/table-row
      [mui/table-cell "Payable to:"]
      [mui/table-cell "Amount:"]
      [mui/table-cell "Date:"]]]
    [table-body]]])

(defn app
  []
  [:<>
   [mui/css-baseline]
   [mui/container
    [:h1 "Accounting App Demo"]
    [mui/paper {:elevation 3}
     [mui/card [mui/card-header {:title "Accounts Payable:"}]]
     [table]]]])
