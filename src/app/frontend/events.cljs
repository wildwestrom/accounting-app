(ns app.frontend.events
  (:require [app.frontend.db :as db]
            [re-frame.core :as re-frame :refer [reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(reg-event-db
 ::initialise-db
 (fn-traced [_ _]
            db/default-db))

(reg-event-db
 ::add-transaction
 (fn [db [_ tx]]
   (conj db tx)))
