(ns app.frontend.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]
            [tick.core :as t]))

(reg-sub
 ::table
 (fn [db _]
   (sort-by :date (:all-data db))))

(reg-sub
 ::current-month-total
 (fn [db _]
   (let [now                (t/instant)
         current-month      (t/int (t/month now))
         current-year       (t/int (t/year now))
         beginning-of-month (-> (t/new-date current-year current-month 1)
                                (t/at (t/midnight))
                                (t/in (t/zone "Z"))
                                (t/instant))
         end-of-month       (-> (t/new-date current-year (+ 1 current-month) 1)
                                (t/at (t/midnight))
                                (t/in (t/zone "Z"))
                                (t/<< (t/new-duration 1 :seconds))
                                (t/instant))]
     (apply + (map :amount
                   (filter #(t/<= beginning-of-month (:date %) end-of-month)
                           (:all-data db)))))))
