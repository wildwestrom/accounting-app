(ns app.frontend.events
  (:require [app.frontend.db :as db]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))
