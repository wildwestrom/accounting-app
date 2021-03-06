(ns app.frontend.events
  (:require [app.frontend.db :as db]
            [re-frame.core :as re-frame :refer [reg-event-db]]))

(reg-event-db
 ::initialise-db
 (fn [_ _]
            db/default-db))

(reg-event-db
 ::add-transaction
 (fn [db [_ tx]]
   (update db :all-data #(conj % tx))))
