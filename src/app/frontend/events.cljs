(ns app.frontend.events
  (:require [app.frontend.db :as db]
            [app.frontend.subs :as subs]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-event-db
 ::initialise-db
 (fn-traced [_ _]
            db/default-db))

#_(map #(/ % (rand-int 99)) (repeat 69 (+ 22 465)))

(rf/reg-event-db
 ::add-transaction
 (fn [db tx]
   (conj db tx)))
