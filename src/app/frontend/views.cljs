(ns app.frontend.views
  (:require-macros [reagent-material-ui.util :refer [react-component]])
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [reagent-material-ui.styles :as styles]
            [reagent-material-ui.colors :as colors]
            [reagent-material-ui.core.app-bar :refer [app-bar]]
            [reagent-material-ui.core.button :refer [button]]
            [reagent-material-ui.core.container :refer [container]]
            [reagent-material-ui.core.card :refer [card]]
            [reagent-material-ui.core.card-content :refer [card-content]]
            [reagent-material-ui.core.typography :refer [typography]]
            [reagent-material-ui.core.text-field :refer [text-field]]
            [reagent-material-ui.core.input-adornment :refer [input-adornment]]
            [reagent-material-ui.core.grid :refer [grid]]
            [reagent-material-ui.core.table :refer [table]]
            [reagent-material-ui.core.table-head :refer [table-head]]
            [reagent-material-ui.core.table-body :refer [table-body]]
            [reagent-material-ui.core.table-footer :refer [table-footer]]
            [reagent-material-ui.core.table-row :refer [table-row]]
            [reagent-material-ui.core.table-cell :refer [table-cell]]
            [reagent-material-ui.core.table-container :refer [table-container]]
            [reagent-material-ui.core.css-baseline :refer [css-baseline]]
            [reagent-material-ui.core.styled-engine-provider :refer [styled-engine-provider]]
            [reagent-material-ui.cljs-time-adapter :refer [cljs-time-adapter]]
            [reagent-material-ui.lab.localization-provider :refer [localization-provider]]
            [reagent-material-ui.lab.date-time-picker :refer [date-time-picker]]
            [reagent-material-ui.icons.add-box :refer [add-box]]
            [tick.core :as t]
            [app.frontend.events :as events]
            [app.frontend.subs :as subs])
  (:import (goog.i18n DateTimeSymbols)))

(defn event-value
  [e]
  (.. e -target -value))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Theming
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def custom-theme
  {:palette {:primary colors/blue
             :secondary colors/red}})

(def classes (let [prefix "rmui-example"]
               {:root       (str prefix "-root")
                :button     (str prefix "-button")
                :text-field (str prefix "-text-field")}))

(defn custom-styles [{:keys [theme]}]
  (let [spacing (:spacing theme)]
    {(str "&." (:root classes))        {:margin-left (spacing 8)
                                        :align-items :flex-start}
     (str "& ." (:button classes))     {:margin (spacing 1)}
     (str "& ." (:text-field classes)) {:width        200
                                        :margin-left  (spacing 1)
                                        :margin-right (spacing 1)}}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; State
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def payable-to (r/atom "Company Name"))
(def amount     (r/atom "0"))
(def datetime   (r/atom (js/Date.)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn current-month-expenses
  []
  [card
   [card-content
    [typography {:variant "h6"
                 :component "h3"}
     "Current month expenses: "]
    [typography {:variant "body1"
                 :component "h4"}
     (str @(rf/subscribe [::subs/current-month-total]) "₴")]]])

(defn input-fields
  [{:keys [classes] :as props}]
  [:<>
   [table-row
    [table-cell
     [text-field {:type        "string"
                  :required    true
                  :placeholder "Payable to..."
                  :value       @payable-to
                  :on-change   #(reset! payable-to (event-value %))
                  :label       "Required"}]]
    [table-cell
     [text-field {:type        "number"
                  :label       "Required"
                  :required    true
                  :step        1
                  :placeholder "Amount..."
                  :value       @amount
                  :on-change   #(reset! amount (int (event-value %)))
                  :InputProps  {:end-adornment
                                (r/as-element
                                 [input-adornment {:position "end"} "₴"])}}]]
    [table-cell
     [date-time-picker
      {:value        @datetime
       :ampm         false
       ;; :mask         "____-__-__ __:__ z"
       :disableMaskedInput true
       :input-format "yyyy-mm-dd HH:mm z"
       :variant      "inline"
       :onChange     #(reset! datetime (.. % -date))
       :render-input (react-component [props]
                                      [text-field props])}]]]])

(defn submit-button
  []
  [table-footer
   [table-row
    [table-cell
     [button
      {:variant  "outlined"
       :type     "submit"
       :on-click #(rf/dispatch
                   [::events/add-transaction
                    {:payable-to @payable-to
                     :amount     @amount
                     :date       (t/instant (js/Date. @datetime))}])}
      "Submit"
      [add-box]]]]])

(defn table-data
  []
  [:<>
   (let [table (rf/subscribe [::subs/table])]
     [table-body
      (for [row @table]
        [table-row {:key (random-uuid)}
         (let [{:keys [payable-to amount date]} row]
           [:<>
            [table-cell payable-to]
            [table-cell
             [grid {:container      true
                    :justifyContent "space-between"}
              [:span amount]
              [:span "₴"]]]
            [table-cell (t/format (t/in date (t/zone "Z")))]])])])
   [table-footer
    [input-fields]]])

(defn main-table
  []
  [table-container
   [table
    [table-head
     [table-row
      [table-cell "Payable to:"]
      [table-cell "Amount:"]
      [table-cell "Date:"]]]
    [submit-button]
    [table-data]]])

(defn page*
  []
  [:<>
   [app-bar {:position "static"}
    [typography {:variant "h3"
                 :component "h1"}
     "Accounting App Demo"]]
   [container
    [grid
     [typography {:variant "h4"
                  :component "h2"}
      "Accounts Payable:"]
     [current-month-expenses]]
    [main-table]]])

(def page (styles/styled page* custom-styles))

(defn app
  []
  [:<>
   [css-baseline]
   ;; localization-provider provides date handling utils to date and time pickers.
   ;; cljs-time-adapter is a date adapter that allows you to use cljs-time / goog.date date objects.
   ;; styled-engine-provider is needed to ensure correct order of CSS injection until Material UI has completely migrated to emotion
   [localization-provider {:date-adapter cljs-time-adapter
                           :locale       DateTimeSymbols}
    [styles/theme-provider (styles/create-theme custom-theme)
     [styled-engine-provider {:inject-first true}
      [page]]]]])
